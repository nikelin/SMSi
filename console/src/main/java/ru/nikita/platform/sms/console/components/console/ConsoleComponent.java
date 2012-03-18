package ru.nikita.platform.sms.console.components.console;

import com.redshape.ui.application.events.components.ComponentEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

import java.awt.*;
import java.awt.event.ComponentEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleComponent extends AbstractComponent {

    public static class Events extends ComponentEvents {

        protected Events(String code) {
            super(code);
        }

        public static class Actions extends Events {

            protected Actions(String code) {
                super(code);
            }
            
            public static final Actions ExecuteScript = new Actions("ConsoleComponent.Events.Actions.ExecuteScript");
        }
        
        public static class Views extends Events {

            protected Views(String code) {
                super(code);
            }
            
            public static final Views Open = new Views("ConsoleComponent.Events.Views.Open");
            public static final Views Library = new Views("ConsoleComponent.Events.Views.Library");
        }
    }
    
    public ConsoleComponent() {
        super("console", "Console");
    }

    @Override
    public void init() {
        this.addController( new ConsoleController() );
        this.addAction( new ComponentAction("Open", this, Events.Views.Open ) );
    }
}
