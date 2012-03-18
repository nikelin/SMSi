package ru.nikita.platform.sms.messages;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/20/11
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMessage implements IMessage {
    private UUID id;
    private UUID providerId;

    public AbstractMessage( UUID providerId ) {
        this.id = UUID.randomUUID();
        this.providerId = providerId;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getProvider() {
        return providerId;
    }
}
