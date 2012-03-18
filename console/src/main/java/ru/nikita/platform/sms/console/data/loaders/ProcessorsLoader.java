package ru.nikita.platform.sms.console.data.loaders;

import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIRegistry;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.common.JarHelper;
import ru.nikita.platform.sms.console.App;
import ru.nikita.platform.sms.console.data.records.Processor;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.console.data.stores.ServersStore;
import ru.nikita.platform.sms.processors.IProcessorModel;
import ru.nikita.platform.sms.services.IKernelService;
import ru.nikita.platform.sms.services.IManagerService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarFile;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/16/11
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorsLoader extends AbstractDataLoader<Processor> {
    private static final Logger log = Logger.getLogger(ProcessorsLoader.class);

    @Override
    protected Collection<Processor> doLoad() throws LoaderException {
        IManagerService service = UIRegistry.get(App.Attributes.Manager);
        if ( service == null ) {
            throw new LoaderException("Manager service not established");
        }

        try {
            List<Processor> result = new ArrayList<Processor>();
            for ( Server server : UIRegistry.getStoresManager().getStore(ServersStore.class).getList() ) {
                for ( UUID modelId : service.getProcessorsList(server.getId() ) ) {
                    result.add( Processor.fromModel( service.getProcessor(server.getId(), modelId) ) );
                }
            }

            return result;
        } catch ( RemoteException e ) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        } catch ( InstantiationException e ) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
