package ru.nikita.platform.sms.processors;

import ru.nikita.platform.sms.registry.IRegistry;
import ru.nikita.platform.sms.registry.IRestorationData;

import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IProcessorsRegistry<T extends IRestorationData> extends IRegistry<IProcessor, T> {

    public IProcessorModel findByName( String name );
    
    public IProcessorModel getModel( IProcessor processor );
    
    public IProcessor createProcessor( IProcessorModel model );
    
    public void registerProcessor( IProcessorModel model, IProcessor processor );

    public boolean isRegistered( IProcessorModel model );

    public IProcessor getProcessor( UUID processorId );
    
    public IProcessor getProcessor( IProcessorModel model );

    public void removeProcessor( IProcessorModel model );
    
    public List<IProcessorModel> getModels();
    
}
