package ru.nikita.platform.sms.providers;

import ru.nikita.platform.sms.providers.adapters.IAdapterModel;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IProviderModel extends Serializable {

    public UUID getId();

    public void setId( UUID id );

    public void setProtocolType( ProtocolType type );
    
    public ProtocolType getProtocolType();
    
    public void setName( String name );
    
    public String getName();
    
    public void setTitle( String title );
    
    public String getTitle();
    
    public void setDescription( String description );
    
    public String getDescription();
    
    public void setAdapterModel( IAdapterModel adapter );

    public IAdapterModel getAdapterModel();
    
}
