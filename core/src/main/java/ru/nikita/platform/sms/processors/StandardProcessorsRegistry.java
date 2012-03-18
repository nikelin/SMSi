package ru.nikita.platform.sms.processors;

import com.redshape.utils.Commons;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ru.nikita.platform.sms.registry.AbstractRegistry;
import ru.nikita.platform.sms.registry.IRestorationData;
import ru.nikita.platform.sms.utils.ContextHandler;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class StandardProcessorsRegistry extends AbstractRegistry<IProcessor,
                                                                StandardProcessorsRegistry.ProcessorRestorationData>
                                        implements IProcessorsRegistry<StandardProcessorsRegistry.ProcessorRestorationData>,
                                                   ApplicationContextAware {

    public static final class ProcessorRestorationData implements IRestorationData {
        private IProcessorModel model;

        public ProcessorRestorationData(IProcessorModel model) {
            this.model = model;
        }

        public IProcessorModel getModel() {
            return model;
        }
    }

    private Map<IProcessorModel, IProcessor> processors;

    private ApplicationContext context;
    
    public StandardProcessorsRegistry( List<IRegistryInterceptor<IProcessor, ProcessorRestorationData>> interceptors ) {
        super(interceptors);
    }

    @Override
    public IProcessorModel findByName(String name) {
        for ( IProcessorModel model : this.getModels() ) {
            if ( model.getName().equals(name) ) {
                return model;
            }
        }

        return null;
    }

    @Override
    protected void init() {
        this.processors = new HashMap<IProcessorModel, IProcessor>();

        super.init();
    }

    protected ApplicationContext getContext() {
        return this.context;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public IProcessor createProcessor(IProcessorModel model) {
        Processor processor = new Processor(model);
        return processor;
    }

    @Override
    public IProcessor getProcessor(UUID processorId) {
        for ( IProcessorModel model : this.processors.keySet() ) {
            if ( model.getId().equals( processorId ) ) {
                return this.processors.get(model);
            }
        }

        return null;
    }

    @Override
    public void registerProcessor(IProcessorModel model, IProcessor processor) {
        model.setId(UUID.randomUUID() );
        this.processors.put(model, processor);
        this.onRecord(processor);
    }

    @Override
    public boolean isRegistered(IProcessorModel model) {
        return this.processors.containsKey(model);
    }

    @Override
    public IProcessor getProcessor(IProcessorModel model) {
        return this.processors.get(model);
    }

    @Override
    public void removeProcessor(IProcessorModel model) {
        this.processors.remove(model);
    }

    @Override
    public Collection<IProcessor> list() {
        return this.processors.values();
    }

    @Override
    public List<IProcessorModel> getModels() {
        return Commons.<IProcessorModel>list(this.processors.keySet().toArray(new IProcessorModel[this.processors.size()]));
    }

    @Override
    public IProcessorModel getModel( IProcessor processor ) {
        for ( Map.Entry<IProcessorModel, IProcessor> entry : this.processors.entrySet() ) {
            if ( entry.getValue() == processor ) {
                return entry.getKey();
            }
        }

        return null;
    }

    @Override
    public void onRestore(ProcessorRestorationData item) {
        this.registerProcessor( item.getModel(), this.createProcessor(item.getModel()) );
    }

    @Override
    public ProcessorRestorationData prepareRestoration(IProcessor item) {
        return new ProcessorRestorationData( this.getModel(item) );
    }
}
