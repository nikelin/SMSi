package ru.nikita.platform.sms.console.components.processors;

import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;
import ru.nikita.platform.sms.console.components.processors.templates.TemplatesComponent;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorsComponent extends AbstractComponent {

    public static class Events extends ComponentEvents {

        protected Events(String code) {
            super(code);
        }
        
        public static class Actions extends Events {
            
            protected Actions( String code ) {
                super(code);
            }
            
            public static final Actions Create = new Actions("ProcessorsComponent.Events.Actions.Create");
        }
        
        public static class Views extends Events {

            protected Views(String code) {
                super(code);
            }
            
            public static final Views List = new Views("ProcessorsComponent.Events.Views.List");
            public static final Views Create = new Views("ProcessorsComponent.Events.Views.Create");
        }
    }
    
    public ProcessorsComponent() {
        super("processors", "Processor");
    }

    @Override
    public void init() {
        this.addController( new ProcessorsController() );

        this.addChild( new TemplatesComponent() );
        this.addAction( new ComponentAction("List", this, Events.Views.List ) );
        this.addAction( new ComponentAction("Create", this, Events.Views.Create ) );
    }
}
