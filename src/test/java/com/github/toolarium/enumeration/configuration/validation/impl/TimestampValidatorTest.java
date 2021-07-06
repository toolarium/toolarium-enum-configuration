/*
 * TimestampValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import java.time.Instant;


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
        super(EnumKeyValueConfigurationDataType.TIMESTAMP, 
              "1973-01-01T00:00:00.0Z", 
              "2037-05-31T00:00:00.0Z",
              /* valid values */
              new String[] {"2021-03-15T12:34:55.00Z", "  2021-03-15T13:34:55Z  ", "2021-03-10T14:34:55.00Z"},
              /* invalid values */
              new String[] {"2021-03-15", "12:34:55Z", "2021-33-35T12:34:55Z"},
              /* too small value */
              new String[] {"1972-12-31T00:00:00.0Z"},        
              /* too big value */
              new String[] {"2037-06-01T00:00:00.0Z", "2038-01-01T00:00:00.0Z"},
              true /* isUniqueness */);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#isTooSmallValue(java.lang.String, java.lang.String)
     */
    @Override
    protected boolean isTooSmallValue(String value1, String value2) {
        return Instant.parse(value1).compareTo(Instant.parse(value2)) < 0;
    }
}
