/*
 * EnumConfigurationContent.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the enum configuration content
 *
 * @author Meier Patrick
 * @version $Revision:  $
 */
public class EnumConfigurationContent extends AbstractEnumConfigurationContent {
    private static final long serialVersionUID = -5016414165364299512L;
    private String name;
    private String category;
    private List<EnumValueConfigurationContent> keyList;

    
    /**
     * Constructor 
     */
    public EnumConfigurationContent() {
        keyList = new ArrayList<EnumValueConfigurationContent>();
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
     * Get the configuration category
     * 
     * @return the configuration category
     */
    public String getCategory() {
        return category;
    }
    
    
    /**
     * Set the configuration category
     * 
     * @param category the configuration category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    
    /**
     * Get the key list
     * 
     * @return the key list
     */
    public List<EnumValueConfigurationContent> getKeyList() {
        return keyList;
    }

    
    /**
     * Add a enumeration value configuration
     * 
     * @param enumValueConfiguration the enumeration value configuration
     */
    public void addEnumValueConfiguration(EnumValueConfigurationContent enumValueConfiguration) {
        this.keyList.add(enumValueConfiguration);
    }

    
    /**
     * Set the key list
     * 
     * @param keyList the key list
     */
    public void setKeyList(List<EnumValueConfigurationContent> keyList) {
        this.keyList = keyList;
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
        if (category != null) {
            result += category.hashCode();
        }

        result = prime * result;
        if (keyList != null) {
            result += keyList.hashCode();
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

        EnumConfigurationContent other = (EnumConfigurationContent)obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        if (category == null) {
            if (other.category != null) {
                return false;
            }
        } else if (!category.equals(other.category)) {
            return false;
        }

        if (keyList == null) {
            if (other.keyList != null) {
                return false;
            }
        } else if (!keyList.equals(other.keyList)) {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnumConfigurationContent [name=" + name 
               + ", category=" + category
               + ", description=" + getDescription()
               + ", validFrom=" + getValidFrom() 
               + ", validTill=" + getValidTill() 
               + ", keyList=" + keyList
               + "]";
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/
