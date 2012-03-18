package ru.nikita.platform.sms.console.components.bindings.views;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.IView;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.console.components.bindings.BindingsComponent;
import ru.nikita.platform.sms.console.data.records.Binding;
import ru.nikita.platform.sms.console.data.stores.BindingsStore;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 2:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListView implements IView {
    private static final Logger log = Logger.getLogger(ListView.class);
    private IStore<Binding> store;
    private Component component;
    
    @Override
    public void init() {
        try {
            JPanel panel = new JPanel();
            panel.setLayout( new BoxLayout(panel, BoxLayout.Y_AXIS ) );
            JTable table;
            JScrollPane pane = new JScrollPane( table = new TableAdapter<Binding>(
                    this.store = UIRegistry.getStoresManager().getStore(BindingsStore.class)));
            table.setFillsViewportHeight(true);
            panel.add(pane);
            
            Box buttonsBox = Box.createHorizontalBox();
            buttonsBox.add( new JButton(
                new InteractionAction("Refresh", new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        try {
                            store.load();
                        } catch ( LoaderException e ) {
                            log.error( e.getMessage(), e );
                            throw new UnhandledUIException( e.getMessage(), e );
                        }
                    }
                })
            ));
            buttonsBox.add( new JButton(
                new InteractionAction("Bind", new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        Dispatcher.get().forwardEvent(BindingsComponent.Events.Views.Create);
                    }
                })
            ));
            panel.add(buttonsBox);
            this.component = panel;
        } catch ( InstantiationException e ) {
            log.error( e.getMessage(), e );
            throw new UnhandledUIException( e.getMessage(), e );
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
    }
}
