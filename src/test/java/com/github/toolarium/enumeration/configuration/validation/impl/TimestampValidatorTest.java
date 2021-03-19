/*
 * TimestampValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import java.time.Instant;
import org.junit.jupiter.api.Test;


/**
 * Test timestamp validator test
 * 
 * @author patrick
 */
public class TimestampValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for TimestampValidatorTest
     */
    TimestampValidatorTest() {
        super(EnumValueConfigurationDataType.TIMESTAMP);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("2021-03-15T12:34:55.00Z");
        isValid("  2021-03-15T12:34:55Z  ");
        isInValid("2021-03-15");
        isInValid("12:34:55Z");
        isInValid("2021-33-35T12:34:55Z");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(Instant.parse("2021-03-15T12:34:55Z"), "2021-03-15T12:34:55Z");
        assertException(IllegalArgumentException.class, "Text '2021-33-35T12:34:55Z' could not be parsed at index 0", "2021-33-35T12:34:55Z");
    }
}
