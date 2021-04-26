/*
 * AbstractEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.converter.StringTypeConverterFactory;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;
import java.io.Serializable;


/**
 * Base value validator class.
 *  
 * @author patrick
 */
public abstract class AbstractEnumKeyValueConfigurationValueValidator<D, T> implements Serializable, IEnumKeyConfigurationValueValidator<T> {
    private static final long serialVersionUID = 5414798875779082500L;
    private EnumKeyValueConfigurationDataType valueDataType;
    private EnumKeyValueConfigurationDataType sizeDataType;
    private Class<D> valueClass;

    
    /**
     * Constructor for AbstractEnumKeyValueConfigurationValueValidator
     * 
     * @param valueDataType The value data type
     * @param valueClass the value class
     * @param sizeDataType the size data type
     */
    protected AbstractEnumKeyValueConfigurationValueValidator(EnumKeyValueConfigurationDataType valueDataType, Class<D> valueClass, EnumKeyValueConfigurationDataType sizeDataType) {
        this.valueDataType = valueDataType;
        this.valueClass = valueClass;
        this.sizeDataType = sizeDataType;
    }
    
    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createValueSize(java.lang.String, java.lang.String)
     */
    @Override
    public EnumKeyValueConfigurationSizing<T> createValueSize(String minValue, String maxValue) throws ValidationException {
        if ((minValue == null || minValue.trim().isEmpty()) && (maxValue == null || maxValue.trim().isEmpty())) {
            return null;
        }

        EnumKeyValueConfigurationSizing<T> result = createEnumKeyValueConfigurationSizing();
        if (result != null) {
            if (minValue != null && !minValue.trim().isEmpty()) {
                result.setMinSizeAsString(minValue.trim());
                result.setMinSize(parseSizeValue(minValue.trim()));
            } else {
                result.setMinSize(getMinSize());
            }
    
            if (maxValue != null && !maxValue.trim().isEmpty()) {
                result.setMaxSizeAsString(maxValue.trim());
                result.setMaxSize(parseSizeValue(maxValue.trim()));
            } else {
                result.setMaxSize(getMaxSize());
            }
            
            if (!isGreaterThan(result.getMaxSize(), result.getMinSize())) {
                if (!result.getMaxSize().equals(result.getMinSize())) {
                    throw new ValidationException("Invalid value size, max size [" + maxValue + "] should be gerater or equal than the min size [" + minValue + "]!");
                }
            }
        }

        
        return result;
    }

    
    /**
     * Parse the size value
     *
     * @param input the string input, either number or *. In case it's null it will return null 
     * @return the parsed result
     * @throws ValidationException In case of invalid data
     */
    @SuppressWarnings("unchecked")
    public T parseSizeValue(String input) throws ValidationException {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        
        if ("*".equals(input.trim())) {
            return getMaxSize();
        }
        
        return (T)StringTypeConverterFactory.getInstance().getStringTypeConverter().convert(sizeDataType, input);
    }    

    
    /**
     * Parse the size value
     *
     * @param input the string input, either number or *. In case it's null it will return null 
     * @return the parsed result
     * @throws ValidationException In case of invalid data
     */
    public D parseValue(String input) throws ValidationException {
        if (input == null || input.isEmpty()) {
            return null;
        }
        
        try {
            return StringTypeConverterFactory.getInstance().getStringTypeConverter().convert(valueDataType, input);
        } catch (Exception e) {
            throw new ValidationException("Invalid value [" + input + "] (" + e.getMessage() + ")!");
        }
    }    

    
    /**
     * Prepare min / max value
     *
     * @param valueSize the value size
     * @param inputValue The input value
     * @return the MinMaxValue.
     * @throws ValidationException In case of min / max inconsistency.
     */
    protected MinMaxValue<T> preapreMinMaxValue(EnumKeyValueConfigurationSizing<T> valueSize, String inputValue) throws ValidationException {
        if (valueSize == null) {
            return null;
        }

        T min = getMinSize();
        if (valueSize.getMinSize() != null) {
            min = valueSize.getMinSize();
        }

        T max = getMaxSize();
        if (valueSize.getMaxSize() != null) {
            max = valueSize.getMaxSize();
        }
        
        try {
            if (isGreaterThan(min, max)) { // max < min
                throw new ValidationException("Invalid maxValue / minValue of [" + inputValue + "], the minValue [" + valueSize.getMinSizeAsString() + "] should be <= then maxValue [" + valueSize.getMinSizeAsString() + "]!");
            }
        } catch (RuntimeException ex) {
            ValidationException e = new ValidationException(ex.getMessage());
            e.setStackTrace(ex.getStackTrace());
            throw ex;
        }
        
        return new MinMaxValue<T>(valueSize.getMinSizeAsString(), min, valueSize.getMaxSizeAsString(), max);
    }     
    
    
    /**
     * Get the min size.
     *
     * @return the min size.
     */
    public abstract T getMinSize();
    

    /**
     * Get the max size.
     *
     * @return the max size.
     */
    public abstract T getMaxSize();

    
    /**
     * Validate if the first value is greater than the second.
     *
     * @param first value
     * @param second value
     * @return true if the first is greater than the seconds.
     */
    protected abstract boolean isGreaterThan(T first, T second);
    

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        result = prime * result;
        if (valueDataType != null) {
            result += valueDataType.hashCode();
        }

        result = prime * result;
        if (valueClass != null) {
            result += valueClass.hashCode();
        }

        result = prime * result;
        if (sizeDataType != null) {
            result += sizeDataType.hashCode();
        }

        return result;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
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
        
        AbstractEnumKeyValueConfigurationValueValidator<D, T> other = (AbstractEnumKeyValueConfigurationValueValidator<D, T>) obj;
        if (valueDataType != other.valueDataType) {
            return false;
        }
        
        if (valueClass == null) {
            if (other.valueClass != null) {
                return false;
            }
        } else if (!valueClass.equals(other.valueClass)) {
            return false;
        }

        if (sizeDataType != other.sizeDataType) {
            return false;
        }

        return true;
    }

    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getName() + " [valueDataType=" + valueDataType + ", valueClass=" + valueClass + ", sizeDataType=" + sizeDataType + "]";
    }


    /**
     * The min max value
     *  
     * @author patrick
     */
    @SuppressWarnings("hiding")
    class MinMaxValue<T> implements Serializable {
        private static final long serialVersionUID = -3107817743258894907L;
        private String minOriginal;
        private T min;
        private String maxOriginal;
        private T max;
        
        
        /**
         * Constructor for MinMaxValue
         * @param minOriginal the min original
         * @param min the min 
         * @param maxOriginal the max original
         * @param max the max
         */
        MinMaxValue(String minOriginal, T min, String maxOriginal, T max) {
            this.minOriginal = minOriginal;
            this.min = min;
            this.maxOriginal = maxOriginal;
            this.max = max;
        }

        
        /**
         * Get the min string value
         *
         * @return the min string value
         */
        public String getMinOriginal() {
            return minOriginal;
        }


        /**
         * Get the min value
         *
         * @return the min value
         */
        public T getMin() {
            return min;
        }

        
        /**
         * Get the max string value
         *
         * @return the max string value
         */
        public String getMaxOriginal() {
            return maxOriginal;
        }


        /**
         * Get the max value
         *
         * @return the max value
         */
        public T getMax() {
            return max;
        }


        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();

            result = prime;
            if (minOriginal != null) {
                result += minOriginal.hashCode();
            }

            result = prime;
            if (min != null) {
                result += min.hashCode();
            }

            result = prime;
            if (maxOriginal != null) {
                result += maxOriginal.hashCode();
            }

            result = prime;
            if (max != null) {
                result += max.hashCode();
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
            
            @SuppressWarnings("unchecked")
            MinMaxValue<T> other = (MinMaxValue<T>) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance())) {
                return false;
            }

            if (minOriginal == null) {
                if (other.minOriginal != null) {
                    return false;
                }
            } else if (!minOriginal.equals(other.minOriginal)) {
                return false;
            }

            if (min == null) {
                if (other.min != null) {
                    return false;
                }
            } else if (!min.equals(other.min)) {
                return false;
            }

            if (maxOriginal == null) {
                if (other.maxOriginal != null) {
                    return false;
                }
            } else if (!maxOriginal.equals(other.maxOriginal)) {
                return false;
            }

            if (max == null) {
                if (other.max != null) {
                    return false;
                }
            } else if (!max.equals(other.max)) {
                return false;
            }
            
            return true;
        }


        /**
         * Get enclosing instance
         *
         * @return the enclosing instance
         */
        @SuppressWarnings("rawtypes")
        private AbstractEnumKeyValueConfigurationValueValidator getEnclosingInstance() {
            return AbstractEnumKeyValueConfigurationValueValidator.this;
        }
    }
}
