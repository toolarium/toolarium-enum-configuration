/*
 * EnumConfigurations.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.toolarium.enumeration.configuration.Version;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Defines the enumeration configurations
 * 
 * @author patrick
 */
public class EnumConfigurations implements Serializable {
    private static final long serialVersionUID = 8595845598162960442L;
    private String name;
    private String version;
    private Map<String, EnumConfiguration<? extends EnumKeyConfiguration>> enumConfigurationContentMap;

    
    /**
     * Constructor
     */
    public EnumConfigurations() {
        name = Version.PROJECT_NAME;
        version = Version.VERSION;
        enumConfigurationContentMap = new LinkedHashMap<>();
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
     * Get a {@link EnumConfiguration}.
     * @param name the name
     * @return the {@link EnumConfiguration}.
     */    
    public EnumConfiguration<? extends EnumKeyConfiguration> get(String name) {
        if (name == null) {
            return null;
        }
        
        return enumConfigurationContentMap.get(name.trim());
    }
    
    
    /**
     * Gets the enumeration configuration content list
     * 
     * @return true if it is empty
     */
    public Set<EnumConfiguration<? extends EnumKeyConfiguration>> getEnumConfigurationList() {
        Set<EnumConfiguration<? extends EnumKeyConfiguration>> result = new LinkedHashSet<EnumConfiguration<? extends EnumKeyConfiguration>>();
        for (Map.Entry<String, EnumConfiguration<? extends EnumKeyConfiguration>> e : enumConfigurationContentMap.entrySet()) {
            result.add(e.getValue());
        }
        
        return result;
    }
    
    
    /**
     * Sets the enumeration configuration content list
     * 
     * @param list the enumeration configuration content list
     */
    @JsonDeserialize(as = LinkedHashSet.class)
    public void setEnumConfigurationList(Set<EnumConfiguration<? extends EnumKeyConfiguration>> list) {
        enumConfigurationContentMap = new LinkedHashMap<String, EnumConfiguration<? extends EnumKeyConfiguration>>();
        
        for (EnumConfiguration<? extends EnumKeyConfiguration> e : list) {
            add(e);
        }
    }

    
    /**
     * Adds an {@link EnumConfiguration}.
     * 
     * @param enumConfiguration the {@link EnumConfiguration}
     * @return the added {@link EnumConfiguration}
     * @throws IllegalArgumentException In case the name doesn't exist
     */
    public EnumConfiguration<? extends EnumKeyConfiguration> add(EnumConfiguration<? extends EnumKeyConfiguration> enumConfiguration) {
        
        if (enumConfiguration == null || enumConfiguration.getName() == null || enumConfiguration.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid enum configuration:" + enumConfiguration);
        }

        String name = enumConfiguration.getName().trim();
        if (!enumConfigurationContentMap.containsKey(name)) {
            this.enumConfigurationContentMap.put(name, enumConfiguration);
            return enumConfiguration;
        }

        return null;
    }


    /**
     * Returns a list of mandatory configuration entries.
     *
     * @return the list of mandatory configuration entries.
     */
    public Set<EnumConfiguration<? extends EnumKeyConfiguration>> selectMandatoryConfigurationList() {
        Set<EnumConfiguration<? extends EnumKeyConfiguration>> result = new LinkedHashSet<>();
        
        for (Map.Entry<String, EnumConfiguration<? extends EnumKeyConfiguration>> e : enumConfigurationContentMap.entrySet()) {
            Set<? extends EnumKeyConfiguration> enumKeyConfigurationList = e.getValue().selectMandatoryConfigurationList();
            
            if (enumKeyConfigurationList != null && !enumKeyConfigurationList.isEmpty()) {
                EnumConfiguration<? extends EnumKeyConfiguration> enumConfigurationToAdd = new EnumConfiguration<>();
                enumConfigurationToAdd.setDescription(e.getValue().getDescription());
                enumConfigurationToAdd.setValidFrom(e.getValue().getValidFrom());
                enumConfigurationToAdd.setValidTill(e.getValue().getValidTill());
                enumConfigurationToAdd.setName(e.getValue().getName());
                enumConfigurationToAdd.setInterfaceList(e.getValue().getInterfaceList());
                enumConfigurationToAdd.setMarkerInterfaceList(e.getValue().getMarkerInterfaceList());
                enumConfigurationToAdd.setKeyList(enumKeyConfigurationList);
                result.add(enumConfigurationToAdd);
            }
        }
        
        return result;
    }

    
    /**
     * Returns a list of mandatory configuration entries.
     *
     * @return the list of mandatory configuration entries.
     */
    public Set<EnumConfiguration<? extends EnumKeyConfiguration>> selectMandatoryConfigurationListWithMissingDefaultValue() {
        Set<EnumConfiguration<? extends EnumKeyConfiguration>> result = new LinkedHashSet<>();
        
        for (Map.Entry<String, EnumConfiguration<? extends EnumKeyConfiguration>> e : enumConfigurationContentMap.entrySet()) {
            Set<? extends EnumKeyConfiguration> enumKeyConfigurationList = e.getValue().selectMandatoryListWithMissingDefaultValue();
            
            if (enumKeyConfigurationList != null && !enumKeyConfigurationList.isEmpty()) {
                EnumConfiguration<? extends EnumKeyConfiguration> enumConfigurationToAdd = new EnumConfiguration<>();
                enumConfigurationToAdd.setDescription(e.getValue().getDescription());
                enumConfigurationToAdd.setValidFrom(e.getValue().getValidFrom());
                enumConfigurationToAdd.setValidTill(e.getValue().getValidTill());
                enumConfigurationToAdd.setName(e.getValue().getName());
                enumConfigurationToAdd.setInterfaceList(e.getValue().getInterfaceList());
                enumConfigurationToAdd.setMarkerInterfaceList(e.getValue().getMarkerInterfaceList());
                enumConfigurationToAdd.setKeyList(enumKeyConfigurationList);
                result.add(enumConfigurationToAdd);
            }
        }
        
        return result;
    }

    
    /**
     * Returns a list of enumeration configuration which implementing the interfaces.
     *
     * @param interfacesToCompare the interfaces to compare
     * @param compareMarkerInterface true to compare the marker interfaces; otherwise the interfaces
     * @return the enumeration configuration which are implementing the defined interfaces
     */
    public Set<EnumConfiguration<? extends EnumKeyConfiguration>> selectEnumConfigurationByInterfaceList(Set<String> interfacesToCompare, boolean compareMarkerInterface) {
        Set<EnumConfiguration<? extends EnumKeyConfiguration>> result = new LinkedHashSet<>();
        
        for (Map.Entry<String, EnumConfiguration<? extends EnumKeyConfiguration>> e : enumConfigurationContentMap.entrySet()) {
            if (e.getValue().matchInterfaces(interfacesToCompare, compareMarkerInterface)) {
                result.add(e.getValue());
            }
        }
        
        return result;
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
        
        EnumConfigurations other = (EnumConfigurations)obj;
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
                + ", enumConfigurationList=" + getEnumConfigurationList() + "]";
    }
}
