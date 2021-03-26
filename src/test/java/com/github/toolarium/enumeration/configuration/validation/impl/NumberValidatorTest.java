/*
 * NumberValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test number validator test
 * 
 * @author patrick
 */
public class NumberValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for NumberValidatorTest
     */
    NumberValidatorTest() {
        super(EnumValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("123");
        isValid("12345678901234");
        isValid("  12345678901234  ");
        isInValid("123'45678901234");
        isInValid("1-B-2");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(12345678901234L, "12345678901234");
        assertException("For input string: \"123'45678901234\"", "123'45678901234");
    }
}
