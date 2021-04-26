/*
 * EmailValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;


/**
 * Test email validator test
 * 
 * @author patrick
 */
public class EmailValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for MailValidatorTest
     */
    EmailValidatorTest() {
        super(EnumKeyValueConfigurationDataType.EMAIL,
              null, /* min value */
              null, /* max value */
              /* valid values */
              new String[] {"my@mail", "  my@mail  ", "your@mail"},
              /* invalid values */
              new String[] {"mymail"},
              null, /* too small values */
              null  /* too big values */);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#createValueContent(java.lang.String)
     */
    @Override
    protected String createValueContent(String minValueSize) {
        
        String result = "";
        
        if (minValueSize != null) {
            long size = Long.valueOf(minValueSize);
            for (int i = 0; i < size; i++) {
                result += ".";
            }
        }
        
        return result;
    }
}
