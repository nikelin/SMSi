package ru.nikita.platform.sms.providers;

import com.redshape.utils.IEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProtocolType implements IEnum<String> {
    private String name;
    private String title;
    private String scheme;
    
    protected ProtocolType( String code, String title, String scheme ) {
        this.name = code;
        this.scheme = scheme;
        this.title = title;
        REGISTRY.put(code, this);
    }
    
    public String title() {
        return this.title;
    }

    public static ProtocolType[] values() {
        return REGISTRY.values().toArray( new ProtocolType[ REGISTRY.size() ] );
    }
    
    public static ProtocolType valueOf( String name ) {
        return REGISTRY.get( name );
    }

    public String scheme() {
        return this.scheme;
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    private static final Map<String, ProtocolType> REGISTRY = new HashMap<String, ProtocolType>();
    
    public static final ProtocolType SMSC = new ProtocolType("SMSC", "SMPP Protocol", "smpp");
    public static final ProtocolType RMI = new ProtocolType("RMI", "RMI connection", "rmi");
    public static final ProtocolType HTTP = new ProtocolType("HTTP", "HTTP", "http");

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && ProtocolType.class.isAssignableFrom( obj.getClass() )
                && ( (ProtocolType) obj).name().equals( this.name );
    }
}
