package ru.nikita.platform.sms.bind;

import ru.nikita.platform.sms.registry.IRegistry;
import ru.nikita.platform.sms.registry.IRestorationData;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IBindedPairsRegistry<T extends IRestorationData> extends IRegistry<IBindedPair, T> {
                           
    public Collection<IBindedPair> getProcessors( UUID providerId );
    
    public void registerProcessor( IBindedPair pair);
    
}
