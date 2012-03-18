package ru.nikita.platform.sms.providers;

import com.redshape.utils.Commons;
import com.redshape.utils.config.IConfig;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.registry.AbstractRegistry;
import ru.nikita.platform.sms.registry.IRestorationData;
import ru.nikita.platform.sms.utils.ContextHandler;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class StandardProvidersRegistry extends AbstractRegistry<IProvider, StandardProvidersRegistry.ProviderRestorationData>
                                    implements IProvidersRegistry<StandardProvidersRegistry.ProviderRestorationData> {
    public static final class ProviderRestorationData implements IRestorationData {
        private IProviderModel model;

        public ProviderRestorationData(IProviderModel model) {
            this.model = model;
        }

        public IProviderModel getModel() {
            return model;
        }

    }

    @Autowired( required = true )
    private IConfig config;

    private Map<IProviderModel, IProvider> providers;

    public StandardProvidersRegistry( List<IRegistryInterceptor<IProvider, ProviderRestorationData>> interceptors ) {
        super( interceptors );
    }

    @Override
    protected void init() {
        this.providers = new HashMap<IProviderModel, IProvider>();
        
        super.init();
    }

    public void setConfig( IConfig config ) {
        this.config = config;
    }
    
    public IConfig getConfig() {
        return config;
    }
    @Override
    public IProviderModel getModel(IProvider provider) {
        for ( Map.Entry<IProviderModel, IProvider> entry : this.providers.entrySet() ) {
            if ( entry.getValue() == provider ) {
                return entry.getKey();
            }
        }

        return null;
    }

    @Override
    public IProvider getProvider(UUID providerId) {
        for ( IProviderModel model : this.providers.keySet() ) {
            if ( model.getId().equals(providerId) ) {
                return this.getProvider(model);
            }
        }

        return null;
    }

    @Override
    public void removeProvider(UUID providerId) {
        IProviderModel target = null;
        for ( IProviderModel model : this.providers.keySet() ) {
            if ( model.getId().equals( providerId ) ) {
                target = model;
                break;
            }
        }
        
        if ( target == null ) {
            return;
        }
        
        this.providers.remove( target );
    }

    @Override
    public IProviderModel findByName(String name) {
        for ( IProviderModel model : this.getModels() ) {
            if ( model.getName().equals( name ) ) {
                return model;
            }
        }

        return null;
    }

    @Override
    public IProvider createProvider(IProviderModel model) {
        Class<? extends IProvider> providerClazz = ProtocolsRegistry.get( model.getProtocolType() );
        if ( providerClazz == null ) {
            throw new IllegalArgumentException(
                String.format(
                    "Not supported protocol %s",
                    model.getProtocolType().name()
                )
            );
        }

        Constructor<? extends IProvider> targetConstructor = null;
        for ( Constructor<? extends IProvider> constructor : (Constructor<IProvider>[]) providerClazz.getConstructors() ) {
            if ( constructor.getParameterTypes().length < 2 ) {
                continue;
            }

            if ( !constructor.getParameterTypes()[0].equals( IProviderModel.class )
                    && !constructor.getParameterTypes()[1].equals( IConfig.class) ) {
                continue;
            }

            targetConstructor = constructor;
            break;
        }

        if ( targetConstructor == null ) {
            throw new IllegalStateException("Matching constructor not found");
        }

        IProvider provider;
        try {
            provider = targetConstructor.newInstance(new Object[]{model, ContextHandler.instance().getContext().getBean(IConfig.class)});
        } catch ( Throwable e ) {
            throw new IllegalStateException("Instantiation failed", e );
        }

        this.providers.put( model, provider );
        
        return provider;
    }

    @Override
    public void registerProvider(IProviderModel model, IProvider provider) {
        model.setId( UUID.randomUUID() );
        this.onRecord(provider);
        this.providers.put( model, provider );
    }

    @Override
    public boolean isRegistered(IProviderModel model) {
        return this.providers.containsKey(model);
    }

    @Override
    public IProvider getProvider(IProviderModel model) {
        return this.providers.get(model);
    }

    @Override
    public void removeProvider(IProviderModel model) {
        this.providers.remove(model);
    }

    @Override
    public List<IProvider> list() {
        return Commons.list( this.providers.values().toArray( new IProvider[ this.providers.size() ] ) );
    }

    public List<IProviderModel> getModels() {
        return Commons.list(this.providers.keySet().toArray( new IProviderModel[ this.providers.size() ] ));
    }

    @Override
    public void onRestore(ProviderRestorationData item) {
        this.registerProvider( item.getModel(), this.createProvider(item.getModel()));
    }

    @Override
    public ProviderRestorationData prepareRestoration(IProvider item) {
        return new ProviderRestorationData( this.getModel(item) );
    }
}
