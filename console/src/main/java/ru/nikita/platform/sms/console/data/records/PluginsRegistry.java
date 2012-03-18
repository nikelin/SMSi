package ru.nikita.platform.sms.console.data.records;

import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.types.BindableType;
import com.redshape.ui.data.AbstractModelData;
import com.redshape.validators.annotations.Validator;
import com.redshape.validators.impl.common.NotEmptyValidator;

import java.net.URI;
import static ru.nikita.platform.sms.console.data.records.PluginsRegistryModel.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PluginsRegistry extends AbstractModelData {
    
    public PluginsRegistry() {
        super();
    }

    public String getName() {
        return this.get(NAME);
    }

    @Bindable( name = "Name" )
    @Validator(NotEmptyValidator.class)
    public void setName( String name ) {
        this.set(NAME, name);
    }
    
    public URI getURI() {
        return this.get( URI );
    }

    @Bindable( name = "URI", type = BindableType.STRING )
    @Validator(NotEmptyValidator.class)
    public void setURI( URI uri ) {
        this.set( URI, uri );
    }
    
}
