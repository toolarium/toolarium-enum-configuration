/*
 * DoubleEnumKeyValueConfigurationValueValidator.java
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
 * Defines the double {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class DoubleEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<Double, Double> {
    private static final long serialVersionUID = 6341855162198787917L;


    /**
     * Constructor for DoubleEnumKeyValueConfigurationValueValidator
     */
    public DoubleEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.DOUBLE, Double.class, EnumKeyValueConfigurationDataType.DOUBLE);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String)
     */
    @Override
    public Double validateValue(EnumKeyValueConfigurationSizing<Double> valueSize, String inputValue) throws EmptyValueException, ValidationException {
        
        Double inputDouble = parseValue(inputValue);
        MinMaxValue<Double> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return inputDouble;
        }
        
        if (inputDouble == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return inputDouble;
            }
            
            throw new EmptyValueException("Empty value: invalid size, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }

        if (inputDouble.compareTo(minMaxValue.getMin()) < 0) {
            throw new ValidationException("Too small: invalid size of [" + inputValue + "], should be at least [" + valueSize.getMinSizeAsString() + "] (now " + inputValue + ")!", inputValue, inputDouble);
        }
    
        if (inputDouble.compareTo(minMaxValue.getMax()) > 0) {
            throw new ValidationException("Too big: invalid size of [" + inputValue + "], should be in range of [" + valueSize.getMinSizeAsString() + ".." + valueSize.getMaxSizeAsString() + "] (now " + inputValue + ")!", inputValue, inputDouble);
        }
        
        return inputDouble;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createEnumKeyValueConfigurationSizing()
     */
    @Override
    public EnumKeyValueConfigurationSizing<Double> createEnumKeyValueConfigurationSizing() {
        return new EnumKeyValueConfigurationSizing<Double>();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Double getMinSize() {
        return 0.0D;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public Double getMaxSize() {
        return Double.MAX_VALUE;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
     */
    @Override
    protected boolean isGreaterThan(Double first, Double second) {
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
