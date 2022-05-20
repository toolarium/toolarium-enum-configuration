/*
 * DateEnumKeyValueConfigurationValueValidator.java
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
import java.time.LocalDate;


/**
 * Defines the date {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class DateEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<LocalDate, LocalDate> {
    private static final long serialVersionUID = -5563031423758776454L;

    
    /**
     * Constructor for DateEnumKeyValueConfigurationValueValidator
     */
    public DateEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.DATE, LocalDate.class, EnumKeyValueConfigurationDataType.DATE);
    }
    
    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String)
     */
    @Override
    public LocalDate validateValue(EnumKeyValueConfigurationSizing<LocalDate> valueSize, String inputValue) throws EmptyValueException, ValidationException {

        LocalDate inputDate = parseValue(inputValue);
        MinMaxValue<LocalDate> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return inputDate;
        }
        
        if (inputDate == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return inputDate;
            }
            
            throw new EmptyValueException("Empty value: invalid date, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }
        
        if (inputDate.compareTo(minMaxValue.getMin()) < 0) {
            throw new ValidationException("Too small: invalid date of [" + inputValue + "], should be at least [" + valueSize.getMinSizeAsString() + "] (now " + inputValue + ")!", inputValue, inputDate);
        }
    
        if (inputDate.compareTo(minMaxValue.getMax()) > 0) {
            throw new ValidationException("Too big: invalid date of [" + inputValue + "], should be in range of [" + valueSize.getMinSizeAsString() + ".." + valueSize.getMaxSizeAsString() + "] (now " + inputValue + ")!", inputValue, inputDate);
        }
        
        return inputDate;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createEnumKeyValueConfigurationSizing()
     */
    @Override
    public EnumKeyValueConfigurationSizing<LocalDate> createEnumKeyValueConfigurationSizing() {
        return new EnumKeyValueConfigurationSizing<LocalDate>();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public LocalDate getMinSize() {
        return LocalDate.EPOCH;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public LocalDate getMaxSize() {
        return DateUtil.MAX_DATE;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
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
