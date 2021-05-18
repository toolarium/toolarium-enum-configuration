/*
 * StringValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;


import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test string validator test
 * 
 * @author patrick
 */
public class StringValidatorTest extends AbstractValidatorTest {
    private static final String STRING_MAX_CONTENT = "123456789012345678901234567890";
    private static final String STRING_TOO_LONG_CONTENT = " " + STRING_MAX_CONTENT + " ";

    
    /**
     * Constructor for StringValidatorTest
     */
    StringValidatorTest() {
        super(EnumKeyValueConfigurationDataType.STRING, 
              "2", 
              "" + STRING_MAX_CONTENT.length(),
              /* valid values */
              new String[] {"My string1", "  My string2   ", STRING_MAX_CONTENT},
              /* invalid values */
              new String[] {STRING_TOO_LONG_CONTENT},
              /* too small value */
              new String[] {"m"},        
              /* too big value */
              new String[] {STRING_TOO_LONG_CONTENT, "too big text !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"},
              true /* uniqueness */);
    }


    /**
     * Test valid and invalid objects.
     */
    @Test
    public void testValidateWithValidatorMinSize() {
        testValidate(getValidatorMinValueSizeAsString(), 
                     "" + STRING_MAX_CONTENT.length(),
                     /* valid values */
                     new String[] {"My string1", "  My string2   ", STRING_MAX_CONTENT},
                     /* invalid values */
                     new String[] {STRING_TOO_LONG_CONTENT},
                     /* too small value */
                     null,        
                     /* too big value */
                     new String[] {STRING_TOO_LONG_CONTENT, "too big text !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"});
    }

    
    /**
     * Test object in range.
     */
    @Test
    public void testRangeWithValidatorMinSize() {
        testRange(getValidatorMinValueSizeAsString(), 
                  "" + STRING_MAX_CONTENT.length(),
                  /* valid values */
                  new String[] {"My string1", "  My string2   ", STRING_MAX_CONTENT},
                  /* invalid values */
                  new String[] {STRING_TOO_LONG_CONTENT},
                  /* too small value */
                  null,        
                  /* too big value */
                  new String[] {STRING_TOO_LONG_CONTENT, "too big text !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"});
    }

    
    /**
     * Test object in range.
     */
    @Test
    public void testStringRange() {
        testRange("4", 
                  "4",
                  /* valid values */
                  new String[] {"1234", "abcd", "    "},
                  /* invalid values */
                  new String[] {" 1234 "},
                  /* too small value */
                  new String[] {"123"},
                  /* too big value */
                  new String[] {"1234 "});
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#createValueContent(java.lang.String)
     */
    @Override
    protected String createValueContent(String minValueSize) {
        
        String result = "";
        long size = Long.valueOf(minValueSize);
        for (int i = 0; i < size; i++) {
            result += ".";
        }
        
        return result;
    }
}
