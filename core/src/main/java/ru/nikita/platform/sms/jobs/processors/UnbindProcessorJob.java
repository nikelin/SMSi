package ru.nikita.platform.sms.jobs.processors;

import com.redshape.daemon.jobs.AbstractJob;

import java.util.Date;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnbindProcessorJob extends AbstractJob {
    private Date updated;
    private UUID processorId;

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public UUID getProcessorId() {
        return processorId;
    }

    public void setProcessorId(UUID processorId) {
        this.processorId = processorId;
    }
}
