/*
 * StringConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;


import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test string converter test
 * 
 * @author patrick
 */
public class StringConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for StringValidatorTest
     */
    StringConverterTest() {
        super(EnumValueConfigurationDataType.STRING);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("My string", "My string");
    }
}
