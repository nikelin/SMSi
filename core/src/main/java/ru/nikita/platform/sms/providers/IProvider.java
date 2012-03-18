package ru.nikita.platform.sms.providers;

import com.redshape.utils.config.ConfigException;
import com.redshape.utils.events.IEventDispatcher;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.messages.Message;

import java.io.IOException;
import java.util.List;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package ru.nikita.platform.sms
 * @time 1:24 PM
 */
public interface IProvider extends IEventDispatcher {
    public boolean isAsync();

    public boolean isBind();

    public void disconnect() throws IOException, ConfigException;

    public void bind() throws IOException, ConfigException;

    public void receive() throws IOException, ConfigException;

    public IMessage receiveAndWait() throws IOException, ConfigException;

    public void send( Message message ) throws IOException;
    
    public boolean sendAndWait( Message message ) throws IOException;

    public void send( List<Message> messages ) throws IOException;
    
    public boolean sendAndWait( List<Message> messages ) throws IOException;

}
