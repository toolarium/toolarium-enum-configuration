/*
 * EnumConfigurationValue.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.dto;

import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue;
import java.util.Collection;
import java.util.Iterator;


/**
 * Implements the {@link IEnumConfigurationValue}.
 * 
 * @author patrick
 */
public class EnumConfigurationValue<D> implements IEnumConfigurationValue<D> {
    private String configurationValue;
    private Collection<D> valueList;
    private Iterator<D> iterator;

    
    /**
     * Constructor for EnumConfigurationValue
     * 
     * @param configurationValue the configuration value
     * @param valueList the value list
     */
    public EnumConfigurationValue(String configurationValue, Collection<D> valueList) {
        this.configurationValue = configurationValue;
        this.valueList = valueList;
        this.iterator = null;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue#getValue()
     */
    @Override
    public D getValue() {
        if (valueList == null || valueList.isEmpty()) {
            return null;
        }
        
        if (iterator == null) {
            iterator = iterator();
        }
        
        return iterator.next();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue#getValueList()
     */
    @Override
    public Collection<D> getValueList() {
        return valueList;
    }

    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return configurationValue;
    }
    
    
    /**
     * Get the iterator back
     *
     * @return the itertaor
     */
    protected Iterator<D> iterator() {
        return valueList.iterator();
    }
}