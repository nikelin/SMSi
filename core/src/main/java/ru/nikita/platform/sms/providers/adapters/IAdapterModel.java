package ru.nikita.platform.sms.providers.adapters;

import java.io.Serializable;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:04 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IAdapterModel extends Serializable {
    
    public void setURI( URI uri );
    
    public URI getURL();
    
    public void setAttribute(AdapterAttribute attribute, Object value);

    public <T> T getAttribute( AdapterAttribute attribute );
    
    
}
