/*
 * TimeValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

/**
 * Test time validator test
 * 
 * @author patrick
 */
public class TimeValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for TimeValidatorTest
     */
    TimeValidatorTest() {
        super(EnumValueConfigurationDataType.TIME);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("13:15:18.009");
        isValid("  13:15:18.009  ");
        isInValid("33:15:18.009");
        isInValid("2021-03-35T12:34:55Z");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(LocalTime.parse("13:15:18.009"), "13:15:18.009");
        assertException("Text '33:15:18.009' could not be parsed: Invalid value for HourOfDay (valid values 0 - 23): 33", "33:15:18.009");
    }
}