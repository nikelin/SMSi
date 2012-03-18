package ru.nikita.platform.sms.console.data.stores;

import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;
import ru.nikita.platform.sms.console.data.records.Binding;
import ru.nikita.platform.sms.console.data.records.BindingModel;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class BindingsStore extends ListStore<Binding> {

    public BindingsStore() {
        this(null);
    }

    public BindingsStore( IDataLoader<Binding> loader ) {
        super( new BindingModel(), loader );
    }

}
