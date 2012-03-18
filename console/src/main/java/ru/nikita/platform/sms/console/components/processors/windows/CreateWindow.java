package ru.nikita.platform.sms.console.components.processors.windows;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.ComboBoxAdapter;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.utils.Function;
import ru.nikita.platform.sms.console.components.processors.ProcessorsComponent;
import ru.nikita.platform.sms.console.data.records.Processor;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.console.data.stores.ProcessorsStore;
import ru.nikita.platform.sms.console.data.stores.ServersStore;
import ru.nikita.platform.sms.processors.ActionType;
import ru.nikita.platform.sms.processors.ProcessorAttribute;
import ru.nikita.platform.sms.processors.ActionType;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateWindow extends JFrame {
    private FormPanel form;
    
    public CreateWindow() {
        super();
        
        this.configUI();
        this.buildUI();
    }

    protected void configUI() {
        this.setTitle("New messages processor");
        this.setSize( 500, 600 );
    }

    protected void buildUI() {
        final FormPanel form = this.form = new FormPanel();
        form.setBorder( BorderFactory.createTitledBorder("Processor details") );
        form.addField("server", "Server", this.createServerSelector() );
        form.addField("name", "Name", new JTextField() );
        form.addField("title", "Title", new JTextField() );
        form.addField("description", "Description", new JTextArea() );
        form.addField("actionType", "Action type", this.createTypeSelector() );
        form.addField("parameters", "Parameters (comma-separated)", new JTextField() );

        form.addButton(new JButton(
                new InteractionAction("Create", new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        if (!form.isDataValid()) {
                            return;
                        }

                        CreateWindow.this.onCreate();
                    }
                })
        ));
        
        form.addButton( new JButton(
            new InteractionAction("Cancel", new WindowCloseHandler(this) )
        ));
        
        this.add( form );
    }

    protected void onCreate() {
        Processor record = new Processor() ;
        record.setServer( form.<Server>getField("server").getValue() );
        record.setActionType( form.<ActionType>getField("actionType").getValue() );
        record.setName( form.<String>getField("name").getValue() );
        record.setTitle( form.<String>getField("title").getValue() );
        record.setDescription( form.<String>getField("description").getValue() );
        
        String parameters = form.<String>getField("parameters").getValue();
        if ( !parameters.isEmpty() ) {
            for ( String parameter : parameters.split(",") ) {
                String[] parts = parameter.split("=");
                if ( parts.length != 2 ) {
                    continue;
                }

                record.setParameter(ProcessorAttribute.valueOf(parts[0]), parts[1] );
            }
        }
        
        Dispatcher.get().forwardEvent(ProcessorsComponent.Events.Actions.Create, record);
    }

    protected JComboBox<Server> createServerSelector() {
        try {
            JComboBox<Server> selector = new ComboBoxAdapter(UIRegistry.getStoresManager().getStore(ServersStore.class));
            selector.setRenderer( new ListCellRenderer<Server>() {
                @Override
                public Component getListCellRendererComponent(JList<? extends Server> jList, Server value, int index, boolean isSelected, boolean cellHasFocus) {
                    return new JLabel( value.getName() );
                }
            });
    
            return selector;
        } catch ( InstantiationException e ) {
            UIRegistry.getNotificationsManager().error("View dispatching exception!");
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }
    
    protected JComboBox<ActionType> createTypeSelector() {
        JComboBox<ActionType> selector = new JComboBox<ActionType>( ActionType.values() );
        selector.setRenderer( new ListCellRenderer<ActionType>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends ActionType> jList, ActionType value, int index, boolean isSelected, boolean cellHasFocus) {
                return new JLabel( value.title() );
            }
        });

        return selector;
    }
}
