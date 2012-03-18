package ru.nikita.platform.sms.plugins.services;

import com.redshape.daemon.DaemonState;
import com.redshape.daemon.IRemoteService;
import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.services.execution.IExecutorDescriptor;
import com.redshape.daemon.services.IJobsDispatcherService;
import com.redshape.utils.events.AbstractEventDispatcher;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerService extends AbstractEventDispatcher implements IJobsDispatcherService<IJob, IJobResult>,
                                                                    IRemoteService {
    private String serviceName;
    
    public ServerService( String serviceName ) {
        super();
        
        this.serviceName = serviceName;
    }

    @Override
    public String getServiceName() throws RemoteException {
        return this.serviceName;
    }

    @Override
    public Map<UUID, IExecutorDescriptor> getConnectedExecutors() throws RemoteException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<UUID, IJob> getJobs(UUID agentId) throws RemoteException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IJobResult scheduleJob(IJob job) throws RemoteException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IJobResult scheduleJob(IJob job, Date date) throws RemoteException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cancelJob(UUID job) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isComplete(UUID job) throws RemoteException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isFailed(UUID job) throws RemoteException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DaemonState state() throws RemoteException {
        return DaemonState.STARTED;
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }
}
