package ru.nikita.platform.sms.messages;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class NullMessage extends DataMessage {

    public NullMessage(UUID providerId) {
        super(providerId, new byte[] {} );
    }

    @Override
    public MessageType getType() {
        return MessageType.NULL;
    }

}
