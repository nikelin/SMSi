package ru.nikita.platform.sms.bind;

import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.conditions.ICondition;
import ru.nikita.platform.sms.processors.IProcessorModel;
import ru.nikita.platform.sms.providers.IProviderModel;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IBindedPair extends Serializable {

    public void setId( UUID id );
    
    public UUID getId();

    public BindingType getType();
    
    public IProcessorModel getProcessor();

    public IProviderModel getProvider();

    public ICondition<IMessage> getCondition();
    
}
