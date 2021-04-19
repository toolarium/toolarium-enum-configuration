/*
 * DateEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;
import java.time.LocalDate;


/**
 * Defines the date {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class DateEnumValueConfigurationValueValidator extends AbstractEnumValueConfigurationValueValidator<LocalDate, LocalDate> {
    private static final long serialVersionUID = -5563031423758776454L;

    
    /**
     * Constructor for DateEnumValueConfigurationDataType
     */
    public DateEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.DATE, LocalDate.class, EnumValueConfigurationDataType.DATE);
    }
    
    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, java.lang.String)
     */
    @Override
    public void validateValue(EnumValueConfigurationSizing<LocalDate> valueSize, String inputValue) throws ValidationException {

        LocalDate inputDate = parseValue(inputValue);
        MinMaxValue<LocalDate> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return;
        }
        
        if (inputDate == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return;
            }
            
            throw new ValidationException("Empty value: invalid date, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }
        
        if (inputDate.compareTo(minMaxValue.getMin()) < 0) {
            throw new ValidationException("Too small: invalid date of [" + inputValue + "], should be at least [" + valueSize.getMinSizeAsString() + "] (now " + inputValue + ")!");
        }
    
        if (inputDate.compareTo(minMaxValue.getMax()) > 0) {
            throw new ValidationException("Too big: invalid date of [" + inputValue + "], should be in range of [" + valueSize.getMinSizeAsString() + ".." + valueSize.getMaxSizeAsString() + "] (now " + inputValue + ")!");
        }
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#createEnumValueConfigurationSizing()
     */
    @Override
    public EnumValueConfigurationSizing<LocalDate> createEnumValueConfigurationSizing() {
        return new EnumValueConfigurationSizing<LocalDate>();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public LocalDate getMinSize() {
        return LocalDate.EPOCH;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public LocalDate getMaxSize() {
        return DateUtil.MAX_DATE;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
     */
    @Override
    protected boolean isGreaterThan(LocalDate first, LocalDate second) {
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
