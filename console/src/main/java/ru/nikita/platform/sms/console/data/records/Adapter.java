package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelData;
import ru.nikita.platform.sms.providers.adapters.AdapterAttribute;
import ru.nikita.platform.sms.providers.adapters.IAdapterModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/9/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Adapter extends AbstractModelData {

    public Adapter() {
        super();
        
        this.set( AdapterModel.ATTRIBUTES, new HashMap<AdapterAttribute, Object>());
    }

    public void setHost( String host ) {
        this.set( AdapterModel.HOST, host );
    }
    
    public String getHost() {
        return this.get( AdapterModel.HOST );
    }
    
    public void setPort( Integer port ) {
        this.set( AdapterModel.PORT, port );
    }
    
    public Integer getPort() {
        return this.get( AdapterModel.PORT );
    }
    
    public void setClientId( String clientId ) {
        this.set( AdapterModel.CLIENT_ID, clientId );
    }
    
    public String getClientId() {
        return this.get( AdapterModel.CLIENT_ID );
    }

    public void setPassword( String password ) {
        this.set( AdapterModel.PASSWORD, password );
    }
    
    public String getPassword() {
        return this.get( AdapterModel.PASSWORD );
    }
    
    public IAdapterModel toModel() throws URISyntaxException {
        IAdapterModel model = new ru.nikita.platform.sms.providers.adapters.AdapterModel();
        model.setURI(new URI("", "", this.getHost(), this.getPort(), "", "", ""));
        model.setAttribute( AdapterAttribute.LOGIN, this.getClientId() );
        model.setAttribute( AdapterAttribute.PASSWORD, this.getPassword() );
        model.setAttribute( AdapterAttribute.CLIENT_ID, this.getClientId() );

        return model;
    }
    
    public static Adapter fromModel( IAdapterModel model ) {
        Adapter adapter = new Adapter();
        adapter.setClientId( model.<String>getAttribute(AdapterAttribute.CLIENT_ID) );
        adapter.setHost( model.getURL().getHost() );
        adapter.setPassword( model.<String>getAttribute( AdapterAttribute.PASSWORD ) );
        adapter.setPort( model.getURL().getPort() );

        return adapter;
    }

    @Override
    public int hashCode() {
        return ( this.getHost() + this.getPort() ).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) { return false; }
        
        if ( !( obj instanceof Adapter ) ) {
            return false;
        }

        return ((Adapter) obj).getHost().equals( this.getHost() )
            && ((Adapter) obj).getPort().equals( this.getPort() );
    }
}
