package ru.nikita.platform.sms.console.components.plugins;

import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.AbstractController;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class PluginsController extends AbstractController {

    @Override
    protected void initEvents() {
        Dispatcher.get().addListener( PluginsComponent.Events.Views.Browse, this );
        Dispatcher.get().addListener( PluginsComponent.Events.Views.Registry, this );
        Dispatcher.get().addListener( PluginsComponent.Events.Views.Import, this );
    }

    @Override
    protected void initViews() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
