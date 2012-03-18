package ru.nikita.platform.sms.providers;

import ru.nikita.platform.sms.registry.IRegistry;
import ru.nikita.platform.sms.registry.IRestorationData;

import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IProvidersRegistry<T extends IRestorationData> extends IRegistry<IProvider, T> {
    
    public IProviderModel findByName( String name );
    
    public IProviderModel getModel( IProvider provider );
    
    public IProvider createProvider( IProviderModel model );
    
    public void registerProvider( IProviderModel model, IProvider provider );
    
    public boolean isRegistered( IProviderModel model );
    
    public IProvider getProvider( IProviderModel model );
    
    public IProvider getProvider( UUID providerId );
    
    public void removeProvider( UUID providerId );
    
    public void removeProvider( IProviderModel model );
    
    public List<IProvider> list();
    
    public List<IProviderModel> getModels();
    
}
