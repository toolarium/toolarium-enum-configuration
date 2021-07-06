/*
 * DateValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import java.time.LocalDate;


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
        super(EnumKeyValueConfigurationDataType.DATE, 
              "1973-01-01", 
              "2037-05-31",
              /* valid values */
              new String[] {"2021-03-15", "  2021-03-14  ", "1973-02-01"},
              /* invalid values */
              new String[] {"2021-03-35", "2021-03-35T12:34:55Z"},
              /* too small value */
              new String[] {"1972-12-31"},        
              /* too big value */
              new String[] {"2037-06-01", "2038-01-01"},
              true /* isUniqueness */);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#isTooSmallValue(java.lang.String, java.lang.String)
     */
    @Override
    protected boolean isTooSmallValue(String value1, String value2) {
        return LocalDate.parse(value1).compareTo(LocalDate.parse(value2)) < 0;
    }
}