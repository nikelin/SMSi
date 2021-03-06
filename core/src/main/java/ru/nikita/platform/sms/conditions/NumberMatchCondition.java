package ru.nikita.platform.sms.conditions;

import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.messages.Message;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class NumberMatchCondition implements ICondition<IMessage> {
    private String number;

    public NumberMatchCondition( String number ) {
        this.number = number;
    }

    @Override
    public boolean isMatch(IMessage object) {
        if ( !( object instanceof Message) ) {
            return false;
        }

        Message message = (Message) object;
        return message.getPhone().equals(this.number);
    }
}
