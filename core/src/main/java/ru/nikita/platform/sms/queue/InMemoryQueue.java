package ru.nikita.platform.sms.queue;

import com.redshape.utils.events.AbstractEventDispatcher;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.messages.Message;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class InMemoryQueue extends AbstractEventDispatcher implements IMessageQueue {
    private static final Logger log = Logger.getLogger(InMemoryQueue.class);
    private Queue<IMessage> store = new LinkedBlockingQueue<IMessage>();

    @Override
    public int size() {
        return this.store.size();
    }

    @Override
    public void push(IMessage message) {
        this.store.add( message );
        this.raiseEvent( new QueueEvent( QueueEvent.Type.PUSH, message ) );
    }

    @Override
    public IMessage pop() {
        IMessage message = this.store.poll();
        this.raiseEvent( new QueueEvent( QueueEvent.Type.POLL, message ) );
        return message;
    }

    @Override
    public IMessage peek() {
        return this.store.peek();
    }
}
