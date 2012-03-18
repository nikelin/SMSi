package ru.nikita.platform.sms.processors;

import ru.nikita.platform.sms.conditions.ICondition;
import ru.nikita.platform.sms.processors.ActionType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IProcessorModel extends Serializable {

    public void setId( UUID id );

    public UUID getId();

    public void setName( String name );
    
    public String getName();
    
    public void setDescription( String description );

    public String getDescription();

    public void setActionType( ActionType type );
    
    public ActionType getActionType();

    public <T> Map<ProcessorAttribute, T> getAttributes();

    public void setAttribute( ProcessorAttribute attribute, Object value );
    
    public <T> T getAttribute( ProcessorAttribute attribute );
    
}
