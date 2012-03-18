package ru.nikita.platform.sms.console.data.loaders;

import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIRegistry;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.console.App;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.services.IManagerService;
import ru.nikita.platform.sms.services.data.ServerConfiguration;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServersLoader extends AbstractDataLoader<Server> {
    private static final Logger log = Logger.getLogger(ServersLoader.class);
    
    @Override
    protected Collection<Server> doLoad() throws LoaderException {
        IManagerService service = UIRegistry.get(App.Attributes.Manager);
        if ( service == null ) {
            return new ArrayList<Server>();
        }
        
        List<ServerConfiguration> configurations = new ArrayList<ServerConfiguration>();
        try {
            configurations.addAll( service.list() );
        } catch ( RemoteException e ) {
            log.error("Unable to load configurations list from kernels manager", e);
        }
        
        List<Server> result = new ArrayList<>( configurations.size() );
        for ( ServerConfiguration configuration : configurations ) {
            Server server = new Server();
            server.setId( configuration.getId() );
            server.setHost( configuration.getHost() );
            server.setPort( configuration.getPort() );
            server.setPath( configuration.getPath() );
            server.setLastPing( configuration.getLastPingDate() );
            server.setStartedOn( configuration.getStarted() );
            server.setName( configuration.getName() );
            
            result.add( server );
        }

        return result;
    }

}
