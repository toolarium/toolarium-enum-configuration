/*
 * DoubleEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;


/**
 * Defines the double {@link IEnumValueConfigurationDataType}.
 * 
 * @author patrick
 */
public class DoubleEnumValueConfigurationValueValidator extends AbstractEnumValueConfigurationValueValidator<Double, Double> {
    private static final long serialVersionUID = 6341855162198787917L;


    /**
     * Constructor for DoubleEnumValueConfigurationDataType
     */
    public DoubleEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.DOUBLE, Double.class, EnumValueConfigurationDataType.DOUBLE);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, java.lang.String)
     */
    @Override
    public void validateValue(EnumValueConfigurationSizing<Double> valueSize, String inputValue) throws ValidationException {
        
        Double inputDouble = parseValue(inputValue);
        MinMaxValue<Double> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return;
        }
        
        if (inputDouble == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return;
            }
            
            throw new ValidationException("Empty value: invalid size, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }

        if (inputDouble.compareTo(minMaxValue.getMin()) < 0) {
            throw new ValidationException("Too small: invalid size of [" + inputValue + "], should be at least [" + valueSize.getMinSizeAsString() + "] (now " + inputValue + ")!");
        }
    
        if (inputDouble.compareTo(minMaxValue.getMax()) > 0) {
            throw new ValidationException("Too big: invalid size of [" + inputValue + "], should be in range of [" + valueSize.getMinSizeAsString() + ".." + valueSize.getMaxSizeAsString() + "] (now " + inputValue + ")!");
        }
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.datatype.IEnumValueConfigurationValueValidator#createEnumValueConfigurationSizing()
     */
    @Override
    public EnumValueConfigurationSizing<Double> createEnumValueConfigurationSizing() {
        return new EnumValueConfigurationSizing<Double>();
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.datatype.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Double getMinSize() {
        return 0.0D;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.datatype.impl.AbstractEnumValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public Double getMaxSize() {
        return Double.MAX_VALUE;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
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
