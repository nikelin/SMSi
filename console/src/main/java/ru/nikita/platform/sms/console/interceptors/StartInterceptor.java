package ru.nikita.platform.sms.console.interceptors;

import com.redshape.daemon.services.Connector;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractApplication;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.status.StandardStatusBar;
import com.redshape.ui.data.loaders.policies.RefreshPolicy;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.utils.Constants;
import ru.nikita.platform.sms.console.App;
import ru.nikita.platform.sms.console.components.main.MainComponent;
import ru.nikita.platform.sms.console.data.loaders.ProcessorsLoader;
import ru.nikita.platform.sms.console.data.loaders.ProvidersLoader;
import ru.nikita.platform.sms.console.data.loaders.ServersLoader;
import ru.nikita.platform.sms.console.data.records.Processor;
import ru.nikita.platform.sms.console.data.records.Provider;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.console.data.stores.ProcessorsStore;
import ru.nikita.platform.sms.console.data.stores.ProvidersStore;
import ru.nikita.platform.sms.console.data.stores.ServersStore;
import ru.nikita.platform.sms.services.IManagerService;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class StartInterceptor implements IEventHandler {
    
    @Override
    public void handle(AppEvent event) {
        UIRegistry.setStatusBar( new StandardStatusBar() );
        UIRegistry.set(App.Attributes.Connector, new Connector<IManagerService>() );
        Dispatcher.get().forwardEvent(MainComponent.Events.Views.Main);

        this.initStores();
        this.disableMenu();
        
        Dispatcher.get().addListener( MainComponent.Events.Actions.Connected, new IEventHandler() {
            @Override
            public void handle(AppEvent event) {
                StartInterceptor.this.enableMenu();
            }
        });
    }

    protected void initStores() {
        UIRegistry.getStoresManager().register(
            new ProvidersStore( new RefreshPolicy<Provider>( new ProvidersLoader(), Constants.TIME_SECOND * 1 ) )
        );

        UIRegistry.getStoresManager().register(
            new ProcessorsStore( new RefreshPolicy<Processor>( new ProcessorsLoader(), Constants.TIME_SECOND * 1 ) )
        );

        UIRegistry.getStoresManager().register(
            new ServersStore( new RefreshPolicy<Server>( new ServersLoader(), Constants.TIME_SECOND * 1 ) ) );
    }

    protected void disableMenu() {
        MenuBar menu = UIRegistry.getMenu();
        for ( int i = 0; i < menu.getMenuCount(); i++ ) {
            Menu menuItem = menu.getMenu(i);
            menuItem.setEnabled(false);
        }

        UIRegistry.getRootContext().revalidate();
        UIRegistry.getRootContext().repaint();
    }

    protected void enableMenu() {
        MenuBar menu = UIRegistry.getMenu();
        for ( int i = 0; i < menu.getMenuCount(); i++ ) {
            Menu menuItem = menu.getMenu(i);
            menuItem.setEnabled(true);
        }

        UIRegistry.getRootContext().revalidate();
        UIRegistry.getRootContext().repaint();
    }
}
