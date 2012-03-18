package ru.nikita.platform.sms.conditions;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ICondition<T> extends Serializable {
    
    public boolean isMatch( T object );
    
}
