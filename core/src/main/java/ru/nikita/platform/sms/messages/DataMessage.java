package ru.nikita.platform.sms.messages;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataMessage extends AbstractMessage {

    private MessageMimeType.MimeType mimeType;
    private MessageType type;
    private byte[] data;

    public DataMessage( UUID providerId, byte[] data ) {
        this(providerId, MessageType.DATA, data);
    }

    public DataMessage( UUID providerId, MessageType type, byte[] data ) {
        super(providerId);

        this.mimeType = MessageMimeType.MimeType.BINARY;
        this.type = type;
        this.data = data;
    }

    protected void setMimeType( MessageMimeType.MimeType mimeType ) {
        this.mimeType = mimeType;
    }

    @Override
    public MessageMimeType.MimeType getMimeType() {
        return this.mimeType;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }
}
