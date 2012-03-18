package ru.nikita.platform.sms.console.data.stores;

import com.redshape.ui.data.IModelType;
import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.stores.ListStore;
import ru.nikita.platform.sms.console.data.records.Template;
import ru.nikita.platform.sms.console.data.records.TemplateModel;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemplatesStore extends ListStore<Template> {

    public TemplatesStore() {
        this(null);
    }

    public TemplatesStore(IDataLoader<Template> loader) {
        super( new TemplateModel(), loader);
    }

}
