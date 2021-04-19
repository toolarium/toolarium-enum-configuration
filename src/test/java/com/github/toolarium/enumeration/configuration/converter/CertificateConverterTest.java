/*
 * CertificateConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test certficate converter test
 * 
 * @author patrick
 */
public class CertificateConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for CertificateValidatorTest
     */
    CertificateConverterTest() {
        super(EnumValueConfigurationDataType.CERTIFICATE);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("-- my certificate --", "-- my certificate --");

        assertException("Invalid value: [123].", "123");
    }
}
