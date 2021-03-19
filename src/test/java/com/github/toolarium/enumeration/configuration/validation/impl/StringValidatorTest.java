/*
 * StringValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;


import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test string validator test
 * 
 * @author patrick
 */
public class StringValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for StringValidatorTest
     */
    StringValidatorTest() {
        super(EnumValueConfigurationDataType.STRING);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("My string");
        isValid("  My string  ");
        //isInValid("My string");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("My string", "My string");
    }
}
