/*
 * DateValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;


/**
 * Test date validator test
 * 
 * @author patrick
 */
public class DateValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for DateValidatorTest
     */
    DateValidatorTest() {
        super(EnumValueConfigurationDataType.DATE);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("2021-03-15");
        isValid("  2021-03-15  ");
        isInValid("2021-03-35");
        isInValid("2021-03-35T12:34:55Z");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(LocalDate.parse("2021-03-15"), "2021-03-15");
        assertException("Text '2021-43-15' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 43", "2021-43-15");
    }
}
