/*
 * BooleanValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Boolean validator test
 * 
 * @author patrick
 */
public class BooleanValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for BooleanValidatorTest
     */
    BooleanValidatorTest() {
        super(EnumValueConfigurationDataType.BOOLEAN);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Test
    public void testValidate() {
        isValid("true");
        isValid("yes");
        isValid("  true  ");
        isValid("  yes  ");

        isValid("True");
        isValid("Yes");
        isValid("  True  ");
        isValid("  Yes  ");


        isValid("false");
        isValid("no");
        isValid("  false ");
        isValid(" no ");

        isValid("False");
        isValid("No");
        isValid("  False ");
        isValid(" No ");

        isInValid("trus");
        isInValid("ja");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Test
    public void testConvert() {
        assertValue(Boolean.TRUE, "true");
        assertValue(Boolean.TRUE, "yes");
        assertValue(Boolean.TRUE, "  true  ");
        assertValue(Boolean.TRUE, "  yes  ");

        assertValue(Boolean.TRUE, "True");
        assertValue(Boolean.TRUE, "Yes");
        assertValue(Boolean.TRUE, "  True  ");
        assertValue(Boolean.TRUE, "  Yes  ");

        assertValue(Boolean.FALSE, "false");
        assertValue(Boolean.FALSE, "no");
        assertValue(Boolean.FALSE, "  false  ");
        assertValue(Boolean.FALSE, "  no  ");

        assertValue(Boolean.FALSE, "False");
        assertValue(Boolean.FALSE, "No");
        assertValue(Boolean.FALSE, "  False  ");
        assertValue(Boolean.FALSE, "  No  ");
        
        assertException(IllegalArgumentException.class, "Invalid value: [trus].", "trus");
        assertException(IllegalArgumentException.class, "Invalid value: [ja].", "ja");
    }
}
