/*
 * EmailEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;

/**
 * Defines the email {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class EmailEnumValueConfigurationValueValidator extends StringEnumValueConfigurationValueValidator {
    private static final long serialVersionUID = -5793204086995434283L;

    
    /**
     * Constructor for EmailEnumValueConfigurationDataType
     */
    public EmailEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.EMAIL, EnumValueConfigurationDataType.NUMBER);
    }
}
