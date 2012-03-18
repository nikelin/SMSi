package ru.nikita.platform.sms.console.components.servers;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.console.App;
import ru.nikita.platform.sms.console.components.main.MainComponent;
import ru.nikita.platform.sms.console.components.servers.views.ListView;
import ru.nikita.platform.sms.console.components.servers.windows.StartWindow;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.services.IManagerService;
import ru.nikita.platform.sms.services.data.ServerConfiguration;

import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServersController extends AbstractController {
    private static final Logger log = Logger.getLogger(ServersController.class);

    public static final String LIST_VIEW = "ServersComponent.Views.List";
    
    @Override
    protected void initEvents() {
        Dispatcher.get().addListener( ServersComponent.Events.Actions.Started, this );
        Dispatcher.get().addListener( MainComponent.Events.Actions.Connected, this );
        Dispatcher.get().addListener( ServersComponent.Events.Actions.Start, this );
        Dispatcher.get().addListener( ServersComponent.Events.Views.List, this );
    }

    @Override
    protected void initViews() {
        UIRegistry.getViewsManager().register( new ListView(), LIST_VIEW );
    }
    
    @Action( eventType = "MainComponent.Events.Actions.Connected")
    public void onConnectedAction() throws ViewException {
        UIRegistry.getViewsManager().activate( LIST_VIEW );
    }
    
    @Action( eventType = "ServersComponent.Events.Views.List")
    public void onListView() throws ViewException {
        UIRegistry.getViewsManager().activate( LIST_VIEW );
    }
    
    @Action( eventType = "ServersComponent.Events.Actions.Start")
    public void onStartedEvent() {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open(StartWindow.class);
    }

    @Action( eventType = "ServersComponent.Events.Actions.Started")
    public void onStartedAction( AppEvent event ) {
        Server server = event.getArg(0);
        if ( server == null ) {
            UIRegistry.getNotificationsManager().error("Server record not provided");
            return;
        }

        IManagerService manager = UIRegistry.get(App.Attributes.Manager);
        if ( manager == null ) {
            UIRegistry.getNotificationsManager().error("Connection with servers manager has not been established!");
            Dispatcher.get().forwardEvent( MainComponent.Events.Views.Main );
            return;
        }

        try {
            UIRegistry.getStatusBar().status("Attempting to start server up...");
            UUID id = manager.start( server.toConfiguration() );
            UIRegistry.getStatusBar().status( String.format( "Kernel server started with ID#%s", id.toString() ) );

            UIRegistry.<ISwingWindowsManager>getWindowsManager().closeAll();
        } catch ( RemoteException e ) {
            log.error( e.getMessage(), e );
            UIRegistry.getNotificationsManager().error("Unable to start server instance!");
        }
    }

}
