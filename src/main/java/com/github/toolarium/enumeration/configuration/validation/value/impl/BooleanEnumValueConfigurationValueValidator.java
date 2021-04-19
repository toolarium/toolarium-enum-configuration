/*
 * BooleanEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;


/**
 * Defines the boolean {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class BooleanEnumValueConfigurationValueValidator extends AbstractEnumValueConfigurationValueValidator<Boolean, Boolean> {
    private static final long serialVersionUID = 3112713117065033536L;


    /**
     * Constructor for BinaryEnumValueConfigurationDataType
     */
    public BooleanEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.BOOLEAN, Boolean.class, EnumValueConfigurationDataType.BOOLEAN);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, java.lang.String)
     */
    @Override
    public void validateValue(EnumValueConfigurationSizing<Boolean> valueSize, String inputValue) throws ValidationException {
        
        Boolean inputBoolean = parseValue(inputValue);
        MinMaxValue<Boolean> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return;
        }

        if (inputBoolean == null) {
            throw new ValidationException("Empty value: invalid boolean [" + inputValue + "]!");
        }
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#createEnumValueConfigurationSizing()
     */
    @Override
    public EnumValueConfigurationSizing<Boolean> createEnumValueConfigurationSizing() {
        return null;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Boolean getMinSize() {
        return null;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public Boolean getMaxSize() {
        return null;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
     */
    @Override
    protected boolean isGreaterThan(Boolean first, Boolean second) {
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
