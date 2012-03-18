package ru.nikita.platform.sms.console.data.loaders;

import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIRegistry;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.console.App;
import ru.nikita.platform.sms.console.data.records.Provider;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.console.data.stores.ServersStore;
import ru.nikita.platform.sms.services.IManagerService;
import ru.nikita.platform.sms.services.data.ServerConfiguration;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/16/11
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProvidersLoader extends AbstractDataLoader<Provider> {
    private static final Logger log = Logger.getLogger(ProvidersLoader.class);
    
    @Override
    protected Collection<Provider> doLoad() throws LoaderException {
        IManagerService service = UIRegistry.get(App.Attributes.Manager);
        if ( service == null ) {
            log.info("Service not initialized");
            return new ArrayList<Provider>();
        }

        log.info("Providers loader");
        List<Provider> result = new ArrayList<Provider>();
        try {
            for ( ServerConfiguration server : service.list() )  {
                log.info( "Server:" + server.getId() );
                for (UUID providerId : service.getProvidersList(server.getId()) ) {
                    log.info("Provider ID: " + providerId );
                    Provider provider = Provider.fromModel(service.getProvider(server.getId(), providerId));
                    Server serverModel = new Server();
                    serverModel.setId( server.getId() );
                    provider.setServer( serverModel );
                    result.add( provider );
                }
            }
        } catch ( RemoteException e ) {
            log.error("Remote server interaction failed", e);
        }
            
        
        return result;
    }
}
