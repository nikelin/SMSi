package ru.nikita.platform.sms.console.components.servers.views;

import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.data.loaders.policies.RefreshPolicy;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.IView;
import com.redshape.utils.Constants;
import ru.nikita.platform.sms.console.components.servers.ServersComponent;
import ru.nikita.platform.sms.console.data.loaders.ServersLoader;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.console.data.stores.ServersStore;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class ListView implements IView {
    private Component component;
    private IStore<Server> store;

    @Override
    public void init() {
        try {
            this.store = UIRegistry.getStoresManager().getStore(ServersStore.class);
    
            JPanel panel = new JPanel();
            panel.setMinimumSize( new Dimension(600, 400) );
            panel.setLayout( new BoxLayout(panel, BoxLayout.Y_AXIS) );
    
            panel.add( new JLabel("Processing servers registry") );
    
            JTable table = new TableAdapter<Server>(this.store);
            JScrollPane pane = new JScrollPane( table );
            pane.setMinimumSize(new Dimension(600, 350));
            table.setFillsViewportHeight(true);
            panel.add( pane );
    
            Box buttonsBox = Box.createHorizontalBox();
            buttonsBox.setMinimumSize( new Dimension(600, 30) );
            buttonsBox.add( new JButton( new InteractionAction("Start", ServersComponent.Events.Actions.Start)));
            buttonsBox.add( new JButton( new InteractionAction("Refresh", new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                    try {
                        ListView.this.store.load();
                    } catch ( LoaderException e ) {
                        UIRegistry.getNotificationsManager().error("Unable to refresh store instance!");
                    }
                }
            })));
            panel.add( buttonsBox );
    
            this.component = panel;
        } catch ( InstantiationException e ) {
            UIRegistry.getNotificationsManager().error("View dispatching exception!");
        }
    }

    @Override
    public void unload(Container component) {
        component.remove( this.component );
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
