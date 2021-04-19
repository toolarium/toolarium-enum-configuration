/*
 * CIDRValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;


/**
 * Test cidr validator test
 * 
 * @author patrick
 */
public class CIDRValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for CIDRValidatorTest
     */
    CIDRValidatorTest() {
        super(EnumValueConfigurationDataType.CIDR,
              null, /* min value */
              null, /* max value */
              /* valid values */
              new String[] {"10.2.0.0/16", "FF00::/8", "  10.2.0.0/16  ", "  FF00::/8   "},
              /* invalid values */
              new String[] {"10.2.0.0"},
              null, /* too small values */
              null  /* too big values */);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#createValueContent(java.lang.String)
     */
    @Override
    protected String createValueContent(String minValueSize) {
        return "0.0.0.0/8";
    }
}
