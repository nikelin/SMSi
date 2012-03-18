package ru.nikita.platform.sms.jobs.processors;

import com.redshape.daemon.jobs.AbstractJob;

import java.util.Date;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/23/11
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveProcessorJob extends AbstractJob {

    private Date updated;
    private UUID processorId;

    public RemoveProcessorJob(UUID processorId) {
        this.processorId = processorId;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getUpdated() {
        return updated;
    }

    public UUID getProcessorId() {
        return processorId;
    }

}
