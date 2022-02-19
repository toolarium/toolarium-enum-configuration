/*
 * IEnumConfigurationValue.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import java.util.Collection;

/**
 * Defines the enum configuration value
 *  
 * @author patrick
 */
public interface IEnumConfigurationValue<D> {

    /**
     * Get the value. In case of a list it will be the first element.
     *
     * @return the value. In case of a list it will be the first element.
     */
    D getValue();
    
    
    /**
     * Get the converted value as list
     *
     * @return the value list
     */
    Collection<D> getValueList();
    
    
    /**
     * Get the string representation
     *
     * @return the string representation
     */
    String toString();
}
