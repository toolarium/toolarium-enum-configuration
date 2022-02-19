/*
 * BooleanConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Boolean validator test
 * 
 * @author patrick
 */
public class BooleanConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for BooleanConverterTest
     */
    BooleanConverterTest() {
        super(EnumKeyValueConfigurationDataType.BOOLEAN);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
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
        
        assertException("Invalid value [trus], it can not be converted into a BOOLEAN data type.", "trus");
        assertException("Invalid value [ja], it can not be converted into a BOOLEAN data type.", "ja");
    }
}
