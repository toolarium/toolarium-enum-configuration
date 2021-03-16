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
    private String name;
    private String version;
    private Map<String, EnumConfiguration> enumConfigurationContentMap;

    
    /**
     * Constructor
     */
    public EnumConfigurations() {
        name = "";
        version = "";
        enumConfigurationContentMap = new HashMap<>();
    }

    
    /**
     * The name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    
    /**
     * Sets the name
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * The version
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    
    
    /**
     * Sets the version
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
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
        if (name != null) {
            result += name.hashCode();
        }

        result = prime * result;
        if (version != null) {
            result += version.hashCode();
        }

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
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }

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
        return "EnumConfigurations [name=" + name 
                + ", version=" + version 
                + ", enumConfigurationContentMap=" + enumConfigurationContentMap + "]";
    }
}
