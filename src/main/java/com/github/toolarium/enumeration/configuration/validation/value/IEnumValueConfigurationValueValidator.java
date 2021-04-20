/*
 * IEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;

/**
 * Defines the enum configuration data type
 * 
 * @author patrick
 */
public interface IEnumValueConfigurationValueValidator<T> {

    /**
     * Create value size
     *
     * @param minValue the min value
     * @param maxValue the max value
     * @return the preapred value
     * @throws ValidationException In case of invalid data
     */
    EnumValueConfigurationSizing<T> createValueSize(String minValue, String maxValue) throws ValidationException;


    /**
     * Validate value 
     *
     * @param inputValue the input value to validate
     * @param valueSize the value size
     * @throws EmptyValueException In case of an empty value
     * @throws ValidationException In case of a validation error
     */
    void validateValue(EnumValueConfigurationSizing<T> valueSize, String inputValue) throws EmptyValueException, ValidationException;
    
    
    /**
     * Create an {@link EnumValueConfigurationSizing}.
     *
     * @return the {@link EnumValueConfigurationSizing}.
     */
    EnumValueConfigurationSizing<T> createEnumValueConfigurationSizing();
}
