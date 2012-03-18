package ru.nikita.platform.sms.console.components.console;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;
import com.redshape.ui.application.annotations.Action;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;
import ru.nikita.platform.sms.console.components.console.windows.ConsoleWindow;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleController extends AbstractController {

    @Override
    protected void initEvents() {
        Dispatcher.get().addListener( ConsoleComponent.Events.Views.Open, this );
    }

    @Override
    protected void initViews() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Action( eventType = "ConsoleComponent.Events.Views.Open" )
    public void onOpenViewRequest( AppEvent event ) {
        UIRegistry.<ISwingWindowsManager>getWindowsManager().open( ConsoleWindow.class );
    }

}
