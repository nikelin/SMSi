package ru.nikita.platform.sms.plugins;

import com.redshape.daemon.AbstractRMIDaemon;
import com.redshape.daemon.DaemonAttributes;
import com.redshape.daemon.DaemonException;
import com.redshape.daemon.traits.IPublishableDaemon;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.AbstractRMIServer;
import ru.nikita.platform.sms.plugins.services.ServerService;

import java.rmi.registry.Registry;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 6:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Server extends AbstractRMIServer {
    private static final Logger log = Logger.getLogger(Server.class);
    
    public Server(String contextPath) throws DaemonException, ConfigException {
        super(contextPath);
    }

    @Override
    protected void doPublish() throws DaemonException {
        try {
            String serviceName = this.<String>getAttribute( AbstractRMIDaemon.Attributes.SERVICE_NAME );
            log.info("Starting service with name: " + serviceName);

            ServerService service = new ServerService( this.getPath() );
            this.getContext().getAutowireCapableBeanFactory().autowireBean(service);
            this.exportService(service);
        } catch ( Throwable e ) {
            throw new DaemonException( e.getMessage(), e );
        }
    }
    
    public static void main( String[] args ) {
        if ( args.length < 1 ) {
            throw new IllegalArgumentException("Spring context path must be provided");
        }
        
        try {
            Server server = new Server(args[0]);
            server.start();
        } catch ( Throwable e ) {
            log.error( e.getMessage(), e );
        }
    }
    
}
