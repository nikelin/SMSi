package ru.nikita.platform.sms.listeners;

import com.redshape.utils.events.IEventListener;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.registry.IRegistry;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.providers.IProvider;
import ru.nikita.platform.sms.providers.ProviderEvent;
import ru.nikita.platform.sms.queue.IMessageQueue;
import ru.nikita.platform.sms.queue.IQueuesRegistry;
import ru.nikita.platform.sms.queue.InMemoryQueue;
import ru.nikita.platform.sms.registry.IRestorationData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 9:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProviderListener implements IRegistry.IRegistryInterceptor<IProvider, IRestorationData> {
    private static final Logger log = Logger.getLogger(ProviderListener.class);
    private List<IEventListener<ProviderEvent>> cascadeInterceptors = new ArrayList<IEventListener<ProviderEvent>>();
    private IQueuesRegistry registry;

    public ProviderListener(IQueuesRegistry registry) {
        this.registry = registry;
    }

    public IQueuesRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(IQueuesRegistry registry) {
        this.registry = registry;
    }

    public void setCascadeInterceptors(List<IEventListener<ProviderEvent>> cascadeInterceptors) {
        this.cascadeInterceptors = cascadeInterceptors;
    }

    public List<IEventListener<ProviderEvent>> getCascadeInterceptors() {
        return cascadeInterceptors;
    }

    @Override
    public void init(IRegistry<IProvider, IRestorationData> iProviderIRegistry) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void onMessageReceived( ProviderEvent event ) {
        IMessageQueue queue = this.getRegistry().getQueue(event.getProvider());
        if ( queue == null ) {
            log.error("Queue for provider " + event.getProvider().getClass().getCanonicalName() + " not initialized");
            return;
        }

        queue.push(event.<IMessage>getArg(0));

        for ( IEventListener<ProviderEvent> listener : this.getCascadeInterceptors() ) {
            listener.handleEvent(event);
        }
    }

    @Override
    public void onRecord(IProvider record) {
        IMessageQueue queue = this.getRegistry().getQueue(record);
        if ( queue == null ) {
            log.info("Setting default messages queue for provider " + record.toString() );
            this.getRegistry().registerQueue( record, new InMemoryQueue() );
        }
        
        record.addEventListener(ProviderEvent.class, new IEventListener<ProviderEvent>() {
            @Override
            public void handleEvent(ProviderEvent event) {
                if ( event.getType().equals( ProviderEvent.Type.Message.Received ) ) {
                    ProviderListener.this.onMessageReceived(event);
                }
            }
        });
    }
}
