package ru.nikita.platform.sms.console.components.plugins;

import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;
import ru.nikita.platform.sms.console.components.plugins.registry.RegistryComponent;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class PluginsComponent extends AbstractComponent {
    
    public static class Events extends ComponentEvents {

        protected Events(String code) {
            super(code);
        }
        
        public static class Views extends Events {

            protected Views(String code) {
                super(code);
            }

            public static final Views Browse = new Views("PluginsComponent.Events.Views.Browse");
            public static final Views Import = new Views("PluginsComponent.Events.Views.Import");
            public static final Views Registry = new Views("PluginsComponent.Events.Views.Registry");
        }
    }

    public PluginsComponent() {
        super("plugins", "Plugin");
    }

    @Override
    public void init() {
        this.addController( new PluginsController() );

        this.addAction( new ComponentAction("Browse", this, Events.Views.Browse ) );
        this.addAction( new ComponentAction("Import", this, Events.Views.Import ) );
        this.addChild( new RegistryComponent() );
    }
}
