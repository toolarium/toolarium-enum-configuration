/*
 * IEnumValueConfigurationValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;

/**
 * Defines the validator for {@link EnumValueConfiguration}.
 * 
 * @author patrick
 */
public interface IEnumValueConfigurationValidator {

    /**
     * Validate
     *
     * @param dataType the data type 
     * @param input the input to validate
     * @return the result
     */
    ValidationResult validate(EnumValueConfigurationDataType dataType, String input);


    /**
     * Convert
     *
     * @param dataType the data type 
     * @param input the input to validate
     * @param <T> the generic type
     * @return the result
     */
    <T> T convert(EnumValueConfigurationDataType dataType, String input);
}
