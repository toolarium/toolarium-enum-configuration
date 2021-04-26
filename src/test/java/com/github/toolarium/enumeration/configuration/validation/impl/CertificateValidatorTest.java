/*
 * CertificateValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;


/**
 * Test certficate validator test
 * 
 * @author patrick
 */
public class CertificateValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for CertificateValidatorTest
     */
    CertificateValidatorTest() {
        super(EnumKeyValueConfigurationDataType.CERTIFICATE, 
              null, 
              null,
              /* valid values */
              new String[] {"-- my certficiate --", "  -- my certificate --   ", "-- your certficiate --"},
              /* invalid values */
              new String[] {"123"},
              /* too small value */
              null,        
              /* too big value */
              null);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#createValueContent(java.lang.String)
     */
    @Override
    protected String createValueContent(String minValueSize) {
        return "-- certificate --";
    }
}
