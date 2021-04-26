/*
 * TimestampConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import java.time.Instant;
import org.junit.jupiter.api.Test;


/**
 * Test timestamp converter test
 * 
 * @author patrick
 */
public class TimestampConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for TimestampValidatorTest
     */
    TimestampConverterTest() {
        super(EnumKeyValueConfigurationDataType.TIMESTAMP);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(Instant.parse("2021-03-15T12:34:55Z"), "2021-03-15T12:34:55Z");
        assertException("Text '2021-33-35T12:34:55Z' could not be parsed at index 0", "2021-33-35T12:34:55Z");
    }
}
