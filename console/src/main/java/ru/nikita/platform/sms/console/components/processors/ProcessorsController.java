package ru.nikita.platform.sms.console.components.processors;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.console.App;
import ru.nikita.platform.sms.console.components.processors.views.ListView;
import ru.nikita.platform.sms.console.components.processors.windows.CreateWindow;
import ru.nikita.platform.sms.console.data.records.Processor;
import ru.nikita.platform.sms.jobs.processors.RegisterProcessorJob;
import ru.nikita.platform.sms.services.IManagerService;

import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorsController extends AbstractController {
    private static final Logger log = Logger.getLogger(ProcessorsController.class);
    
    public static final String LIST_VIEW = "ProcessorsControllers.Views.List";
    
    @Override
    protected void initEvents() {
        Dispatcher.get().addListener( ProcessorsComponent.Events.Actions.Create, this );
        Dispatcher.get().addListener( ProcessorsComponent.Events.Views.List, this );
        Dispatcher.get().addListener( ProcessorsComponent.Events.Views.Create, this );
    }

    @Override
    protected void initViews() {
        UIRegistry.getViewsManager().register( new ListView(), LIST_VIEW );
    }

    @Action( eventType = "ProcessorsComponent.Events.Actions.Create")
    public void onCreateAction( AppEvent event ) {
        Processor record = event.getArg(0);
        if ( record == null ) {
            UIRegistry.getNotificationsManager().error("Processor record not given!");
            return;
        }

        IManagerService service = UIRegistry.get(App.Attributes.Manager);
        if ( service == null ) {
            UIRegistry.getNotificationsManager().error("Connection with manager has not been established!");
            return;
        }

        RegisterProcessorJob job = new RegisterProcessorJob();
        job.setModel( record.toModel() );

        try {
            service.scheduleJob( record.getServer().getId(), job );
        } catch ( RemoteException e ) {
            UIRegistry.getNotificationsManager().error("Remote service interaction failed!");
            log.error( e.getMessage(), e );
        }
    }
    
    @Action( eventType = "ProcessorsComponent.Events.Views.List")
    public void onListView( AppEvent event ) throws ViewException {
        UIRegistry.getViewsManager().activate( LIST_VIEW );
    }
    
    @Action( eventType = "ProcessorsComponent.Events.Views.Create" )
    public void onCreateView( AppEvent event ) {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open( new CreateWindow() );
    }
}
