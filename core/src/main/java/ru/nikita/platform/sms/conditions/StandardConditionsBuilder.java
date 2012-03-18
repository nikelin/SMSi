package ru.nikita.platform.sms.conditions;

import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.messages.Message;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class StandardConditionsBuilder implements IConditionBuilder {

    @Override
    public ICondition keywordMatch(String keyword) {
        return new KeywordMatchCondition(keyword);
    }

    @Override
    public ICondition numberMatch(String number) {
        return new NumberMatchCondition(number);
    }

    @Override
    public <T> ICondition<T> list(ICondition<T>... list) {
        return new ListCondition<T>(list);
    }

    @Override
    public <T> ICondition<T> not(ICondition<T> condition) {
        return new NotCondition(condition);
    }

    @Override
    public <T> ICondition<T> and(ICondition<T> left, ICondition<T> right) {
        return new AndCondition<T>( left, right );
    }

    @Override
    public <T> ICondition<T> or(ICondition<T> left, ICondition<T> right) {
        return new OrCondition<T>( left, right );
    }
}
