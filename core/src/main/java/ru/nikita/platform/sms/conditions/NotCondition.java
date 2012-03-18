package ru.nikita.platform.sms.conditions;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class NotCondition<T> implements ICondition<T> {
    private ICondition<T> condition;

    public NotCondition(ICondition<T> condition) {
        this.condition = condition;
    }

    @Override
    public boolean isMatch(T object) {
        return this.condition == null || !this.condition.isMatch(object);
    }
}