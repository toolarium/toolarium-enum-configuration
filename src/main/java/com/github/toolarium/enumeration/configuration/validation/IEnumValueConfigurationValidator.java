/*
 * IEnumValueConfigurationValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;


/**
 * Defines the validator for {@link EnumValueConfiguration}.
 * 
 * @author patrick
 */
public interface IEnumValueConfigurationValidator {

    /**
     * Validate an {@link EnumValueConfiguration} against an input string
     *
     * @param enumValueConfiguration enum value configuration 
     * @param input the input to validate
     * @throws ValidationException In case of a validation error
     */
    void validate(EnumValueConfiguration enumValueConfiguration, String input)  throws ValidationException;

    
    /**
     * Validate consistency of an {@link EnumValueConfiguration}
     *
     * @param enumValueConfiguration enum value configuation 
     * @throws ValidationException In case of a validation error
     */
    void validate(EnumValueConfiguration enumValueConfiguration) throws ValidationException;   

    
    /**
     * Validate the data type against an input string
     *
     * @param dataType the data type 
     * @param cardinality the cardinality 
     * @param valueSize the value size
     * @param isOptional true if it is optional
     * @param input the input to validate
     * @throws ValidationException In case of a validation error
     */
    void validate(EnumValueConfigurationDataType dataType, EnumValueConfigurationSizing cardinality, EnumValueConfigurationSizing valueSize, boolean isOptional, String input) throws ValidationException;

    
    /**
     * Convert
     *
     * @param dataType the data type 
     * @param input the input to validate
     * @param <T> the generic type
     * @return the result
     * @throws ValidationException In case of a validation error
     */
    <T> T convert(EnumValueConfigurationDataType dataType, String input) throws ValidationException;
}
