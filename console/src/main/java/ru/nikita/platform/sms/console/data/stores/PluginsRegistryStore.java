package ru.nikita.platform.sms.console.data.stores;

import com.redshape.ui.data.IModelType;
import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;
import ru.nikita.platform.sms.console.data.records.PluginsRegistry;
import ru.nikita.platform.sms.console.data.records.PluginsRegistryModel;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class PluginsRegistryStore extends ListStore<PluginsRegistry> {

    public PluginsRegistryStore() {
        this(null);
    }

    public PluginsRegistryStore(IDataLoader<PluginsRegistry> pluginsRegistryStoreIDataLoader) {
        super(new PluginsRegistryModel(), pluginsRegistryStoreIDataLoader);
    }

}
