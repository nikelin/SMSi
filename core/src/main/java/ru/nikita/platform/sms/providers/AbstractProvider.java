package ru.nikita.platform.sms.providers;

import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.events.AbstractEventDispatcher;
import com.redshape.utils.events.IEvent;
import com.redshape.utils.events.IEventListener;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractProvider extends AbstractEventDispatcher implements IProvider {
    private static final Logger logger = Logger.getLogger(AbstractProvider.class);

    private IProviderModel model;
    private IConfig config;
    private boolean binded;

    protected AbstractProvider(IProviderModel model, IConfig config) {
        super();

        this.model = model;
        this.config = config;
    }

    protected void markBinded() {
        this.binded = true;
    }

    protected void markUnbinded() {
        this.binded = false;
    }

    @Override
    public boolean isBind() {
        return this.binded;
    }

    protected boolean isTransciever() throws ConfigException {
        return this.getConfig().get("sms.bind.mode").value().equals("tr");
    }
    
    protected IProviderModel getModel() {
        return model;
    }

    protected IConfig getConfig() {
        return config;
    }

    protected int getThreadsCount() {
        try {
            return this.<Integer>readParameter("core.executorThreads", Integer.class);
        } catch ( Throwable e ) {
            return 10;
        }
    }
    
    protected ExecutorService createThreadsExecutor() throws ConfigException {
        return Executors.newScheduledThreadPool(this.getThreadsCount());
    }

    protected String getServiceName() {
        return this.getModel().getName();
    }

    protected <T> T readParameter( String name, Class<?> toType ) throws ConfigException {
        String value = this.getConfig().get(name).value();
        try {
            if ( Integer.class.equals( toType ) ) {
                return (T) Integer.valueOf(value);
            } else if ( Long.class.equals( toType ) ) {
                return (T) Long.valueOf( value );
            } else {
                throw new UnsupportedOperationException("Destination type is not supported at the moment");
            }
        } catch ( NumberFormatException e ) {
            logger.warn( String.format( "Reading parameter %s failed. Taking defaults.", name ) );
            return null;
        }
    }

    protected boolean isDebug() throws ConfigException {
        return Boolean.valueOf( this.getConfig().get("sms.connection.debug").value() );
    }

    protected Socket createSocket() throws IOException {
        URI uri = this.getModel().getAdapterModel().getURL();
        Socket socket = new Socket( uri.getHost(), uri.getPort() );

        return socket;
    }

}
