package ru.nikita.platform.sms.queue;

import ru.nikita.platform.sms.registry.IRegistry;
import ru.nikita.platform.sms.providers.IProvider;
import ru.nikita.platform.sms.registry.IRestorationData;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IQueuesRegistry<V extends IRestorationData> extends IRegistry<IMessageQueue, V> {

    /**
     * Return messages queue related to the given provider
     * @param provider
     * @return
     */
    public IMessageQueue getQueue( IProvider provider );

    public void registerQueue( IProvider provider, IMessageQueue messageQueue );
    
    public Collection<IMessageQueue> list();

}
