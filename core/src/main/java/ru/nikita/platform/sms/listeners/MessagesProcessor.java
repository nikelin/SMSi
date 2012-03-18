package ru.nikita.platform.sms.listeners;

import com.redshape.utils.events.IEventListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.registry.IRegistry;
import ru.nikita.platform.sms.bind.IBindedPair;
import ru.nikita.platform.sms.bind.IBindedPairsRegistry;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.processors.IProcessor;
import ru.nikita.platform.sms.processors.IProcessorsRegistry;
import ru.nikita.platform.sms.processors.ProcessingException;
import ru.nikita.platform.sms.queue.IMessageQueue;
import ru.nikita.platform.sms.queue.QueueEvent;
import ru.nikita.platform.sms.registry.IRestorationData;
import ru.nikita.platform.sms.utils.ContextHandler;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/20/11
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessagesProcessor implements IRegistry.IRegistryInterceptor<IMessageQueue, IRestorationData> {
    private static final Logger log = Logger.getLogger(MessagesProcessor.class);

    public MessagesProcessor() {
        super();
    }

    public IProcessorsRegistry<?> getProcessorsRegistry() {
        return ContextHandler.instance().getContext().getBean( IProcessorsRegistry.class );
    }

    public IBindedPairsRegistry<?> getRegistry() {
        return ContextHandler.instance().getContext().getBean(IBindedPairsRegistry.class);
    }

    @Override
    public void init(IRegistry<IMessageQueue, IRestorationData> registry) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void processIncoming( IMessage message ) {
        for ( IBindedPair pair : this.getRegistry().getProcessors( message.getProvider() ) ) {
            if ( !pair.getCondition().isMatch(message) ) {
                continue;
            }

            IProcessor processor = this.getProcessorsRegistry().getProcessor(pair.getProcessor().getId());
            log.info("Applying processor " + pair.getProcessor().getName() + " on message " + message.getId() );
            if ( processor == null ) {
                log.info("Processor " + pair.getProcessor().getName() + " is not initialized! Skiping...");
                continue;
            }

            try {
                processor.process(message);
            } catch ( ProcessingException e ) {
                log.error( "Message processing failed!", e );
            }
        }
    }

    @Override
    public void onRecord(IMessageQueue record) {
        record.addEventListener( QueueEvent.class, new IEventListener<QueueEvent>() {
            @Override
            public void handleEvent(QueueEvent event) {
                if ( event.getType().equals( QueueEvent.Type.PUSH ) ) {
                    MessagesProcessor.this.processIncoming( event.<IMessage>getArg(0) );
                }
            }
        });
    }
}
