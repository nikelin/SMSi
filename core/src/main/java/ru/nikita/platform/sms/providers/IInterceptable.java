package ru.nikita.platform.sms.providers;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IInterceptable {

    public void addInterceptor( IInterceptor interceptor );

}
