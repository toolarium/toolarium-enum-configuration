/*
 * TimeValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import java.time.LocalTime;

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
        super(EnumKeyValueConfigurationDataType.TIME, 
              "00:01:00.000", 
              "23:00:00",
              /* valid values */
              new String[] {"13:15:18.009", "  13:15:18.009  ", "23:00"},
              /* invalid values */
              new String[] {"33:15:18.009", "2021-03-35T12:34:55Z", "23:00:01"},
              /* too small value */
              new String[] {"00:00:00"},        
              /* too big value */
              new String[] {"23:10", "23:00:2"});
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#isTooSmallValue(java.lang.String, java.lang.String)
     */
    @Override
    protected boolean isTooSmallValue(String value1, String value2) {
        return LocalTime.parse(value1).compareTo(LocalTime.parse(value2)) < 0;
    }
}