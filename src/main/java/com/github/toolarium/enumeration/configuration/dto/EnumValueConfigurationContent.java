/*
 * EnumConfigurationContent.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;


/**
 * Defines the enum value configuration content 
 * 
 * @author Meier Patrick
 * @version $Revision:  $
 */
public class EnumValueConfigurationContent extends AbstractEnumConfigurationContent {
    
    private static final long serialVersionUID = -7293637675260240900L;
    private String key;
    private String defaultValue;
    private boolean isOptional;
    private boolean isConfidential;
      
    
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
     * Get the default value
     * 
     * @return the default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }
    
        
    /**
     * Set the default value
     * 
     * @param defaultValue the default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    
    /**
     * Define if the value is optional or not
     * 
     * @return define if the value is optional or not
     */
    public boolean isOptional() {
        return isOptional;
    }

    
    /**
     * Define if the value is optional or not
     * 
     * @param isOptional define if the value is optional or not
     */
    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
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
        if (defaultValue != null) {
            result += defaultValue.hashCode();
        }

        result = prime * result;
        if (isOptional) {
            result += 1231;
        } else {
            result += 1237;
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
        
        EnumValueConfigurationContent other = (EnumValueConfigurationContent)obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }

        if (defaultValue == null) {
            if (other.defaultValue != null) {
                return false;
            }
        } else if (!defaultValue.equals(other.defaultValue)) {
            return false;
        }
        
        if (isOptional != other.isOptional) {
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
        return "EnumValueConfigurationContent [key=" + key
               + ", description=" + getDescription() 
               + ", defaultValue=" + defaultValue 
               + ", isOptional=" + isOptional
               + ", isConfidential=" + isConfidential
               + ", validFrom=" + getValidFrom() 
               + ", validTill=" + getValidTill() 
               + "]";
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/