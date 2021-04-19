/*
 * RegexpConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test regexp converter test
 * 
 * @author patrick
 */
public class RegexpConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for RegexpValidatorTest
     */
    RegexpConverterTest() {
        super(EnumValueConfigurationDataType.REGEXP);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("^.*[a-b]*$", "^.*[a-b]*$");
        assertException("Illegal character range near index 3\n[b-", "[b-");
    }
}
