package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelData;
import ru.nikita.platform.sms.processors.ActionType;
import ru.nikita.platform.sms.processors.IProcessorModel;
import ru.nikita.platform.sms.processors.ProcessorAttribute;
import ru.nikita.platform.sms.processors.ActionType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Processor extends AbstractModelData {

    public Processor() {
        super();
        
        this.set(ProcessorModel.PARAMETERS, new HashMap() );
    }
    
    public Server getServer() {
        return this.get( ProcessorModel.SERVER );
    }
    
    public void setServer( Server server ) {
        this.set( ProcessorModel.SERVER, server );
    }
    
    public Map<ProcessorAttribute, Object> getParameters() {
        return this.get(ProcessorModel.PARAMETERS);
    }
    
    public <T> T getParameter( ProcessorAttribute name ) {
        return (T) this.getParameters().get(name);
    }
    
    public void setParameter( ProcessorAttribute name, Object value ) {
        this.getParameters().put(name, value);
    }
    
    public void setId( UUID id ) {
        this.set( ProcessorModel.ID, id );
    }
    
    public UUID getId() {
        return this.get( ProcessorModel.ID );
    }
    
    public void setTitle( String title ) {
        this.set( ProcessorModel.TITLE, title );
    }
    
    public String getTitle() {
        return this.get( ProcessorModel.TITLE );
    }
    
    public void setActionType( ActionType type ) {
        this.set( ProcessorModel.ACTION_TYPE, type );
    }
    
    public ActionType getActionType() {
        return this.get( ProcessorModel.ACTION_TYPE );
    }
    
    public String getDescription() {
        return this.get( ProcessorModel.DESCRIPTION );
    }
    
    public void setDescription( String description ) {
        this.set( ProcessorModel.DESCRIPTION, description );
    }
    
    public String getName() {
        return this.get( ProcessorModel.NAME );
    }
    
    public void setName( String name ) {
        this.set( ProcessorModel.NAME, name );
    }
    
    public static Processor fromModel( IProcessorModel model ) {
        Processor processor = new Processor();
        processor.setId( model.getId() );
        processor.setActionType( model.getActionType() );
        processor.getParameters().putAll( model.getAttributes() );
        processor.setName( model.getName() );
        processor.setDescription( model.getDescription() );
        return processor;
    }
    
    public IProcessorModel toModel() {
        IProcessorModel model = new ru.nikita.platform.sms.processors.ProcessorModel();
        model.setId( this.getId() );
        model.setName( this.getName() );
        model.setDescription( this.getDescription() );
        model.setActionType( this.getActionType() );
        model.getAttributes().putAll( this.getParameters() );

        return model;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) {
            return false;
        }
        
        if ( !( obj instanceof Processor ) ) {
            return false;
        }
        
        return ((Processor) obj).getName().equals( this.getName() );
    }
}
