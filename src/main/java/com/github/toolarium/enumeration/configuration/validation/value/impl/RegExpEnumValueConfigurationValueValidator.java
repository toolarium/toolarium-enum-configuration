/*
 * RegExpEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;

/**
 * Defines the regexp {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class RegExpEnumValueConfigurationValueValidator extends StringEnumValueConfigurationValueValidator {
    private static final long serialVersionUID = -2168825459260393815L;

    
    /**
     * Constructor for RegExpEnumValueConfigurationDataType
     */
    public RegExpEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.REGEXP, EnumValueConfigurationDataType.NUMBER);
    }
}
