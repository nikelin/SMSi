package ru.nikita.platform.sms.console.data.stores;

import com.redshape.ui.data.IModelType;
import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.console.data.records.ServerModel;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServersStore extends ListStore<Server> {

    public ServersStore() {
        this(null);
    }
    
    public ServersStore(IDataLoader<Server> loader) {
        super(new ServerModel(), loader);
    }
}
