package ru.nikita.platform.sms.console.components.processors.views;

import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.IView;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.console.components.processors.ProcessorsComponent;
import ru.nikita.platform.sms.console.data.stores.ProcessorsStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListView implements IView {
    private static final Logger log = Logger.getLogger(ListView.class);
    
    private Component component;
    
    @Override
    public void init() {
        try {
            Box box;
            this.component = box = Box.createVerticalBox();
            
            JTable table;
            JScrollPane pane = new JScrollPane( table = new TableAdapter(UIRegistry.getStoresManager().getStore(ProcessorsStore.class)));
            table.addMouseListener( new MouseAdapter() {
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

                    int rowindex = table.getSelectedRow();
                    if (rowindex < 0) {
                        return;
                    }
                
                    JPopupMenu popup = new JPopupMenu();
                    popup.add( new InteractionAction("Remove", new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {

                        }
                    }));
                    popup.add( new InteractionAction("Stop", new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {
                            //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }));
                    popup.add( new InteractionAction("Start", new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {
                            //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }));

                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            });
            table.setFillsViewportHeight(true);
            box.add(pane);
            
            Box buttonsBox = Box.createHorizontalBox();
            buttonsBox.add( new JButton(
                new InteractionAction("Create", ProcessorsComponent.Events.Views.Create )
            ));
            buttonsBox.add( new JButton(
                new InteractionAction("Refresh", new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        try {
                            UIRegistry.getStoresManager().getStore(ProcessorsStore.class).load();
                        } catch ( Throwable e ) {
                            log.error("Unable to refresh processors store", e );
                        }
                    }
                }
            )) );
            box.add( buttonsBox );
        } catch ( Throwable e ) {
            UIRegistry.getNotificationsManager().error( e.getMessage() );
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
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
