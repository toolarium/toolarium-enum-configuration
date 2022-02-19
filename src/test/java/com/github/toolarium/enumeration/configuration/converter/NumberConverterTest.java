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
     * Constructor for NumberConverterTest
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
        assertException("Invalid value [123'45678901234], it can not be converted into a NUMBER data type.", "123'45678901234");
    }
}
