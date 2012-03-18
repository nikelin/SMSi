package ru.nikita.platform.sms.console.components.providers;

import com.redshape.persistence.dao.IManager;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.console.App;
import ru.nikita.platform.sms.console.components.providers.views.ListView;
import ru.nikita.platform.sms.console.components.providers.windows.CreateWindow;
import ru.nikita.platform.sms.console.data.records.Provider;
import ru.nikita.platform.sms.jobs.providers.RegisterProviderJob;
import ru.nikita.platform.sms.jobs.providers.RemoveProviderJob;
import ru.nikita.platform.sms.jobs.providers.StartProviderJob;
import ru.nikita.platform.sms.jobs.providers.StopProviderJob;
import ru.nikita.platform.sms.providers.IProviderModel;
import ru.nikita.platform.sms.services.IManagerService;

import java.net.URISyntaxException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/9/11
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProvidersController extends AbstractController {
    private static final Logger log =  Logger.getLogger(ProvidersController.class);

    public static final String CREATE_VIEW = "ProvidersControllers.Views.CreateView";
    public static final String LIST_VIEW = "ProvidersControllers.Views.ListView";
    
    @Override
    protected void initEvents() {
        Dispatcher.get().addListener( ProvidersComponent.Events.Actions.Start, this );
        Dispatcher.get().addListener( ProvidersComponent.Events.Actions.Stop, this );
        Dispatcher.get().addListener( ProvidersComponent.Events.Actions.Remove, this );
        Dispatcher.get().addListener( ProvidersComponent.Events.Actions.Created, this );
        Dispatcher.get().addListener( ProvidersComponent.Events.Views.List, this );
        Dispatcher.get().addListener( ProvidersComponent.Events.Views.Create, this );
    }

    @Override
    protected void initViews() {
        UIRegistry.getViewsManager().register( new ListView(), LIST_VIEW );
    }

    @Action( eventType = "ProvidersComponent.Events.Actions.Stop" )
    public void onStopRequest( AppEvent event ) {
        IManagerService service = UIRegistry.get( App.Attributes.Manager );
        if ( service == null ) {
            UIRegistry.getNotificationsManager().error("Kernel manager not defined!");
            return;
        }

        Provider provider = event.getArg(0);
        if ( provider == null ) {
            return;
        }

        StopProviderJob job = new StopProviderJob();
        job.setProviderId( provider.getId() );

        try {
            service.scheduleJob(provider.getServer().getId(), job);
        } catch ( RemoteException e ) {
            UIRegistry.getNotificationsManager().error("Remote service interaction failed!");
        }
    }

    @Action( eventType = "ProvidersComponent.Events.Actions.Start" )
    public void onStartRequest( AppEvent event ) {
        IManagerService service = UIRegistry.get( App.Attributes.Manager );
        if ( service == null ) {
            UIRegistry.getNotificationsManager().error("Kernel manager not defined!");
            return;
        }

        Provider provider = event.getArg(0);
        if ( provider == null ) {
            return;
        }

        StartProviderJob job = new StartProviderJob();
        job.setProviderId( provider.getId() );

        try {
            service.scheduleJob(provider.getServer().getId(), job);
        } catch ( RemoteException e ) {
            UIRegistry.getNotificationsManager().error("Remote service interaction failed!");
        }
    }

    @Action( eventType = "ProvidersComponent.Events.Actions.Remove" )
    public void onRemoveRequest( AppEvent event ) {
        IManagerService service = UIRegistry.get( App.Attributes.Manager );
        if ( service == null ) {
            UIRegistry.getNotificationsManager().error("Kernel manager not defined!");
            return;
        }

        Provider provider = event.getArg(0);
        if ( provider == null ) {
            return;
        }

        RemoveProviderJob job = new RemoveProviderJob();
        job.setProviderId( provider.getId() );
        
        try {
            service.scheduleJob(provider.getServer().getId(), job);
        } catch ( RemoteException e ) {
            UIRegistry.getNotificationsManager().error("Remote service interaction failed!");
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }
    
    @Action( eventType = "ProvidersComponent.Events.Actions.Created" )
    public void onCreatedActionRequest( AppEvent event ) {
        Provider provider = event.getArg(0);
        if ( provider == null ) {
            throw new UnhandledUIException("Provider not defined!");
        }

        IManagerService service = UIRegistry.get( App.Attributes.Manager );
        if ( service == null ) {
            UIRegistry.getNotificationsManager().error("Kernel servers manager not defined!");
        }

        try {
            service.scheduleJob( provider.getServer().getId(),
                    new RegisterProviderJob( provider.toModel() ) );
        } catch ( RemoteException e ) {
            UIRegistry.getNotificationsManager().error("Unable to proceed provider registration!");
            log.error( e.getMessage(), e );
            return;
        } catch ( URISyntaxException e ) {
            UIRegistry.getNotificationsManager().error("Illegal provider connection credentials!");
            log.error( e.getMessage(), e );
            return;
        }

        UIRegistry.<ISwingWindowsManager>getWindowsManager().close(CreateWindow.class);
        UIRegistry.getNotificationsManager().info("Provider successfully registered!");
    }

    @Action( eventType = "ProvidersComponent.Events.Views.List" )
    public void onListViewRequest( AppEvent event ) throws ViewException {
        UIRegistry.getViewsManager().activate( LIST_VIEW );
    }

    @Action( eventType = "ProvidersComponent.Events.Views.Create" )
    public void onCreateViewRequest( AppEvent event ) throws ViewException {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open( CreateWindow.class );
    }

}
