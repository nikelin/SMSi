package ru.nikita.platform.sms.conditions;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class AndCondition<T> implements ICondition<T> {
    private ICondition<T> left;
    private ICondition<T> right;

    public AndCondition( ICondition<T> left, ICondition<T> right ) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isMatch(T object) {
        return ( this.left != null && this.left.isMatch(object) )
                && ( this.right != null && this.right.isMatch(object) );
    }
}