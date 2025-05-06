/*
 * IEnumKeyConfigurationValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import java.util.Collection;


/**
 * Defines the validator for {@link EnumKeyValueConfiguration}.
 * 
 * @author patrick
 */
public interface IEnumConfigurationValueValidator {

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
}
