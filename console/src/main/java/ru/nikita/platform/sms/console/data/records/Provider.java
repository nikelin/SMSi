package ru.nikita.platform.sms.console.data.records;

import com.redshape.bindings.annotations.Bindable;
import com.redshape.bindings.types.BindableType;
import com.redshape.ui.data.AbstractModelData;
import ru.nikita.platform.sms.providers.adapters.*;
import ru.nikita.platform.sms.providers.IProviderModel;
import ru.nikita.platform.sms.providers.ProtocolType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/9/11
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Provider extends AbstractModelData {

    public Provider() {
        super();
    }
    
    public UUID getId() {
        return this.get( ProviderModel.ID );
    }
    
    public void setId( UUID id ) {
        this.set( ProviderModel.ID, id );
    }
    
    public Server getServer() {
        return this.get( ProviderModel.SERVER );
    }
    
    public void setServer( Server server ) {
        this.set( ProviderModel.SERVER, server );
    }
    
    public void setProtocol( ProtocolType type ) {
        this.set( ProviderModel.PROTOCOL, type );
    }
    
    public ProtocolType getProtocol() {
        return this.get( ProviderModel.PROTOCOL );
    }

    @Bindable( name = "Title" )
    public String getTitle() {
        return this.get( ProviderModel.TITLE );
    }

    public void setTitle(String title) {
        this.set( ProviderModel.TITLE, title );
    }

    @Bindable( name = "Name" )
    public String getName() {
        return this.get( ProviderModel.NAME );
    }

    public void setName(String name) {
        this.set( ProviderModel.NAME, name );
    }

    @Bindable( name = "Description" )
    public String getDescription() {
        return this.get( ProviderModel.DESCRIPTION );
    }

    public void setDescription(String description) {
        this.set( ProviderModel.DESCRIPTION, description );
    }

    @Bindable( name = "Adapter settings", type = BindableType.COMPOSITE, targetType = Adapter.class )
    public Adapter getAdapter() {
        return this.get( ProviderModel.ADAPTER );
    }

    public void setAdapter(Adapter adapter) {
        this.set( ProviderModel.ADAPTER, adapter );
    }
    
    public IProviderModel toModel() throws URISyntaxException {
        IProviderModel model = new ru.nikita.platform.sms.providers.ProviderModel() ;
        model.setId( this.getId() );
        IAdapterModel adapterModel = new ru.nikita.platform.sms.providers.adapters.AdapterModel();

        model.setProtocolType( this.getProtocol() );
        adapterModel.setAttribute(AdapterAttribute.UUID, this.getId());
        adapterModel.setAttribute( AdapterAttribute.CLIENT_ID, this.getAdapter().getClientId() );
        adapterModel.setAttribute( AdapterAttribute.PASSWORD, this.getAdapter().getPassword() );
        adapterModel.setURI(
            new URI( this.getProtocol().scheme(), "", this.getAdapter().getHost(),
                this.getAdapter().getPort(), "", "", "" )
        );

        model.setAdapterModel(  adapterModel );
        model.setDescription( this.getDescription() );
        model.setName( this.getName() );
        model.setTitle( this.getTitle() );

        return model;
    }
    
    public static Provider fromModel( IProviderModel model ) {
        Provider provider = new Provider();
        provider.setId( model.getId() );
        provider.setName( model.getName() );
        provider.setDescription( model.getDescription() );
        provider.setAdapter( Adapter.fromModel( model.getAdapterModel() ) );
        provider.setProtocol( model.getProtocolType() );
        /**
         * @TODO Add server assignation
         */

        return provider;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) {
            return false;
        }

        if ( !( obj instanceof Provider ) ) {
            return false;
        }

        return ((Provider) obj).getName().equals( this.getName() );
    }
}
