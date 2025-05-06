/*
 * TestInterfaceEnumConfigurationStructureValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validator;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.validation.IEnumConfigurationStructureValidator;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;


/**
 * Implement a test enum-configzuration interface structure validator
 *  
 * @author patrick
 */
public class TestInterfaceEnumConfigurationStructureValidator implements IEnumConfigurationStructureValidator {

    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumConfigurationValueValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration)
     */
    @Override
    public void validate(EnumKeyConfiguration enumKeyConfiguration) throws ValidationException {
        //throw new ValidationException("TTTTT:"+enumKeyConfiguration.getKey());
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumConfigurationValueValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration)
     */
    @Override
    public void validate(EnumKeyValueConfiguration enumKeyValueConfiguration) throws ValidationException {
        if (enumKeyValueConfiguration.getKey().equals("MYPORT")) {
            throw new ValidationException("Validation error:" + enumKeyValueConfiguration.getEnumerationValue());
        }
    }
}
