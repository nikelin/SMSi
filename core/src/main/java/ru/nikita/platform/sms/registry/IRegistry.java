package ru.nikita.platform.sms.registry;

import com.redshape.utils.events.IEventDispatcher;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 6:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IRegistry<T, V extends IRestorationData> extends IEventDispatcher {

    public interface IRegistryInterceptor<Z, E extends IRestorationData> {

        public void init( IRegistry<Z, E> registry );
        
        public void onRecord( Z record );

    }

    public Collection<T> list();
    
    public void setInterceptors( List<IRegistryInterceptor<T, V>> interceptors );

    public void addInterceptor( IRegistryInterceptor<T, V> interceptor );

    public void onRestore( V item );
    
    public V prepareRestoration( T item );

}
