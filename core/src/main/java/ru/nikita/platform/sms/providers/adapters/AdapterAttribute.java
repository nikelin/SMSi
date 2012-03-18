package ru.nikita.platform.sms.providers.adapters;

import com.redshape.utils.IEnum;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdapterAttribute implements IEnum<String> {
    private String name;
    
    protected AdapterAttribute( String name ) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    public static final AdapterAttribute CLIENT_ID = new AdapterAttribute("AdapterAttributes.ClientID");
    public static final AdapterAttribute UUID = new AdapterAttribute("AdapterAttributes.UUID");
    public static final AdapterAttribute LOGIN = new AdapterAttribute("AdapterAttributes.Login");
    public static final AdapterAttribute PASSWORD = new AdapterAttribute("AdapterAttributes.Password");

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null &&
        AdapterAttribute.class.isAssignableFrom(obj.getClass()) &&
            ( (AdapterAttribute) obj ).name().equals( this.name() );

    }

    @Override
    public String toString() {
        return this.name();
    }
}
