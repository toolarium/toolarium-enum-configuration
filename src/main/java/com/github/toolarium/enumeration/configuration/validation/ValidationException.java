/*
 * ValidationException.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import java.util.Arrays;
import java.util.Collection;

/**
 * Defines a validation exception
 * 
 * @author patrick
 */
public class ValidationException extends Exception {
    private static final long serialVersionUID = 2462346085465724118L;
    private String configurationValue;
    private Collection<Object> convertedValueList;

    
    /**
     * Constructor for ValidationException
     * 
     * @param message the message
     */
    public ValidationException(String message) {
        super(message);
        this.convertedValueList = null;
    }

    
    /**
     * Constructor for ValidationException
     * 
     * @param message the message
     * @param configurationValue the configuration value
     * @param convertedValue the converted value
     */
    @SuppressWarnings("unchecked")
    public ValidationException(String message, String configurationValue, Object convertedValue) {
        super(message);
        this.configurationValue = configurationValue;

        if (convertedValue != null) {
            if (Collection.class.isAssignableFrom(convertedValue.getClass())) {
                this.convertedValueList = (Collection<Object>)convertedValue;
            } else {
                this.convertedValueList = Arrays.asList(convertedValue);
            }
        }
    }

    
    /**
     * Constructor for ValidationException
     * 
     * @param message the message
     * @param configurationValue the configuration value
     * @param convertedValueList the converted value list
     */
    public ValidationException(String message, String configurationValue, Collection<Object> convertedValueList) {
        super(message);
        this.configurationValue = configurationValue;
        this.convertedValueList = convertedValueList;
    }


    /**
     * Constructor for ValidationException
     * 
     * @param message the message
     * @param e the nested exception
     */
    public ValidationException(String message, Exception e) {
        super(message, e);
    }
    
    
    /**
     * Get the configuration value
     *
     * @return the configuration value
     */
    public String getConfigurationValue() {
        return configurationValue;
    }
    
    
    /**
     * Get the converted value list
     *
     * @return the converted value of possible
     */
    public Collection<Object> getConvertedValueList() {
        return convertedValueList;
    }
    
    
    /**
     * Set the value
     *
     * @param configurationValue the configuration value
     * @param convertedValueList the converted value list
     */
    public void setValue(String configurationValue, Collection<Object> convertedValueList) {
        this.configurationValue = configurationValue;
        this.convertedValueList = convertedValueList;
    }
}
