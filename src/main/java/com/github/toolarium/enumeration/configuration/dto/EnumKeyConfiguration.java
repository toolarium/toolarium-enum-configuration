/*
 * EnumKeyConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Defines the enum key configuration  
 * 
 * @author patrick
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")

// magic happens here, when we give type information using per-class annotations, 
// this will be used by jackson to convert the json to appropriate java objects.
@JsonSubTypes({ @Type(value = EnumKeyConfiguration.class, name = "key"), @Type(value = EnumKeyValueConfiguration.class, name = "key-value") }) 
public class EnumKeyConfiguration extends AbstractEnumConfiguration {
    private static final long serialVersionUID = 1865479999328925899L;
    private String key;
    private boolean isConfidential;

    
    /**
     * Constructor for EnumKeyConfiguration
     */
    public EnumKeyConfiguration() {
        key = null;
        isConfidential = false;
    }
    
    
    /**
     * Get the configuration key
     * 
     * @return the configuration key
     */
    public String getKey() {
        return key;
    }
    
    
    /**
     * Set the configuration key
     * 
     * @param key the configuration key
     */
    public void setKey(String key) {
        this.key = key;
    }

    
    /**
     * Define if the value is confidential or not
     * 
     * @return define if the value is confidential or not
     */
    public boolean isConfidential() {
        return isConfidential;
    }

    
    /**
     * Define if the value is confidential or not
     * 
     * @param isConfidential define if the value is confidential or not
     */
    public void setConfidential(boolean isConfidential) {
        this.isConfidential = isConfidential;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();

        result = prime * result;
        if (key != null) {
            result += key.hashCode();
        }

        result = prime * result;
        if (isConfidential) {
            result += 1231;
        } else {
            result += 1237;
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

        EnumKeyConfiguration other = (EnumKeyConfiguration)obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }

        if (isConfidential != other.isConfidential) {
            return false;
        }


        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnumNameConfiguration [key=" + getKey()
               + ", description=" + getDescription() 
               + ", isConfidential=" + isConfidential()
               + ", validFrom=" + getValidFrom() 
               + ", validTill=" + getValidTill() 
               + "]";
    }
}
