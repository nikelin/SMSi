package ru.nikita.platform.sms.console.components.plugins.registry;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import ru.nikita.platform.sms.console.components.plugins.registry.views.ListView;
import ru.nikita.platform.sms.console.components.plugins.registry.windows.RegisterWindow;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegistryController extends AbstractController {
    public static final String REGISTER_VIEW = "RegistryController.Views.RegisterWindow";
    public static final String LIST_VIEW = "RegistryController.Views.ListView";
    
    @Override
    protected void initEvents() {
        Dispatcher.get().addListener( RegistryComponent.Events.Views.List, this );
        Dispatcher.get().addListener( RegistryComponent.Events.Views.Register, this );
    }

    @Override
    protected void initViews() {
        UIRegistry.getViewsManager().register( new ListView(), LIST_VIEW );
    }
    
    @Action( eventType = "Plugins.RegistryComponent.Events.Views.List" )
    public void onListView( AppEvent event ) throws ViewException {
        UIRegistry.getViewsManager().activate( LIST_VIEW );
    }

    @Action( eventType = "Plugins.RegistryComponent.Events.Views.Register" )
    public void onRegisterView() throws ViewException {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open( new RegisterWindow() );
    }
    
}
