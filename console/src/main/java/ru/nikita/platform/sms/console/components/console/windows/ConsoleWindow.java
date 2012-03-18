package ru.nikita.platform.sms.console.components.console.windows;

import com.redshape.ascript.EvaluationException;
import com.redshape.ascript.IEvaluator;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.utils.StringUtils;
import ru.nikita.platform.sms.console.components.console.ConsoleComponent;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author nikelin
 * @date 19/04/11
 * @package com.api.deployer.ui.components.system.windows
 */
public class ConsoleWindow extends JFrame {
    private JTextArea consoleAreaComponent;
    private JTextField commandLineComponent;
    private JButton executeButton;
    private FormPanel executeDetailsPanel;

    private boolean turnedOff;
    private boolean isConfigured;

    private Queue<String> prev = new LinkedBlockingQueue<String>();
    private Queue<String> next = new LinkedBlockingQueue<String>();

    public ConsoleWindow() {
        super();

        this.buildUI();
        this.configUI();
    }

    protected IEvaluator getEvaluator() {
        return UIRegistry.getContext().getBean( IEvaluator.class );
    }

    @SuppressWarnings("deprecation")
    protected void appendConsoleMessage( String message ) {
        this.consoleAreaComponent.append( new Date().toLocaleString() );
        this.consoleAreaComponent.append(" - ");
        this.consoleAreaComponent.append(message);
        this.consoleAreaComponent.append("\n");
    }

    protected void turnOff() {
        this.turnedOff = true;
        this.executeButton.setEnabled(false);
    }

    protected boolean isTurnedOff() {
        return this.turnedOff;
    }

    protected void buildUI() {
        this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );

        JTabbedPane pane = new JTabbedPane();
        pane.add( "Console", this.createConsolePane() );
        this.add(pane);
    }


    protected JComponent createConsolePane() {
        JPanel component = new JPanel();
        component.setSize(500, 400);
        component.setLayout( new BoxLayout(component, BoxLayout.Y_AXIS ) );

        component.add( new JLabel("API Script Console") );

        this.consoleAreaComponent = new JTextArea(10, 25);
        this.consoleAreaComponent.setEditable(false);
        JScrollPane consoleAreaWrapper = new JScrollPane(this.consoleAreaComponent);
        component.add( consoleAreaWrapper );

        JPanel commandLinePanel = new JPanel();
        commandLinePanel.add( this.commandLineComponent = new JTextField(45) );

        this.commandLineComponent.addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch ( keyEvent.getKeyCode() ) {
                    case KeyEvent.VK_ENTER:
                        ConsoleWindow.this.onExecute();
                        break;
                    case KeyEvent.VK_UP:
                        ConsoleWindow.this.onPrevRequest();
                        break;
                    case KeyEvent.VK_DOWN:
                        ConsoleWindow.this.onNextRequest();
                        break;
                }
            }
        });

        commandLinePanel.add( this.executeButton =  new JButton(
                new InteractionAction(
                        "Execute",
                        new IEventHandler() {
                            @Override
                            public void handle(AppEvent event) {
                                ConsoleWindow.this.onExecute();
                            }
                        }
                )
        ));

        commandLinePanel.add( new JButton(
                new InteractionAction(
                        "Reset",
                        new IEventHandler() {
                            @Override
                            public void handle( AppEvent event ) {
                                try {
                                    ConsoleWindow.this.onReset();
                                } catch ( EvaluationException e ) {
                                    ConsoleWindow.this.turnOff();
                                    ConsoleWindow.this.appendConsoleMessage( "Error: " + e.getMessage() );
                                }
                            }
                        }
                )
        ));
        
        commandLinePanel.add( new JButton( new InteractionAction("Close", new WindowCloseHandler(this))));

        component.add(commandLinePanel);
        return component;
    }

    protected void onReset() throws EvaluationException {
        int count = this.getEvaluator().getRootContext().getObjectsCount();
        this.appendConsoleMessage("Cleared " + count + " objects.");
        this.getEvaluator().reset();
    }

    protected void onPrevRequest() {
        if ( this.prev.isEmpty() ) {
            this.prev.addAll( this.next );
            this.next.clear();
        }

        String command = this.prev.poll();
        this.next.add( command );
        this.commandLineComponent.setText(command);

        Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this.commandLineComponent);
    }

    protected void onNextRequest() {
        if ( this.next.isEmpty() ) {
            this.next.addAll( this.prev );
            this.prev.clear();
        }

        String command = this.next.poll();
        this.prev.add( command );

        this.commandLineComponent.setText(command);

        Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this.commandLineComponent);
    }

    protected void onExecute() {
        if ( this.isTurnedOff() ) {
            return;
        }

        String eval = this.commandLineComponent.getText();
        this.appendConsoleMessage( eval );
        this.commandLineComponent.setText("");

        this.prev.add( eval );

        try {
            this.appendConsoleMessage( String.valueOf(this.getEvaluator().evaluate(eval)) );
        } catch ( Throwable e ) {
            this.appendConsoleMessage( "Error: " + e.getMessage() );
        }

        Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this.commandLineComponent);
    }

    protected void configUI() {
        this.setSize( 500, 400 );
        this.setTitle("Scripting console");
    }

}