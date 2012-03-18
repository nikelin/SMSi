package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;
import ru.nikita.platform.sms.providers.IProviderModel;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/9/11
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProviderModel extends AbstractModelType {

    public static final String ID = "id";
    public static final String SERVER = "server";
    public static final String PROTOCOL = "protocol";
    public static final String NAME = "name";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String ADAPTER = "adapter";
    public static final String NUMBERS = "numbers";
    
    public ProviderModel() {
        super(Provider.class);

        this.addField( ID )
            .setTitle("ID")
            .setType(String.class);
        this.addField( SERVER )
            .setTitle("Server")
            .setType( String.class );
        this.addField(PROTOCOL)
            .setTitle("Protocol")
            .setType(String.class);
        this.addField(NAME)
            .setTitle("Name")
            .setType(String.class);
        this.addField(TITLE)
            .setType(String.class)
            .setTitle("Title");
        this.addField(ADAPTER)
            .setType(Adapter.class)
            .makeTransient(true);
        this.addField(DESCRIPTION)
            .setTitle("Description")
            .setType(String.class);
        this.addField(NUMBERS);
    }

    public IModelData createRecord() {
        return new Provider();
    }
}
