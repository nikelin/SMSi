package ru.nikita.platform.sms.services;

import com.redshape.daemon.IRemoteService;
import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.services.IJobsDispatcherService;
import ru.nikita.platform.sms.conditions.ICondition;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.processors.IProcessorModel;
import ru.nikita.platform.sms.providers.IProviderModel;
import ru.nikita.platform.sms.services.data.ServerConfiguration;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IManagerService extends IRemoteService {

    public UUID start( ServerConfiguration configuration ) throws RemoteException;
    
    public boolean stop( UUID serverId ) throws RemoteException;

    public List<ServerConfiguration> list() throws RemoteException;
    
    public IJobResult scheduleJob( UUID serverId, IJob job ) throws RemoteException;

    public IProviderModel getProvider( UUID serverId, UUID id ) throws RemoteException;

    public IProcessorModel getProcessor( UUID serverId, UUID id ) throws RemoteException;
    
    public List<UUID> getProcessorsList( UUID serverId ) throws RemoteException;

    public List<UUID> getProvidersList( UUID serverId ) throws RemoteException;


}
