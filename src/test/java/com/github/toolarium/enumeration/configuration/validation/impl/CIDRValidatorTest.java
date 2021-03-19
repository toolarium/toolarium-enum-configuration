/*
 * CIDRValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test cidr validator test
 * 
 * @author patrick
 */
public class CIDRValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for CIDRValidatorTest
     */
    CIDRValidatorTest() {
        super(EnumValueConfigurationDataType.CIDR);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("10.2.0.0/16");
        isValid("FF00::/8");
        isValid("  10.2.0.0/16  ");
        isValid("  FF00::/8   ");
        //isInValid("");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("10.2.0.0/16", "10.2.0.0/16");
        //assertException(IllegalArgumentException.class, "unknown protocol: httds", "httds://www.sbb.ch");
    }
}
