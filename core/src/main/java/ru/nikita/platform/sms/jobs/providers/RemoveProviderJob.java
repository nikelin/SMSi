package ru.nikita.platform.sms.jobs.providers;

import com.redshape.daemon.jobs.AbstractJob;

import java.util.Date;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveProviderJob extends AbstractJob {

    private UUID providerId;
    private Date updated;

    public UUID getProviderId() {
        return providerId;
    }

    public void setProviderId(UUID providerId) {
        this.providerId = providerId;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
