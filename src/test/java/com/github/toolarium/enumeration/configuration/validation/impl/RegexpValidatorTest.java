/*
 * RegexpValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test regexp validator test
 * 
 * @author patrick
 */
public class RegexpValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for RegexpValidatorTest
     */
    RegexpValidatorTest() {
        super(EnumValueConfigurationDataType.REGEXP);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("^.*[a-b]*$");
        isValid("  .*[a-b]*  ");
        isInValid("[b-");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("^.*[a-b]*$", "^.*[a-b]*$");
        assertException(IllegalArgumentException.class, "Illegal character range near index 3\n[b-", "[b-");
    }
}
