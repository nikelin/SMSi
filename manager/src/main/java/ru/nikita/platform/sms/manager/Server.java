package ru.nikita.platform.sms.manager;

import com.redshape.applications.bootstrap.LoggingStarter;
import com.redshape.daemon.*;
import com.redshape.daemon.events.PublishedEvent;
import com.redshape.daemon.services.ClientsFactory;
import com.redshape.daemon.services.ServerFactory;
import com.redshape.daemon.traits.IPublishableDaemon;
import com.redshape.utils.Constants;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.events.IEventListener;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.AbstractRMIServer;
import ru.nikita.platform.sms.manager.services.ServerService;

import java.io.IOException;
import java.rmi.registry.Registry;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Server extends AbstractRMIServer {
    private static final Logger log = Logger.getLogger( Server.class );

    private IRemoteService service;

    public Server(String contextPath) throws DaemonException, ConfigException {
        super(contextPath);
    }

    public IRemoteService getService() {
        return this.service;
    }

    @Override
    protected void doPublish() throws DaemonException {
        try {
            String serviceName = this.<String>getAttribute( Attributes.SERVICE_NAME );
            log.info("Starting service with name: " + serviceName);

            ServerService service = new ServerService( this.getContext(), this.getPath() );
            this.getContext().getAutowireCapableBeanFactory().autowireBean(service);

            this.service = this.exportService(service);
        } catch ( Throwable e ) {
            log.error( e.getMessage(), e );
            throw new DaemonException( e.getMessage(), e );
        }
    }

    public static void main( String[] args ) {
        if ( args.length < 1 ) {
            throw new IllegalArgumentException("Spring context path not defined!");
        }

        try {
            final Server server = new Server( args[0] );
            server.loadConfiguration();
            server.start();

            do {
                log.info("IDLE");
                Thread.sleep( Constants.TIME_SECOND * 5 );
            } while ( server.getState().equals( DaemonState.STARTED ) );
        } catch ( Throwable e ) {
            log.error( e.getMessage(), e );
        }
    }
}
