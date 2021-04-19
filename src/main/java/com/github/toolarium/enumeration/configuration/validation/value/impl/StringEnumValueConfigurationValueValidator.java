/*
 * StringEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;


/**
 * Defines the string {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class StringEnumValueConfigurationValueValidator extends AbstractEnumValueConfigurationValueValidator<String, Long> {
    private static final long serialVersionUID = -7649231162358270591L;


    /**
     * Constructor for StringEnumValueConfigurationDataType
     */
    public StringEnumValueConfigurationValueValidator() {
        this(EnumValueConfigurationDataType.STRING, EnumValueConfigurationDataType.NUMBER);
    }

    
    /**
     * Constructor for StringEnumValueConfigurationDataType
     * 
     * @param dataType The data type.
     * @param sizeDataType the data type of the size.
     */
    protected StringEnumValueConfigurationValueValidator(EnumValueConfigurationDataType dataType, EnumValueConfigurationDataType sizeDataType) {
        super(dataType, String.class, sizeDataType);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, java.lang.String)
     */
    @Override
    public void validateValue(EnumValueConfigurationSizing<Long> valueSize, String inputValue) throws ValidationException {
        
        String inputString = parseValue(inputValue);        
        MinMaxValue<Long> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return;
        }

        if (inputString == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return;
            }
            
            throw new ValidationException("Empty value: invalid string, should be at the length of [" + valueSize.getMinSizeAsString() + "]!");
        }

        long inputLength = inputString.length();
        if (inputLength < minMaxValue.getMin().longValue()) {
            throw new ValidationException("Too short: invalid length of [" + inputValue + "], should be at least [" + minMaxValue.getMin() + "] (now " + inputLength + ")!");
        }

        if (inputLength > minMaxValue.getMax().longValue()) {
            throw new ValidationException("Too long: invalid length of [" + inputValue + "], should be in range of [" + minMaxValue.getMin() + ".." + minMaxValue.getMax() + "] (now " + inputLength + ")!");
        }
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#createEnumValueConfigurationSizing()
     */
    @Override
    public EnumValueConfigurationSizing<Long> createEnumValueConfigurationSizing() {
        return new EnumValueConfigurationSizing<Long>();
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 0L;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public Long getMaxSize() {
        return Long.MAX_VALUE;
    }
    
    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
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
