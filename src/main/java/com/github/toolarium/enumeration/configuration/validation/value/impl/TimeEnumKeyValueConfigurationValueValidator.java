/*
 * TimeEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;
import java.time.LocalTime;

/**
 * Defines the time {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class TimeEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<LocalTime, LocalTime> {
    private static final long serialVersionUID = -5464834689859257617L;

    
    /**
     * Constructor for TimeEnumKeyValueConfigurationValueValidator
     */
    public TimeEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.TIME, LocalTime.class, EnumKeyValueConfigurationDataType.TIME);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String)
     */
    @Override
    public LocalTime validateValue(EnumKeyValueConfigurationSizing<LocalTime> valueSize, String inputValue) throws EmptyValueException, ValidationException {

        LocalTime inputTime = parseValue(inputValue);
        MinMaxValue<LocalTime> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return inputTime;
        }
        
        if (inputTime == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return inputTime;
            }
            
            throw new EmptyValueException("Empty value: invalid time, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }
        
        if (inputTime.compareTo(minMaxValue.getMin()) < 0) {
            throw new ValidationException("Too small: invalid date of [" + inputValue + "], should be at least [" + valueSize.getMinSizeAsString() + "] (now " + inputValue + ")!");
        }
    
        if (inputTime.compareTo(minMaxValue.getMax()) > 0) {
            throw new ValidationException("Too big: invalid date of [" + inputValue + "], should be in range of [" + valueSize.getMinSizeAsString() + ".." + valueSize.getMaxSizeAsString() + "] (now " + inputValue + ")!");
        }
        
        return inputTime;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createEnumKeyValueConfigurationSizing()
     */
    @Override
    public EnumKeyValueConfigurationSizing<LocalTime> createEnumKeyValueConfigurationSizing() {
        return new EnumKeyValueConfigurationSizing<LocalTime>();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public LocalTime getMinSize() {
        return LocalTime.MIN;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public LocalTime getMaxSize() {
        return DateUtil.MAX_TIME;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
     */
    @Override
    protected boolean isGreaterThan(LocalTime first, LocalTime second) {
        if (first == null) {
            if (second == null) {
                return false;
            }
            
            return true;
        } else if (second == null) {
            return false;
        }
        
        return first.isAfter(second);
    }
}
