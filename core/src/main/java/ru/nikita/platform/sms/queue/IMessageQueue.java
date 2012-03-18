package ru.nikita.platform.sms.queue;

import com.redshape.utils.events.IEventDispatcher;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.messages.Message;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMessageQueue extends IEventDispatcher {

    /**
     * Take the upcoming object in queue
     * without actual remove him from a queue.
     * @return
     */
    public IMessage peek();

    /**
     * Return the upcoming object and remove
     * him from a queue
     * @return
     */
    public IMessage pop();

    /**
     * Push object to the end of queue
     * @param message
     */
    public void push( IMessage message );

    /**
     * Return size of queue
     * @return
     */
    public int size();
    
}
