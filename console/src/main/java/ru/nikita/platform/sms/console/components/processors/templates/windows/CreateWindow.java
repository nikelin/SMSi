package ru.nikita.platform.sms.console.components.processors.templates.windows;

import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.bindings.render.ISwingRenderer;
import com.redshape.ui.data.bindings.render.components.ObjectUI;
import com.redshape.ui.utils.UIRegistry;
import ru.nikita.platform.sms.console.data.records.Template;
import ru.nikita.platform.sms.console.data.stores.TemplatesStore;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/12/11
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateWindow extends JFrame {

    private ObjectUI ui;
    
    public CreateWindow() {
        super();
        
        this.configUI();
        this.buildUI();
    }
    
    protected void configUI() {
        this.setTitle("New template");
        this.setSize(400, 500);
    }
    
    protected void buildUI() {
        try {
            JPanel panel = new JPanel();
            panel.setLayout( new BoxLayout(panel, BoxLayout.Y_AXIS) );
            
            this.ui = UIRegistry.<ISwingRenderer>getViewRendererFacade().createRenderer(Template.class)
                                                            .render(panel, Template.class);
            
            Box buttonsBox = Box.createHorizontalBox();
            buttonsBox.add( new JButton( new InteractionAction("Save", new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                    CreateWindow.this.onSave();
                }
            })) );

            buttonsBox.add( new JButton( new InteractionAction( "Cancel", new WindowCloseHandler(this) ) ) );
            panel.add(buttonsBox);
    
            this.add(panel);
        } catch ( Throwable e ) {
            UIRegistry.getNotificationsManager().error( e.getMessage() );
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }

    protected void onSave() {
        try {
            UIRegistry.getStoresManager().getStore(TemplatesStore.class).add( this.ui.<Template>createInstance() );
        } catch ( Throwable e ) {
            UIRegistry.getNotificationsManager().error( e.getMessage() );
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }
}
