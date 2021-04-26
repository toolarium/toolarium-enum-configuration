/*
 * NumberConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test number converter test
 * 
 * @author patrick
 */
public class NumberConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for NumberValidatorTest
     */
    NumberConverterTest() {
        super(EnumKeyValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(12345678901234L, "12345678901234");
        assertException("For input string: \"123'45678901234\"", "123'45678901234");
    }
}
