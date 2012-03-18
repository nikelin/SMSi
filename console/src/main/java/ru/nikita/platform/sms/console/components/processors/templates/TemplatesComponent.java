package ru.nikita.platform.sms.console.components.processors.templates;

import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemplatesComponent extends AbstractComponent {

    public static class Events extends ComponentEvents {

        protected Events(String code) {
            super(code);
        }
        
        public static class Actions extends Events {
            
            protected Actions( String code ) {
                super(code);
            }
            
            public static final Actions Create = new Actions("TemplatesComponent.Events.Actions.Create");
            
        }
        
        public static class Views extends Events {

            protected Views(String code) {
                super(code);
            }
            
            public static final Views List = new Views("TemplatesComponent.Events.Views.List");
            public static final Views Create = new Views("TemplatesComponent.Events.Views.Create");
        }
    }
    
    public TemplatesComponent() {
        super("templates", "Templates");
    }

    @Override
    public void init() {
        this.addController( new TemplatesController() );

        this.addAction( new ComponentAction("List", this, Events.Views.List ) );
        this.addAction( new ComponentAction("Create", this, Events.Views.Create ) );
    }
}
