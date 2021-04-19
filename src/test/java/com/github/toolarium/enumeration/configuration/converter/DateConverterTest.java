/*
 * DateConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;


/**
 * Test date converter test
 * 
 * @author patrick
 */
public class DateConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for DateValidatorTest
     */
    DateConverterTest() {
        super(EnumValueConfigurationDataType.DATE);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(LocalDate.parse("2021-03-15"), "2021-03-15");
        assertException("Text '2021-43-15' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 43", "2021-43-15");
    }
}
