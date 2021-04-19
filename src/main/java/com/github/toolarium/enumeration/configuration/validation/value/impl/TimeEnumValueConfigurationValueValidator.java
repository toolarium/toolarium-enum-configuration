/*
 * TimeEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;
import java.time.LocalTime;

/**
 * Defines the time {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class TimeEnumValueConfigurationValueValidator extends AbstractEnumValueConfigurationValueValidator<LocalTime, LocalTime> {
    private static final long serialVersionUID = -5464834689859257617L;

    
    /**
     * Constructor for TimeEnumValueConfigurationDataType
     */
    public TimeEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.TIME, LocalTime.class, EnumValueConfigurationDataType.TIME);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, java.lang.String)
     */
    @Override
    public void validateValue(EnumValueConfigurationSizing<LocalTime> valueSize, String inputValue) throws ValidationException {

        LocalTime inputTime = parseValue(inputValue);
        MinMaxValue<LocalTime> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return;
        }
        
        if (inputTime == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return;
            }
            
            throw new ValidationException("Empty value: invalid time, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }
        
        if (inputTime.compareTo(minMaxValue.getMin()) < 0) {
            throw new ValidationException("Too small: invalid date of [" + inputValue + "], should be at least [" + valueSize.getMinSizeAsString() + "] (now " + inputValue + ")!");
        }
    
        if (inputTime.compareTo(minMaxValue.getMax()) > 0) {
            throw new ValidationException("Too big: invalid date of [" + inputValue + "], should be in range of [" + valueSize.getMinSizeAsString() + ".." + valueSize.getMaxSizeAsString() + "] (now " + inputValue + ")!");
        }
        
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#createEnumValueConfigurationSizing()
     */
    @Override
    public EnumValueConfigurationSizing<LocalTime> createEnumValueConfigurationSizing() {
        return new EnumValueConfigurationSizing<LocalTime>();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public LocalTime getMinSize() {
        return LocalTime.MIN;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public LocalTime getMaxSize() {
        return DateUtil.MAX_TIME;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
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
