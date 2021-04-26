/*
 * AnnotationConvertUtilTest.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.toolarium.enumeration.configuration.IEnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test the AnnotationConvertUtil
 * 
 * @author patrick
 */
public class AnnotationConvertUtilTest {
    private static final String ONE_STRING = "1";


    /**
     * Test the trim quotation marks
     */
    @Test
    public void testTrimQuotationMarks() {
        assertNull(AnnotationConvertUtil.getInstance().trimQuotationMarks(null));
        assertNotNull(AnnotationConvertUtil.getInstance().trimQuotationMarks(""));
        assertEquals("", AnnotationConvertUtil.getInstance().trimQuotationMarks(""));
        assertEquals("abc", AnnotationConvertUtil.getInstance().trimQuotationMarks("abc"));
        assertEquals("abc", AnnotationConvertUtil.getInstance().trimQuotationMarks("\"abc\""));
    }
    

    /**
     * Test parse date
     */
    @Test
    public void testParseDate() {
        assertNull(DateUtil.getInstance().parseDate(null));
        assertNotNull(DateUtil.getInstance().parseTimestamp("9999-12-31T12:00:00.000Z"));
        assertNotNull(DateUtil.getInstance().parseTimestamp("2020-05-26T12:34:56Z"));
    }
    
    
    /**
     * Test parse annotation
     */
    @Test
    public void testParseEnumKeyConfigurationAnnotation() {
        com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration value = EnumUtil.getInstance().getEnumKeyConfigurationAnnotationInformation(MyConfigTest.THIRD);
        assertNotNull(value);
        assertEquals("THIRD", value.getKey());
        assertEquals("Third", value.getDescription());
        //assertEquals("11", value.getValidFrom());
        //assertEquals("11", value.getValidTill());

        assertNull(EnumUtil.getInstance().getEnumKeyConfigurationAnnotationInformation(MyConfigTest.SECOND));
    }

    
    /**
     * Test parse annotation
     */
    @Test
    public void testParseEnumKeyValueConfigurationAnnotation() {
        com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration value = EnumUtil.getInstance().getEnumKeyValueConfigurationAnnotationInformation(MyConfigTest.FIRST);
        assertNotNull(value);
        assertEquals("FIRST", value.getKey());
        assertEquals("First", value.getDescription());
        assertEquals("11", value.getDefaultValue());
        //assertEquals("11", value.getValidFrom());
        //assertEquals("11", value.getValidTill());

        value = EnumUtil.getInstance().getEnumKeyValueConfigurationAnnotationInformation(MyConfigTest.SECOND);
        assertNotNull(value);
        assertEquals("SECOND", value.getKey());
        assertEquals("Second", value.getDescription());
        assertEquals("22", value.getDefaultValue());
        
        assertNull(EnumUtil.getInstance().getEnumKeyValueConfigurationAnnotationInformation(MyConfigTest.THIRD));
    }


    /**
     * Test the parseCardinality
     */
    @Test
    public void testParseCardinality() {
        assertNull(AnnotationConvertUtil.getInstance().parseCardinality(null));
        assertNull(AnnotationConvertUtil.getInstance().parseCardinality(""));
        assertEquals(new EnumKeyValueConfigurationSizing<Integer>(ONE_STRING, 1, ONE_STRING, 1), AnnotationConvertUtil.getInstance().parseCardinality(ONE_STRING));
        assertEquals(new EnumKeyValueConfigurationSizing<Integer>(ONE_STRING, 1, ONE_STRING, 1), AnnotationConvertUtil.getInstance().parseCardinality("  1  "));
        assertEquals(new EnumKeyValueConfigurationSizing<Integer>("0", 0, ONE_STRING, 1), AnnotationConvertUtil.getInstance().parseCardinality("  0..1  "));
        assertEquals(new EnumKeyValueConfigurationSizing<Integer>(ONE_STRING, 1, "2", 2), AnnotationConvertUtil.getInstance().parseCardinality("  1   .. 2  "));
        assertEquals(new EnumKeyValueConfigurationSizing<Integer>(ONE_STRING, 1, "*", Integer.MAX_VALUE), AnnotationConvertUtil.getInstance().parseCardinality("*"));
        assertEquals(new EnumKeyValueConfigurationSizing<Integer>(ONE_STRING, 1, "*", Integer.MAX_VALUE), AnnotationConvertUtil.getInstance().parseCardinality("1..*"));
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationConvertUtil.getInstance().parseCardinality("*..1");
        });
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationConvertUtil.getInstance().parseCardinality("..1");
        });
    }
    
       
    @EnumConfiguration(description = "The description")
    public enum MyConfigTest implements IEnumConfiguration {
        @EnumKeyValueConfiguration(description =  "First", defaultValue = "11", exampleValue = "42")
        FIRST,
        
        @EnumKeyValueConfiguration(description =  "Second", defaultValue = "22", exampleValue = "42")
        SECOND,
        
        @EnumKeyConfiguration(description =  "Third")
        THIRD;
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/