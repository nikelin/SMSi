package ru.nikita.platform.sms.console.components.main;

import com.redshape.daemon.events.ServiceBindExceptionEvent;
import com.redshape.daemon.events.ServiceBindedEvent;
import com.redshape.daemon.services.Connector;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.utils.StringUtils;
import com.redshape.utils.events.IEventListener;
import ru.nikita.platform.sms.console.App;
import ru.nikita.platform.sms.console.components.main.view.ConfigurationView;
import ru.nikita.platform.sms.console.components.servers.ServersComponent;
import ru.nikita.platform.sms.console.components.servers.views.ListView;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.console.tasks.KernelManagerPingTask;
import ru.nikita.platform.sms.services.IManagerService;
import ru.nikita.platform.sms.services.data.ServerConfiguration;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainController extends AbstractController {

    public static final String MAIN_VIEW = "MainComponent.Views.Main";
    
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private Timer timer = new Timer();
    
    @Override
    protected void initEvents() {
        Dispatcher.get().addListener( MainComponent.Events.Views.Main, this );
        Dispatcher.get().addListener( MainComponent.Events.Views.Actions.StopConnection, this );
        Dispatcher.get().addListener( MainComponent.Events.Views.Actions.Connect, this );
    }

    @Override
    protected void initViews() {
        UIRegistry.getViewsManager().register( new ConfigurationView(), MAIN_VIEW );
    }

    @Action( eventType = "MainComponent.Events.Actions.StopConnection" )
    public void onStopConnectionAction( AppEvent event ) throws ViewException {
        Connector connector = UIRegistry.get(App.Attributes.Connector);
        connector.stop();
        this.service.shutdownNow();
        this.service = Executors.newSingleThreadExecutor();
        this.stopTimer();
        UIRegistry.getStatusBar().status("Connection cancelled");
    }
    
    protected void stopTimer() {
        if ( this.timer != null ) {
            this.timer.cancel();
        }
        
        this.timer = new Timer();
    }
    
    @Action( eventType = "MainComponent.Events.Actions.Connect" )
    public void onConnectAction( AppEvent event ) throws ViewException {
        final Server server = event.getArg(0);
        
        final Connector<IManagerService> connector = UIRegistry.get( App.Attributes.Connector );
        connector.addEventListener(ServiceBindedEvent.class, new IEventListener<ServiceBindedEvent>() {
            @Override
            public void handleEvent(ServiceBindedEvent event) {
                UIRegistry.set( App.Attributes.Manager, event.getService() );
                connector.removeEventListener(ServiceBindedEvent.class, this);
                UIRegistry.getNotificationsManager().info("Kernel manager connected!");
                MainController.this.stopTimer();
                UIRegistry.getStatusBar().status("Connection established");
                Dispatcher.get().forwardEvent( MainComponent.Events.Actions.Connected );
                UIRegistry.getTimer().scheduleAtFixedRate( new KernelManagerPingTask(), 0, 1000 );
            }
        });
        connector.addEventListener(ServiceBindExceptionEvent.class, new IEventListener<ServiceBindExceptionEvent>() {
            @Override
            public void handleEvent(ServiceBindExceptionEvent event) {
                connector.removeEventListener(ServiceBindExceptionEvent.class, this);
                UIRegistry.getStatusBar().status("Unable to establish connection with kernel manager!");
                MainController.this.stopTimer();
                Dispatcher.get().forwardEvent( MainComponent.Events.Actions.ConnectionFailed );
            }
        });

        timer.scheduleAtFixedRate(new TimerTask() {
            private int counter;

            @Override
            public void run() {
                if (this.counter > 3) {
                    this.counter = 0;
                }

                UIRegistry.getStatusBar().status("Connecting" + (StringUtils.repeat(".", this.counter++)));
            }
        }, 0, 1000);


        this.service.execute(
            new Runnable() {
                @Override
                public void run() {
                    connector.find(server.getHost(), server.getPort(), server.getPath());
                }
            }
        );
    }

    @Action( eventType = "MainComponent.Events.Views.Main")
    public void onMainView() throws ViewException {
        if ( UIRegistry.get( App.Attributes.Manager ) == null ) {
            UIRegistry.getViewsManager().activate( MAIN_VIEW );
        } else {
            Dispatcher.get().forwardEvent(ServersComponent.Events.Views.List);
        }
    }
}
