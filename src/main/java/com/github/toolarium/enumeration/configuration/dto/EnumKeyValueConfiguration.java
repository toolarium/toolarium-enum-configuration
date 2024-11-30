/*
 * EnumKeyValueConfiguration.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Defines the enum value configuration  
 * 
 * @author patrick
 */
public class EnumKeyValueConfiguration extends EnumKeyConfiguration {
    
    private static final long serialVersionUID = -7293637675260240900L;
    private EnumKeyValueConfigurationDataType dataType;
    private String defaultValue;
    private String exampleValue;
    private String enumerationValue;
    private EnumKeyValueConfigurationSizing<?> valueSize;
    private EnumKeyValueConfigurationSizing<Integer> cardinality;
    private boolean isUniqueness;

  
    /**
     * Constructor for EnumKeyValueConfiguration
     */
    public EnumKeyValueConfiguration() {
        super();
        dataType = EnumKeyValueConfigurationDataType.STRING;
        defaultValue = "";
        exampleValue = "";
        enumerationValue = "";
        valueSize = null;
        cardinality = new EnumKeyValueConfigurationSizing<Integer>(1, 1);
        isUniqueness = false;
    }
    
        
    /**
     * Get the configuration value data type
     * 
     * @return the configuration value data type
     */
    public EnumKeyValueConfigurationDataType getDataType() {
        return dataType;
    }
    
    
    /**
     * Set the configuration value data type
     * 
     * @param dataType the configuration value data type
     */
    public void setDataType(EnumKeyValueConfigurationDataType dataType) {
        this.dataType = dataType;
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
     * Check if a default value exist.
     *
     * @return true if a default value exist.
     */
    @JsonIgnore
    public boolean hasDefaultValue() {
        return (defaultValue != null && !defaultValue.isEmpty());
    }

    
    /**
     * Get the example value
     * 
     * @return the example value
     */
    public String getExampleValue() {
        return exampleValue;
    }
    
        
    /**
     * Set the example
     * 
     * @param exampleValue the example value
     */
    public void setExampleValue(String exampleValue) {
        this.exampleValue = exampleValue;
    }

    
    /**
     * Get the enumeration value
     * 
     * @return the enumeration value
     */
    public String getEnumerationValue() {
        return enumerationValue;
    }
    
        
    /**
     * Set the enumeration value
     * 
     * @param enumerationValue the enumeration value
     */
    public void setEnumerationValue(String enumerationValue) {
        this.enumerationValue = enumerationValue;
    }
    
    
    /**
     * Get the value size
     * 
     * @return the value size
     */
    public EnumKeyValueConfigurationSizing<?> getValueSize() {
        return valueSize;
    }
    
        
    /**
     * Set the value size
     * 
     * @param valueSize the value size
     */
    public void setValueSize(EnumKeyValueConfigurationSizing<?> valueSize) {
        this.valueSize = valueSize;
    }

    
    /**
     * Get the cardinality
     * 
     * @return the cardinality
     */
    public EnumKeyValueConfigurationSizing<Integer> getCardinality() {
        return cardinality;
    }
    
        
    /**
     * Set the cardinality
     * 
     * @param cardinality the cardinality
     */
    public void setCardinality(EnumKeyValueConfigurationSizing<Integer> cardinality) {
        this.cardinality = cardinality;
    }

    
    /**
     * An {@link EnumKeyValueConfiguration} is mandatory in case the cardinality is defined and the min value is &gt; 0.
     *
     * @return true if it is not optinal.
     */
    @JsonIgnore
    public boolean isMandatory() {
        return (cardinality != null && cardinality.getMinSize() != null && cardinality.getMinSize().intValue() > 0);
    }
    
    
    /**
     * Specifies that the input value is unique. This is only relevant if you have a cardinality.
     * 
     * @return True if it is unique; otherwise false, which means that the same value can occur more than once.
     */
    public boolean isUniqueness() {
        return isUniqueness;
    }

    
    /**
     * Specifies that the input value is unique. This is only relevant if you have a cardinality.
     * 
     * @param isUniqueness True if it is unique; otherwise false, which means that the same value can occur more than once.
     */
    public void setUniqueness(boolean isUniqueness) {
        this.isUniqueness = isUniqueness;
    }

    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        
        result = prime * result;
        if (dataType != null) {
            result += dataType.hashCode();
        }
        
        result = prime * result;
        if (defaultValue != null) {
            result += defaultValue.hashCode();
        }

        result = prime * result;
        if (exampleValue != null) {
            result += exampleValue.hashCode();
        }

        result = prime * result;
        if (enumerationValue != null) {
            result += enumerationValue.hashCode();
        }
        
        result = prime * result;
        if (valueSize != null) {
            result += valueSize.hashCode();
        }

        result = prime * result;
        if (cardinality != null) {
            result += cardinality.hashCode();
        }

        result = prime * result;
        if (isUniqueness) {
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
        
        EnumKeyValueConfiguration other = (EnumKeyValueConfiguration)obj;
        if (dataType == null) {
            if (other.dataType != null) {
                return false;
            }
        } else if (!dataType.equals(other.dataType)) {
            return false;
        }
        
        if (defaultValue == null) {
            if (other.defaultValue != null) {
                return false;
            }
        } else if (!defaultValue.equals(other.defaultValue)) {
            return false;
        }

        if (exampleValue == null) {
            if (other.exampleValue != null) {
                return false;
            }
        } else if (!exampleValue.equals(other.exampleValue)) {
            return false;
        }

        if (enumerationValue == null) {
            if (other.enumerationValue != null) {
                return false;
            }
        } else if (!enumerationValue.equals(other.enumerationValue)) {
            return false;
        }

        if (valueSize == null) {
            if (other.valueSize != null) {
                return false;
            }
        } else if (!valueSize.equals(other.valueSize)) {
            return false;
        }

        if (cardinality == null) {
            if (other.cardinality != null) {
                return false;
            }
        } else if (!cardinality.equals(other.cardinality)) {
            return false;
        }

        if (isUniqueness != other.isUniqueness) {
            return false;
        }

        return true;
    }

    
    /**
     * Check if the enum configuration is compliant with another. 
     *
     * @param o the 
     * @return true if it is compliant
     */
    public EnumConfigurationComplianceResult isCompliant(EnumKeyValueConfiguration o) {
        EnumConfigurationComplianceResult result = super.isCompliant(o);
        if (!result.isValid()) {
            return result;
        }

        if (dataType != null && o.getDataType() != null && !dataType.equals(o.getDataType())) {
            return new EnumConfigurationComplianceResult("Incompliant data type (" + dataType + " != " + o.getDataType() + ")");
        }

        if (enumerationValue != null && o.getEnumerationValue() != null && !enumerationValue.equals(o.getEnumerationValue())) {
            return new EnumConfigurationComplianceResult("Incompliant enumeration value type (" + enumerationValue + " != " + o.getEnumerationValue() + ")");
        }

        if (valueSize != null && o.getValueSize() != null) {
            result = valueSize.isCompliant(o.getValueSize());
            if (!result.isValid()) {
                return result;
            }
        }

        /*
        if (cardinality != null && o.getCardinality() != null) {
            result = cardinality.isCompliant(o.cardinality());
            if (!result.isValid()) {
                return result;
            }
        }
        */
        // ignore defaultValue, exampleValue
        
        // TODO: isUniqueness
        return result;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnumKeyValueConfiguration [key=" + getKey()
               + ", dataType=" + getDataType()
               + ", description=" + getDescription() 
               + ", defaultValue=" + getDefaultValue() 
               + ", exampleValue=" + getExampleValue()
               + ", enumerationValue=" + getEnumerationValue()
               + ", valueSize=" + getValueSize() 
               + ", cardinality=" + getCardinality() 
               + ", isUniqueness=" + isUniqueness()
               + ", mandatory=" + isMandatory()
               + ", confidential=" + isConfidential()
               + ", validFrom=" + getValidFrom() 
               + ", validTill=" + getValidTill() 
               + "]";
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/