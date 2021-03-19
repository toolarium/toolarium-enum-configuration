/*
 * DoubleValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test double validator test
 * 
 * @author patrick
 */
public class DoubleValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for DoubleValidatorTest
     */
    DoubleValidatorTest() {
        super(EnumValueConfigurationDataType.DOUBLE);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("12.34567");
        isValid("  12.34567  ");
        isInValid("1A2.34567");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(12.34567d, "12.34567");
        assertException(IllegalArgumentException.class, "For input string: \"1A2.34567\"", "1A2.34567");
    }
}
