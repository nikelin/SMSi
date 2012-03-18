package ru.nikita.platform.sms.bind;

import ru.nikita.platform.sms.conditions.ICondition;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.processors.IProcessorModel;
import ru.nikita.platform.sms.providers.IProviderModel;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class BindedPair implements IBindedPair {
    private UUID id;
    private IProcessorModel processor;
    private IProviderModel provider;
    private ICondition<IMessage> condition;
    private BindingType type;

    public BindedPair(BindingType type, IProcessorModel processor, IProviderModel provider, ICondition<IMessage> condition) {
        this(null, type, processor, provider, condition );
    }

    public BindedPair( UUID id, BindingType type, IProcessorModel processor, IProviderModel provider, ICondition<IMessage> condition) {
        this.id = id;
        this.processor = processor;
        this.type = type;
        this.provider = provider;
        this.condition = condition;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }

    @Override
    public BindingType getType() {
        return this.type;
    }

    @Override
    public IProcessorModel getProcessor() {
        return this.processor;
    }

    @Override
    public IProviderModel getProvider() {
        return this.provider;
    }

    @Override
    public ICondition<IMessage> getCondition() {
        return this.condition;
    }
}
