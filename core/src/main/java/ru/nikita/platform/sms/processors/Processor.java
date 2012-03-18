package ru.nikita.platform.sms.processors;

import com.redshape.ascript.EvaluationException;
import com.redshape.ascript.IEvaluator;
import com.redshape.ascript.context.IEvaluationContext;
import com.redshape.utils.events.AbstractEventDispatcher;
import com.redshape.utils.events.IEvent;
import com.redshape.utils.events.IEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.messages.Message;
import ru.nikita.platform.sms.processors.ActionType;
import ru.nikita.platform.sms.utils.ContextHandler;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/15/11
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class Processor extends AbstractEventDispatcher implements IProcessor {
    
    private IProcessorModel model;

    private Object evaluationLock = new Object();
    
    public Processor( IProcessorModel model ) {
        this.model = model;
    }

    protected IProcessorModel getModel() {
        return this.model;
    }

    protected IEvaluator getEvaluator() {
        return ContextHandler.instance().getContext().getBean(IEvaluator.class);
    }

    protected INamedProcessorsRegistry getRegistry() {
        return ContextHandler.instance().getContext().getBean(INamedProcessorsRegistry.class);
    }

    @Override
    public void process(IMessage process) throws ProcessingException {
        ActionType type = this.model.getActionType();
        if ( type.equals(ActionType.CustomReaction) ) {
            this.processCustomReaction( process );
        } else if ( type.equals(ActionType.ProcessorInvocation) ) {
            this.processProcessorInvocation( process );
        }
    }
    
    protected void processCustomReaction( IMessage message ) throws ProcessingException {
        synchronized (evaluationLock)  {
            try {
                IEvaluationContext context = this.getEvaluator().createContext("sms");
                context.reset();
                context.exportValue("message", message);
                context.exportValue("processor", this );

                this.getEvaluator().evaluate( this.getModel().<String>getAttribute(ProcessorAttribute.CustomReactionDecl) );
            } catch ( EvaluationException e  ) {
                throw new ProcessingException( e.getMessage(), e );
            }
        }
    }
    
    protected void processProcessorInvocation( IMessage message ) throws ProcessingException {
        IProcessor processor = this.getRegistry().getProcessor(
                this.getModel().<String>getAttribute( ProcessorAttribute.NamedProcessorID ) );
        if ( processor == null ) {
            throw new ProcessingException("Assigned processor not presents in registry!");
        }

        processor.process(message);
    }

}
