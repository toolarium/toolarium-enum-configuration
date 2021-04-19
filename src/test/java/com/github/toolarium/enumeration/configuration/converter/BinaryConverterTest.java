/*
 * BinaryConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import org.junit.jupiter.api.Test;

/**
 * Test binary validator test
 * 
 * @author patrick
 */
public class BinaryConverterTest extends AbstractStringTypeConverterTest {
    private static final String MESSAGE = "  my file content  "; 
    private static final String MESSAGE_ENCODED = "ICBteSBmaWxlIGNvbnRlbnQgIA=="; 

    
    /**
     * Constructor for FileValidatorTest
     */
    BinaryConverterTest() {
        super(EnumValueConfigurationDataType.BINARY);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(new EnumValueConfigurationBinaryObject(null, null, null, "my file content"), "my file content");
        assertValue(new EnumValueConfigurationBinaryObject(null, null, null, MESSAGE), MESSAGE);
        assertValue(new EnumValueConfigurationBinaryObject("filename.txt", null, null, MESSAGE), "filename.txt|" + MESSAGE);
        assertValue(new EnumValueConfigurationBinaryObject("filename.txt", DateUtil.getInstance().parseTimestamp("2021-03-15T08:59:22.123Z"), null, MESSAGE), "filename.txt|2021-03-15T08:59:22.123Z|" + MESSAGE);
        assertValue(new EnumValueConfigurationBinaryObject("filename.txt", DateUtil.getInstance().parseTimestamp("2021-03-15T08:59:22.123Z"), "plain/text", MESSAGE), "filename.txt|2021-03-15T08:59:22.123Z|plain/text|" + MESSAGE);
        assertException("Invalid timestamp [2021-33-15T08:59:22.123Z]!", "filename.txt|2021-33-15T08:59:22.123Z|plain/text|" + MESSAGE_ENCODED);
    }
}
