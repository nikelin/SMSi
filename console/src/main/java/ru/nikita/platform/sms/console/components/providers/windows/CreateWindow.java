package ru.nikita.platform.sms.console.components.providers.windows;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.ComboBoxAdapter;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;
import ru.nikita.platform.sms.console.components.providers.ProvidersComponent;
import ru.nikita.platform.sms.console.data.records.Adapter;
import ru.nikita.platform.sms.console.data.records.Provider;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.console.data.stores.ServersStore;
import ru.nikita.platform.sms.providers.ProtocolType;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/15/11
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateWindow extends JFrame {
    private FormPanel form;
    private FormPanel adapterForm;
    
    public CreateWindow() {
        super();

        this.buildUI();
        this.configUI();
    }

    protected void buildUI() {
        try {
            FormPanel panel = this.form =  new FormPanel();
            panel.setBorder( BorderFactory.createTitledBorder("Provider configuration") );
            panel.addField("server", "Server", new ComboBoxAdapter(UIRegistry.getStoresManager().getStore(ServersStore.class) ) );
            panel.addField("name", "Name", new JTextField() );
            panel.addField("title", "Title", new JTextField() );
            panel.addField("description", "Description", new JTextField() );

            FormPanel adapterForm = this.adapterForm = new FormPanel();
            adapterForm.setBorder( BorderFactory.createTitledBorder("Connection configuration") );
            adapterForm.addField("protocol", "Protocol", this.createProtocolSelector() );
            adapterForm.addField("host", "Host", new JTextField() );
            adapterForm.addField("port", "Port", new JTextField() );
            adapterForm.addField("clientId", "Login", new JTextField() );
            adapterForm.addField("password", "Password", new JPasswordField() );
            panel.addRaw( adapterForm );

            panel.addButton(
                new JButton(
                    new InteractionAction("Create", new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {
                            CreateWindow.this.onCreate( event );
                        }
                    })
                )
            );

            panel.addButton(
                new JButton( new InteractionAction("Cancel", new WindowCloseHandler(this) ) )
            );

            this.add( panel );
        } catch ( InstantiationException e ) {
            UIRegistry.getNotificationsManager().error("View dispatching exception!");
        }
    }
    
    protected void onCreate( AppEvent event ) {
        Provider provider = new Provider();
        provider.setServer( this.form.<Server>getField("server").getValue() );
        provider.setProtocol( this.adapterForm.<ProtocolType>getField("protocol").getValue() );
        provider.setName(this.form.<String>getField("name").getValue());
        provider.setTitle( this.form.<String>getField("title").getValue() );
        provider.setDescription( this.form.<String>getField("description").getValue() );

        Adapter adapter = new Adapter();
        adapter.setHost( this.adapterForm.<String>getField("host").getValue() );
        adapter.setPort( Integer.valueOf( this.adapterForm.<String>getField("port").getValue() ) );
        adapter.setPassword( this.adapterForm.<String>getField("password").getValue() );
        adapter.setClientId( this.adapterForm.<String>getField("clientId").getValue() );
        provider.setAdapter(adapter);

        Dispatcher.get().forwardEvent( ProvidersComponent.Events.Actions.Created, provider );
    }
    
    protected JComboBox<ProtocolType> createProtocolSelector() {
        JComboBox<ProtocolType> selector = new JComboBox<ProtocolType>();
        selector.setRenderer( new ListCellRenderer<ProtocolType>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends ProtocolType> jList, ProtocolType value, int index, boolean isSelected, boolean cellHasFocus) {
                return new JLabel( value.title() );
            }
        });

        for ( ProtocolType type : ProtocolType.values() ) {
            selector.addItem( type );
        }

        return selector;
    }

    protected void configUI() {
        this.setTitle("New mobile provider configuration");
        this.setSize( 200, 350 );
    }

}
