package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/9/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdapterModel extends AbstractModelType {
    public static final String ATTRIBUTES = "attributes";
    public static final String PASSWORD = "password";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String CLIENT_ID = "clientId";
    
    public AdapterModel() {
        super(Adapter.class);

        this.addField(HOST)
            .setTitle("Host")
            .setType(String.class);
        this.addField(PORT)
            .setTitle("Port")
            .setType(Integer.class);
        this.addField(ATTRIBUTES)
            .setTitle("Attributes")
            .setType(Map.class);
        this.addField(CLIENT_ID)
            .setTitle("Client")
            .setType(String.class);
        this.addField(PASSWORD)
            .setType(String.class);
    }

    public IModelData createRecord() {
        return new Adapter();
    }
}
