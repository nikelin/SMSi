package ru.nikita.platform.sms.console.components.servers;

import com.redshape.ui.application.events.EventType;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServersComponent extends AbstractComponent {
    public static class Events extends EventType {
        protected Events(String code) {
            super(code);
        }
        
        public static class Views extends Events {

            protected Views(String code) {
                super(code);
            }
            
            public static final Views List = new Views("ServersComponent.Events.Views.List");
        }
        
        public static class Actions extends Events {
            protected Actions(String code) {
                super(code);
            }

            public static final Actions Started = new Actions("ServersComponent.Events.Actions.Started");
            public static final Actions Start = new Actions("ServersComponent.Events.Actions.Start");
        }
    }
    
    public ServersComponent() {
        super("servers", "Kernel servers");
    }

    @Override
    public void init() {
        this.addController(new ServersController());
        this.addAction( new ComponentAction("List", this, Events.Views.List ));
        this.addAction( new ComponentAction("Start new", this, Events.Actions.Start ));
    }
}
