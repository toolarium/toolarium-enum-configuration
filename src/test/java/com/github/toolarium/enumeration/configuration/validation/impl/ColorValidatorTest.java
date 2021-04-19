/*
 * ColorValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;

/**
 * Test color validator test
 * 
 * @author patrick
 */
public class ColorValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for ColorValidatorTest
     */
    ColorValidatorTest() {
        super(EnumValueConfigurationDataType.COLOR,
              null, /* min value */
              null, /* max value */
              /* valid values */
              new String[] {"#12B356", "   #12B356  ", "#A2B356"},
              /* invalid values */
              new String[] {"12B356", "KFB356"},
              null, /* too small values */
              null  /* too big values */);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#createValueContent(java.lang.String)
     */
    @Override
    protected String createValueContent(String minValueSize) {
        return "#000000";
    }
}