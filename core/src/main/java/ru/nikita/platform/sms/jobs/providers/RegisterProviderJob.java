package ru.nikita.platform.sms.jobs.providers;

import com.redshape.daemon.jobs.AbstractJob;
import ru.nikita.platform.sms.providers.IProviderModel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 10:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterProviderJob extends AbstractJob {

    private IProviderModel processor;
    private Date updated;

    public RegisterProviderJob(IProviderModel processor) {
        this.processor = processor;
    }

    public IProviderModel getProcessor() {
        return processor;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
