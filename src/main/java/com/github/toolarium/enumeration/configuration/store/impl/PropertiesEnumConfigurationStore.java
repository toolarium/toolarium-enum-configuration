/*
 * PropertiesEnumConfigurationStore.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.impl;

import com.github.toolarium.enumeration.configuration.dto.SortedProperties;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationResourceResolver;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements a properties enum configuration store
 *  
 * @author patrick
 */
public class PropertiesEnumConfigurationStore extends AbstractBaseTypeEnumConfigurationStore {
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesEnumConfigurationStore.class);
    private Set<String> keySet;
    private Properties properties;

    
    /**
     * Constructor for PropertiesEnumConfigurationStore
     */
    public PropertiesEnumConfigurationStore() {
        super();
        keySet = new HashSet<>();
        properties = new Properties();
    }


    /**
     * Constructor for PropertiesConfigurationStore
     * 
     * @param enumConfigurationResourceResolver the {@link IEnumConfigurationResourceResolver}.
     */
    public PropertiesEnumConfigurationStore(IEnumConfigurationResourceResolver enumConfigurationResourceResolver) {
        this();
        setEnumConfigurationKeyResolver(new EnumConfigurationKeyResolver(enumConfigurationResourceResolver, true));
    }

    
    /**
     * Get the properties
     *
     * @return the properties
     */
    public Properties getProperties() {
        return new SortedProperties((Properties) properties.clone());
    }


    /**
     * Set the properties
     *
     * @param properties the properties
     */
    public void setProperties(Properties properties) {
        this.properties = properties;

        // get keys
        Set<String> result = new HashSet<>();
        for (Object key : properties.keySet()) {
            result.add("" + key);
        }
        keySet = result;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractBaseEnumConfigurationStore#loadConfiguration(java.lang.String)
     */
    @Override
    protected String loadConfiguration(String configurationKeyName) throws EnumConfigurationStoreException {
        if (configurationKeyName == null || configurationKeyName.isBlank()) {
            return null;
        }
        
        return properties.getProperty(configurationKeyName);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractBaseEnumConfigurationStore#writeConfiguration(java.lang.String, java.lang.String)
     */
    @Override
    protected <D> void writeConfiguration(String configurationKeyName, String configurationValue) throws EnumConfigurationStoreException {
        if (configurationKeyName != null && !configurationKeyName.isBlank()) {
            LOG.debug("Write [" + configurationKeyName + "]");
            
            if (!keySet.contains(configurationKeyName)) {
                keySet.add(configurationKeyName);
            }

            properties.setProperty(configurationKeyName, configurationValue);
        }
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractBaseEnumConfigurationStore#deleteConfiguration(java.lang.String)
     */
    @Override
    protected <D> String deleteConfiguration(String configurationKeyName) throws EnumConfigurationStoreException {
        if (configurationKeyName == null || configurationKeyName.isBlank()) {
            return null;
        }

        LOG.debug("Delete [" + configurationKeyName + "]");
        String value = loadConfiguration(configurationKeyName);
        properties.remove(configurationKeyName);
        
        if (keySet.contains(configurationKeyName)) {
            keySet.remove(configurationKeyName);
        }

        return value;
    }
    

    /**
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractBaseEnumConfigurationStore#readKeys()
     */
    @Override
    protected Set<String> readKeys() throws EnumConfigurationStoreException {
        return new TreeSet<String>(keySet);
    }
}
