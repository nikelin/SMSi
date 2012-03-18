package ru.nikita.platform.sms.manager.kernels;

import com.redshape.daemon.DaemonException;
import com.redshape.utils.config.ConfigException;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ru.nikita.platform.sms.Server;
import ru.nikita.platform.sms.registry.AbstractRegistry;
import ru.nikita.platform.sms.registry.IKernelsRegistry;
import ru.nikita.platform.sms.registry.IRestorationData;
import ru.nikita.platform.sms.services.IKernelService;
import ru.nikita.platform.sms.services.data.ServerConfiguration;
import ru.nikita.platform.sms.utils.ContextHandler;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/22/11
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class StandardKernelsRegistry extends AbstractRegistry<ServerConfiguration, StandardKernelsRegistry.KernelRestorationData>
                                     implements IKernelsRegistry<StandardKernelsRegistry.KernelRestorationData>,
                                                ApplicationContextAware {
    private static final Logger log = Logger.getLogger( StandardKernelsRegistry.class );

    private Map<UUID, ServerConfiguration> configurationsIndex;
    private Map<UUID, Server> serversIndex;
    private Map<UUID, IKernelService> servicesRegistry;
    
    private ApplicationContext context;
    
    public static class KernelRestorationData implements IRestorationData {
        private ServerConfiguration configuration;

        public KernelRestorationData( ServerConfiguration configuration ) {
            this.configuration = configuration;
        }

        public ServerConfiguration getConfiguration() {
            return configuration;
        }
    }

    public StandardKernelsRegistry( List<IRegistryInterceptor<ServerConfiguration, KernelRestorationData>> listeners ) {
        super(listeners);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    protected void init() {
        this.configurationsIndex = new HashMap<UUID, ServerConfiguration>();
        this.servicesRegistry = new HashMap<UUID, IKernelService>();
        this.serversIndex = new HashMap<UUID, Server>();
        
        super.init();
    }

    protected UUID createServer( ServerConfiguration configuration ) {
        try {
            Server server = new Server(ContextHandler.instance().getContext());
            server.setHost( configuration.getHost() );
            server.setPort( configuration.getPort() );
            server.setPath( configuration.getPath() );

            configuration.setId( UUID.randomUUID() );

            this.configurationsIndex.put( configuration.getId(), configuration );
            this.serversIndex.put( configuration.getId(), server );

            server.setConfigured(true);

            try {
                server.start();
            } catch ( DaemonException e ) {
                log.error( e.getMessage(), e );
                throw new IllegalStateException("Server start exception due to internal failures", e );
            }

            IKernelService service = this.servicesRegistry.get( configuration.getId() );
            if ( service == null ) {
                try {
                    this.servicesRegistry.put( configuration.getId(), service = server.getService() );
                } catch ( Throwable e ) {
                    throw new DaemonException("Unable to locate kernel server access point!", e );
                }
            }

            ServerConfiguration conf = configurationsIndex.get(configuration.getId());
            conf.setId( configuration.getId() );
            conf.setStarted( new Date() );
            conf.setState( server.getState() );

            this.onRecord(conf);

            return conf.getId();
        } catch ( DaemonException e ) {
            throw new IllegalStateException("Unable to start server due to internal fail", e);
        } catch ( ConfigException e ) {
            throw new IllegalStateException("Unable to start server due to config reading exception", e );
        }
    }

    @Override
    public UUID registerServer(ServerConfiguration configuration) {
        return this.createServer(configuration);
    }

    @Override
    public ServerConfiguration getConfiguration(UUID serverId) {
        return this.configurationsIndex.get(serverId);
    }

    @Override
    public boolean removeServer(UUID serverId) {
        Server server = this.serversIndex.get(serverId);

        try {
            server.stop();
        } catch (DaemonException e ) {
            log.error("Unable to stop server due to internal exception", e );
            return false;
        }

        this.configurationsIndex.remove(serverId);
        this.serversIndex.remove(serverId);
        this.servicesRegistry.remove(serverId);

        return true;
    }

    @Override
    public IKernelService getService( UUID serverId ) {
        return this.servicesRegistry.get(serverId);
    }

    @Override
    public Collection<ServerConfiguration> list() {
        return this.configurationsIndex.values();
    }

    @Override
    public void onRestore(KernelRestorationData item) {
        this.registerServer(item.getConfiguration());
    }

    @Override
    public KernelRestorationData prepareRestoration( ServerConfiguration configuration ) {
        return new KernelRestorationData(configuration);
    }
}
