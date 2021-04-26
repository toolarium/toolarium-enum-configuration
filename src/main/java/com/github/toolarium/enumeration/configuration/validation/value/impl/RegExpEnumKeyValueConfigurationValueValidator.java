/*
 * RegExpEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;

/**
 * Defines the regexp {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class RegExpEnumKeyValueConfigurationValueValidator extends StringEnumKeyValueConfigurationValueValidator {
    private static final long serialVersionUID = -2168825459260393815L;

    
    /**
     * Constructor for RegExpEnumKeyValueConfigurationValueValidator
     */
    public RegExpEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.REGEXP, EnumKeyValueConfigurationDataType.NUMBER);
    }
}
