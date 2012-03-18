package ru.nikita.platform.sms.providers.adapters;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/15/11
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdapterModel implements IAdapterModel {
    private URI uri;
    private Map<AdapterAttribute, Object> attributes = new HashMap<AdapterAttribute, Object>();

    @Override
    public void setURI(URI uri) {
        this.uri = uri;
    }

    @Override
    public URI getURL() {
        return this.uri;
    }

    @Override
    public void setAttribute(AdapterAttribute attribute, Object value) {
        this.attributes.put( attribute, value );
    }

    @Override
    public <T> T getAttribute(AdapterAttribute attribute) {
        return (T) this.attributes.get(attribute);
    }
}
