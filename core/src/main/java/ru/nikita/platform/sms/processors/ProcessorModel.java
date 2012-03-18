package ru.nikita.platform.sms.processors;

import ru.nikita.platform.sms.processors.ActionType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/15/11
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorModel implements IProcessorModel {
    private UUID id;
    private String name;
    private String description;
    private ActionType actionType;
    private Map<ProcessorAttribute, Object> attributes = new HashMap<ProcessorAttribute, Object>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public void setName( String name ) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setActionType(ActionType type) {
        this.actionType = type;
    }

    @Override
    public ActionType getActionType() {
        return this.actionType;
    }

    @Override
    public <T> Map<ProcessorAttribute, T> getAttributes() {
        return (Map<ProcessorAttribute, T>) this.attributes;
    }
    
    @Override
    public void setAttribute(ProcessorAttribute attribute, Object value) {
        this.attributes.put( attribute, value );
    }

    @Override
    public <T> T getAttribute(ProcessorAttribute attribute) {
        return (T) this.attributes.get(attribute);
    }
}
