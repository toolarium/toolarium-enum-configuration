/*
 * DoubleConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test double converter test
 * 
 * @author patrick
 */
public class DoubleConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for DoubleConverterTest
     */
    DoubleConverterTest() {
        super(EnumKeyValueConfigurationDataType.DOUBLE);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(12.34567d, "12.34567");
        assertException("Invalid value [1A2.34567], it can not be converted into a DOUBLE data type.", "1A2.34567");
    }
}
