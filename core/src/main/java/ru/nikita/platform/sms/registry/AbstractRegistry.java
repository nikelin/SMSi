package ru.nikita.platform.sms.registry;

import com.redshape.utils.events.AbstractEventDispatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractRegistry<T, V extends IRestorationData> extends AbstractEventDispatcher
                                                                   implements IRegistry<T, V> {
    private List<IRegistryInterceptor<T, V>> interceptors = new ArrayList<IRegistryInterceptor<T, V>>();

    public AbstractRegistry( List<IRegistryInterceptor<T, V>> interceptors ) {
        this.setInterceptors(interceptors);

        this.init();
    }
    
    protected void init() {
        for ( IRegistryInterceptor<T, V> interceptor : this.interceptors ) {
            interceptor.init(this);
        }
    }

    protected void onRecord( T record ) {
        for ( IRegistryInterceptor<T, V> interceptor : this.interceptors ) {
            interceptor.onRecord(record);
        }
    }

    @Override
    public void setInterceptors(List<IRegistryInterceptor<T, V>> list) {
        this.interceptors = list;
    }

    @Override
    public void addInterceptor(IRegistryInterceptor<T, V> interceptor ) {
        this.interceptors.add( interceptor );
    }
}
