/*
 * EnumConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the enum configuration
 *
 * @author Meier Patrick
 * @version $Revision:  $
 */
public class EnumConfiguration extends AbstractEnumConfiguration {
    private static final long serialVersionUID = -5016414165364299512L;
    private String name;
    private List<EnumValueConfiguration> keyList;
    private List<String> markerInterfaceList;

    
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
     * Constructor
     */
    public EnumConfiguration() {
        keyList = new ArrayList<EnumValueConfiguration>();
        markerInterfaceList = new ArrayList<String>();
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
     * @param keyList the key list
     */
    public void setKeyList(List<EnumValueConfiguration> keyList) {
        this.keyList = keyList;
    }

    
    /**
     * Get the key list
     * 
     * @return the key list
     */
    public List<EnumValueConfiguration> getKeyList() {
        return keyList;
    }

    
    /**
     * Set the marker interface list
     * 
     * @param markerInterfaceList the marker interface list
     */
    public void setMarkerInterfaceList(List<String> markerInterfaceList) {
        this.markerInterfaceList = markerInterfaceList;
    }

    
    /**
     * Get the marker interface list
     * 
     * @return the marker interface list
     */
    public List<String> getMarkerInterfaceList() {
        return markerInterfaceList;
    }

    
    /**
     * Add a enumeration value configuration
     * 
     * @param enumValueConfiguration the enumeration value configuration
     */
    public void addEnumValueConfiguration(EnumValueConfiguration enumValueConfiguration) {
        
        if (enumValueConfiguration.getValidFrom() == null || getValidFrom().isAfter(enumValueConfiguration.getValidFrom())) {
            enumValueConfiguration.setValidFrom(getValidFrom());
        }
        
        if (enumValueConfiguration.getValidTill() == null || getValidTill().isBefore(enumValueConfiguration.getValidTill())) {
            enumValueConfiguration.setValidTill(getValidTill());
        }

        this.keyList.add(enumValueConfiguration);
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
               + ", keyList=" + keyList
               + ", markerInterfaceList=" + markerInterfaceList
               + "]";
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/
