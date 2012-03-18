package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class PluginModel extends AbstractModelType {

    public static final String NAME = "name";
    public static final String REGISTRY = "registry";
    public static final String PATH = "path";
    public static final String TYPE = "type";
    public static final String UPDATED = "updated";
    public static final String ENTRY = "entry";

    public PluginModel() {
        super(Plugin.class);

        this.addField(NAME);
        this.addField(REGISTRY);
        this.addField(PATH);
        this.addField(TYPE);
        this.addField(UPDATED);
        this.addField(ENTRY);
    }

    @Override
    public IModelData createRecord() {
        return new Plugin();
    }
}
