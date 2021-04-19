/*
 * DoubleConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test double converter test
 * 
 * @author patrick
 */
public class DoubleConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for DoubleValidatorTest
     */
    DoubleConverterTest() {
        super(EnumValueConfigurationDataType.DOUBLE);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(12.34567d, "12.34567");
        assertException("For input string: \"1A2.34567\"", "1A2.34567");
    }
}
