/*
 * ColorConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import org.junit.jupiter.api.Test;

/**
 * Test color converter test
 * 
 * @author patrick
 */
public class ColorConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for ColorConverterTest
     */
    ColorConverterTest() {
        super(EnumKeyValueConfigurationDataType.COLOR);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("#12B356", "#12B356");
        assertException("Invalid value [#KFB356], it can not be converted into a COLOR data type.", "#KFB356");
    }
}
