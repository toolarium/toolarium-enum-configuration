/*
 * EnumConfigurationResourceFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Defines the enumeration configurations
 * 
 * @author patrick
 */
public class EnumConfigurations implements Serializable {
    private static final long serialVersionUID = 8595845598162960442L;
    private Map<String, EnumConfiguration> enumConfigurationContentMap;

    
    /**
     * Constructor
     */
    public EnumConfigurations() {
        enumConfigurationContentMap = new HashMap<>();
    }
    
    

    /**
     * Adds a {@link EnumConfiguration}.
     * 
     * @param e the {@link EnumConfiguration}.
     */
    public void add(EnumConfiguration e) {
        if (e != null && e.getName() != null && !e.getName().trim().isEmpty()) {
            enumConfigurationContentMap.put(e.getName(), e);
        }
    }

    
    /**
     * Get a {@link EnumConfiguration}.
     * @param name the name
     * @return the {@link EnumConfiguration}.
     */    
    public EnumConfiguration get(String name) {
        if (name == null) {
            return null;
        }
        
        return enumConfigurationContentMap.get(name);
    }
    
    
    /**
     * Gets the enumeration configuration content list
     * 
     * @return true if it is empty
     */
    public List<EnumConfiguration> getEnumConfigurationList() {
        return new ArrayList<>(enumConfigurationContentMap.values());
    }
    
    
    /**
     * Sets the enumeration configuration content list
     * 
     * @param list the enumeration configuration content list
     */
    public void setEnumConfigurationList(List<EnumConfiguration> list) {
        for (EnumConfiguration e : list) {
            enumConfigurationContentMap.put(e.getName(), e);
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result;
        if (enumConfigurationContentMap != null) {
            result += enumConfigurationContentMap.hashCode();
        }
        
        return result;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        EnumConfigurations other = (EnumConfigurations) obj;
        if (enumConfigurationContentMap == null) {
            if (other.enumConfigurationContentMap != null) {
                return false;
            }
        } else if (!enumConfigurationContentMap.equals(other.enumConfigurationContentMap)) {
            return false;
        }
        
        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnumConfigurations [enumConfigurationContentMap=" + enumConfigurationContentMap + "]";
    }
}
