/*
 * TimestampEnumKeyValueConfigurationValueValidator.java
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;


/**
 * Defines the timestamp {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class TimestampEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<Instant, Instant> {
    private static final long serialVersionUID = 2922787805935547890L;


    /**
     * Constructor for TimestampEnumKeyValueConfigurationValueValidator
     */
    public TimestampEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.TIMESTAMP, Instant.class, EnumKeyValueConfigurationDataType.TIMESTAMP);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String)
     */
    @Override
    public Instant validateValue(EnumKeyValueConfigurationSizing<Instant> valueSize, String inputValue) throws EmptyValueException, ValidationException {

        Instant inputTimestamp = parseValue(inputValue);
        MinMaxValue<Instant> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return inputTimestamp;
        }
        
        if (inputTimestamp == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return inputTimestamp;
            }
            
            throw new EmptyValueException("Empty value: invalid timestamp, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }

        inputTimestamp = inputTimestamp.truncatedTo(ChronoUnit.MILLIS);
        if (inputTimestamp.compareTo(minMaxValue.getMin()) < 0) {
            throw new ValidationException("Too small: invalid timestamp of [" + inputValue + "], should be at least [" + valueSize.getMinSizeAsString() + "] (now " + inputValue + ")!", inputValue, inputTimestamp);
        }
    
        if (inputTimestamp.compareTo(minMaxValue.getMax()) > 0) {
            throw new ValidationException("Too big: invalid timestamp of [" + inputValue + "], should be in range of [" + valueSize.getMinSizeAsString() + ".." + valueSize.getMaxSizeAsString() + "] (now " + inputValue + ")!", 
                                          inputValue, inputTimestamp);
        }
        
        return inputTimestamp;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createEnumKeyValueConfigurationSizing()
     */
    @Override
    public EnumKeyValueConfigurationSizing<Instant> createEnumKeyValueConfigurationSizing() {
        return new EnumKeyValueConfigurationSizing<Instant>();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Instant getMinSize() {
        return Instant.MIN;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public Instant getMaxSize() {
        return DateUtil.MAX_TIMESTAMP;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
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
