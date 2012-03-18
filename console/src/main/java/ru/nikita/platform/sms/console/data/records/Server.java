package ru.nikita.platform.sms.console.data.records;

import com.redshape.daemon.DaemonState;
import com.redshape.ui.data.AbstractModelData;
import ru.nikita.platform.sms.services.data.ServerConfiguration;

import java.util.Date;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class Server extends AbstractModelData {

    public Server() {
        super();
    }

    public DaemonState getState() {
        return this.get( ServerModel.STATE );
    }
    
    public void setState( DaemonState state ) {
        this.set( ServerModel.STATE, state );
    }
    
    public UUID getId() {
        return this.get( ServerModel.ID );
    }
    
    public void setId( UUID id ) {
        this.set( ServerModel.ID, id );
    }
    
    public String getName() {
        return this.get(ServerModel.NAME);
    }
    
    public void setName( String name ) {
        this.set( ServerModel.NAME, name );
    }
    
    public void setHost( String host ) {
        this.set( ServerModel.HOST, host );
    }
    
    public String getHost() {
        return this.get( ServerModel.HOST );
    }
    
    public void setPort( Integer port ) {
        this.set( ServerModel.PORT, port );
    }
    
    public Integer getPort() {
        return this.get( ServerModel.PORT );
    }
    
    public void setLastPing( Date date ) {
        this.set( ServerModel.LAST_PINGED, date );
    }
    
    public Date getLastPing() {
        return this.get( ServerModel.LAST_PINGED );
    }
    
    public void setStartedOn( Date date ) {
        this.set( ServerModel.STARTED, date );
    }
    
    public Date getStartedOn() {
        return this.get( ServerModel.STARTED );
    }
    
    public String getPath() {
        return this.get( ServerModel.PATH );
    }
    
    public void setPath( String path ) {
        this.set( ServerModel.PATH, path );
    }
    
    public ServerConfiguration toConfiguration() {
        ServerConfiguration configuration = new ServerConfiguration();
        configuration.setId(this.getId());
        configuration.setName(this.getName());
        configuration.setPath(this.getPath());
        configuration.setHost(this.getHost());
        configuration.setPort( this.getPort() );
        configuration.setLastPingDate( this.getLastPing() );
        configuration.setStarted( this.getStartedOn() );
        configuration.setState( this.getState() );
        
        return configuration;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) { return false; }
        
        if ( !( obj instanceof Server ) ) {
            return false;
        }
        
        return ((Server) obj).getName().equals( this.getName() )
                && ( ((Server) obj).getId() == null ||
                        ((Server) obj).getId().equals( this.getId() ) );
    }
}
