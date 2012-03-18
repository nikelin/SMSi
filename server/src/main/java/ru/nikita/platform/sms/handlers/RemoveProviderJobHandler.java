package ru.nikita.platform.sms.handlers;

import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.jobs.providers.RemoveProviderJob;
import ru.nikita.platform.sms.providers.IProvider;
import ru.nikita.platform.sms.providers.IProvidersRegistry;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveProviderJobHandler extends AbstractJobHandler<RemoveProviderJob, IJobResult> {

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
    public IJobResult handle(RemoveProviderJob job) throws HandlingException {
        this.getRegistry().removeProvider( job.getProviderId() );

        return this.createJobResult(null);
    }

    @Override
    public void cancel() throws HandlingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
