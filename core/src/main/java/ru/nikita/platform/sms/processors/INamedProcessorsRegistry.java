package ru.nikita.platform.sms.processors;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/15/11
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public interface INamedProcessorsRegistry {

    public void registerProcessor( String name, IProcessor processor );
    
    public IProcessor getProcessor( String name );
    
    public IProcessor[] getList();
    
    public void removeProcessor( String name );

}
