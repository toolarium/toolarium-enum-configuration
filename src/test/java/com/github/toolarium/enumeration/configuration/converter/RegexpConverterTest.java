/*
 * RegexpConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test regexp converter test
 * 
 * @author patrick
 */
public class RegexpConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for RegexpConverterTest
     */
    RegexpConverterTest() {
        super(EnumKeyValueConfigurationDataType.REGEXP);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue("^.*[a-b]*$", "^.*[a-b]*$");
        assertException("Invalid value [[b-], it can not be converted into a REGEXP data type: Illegal character range near index 3\n[b-.", "[b-");
    }
}
