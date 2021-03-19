/*
 * CertificateValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


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
        super(EnumValueConfigurationDataType.CERTIFICATE);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("-- my certficiate --");
        isValid("  -- my certificate --   ");
        isInValid("123");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("-- my certificate --", "-- my certificate --");

        assertException(IllegalArgumentException.class, "Invalid value: [123].", "123");
    }
}
