package ru.nikita.platform.sms;

import com.redshape.applications.bootstrap.LoggingStarter;
import com.redshape.daemon.*;
import com.redshape.daemon.events.PublishedEvent;
import com.redshape.daemon.jobs.managers.IJobsManager;
import com.redshape.daemon.services.ClientsFactory;
import com.redshape.daemon.services.ServerFactory;
import com.redshape.daemon.traits.IPublishableDaemon;
import com.redshape.utils.Constants;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.events.IEventListener;
import com.redshape.utils.net.Utils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import ru.nikita.platform.sms.services.IKernelService;
import ru.nikita.platform.sms.services.ServerService;

import java.io.IOException;
import java.rmi.registry.Registry;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Server extends AbstractRMIServer {
    private static final Logger log = Logger.getLogger( Server.class );
    private IKernelService service;

    public Server( ApplicationContext context ) throws DaemonException, ConfigException {
        super(context);
    }
    
    public Server(String contextPath) throws DaemonException, ConfigException {
        super(contextPath);
    }

    public <T extends IRemoteService> T getService() {
        return (T) this.service;
    }

    @Override
    protected void doPublish() throws DaemonException {
        try {
            String serviceName = this.<String>getAttribute( Attributes.SERVICE_NAME );
            log.info("Starting service with name: " + serviceName);

            this.service = new ServerService( this.getContext().getBean(IJobsManager.class), this.getPath() );
            this.getContext().getAutowireCapableBeanFactory().autowireBean(this.service);
            this.exportService(this.service);
        } catch ( Throwable e ) {
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
