/*
 * DoubleValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;


/**
 * Test double validator test
 * 
 * @author patrick
 */
public class DoubleValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for DoubleValidatorTest
     */
    public DoubleValidatorTest() {
        super(EnumValueConfigurationDataType.DOUBLE, 
              "1", 
              "123",
              /* valid values */
              new String[] {"12.34567", "  12.34567  ", "26.51973"},
              /* invalid values */
              new String[] {"123'45678901234", "1A2.34567"},
              /* too small value */
              new String[] {"0.999"},        
              /* too big value */
              new String[] {"123.001", "124.0", "125", "321321321321"});
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#isTooSmallValue(java.lang.String, java.lang.String)
     */
    @Override
    protected boolean isTooSmallValue(String value1, String value2) {
        return Double.valueOf(value1).compareTo(Double.valueOf(value2)) < 0;
    }
}
