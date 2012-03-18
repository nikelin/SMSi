package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerModel extends AbstractModelType {
    public static final String ID = "id";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String STATE = "state";
    public static final String STARTED = "started";
    public static final String LAST_PINGED = "lastPinged";

    public ServerModel() {
        super(Server.class);

        this.addField(ID)
            .setTitle("ID")
            .setType(String.class);
        this.addField(STATE)
            .setTitle("State")
            .setType(String.class);
        this.addField(NAME)
            .setTitle("Name")
            .setType(String.class);
        this.addField(HOST)
            .setTitle("Host")
            .setType(String.class);
        this.addField(PORT)
            .setTitle("Port")
            .setType(Integer.class);
        this.addField(PATH)
            .setTitle("Path")
            .setType(String.class);
        this.addField(STARTED)
            .setTitle("Started")
            .setType(Date.class)
            .setFormat("timestamp");
        this.addField(LAST_PINGED)
            .setTitle("Ping")
            .setType(Date.class)
            .setFormat("timestamp");
    }

    @Override
    public IModelData createRecord() {
        return new Server();
    }
}
