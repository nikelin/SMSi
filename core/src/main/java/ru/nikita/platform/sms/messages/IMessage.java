package ru.nikita.platform.sms.messages;

import ru.nikita.platform.sms.processors.IProcessorModel;
import ru.nikita.platform.sms.providers.IProviderModel;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMessage {

    public MessageMimeType.MimeType getMimeType();
    
    /**
     * Indicated unique identifier of message
     *
     * @return
     */
    public UUID getId();

    /**
     * Return entry-point provider identifier related to this message
     *
     * @return
     */
    public UUID getProvider();

    /**
     * Return type of the message (data, delivery, sent, etc. )
     *
     * @return
     */
    public MessageType getType();

    /**
     * Return message binary representation
     * @return
     */
    public byte[] getData();

}
