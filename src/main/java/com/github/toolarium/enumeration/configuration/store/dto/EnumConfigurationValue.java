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
public class EnumConfigurationValue<P> implements IEnumConfigurationValue<P> {
    //private Class<P> clazz;
    private String configurationValue;
    private Collection<P> valueList;
    private Iterator<P> iterator;

    
    /**
     * Constructor for EnumConfigurationValue
     * 
     * @param configurationValue the configuration value
     * @param valueList the value list
     */
    public EnumConfigurationValue(String configurationValue, Collection<P> valueList) {
        //this.clazz = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.configurationValue = configurationValue;
        this.valueList = valueList;
        this.iterator = null;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue#getValue()
     */
    @SuppressWarnings("unchecked")
    @Override
    public <D> D getValue() {
        if (valueList == null || valueList.isEmpty()) {
            return null;
        }
        
        if (iterator == null) {
            newIterator();
        }
        
        return (D) iterator.next();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue#getValueList()
     */
    @SuppressWarnings("unchecked")
    @Override
    public <D> Collection<D> getValueList() {
        return (Collection<D>) valueList;
    }

    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return configurationValue;
    }
    
    
    /**
     * Initialise iterator back
     */
    public void newIterator() {
        iterator = (Iterator<P>) valueList.iterator();
    }
}