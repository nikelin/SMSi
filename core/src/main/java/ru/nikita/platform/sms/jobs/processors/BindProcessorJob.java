package ru.nikita.platform.sms.jobs.processors;

import com.redshape.daemon.jobs.AbstractJob;
import ru.nikita.platform.sms.bind.IBindedPair;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class BindProcessorJob extends AbstractJob {
    private Date updated;

    private IBindedPair binding;

    public BindProcessorJob(IBindedPair binding) {
        this.binding = binding;
    }

    public IBindedPair getBinding() {
        return binding;
    }

    public void setBinding(IBindedPair binding) {
        this.binding = binding;
    }

    @Override
    public void setUpdated(Date date) {
        this.updated = date;
    }

    @Override
    public Date getUpdated() {
        return this.updated;
    }
}
