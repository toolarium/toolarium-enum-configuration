/*
 * UUIDEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;
import java.util.UUID;


/**
 * Defines the UUID {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class UUIDEnumValueConfigurationValueValidator extends AbstractEnumValueConfigurationValueValidator<UUID, Long> {
    private static final long serialVersionUID = 3680551029203917086L;

    
    /**
     * Constructor for UUIDEnumValueConfigurationDataType
     */
    public UUIDEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.UUID, UUID.class, EnumValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, java.lang.String)
     */
    @Override
    public void validateValue(EnumValueConfigurationSizing<Long> valueSize, String inputValue) throws EmptyValueException, ValidationException {
        
        UUID inputUUID = parseValue(inputValue);
        MinMaxValue<Long> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return;
        }

        if (inputUUID == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return;
            }
            
            throw new EmptyValueException("Empty value: invalid string, should be at the length of [" + valueSize.getMinSizeAsString() + "]!");
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
        return 36L;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
     */
    @Override
    protected boolean isGreaterThan(Long first, Long second) {
        if (first == null) {
            if (second == null) {
                return true;
            }
            
            return false;
        } else if (second == null) {
            return false;
        }
        
        return first.compareTo(second) > 0;
    }
}
