/*
 * ColorEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;

/**
 * Defines the color {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class ColorEnumValueConfigurationValueValidator extends StringEnumValueConfigurationValueValidator {
    private static final long serialVersionUID = -4901835673395526030L;

    
    /**
     * Constructor for ColorEnumValueConfigurationDataType
     */
    public ColorEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.COLOR, EnumValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 3L;
    }
}
