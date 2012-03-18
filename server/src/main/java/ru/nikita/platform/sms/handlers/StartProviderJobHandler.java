package ru.nikita.platform.sms.handlers;

import com.redshape.daemon.jobs.AbstractJob;
import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.jobs.providers.StartProviderJob;
import ru.nikita.platform.sms.providers.IProvider;
import ru.nikita.platform.sms.providers.IProvidersRegistry;

import java.util.Date;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class StartProviderJobHandler extends AbstractJobHandler<StartProviderJob, IJobResult> {
    @Autowired( required = true )
    private IProvidersRegistry providersRegistry;

    public IProvidersRegistry getProvidersRegistry() {
        return providersRegistry;
    }

    public void setProvidersRegistry(IProvidersRegistry providersRegistry) {
        this.providersRegistry = providersRegistry;
    }

    @Override
    protected IJobResult createJobResult(UUID jobId) {
        return new JobResult(jobId);
    }

    @Override
    public IJobResult handle(StartProviderJob job) throws HandlingException {
        IProvider provider = this.getProvidersRegistry().getProvider(job.getProviderId());
        if ( provider == null ) {
            throw new HandlingException("Provider not registered");
        }
        
        if ( provider.isBind() ) {
            throw new HandlingException("Provider already binded!");
        }
        
        try {
            provider.bind();
        } catch ( Throwable e ) {
            throw new HandlingException( e.getMessage(), e );
        }
        
        return this.createJobResult(null);
    }

    @Override
    public void cancel() throws HandlingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
