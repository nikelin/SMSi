package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PluginsRegistryModel extends AbstractModelType {
    public static final String URI = "uri";
    public static final String NAME = "name";
    
    public PluginsRegistryModel() {
        super( PluginsRegistry.class );

        this.addField(NAME)
            .setType(String.class)
            .setTitle("Name");

        this.addField(URI)
            .setType(String.class)
            .setTitle("Address");
    }

    @Override
    public IModelData createRecord() {
        return new PluginsRegistry();
    }
}
