package ru.nikita.platform.sms.console;

import com.redshape.applications.bootstrap.LoggingStarter;
import com.redshape.ui.application.AbstractApplication;
import com.redshape.ui.application.ApplicationException;
import com.redshape.ui.utils.UIConstants;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.AbstractMainWindow;
import com.redshape.ui.windows.DefaultMainWindow;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/9/11
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class App extends AbstractApplication {
    static {
        LoggingStarter.init();
    }

    private static final Logger log = Logger.getLogger( App.class );
    
    public static class Attributes implements UIConstants.Attribute {
        private String name;
        
        protected Attributes( String code ) {
            this.name = code;
        }

        @Override
        public String name() {
            return this.name;
        }

        public static final Attributes Manager = new Attributes("App.Attributes.Manager");
        public static final Attributes Connector = new Attributes("App.Attributes.Connector");
    }
    
    public App(ApplicationContext applicationContext, AbstractMainWindow context) throws ApplicationException {
        super(applicationContext, context);
    }
    
    public static void main( String[] args ) {
        try {
            App app = new App( loadContext(args[0]), new DefaultMainWindow("SMS Control Panel", new Dimension(600, 250) ) );

            app.start();
        } catch ( Throwable e ) {
            log.error( e.getMessage(), e );
        }
    }
    
}
