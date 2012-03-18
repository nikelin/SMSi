package ru.nikita.platform.sms.handlers;

import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.jobs.processors.BindProcessorJob;
import ru.nikita.platform.sms.bind.IBindedPairsRegistry;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class BindProcessorHandler extends AbstractJobHandler<BindProcessorJob, IJobResult> {

    @Autowired( required = true )
    private IBindedPairsRegistry registry;


    public IBindedPairsRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(IBindedPairsRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected IJobResult createJobResult(UUID jobId) {
        return new JobResult(jobId);
    }

    @Override
    public IJobResult handle(BindProcessorJob job) throws HandlingException {
        this.getRegistry().registerProcessor( job.getBinding() );

        return this.createJobResult(null);
    }

    @Override
    public void cancel() throws HandlingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
