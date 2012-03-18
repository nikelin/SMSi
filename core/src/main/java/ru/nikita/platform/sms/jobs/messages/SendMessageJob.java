package ru.nikita.platform.sms.jobs.messages;

import com.redshape.daemon.jobs.AbstractJob;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 7:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class SendMessageJob extends AbstractJob {
    private Date updated;
    private String phone;
    private String text;

    public SendMessageJob() {
        super();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
