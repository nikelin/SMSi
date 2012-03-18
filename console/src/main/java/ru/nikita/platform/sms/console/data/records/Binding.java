package ru.nikita.platform.sms.console.data.records;

import com.redshape.ui.data.AbstractModelData;
import ru.nikita.platform.sms.bind.BindingType;
import ru.nikita.platform.sms.conditions.ICondition;
import ru.nikita.platform.sms.messages.IMessage;
import ru.nikita.platform.sms.bind.BindedPair;

import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Binding extends AbstractModelData {

    public Binding() {
        super();
    }
    
    public void setType( BindingType type ) {
        this.set( BindingModel.TYPE, type );
    }
    
    public BindingType getType() {
        return this.get( BindingModel.TYPE );
    }
    
    public void setId( UUID id ) {
        this.set( BindingModel.ID, id );
    }

    public UUID getId() {
        return this.get( BindingModel.ID );
    }
    
    public Provider getProvider() {
        return this.get(BindingModel.PROVIDER);
    }
    
    public void setProvider( Provider provider ) {
        this.set( BindingModel.PROVIDER, provider );
    }
    
    public void setProcessor( Processor processor ) {
        this.set( BindingModel.PROCESSOR, processor );
    }
    
    public Processor getProcessor() {
        return this.get( BindingModel.PROCESSOR );
    }
    
    public ICondition<IMessage> getCondition() {
        return this.get( BindingModel.CONDITION );
    }
    
    public void setCondition( ICondition<IMessage> condition ) {
        this.set( BindingModel.CONDITION, condition );
    }
    
    public BindedPair toModel() throws URISyntaxException {
        return new BindedPair( this.getType(), this.getProcessor().toModel(), this.getProvider().toModel(), this.getCondition() );
    }
    
    public static Binding fromModel( BindedPair pair ) {
        Binding binding = new Binding();
        binding.setId( pair.getId() );
        binding.setType( pair.getType() );
        binding.setProcessor( Processor.fromModel( pair.getProcessor() ) );
        binding.setProvider( Provider.fromModel( pair.getProvider() ) );

        return binding;
    }

    @Override
    public int hashCode() {
        return ( this.getProcessor().getName() + this.getProvider().getName() ).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) {
            return false;
        }
        
        if ( !( obj instanceof Binding ) ) {
            return false;
        }

        return ((Binding) obj).getProcessor().equals( this.getProcessor() )
                && ((Binding) obj).getProvider().equals(this.getProvider());
    }
}
