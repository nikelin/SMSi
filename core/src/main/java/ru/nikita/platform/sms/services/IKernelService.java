package ru.nikita.platform.sms.services;

import com.redshape.daemon.IRemoteService;
import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.services.IJobsDispatcherService;
import ru.nikita.platform.sms.conditions.ICondition;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.messages.Message;
import ru.nikita.platform.sms.processors.IProcessorModel;
import ru.nikita.platform.sms.providers.IProviderModel;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/9/11
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IKernelService extends IJobsDispatcherService<IJob, IJobResult>, IRemoteService {

    public IProviderModel getProvider( UUID id ) throws RemoteException;

    public IProcessorModel getProcessor( UUID id ) throws RemoteException;
    
    public List<UUID> getProcessorsList() throws RemoteException;
    
    public List<UUID> getProvidersList() throws RemoteException;
    
}
