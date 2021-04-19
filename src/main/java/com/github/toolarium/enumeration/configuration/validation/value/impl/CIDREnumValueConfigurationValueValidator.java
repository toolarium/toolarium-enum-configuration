/*
 * CIDREnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;

/**
 * Defines the CIRD {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class CIDREnumValueConfigurationValueValidator extends StringEnumValueConfigurationValueValidator {
    private static final long serialVersionUID = 6634851645360839594L;

    
    /**
     * Constructor for CIDREnumValueConfigurationDataType
     */
    public CIDREnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.CIDR, EnumValueConfigurationDataType.NUMBER);
    }
}
