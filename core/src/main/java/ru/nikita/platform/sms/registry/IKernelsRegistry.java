package ru.nikita.platform.sms.registry;

import ru.nikita.platform.sms.registry.IRegistry;
import ru.nikita.platform.sms.registry.IRestorationData;
import ru.nikita.platform.sms.services.IKernelService;
import ru.nikita.platform.sms.services.data.ServerConfiguration;

import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/22/11
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IKernelsRegistry<V extends IRestorationData> extends IRegistry<ServerConfiguration, V> {

    public ServerConfiguration getConfiguration( UUID serverId );
    
    public UUID registerServer( ServerConfiguration configuration );

    public boolean removeServer( UUID serverId );

    public IKernelService getService( UUID serverId );

}
