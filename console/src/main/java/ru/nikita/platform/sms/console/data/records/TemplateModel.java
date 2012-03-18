package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateModel extends AbstractModelType {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String TEXT = "text";
    
    public TemplateModel() {
        super(Template.class);
        
        this.addField(NAME)
            .setTitle("Name");
        this.addField(DESCRIPTION)
            .setTitle("Description");
        this.addField(TEXT)
            .setTitle("Text")
            .makeTransient(true);
    }

    @Override
    public IModelData createRecord() {
        return new Template();
    }
}
