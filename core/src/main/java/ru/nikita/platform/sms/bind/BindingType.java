package ru.nikita.platform.sms.bind;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 6:31 PM
 * To change this template use File | Settings | File Templates.
 */
public enum BindingType {
    SEND("Processor to provider"),
    RECEIVE("Provider to processor");
    
    private String type;
    
    private BindingType( String type ) {
        this.type = type;
    }
    
    public String type() {
        return this.type;
    }
}
