/*
 * CertificateEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;


/**
 * Defines the certificate {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class CertificateEnumKeyValueConfigurationValueValidator extends StringEnumKeyValueConfigurationValueValidator {
    private static final long serialVersionUID = 750383057986668026L;

    
    /**
     * Constructor for CertificateEnumKeyValueConfigurationValueValidator
     */
    public CertificateEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.CERTIFICATE, EnumKeyValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 16L;
    }
}
