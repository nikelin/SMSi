package ru.nikita.platform.sms.console.data.records;

import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.annotations.BindableAttributes;
import com.redshape.ui.data.AbstractModelData;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Template extends AbstractModelData {

    public Template() {
        super();
    }

    public String getDescription() {
        return this.get( TemplateModel.DESCRIPTION );
    }

    @Bindable( name = "Description", attributes = {BindableAttributes.LONGTEXT })
    public void setDescription( String description ) {
        this.set( TemplateModel.DESCRIPTION, description );
    }

    public String getName() {
        return this.get( TemplateModel.NAME );
    }

    @Bindable( name = "Name" )
    public void setName( String name ) {
        this.set( TemplateModel.NAME, name );
    }
    
    public String getText() {
        return this.get( TemplateModel.TEXT );
    }

    @Bindable( name = "Text", attributes = {BindableAttributes.LONGTEXT})
    public void setText( String text ) {
        this.set( TemplateModel.TEXT, text );
    }
    
}
