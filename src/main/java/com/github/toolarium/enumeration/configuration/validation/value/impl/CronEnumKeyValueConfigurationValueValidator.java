/*
 * CronEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;

/**
 * Defines the cron {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class CronEnumKeyValueConfigurationValueValidator extends StringEnumKeyValueConfigurationValueValidator {
    private static final long serialVersionUID = -6004454856535231787L;

    
    /**
     * Constructor for CronEnumKeyValueConfigurationValueValidator
     */
    public CronEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.CRON, EnumKeyValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 9L;
    }
}
