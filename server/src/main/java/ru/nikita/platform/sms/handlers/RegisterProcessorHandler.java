package ru.nikita.platform.sms.handlers;

import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.jobs.processors.RegisterProcessorJob;
import ru.nikita.platform.sms.processors.IProcessorsRegistry;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterProcessorHandler extends AbstractJobHandler<RegisterProcessorJob, IJobResult> {

    @Autowired( required = true )
    private IProcessorsRegistry registry;

    public IProcessorsRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(IProcessorsRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected IJobResult createJobResult(UUID jobId) {
        return new JobResult(jobId);
    }

    @Override
    public IJobResult handle(RegisterProcessorJob job) throws HandlingException {
        job.getModel().setId( UUID.randomUUID() );
        this.getRegistry().registerProcessor( job.getModel(), this.getRegistry().createProcessor(job.getModel()) );

        return this.createJobResult(null);
    }

    @Override
    public void cancel() throws HandlingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
