package ru.nikita.platform.sms.processors.impl;

import com.redshape.utils.events.AbstractEventDispatcher;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.bind.IBindedPair;
import ru.nikita.platform.sms.bind.IBindedPairsRegistry;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.messages.Message;
import ru.nikita.platform.sms.messages.MessageMimeType;
import ru.nikita.platform.sms.processors.IProcessor;
import ru.nikita.platform.sms.processors.ProcessingException;
import ru.nikita.platform.sms.providers.IProvider;
import ru.nikita.platform.sms.providers.IProvidersRegistry;
import ru.nikita.platform.sms.utils.ContextHandler;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class MZProcessor extends AbstractEventDispatcher implements IProcessor {
    private static final Logger log = Logger.getLogger(MZProcessor.class);


    public IProvidersRegistry<?> getProvidersRegistry() {
        return ContextHandler.instance().getContext().getBean( IProvidersRegistry.class );
    }

    public IBindedPairsRegistry<?> getBindingsRegistry() {
        return ContextHandler.instance().getContext().getBean( IBindedPairsRegistry.class );
    }

    @Override
    public void process(IMessage process) throws ProcessingException {
        log.info("Message received by MZ service");
        for ( int i = 0; i < 300; i++ ) {
            for ( IBindedPair provider : this.getBindingsRegistry().list() ) {
                UUID providerId = provider.getProvider().getId();
                IProvider obj = this.getProvidersRegistry().getProvider( providerId );

                Message message = new Message( providerId );
                message.setContent("Afla!");
                message.setPhone("12345");

                try {
                    if ( obj != null ) {
                        obj.send( message );
                    }
                } catch ( Throwable e ) {
                    log.error( e.getMessage(), e );
                }
            }
        }
    }

}
