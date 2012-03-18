package ru.nikita.platform.sms.services.data;

import com.redshape.daemon.DaemonState;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/14/11
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerConfiguration implements Serializable {
    private UUID id;
    private String name;
    private String host;
    private Integer port;
    private String path;
    private Date started;
    private Date lastPingDate;
    private DaemonState state;

    public ServerConfiguration() {
        this(null, null, null, null);
    }

    public ServerConfiguration( String name, String host, Integer port, String path) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public DaemonState getState() {
        return state;
    }

    public void setState(DaemonState state) {
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getLastPingDate() {
        return lastPingDate;
    }

    public void setLastPingDate(Date lastPingDate) {
        this.lastPingDate = lastPingDate;
    }
}
