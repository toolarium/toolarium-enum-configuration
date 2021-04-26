/*
 * CIDRConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test cidr converter test
 * 
 * @author patrick
 */
public class CIDRConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for CIDRValidatorTest
     */
    CIDRConverterTest() {
        super(EnumKeyValueConfigurationDataType.CIDR);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("10.2.0.0/16", "10.2.0.0/16");        
        assertException("Invalid value: [10.2.0.0].", "10.2.0.0");
    }
}
