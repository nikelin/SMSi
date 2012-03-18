package ru.nikita.platform.sms.console.components.bindings.windows;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.ComboBoxAdapter;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.bind.BindingType;
import ru.nikita.platform.sms.conditions.ICondition;
import ru.nikita.platform.sms.conditions.IConditionBuilder;
import ru.nikita.platform.sms.console.components.bindings.BindingsComponent;
import ru.nikita.platform.sms.console.data.records.Binding;
import ru.nikita.platform.sms.console.data.records.Processor;
import ru.nikita.platform.sms.console.data.records.Provider;
import ru.nikita.platform.sms.console.data.stores.ProcessorsStore;
import ru.nikita.platform.sms.console.data.stores.ProvidersStore;
import ru.nikita.platform.sms.messages.IMessage;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateWindow extends JFrame {
    private static final Logger log = Logger.getLogger(CreateWindow.class);

    private FormPanel form;
    
    public CreateWindow() {
        super();

        this.configUI();
        this.buildUI();
    }

    protected void configUI() {
        this.setTitle("Processor bind");
        this.setSize(265, 250);
    }

    protected void buildUI() {
        try {
            this.form = new FormPanel();
            this.form.addField("type", "Type", this.createTypeSelector() );
            this.form.addField("processor", "Processor", this.createProcessorSelector() );
            this.form.addField("provider", "Provider", this.createProviderSelector() );
            this.form.addField("numbers", "Numbers (comma-separated)", new JTextField() );
            this.form.addButton(
                new JButton( new InteractionAction("Bind", new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        if ( form.isDataValid() ) {
                            CreateWindow.this.onCreate(event);
                        }
                    }
                }) )
            );
            this.form.addButton(
                new JButton( new InteractionAction("Cancel", new WindowCloseHandler(this) ) )
            );
            
            this.add( this.form );
        } catch ( InstantiationException e ) {
            UIRegistry.getNotificationsManager().error("View dispatching exception!");
            log.error( e.getMessage(), e );
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }
    
    protected void onCreate( AppEvent event ) {
        Binding binding = new Binding();
        binding.setType( this.form.<BindingType>getField("type").getValue() );
        binding.setProcessor( this.form.<Processor>getField("processor").getValue() );
        binding.setProvider( this.form.<Provider>getField("provider").getValue() );
        
        IConditionBuilder builder = UIRegistry.getContext().getBean(IConditionBuilder.class);
        
        String numbers = this.form.<String>getField("numbers").getValue();
        if ( numbers.isEmpty() ) {
            UIRegistry.getNotificationsManager().error("Numbers list must be defined!");
            return;
        }
        
        ICondition<IMessage> condition = null;
        for ( String number : numbers.split(",") ) {
            if ( condition == null ) {
                condition = builder.numberMatch(number);
            } else {
                condition = builder.or(builder.<IMessage>numberMatch(number), condition);
            }
        }

        binding.setCondition(condition);
        
        Dispatcher.get().forwardEvent(BindingsComponent.Events.Actions.Bind, binding );
    }
    
    protected JComboBox<BindingType> createTypeSelector() {
        JComboBox<BindingType> selector = new JComboBox<BindingType>( BindingType.values() );
        selector.setRenderer( new ListCellRenderer<BindingType>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends BindingType> jList, BindingType value, int index, boolean isSelected, boolean cellHasFocus) {
                return new JLabel( value.type() );
            }
        });

        return selector;
    }
    
    protected JComboBox<Processor> createProcessorSelector() throws InstantiationException {
        JComboBox<Processor> selector = new ComboBoxAdapter<Processor>(UIRegistry.getStoresManager().getStore(ProcessorsStore.class));
        selector.setRenderer( new ListCellRenderer<Processor>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Processor> jList, Processor value, int index, boolean isSelected, boolean cellHasFocus) {
                return value != null ? new JLabel( value.getName() ) : new JLabel("<null>");
            }
        });

        return selector;
    }
    
    protected JComboBox<Provider> createProviderSelector() throws InstantiationException  {
        JComboBox<Provider> selector = new ComboBoxAdapter<Provider>(UIRegistry.getStoresManager().getStore(ProvidersStore.class));
        selector.setRenderer(new ListCellRenderer<Provider>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Provider> jList, Provider value, int index, boolean isSelected, boolean cellHasFocus) {
                return value != null ? new JLabel( value.getName() ) : new JLabel("<null>");
            }
        });

        return selector;
    }

}
