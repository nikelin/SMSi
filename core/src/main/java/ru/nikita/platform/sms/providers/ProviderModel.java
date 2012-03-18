package ru.nikita.platform.sms.providers;

import ru.nikita.platform.sms.providers.adapters.IAdapterModel;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/15/11
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProviderModel implements IProviderModel {
    private UUID id;
    private ProtocolType protocolType;
    private String name;
    private String title;
    private String description;
    private IAdapterModel adapter;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public void setProtocolType( ProtocolType type ) {
        this.protocolType = type;
    }

    @Override
    public ProtocolType getProtocolType() {
        return this.protocolType;
    }

    @Override
    public void setName( String name ) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void setTitle( String title ) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setDescription( String description ) {
        this.description = description;
    }
    
    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setAdapterModel( IAdapterModel adapter ) {
        this.adapter = adapter;
    }

    @Override
    public IAdapterModel getAdapterModel() {
        return this.adapter;
    }
}
