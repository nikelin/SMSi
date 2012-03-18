package ru.nikita.platform.sms.console.components.bindings;

import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class BindingsComponent extends AbstractComponent {

    public static class Events extends ComponentEvents {

        protected Events(String code) {
            super(code);
        }
        
        public static class Actions extends Events {
            protected Actions(String code) {
                super(code);
            }
            
            public static final Actions Bind = new Actions("BindingsComponent.Events.Actions.Bind");
        }
        
        public static class Views extends Events {

            protected Views(String code) {
                super(code);
            }
            
            public static final Views List = new Views("BindingsComponent.Events.Views.List");
            public static final Views Create = new Views("BindingsComponent.Events.Views.Create");
        }
    }
    
    public BindingsComponent() {
        super("bindings", "Bindings");
    }

    @Override
    public void init() {
        this.addController( new BindingsController() );
        this.addAction( new ComponentAction("List", this, Events.Views.List ) );
    }

}
