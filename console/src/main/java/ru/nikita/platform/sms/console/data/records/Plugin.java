package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelData;

import java.util.Date;

import static ru.nikita.platform.sms.console.data.records.PluginModel.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Plugin extends AbstractModelData {

    public Plugin() {
        super();
    }
    
    public String getName() {
        return this.get(NAME);
    }
    
    public void setName( String name ) {
        this.set( NAME, name );
    }
    
    public void setEntry( String path ) {
        this.set( ENTRY, path );
    }
    
    public String getEntry() {
        return this.get( ENTRY );
    }
    
    public void setPath( String path ) {
        this.set( PATH, path );
    }
    
    public String getPath() {
        return this.get(PATH);
    }
    
    public void setRegistry( String registry ) {
        this.set(REGISTRY, registry);
    }
    
    public String getRegistry() {
        return this.get(REGISTRY);
    }
    
    public Date getUpdated() {
        return this.get(UPDATED);
    }
    
    public void setUpdated( Date date ) {
        this.set( UPDATED, date );
    }
    

}
