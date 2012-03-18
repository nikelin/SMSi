package ru.nikita.platform.sms.providers;

import com.redshape.utils.IEnum;
import ru.nikita.platform.sms.providers.impl.ESMEProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is not suitable to be used in RMI context!
 */
public final class ProtocolsRegistry {
    private static Map<ProtocolType, Class<? extends IProvider>> registry
                    = new HashMap<ProtocolType, Class<? extends IProvider>>();
    
    static {
        registry.put( ProtocolType.SMSC, ESMEProvider.class );
    }
    
    public static Class<? extends IProvider> get( ProtocolType type ) {
        return registry.get( type );
    }
    
    public static void register( ProtocolType type, Class<? extends IProvider> protocolClazz ) {
        registry.put( type, protocolClazz );
    }
    
}
