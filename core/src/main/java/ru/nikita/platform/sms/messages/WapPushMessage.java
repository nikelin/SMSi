/*
 * WapPushMessage.java
 *
 * Created on 15 �������� 2005 �., 15:52
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package ru.nikita.platform.sms.messages;

import com.redshape.utils.StringUtils;

import java.util.UUID;

/**
 * @author Alexey Solomin
 * @author Cyril A. Karpenko
 */
public class WapPushMessage extends DataMessage {

    private String name;
    private String url;

    public WapPushMessage( UUID providerId ) {
        this(providerId, null, null);

        this.setMimeType(MessageMimeType.MimeType.DEFAULT);
    }

    /** Creates a new instance of WapPushMessage */
    public WapPushMessage(UUID providerId, String name, String url) {
        super( providerId, new byte[] {} );
        
        this.setMimeType(MessageMimeType.MimeType.DEFAULT);
        this.name = name;
        this.url = url;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public byte[] getData() {
        String bd = "0605040B8423F025060A03AE81EAAF828DD9B484" +
                "02056A00" +
                "45C6";
        if (url.startsWith("http://")) {
            bd += "0C";
            url = url.substring(7);
        } else if (url.startsWith("https://")) {
            bd += "0E";
            url = url.substring(8);
        }
        bd += "03";
        bd += StringUtils.toHEX(url);
        bd += "00";
        bd += "01";
        bd += "03";
        bd += StringUtils.toHEX(name);
        bd += "000101";
        byte[] ret = new byte[bd.length() / 2];
        for (int z = 0; z < bd.length(); z+=2) {
            String st = bd.substring(z, z + 2);
            byte b = (byte)Integer.parseInt(st, 16);
            ret[z / 2] = b;
        }
        return ret;
    }

}
