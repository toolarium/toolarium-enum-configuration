/*
 * EnumConfigurationStoreException.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.exception;

import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue;
import com.github.toolarium.enumeration.configuration.store.dto.EnumConfigurationValue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Defines the enum configuration store exception.
 * 
 * @author patrick
 */
public class EnumConfigurationStoreException extends RuntimeException {
    private static final long serialVersionUID = -247429687108299666L;
    private Map<String, IEnumConfigurationValue<Object>> invalidConfigurationValueMap; 


    /**
     * Constructor for EnumConfigurationStoreException
     */
    public EnumConfigurationStoreException() {
        super();
    }

    
    /**
     * Constructor.
     * 
     * @param msg The message.
     */
    public EnumConfigurationStoreException(String msg) {
        super(msg);
    }

    
    /**
     * Constructor.
     * 
     * @param msg The message.
     * @param cause the cause
     */
    public EnumConfigurationStoreException(String msg, Throwable cause) {
        super(msg, cause);
    }

    
    /**
     * Add the invalid configuration
     *
     * @param key the key
     * @param value the value
     * @param convertedObjectList the converted value list or null (optional)
     */
    public void add(String key, String value, Collection<Object> convertedObjectList) {
        if (key == null || value == null) {
            return;
        }
        
        if (invalidConfigurationValueMap == null) {
            invalidConfigurationValueMap = new HashMap<>();            
        }
        
        Collection<Object> collection = null;
        if (convertedObjectList != null) {
            collection = convertedObjectList;
        }

        invalidConfigurationValueMap.put(key, new EnumConfigurationValue<>(value, collection));
    }

    
    /**
     * Get the key set of the invalid configurations
     *
     * @return the key set
     */
    public Set<String> keySet() {
        if (invalidConfigurationValueMap == null) {
            return Collections.emptySet();
        }
        
        return invalidConfigurationValueMap.keySet();
    }
    

    /**
     * Get the invalid configuration value
     *
     * @param key the configuration key
     * @return the configuration value
     */
    public IEnumConfigurationValue<?> getInvalidConfigurationValue(String key) {
        if (key == null || invalidConfigurationValueMap == null) {
            return null;
        }

        return invalidConfigurationValueMap.get(key);
    }
}
