/*
 * CronEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;

/**
 * Defines the cron {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class CronEnumValueConfigurationValueValidator extends StringEnumValueConfigurationValueValidator {
    private static final long serialVersionUID = -6004454856535231787L;

    
    /**
     * Constructor for CronEnumValueConfigurationDataType
     */
    public CronEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.CRON, EnumValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 9L;
    }
}
