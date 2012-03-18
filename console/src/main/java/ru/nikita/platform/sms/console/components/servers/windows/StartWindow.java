package ru.nikita.platform.sms.console.components.servers.windows;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.ComboBoxAdapter;
import com.redshape.ui.panels.FormPanel;
import com.redshape.utils.Constants;
import com.redshape.validators.impl.common.NotEmptyValidator;
import ru.nikita.platform.sms.console.components.servers.ServersComponent;
import ru.nikita.platform.sms.console.data.records.Server;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 10:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class StartWindow extends JFrame {
    public static final Map<String, Integer> RATES = new HashMap<String, Integer>();
    static {
        RATES.put("5 sec", Constants.TIME_SECOND * 5 );
        RATES.put("15 sec", Constants.TIME_SECOND * 15 );
        RATES.put("30 sec", Constants.TIME_SECOND * 30 );
        RATES.put("1 min", Constants.TIME_MINUTE  );
    }
    
    public StartWindow() {
        super();
        
        this.configUI();
        this.buildUI();
    }
    
    protected void configUI() {
        this.setTitle("Start new server");
        this.setSize( new Dimension(400, 350) );
    }
    
    protected void buildUI() {
        final FormPanel panel = new FormPanel();
        panel.addField("name", "Name", new JTextField());
        panel.addField("host", "Host", new JTextField() );
        panel.addField("port", "Port", new JTextField() );
        panel.addField("service", "Service", new JTextField() );
        panel.addField("pingRate", "Ping rate", new JComboBox<String>( RATES.keySet().toArray( new String[RATES.size()] ) ) );
        
        panel.addButton(new JButton(
                new InteractionAction("Start", new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        if (panel.isDataValid()) {
                            Server server = new Server();
                            server.setName(panel.<String>getField("name").getValue());
                            server.setHost(panel.<String>getField("host").getValue());
                            server.setPort(Integer.valueOf(panel.<String>getField("port").getValue()));
                            server.setPath(panel.<String>getField("service").getValue());
                            Dispatcher.get().forwardEvent(
                                new AppEvent(ServersComponent.Events.Actions.Started, server)
                            );
                        }
                    }
                })
        ));

        panel.addButton(
            new JButton(
                new InteractionAction("Cancel", new WindowCloseHandler(this) )
            )
        );
        
        this.add( panel );
    }
}
