/*
 * ColorValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;

/**
 * Test color validator test
 * 
 * @author patrick
 */
public class ColorValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for ColorValidatorTest
     */
    ColorValidatorTest() {
        super(EnumValueConfigurationDataType.COLOR);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("#12B356");
        isValid("  #12B356  ");
        isInValid("12B356");
        isInValid("KFB356");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("#12B356", "#12B356");
        assertException("Invalid value: [#KFB356].", "#KFB356");
    }
}
