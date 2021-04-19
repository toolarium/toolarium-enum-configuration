/*
 * CertificateEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;


/**
 * Defines the certificate {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class CertificateEnumValueConfigurationValueValidator extends StringEnumValueConfigurationValueValidator {
    private static final long serialVersionUID = 750383057986668026L;

    
    /**
     * Constructor for CertificateEnumValueConfigurationDataType
     */
    public CertificateEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.CERTIFICATE, EnumValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 16L;
    }
}
