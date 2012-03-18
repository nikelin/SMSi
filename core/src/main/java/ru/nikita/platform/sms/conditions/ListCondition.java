package ru.nikita.platform.sms.conditions;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListCondition<T> implements ICondition<T> {
    private ICondition<T>[] items;

    public ListCondition( ICondition<T>... items ) {
        this.items = items;
    }

    @Override
    public boolean isMatch(T object) {
        for ( ICondition<T> condition : this.items ) {
            if ( condition == null ) {
                continue;
            }

            if ( !condition.isMatch(object) ) {
                return false;
            }
        }

        return true;
    }
}