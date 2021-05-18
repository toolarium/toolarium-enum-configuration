/*
 * UUIDEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;
import java.util.UUID;


/**
 * Defines the UUID {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class UUIDEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<UUID, Long> {
    private static final long serialVersionUID = 3680551029203917086L;

    
    /**
     * Constructor for UUIDEnumKeyValueConfigurationValueValidator
     */
    public UUIDEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.UUID, UUID.class, EnumKeyValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String)
     */
    @Override
    public UUID validateValue(EnumKeyValueConfigurationSizing<Long> valueSize, String inputValue) throws EmptyValueException, ValidationException {
        
        UUID inputUUID = parseValue(inputValue);
        MinMaxValue<Long> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return inputUUID;
        }

        if (inputUUID == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return inputUUID;
            }
            
            throw new EmptyValueException("Empty value: invalid string, should be at the length of [" + valueSize.getMinSizeAsString() + "]!");
        }
        
        return inputUUID;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createEnumKeyValueConfigurationSizing()
     */
    @Override
    public EnumKeyValueConfigurationSizing<Long> createEnumKeyValueConfigurationSizing() {
        return new EnumKeyValueConfigurationSizing<Long>();
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
        return 36L;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
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
