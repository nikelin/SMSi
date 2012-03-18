package ru.nikita.platform.sms.listeners;

import com.redshape.utils.events.IEventListener;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.providers.ProviderEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class CDRListener implements IEventListener<ProviderEvent> {
    private static final Logger log = Logger.getLogger(CDRListener.class);
    
    @Override
    public void handleEvent(ProviderEvent event) {
        log.info("Logging into CDR report...");
    }
}
