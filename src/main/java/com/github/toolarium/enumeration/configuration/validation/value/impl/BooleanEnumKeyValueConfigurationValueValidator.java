/*
 * BooleanEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;


/**
 * Defines the boolean {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class BooleanEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<Boolean, Boolean> {
    private static final long serialVersionUID = 3112713117065033536L;


    /**
     * Constructor for BooleanEnumKeyValueConfigurationValueValidator
     */
    public BooleanEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.BOOLEAN, Boolean.class, EnumKeyValueConfigurationDataType.BOOLEAN);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String)
     */
    @Override
    public Boolean validateValue(EnumKeyValueConfigurationSizing<Boolean> valueSize, String inputValue) throws EmptyValueException, ValidationException {
        
        Boolean inputBoolean = parseValue(inputValue);
        MinMaxValue<Boolean> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return inputBoolean;
        }

        if (inputBoolean == null) {
            throw new EmptyValueException("Empty value: invalid boolean [" + inputValue + "]!");
        }
        
        return inputBoolean;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createEnumKeyValueConfigurationSizing()
     */
    @Override
    public EnumKeyValueConfigurationSizing<Boolean> createEnumKeyValueConfigurationSizing() {
        return null;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Boolean getMinSize() {
        return null;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public Boolean getMaxSize() {
        return null;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
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
