/*
 * BinaryValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import org.junit.jupiter.api.Test;

/**
 * Test binary validator test
 * 
 * @author patrick
 */
public class BinaryValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for FileValidatorTest
     */
    BinaryValidatorTest() {
        super(EnumValueConfigurationDataType.BINARY);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("my file content");
        isValid("  my file content   ");
        isValid("filename.txt|my file content");
        isValid("filename.txt|  my file content  ");
        isValid("filename.txt|2021-03-15T08:59:22.123Z|  my file content  ");
        isValid("filename.txt|2021-03-15T08:59:22.123Z|plain/text|  my file content  ");
        isValid("|2021-03-15T08:59:22.123Z|plain/text|  my file content  ");
        
        isInValid("filename.txt|2021-33-15T08:59:22.123Z|plain/text|  my file content  ");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(new EnumValueConfigurationBinaryObject(null, null, null, "my file content"), "my file content");
        assertValue(new EnumValueConfigurationBinaryObject(null, null, null, "  my file content  "), "  my file content  ");
        assertValue(new EnumValueConfigurationBinaryObject("filename.txt", null, null, "  my file content  "), "filename.txt|  my file content  ");
        assertValue(new EnumValueConfigurationBinaryObject("filename.txt", DateUtil.getInstance().parseDate("2021-03-15T08:59:22.123Z"), null, "  my file content  "), "filename.txt|2021-03-15T08:59:22.123Z|  my file content  ");
        assertValue(new EnumValueConfigurationBinaryObject("filename.txt", DateUtil.getInstance().parseDate("2021-03-15T08:59:22.123Z"), "plain/text", "  my file content  "), "filename.txt|2021-03-15T08:59:22.123Z|plain/text|  my file content  ");

        assertException(IllegalArgumentException.class, "Invalid timestamp [2021-33-15T08:59:22.123Z]!", "filename.txt|2021-33-15T08:59:22.123Z|plain/text|  my file content  ");
    }
}
