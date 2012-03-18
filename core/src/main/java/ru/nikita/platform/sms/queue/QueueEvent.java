package ru.nikita.platform.sms.queue;

import com.redshape.utils.events.AbstractEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/20/11
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueueEvent extends AbstractEvent {
    public static enum Type {
        POLL,
        PUSH
    }

    private Type type;

    public QueueEvent() {
        this(null, new Object[] {} );
    }

    public QueueEvent( Type type, Object... args ) {
        super(args);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
