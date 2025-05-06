/*
 * IEnumKeyConfigurationValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;


/**
 * Defines the enum-configuration structure validator.
 * 
 * @author patrick
 */
public interface IEnumConfigurationStructureValidator {

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
}
