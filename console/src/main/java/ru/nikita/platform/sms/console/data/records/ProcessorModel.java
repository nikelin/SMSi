package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorModel extends AbstractModelType {
    public static final String ID = "id";
    public static final String SERVER = "server";
    public static final String NAME = "name";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String ACTION_TYPE = "actionType";
    public static final String PARAMETERS = "parameters";
    
    public ProcessorModel() {
        super(Processor.class);

        this.addField(SERVER)
            .makeTransient(true);
        this.addField(ID)
            .setTitle("ID")
            .setType(String.class);
        this.addField(NAME)
            .setTitle("Name")
            .setType(String.class);
        this.addField(TITLE)
            .setTitle("Title")
            .setType(String.class);
        this.addField(DESCRIPTION)
            .setTitle("Description")
            .setType(String.class);
        this.addField(ACTION_TYPE)
            .setTitle("Action type")
            .setType(String.class);
        this.addField(PARAMETERS)
            .setTitle("Parameters")
            .setType(String.class);
    }

    @Override
    public IModelData createRecord() {
        return new Processor();
    }

}
