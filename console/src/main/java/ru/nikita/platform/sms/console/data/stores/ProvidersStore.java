package ru.nikita.platform.sms.console.data.stores;

import com.redshape.ui.data.IModelType;
import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;
import ru.nikita.platform.sms.console.data.records.Provider;
import ru.nikita.platform.sms.console.data.records.ProviderModel;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/15/11
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProvidersStore extends ListStore<Provider> {

    public ProvidersStore() {
        this(null);
    }

    public ProvidersStore( IDataLoader<Provider> loader ) {
        super(new ProviderModel(), loader);
    }

}
