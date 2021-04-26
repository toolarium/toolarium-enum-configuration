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
    private EnumKeyValueConfigurationSizing<?> valueSize;
    private EnumKeyValueConfigurationSizing<Integer> cardinality;

  
    /**
     * Constructor for EnumKeyValueConfiguration
     */
    public EnumKeyValueConfiguration() {
        super();
        dataType = EnumKeyValueConfigurationDataType.STRING;
        defaultValue = "";
        exampleValue = "";
        valueSize = null;
        cardinality = new EnumKeyValueConfigurationSizing<Integer>(1, 1);
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
     * Initialize a default example value
     */
    /*
    public void initExampleValue() {
        if (getExampleValue() == null || exampleValue.isEmpty()) {
            switch (dataType) {
                case NUMBER:      setExampleValue("1234"); 
                                  break;
                case DOUBLE:      setExampleValue("12.34"); 
                                  break;
                case BOOLEAN:     setExampleValue("true"); 
                                  break;
                case DATE:        setExampleValue("2021-03-15"); 
                                  break;
                case TIME:        setExampleValue("12:34:56.789");
                                  break;
                case TIMESTAMP:   setExampleValue("2021-03-15T08:59:22.123Z");
                                  break;
                case REGEXP:      setExampleValue(".*(jim|joe).*");
                                  break;
                case UUID:        setExampleValue("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
                                  break;
                case URI:         setExampleValue("https://my.url.com");
                                  break;
                case CIDR:        setExampleValue("10.2.0.0/16");                
                                  break;
                case EMAIL:       setExampleValue("mail@domain.com");
                                  break;
                case CRON:        setExampleValue("* * * * *");
                                  break;
                case COLOR:       setExampleValue("#40394A");
                                  break;
                case BINARY:      setExampleValue("myfile.txt|2021-03-15T08:59:22.123Z|text/plain|VGV4dAo=");
                                  break;
                case CERTIFICATE: 
                case STRING:      
                default:
                    setExampleValue("");
            }
        }
    }
    */
    
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
        if (valueSize != null) {
            result += valueSize.hashCode();
        }

        result = prime * result;
        if (cardinality != null) {
            result += cardinality.hashCode();
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

        return true;
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
               + ", valueSize=" + getValueSize() 
               + ", cardinality=" + getCardinality() 
               + ", isConfidential=" + isConfidential()
               + ", validFrom=" + getValidFrom() 
               + ", validTill=" + getValidTill() 
               + "]";
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/