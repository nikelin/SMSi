package ru.nikita.platform.sms.console.components.providers.views;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.data.loaders.policies.RefreshPolicy;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.IView;
import com.redshape.utils.Constants;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.console.components.processors.templates.TemplatesComponent;
import ru.nikita.platform.sms.console.components.providers.ProvidersComponent;
import ru.nikita.platform.sms.console.data.records.Provider;
import ru.nikita.platform.sms.console.data.stores.ProvidersStore;
import ru.nikita.platform.sms.console.data.stores.TemplatesStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/9/11
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListView implements IView {
    private static final Logger log = Logger.getLogger(ListView.class);

    private IStore<Provider> store;
    private Component component;
    
    @Override
    public void init() {
        try {
            this.store = UIRegistry.getStoresManager().getStore(ProvidersStore.class);
            Box box;
            this.component = box = Box.createVerticalBox();
            JTable table;
            JScrollPane pane = new JScrollPane( table = new TableAdapter( this.store ) );
            table.addMouseListener( new MouseAdapter() {
                protected void showTableContext( MouseEvent e ) {
                    JPopupMenu menu = new JPopupMenu();
                    menu.add( new InteractionAction("Create", ProvidersComponent.Events.Views.Create ) );
                    menu.add( new InteractionAction("Refresh", new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {
                            //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }) );

                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    if ( !SwingUtilities.isRightMouseButton(e) ) {
                        return;
                    }

                    JTable table = (JTable) e.getComponent();
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                    } else {
                        table.clearSelection();
                    }

                    final int rowindex = table.getSelectedRow();
                    if (rowindex < 0) {
                        this.showTableContext(e);
                        return;
                    }

                    JPopupMenu popup = new JPopupMenu();
                    popup.add( new InteractionAction("Remove", new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {
                            Dispatcher.get().forwardEvent( ProvidersComponent.Events.Actions.Remove,
                                    ListView.this.store.getAt( rowindex ) );
                        }
                    }));
                    popup.add( new InteractionAction("Stop", new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {
                            Dispatcher.get().forwardEvent( ProvidersComponent.Events.Actions.Stop,
                                    ListView.this.store.getAt( rowindex ) );
                        }
                    }));
                    popup.add( new InteractionAction("Start", new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {
                            Dispatcher.get().forwardEvent( ProvidersComponent.Events.Actions.Start,
                                    ListView.this.store.getAt( rowindex ) );
                        }
                    }));

                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            });
            table.setFillsViewportHeight(true);
            box.add( pane );
            
            Box buttonsBox = Box.createHorizontalBox();
            buttonsBox.add( new JButton( new InteractionAction("Create", ProvidersComponent.Events.Views.Create ) ) );
            buttonsBox.add( new JButton( new InteractionAction("Refresh", new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                    try {
                        ListView.this.store = UIRegistry.getStoresManager().getStore(ProvidersStore.class);
                    } catch ( Throwable e ) {
                        UIRegistry.getNotificationsManager().error("Store refreshing failed!");
                        log.error(e.getMessage(), e);
                    }
                }
            }) ) );
            box.add( buttonsBox );
        } catch ( Throwable e ) {
            UIRegistry.getNotificationsManager().error( e.getMessage() );
            throw new UnhandledUIException( e.getMessage(), e ); 
        }
    }

    public void unload(Container component) {
        component.remove( this.component );
    }

    public void render(Container component) {
        component.add( this.component );
    }

    public void handle(AppEvent event) {

    }
}
