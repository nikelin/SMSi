package ru.nikita.platform.sms.console.components.processors.templates;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.views.ViewException;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import ru.nikita.platform.sms.console.components.processors.templates.windows.CreateWindow;
import ru.nikita.platform.sms.console.components.providers.views.ListView;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemplatesController extends AbstractController {

    public static final String LIST_VIEW = "TemplatesController.Views.List";
    
    @Override
    protected void initEvents() {
        Dispatcher.get().addListener( TemplatesComponent.Events.Views.List, this );
        Dispatcher.get().addListener( TemplatesComponent.Events.Views.Create, this );
    }

    @Override
    protected void initViews() {
        UIRegistry.getViewsManager().register( new ListView(), LIST_VIEW );
    }

    @Action( eventType = "TemplatesComponent.Events.Views.List")
    public void onListView() throws ViewException {
        UIRegistry.getViewsManager().activate( LIST_VIEW );
    }

    @Action( eventType = "TemplatesComponent.Events.Views.Create")
    public void onCreateView() {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open( new CreateWindow() );
    }
}
