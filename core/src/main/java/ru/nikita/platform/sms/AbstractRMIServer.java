package ru.nikita.platform.sms;

import com.redshape.applications.bootstrap.LoggingStarter;
import com.redshape.daemon.AbstractRMIDaemon;
import com.redshape.daemon.DaemonAttributes;
import com.redshape.daemon.DaemonException;
import com.redshape.daemon.DaemonState;
import com.redshape.daemon.events.PublishedEvent;
import com.redshape.daemon.services.ClientsFactory;
import com.redshape.daemon.services.ServerFactory;
import com.redshape.daemon.traits.IPublishableDaemon;
import com.redshape.utils.Commons;
import com.redshape.utils.Constants;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.events.IEventListener;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRMIServer extends AbstractRMIDaemon<DaemonAttributes>
        implements IPublishableDaemon<Registry,DaemonAttributes> {
    private static final List<AbstractRMIServer> instances = new ArrayList<AbstractRMIServer>();
    
    static {
        LoggingStarter.init();

        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run() {
                log.info("Shutdown hook activated...");
                for ( AbstractRMIServer instance : instances ) {
                    try {
                        log.info("Stopping instance binded on: " +
                                Commons.select( instance.getHost(), "<unknown>" )
                                + " : " +
                                Commons.select( instance.getPort(), -1 ) );
                        instance.stop();
                    } catch ( Throwable e ) {
                        log.error( e.getMessage(), e );
                    }
                }
            }
        });
    }

    public static class Attributes extends DaemonAttributes {
        protected Attributes(String code) {
            super(code);
        }

        public static final Attributes DISCOVERY_ENABLED = new Attributes("Server.Attributes.DiscoveryEnabled");
    }

    private static final Logger log = Logger.getLogger(AbstractRMIServer.class);

    public AbstractRMIServer( ApplicationContext context ) throws DaemonException, ConfigException {
        super(context);

        instances.add( this );
        
        this.init();
    }

    public AbstractRMIServer(String contextPath) throws DaemonException, ConfigException {
        super(contextPath);

        this.init();
    }

    protected void init() {
        this.setClientsFactory( new ClientsFactory(this.getHost()) );
        this.setServerFactory( new ServerFactory(this.getHost(), this.getMaxConnections() ) );
    }

    @Override
    public boolean ping() {
        return true;
    }

    @Override
    public String status() {
        return null;
    }

    @Override
    public Registry getEndPoint() {
        return this.getRegistry();
    }

    @Override
    public void publish() throws DaemonException {
        if ( this.doPublishing() ) {
            this.doPublish();
        }
    }

    protected abstract void doPublish() throws DaemonException;

    protected Boolean isDiscoveryEnabled() {
        return this.<Boolean>getAttribute( Attributes.DISCOVERY_ENABLED );
    }

    @Override
    public boolean doPublishing() {
        return true;
    }

    @Override
    protected void onStarted() throws DaemonException {
        if ( this.doPublishing() ) {
            this.publish();
        }
    }

    @Override
    public void loadConfiguration(IConfig configLocation) throws DaemonException, ConfigException {
        this.setHost(configLocation.get("server.host").value());
        this.setPort(Integer.valueOf(configLocation.get("server.port").value()));
        this.setPath( configLocation.get("server.service").value() );
        this.setMaxConnections( Integer.valueOf( configLocation.get("server.maxConnections").value() ) );
        this.setAttribute( Attributes.DISCOVERY_ENABLED, true );
    }
}