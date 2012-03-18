package ru.nikita.platform.sms.console.components.plugins.registry.views;

import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.IView;
import ru.nikita.platform.sms.console.components.plugins.registry.RegistryComponent;
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
public class ListView implements IView {
    private Component component;

    @Override
    public void init() {
        try {
            Box box;
            this.component = box = Box.createVerticalBox();
            TableAdapter table;
            JScrollPane tableView = new JScrollPane( table = new TableAdapter( UIRegistry.getStoresManager().getStore(PluginsRegistryStore.class) ) );
            table.setFillsViewportHeight(true);
            box.add( tableView );
            
            Box buttonsBox = Box.createHorizontalBox();
            buttonsBox.add( new JButton( new InteractionAction("Register new...", RegistryComponent.Events.Views.Register ) ) );
            box.add( buttonsBox );
        } catch ( InstantiationException e ) {
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
