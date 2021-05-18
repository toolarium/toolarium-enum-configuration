/*
 * StringEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;


/**
 * Defines the string {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class StringEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<String, Long> {
    private static final long serialVersionUID = -7649231162358270591L;


    /**
     * Constructor for StringEnumKeyValueConfigurationValueValidator
     */
    public StringEnumKeyValueConfigurationValueValidator() {
        this(EnumKeyValueConfigurationDataType.STRING, EnumKeyValueConfigurationDataType.NUMBER);
    }

    
    /**
     * Constructor for StringEnumKeyValueConfigurationValueValidator
     * 
     * @param dataType The data type.
     * @param sizeDataType the data type of the size.
     */
    protected StringEnumKeyValueConfigurationValueValidator(EnumKeyValueConfigurationDataType dataType, EnumKeyValueConfigurationDataType sizeDataType) {
        super(dataType, String.class, sizeDataType);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String)
     */
    @Override
    public String validateValue(EnumKeyValueConfigurationSizing<Long> valueSize, String inputValue) throws EmptyValueException, ValidationException {
        
        String inputString = parseValue(inputValue);        
        MinMaxValue<Long> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return inputString;
        }

        if (inputString == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return inputString;
            }
            
            throw new EmptyValueException("Empty value: invalid string, should be at the length of [" + valueSize.getMinSizeAsString() + "]!");
        }

        long inputLength = inputString.length();
        if (inputLength < minMaxValue.getMin().longValue()) {
            throw new ValidationException("Too short: invalid length of [" + inputValue + "], should be at least [" + minMaxValue.getMin() + "] (now " + inputLength + ")!");
        }

        if (inputLength > minMaxValue.getMax().longValue()) {
            throw new ValidationException("Too long: invalid length of [" + inputValue + "], should be in range of [" + minMaxValue.getMin() + ".." + minMaxValue.getMax() + "] (now " + inputLength + ")!");
        }
        
        return inputString;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createEnumKeyValueConfigurationSizing()
     */
    @Override
    public EnumKeyValueConfigurationSizing<Long> createEnumKeyValueConfigurationSizing() {
        return new EnumKeyValueConfigurationSizing<Long>();
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 0L;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public Long getMaxSize() {
        return Long.MAX_VALUE;
    }
    
    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
     */
    @Override
    protected boolean isGreaterThan(Long first, Long second) {
        if (first == null) {
            if (second == null) {
                return false;
            }
            
            return true;
        } else if (second == null) {
            return false;
        }
        
        return first.compareTo(second) > 0;
    }
}
