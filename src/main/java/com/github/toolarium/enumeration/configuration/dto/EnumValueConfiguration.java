/*
 * EnumValueConfiguration.java
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
public class EnumValueConfiguration extends AbstractEnumConfiguration {
    
    private static final long serialVersionUID = -7293637675260240900L;
    private String key;
    private EnumValueConfigurationDataType dataType;
    private String defaultValue;
    private String exampleValue;
    private EnumValueConfigurationSizing<?> valueSize;
    private EnumValueConfigurationSizing<Integer> cardinality;
    private boolean isConfidential;

  
    /**
     * Constructor for EnumValueConfiguration
     */
    public EnumValueConfiguration() {
        super();
        key = null;
        dataType = EnumValueConfigurationDataType.STRING;
        defaultValue = "";
        exampleValue = "";
        valueSize = null;
        cardinality = new EnumValueConfigurationSizing<Integer>(1, 1);
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
     * Get the configuration value data type
     * 
     * @return the configuration value data type
     */
    public EnumValueConfigurationDataType getDataType() {
        return dataType;
    }
    
    
    /**
     * Set the configuration value data type
     * 
     * @param dataType the configuration value data type
     */
    public void setDataType(EnumValueConfigurationDataType dataType) {
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
    public EnumValueConfigurationSizing<?> getValueSize() {
        return valueSize;
    }
    
        
    /**
     * Set the value size
     * 
     * @param valueSize the value size
     */
    public void setValueSize(EnumValueConfigurationSizing<?> valueSize) {
        this.valueSize = valueSize;
    }

    
    /**
     * Get the cardinality
     * 
     * @return the cardinality
     */
    public EnumValueConfigurationSizing<Integer> getCardinality() {
        return cardinality;
    }
    
        
    /**
     * Set the cardinality
     * 
     * @param cardinality the cardinality
     */
    public void setCardinality(EnumValueConfigurationSizing<Integer> cardinality) {
        this.cardinality = cardinality;
    }

    
    /**
     * An {@link EnumValueConfiguration} is mandatory in case the cardinality is defined and the min value is &gt; 0.
     *
     * @return true if it is not optinal.
     */
    @JsonIgnore
    public boolean isMandatory() {
        return (cardinality != null && cardinality.getMinSize() != null && cardinality.getMinSize().intValue() > 0);
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
        if (key != null) {
            result += key.hashCode();
        }

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
        
        EnumValueConfiguration other = (EnumValueConfiguration)obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }

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
        return "EnumValueConfiguration [key=" + key
               + ", dataType=" + getDataType()
               + ", description=" + getDescription() 
               + ", defaultValue=" + defaultValue 
               + ", exampleValue=" + getExampleValue() 
               + ", valueSize=" + getValueSize() 
               + ", cardinality=" + getCardinality() 
               + ", isConfidential=" + isConfidential
               + ", validFrom=" + getValidFrom() 
               + ", validTill=" + getValidTill() 
               + "]";
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/