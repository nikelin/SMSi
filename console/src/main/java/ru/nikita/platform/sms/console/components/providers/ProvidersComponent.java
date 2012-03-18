package ru.nikita.platform.sms.console.components.providers;

import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/9/11
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProvidersComponent extends AbstractComponent {

    public static class Events extends ComponentEvents {

        protected Events(String code) {
            super(code);
        }

        public static class Actions extends Events {
            
            protected Actions( String code ) {
                super(code);
            }


            public static final Actions Start = new Actions("ProvidersComponent.Events.Actions.Start");
            public static final Actions Stop = new Actions("ProvidersComponent.Events.Actions.Stop");
            public static final Actions Remove = new Actions("ProvidersComponent.Events.Actions.Remove");
            public static final Actions Created = new Actions("ProvidersComponent.Events.Actions.Created");
            
        }
        
        public static class Views extends Events {

            protected Views(String code) {
                super(code);
            }

            public static final Views List = new Views("ProvidersComponent.Events.Views.List");
            public static final Views Create = new Views("ProvidersComponent.Events.Views.Create");
        }

    }

    public ProvidersComponent() {
        super("providers", "Providers");
    }

    public void init() {
        this.addController( new ProvidersController() );
        this.addAction( new ComponentAction("List", this, Events.Views.List) );
        this.addAction( new ComponentAction("Create", this, Events.Views.Create ) );
        this.addAction( ComponentAction.Empty.create() );
    }
}
