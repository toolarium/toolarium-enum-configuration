/*
 * ColorEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;

/**
 * Defines the color {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class ColorEnumKeyValueConfigurationValueValidator extends StringEnumKeyValueConfigurationValueValidator {
    private static final long serialVersionUID = -4901835673395526030L;

    
    /**
     * Constructor for ColorEnumKeyValueConfigurationValueValidator
     */
    public ColorEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.COLOR, EnumKeyValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 3L;
    }
}
