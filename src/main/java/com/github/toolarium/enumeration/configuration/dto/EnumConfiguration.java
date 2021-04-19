/*
 * EnumConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Defines the enum configuration
 *
 * @author patrick
 */
public class EnumConfiguration extends AbstractEnumConfiguration {
    /** Defines the max timestamp */
    public static final String MAX_TIMESTAMP_STRING = "9999-12-31T12:00:00.000Z";

    /** Defines the max date */
    public static final String MAX_DATE_STRING = "9999-12-31";

    /** Defines the max time */
    public static final String MAX_TIME_STRING = "23:59:59.999";

    private static final long serialVersionUID = -5016414165364299512L;
    private String name;
    private Map<String, EnumValueConfiguration> keyList;
    private Set<String> interfaceList;
    private Set<String> markerInterfaceList;

    
    /**
     * Constructor
     */
    public EnumConfiguration() {
        super();
        name = null;
        keyList = new LinkedHashMap<String, EnumValueConfiguration>();
        interfaceList = new LinkedHashSet<String>();
        markerInterfaceList = new LinkedHashSet<String>();
    }

    
    /**
     * Constructor
     * 
     * @param name the name
     */
    public EnumConfiguration(String name) {
        this();
        setName(name);
    }
    
    
    /**
     * Get the configuration name
     * 
     * @return the configuration name
     */
    public String getName() {
        return name;
    }
    
    
    /**
     * Set the configuration name
     * 
     * @param name the configuration name
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * Set the key list
     * 
     * @param keyList the key list.
     */
    public void setKeyList(Set<EnumValueConfiguration> keyList) {
        this.keyList = new LinkedHashMap<String, EnumValueConfiguration>();
        
        if (keyList != null) {
            for (EnumValueConfiguration e : keyList) {
                add(e);
            }
        }
    }

    
    /**
     * Get the key list
     * 
     * @return the key list
     */
    public Set<EnumValueConfiguration> getKeyList() {
        Set<EnumValueConfiguration> result = new LinkedHashSet<EnumValueConfiguration>();
        for (Map.Entry<String, EnumValueConfiguration> e : keyList.entrySet()) {
            result.add(e.getValue());
        }
        
        return result;
    }

    
    /**
     * Set the interface list
     * 
     * @param interfaceList the interface list
     */
    public void setInterfaceList(Set<String> interfaceList) {
        this.interfaceList = interfaceList;
    }

    
    /**
     * Get the interface list
     * 
     * @return the interface list
     */
    public Set<String> getInterfaceList() {
        return interfaceList;
    }

    
    /**
     * Set the marker interface list
     * 
     * @param markerInterfaceList the marker interface list
     */
    public void setMarkerInterfaceList(Set<String> markerInterfaceList) {
        this.markerInterfaceList = markerInterfaceList;
    }

    
    /**
     * Get the marker interface list
     * 
     * @return the marker interface list
     */
    public Set<String> getMarkerInterfaceList() {
        return markerInterfaceList;
    }

    
    /**
     * Adds an {@link EnumValueConfiguration} and corrects validFrom / validTill in case it's not consistent regarding the parent element.
     * 
     * @param enumValueConfiguration the {@link EnumValueConfiguration}
     * @return the added enum value configuration
     * @throws IllegalArgumentException In case the key doesn't exist
     */
    public EnumValueConfiguration add(EnumValueConfiguration enumValueConfiguration) {
        
        if (enumValueConfiguration == null || enumValueConfiguration.getKey() == null || enumValueConfiguration.getKey().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid enum value configuration:" + enumValueConfiguration);
        }
        
        if (enumValueConfiguration.getValidFrom() == null || getValidFrom().isAfter(enumValueConfiguration.getValidFrom())) {
            enumValueConfiguration.setValidFrom(getValidFrom());
        }
        
        if (enumValueConfiguration.getValidTill() == null || getValidTill().isBefore(enumValueConfiguration.getValidTill())) {
            enumValueConfiguration.setValidTill(getValidTill());
        }

        String key = enumValueConfiguration.getKey().trim();
        if (!keyList.containsKey(key)) {
            this.keyList.put(key, enumValueConfiguration);
            return enumValueConfiguration;
        }
        
        return null;
    }


    /**
     * Returns a list of mandatory {@link EnumValueConfiguration}.
     *
     * @return the list of mandatory {@link EnumValueConfiguration}.
     */
    public Set<EnumValueConfiguration> selectMandatoryEnumValueConfigurationList() {
        Set<EnumValueConfiguration> result = new LinkedHashSet<EnumValueConfiguration>();
        for (Map.Entry<String, EnumValueConfiguration> e : keyList.entrySet()) {
            if (e.getValue().isMandatory()) {
                result.add(e.getValue());
            }
        }
        
        return result;
    }

    
    /**
     * Returns a list of mandatory {@link EnumValueConfiguration} with no default value
     *
     * @return the list of mandatory {@link EnumValueConfiguration}.
     */
    public Set<EnumValueConfiguration> selectMandatoryEnumValueConfigurationListWithMissingDefaultValue() {
        Set<EnumValueConfiguration> result = new LinkedHashSet<EnumValueConfiguration>();
        for (Map.Entry<String, EnumValueConfiguration> e : keyList.entrySet()) {
            if (e.getValue().isMandatory() && !e.getValue().hasDefaultValue()) {
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
        int result = super.hashCode();

        result = prime * result;
        if (name != null) {
            result += name.hashCode();
        }

        result = prime * result;
        if (keyList != null) {
            result += keyList.hashCode();
        }

        result = prime * result;
        if (interfaceList != null) {
            result += interfaceList.hashCode();
        }

        result = prime * result;
        if (markerInterfaceList != null) {
            result += markerInterfaceList.hashCode();
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

        if (!super.equals(obj)) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }

        EnumConfiguration other = (EnumConfiguration)obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        if (keyList == null) {
            if (other.keyList != null) {
                return false;
            }
        } else if (!keyList.equals(other.keyList)) {
            return false;
        }

        if (interfaceList == null) {
            if (other.interfaceList != null) {
                return false;
            }
        } else if (!interfaceList.equals(other.interfaceList)) {
            return false;
        }

        if (markerInterfaceList == null) {
            if (other.markerInterfaceList != null) {
                return false;
            }
        } else if (!markerInterfaceList.equals(other.markerInterfaceList)) {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnumConfiguration [name=" + name 
               + ", description=" + getDescription()
               + ", validFrom=" + getValidFrom() 
               + ", validTill=" + getValidTill() 
               + ", keyList=" + getKeyList()
               + ", interfaceList=" + getInterfaceList()
               + ", markerInterfaceList=" + getMarkerInterfaceList()
               + "]";
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/
