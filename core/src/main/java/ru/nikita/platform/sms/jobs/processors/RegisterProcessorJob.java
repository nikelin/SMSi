package ru.nikita.platform.sms.jobs.processors;

import com.redshape.daemon.jobs.AbstractJob;
import ru.nikita.platform.sms.processors.IProcessorModel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterProcessorJob extends AbstractJob {

    private IProcessorModel model;
    private Date updated;

    public RegisterProcessorJob() {
        super();
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public IProcessorModel getModel() {
        return model;
    }

    public void setModel(IProcessorModel model) {
        this.model = model;
    }
}
