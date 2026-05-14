/*
 * EmailConverterExtendedTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import org.junit.jupiter.api.Test;


/**
 * Extended test for email validation
 *
 * @author patrick
 */
public class EmailConverterExtendedTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for EmailConverterExtendedTest
     */
    EmailConverterExtendedTest() {
        super(EnumKeyValueConfigurationDataType.EMAIL);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        // Valid emails
        assertValue("user@domain.com", "user@domain.com");
        assertValue("a@b", "a@b");

        // Invalid: multiple @ signs
        assertException(ValidationException.class, "user@domain@other");

        // Invalid: empty local part
        assertException(ValidationException.class, "@domain.com");

        // Invalid: empty domain part
        assertException(ValidationException.class, "user@");

        // Invalid: just @
        assertException(ValidationException.class, "@");

        // Invalid: no @
        assertException(ValidationException.class, "nodomain");
    }
}
