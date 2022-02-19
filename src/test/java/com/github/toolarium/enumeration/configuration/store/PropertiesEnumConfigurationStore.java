/*
 * PropertiesEnumConfigurationStore.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import com.github.toolarium.enumeration.configuration.store.impl.AbstractBaseTypeEnumConfigurationStore;
import com.github.toolarium.enumeration.configuration.util.EnumUtil;
import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.stream.Collectors;
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
    private InputStream enumConfigurationResourceInputStream;
    

    /**
     * Constructor for PropertiesEnumConfigurationStore
     * 
     * @param supportDefaultValues true to support default values
     */
    public PropertiesEnumConfigurationStore(boolean supportDefaultValues) {
        properties = new Properties();
        enumConfigurationResourceInputStream = null;
        
        setSupportReturnDefaultValueIfMissing(supportDefaultValues);
    }

    
    /**
     * Constructor for PropertiesConfigurationStore
     * 
     * @param supportDefaultValues true to support default values
     * @param enumConfigurationResourceInputStream the input stream
     */
    public PropertiesEnumConfigurationStore(boolean supportDefaultValues, InputStream enumConfigurationResourceInputStream) {
        properties = new Properties();
        this.enumConfigurationResourceInputStream = enumConfigurationResourceInputStream;
        
        setSupportReturnDefaultValueIfMissing(supportDefaultValues);
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
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractConfigurationStore#convertToConfigurationKeyName(java.lang.Enum)
     */
    @Override
    protected <T extends Enum<T>> String convertToConfigurationKeyName(T configurationName) {
        // in this sample we lowercase the class name. To resolve the class we need some extra effort
        return combineKeyName(configurationName.getClass().getCanonicalName().toLowerCase(), configurationName.name().toLowerCase());
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractConfigurationStore#convertToConfigurationKey(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Enum<T>> T convertToConfigurationKey(String inputConfigurationName) {
        if (inputConfigurationName == null || inputConfigurationName.isBlank()) {
            return null;
        }
        
        String[] split = splitKeyName(inputConfigurationName); 
        if (split == null) {
            return null;
        }
        
        String className = split[0].trim();
        String enumName =  split[1].trim();
        int idx = className.lastIndexOf('.');
        if (idx > 0) {
            /* jptools to resolve
            List<String> list = ClassPath.getInstance().searchClassByPackageName(className.substring(0, idx));
            className = list.stream().filter(string -> classLowecaseName.equalsIgnoreCase(string)).findAny().orElse(null);
            */
            
            // use guava to resolve
            try {
                final String packageName = className.substring(0, idx);
                final String classLowecaseName = className;
                Class<?> enumClazz = ClassPath.from(ClassLoader.getSystemClassLoader()).getAllClasses().stream()
                                                        .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(packageName))
                                                        .map(clazz -> clazz.load())
                                                        .collect(Collectors.toSet()).stream().filter(clazz -> classLowecaseName.equalsIgnoreCase(clazz.getName())).findAny().orElse(null);
                if (enumClazz != null) {
                    className = enumClazz.getName();
                }
            } catch (IOException e) {
                LOG.warn("Error occured: " + e.getMessage(), e);
            }
        }
        
        try {
            Class<?> clazz = Class.forName(className);
            return EnumUtil.getInstance().valueOf((Class<T>) clazz, enumName);
        } catch (ClassNotFoundException e) {
            LOG.warn("Could not found [" + enumName + "] in class [" + className + "]: " + e.getMessage(), e);
        }
        
        return null;
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


    /**
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractConfigurationStore#getEnumConfigurationResourceInputStream()
     */
    @Override
    protected InputStream getEnumConfigurationResourceInputStream() {
        try {
            if (enumConfigurationResourceInputStream != null) {
                enumConfigurationResourceInputStream.reset();
            }
        } catch (IOException e) {
            LOG.warn("Error occured: " + e.getMessage(), e);
        }
        
        return enumConfigurationResourceInputStream;
    }
}
