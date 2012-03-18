package ru.nikita.platform.sms.processors;

import com.redshape.utils.IEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorAttribute implements IEnum<String> {
    
    private String name;
    private String title;
    
    protected ProcessorAttribute( String name, String title ) {
        this.name = name;
        this.title = title;
        REGISTRY.put(name, this);
    }

    public String title() {
        return this.title;
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    private static final Map<String, ProcessorAttribute> REGISTRY = new HashMap<String, ProcessorAttribute>();

    public static final ProcessorAttribute CustomReactionDecl = new ProcessorAttribute("ProcessorAttribute.CustomReactionDecl", "scriptBody");
    public static final ProcessorAttribute NamedProcessorID = new ProcessorAttribute("ProcessorAttribute.NamedProcessorID", "processorName");
    
    public static final ProcessorAttribute valueOf( String name ) {
        return REGISTRY.get(name);
    }

    public static final ProcessorAttribute[] values() {
        return REGISTRY.values().toArray( new ProcessorAttribute[REGISTRY.size()] );
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessorAttribute that = (ProcessorAttribute) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }
}
