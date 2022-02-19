/*
 * EnumConfigurationStoreException.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.exception;

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
    private Map<String, String> invalidConfigurationMap; 


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
     */
    public void add(String key, String value) {
        if (invalidConfigurationMap == null) {
            invalidConfigurationMap = new HashMap<>();            
        }
        
        invalidConfigurationMap.put(key, value);
    }
    

    /**
     * Get the key set of the invalid configurations
     *
     * @return the key set
     */
    public Set<String> keySet() {
        return invalidConfigurationMap.keySet();
    }
    
    
    /**
     * Get the invalid configuration value
     *
     * @param key the configuration key
     * @return the configuration value
     */
    public String getInvalidConfiguration(String key) {
        return invalidConfigurationMap.get(key);
    }
}
