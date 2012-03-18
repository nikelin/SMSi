package ru.nikita.platform.sms.providers;

import ru.nikita.platform.sms.messages.IMessage;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IInterceptor {
    
    public void preHandle( IProvider provider );
    
    public void handle( IProvider provider, IMessage message );
    
    public void postHandle( IProvider provider );
    
}
