/*
 * PropertiesEnumConfigurationStore.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.impl;

import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationResourceResolver;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements a properties enum configuration store
 *  
 * @author patrick
 */
public class PropertiesEnumConfigurationStore extends AbstractBaseTypeEnumConfigurationStore {
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesEnumConfigurationStore.class);
    private Properties properties;

    
    /**
     * Constructor for PropertiesEnumConfigurationStore
     */
    public PropertiesEnumConfigurationStore() {
        super();
        properties = new Properties();
        setSupportReturnDefaultValueIfMissing(true);
    }


    /**
     * Constructor for PropertiesEnumConfigurationStore
     * 
     * @param supportDefaultValues true to support default values
     */
    public PropertiesEnumConfigurationStore(boolean supportDefaultValues) {
        this();
        setSupportReturnDefaultValueIfMissing(supportDefaultValues);
    }

    
    /**
     * Constructor for PropertiesConfigurationStore
     * 
     * @param enumConfigurationResourceResolver the {@link IEnumConfigurationResourceResolver}.
     */
    public PropertiesEnumConfigurationStore(IEnumConfigurationResourceResolver enumConfigurationResourceResolver) {
        this();
        setEnumConfigurationResourceResolver(enumConfigurationResourceResolver);
    }

    
    /**
     * Constructor for PropertiesConfigurationStore
     * 
     * @param enumConfigurationResourceResolver the {@link IEnumConfigurationResourceResolver}.
     * @param supportDefaultValues true to support default values
     */
    public PropertiesEnumConfigurationStore(IEnumConfigurationResourceResolver enumConfigurationResourceResolver, boolean supportDefaultValues) {
        this(supportDefaultValues);
        setEnumConfigurationResourceResolver(enumConfigurationResourceResolver);
    }

    
    /**
     * Get the properties
     *
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractConfigurationStore#loadConfiguration(java.lang.String)
     */
    @Override
    protected String loadConfiguration(String configurationKeyName) throws EnumConfigurationStoreException {
        if (configurationKeyName == null || configurationKeyName.isBlank()) {
            return null;
        }
        
        return properties.getProperty(configurationKeyName);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractConfigurationStore#writeConfiguration(java.lang.String, java.lang.String)
     */
    @Override
    protected <D> void writeConfiguration(String configurationKeyName, String configurationValue) throws EnumConfigurationStoreException {
        if (configurationKeyName != null && !configurationKeyName.isBlank()) {
            LOG.debug("Store [" + configurationKeyName + "], [" + configurationValue + "]");            
            properties.setProperty(configurationKeyName, configurationValue);
        }
    }
}
