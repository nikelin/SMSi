package ru.nikita.platform.sms.processors;

import com.redshape.utils.IEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/15/11
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class ActionType implements IEnum<String> {
    private String name;
    private String title;
    
    protected ActionType( String name, String title ) {
        this.name = name;
        this.title = title;
        REGISTRY.put(name, this);
    }

    @Override
    public String name() {
        return this.name;
    }
    
    public String title() {
        return this.title;
    }
    
    public static final Map<String, ActionType> REGISTRY = new HashMap<String, ActionType>();
    
    public static final ActionType CustomReaction = new ActionType("ActionType.CustomReaction", "Script invocation");
    public static final ActionType TemplateResponse = new ActionType("ActionType.TemplateResponse", "Template response");
    public static final ActionType Retranslation = new ActionType("ActionType.Retranslation", "Retranslation");
    public static final ActionType ProcessorInvocation = new ActionType("ActionType.ProcessorInvocation", "Processor invocation");

    public static ActionType valueOf( String value ) {
        return REGISTRY.get(value);
    }
    
    public static ActionType[] values() {
        return REGISTRY.values().toArray( new ActionType[REGISTRY.size()] );
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;

        ActionType that = (ActionType) o;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
