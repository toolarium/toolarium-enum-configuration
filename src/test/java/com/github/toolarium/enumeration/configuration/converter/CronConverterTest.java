/*
 * CronConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;

/**
 * Test uri converter test
 * 
 * @author patrick
 */
public class CronConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for CronValidatorTest
     */
    CronConverterTest() {
        super(EnumValueConfigurationDataType.CRON);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("* * * * *", "* * * * *");
        assertValue("* * * * *", "  * * * * *  ");
        assertValue("* * * * *", "  * * * * *  ");
        assertValue("1/2 * * * *", "  1/2 * * * *  ");
        assertException("Invalid cron entry [* * * *]!", "* * * *");
    }
}
