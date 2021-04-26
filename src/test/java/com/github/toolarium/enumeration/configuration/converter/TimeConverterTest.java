/*
 * TimeConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

/**
 * Test time converter test
 * 
 * @author patrick
 */
public class TimeConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for TimeValidatorTest
     */
    TimeConverterTest() {
        super(EnumKeyValueConfigurationDataType.TIME);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(LocalTime.parse("13:15:18.009"), "13:15:18.009");
        assertException("Text '33:15:18.009' could not be parsed: Invalid value for HourOfDay (valid values 0 - 23): 33", "33:15:18.009");
    }
}