package ru.nikita.platform.sms.conditions;

import com.redshape.ascript.EvaluationException;
import com.redshape.ascript.context.IEvaluationContext;
import com.redshape.ascript.evaluation.ExpressionEvaluator;
import ru.nikita.platform.sms.messages.IMessage;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AscriptCondition implements ICondition<IMessage> {
    private String declaration;
    
    public AscriptCondition( String declaration ) {
        this.declaration = declaration;
    }

    @Override
    public boolean isMatch(IMessage object) {
        try {
            ExpressionEvaluator evaluator = new ExpressionEvaluator();

            IEvaluationContext context = evaluator.createContext("sms");
            context.exportValue("message", object);
            
            Object result = evaluator.evaluate(this.declaration);
            if ( result instanceof List ) {
                return (Boolean) ( (List) result ).get(0);
            }

            return (Boolean) result;
        } catch ( EvaluationException e ) {
            throw new IllegalStateException( e.getMessage(), e ); 
        }
    }
}
