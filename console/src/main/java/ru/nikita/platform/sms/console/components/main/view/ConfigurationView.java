package ru.nikita.platform.sms.console.components.main.view;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.IView;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import ru.nikita.platform.sms.console.components.main.MainComponent;
import ru.nikita.platform.sms.console.components.servers.ServersComponent;
import ru.nikita.platform.sms.console.data.records.Server;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationView implements IView {
    private Component component;
    
    private FormPanel form;
    
    private JButton connectButton;
    private JButton stopButton;
    
    @Override
    public void init() {
        JPanel panel = new JPanel();
        panel.setBorder( BorderFactory.createTitledBorder("Nikita SMS Platfrom") );
        panel.setLayout( new BoxLayout(panel, BoxLayout.Y_AXIS));

        IConfig config = UIRegistry.getContext().getBean(IConfig.class);
        
        Dispatcher.get().addListener( MainComponent.Events.Actions.ConnectionFailed, new IEventHandler() {
            @Override
            public void handle(AppEvent event) {
                ConfigurationView.this.connectButton.setEnabled(true);
                ConfigurationView.this.connectButton.repaint();

                ConfigurationView.this.stopButton.setEnabled(false);
                ConfigurationView.this.stopButton.repaint();
            }
        });
        
        this.form = new FormPanel();
        this.form.setBorder( BorderFactory.createTitledBorder("Kernel Manager Connection"));
        
        this.form.addField("host", "Host", new JTextField())
                 .setValue(this.readConfiguration(config, "server.host", "localhost"));
        this.form.addField("port", "Port", new JTextField())
                 .setValue( Integer.valueOf( this.readConfiguration(config, "server.port", "55532") ) );
        this.form.addField("path", "Path", new JTextField())
                 .setValue(this.readConfiguration(config, "server.path", "Server"));

        this.form.addButton(this.connectButton = new JButton(
                new InteractionAction("Connect", new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        if ( ConfigurationView.this.form.isDataValid() ) {
                            ConfigurationView.this.connectButton.setEnabled(false);
                            ConfigurationView.this.connectButton.repaint();

                            ConfigurationView.this.stopButton.setEnabled(true);
                            ConfigurationView.this.stopButton.repaint();
                            
                            ConfigurationView.this.onConnect(event);
                        }
                    }
                })
        ));
        
        this.stopButton = new JButton(
            new InteractionAction("Stop", new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                    ConfigurationView.this.connectButton.setEnabled(true);
                    ConfigurationView.this.connectButton.repaint();
                    
                    JButton stopButton = event.getArg(0);
                    stopButton.setEnabled(false);
                    stopButton.repaint();

                    Dispatcher.get().forwardEvent( new AppEvent( MainComponent.Events.Actions.StopConnection ) );
                }
            })
        );
        
        this.form.addButton( stopButton );
        

        panel.add( this.form );
        
        this.component = panel;
    }
    
    protected String readConfiguration( IConfig config, String path, String defaultValue ) {
        try {
            IConfig configNode = config.get(path);
            if ( configNode.isNull() ) {
                return defaultValue;
            }

            return configNode.value();
        } catch ( ConfigException e ) {
            return defaultValue;
        }
    }
    
    protected void onConnect( AppEvent event ) {
        Server server = new Server();
        server.setHost( this.form.<String>getField("host").getValue() );
        server.setPort( Integer.valueOf(this.form.<String>getField("port").getValue()) );
        server.setPath( String.valueOf( this.form.<String>getField("path").getValue() ) );

        Dispatcher.get().forwardEvent( MainComponent.Events.Actions.Connect, server );
    }

    @Override
    public void unload(Container component) {
        component.remove(this.component);
    }

    @Override
    public void render(Container component) {
        component.add( this.component );
    }

    @Override
    public void handle(AppEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
