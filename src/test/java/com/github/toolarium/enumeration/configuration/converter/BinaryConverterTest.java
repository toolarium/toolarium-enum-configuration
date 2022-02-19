/*
 * BinaryConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import org.junit.jupiter.api.Test;

/**
 * Test binary validator test
 * 
 * @author patrick
 */
public class BinaryConverterTest extends AbstractStringTypeConverterTest {
    //private static final String MESSAGE = "  my file content  "; 
    private static final String MESSAGE_ENCODED = "ICBteSBmaWxlIGNvbnRlbnQgIA=="; 

    
    /**
     * Constructor for BinaryConverterTest
     */
    BinaryConverterTest() {
        super(EnumKeyValueConfigurationDataType.BINARY);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(new EnumKeyValueConfigurationBinaryObject(null, null, null, "my file content"), "my file content");
        assertValue(new EnumKeyValueConfigurationBinaryObject(null, null, null, MESSAGE_ENCODED), MESSAGE_ENCODED);
        assertValue(new EnumKeyValueConfigurationBinaryObject("filename.txt", null, null, MESSAGE_ENCODED), "filename.txt|" + MESSAGE_ENCODED);
        assertValue(new EnumKeyValueConfigurationBinaryObject("filename.txt", DateUtil.getInstance().parseTimestamp("2021-03-15T08:59:22.123Z"), null, MESSAGE_ENCODED), "filename.txt|2021-03-15T08:59:22.123Z|" + MESSAGE_ENCODED);
        assertValue(new EnumKeyValueConfigurationBinaryObject("filename.txt", DateUtil.getInstance().parseTimestamp("2021-03-15T08:59:22.123Z"), "plain/text", MESSAGE_ENCODED), "filename.txt|2021-03-15T08:59:22.123Z|{plain/text}" + MESSAGE_ENCODED);
        assertException("Invalid value [filename.txt|2021-33-15T08:59:22.123Z|{plain/text}ICBteSBmaWxlIGNvbnRlbnQgIA==], it can not be converted into a BINARY data type: Text '2021-33-15T08:59:22.123Z' could not be parsed at index 0.", 
                        "filename.txt|2021-33-15T08:59:22.123Z|{plain/text}" + MESSAGE_ENCODED);
    }
}
