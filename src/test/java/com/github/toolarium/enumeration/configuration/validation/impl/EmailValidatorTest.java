/*
 * EmailValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


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
        super(EnumValueConfigurationDataType.EMAIL);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("my@mail");
        isValid("  my@mail  ");
        isInValid("mymail");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("my@mail", "my@mail");
        assertException(IllegalArgumentException.class, "Invalid value: [mymail].", "mymail");
    }
}
