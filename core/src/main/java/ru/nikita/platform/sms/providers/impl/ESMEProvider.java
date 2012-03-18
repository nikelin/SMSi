package ru.nikita.platform.sms.providers.impl;

/**
 * <p>Title: SMS Kernel</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Nikita Interactive</p>
 * @author  Egor Komarov, Dmitry Vasiyarov
 * @version 1.0
 */
import com.logica.smpp.util.ByteBuffer;

import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

import com.logica.smpp.*;
import com.logica.smpp.pdu.*;
import com.logica.smpp.util.NotEnoughDataInByteBufferException;
import com.redshape.utils.Commons;
import com.redshape.utils.Constants;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.providers.adapters.AdapterAttribute;
import ru.nikita.platform.sms.messages.*;
import ru.nikita.platform.sms.providers.AbstractProvider;
import ru.nikita.platform.sms.providers.IProvider;
import ru.nikita.platform.sms.providers.IProviderModel;
import ru.nikita.platform.sms.providers.ProviderEvent;
import ru.nikita.platform.sms.utils.*;

public class ESMEProvider extends AbstractProvider implements IProvider {
    private static final Logger logger = Logger.getLogger(ESMEProvider.class);
    private static final String SERVICE_NAME                          = "NIKITA";

    private static final boolean DEFAULT_ASYNC_MODE = false;
    private static final long TIMEOUT = Constants.TIME_SECOND;
    private static final long TIME_TO_RECEIVE = 2 * Constants.TIME_MINUTE;
    private static final long QUEUE_WAIT_TIMEOUT = 10 * Constants.TIME_SECOND;
    private static final int RESPONCE_NULL_BARIER = 500;
    private static final int DEFAULT_EXPENSIVE = 0;

    private static int MAX_INCOMING_MESSAGE_COUNT        = 500;
    private static long ENQUIRE                                 = TimeUnit.MILLISECONDS.toNanos( 10 * Constants.TIME_MINUTE );
    private static long RECEIVE_TIMEOUT                         = Constants.TIME_MINUTE;
    private static long ENQ_RECEIVE_TIMEOUT                     = Constants.TIME_MINUTE;
    private static int MAX_SUBMIT_ERROR_CNT                     = 20; //100
    private static byte TON = 0x1;
    private static byte NPI = 0x1;

    private Map<String, Object> expForShortNumber = new HashMap<String, Object>();
    private Map<String, Object> shortNumberForName = new HashMap<String, Object>();
    private Map<String, Object> nameForShortNumber = new HashMap<String, Object>();

    private String cpName = "cp1251";
    private ExecutorService executor;

    /**
     * Connection related objects
     */
    private Session smsSession;
    private Session smsSessionSend;
    private	Connection conn;
    private Connection conn1;
    private Socket sock;
    private Socket sock1;

    private boolean showHEX = true;
    private int submit_error_cnt                         = 0;
    private long lastRequest = 0;
    private int count = 0;

    private int responce_nullCounter = 0;
    private long lastReceiveTime  = 0;

    private boolean mayRecv = true;


    public ESMEProvider( IProviderModel model, IConfig config ) throws ConfigException {
        super(model, config);

        this.init();
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    protected void init() throws ConfigException {
        this.executor = this.createThreadsExecutor();

        logger.info("Creating provider...");
//                SERVICE_NUMBER = cnf.get("sms.service.number");
        TON = Byte.parseByte(this.getConfig().get("sms.ton").value());
        NPI = Byte.parseByte(this.getConfig().get("sms.npi").value());

        this.initShortNumbers();

        logger.info("Provider bind mode = " + this.getConfig().get("sms.bind.mode").value() );
        try {
            ENQUIRE = Integer.parseInt( this.getConfig().get("core.enquire_link_time").value() )*1000;
            logger.info("ENQUIRE_LINK time = " + ENQUIRE);
        } catch (Exception e) {
            logger.warn("Couldn't read ENQUIRE LINK time from config. Taking defaults.");
        }

        RECEIVE_TIMEOUT = this.<Integer>readParameter("core.receive_timeout", Integer.class ) * 1000;
        ENQ_RECEIVE_TIMEOUT = this.readParameter("core.enq_receive_timeout", Long.class );
    }

    protected void initShortNumbers() throws ConfigException {
        StringTokenizer st = new StringTokenizer( this.getConfig().get("sms.numbers.list").value(), ",");
        while (st.hasMoreTokens()) {
            String shortNumber = st.nextToken().trim();
            try {
                expForShortNumber.put(shortNumber, this.getConfig().get("sms.exp.exp" + shortNumber).value() );
            } catch (Throwable e) {
                logger.warn("Couldn't read 'exp." + shortNumber + "' from config. Default value will be used.");
            }

            String servName = this.getServiceName();
            nameForShortNumber.put(shortNumber, servName + shortNumber + "_");
            shortNumberForName.put(servName + shortNumber + "_", shortNumber);
        }
    }
    
    @Override
    public boolean isAsync() {
        try {
            return Boolean.valueOf( this.getConfig().get("core.async").value() );
        } catch ( ConfigException e ) {
            return DEFAULT_ASYNC_MODE;
        }
    }

    @Override
    public void bind() throws IOException, ConfigException {
        sock = this.createSocket();

        conn = new TCPIPConnection(sock);
        if ( this.isDebug() ) {
            conn.getDebug().activate();
        } else {
            conn.getDebug().deactivate();
        }

        smsSession = new Session(conn);
        if ( this.isDebug() ) {
            smsSession.getDebug().activate();
        } else {
            smsSession.getDebug().deactivate();
        }

        smsSession.enableStateChecking();

        conn.setCommsTimeout(1000);
        conn.setReceiveTimeout(1000);

        if ( !this.isTransciever() ) {
            sock1 = this.createSocket();
            conn1 = new TCPIPConnection(sock1);
            smsSessionSend = new Session(conn1);
            if ( !this.isDebug() ) {
                smsSessionSend.getReceiver().getDebug().deactivate();
                smsSessionSend.getDebug().deactivate();
            } else {
                smsSessionSend.getReceiver().getDebug().activate();
                smsSessionSend.getDebug().activate();
            }

            conn1.setCommsTimeout(1000);
            conn1.setReceiveTimeout(1000);

            this.bindReceiver();
        }
        else {
            this.bindTransciever();
        }

        this.lastRequest = System.currentTimeMillis();
        this.markBinded();

        // for ( int i = 0; i < this.getThreadsCount(); i++ ) {
            this.receive();
        // }
    }

    protected int getConnectionWait() throws ConfigException {
        return Integer.valueOf( this.getConfig().get("connection.wait").value() );
    }
    
    protected void bindTransciever() throws ConfigException, IOException {
        try {
            boolean isOk = false;
            int attempts = 0;
            while ( !isOk ) {
                BindRequest breq = new BindTransciever();
                breq.setSystemId( Commons.select(
                        this.getModel().getAdapterModel().<String>getAttribute(AdapterAttribute.LOGIN),
                        this.getModel().getAdapterModel().<String>getAttribute(AdapterAttribute.CLIENT_ID)) );
                breq.setPassword( this.getModel().getAdapterModel().<String>getAttribute(AdapterAttribute.PASSWORD) );
                breq.setInterfaceVersion( Byte.parseByte(this.getConfig().get("core.prot.ver").value(),16) );
                breq.setSystemType( this.getConfig().get("core.sys.type").value() );
                
                BindResponse resp = smsSession.bind(breq);
                if ( !resp.isOk() ) {
                    try {
                        Thread.sleep( this.getConnectionWait() * attempts );
                    } catch ( InterruptedException e ) {}
                    continue;
                }
                
                logger.info("Default RECEIVE_TIMEOUT = " + smsSession.getReceiver().getReceiveTimeout());
                logger.info("Default QUEUE_WAIT_TIMEOUT = " + smsSession.getReceiver().getQueueWaitTimeout());
                smsSession.getReceiver().setReceiveTimeout(RECEIVE_TIMEOUT);
                smsSession.getReceiver().setQueueWaitTimeout(QUEUE_WAIT_TIMEOUT);
                logger.info("Set RECEIVE_TIMEOUT = " + RECEIVE_TIMEOUT);
                logger.info("Set QUEUE_WAIT_TIMEOUT = " + QUEUE_WAIT_TIMEOUT);
    
                logger.info("Protocol Version = "+Byte.parseByte( this.getConfig().get("core.prot.ver").value() ,16));
                logger.info("Transciever bind status:" + resp.getCommandStatus());
            }
        } catch ( PDUException e ) {
            throw new IOException( "PDU en-/de- coding related exception", e );
        } catch ( SmppException e ) {
            throw new IOException("SMPP interaction exception", e );
        }
    }

    protected void bindReceiver() throws IOException, ConfigException {
        try {
            BindRequest breq = new BindReceiver();
            breq.setSystemId( this.getModel().getAdapterModel().<String>getAttribute(AdapterAttribute.LOGIN) );
            breq.setPassword( this.getModel().getAdapterModel().<String>getAttribute(AdapterAttribute.PASSWORD) );
            breq.setInterfaceVersion(Byte.parseByte(this.getConfig().get("core.prot.ver").value(), 16));
            breq.setAddressRange(TON, NPI, "");
            //breq.setAddressRange("");
            breq.setSystemType(this.getConfig().get("core.sys.type").value());

            BindRequest breqS = new BindTransmitter();
            breqS.setSystemId( this.getModel().getAdapterModel().<String>getAttribute(AdapterAttribute.LOGIN) );
            breqS.setPassword( this.getModel().getAdapterModel().<String>getAttribute(AdapterAttribute.PASSWORD) );
            breqS.setInterfaceVersion(Byte.parseByte(this.getConfig().get("core.prot.ver").value(),16));
            breqS.setAddressRange(TON, NPI, "");
            //breqS.setAddressRange("");
            breqS.setSystemType(this.getConfig().get("core.sys.type").value());

            Response resp = smsSession.bind(breq);
            Response resp2 = smsSessionSend.bind(breqS);

            logger.info("Default RECEIVE_TIMEOUT = " + smsSessionSend.getReceiver().getReceiveTimeout());
            logger.info("Default QUEUE_WAIT_TIMEOUT = " + smsSession.getReceiver().getQueueWaitTimeout());
            smsSessionSend.getReceiver().setReceiveTimeout(RECEIVE_TIMEOUT);
            smsSession.getReceiver().setQueueWaitTimeout(QUEUE_WAIT_TIMEOUT);
            logger.info("Set RECEIVE_TIMEOUT = " + RECEIVE_TIMEOUT);
            logger.info("Set QUEUE_WAIT_TIMEOUT = " + QUEUE_WAIT_TIMEOUT);

            logger.info("Protocol Version = "+Byte.parseByte( this.getConfig().get("core.prot.ver").value() ,16));
            logger.info("Receiver bind status:" + resp.getCommandStatus());
            logger.info("Transmitter bind status:" + resp2.getCommandStatus());
        } catch ( PDUException e ) {
            throw new IOException( "PDU en-/de- coding related exception", e );
        } catch ( SmppException e ) {
            throw new IOException( "SMPP interaction exception", e );
        }
    }

    @Override
    public synchronized void send(Message message) throws IOException {
        try {
            new SendTask(Commons.list(message)).call();
        } catch ( Exception e ) {
            throw new IOException("Interrupted", e);
        }
    }

    @Override
    public boolean sendAndWait(List<Message> messages) throws IOException {
        try {
            return new SendTask(messages).call();
        } catch ( Exception e ) {
            throw new IOException( "Unknown submission-related exception", e );
        }
    }

    @Override
    public void send(List<Message> messages) throws IOException {
        try {
            new SendTask(messages).call();
        } catch ( Exception e ) {
            logger.error( e.getMessage(), e );
            throw new IOException( e.getMessage(), e );
        }
    }

    @Override
    public void receive() throws IOException, ConfigException {
        if (!this.isMayRecv()) {
            throw new IOException("Write-only provider unable to" +
                    " receive incoming messages");
        }

        this.getExecutor().submit( new ReceiveThread() );
    }

    @Override
    public synchronized boolean sendAndWait(Message message) throws IOException {
        return this.sendAndWait( Commons.list(message) );
    }

    public boolean isMayRecv() {
        return mayRecv;
    }

    protected DataMessage processDataPacket( DataSM dsm ) throws IOException, ConfigException {
        try {
            logger.info("Incorrect message format must be DeliverSM but found DataSM!");
            String logData = "DataSM = " + dsm.debugString();

            if (this.showHEX) {
                logData += " | " + dsm.getData().getHexDump() + " |";
            }

            logger.info(logData);

            return new DataMessage( this.getModel().getId(), dsm.getData().getBuffer() );
        } catch ( ValueNotSetException e ) {
            throw new IOException( e.getMessage(), e );
        }
    }

    protected Message processDeliveryPacket( DeliverSM dsm ) throws IOException, ConfigException {
        try {
            Message result;

//            String logData = "Receive message " + dsm.debugString();
//            try {
//                if ( this.isDebug() ) {
//                    logData += " | " + dsm.getData().getHexDump() + " |";
//                }
//
//                logger.debug(logData);
//            } catch ( ValueNotSetException e ) {
//                logger.error( e.getMessage(), e );
//            }

            String sendText = dsm.getShortMessage();
            if (sendText == null) {
                try {
                    ByteBuffer payLoadBuf = dsm.getMessagePayload();
                    if (payLoadBuf != null) {
                        sendText = String.valueOf( payLoadBuf );
                    }
                } catch (Exception ex) {
                    logger.debug("No message_payload! ");
                }
            }

            if (dsm.getDataCoding() == 4) {
                logger.debug("Trying to convert message with dataCoding=4! Original sendText="+ sendText);

                ByteBuffer smBody = dsm.getBody()
                        .readBytes(dsm.getBody().length() - dsm.getSmLength());

                sendText = new String(smBody.getBuffer(), this.getConfig().get("core.data.encoding").value() );

            }

            String sAddr = dsm.getSourceAddr().getAddress();
            String dAddr = dsm.getDestAddr().getAddress();

//            if (SOURCE_ADDR_TO_DB.contains(dAddr))
//            { // add to DB for Z1 server.
//                logger.info("IN TO DB PHONE ["+sAddr+"] NUMBER ["+dAddr+"] TEXT ["+sendText+"]");
//                con.addSms(sAddr, sendText, dsm.getDataCoding(), SMSC_NAME);
//            } else
//            {
            result = new Message( this.getModel().getId() );

            byte[] smbody = new byte[dsm.getSmLength()];
            byte[] body = dsm.getBody().getBuffer();
            System.arraycopy(body, body.length - dsm.getSmLength(), smbody, 0, dsm.getSmLength());
            result.setProperty("original-body", smbody);

            result.setPhone(sAddr);
            result.setCount(1);

            int exp = this.DEFAULT_EXPENSIVE;
            String expS = (String) expForShortNumber.get(dAddr);
            if ( expS != null ) {
                exp = new Integer(expS).intValue();
            }

            result.setProperty("expensive", exp);
            if ( sendText != null ) {
                result.setContent(sendText, dsm.getDataCoding());
            }

            this.logString(sAddr, dAddr, " ");

            return result;
        } catch ( NotEnoughDataInByteBufferException e ) {
            throw new IOException("ByteBuffer memory exceed", e );
        }
    }

    @Override
    public synchronized IMessage receiveAndWait() throws IOException, ConfigException {
        try {
            Future<IMessage> resultObject = this.getExecutor().submit( new ReceiveTask() );

            return resultObject.get();
        } catch ( ExecutionException e ) {
            throw new IOException("Receive attempt failed", e );
        } catch ( InterruptedException e ) {
            throw new IOException("Receiving thread has been interrupted", e );
        }
    }

    protected boolean isEnquireElapsed() {
        return this.lastRequest == 0 || ( System.currentTimeMillis()  - this.lastRequest) > ENQUIRE;
    }

    protected void enquireSessionLink( Session session ) throws IOException {
        logger.info("Enquiring T...");
        EnquireLink request = new EnquireLink();
        EnquireLinkResp response;
        try {
            session.getReceiver().setReceiveTimeout(ENQ_RECEIVE_TIMEOUT);
            response = session.enquireLink(request);
            if (response != null) {
                logger.info("Enquire Link response " + response.debugString());
            } else {
                logger.warn("Enquire Link response = null");
            }
            session.getReceiver().setReceiveTimeout(RECEIVE_TIMEOUT);
        } catch (Throwable ex) {
            throw new IOException( "Enquire Link Exception. i'm shuting myself down...", ex );
        } finally {
            lastRequest = System.currentTimeMillis();
        }
    }

    protected void enquireLink() throws IOException, ConfigException {
        if ( !this.isEnquireElapsed() ) {
            return;
        }

        if (!this.isTransciever()) {
            this.enquireSessionLink(smsSessionSend);
            this.enquireSessionLink(smsSession);
        } else {
            this.enquireSessionLink(smsSession);
        }
    }

    /**
     * ??
     */
    @Deprecated
    private boolean _receiveWhileSend() throws IOException, ConfigException {
        boolean result = false;
        if (System.currentTimeMillis() - this.lastReceiveTime > this.TIME_TO_RECEIVE)
        {
            logger.debug("Recv to buff while send...");
            int real_MAX_INCOMING_MESSAGE_COUNT = MAX_INCOMING_MESSAGE_COUNT;
            MAX_INCOMING_MESSAGE_COUNT = 2;
            this.receiveAndWait();
            MAX_INCOMING_MESSAGE_COUNT = real_MAX_INCOMING_MESSAGE_COUNT;
            logger.debug("done.");
            result = true;
        }

        return result;
    }

    protected void logString(String fromPh, String toPh, String errorCode) {
        if (fromPh == null)
            fromPh = "null";
        int PHONE_LENGTH = 11;
        String str = "";
        while (fromPh.length() < PHONE_LENGTH)fromPh = fromPh + " ";
        str = str + this.getModel().getName() + "  ";
        str = str + this.getModel().getName().replaceAll("_", "").toUpperCase() + "  ";
        str = str + fromPh + "  " + toPh + " " + errorCode;

        /**
         * Fixme
         */
        logger.info( str );
    }


    protected boolean submitSM(SubmitSM smsg, String logData) throws IOException, ConfigException {
        try {
            this._receiveWhileSend();

            logger.info("OUT "+count+": PHONE ["+smsg.getDestAddr().getAddress()+"] NUMBER ["+smsg.getSourceAddr().getAddress()+"] TEXT ["+logData+"]");

            Response resp;
            if (!this.isTransciever()) {
                resp = smsSessionSend.submit(smsg);
            } else {
                resp = smsSession.submit(smsg);
            }

            if (resp != null ) {
                if ( resp.getCommandStatus() == Data.ESME_ROK ) {
                    logString(smsg.getSourceAddr().getAddress(), smsg.getDestAddr().getAddress(), String.valueOf(resp.getCommandStatus()));
                    responce_nullCounter = 0;
                    submit_error_cnt = 0;

                    return true;
                }

                logString(smsg.getSourceAddr().getAddress(), smsg.getDestAddr().getAddress(), String.valueOf(resp.getCommandStatus()));
                submit_error_cnt++;
                if (submit_error_cnt == MAX_SUBMIT_ERROR_CNT) {
                    responce_nullCounter = 0;
                    submit_error_cnt = 0;
                    return true;
                }

                if (resp.getCommandStatus() == 88 || resp.getCommandStatus() == 26) {
                    logger.warn("Message submission failed. Status=" + resp.getCommandStatus() + "... trying send it again.");
                    return false;
                }

                logger.warn("Message submission succeed! Status=" + resp.getCommandStatus());
            } else {
                logString(smsg.getSourceAddr().getAddress(), smsg.getDestAddr().getAddress(), "null");
                responce_nullCounter++;
                logger.info("Message submission failed? Response = null ("+responce_nullCounter+")");
                if (responce_nullCounter >= RESPONCE_NULL_BARIER) {
                    throw new IOException("SMS submission impossible!");
                }
            }

            return true;
        } catch ( SmppException e ) {
            throw new IOException("SMPP interaction exception", e );
        }
    }

    protected void closeSession( Session session, Connection connection,
                                 Socket socket ) throws IOException {
        try {
            Receiver r = smsSession.getReceiver();
            UnbindResp ur = smsSession.unbind();
            r.stop();
            if (ur != null) {
                logger.info("Unbind smsSession done. Response = "+ur.debugString());
            } else {
                logger.warn("Unbind smsSession done. Response = null");
            }

            if ( conn != null ) {
                conn.close();
            }
            
            if ( sock != null ) {
                sock.close();
            }
        } catch (Throwable ex) {
            throw new IOException("Exception while unbinding smsSession.", ex);
        } finally {
            conn = null;
            sock = null;
            this.markUnbinded();
        }
    }

    @Override
    public void disconnect() throws IOException, ConfigException {
        this.closeSession(smsSession, conn, sock);

        if ( !this.isTransciever() ) {
            this.closeSession(smsSessionSend, conn1, sock1);
        }

        this.markUnbinded();
    }

    protected ByteBuffer processBinaryMessage( SubmitSM smsg, Message message ) {
        smsg.setDataCoding( message.<Byte>getProperty("dcs") );
        if (smsg.getDataCoding() == 4) {
            smsg.setEsmClass((byte)Data.SM_UDH_GSM);
        }

        ByteBuffer cont = new ByteBuffer();
        cont.appendBytes(message.getData());

        return cont;
    }

    protected ByteBuffer processESMEMessage( ContentPreparator cprep, SubmitSM smsg, Message message ) throws UnsupportedEncodingException {
        ByteBuffer result = null;

        smsg.setDataCoding((byte) 0xF5); // EMS messaging (Binary data, 8 bit, class 1)

        if (!cprep.isOTAContent()) {
            smsg.setEsmClass((byte)Data.SM_UDH_GSM);
        }

        for (Iterator i = cprep.getContent().iterator(); i.hasNext(); ) {
            Object content = i.next();
            if (content instanceof String) {
                String contentS = (String)content;
                com.logica.smpp.util.ByteBuffer bb = new com.logica.smpp.util.ByteBuffer(contentS.getBytes(this.cpName));
                result = bb;
            } else if (content instanceof ByteBuffer) {
                ByteBuffer cont = new ByteBuffer();
                ByteBuffer contentB = (ByteBuffer)content;
                cont.appendBuffer(contentB);
                result = cont;
            }
        }

        return result;
    }

    protected List<ByteBuffer> processWapPushMessage( ContentPreparator cprep, SubmitSM smsg, Message message ) {
        boolean ok = false;
        List<ByteBuffer> result = new ArrayList<ByteBuffer>();
        if ( message.getContent().startsWith("http://") && message.<Boolean>getProperty("wap-push")) {
            logger.info("Sending WAPPush message!!");
            smsg.setEsmClass((byte)0x40);
            smsg.setDataCoding((byte)0x04);
            WapPushMessage wpm = new WapPushMessage( this.getModel().getId() );
            wpm.setName("Nikita");
            wpm.setUrl(message.getContent());
            result.add( new ByteBuffer(wpm.getData() ) );
            ok = true;
        } else {
            if ("RUS".equals( message.getLang() )) {
                String oldStr = message.getContent();
                smsg.setDataCoding((byte) 0x8); // UCS 2
                if (oldStr.length() < 70) {
                    for ( String messagePart : ShortMessageUtils.splitByLength( message.getContent(), 70 ) ) {
                        result.add( new ByteBuffer( messagePart.getBytes() ) );
                    }
                    ok = true;
                }

                this.cpName = Data.ENC_UTF16_BE;
            } else {
                String cont = Transliterator.translate(
                        message.getContent());
                if (cont.length() <= 158) {
                    for ( String messagePart : ShortMessageUtils.splitByLength(cont, 158) ) {
                        result.add( new ByteBuffer( messagePart.getBytes() ) );
                    }

                    ok = true;
                }
            }
        }
        if (!ok) {
            smsg.setEsmClass((byte)0x40);
            byte[][] smd = ShortMessageUtils.split(message, true);
            if (smd != null) {
                for (int i = 0; i < smd.length; i++) {
                    result.add(new ByteBuffer(smd[i]));
                }
            }
        }

        return result;
    }

    protected List<ByteBuffer> prepareMessagesList( SubmitSM smsg, Message message ) throws UnsupportedEncodingException {
        List<ByteBuffer> result = new ArrayList<ByteBuffer>();
        ContentPreparator cprep = new ContentPreparator(message, /** @TODO **/ 0);

        if ( message.getMimeType().equals(MessageMimeType.MimeType.BINARY) ) {
                result.add(this.processBinaryMessage(smsg, message));
        } else if ( cprep.isEMSContent() ) {
                result.add( this.processESMEMessage(cprep, smsg, message) );
        } else {
            result.addAll( this.processWapPushMessage(cprep, smsg, message ) );
        }

        return result;
    }

    protected boolean sendSingle( Message msg ) throws IOException, ConfigException {
        try {
            SubmitSM smsg = new SubmitSM();
            Address src = new Address(TON, NPI, msg.getPhone());
            smsg.setSourceAddr(src);
            Address dest = new Address( (byte) 0x1, (byte) 0x1, msg.getPhone()); //!
            smsg.setDestAddr(dest);

            List<ByteBuffer> msgs = this.prepareMessagesList( smsg, msg );

            ByteBuffer smHeader = new ByteBuffer();
            smHeader.appendBytes(smsg.getBody(), smsg.getBody().length() - 1);

            boolean result = false;
            String logData = null;
            for (Iterator i = msgs.iterator(); i.hasNext(); ) {
                Object content = i.next();
                if (content instanceof String) {
                    String contentS = (String)content;
                    logData = "'" + contentS + "' " + msg.getProperty("mime.type");
                    smsg.setShortMessage(contentS, this.cpName);
                } else if (content instanceof ByteBuffer) {
                    /**
                     * @FIXME
                     */
                    ByteBuffer smBody = new ByteBuffer();
                    smBody.appendBuffer(smHeader);
                    ByteBuffer contentB = (ByteBuffer)content;
                    smBody.appendByte((byte)contentB.length());
                    smBody.appendBuffer(contentB);
                    logData = "'" + new String(smBody.getBuffer()) + "' " + msg.getProperty("mime.type");
                    smsg.setBody(smBody);
                }

                /**
                 * @FIXME
                 */
                int attemptsCount = 0;
                do {
                    result = submitSM(smsg, logData);
                } while ( !result && attemptsCount++ < 10 );

                this.raiseEvent( new ProviderEvent( this, ProviderEvent.Type.Message.Sent, msg, result ) );
                // storage.incSmsCount(msg);
            }

            return result;
        } catch ( SmppException e ) {
            throw new IOException("I/O related exception");
        }
    }

    public void setMayRecv(boolean mayRecv) {
        this.mayRecv = mayRecv;
    }

    public class ReceiveThread implements Runnable {

        @Override
        public void run() {
            do {
                ReceiveTask task = new ReceiveTask() ;

                try {
                    task.call();
                } catch ( Throwable e ) {
                    logger.error( e.getMessage(), e );
                }
            } while ( ESMEProvider.this.isBind() );
        }

    }
    
    public class ReceiveTask implements Callable<IMessage> {
        
        @Override
        public IMessage call() throws Exception {

            ESMEProvider.this.enquireLink();
            ESMEProvider.this.lastReceiveTime = System.currentTimeMillis();

            IMessage result = new NullMessage( ESMEProvider.this.getModel().getId() );

            PDU pdu = null;
            synchronized (smsSession) {
                pdu = smsSession.receive(TIMEOUT);
            }

            if (pdu == null) {
                return result;
            }

            // ESMEProvider.this.raiseEvent( new ProviderEvent(ESMEProvider.this, ProviderEvent.Type.Connection.SMPP.ReceivedPDU, pdu ));

            logger.debug("STARTING RECV PROC...");

            if (pdu instanceof DeliverSM) {
                result = ESMEProvider.this.processDeliveryPacket( (DeliverSM) pdu );
            } else if (pdu instanceof DataSM) {
                result = ESMEProvider.this.processDataPacket((DataSM) pdu);
            } else {
                logger.warn("unknown pdu: " + pdu.debugString());
            }

            if (pdu.isRequest()) {
                Response response = ((Request)pdu).getResponse();
                // respond with default response
                logger.info("Going to send default response to request " + response.debugString());
                synchronized (smsSession) {
                    smsSession.respond(response);
                }
            }

            ESMEProvider.this.raiseEvent( new ProviderEvent(ESMEProvider.this, ProviderEvent.Type.Message.Received, result) );

            return result;
        }
    }

    public class SendTask implements Callable<Boolean> {
        private String cpName;
        private List<Message> messages;

        public SendTask( Message message ) {
            this(Commons.list(message) );
        }

        public SendTask( List<Message> messages ) {
            this.messages = messages;
        }

        public List<Message> getMessages() {
            return messages;
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                enquireLink();

                for ( Message message : this.messages ) {
                    if ( !ESMEProvider.this.sendSingle(message) ) {
                        return false;
                    }
                }

                return true;
            } catch ( IOException e ) {
                throw new Exception( e.getMessage(), e );
            }
        }


    }

    @Override
    public String toString() {
        return this.getModel().getName() + "@" + this.getModel().getAdapterModel().getURL().toString();
    }
}
