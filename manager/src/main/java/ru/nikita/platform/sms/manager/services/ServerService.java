package ru.nikita.platform.sms.manager.services;

import com.redshape.daemon.DaemonState;
import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.utils.Commons;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import ru.nikita.platform.sms.registry.IKernelsRegistry;
import ru.nikita.platform.sms.processors.IProcessorModel;
import ru.nikita.platform.sms.providers.IProviderModel;
import ru.nikita.platform.sms.services.IKernelService;
import ru.nikita.platform.sms.services.IManagerService;
import ru.nikita.platform.sms.services.data.ServerConfiguration;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerService implements IManagerService {
    private static final Logger log = Logger.getLogger(ServerService.class);
    private String serviceName;

    private ApplicationContext context;

    public ServerService( ApplicationContext context, String serviceName ) {
        this.context = context;
        this.serviceName = serviceName;
    }

    protected ApplicationContext getContext() {
        return context;
    }

    protected IKernelsRegistry<?> getKernelsRegistry() {
        return this.getContext().getBean(IKernelsRegistry.class);
    }

    @Override
    public UUID start(ServerConfiguration configuration) throws RemoteException {
        try {
            return this.getKernelsRegistry().registerServer(configuration);
        } catch ( Throwable e ) {
            throw new RemoteException( e.getMessage(), e );
        }
    }

    @Override
    public boolean stop(UUID serverId) throws RemoteException {
        try {
            return this.getKernelsRegistry().removeServer(serverId);
        } catch ( Throwable e ) {
            throw new RemoteException( e.getMessage(), e);
        }
    }

    @Override
    public IJobResult scheduleJob( UUID serverId, IJob job) throws RemoteException {
        IKernelService service = this.getKernelsRegistry().getService(serverId);
        if ( service == null ) {
            throw new RemoteException("Server with a given id not presents in registry!");
        }
        
        return service.scheduleJob(job);
    }

    @Override
    public List<ServerConfiguration> list() throws RemoteException {
        return Commons.list(
                this.getKernelsRegistry().list().toArray(new ServerConfiguration[this.getKernelsRegistry().list().size()])
        );
    }

    @Override
    public DaemonState state() throws RemoteException {
        return DaemonState.STARTED;
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    @Override
    public String getServiceName() throws RemoteException {
        return this.serviceName;
    }

    @Override
    public IProviderModel getProvider(UUID serverId, UUID id) throws RemoteException {
        IKernelService server = this.getKernelsRegistry().getService(serverId);
        if ( server == null ) {
            throw new RemoteException("Server with a given id not presents in registry!");
        }

        return server.getProvider(id);
    }

    @Override
    public IProcessorModel getProcessor(UUID serverId, UUID id) throws RemoteException {
        IKernelService server = this.getKernelsRegistry().getService(serverId);
        if ( server == null ) {
            throw new RemoteException("Server with a given id not presents in registry!");
        }

        return server.getProcessor(id);
    }

    @Override
    public List<UUID> getProcessorsList(UUID serverId) throws RemoteException {
        IKernelService server = this.getKernelsRegistry().getService(serverId);
        if ( server == null ) {
            throw new RemoteException("Server with a given id not presents in registry!");
        }

        return server.getProcessorsList();
    }

    @Override
    public List<UUID> getProvidersList( UUID serverId ) throws RemoteException {
        IKernelService server = this.getKernelsRegistry().getService(serverId);
        if ( server == null ) {
            throw new RemoteException("Server with a given id not presents in registry!");
        }

        return server.getProvidersList();
    }
}
