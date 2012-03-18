package ru.nikita.platform.sms.console.tasks;

import com.redshape.daemon.DaemonState;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.utils.UIRegistry;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.console.App;
import ru.nikita.platform.sms.console.components.main.MainComponent;
import ru.nikita.platform.sms.services.IManagerService;

import java.rmi.RemoteException;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class KernelManagerPingTask extends TimerTask {
    private static final Logger log = Logger.getLogger(KernelManagerPingTask.class);

    protected void stopManager() {
        UIRegistry.getNotificationsManager().error("Connection with kernel manager has been lost!");
        UIRegistry.set( App.Attributes.Manager, null );
        Dispatcher.get().forwardEvent(MainComponent.Events.Views.Main);
    }

    @Override
    public void run() {
        try {
            IManagerService service = UIRegistry.get( App.Attributes.Manager );
            if ( service == null ) {
                return;
            }
    
            if ( !service.ping() /** || !service.state().equals(DaemonState.STARTED) **/ ) {
                this.stopManager();
            }
        } catch ( RemoteException e ) {
            log.error( e.getMessage(), e );
            this.stopManager();
        }
    }
}
