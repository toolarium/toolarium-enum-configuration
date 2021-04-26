/*
 * EmailEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;

/**
 * Defines the email {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class EmailEnumKeyValueConfigurationValueValidator extends StringEnumKeyValueConfigurationValueValidator {
    private static final long serialVersionUID = -5793204086995434283L;

    
    /**
     * Constructor for EmailEnumKeyValueConfigurationValueValidator
     */
    public EmailEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.EMAIL, EnumKeyValueConfigurationDataType.NUMBER);
    }
}
