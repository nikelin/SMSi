package ru.nikita.platform.sms.messages;

import ru.nikita.platform.sms.utils.ShortMessageUtils;
import ru.nikita.platform.sms.utils.TextUtils;
import ru.nikita.platform.sms.utils.Transliterator;

import java.util.*;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package ru.nikita.platform.sms
 * @time 12/9/11 1:26 PM
 */
public class Message extends DataMessage {

    private String phone;
    private String module;
    private String content;
    private int count;
    private String lang;
    private String keyword;
    private int actualLength;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private Date received;

    public Message( UUID providerId ) {
        super(providerId, new byte[] {} );
        this.setMimeType( MessageMimeType.MimeType.MIME_TYPE_SMS );
        this.received = new Date();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Deprecated
    /**
     * @FIXME: ASAP!
     */
    public final void setContent(String content, int dataCoding) {
        if (dataCoding == 0) {
            this.content = TextUtils.removeRepeating(TextUtils.deUnicode(content, true), '@');
        } else if (dataCoding == 8) {
            this.content = TextUtils.deUnicode(content, true);
        } else {
            this.content = content;
        }

        this.content = Transliterator.translate(this.content);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public byte[] getData() {
        return ShortMessageUtils.to7bit( this.getContent().getBytes(), true );
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getActualLength() {
        return actualLength;
    }

    public void setActualLength(int actualLength) {
        this.actualLength = actualLength;
    }

    public void setProperty( String name, Object value ) {
        this.properties.put(name, value);
    }
    
    public <V> V getProperty( String name ) {
        return (V) this.properties.get(name);
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    public String toString() {
        String ret = "{\n";
        ret += "provider: " + this.getProvider() + ";\n";
        ret += "phone: " + phone + ";\n";
        ret += "content: [" + content + "];\n";
        ret += "content (translited): [" + Transliterator.translate(this.getContent()) + "];\n";
        ret += "content (original): [" + this.getContent() + "];\n";
        ret += "keyword: " + keyword + ";\n";
        ret += "module: " + module + ";\n";
        ret += "properties : {\n";
        try {
            Set kSet = properties.keySet();
            for (Iterator it = kSet.iterator(); it.hasNext();) {
                String pName = (String) it.next();
                Object prop = properties.get(pName);
                ret += pName + ": ";
                if (prop instanceof Boolean) {
                    ret += ((Boolean) prop).booleanValue();
                } else if (prop instanceof Integer) {
                    ret += ((Integer) prop).intValue();
                } else if (prop instanceof String) {
                    ret += (String) prop;
                } else if (prop instanceof byte[]) {
                    ret += "[";
                    byte[] bt = (byte[]) prop;
                    for (int i = 0; i < bt.length; i++) {
                        String hex = Integer.toHexString(bt[i] >= 0 ? bt[i] : 256 + bt[i]);
                        if (hex.length() == 1) {
                            hex = "0" + hex;
                        }
                        ret += hex;
                    }
                    ret += "]";
                } else if (prop != null) {
                    ret += prop.toString();
                } else {
                    ret += "NULL";
                }
                ret += ";\n";
            }
        } catch (Exception ex) {
            System.out.println("ALERT!!! Exception in ru.nikita.common.SmsMessage.toString()");
            ex.printStackTrace();
        }
        ret += "}\n";
        ret += "}\n";
        return ret;
    }

}
