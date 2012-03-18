package ru.nikita.platform.sms.queue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.providers.IProvidersRegistry;
import ru.nikita.platform.sms.registry.AbstractRegistry;
import ru.nikita.platform.sms.providers.IProvider;
import ru.nikita.platform.sms.registry.IRestorationData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class StandardQueueRegistry extends AbstractRegistry<IMessageQueue, StandardQueueRegistry.QueueRestorationData>
                                                implements IQueuesRegistry<StandardQueueRegistry.QueueRestorationData> {
    private static final Logger log = Logger.getLogger(StandardQueueRegistry.class);

    public static class QueueRestorationData implements IRestorationData {
        public IProvider provider;
        private IMessageQueue queue;

        public QueueRestorationData(IProvider provider, IMessageQueue queue) {
            this.provider = provider;
            this.queue = queue;
        }

        public IProvider getProvider() {
            return provider;
        }

        public IMessageQueue getQueue() {
            return queue;
        }
    }

    private Map<IProvider, IMessageQueue> queues = new HashMap<IProvider, IMessageQueue>();

    public StandardQueueRegistry( List<IRegistryInterceptor<IMessageQueue, QueueRestorationData>> interceptors ) {
        super(interceptors);
    }

    public void registerQueue( IProvider provider, IMessageQueue queue ) {
        this.queues.put(provider, queue);
        this.onRecord(queue);
    }
    
    @Override
    public IMessageQueue getQueue( IProvider provider ) {
        return this.queues.get( provider );
    }

    @Override
    public Collection<IMessageQueue> list() {
        return this.queues.values();
    }
    
    public void onRestore( QueueRestorationData queue ) {
        this.registerQueue( queue.getProvider(), queue.getQueue() );
        log.debug("Record restored");
    }

    @Override
    public QueueRestorationData prepareRestoration(IMessageQueue item) {
        return null;
    }
}
