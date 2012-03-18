package ru.nikita.platform.sms.listeners;

import com.redshape.utils.StringUtils;
import com.redshape.utils.serializing.ObjectsFlusher;
import com.redshape.utils.serializing.ObjectsLoader;
import com.redshape.utils.serializing.ObjectsLoaderException;
import org.apache.log4j.Logger;
import ru.nikita.platform.sms.registry.IRegistry;
import ru.nikita.platform.sms.registry.IRestorationData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/22/11
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegistryRestorationListener<T, V extends IRestorationData>
                    implements IRegistry.IRegistryInterceptor<T, V> {
    private static final Logger log = Logger.getLogger(RegistryRestorationListener.class);

    private IRegistry<T, V> registry;
    private boolean liveFlush;
    private int flushChunkSize = 500;
    private int flushTicker;
    private String tmpPath;
    private ObjectsLoader loader;
    private ObjectsFlusher flusher;
    
    public RegistryRestorationListener( ObjectsLoader loader, ObjectsFlusher flusher ) {
        this.loader = loader;
        this.flusher = flusher;
    }

    public IRegistry getRegistry() {
        return registry;
    }

    public boolean isLiveFlush() {
        return liveFlush;
    }

    public void setLiveFlush(boolean liveFlush) {
        this.liveFlush = liveFlush;
    }

    public int getFlushChunkSize() {
        return flushChunkSize;
    }

    public void setFlushChunkSize(int flushChunkSize) {
        this.flushChunkSize = flushChunkSize;
    }

    public ObjectsLoader getLoader() {
        return loader;
    }

    public void setLoader(ObjectsLoader loader) {
        this.loader = loader;
    }

    public ObjectsFlusher getFlusher() {
        return flusher;
    }

    public void setFlusher(ObjectsFlusher flusher) {
        this.flusher = flusher;
    }

    public String getTmpPath() {
        return tmpPath;
    }

    public void setTmpPath(String tmpPath) {
        this.tmpPath = tmpPath;
    }

    protected File getRegistryFile() {
        return new File(
            this.getTmpPath() + File.separator + "sms.state." + StringUtils.fromCamelCase( registry.getClass().getName(), "_") + ".dat"
        );
    }
    
    @Override
    public void init(IRegistry<T, V> registry) {
        this.registry = registry;

        File stateHolder = this.getRegistryFile();
        if ( !stateHolder.exists() ) {
            log.info("State file for a current repository not exists");
            return;
        }
        
        try {
            for ( V item : this.getLoader().<List<V>>loadObject(new ArrayList<V>(), stateHolder) ) {
                registry.onRestore(item);
            }
        } catch ( ObjectsLoaderException e ) {
            log.error("Registry state loading failed", e);
            try {
                log.info("Cleaning up old state storage...");
                stateHolder.delete();
            } catch ( Throwable ex ) {
                log.error("State storage clean failed", ex );
            }
        }
    }

    @Override
    public void onRecord(T record) {
        if ( !this.isLiveFlush()
                && 1 + this.flushTicker++ < this.getFlushChunkSize() ) {
            return;
        }
        
        List<V> buffer = new ArrayList<V>();
        for ( T item : this.registry.list() ) {
            if ( item == null ) {
                continue;
            }
            buffer.add( this.registry.prepareRestoration(item) );
        }

        if ( buffer.isEmpty() ) {
            return;
        }
        
        File file = this.getRegistryFile();
        if ( !file.exists() ) {
            try {
                file.createNewFile();
            } catch ( IOException e ) {
                log.error("Unable to create state file!");
                return;
            }
        }

        try {
            this.getFlusher().flush( buffer, new FileOutputStream(file));
        } catch ( IOException e ) {
            log.error("I/O related exception", e );
        } catch ( ObjectsLoaderException e ) {
            log.error("Serialize related exception", e );
        }
    }

}
