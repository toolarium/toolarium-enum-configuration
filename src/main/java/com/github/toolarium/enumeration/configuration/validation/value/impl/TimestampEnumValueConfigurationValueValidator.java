/*
 * TimestampEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


/**
 * Defines the timestamp {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class TimestampEnumValueConfigurationValueValidator extends AbstractEnumValueConfigurationValueValidator<Instant, Instant> {
    private static final long serialVersionUID = 2922787805935547890L;


    /**
     * Constructor for TimestampEnumValueConfigurationDataType
     */
    public TimestampEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.TIMESTAMP, Instant.class, EnumValueConfigurationDataType.TIMESTAMP);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, java.lang.String)
     */
    @Override
    public void validateValue(EnumValueConfigurationSizing<Instant> valueSize, String inputValue) throws EmptyValueException, ValidationException {

        Instant inputTimestamp = parseValue(inputValue);
        MinMaxValue<Instant> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return;
        }
        
        if (inputTimestamp == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return;
            }
            
            throw new EmptyValueException("Empty value: invalid timestamp, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }

        inputTimestamp = inputTimestamp.truncatedTo(ChronoUnit.MILLIS);
        if (inputTimestamp.compareTo(minMaxValue.getMin()) < 0) {
            throw new ValidationException("Too small: invalid timestamp of [" + inputValue + "], should be at least [" + valueSize.getMinSizeAsString() + "] (now " + inputValue + ")!");
        }
    
        if (inputTimestamp.compareTo(minMaxValue.getMax()) > 0) {
            throw new ValidationException("Too big: invalid timestamp of [" + inputValue + "], should be in range of [" + valueSize.getMinSizeAsString() + ".." + valueSize.getMaxSizeAsString() + "] (now " + inputValue + ")!");
        }
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#createEnumValueConfigurationSizing()
     */
    @Override
    public EnumValueConfigurationSizing<Instant> createEnumValueConfigurationSizing() {
        return new EnumValueConfigurationSizing<Instant>();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Instant getMinSize() {
        return Instant.MIN;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public Instant getMaxSize() {
        return DateUtil.MAX_TIMESTAMP;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
     */
    @Override
    protected boolean isGreaterThan(Instant first, Instant second) {
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
