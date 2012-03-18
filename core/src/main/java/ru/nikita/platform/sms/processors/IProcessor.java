package ru.nikita.platform.sms.processors;

import com.redshape.utils.events.IEventDispatcher;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.messages.Message;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package ru.nikita.platform.sms
 * @time 12/9/11 1:30 PM
 */
public interface IProcessor extends IEventDispatcher {

    public void process( IMessage process ) throws ProcessingException;

}
