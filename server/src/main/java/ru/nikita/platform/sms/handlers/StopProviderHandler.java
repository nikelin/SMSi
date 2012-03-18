package ru.nikita.platform.sms.handlers;

import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.jobs.providers.StopProviderJob;
import ru.nikita.platform.sms.providers.IProvider;
import ru.nikita.platform.sms.providers.IProvidersRegistry;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class StopProviderHandler extends AbstractJobHandler<StopProviderJob, IJobResult> {
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
    public IJobResult handle(StopProviderJob job) throws HandlingException {
        IProvider provider = this.getProvidersRegistry().getProvider( job.getProviderId() );
        if ( provider == null ) {
            throw new HandlingException("Provider not registered");
        }
        
        if ( !provider.isBind() ) {
            throw new HandlingException("Provider not binded");
        }
        
        try {
            provider.disconnect();
        } catch ( Throwable e ) {
            throw new HandlingException("Failed to stop provider", e );
        }

        return this.createJobResult(null);
    }

    @Override
    public void cancel() throws HandlingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
