/*
 * CronValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;

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
        super(EnumValueConfigurationDataType.CRON);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("* * * * *");
        isValid("  * * * * *  ");
        isValid("  1/2 * * * *  ");
        isInValid("* * * *");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("* * * * *", "* * * * *");
        assertValue("* * * * *", "  * * * * *  ");
        assertValue("* * * * *", "  * * * * *  ");
        assertValue("1/2 * * * *", "  1/2 * * * *  ");
        assertException(IllegalArgumentException.class, "Invalid cron entry [* * * *]!", "* * * *");
    }
}
