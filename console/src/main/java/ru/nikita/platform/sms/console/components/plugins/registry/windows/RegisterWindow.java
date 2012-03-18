package ru.nikita.platform.sms.console.components.plugins.registry.windows;

import com.redshape.ui.application.UIException;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.bindings.render.ISwingRenderer;
import com.redshape.ui.data.bindings.render.components.ObjectUI;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.IView;
import ru.nikita.platform.sms.console.components.plugins.registry.RegistryController;
import ru.nikita.platform.sms.console.data.records.PluginsRegistry;
import ru.nikita.platform.sms.console.data.stores.PluginsRegistryStore;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterWindow extends JFrame {
    private ObjectUI ui;

    public RegisterWindow() {
        super();

        this.configUI();
        this.buildUI();
    }

    protected void configUI() {
        this.setTitle("New plugins registry details");
        this.setSize(300,200);
    }

    protected void buildUI() {
        try {
            JPanel panel = new JPanel();
            panel.setLayout( new BoxLayout(panel, BoxLayout.Y_AXIS ) );
            panel.setMinimumSize(new Dimension(500, 400));

            ISwingRenderer renderer = UIRegistry.<ISwingRenderer>getViewRendererFacade()
                        .createRenderer( PluginsRegistry.class );

            this.ui = renderer.render(panel, PluginsRegistry.class);
            this.ui.setMinimumSize( new Dimension(400, 500) );
            this.ui.invalidate();
            this.ui.repaint();
            
            Box buttonsBox = Box.createHorizontalBox();
            buttonsBox.add( new JButton( new InteractionAction("Save", new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                   RegisterWindow.this.onSave();
                   RegisterWindow.this.setVisible(false);

                   try {
                        UIRegistry.getViewsManager().activate(RegistryController.LIST_VIEW);
                   } catch ( Throwable e ) {
                       UIRegistry.getNotificationsManager().error( e.getMessage() );
                       throw new UnhandledUIException( e.getMessage(), e );
                   }
                }
            })));
            
            buttonsBox.add( new JButton( new InteractionAction("Cancel", new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                    RegisterWindow.this.setVisible(false);
                }
            }) ) );
            panel.add( buttonsBox );
            
            this.add(panel);
        } catch ( InstantiationException e ) {
            throw new UnhandledUIException( e.getMessage(), e );
        } catch ( UIException e ) {
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }
    
    protected void onSave() {
        try {
            UIRegistry.getStoresManager().getStore(PluginsRegistryStore.class)
                      .add(RegisterWindow.this.ui.<PluginsRegistry>createInstance());
        } catch ( Throwable e ) {
            UIRegistry.getNotificationsManager().error( e.getMessage() );
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }

}
