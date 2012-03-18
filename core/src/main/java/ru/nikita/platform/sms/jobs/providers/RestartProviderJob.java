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
public class RestartProviderJob extends AbstractJob {
    private Date updated;
    private UUID provider;

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public UUID getProvider() {
        return provider;
    }

    public void setProvider(UUID provider) {
        this.provider = provider;
    }
}
