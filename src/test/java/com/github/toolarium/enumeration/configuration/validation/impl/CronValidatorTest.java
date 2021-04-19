/*
 * CronValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;

/**
 * Test uri validator test
 * 
 * @author patrick
 */
public class CronValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for CronValidatorTest
     */
    CronValidatorTest() {
        super(EnumValueConfigurationDataType.CRON, 
              null, 
              null,
              /* valid values */
              new String[] {"* * * * *", "  * * * * *  ", "  1/2 * * * *  "},
              /* invalid values */
              new String[] {"* * * *"},
              /* too small value */
              null,        
              /* too big value */
              null);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#createValueContent(java.lang.String)
     */
    @Override
    protected String createValueContent(String minValueSize) {
        return "* * * * *";
    }
}
