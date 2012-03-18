package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class BindingModel extends AbstractModelType {
    public static final String ID = "id";
    public static final String CONDITION = "condition";
    public static final String TYPE = "type";
    public static final String PROVIDER = "provider";
    public static final String PROCESSOR = "processor";
    
    public BindingModel() {
        super(Binding.class);

        this.addField(ID)
            .setTitle("ID");
        this.addField(TYPE)
            .setTitle("Type");
        this.addField(PROCESSOR)
            .setTitle("Processor");
        this.addField(PROVIDER)
            .setTitle("Provider");
        this.addField(CONDITION)
            .setTitle("Condition");
    }

    @Override
    public IModelData createRecord() {
        return new Binding();
    }
}
