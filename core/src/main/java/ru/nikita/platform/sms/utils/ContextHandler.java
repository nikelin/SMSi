package ru.nikita.platform.sms.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/23/11
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContextHandler implements ApplicationContextAware {
    private static ContextHandler instance;
    private ApplicationContext context;

    public ContextHandler() {
        if ( instance != null ) {
            throw new InstantiationError("Unable to re-instantiate singleton class");
        }

        instance = this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
    
    public ApplicationContext getContext() {
        return this.context;
    }
    
    public static ContextHandler instance() {
        return instance;
    }

}
