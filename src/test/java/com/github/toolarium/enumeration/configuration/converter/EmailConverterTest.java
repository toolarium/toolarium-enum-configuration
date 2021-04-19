/*
 * EmailConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test email converter test
 * 
 * @author patrick
 */
public class EmailConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for MailValidatorTest
     */
    EmailConverterTest() {
        super(EnumValueConfigurationDataType.EMAIL);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("my@mail", "my@mail");
        assertException("Invalid value: [mymail].", "mymail");
    }
}
