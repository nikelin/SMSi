package ru.nikita.platform.sms.console.components.bindings;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import com.redshape.utils.Commons;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.console.App;
import ru.nikita.platform.sms.console.components.bindings.views.ListView;
import ru.nikita.platform.sms.console.components.bindings.windows.CreateWindow;
import ru.nikita.platform.sms.console.data.records.Binding;
import ru.nikita.platform.sms.console.data.records.Server;
import ru.nikita.platform.sms.jobs.processors.BindProcessorJob;
import ru.nikita.platform.sms.services.IManagerService;

import java.net.URISyntaxException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class BindingsController extends AbstractController {
    private static final Logger log = Logger.getLogger(BindingsController.class);

    public static final String LIST_VIEW = "BindingsController.Views.List";
    
    @Override
    protected void initEvents() {
        Dispatcher.get().addListener( BindingsComponent.Events.Actions.Bind, this );
        Dispatcher.get().addListener( BindingsComponent.Events.Views.List, this );
        Dispatcher.get().addListener( BindingsComponent.Events.Views.Create, this );
    }

    @Override
    protected void initViews() {
        UIRegistry.getViewsManager().register( new ListView(), LIST_VIEW );
    }

    @Action( eventType = "BindingsComponent.Events.Views.List" )
    public void onListView( AppEvent event ) throws ViewException {
        UIRegistry.getViewsManager().activate( LIST_VIEW );
    }
    
    @Action( eventType = "BindingsComponent.Events.Actions.Bind" )
    public void onBindAction( AppEvent event ) {
        try {
            Binding binding = event.getArg(0);
            if ( binding == null ) {
                UIRegistry.getNotificationsManager().error("Binding not provided!");
                return;
            }
            
            if ( binding.getProcessor() == null || binding.getProvider() == null ) {
                UIRegistry.getNotificationsManager().error("Required binding attributes missed!");
                return;
            }
    
            IManagerService service = UIRegistry.get( App.Attributes.Manager );
            if ( service == null ) {
                UIRegistry.getNotificationsManager().error("Connection with manager has not been established!");
                return;
            }
    
            try {
                Server server = Commons.select(binding.getProcessor().getServer(), binding.getProvider().getServer());
    
                service.scheduleJob( server.getId(), new BindProcessorJob(binding.toModel()));
                UIRegistry.getNotificationsManager().info("Processor has bind successfull!");
            } catch ( URISyntaxException e ) {
                log.error( e.getMessage(), e );
                throw new UnhandledUIException( e.getMessage(), e );
            } catch ( RemoteException e ) {
                UIRegistry.getNotificationsManager().error("Remote service interaction failed!");
                log.error( e.getMessage(), e );
                throw new UnhandledUIException( e.getMessage(), e );
            } catch ( Throwable e ) {
                UIRegistry.getNotificationsManager().error("Unknown internal exception");
                log.error( e.getMessage(), e );
            }
        } catch ( Throwable e ) {
            log.error( e.getMessage(), e );
        }
    }

    @Action( eventType = "BindingsComponent.Events.Views.Create" )
    public void onCreateView( AppEvent event ) {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open(CreateWindow.class);
    }

}
