package ru.nikita.platform.sms.bind;

import ru.nikita.platform.sms.processors.IProcessorModel;
import ru.nikita.platform.sms.processors.IProcessorsRegistry;
import ru.nikita.platform.sms.providers.IProvidersRegistry;
import ru.nikita.platform.sms.registry.AbstractRegistry;
import ru.nikita.platform.sms.providers.IProviderModel;
import ru.nikita.platform.sms.registry.IRestorationData;
import ru.nikita.platform.sms.utils.ContextHandler;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/13/11
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class StandardBindedPairsRegistry extends AbstractRegistry<IBindedPair, StandardBindedPairsRegistry.BindRestorationData>
        implements IBindedPairsRegistry<StandardBindedPairsRegistry.BindRestorationData> {
    public static final class BindRestorationData implements IRestorationData {
        private IBindedPair pair;

        public BindRestorationData(IBindedPair pair) {
            this.pair = pair;
        }

        public IBindedPair getPair() {
            return pair;
        }
    }

    private Map<IProviderModel, List<IBindedPair>> processors;

    public StandardBindedPairsRegistry( List<IRegistryInterceptor<IBindedPair, BindRestorationData>> interceptors ) {
        super(interceptors);
    }

    protected IProvidersRegistry<?> getProvidersRegistry() {
        return ContextHandler.instance().getContext().getBean(IProvidersRegistry.class);
    }

    protected IProcessorsRegistry<?> getProcessorsRegistry() {
        return ContextHandler.instance().getContext().getBean(IProcessorsRegistry.class);
    }

    @Override
    protected void init() {
        this.processors = new HashMap<IProviderModel, List<IBindedPair>>();

        super.init();
    }

    @Override
    public void registerProcessor(IBindedPair pair) {
        if ( !this.processors.containsKey(pair.getProvider()) ) {
            this.processors.put( pair.getProvider(), new ArrayList<IBindedPair>() );
        }

        pair.setId( UUID.randomUUID() );

        this.processors.get(pair.getProvider())
                .add(pair);

        this.onRecord(pair);
    }

    @Override
    public Collection<IBindedPair> getProcessors(UUID providerId) {
        List<IBindedPair> result = new ArrayList<IBindedPair>();
        for ( Map.Entry<IProviderModel, List<IBindedPair>> entry : this.processors.entrySet() ) {
            result.addAll( entry.getValue() );
        }

        return result;
    }

    @Override
    public Collection<IBindedPair> list() {
        Set<IBindedPair> result = new HashSet<IBindedPair>();
        for ( List<IBindedPair> pairs : this.processors.values() ) {
            result.addAll(pairs);
        }

        return result;
    }

    @Override
    public void onRestore(BindRestorationData item) {
        IBindedPair pair = item.getPair();

        IProviderModel providerModel = this.getProvidersRegistry().findByName(item.getPair().getProvider().getName());
        if ( providerModel != null ) {
            pair.getProvider().setId( providerModel.getId() );
        }

        IProcessorModel processorModel = this.getProcessorsRegistry().findByName(item.getPair().getProcessor().getName());
        if ( processorModel != null ) {
            pair.getProcessor().setId( processorModel.getId() );
        }

        this.registerProcessor( item.getPair() );
    }

    @Override
    public BindRestorationData prepareRestoration(IBindedPair item) {
        return new BindRestorationData(item);
    }
}
