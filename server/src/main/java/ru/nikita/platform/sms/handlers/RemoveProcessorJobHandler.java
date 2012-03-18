package ru.nikita.platform.sms.handlers;

import com.redshape.daemon.jobs.handlers.AbstractJobHandler;
import com.redshape.daemon.jobs.handlers.HandlingException;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.daemon.jobs.result.JobResult;
import ru.nikita.platform.sms.jobs.processors.RemoveProcessorJob;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/23/11
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveProcessorJobHandler extends AbstractJobHandler<RemoveProcessorJob, IJobResult> {


    @Override
    protected IJobResult createJobResult(UUID jobId) {
        return new JobResult(jobId);
    }

    @Override
    public IJobResult handle(RemoveProcessorJob job) throws HandlingException {
        /**
         * @TODO
         */
        return this.createJobResult(null);
    }

    @Override
    public void cancel() throws HandlingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
