/*
 * CIDREnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;

/**
 * Defines the CIRD {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class CIDREnumKeyValueConfigurationValueValidator extends StringEnumKeyValueConfigurationValueValidator {
    private static final long serialVersionUID = 6634851645360839594L;

    
    /**
     * Constructor for CIDREnumKeyValueConfigurationValueValidator
     */
    public CIDREnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.CIDR, EnumKeyValueConfigurationDataType.NUMBER);
    }
}
