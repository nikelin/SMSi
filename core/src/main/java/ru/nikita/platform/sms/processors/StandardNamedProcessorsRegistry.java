package ru.nikita.platform.sms.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/19/11
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class StandardNamedProcessorsRegistry implements INamedProcessorsRegistry {
    private Map<String, IProcessor> processors = new HashMap<String, IProcessor>();
    
    @Override
    public void registerProcessor(String name, IProcessor processor) {
        this.processors.put(name, processor);
    }

    /**
     * For a spring usage
     * @param processors
     */
    public void setProcessors( Map<String, IProcessor> processors ) {
        this.processors = processors;
    }

    public Map<String, IProcessor> getProcessors() {
        return this.processors;
    }

    @Override
    public IProcessor getProcessor(String name) {
        return this.processors.get(name);
    }

    @Override
    public IProcessor[] getList() {
        return this.processors.values().toArray( new IProcessor[this.processors.size()] );
    }

    @Override
    public void removeProcessor(String name) {
        this.processors.remove(name);
    }
}
