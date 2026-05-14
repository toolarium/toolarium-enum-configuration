/*
 * CronConverterExtendedTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import org.junit.jupiter.api.Test;


/**
 * Extended test for cron field validation
 *
 * @author patrick
 */
public class CronConverterExtendedTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for CronConverterExtendedTest
     */
    CronConverterExtendedTest() {
        super(EnumKeyValueConfigurationDataType.CRON);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        // Valid cron expressions
        assertValue("0 0 * * *", "0 0 * * *");
        assertValue("*/5 * * * *", "*/5 * * * *");
        assertValue("0 0 1 1 MON", "0 0 1 1 MON");
        assertValue("0 0 L * ?", "0 0 L * ?");
        assertValue("0 0 15W * *", "0 0 15W * *");
        assertValue("0 0 * * 5#3", "0 0 * * 5#3");

        // Invalid: garbage field values
        assertException(ValidationException.class, "! @ # $ %");
        assertException(ValidationException.class, "0 0 * * (bad)");
        assertException(ValidationException.class, ". . . . .");

        // Invalid: too few fields
        assertException(ValidationException.class, "* * * *");
    }
}
