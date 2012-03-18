package ru.nikita.platform.sms.conditions;

import ru.nikita.platform.sms.messages.IMessage;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IConditionBuilder {
    
    public <T> ICondition<T> list( ICondition<T>... list );
    
    public <T> ICondition<T> not( ICondition<T> condition );
    
    public <T> ICondition<T> and( ICondition<T> left, ICondition<T> right );
    
    public <T> ICondition<T> or( ICondition<T> left, ICondition<T> right );
    
    public <T> ICondition<T> keywordMatch( String keyword );
    
    public <T> ICondition<T> numberMatch( String number );
    
}
