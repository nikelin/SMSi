package ru.nikita.platform.sms.jobs.providers;

import com.redshape.daemon.jobs.AbstractJob;

import java.util.Date;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class StopProviderJob extends AbstractJob {
    private UUID providerId;
    private Date updated;

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public void setProviderId(UUID providerId) {
        this.providerId = providerId;
    }
}
