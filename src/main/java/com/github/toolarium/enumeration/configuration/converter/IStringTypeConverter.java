/*
 * IStringTypeConverter.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;


/**
 * Defines the string type converter.
 * 
 * @author patrick
 */
public interface IStringTypeConverter {
    
    /**
     * Convert
     *
     * @param dataType the data type 
     * @param input the input to validate
     * @param <T> the generic type
     * @return the result
     * @throws ValidationException In case of a validation error
     */
    <T> T convert(EnumKeyValueConfigurationDataType dataType, String input) throws ValidationException;
}
