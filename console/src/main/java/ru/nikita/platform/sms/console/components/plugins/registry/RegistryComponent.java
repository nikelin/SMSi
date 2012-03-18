package ru.nikita.platform.sms.console.components.plugins.registry;

import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegistryComponent extends AbstractComponent {

    public static class Events extends ComponentEvents {
        
        protected Events( String code ) {
            super(code);
        }
        
        public static class Views extends Events {
            
            protected Views( String code ) {
                super(code);
            }
            
            public static final Views List = new Views("Plugins.RegistryComponent.Events.Views.List");
            public static final Views Register = new Views("Plugins.RegistryComponent.Events.Views.Register");
            
        }
        
    }
    
    public RegistryComponent() {
        super("registry", "Registry");
    }

    @Override
    public void init() {
        this.addController( new RegistryController() );
        this.addAction( new ComponentAction("List", this, Events.Views.List ) );
        this.addAction( new ComponentAction("Register", this, Events.Views.Register ) );    }
}
