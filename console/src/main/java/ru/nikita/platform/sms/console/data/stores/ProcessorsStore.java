package ru.nikita.platform.sms.console.data.stores;

import com.redshape.ui.data.IModelType;
import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;
import ru.nikita.platform.sms.console.data.records.Processor;
import ru.nikita.platform.sms.console.data.records.ProcessorModel;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorsStore extends ListStore<Processor> {

    public ProcessorsStore() {
        this(null);
    }

    public ProcessorsStore(IDataLoader<Processor> loader) {
        super( new ProcessorModel(), loader );
    }
}
