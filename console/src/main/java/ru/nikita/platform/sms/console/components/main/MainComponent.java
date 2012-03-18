package ru.nikita.platform.sms.console.components.main;

import com.redshape.ui.application.events.EventType;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;
import com.redshape.ui.utils.UIRegistry;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainComponent extends AbstractComponent {

    public static class Events extends EventType {

        protected Events(String code) {
            super(code);
        }
        
        public static class Actions extends Events {
            
            protected Actions( String code ) {
                super(code);
            }
            
            public static final Actions Connected = new Actions("MainComponent.Events.Actions.Connected");
            public static final Actions ConnectionFailed = new Actions("MainComponent.Events.Actions.ConnectionFailed");
            public static final Actions StopConnection = new Actions("MainComponent.Events.Actions.StopConnection");
            public static final Actions Connect = new Actions("MainComponent.Events.Actions.Connect");
            
        }
        
        public static class Views extends Events {

            protected Views(String code) {
                super(code);
            }
            
            public static final Views Main = new Views("MainComponent.Events.Views.Main");
        }
    }
    
    public MainComponent() {
        super("main", "File");
    }

    @Override
    public void init() {
        this.addController(new MainController());
        this.addAction( new ComponentAction("Home", this, Events.Views.Main ) );
        this.addAction( new ComponentAction("Exit", this,  UIEvents.Core.Exit ) );
    }

}
