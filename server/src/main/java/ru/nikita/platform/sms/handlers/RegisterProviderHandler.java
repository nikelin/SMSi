package ru.nikita.platform.sms.handlers;

import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.jobs.providers.RegisterProviderJob;
import ru.nikita.platform.sms.providers.IProvidersRegistry;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterProviderHandler extends AbstractJobHandler<RegisterProviderJob, IJobResult> {
    @Autowired( required = true )
    private IProvidersRegistry registry;

    public IProvidersRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(IProvidersRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected IJobResult createJobResult(UUID jobId) {
        return new JobResult(jobId);
    }

    @Override
    public IJobResult handle(RegisterProviderJob job) throws HandlingException {
        try {
            this.getRegistry().registerProvider( job.getProcessor(), this.getRegistry().createProvider(job.getProcessor()) );
    
            return this.createJobResult(null);
        } catch ( Throwable e ) {
            throw new HandlingException( e.getMessage(), e );
        }
    }

    @Override
    public void cancel() throws HandlingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
