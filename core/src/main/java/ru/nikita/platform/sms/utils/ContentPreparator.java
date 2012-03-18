package ru.nikita.platform.sms.utils;

/**
 * <p>Title: Nikita SMS Common</p>
 * <p>Description: Common tools for SMS services</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Nikita Interactive</p>
 * @author Dmitry Vasiyarov
 * @version 1.0
 */
import java.util.*;
import com.logica.smpp.util.ByteBuffer;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.messages.Message;
import ru.nikita.platform.sms.messages.MessageMimeType;

public class ContentPreparator {

    private static String MIME_TYPE_PROP_NAME  = "mime.type";
    private static String DEFAULT_CHARSET  = "Cp1251";

    private static short SM_PORT_RING  = 0x1581;
    private static short SM_PORT_LOGO  = 0x1582;
    private static short SM_PORT_PIC   = 0x158A;

    private static int SMS_MAX_SIZE_SM = 140;
    private static int SMS_MAX_SIZE_OTA = 140;
    private static int SMS_MAX_SIZE_EMS = 158;

    private IMessage sms;
    private MessageMimeType.MimeType mimeType;
    //private HashMap providers;
    private int provId;
    private static int PROV_ID_BEE   = 0x0052F099;
    private static int PROV_ID_MTS   = 0x0052F010;
    private static int PROV_ID_SONIC = 0x0052F020;
    private static int PROV_ID_KUBGSM = 0x0052F031;

    public ContentPreparator(IMessage sms, int pid) {
        this.sms = sms;
        this.mimeType = sms.getMimeType() == null ? MessageMimeType.MimeType.DEFAULT : sms.getMimeType();
        this.provId = pid;
    }

    public byte nameGenerator() {
        boolean isOk = false;
        byte ret = 0;
        int cnt = 0;
        while (!isOk && cnt++ < 100) {
            byte testByte = (byte)(Math.random()*128);
            if ((testByte > 0x40 && testByte < 0x5B) || (testByte > 0x60 && testByte < 0x7B)) {
                isOk = true;
                ret = testByte;
            }
        }
        return ret;
    }

    public boolean isEMSContent() {
        return (mimeType.equals(MessageMimeType.MimeType.MIME_TYPE_SMS));
    }
    public boolean isOTAContent () {
        if (mimeType.equals(MessageMimeType.MimeType.MIME_TYPE_OTA_PIC)
                || mimeType.equals(MessageMimeType.MimeType.MIME_TYPE_OTA_RING))
            return true;
        return false;
    }

    private ByteBuffer getContentObject(byte[] buff) {
        ByteBuffer content = new ByteBuffer();
        content.appendBytes(buff);
        return content;
    }

    protected ByteBuffer onSMRing( byte[] contentVector ) {
        int headerBytes = 7;
        int contentSize = contentVector.length;
        int dataSize = this.SMS_MAX_SIZE_SM - headerBytes;
        int allSmsCount = (int)(contentSize / dataSize);
        if (contentSize > allSmsCount*dataSize) allSmsCount++;
        if (allSmsCount > 1) {
            headerBytes = 12;
            byte smsNumber = (byte)(Math.random()*255);
//                                int smsNumber = (int)(Math.random()*16777215);
            dataSize = this.SMS_MAX_SIZE_SM - headerBytes;
            allSmsCount = (int)(contentSize/dataSize);
            if (contentSize > allSmsCount*dataSize) allSmsCount++;
            byte[] temp = new byte[this.SMS_MAX_SIZE_SM];
            temp[0] = 0x0B;
            temp[1] = 0x05;
            temp[2] = 0x04;
            temp[3] = (byte)(this.SM_PORT_RING >> 8);
            temp[4] = (byte)(this.SM_PORT_RING);
            temp[5] = 0x00;
            temp[6] = 0x00;
            temp[7] = 0x00;
            temp[8] = 0x03;
            temp[9] = smsNumber;
//                                temp[7] = (byte)(smsNumber >> 16);
//                                temp[8] = (byte)(smsNumber >> 8);
//                                temp[9] = (byte)(smsNumber);
            temp[10] = (byte)allSmsCount;

            int j = 0;
            byte smsCount = 0;
            for (int i=0; i < contentVector.length; i++) {
                byte aByte = contentVector[i];
                temp[i - (smsCount*dataSize) + headerBytes] = aByte;
                j++;
                if (j == dataSize) {
                    smsCount++;
                    temp[11] = smsCount;
                    j = 0;
                    return this.getContentObject(temp);
                }
            }
            com.logica.smpp.util.ByteBuffer bb = new com.logica.smpp.util.ByteBuffer();
            temp[11] = ++smsCount;
            bb.appendBytes(temp, headerBytes + j);
            temp = bb.getBuffer();
            
            return this.getContentObject(temp);
        } else {
            //���� sms
            byte[] temp = new byte[headerBytes + contentVector.length];
            temp[0] = 0x06;
            temp[1] = 0x05;
            temp[2] = 0x04;
            temp[3] = (byte)(this.SM_PORT_RING >> 8);
            temp[4] = (byte)(this.SM_PORT_RING);
            temp[5] = 0x00;
            temp[6] = 0x00;
            for (int i=0; i < contentVector.length; i++) {
                byte aByte = contentVector[i];
                temp[i + headerBytes] = aByte;
            }
            ByteBuffer content = this.getContentObject(temp);
            return content;
        }
    }

    protected ByteBuffer onSMLogo( byte[] contentVector ) {
        int headerBytes = 10;
        byte[] temp = new byte[headerBytes + contentVector.length];
        temp[0] = 0x06;
        temp[1] = 0x05;
        temp[2] = 0x04;
        temp[3] = (byte)(this.SM_PORT_LOGO >> 8);
        temp[4] = (byte)(this.SM_PORT_LOGO);
        temp[5] = 0x00;
        temp[6] = 0x00;
        temp[7] = (byte)(this.provId >> 16);
        temp[8] = (byte)(this.provId >> 8);
        temp[9] = (byte)(this.provId);
        for (int i=0; i < contentVector.length; i++) {
            byte aByte = contentVector[i];
            if (i > 3) {
                byte tempByte = (byte) ~aByte;
                aByte = new Byte(tempByte);
            }
            temp[i + headerBytes] = aByte;
        }

        return this.getContentObject(temp);
    }

    protected ByteBuffer onSMPic( byte[] contentVector ) {
        byte smsNumber = (byte)(Math.random()*255);
        int headerBytes = 12;
        byte[] temp = new byte[this.SMS_MAX_SIZE_SM];
        temp[0] = 0x0B;
        temp[1] = 0x05;
        temp[2] = 0x04;
        temp[3] = (byte)(this.SM_PORT_PIC >> 8);
        temp[4] = (byte)(this.SM_PORT_PIC);
        temp[5] = 0x00;
        temp[6] = 0x00;
        temp[7] = 0x00;
        temp[8] = 0x03;
        temp[9] = smsNumber;
        temp[10] = 0x03;
        temp[12] = 0x30;
        temp[13] = 0x02;
        temp[14] = 0x01;
        temp[15] = 0x00;
        Vector tempContent = new Vector();
        for (int j=12; j < 16; j++) {
            tempContent.addElement(new Byte(temp[j]));
        }
        for (int i=0; i < contentVector.length; i++) {
            byte aByte = contentVector[i];
            if (i > 3) {
                byte tempByte = (byte) (~aByte);
                aByte = new Byte(tempByte);
            }
            tempContent.addElement(aByte);
        }
        int j = 0;
        byte smsCount = 0;
        for (int i=0; i < tempContent.size(); i++) {
            Byte aByte = (Byte)tempContent.elementAt(i);
            temp[i - (smsCount*(this.SMS_MAX_SIZE_SM - headerBytes)) + headerBytes] = aByte.byteValue();
            j++;
            if ((j + headerBytes) == this.SMS_MAX_SIZE_SM) {
                smsCount++;
                temp[11] = smsCount;
                j = 0;
                return  this.getContentObject(temp);
            }
        }
        com.logica.smpp.util.ByteBuffer bb = new com.logica.smpp.util.ByteBuffer();
        temp[11] = ++smsCount;
        bb.appendBytes(temp, headerBytes + j);
        temp = bb.getBuffer();

        return this.getContentObject(temp);
    }

    protected List<ByteBuffer> onOTARing( byte[] contentVector ) {
        int smsNumber = (int)(Math.random()*2147483647);
        int headerBytes = 30;
        byte[] temp = new byte[this.SMS_MAX_SIZE_OTA];
        int contentSize = contentVector.length;
        int dataSize = this.SMS_MAX_SIZE_OTA - headerBytes;
        int allSmsCount = (int)(contentSize/dataSize);
        if (contentSize > allSmsCount*dataSize) allSmsCount++;
        temp[0] = 0x2F;                       ///
        temp[1] = 0x2F;                       ///
        temp[2] = 0x53;                       //S
        temp[3] = 0x45;                       //E
        temp[4] = 0x4F;                       //O
        temp[5] = 0x01;                       //version = 1
        temp[6] = (byte)(dataSize);           //dataSize.lowByte
        temp[7] = (byte)(dataSize >> 8);      //dataSize.highByte
        temp[8] = (byte)(smsNumber);          //smsHumber.lowWord.lowByte
        temp[9] = (byte)(smsNumber >> 8);     //smsHumber.lowWord.highByte
        temp[10] = (byte)(smsNumber >> 16);   //smsHumber.highWord.lowByte
        temp[11] = (byte)(smsNumber >> 24);   //smsHumber.highWord.highByte
        temp[12] = 0x00;                      //smsCount.lowByte
        temp[13] = 0x00;                      //smsCount.highByte
        temp[14] = (byte)allSmsCount;         //all smsCount.lowByte
        temp[15] = (byte)(allSmsCount >> 8);  //all smsCount.highByte
        temp[16] = (byte)(contentSize);       //contentSize.lowWord.lowByte
        temp[17] = (byte)(contentSize >> 8);  //contentSize.lowWord.highByte
        temp[18] = (byte)(contentSize >> 16); //contentSize.highWord.lowByte
        temp[19] = (byte)(contentSize >> 24); //contentSize.highWord.highByte
        temp[20] = 0x03;                      //length of following string, ex. "mid"
        temp[21] = 0x6D;                      //m
        temp[22] = 0x69;                      //i
        temp[23] = 0x64;                      //d
        temp[24] = 0x05;                      //length of following string, ex. "m.mid"
        temp[25] = nameGenerator();
//                        temp[25] = 0x6D;                      //m
        temp[26] = 0x2E;                      //.
        temp[27] = 0x6D;                      //m
        temp[28] = 0x69;                      //i
        temp[29] = 0x64;                      //d
        int j = 0;
        int i = 0;

        List<ByteBuffer> result = new ArrayList<ByteBuffer>();
        for (int smsCount=0; smsCount < allSmsCount; smsCount++) {
            for (i=smsCount*dataSize; i < allSmsCount*dataSize; i++) {
                try {
                    byte aByte = contentVector[i];
                    temp[i - (smsCount * dataSize) + headerBytes] = aByte;
                } catch (Exception e) {
                    temp[i - (smsCount * dataSize) + headerBytes] = 0;
                }
                j++;
                if (j == dataSize) {
                    temp[12] = (byte)(smsCount + 1); //smsCount.lowByte
                    temp[13] = (byte)((smsCount + 1) >> 8);//smsCount.highByte
                    j = 0;

                    ByteBuffer buffer = this.getContentObject(temp);
                    if ( buffer != null ) {
                        result.add(buffer);
                    }
                }
            }
        }

        return result;
    }
    
    protected List<ByteBuffer> onEMSRing( byte[] contentVector ) {
        int headerBytes = 3;
        int contentSize = contentVector.length;
        int dataSize = this.SMS_MAX_SIZE_EMS - headerBytes;
        int allSmsCount = (int)(contentSize/dataSize);
        if (contentSize > allSmsCount*dataSize) allSmsCount++;
        
        List<ByteBuffer> ret = new ArrayList<ByteBuffer>();
        if (allSmsCount > 1) {
            //��������� sms
            headerBytes = 7;
            short smsNumber = (short)(Math.random()*65535);
            dataSize = this.SMS_MAX_SIZE_EMS - headerBytes;
            allSmsCount = (int)(contentSize/dataSize);
            if (contentSize > allSmsCount*dataSize) allSmsCount++;
            byte[] temp = new byte[this.SMS_MAX_SIZE_EMS];
            temp[0] = 0x06;
            temp[1] = 0x08;
            temp[2] = 0x04;
            temp[3] = (byte)(smsNumber >> 8);
            temp[4] = (byte)(smsNumber);
            temp[5] = (byte)allSmsCount;

            int j = 0;
            byte smsCount = 0;
            for (int i=0; i < contentVector.length; i++) {
                byte aByte = contentVector[i];
                temp[i - (smsCount*dataSize) + headerBytes] = aByte;
                j++;
                if (j == dataSize) {
                    smsCount++;
                    temp[0] = (byte)(this.SMS_MAX_SIZE_EMS - 1);
                    temp[6] = smsCount;
                    j = 0;
                    ByteBuffer content = this.getContentObject(temp);
                    if (content != null) {
                        ret.add(content);
                    }
                }
            }
            com.logica.smpp.util.ByteBuffer bb = new com.logica.smpp.util.ByteBuffer();
            temp[6] = ++smsCount;
            bb.appendBytes(temp, headerBytes + j);
            temp = bb.getBuffer();
            temp[0] = (byte)(temp.length - 1);
            ByteBuffer content = this.getContentObject(temp);
            if (content != null) { 
                ret.add(content);
            }
        }
        else {
            //���� sms
            byte[] temp = new byte[headerBytes + contentVector.length];
            temp[0] = (byte)(temp.length - 1);
            temp[1] = 0x0C;
            temp[2] = (byte)(contentVector.length);
            for (int i = 0; i < contentVector.length; i++) {
                byte aByte = contentVector[i];
                temp[i + headerBytes] = aByte;
            }
            ByteBuffer content = this.getContentObject(temp);
            if (content != null) { 
                ret.add(content);
            }
        }
        
        return ret;
    }
    
    protected List<ByteBuffer> onOTAPic( byte[] contentVector ) {
        //OTA ���� ������
        int smsNumber = (int)(Math.random()*2147483647);
        int headerBytes = 30;
        byte[] temp = new byte[this.SMS_MAX_SIZE_OTA];
        int contentSize = contentVector.length;
        int dataSize = this.SMS_MAX_SIZE_OTA - headerBytes;
        int allSmsCount = (int)(contentSize/dataSize);
        if (contentSize > allSmsCount*dataSize) allSmsCount++;
        temp[0] = 0x2F;                       ///
        temp[1] = 0x2F;                       ///
        temp[2] = 0x53;                       //S
        temp[3] = 0x45;                       //E
        temp[4] = 0x4F;                       //O
        temp[5] = 0x01;                       //version = 1
        temp[6] = (byte)(dataSize);           //dataSize.lowByte
        temp[7] = (byte)(dataSize >> 8);      //dataSize.highByte
        temp[8] = (byte)(smsNumber);          //smsHumber.lowWord.lowByte
        temp[9] = (byte)(smsNumber >> 8);     //smsHumber.lowWord.highByte
        temp[10] = (byte)(smsNumber >> 16);    //smsHumber.highWord.lowByte
        temp[11] = (byte)(smsNumber >> 24);   //smsHumber.highWord.highByte
        temp[12] = 0x00;                      //smsCount.lowByte
        temp[13] = 0x00;                      //smsCount.highByte
        temp[14] = (byte)allSmsCount;         //all smsCount.lowByte
        temp[15] = (byte)(allSmsCount >> 8);  //all smsCount.highByte
        temp[16] = (byte)(contentSize);       //contentSize.lowWord.lowByte
        temp[17] = (byte)(contentSize >> 8);  //contentSize.lowWord.highByte
        temp[18] = (byte)(contentSize >> 16); //contentSize.highWord.lowByte
        temp[19] = (byte)(contentSize >> 24); //contentSize.highWord.highByte
        temp[20] = 0x03;                      //length of following string, ex. "bmp"
        temp[21] = 0x62;                      //b
        temp[22] = 0x6D;                      //m
        temp[23] = 0x70;                      //p
        temp[24] = 0x05;                      //length of following string, ex. "p.bmp"
        temp[25] = nameGenerator();
//                        temp[25] = 0x70;                      //p
        temp[26] = 0x2E;                      //.
        temp[27] = 0x62;                      //b
        temp[28] = 0x6D;                      //m
        temp[29] = 0x70;                      //p
        int j = 0;
        int i = 0;
        
        List<ByteBuffer> ret = new ArrayList<ByteBuffer>();
        for (int smsCount=0; smsCount < allSmsCount; smsCount++) {
            for (i=smsCount*dataSize; i < allSmsCount*dataSize; i++) {
                try {
                    byte aByte = contentVector[i];
                    temp[i - (smsCount * dataSize) + headerBytes] = aByte;
                } catch (Exception e) {
                    temp[i - (smsCount * dataSize) + headerBytes] = 0;
                }
                j++;
                if (j == dataSize) {
                    temp[12] = (byte)(smsCount + 1); //smsCount.lowByte
                    temp[13] = (byte)((smsCount + 1) >> 8);//smsCount.highByte
                    j = 0;
                    ByteBuffer content = this.getContentObject(temp);
                    if (content != null) {
                        ret.add(content);
                    }
                    break;
                }
            }
        }
        
        return ret;
    }

    protected ByteBuffer onEMSPic( byte[] contentVector ) {
        //EMS �� ���� ������
        int headerBytes = 3;
        byte[] temp = new byte[headerBytes + contentVector.length];
        temp[0] = (byte)(temp.length - 1);
        temp[1] = 0x10;
        temp[2] = (byte)(contentVector.length);
        for (int i = 0; i < contentVector.length; i++) {
            byte aByte = contentVector[i];
            if (i > 0) {
                byte tempByte = (byte) (~aByte);
                aByte = new Byte(tempByte);
            }
            temp[i + headerBytes] = aByte;
        }

        return this.getContentObject(temp);
    }

    protected ByteBuffer onEMSVPic( byte[] contentVector ) {
        int headerBytes = 3;
        byte[] temp = new byte[headerBytes + contentVector.length];
        temp[0] = (byte)(temp.length - 1);
        temp[1] = 0x12;
        temp[2] = (byte)(contentVector.length);
        for (int i = 0; i < contentVector.length; i++) {
            Byte aByte = (Byte) contentVector[i];
            if (i > 2) {
                byte tempByte = (byte) (~aByte.byteValue());
                aByte = new Byte(tempByte);
            }
            temp[i + headerBytes] = aByte.byteValue();
        }

        return this.getContentObject(temp);
    }

    public List getContent() {
        List ret = new ArrayList();

        byte[] contentVector = sms.getData();

        switch ( mimeType ) {
            case MIME_TYPE_SM_RING:
                ret.add( this.onSMRing(contentVector) );
            break;
            case MIME_TYPE_SM_LOGO:
               ret.add( this.onSMLogo(contentVector) );
            break;
            case MIME_TYPE_SM_PIC:
                ret.add( this.onSMPic(contentVector) );
            break;
            case MIME_TYPE_OTA_RING:
                ret.addAll( this.onOTARing(contentVector) );
            break;
            case MIME_TYPE_OTA_PIC:
                ret.addAll( this.onOTAPic(contentVector) );
            break;
            case MIME_TYPE_EMS_RING:
               ret.addAll( this.onEMSRing(contentVector) );
            break;
            case MIME_TYPE_EMS_PIC:
                ret.add( this.onEMSPic(contentVector) );
            break;
            case MIME_TYPE_EMS_VPIC:
                ret.add( this.onEMSVPic(contentVector) );
            break;
            default:
                ret.add( String.valueOf(contentVector) );
        }

        return ret;
    }

}
