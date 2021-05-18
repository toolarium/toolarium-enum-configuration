/*
 * IEnumKeyConfigurationValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import java.util.Collection;


/**
 * Defines the validator for {@link EnumKeyValueConfiguration}.
 * 
 * @author patrick
 */
public interface IEnumKeyConfigurationValidator {

    /**
     * Validate an {@link EnumKeyConfiguration} against an input string
     *
     * @param enumKeyConfiguration enum key configuration 
     * @param input the input to validate
     * @throws ValidationException In case of a validation error
     */
    void validate(EnumKeyConfiguration enumKeyConfiguration, String input)  
        throws ValidationException;

    
    /**
     * Validate an {@link EnumKeyValueConfiguration} against an input string
     * 
     * @param <D> The validated data type  
     * @param enumKeyValueConfiguration enum key/value configuration 
     * @param input the input to validate
     * @return the validated values
     * @throws ValidationException In case of a validation error
     */
    <D> Collection<D> validate(EnumKeyValueConfiguration enumKeyValueConfiguration, String input)  
        throws ValidationException;

    
    /**
     * Validate consistency of an {@link EnumKeyConfiguration}
     *
     * @param enumKeyConfiguration enum key configuration 
     * @throws ValidationException In case of a validation error
     */
    void validate(EnumKeyConfiguration enumKeyConfiguration) 
        throws ValidationException;   

    
    /**
     * Validate consistency of an {@link EnumKeyValueConfiguration}
     *
     * @param enumKeyValueConfiguration enum key/value configuration 
     * @throws ValidationException In case of a validation error
     */
    void validate(EnumKeyValueConfiguration enumKeyValueConfiguration) 
        throws ValidationException;   

    
    /**
     * Validate the data type against an input string
     *
     * @param <D> the validated data type
     * @param <T> the size type
     * @param dataType the data type 
     * @param cardinality the cardinality
     * @param uniqueness True if it is unique; otherwise false, which means that the same value can occur more than once. 
     * @param valueSize the value size
     * @param enumerationValue In case the input has to be inside the enumeration
     * @param input the input to validate
     * @return the validated values
     * @throws ValidationException In case of a validation error
     */
    <D,T> Collection<D> validate(EnumKeyValueConfigurationDataType dataType, 
                                 EnumKeyValueConfigurationSizing<Integer> cardinality,
                                 boolean uniqueness,
                                 EnumKeyValueConfigurationSizing<T> valueSize,
                                 String enumerationValue,
                                 String input) 
        throws ValidationException;
}
