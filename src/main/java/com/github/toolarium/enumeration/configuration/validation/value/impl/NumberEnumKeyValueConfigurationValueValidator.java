/*
 * NumberEnumKeyValueConfigurationValueValidator.java
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
 * Defines the number {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class NumberEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<Long, Number> {
    private static final long serialVersionUID = -8613240212933792109L;


    /**
     * Constructor for NumberEnumKeyValueConfigurationValueValidator
     */
    public NumberEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.NUMBER, Long.class, EnumKeyValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String)
     */
    @Override
    public Long validateValue(EnumKeyValueConfigurationSizing<Number> valueSize, String inputValue) throws EmptyValueException, ValidationException {
    
        Long inputNumber = parseValue(inputValue);
        MinMaxValue<Number> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return inputNumber;
        }

        if (inputNumber == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return inputNumber;
            }
            
            throw new EmptyValueException("Empty value: invalid size, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }

        if (inputNumber.compareTo(minMaxValue.getMin().longValue()) < 0) {
            throw new ValidationException("Too small: invalid size of [" + inputValue + "], should be at least [" + valueSize.getMinSizeAsString() + "] (now " + inputValue + ")!");
        }
    
        if (inputNumber.compareTo(minMaxValue.getMax().longValue()) > 0) {
            throw new ValidationException("Too big: invalid size of [" + inputValue + "], should be in range of [" + valueSize.getMinSizeAsString() + ".." + valueSize.getMaxSizeAsString() + "] (now " + inputValue + ")!");
        }
        
        return inputNumber;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createEnumKeyValueConfigurationSizing()
     */
    @Override
    public EnumKeyValueConfigurationSizing<Number> createEnumKeyValueConfigurationSizing() {
        return new EnumKeyValueConfigurationSizing<Number>();
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
    protected boolean isGreaterThan(Number first, Number second) {
        return isGreaterThanValue(first, second);
    }
}
