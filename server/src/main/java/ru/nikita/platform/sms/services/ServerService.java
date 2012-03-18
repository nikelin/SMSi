package ru.nikita.platform.sms.services;

import com.redshape.daemon.DaemonState;
import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.managers.IJobsManager;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.services.execution.IExecutorDescriptor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.processors.IProcessorModel;
import ru.nikita.platform.sms.processors.IProcessorsRegistry;
import ru.nikita.platform.sms.providers.IProviderModel;
import ru.nikita.platform.sms.providers.IProvidersRegistry;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerService implements IKernelService {
    private static final Logger log = Logger.getLogger(ServerService.class);

    @Autowired( required = true )
    private IJobsManager jobsManager;

    @Autowired( required = true )
    private IProvidersRegistry providersRegistry;

    @Autowired( required = true )
    private IProcessorsRegistry processorsRegistry;
    
    private ExecutorService executor;
    
    private Map<UUID, IJob> jobs = new HashMap<UUID, IJob>();
    
    private String serviceName;

    public ServerService( IJobsManager manager, String serviceName ) {
        this.jobsManager = manager;
        this.executor = Executors.newFixedThreadPool(10);
        this.serviceName = serviceName;
    }

    protected ExecutorService getThreadExecutor() {
        return this.executor;
    }

    public IProvidersRegistry<?> getProvidersRegistry() {
        return providersRegistry;
    }

    public void setProvidersRegistry(IProvidersRegistry<?> providersRegistry) {
        this.providersRegistry = providersRegistry;
    }

    public IProcessorsRegistry<?> getProcessorsRegistry() {
        return processorsRegistry;
    }

    public void setProcessorsRegistry(IProcessorsRegistry<?> processorsRegistry) {
        this.processorsRegistry = processorsRegistry;
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

    public IJobsManager getJobsManager() {
        return jobsManager;
    }

    public void setJobsManager(IJobsManager jobsManager) {
        this.jobsManager = jobsManager;
    }

    @Override
    public IProviderModel getProvider(UUID id) throws RemoteException {
        for ( IProviderModel model : this.getProvidersRegistry().getModels() ) {
            if ( model.getId().equals(id) ) {
                return model;
            }
        }

        return null;
    }

    @Override
    public IProcessorModel getProcessor(UUID id) throws RemoteException {
        for ( IProcessorModel model : this.getProcessorsRegistry().getModels() ) {
            if ( model.getId().equals(id) ) {
                return model;
            }
        }

        return null;
    }

    @Override
    public List<UUID> getProcessorsList() throws RemoteException {
        List<UUID> ids = new ArrayList<UUID>();
        for ( IProcessorModel model : this.getProcessorsRegistry().getModels() ) {
            ids.add( model.getId() );
        }

        return ids;
    }

    @Override
    public List<UUID> getProvidersList() throws RemoteException {
        List<UUID> ids = new ArrayList<UUID>();
        for ( IProviderModel model : this.getProvidersRegistry().getModels() ) {
            ids.add( model.getId() );
        }

        return ids;
    }

    @Override
    public Map<UUID, IExecutorDescriptor> getConnectedExecutors() throws RemoteException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<UUID, IJob> getJobs(UUID agentId) throws RemoteException {
        return this.jobs;
    }

    @Override
    public IJobResult scheduleJob(IJob job) throws RemoteException {
        UUID id = UUID.randomUUID();
        this.jobs.put( id, job );
        this.getThreadExecutor().submit(new ExecutionThread(job));

        /**
         * Fixme
         */
        return null;
    }

    @Override
    public IJobResult scheduleJob(IJob job, Date date) throws RemoteException {
        throw new RemoteException("Scheduling type not supported by implementation");
    }

    @Override
    public void cancelJob(UUID job) throws RemoteException {
        throw new RemoteException("Scheduling type not supported by implementation");
    }

    @Override
    public boolean isComplete(UUID job) throws RemoteException {
        throw new RemoteException("Scheduling type not supported by implementation");
    }

    @Override
    public boolean isFailed(UUID job) throws RemoteException {
        throw new RemoteException("Scheduling type not supported by implementation");
    }
    
    public class ExecutionThread implements Runnable {
        private IJob job;
        
        public ExecutionThread( IJob job ) {
            this.job = job;
        }

        @Override
        public void run() {                                         
            try {
                log.info( this.job == null );
                ServerService.this.getJobsManager().execute( this.job );
            } catch ( Throwable e ) {
                log.error( e.getMessage(), e );
            }
        }
    }
}
