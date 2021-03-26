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
import com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test the AnnotationConvertUtil
 * 
 * @author patrick
 */
public class AnnotationConvertUtilTest {
    
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
        assertNotNull(DateUtil.getInstance().parseDate("9999-12-31T12:00:00.000Z"));
        assertNotNull(DateUtil.getInstance().parseDate("2020-05-26T12:34:56Z"));
    }
    
    
    /**
     * Test parse annotation
     */
    @Test
    public void testParseAnnotation() {
        com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration value = EnumUtil.getInstance().getAnnotationInformation(MyConfigTest.FIRST);
        assertNotNull(value);
        assertEquals("FIRST", value.getKey());
        assertEquals("First", value.getDescription());
        assertEquals("11", value.getDefaultValue());
        //assertEquals("11", value.getValidFrom());
        //assertEquals("11", value.getValidTill());

        value = EnumUtil.getInstance().getAnnotationInformation(MyConfigTest.SECOND);
        assertNotNull(value);
        assertEquals("SECOND", value.getKey());
        assertEquals("Second", value.getDescription());
        assertEquals("22", value.getDefaultValue());
        
    }
    

    /**
     * Test the prepareValueSize
     */
    @Test
    public void testPrepareValueSize() {
        assertNull(AnnotationConvertUtil.getInstance().prepareValueSize(null, null));
        assertNull(AnnotationConvertUtil.getInstance().prepareValueSize("", ""));
        assertEquals(new EnumValueConfigurationSizing(0, 1), AnnotationConvertUtil.getInstance().prepareValueSize("0", "1"));
        assertEquals(new EnumValueConfigurationSizing(0, 1), AnnotationConvertUtil.getInstance().prepareValueSize("", "  1  "));
        assertEquals(new EnumValueConfigurationSizing(0, 1), AnnotationConvertUtil.getInstance().prepareValueSize("  0 ", " 1  "));
        assertEquals(new EnumValueConfigurationSizing(1, 2), AnnotationConvertUtil.getInstance().prepareValueSize("  1", " 2  "));
        assertEquals(new EnumValueConfigurationSizing(0, Integer.MAX_VALUE), AnnotationConvertUtil.getInstance().prepareValueSize("", "*"));
        assertEquals(new EnumValueConfigurationSizing(1, Integer.MAX_VALUE), AnnotationConvertUtil.getInstance().prepareValueSize("1", "*"));
      
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationConvertUtil.getInstance().prepareValueSize("*", "4");
        });
    }

    
    /**
     * Test the parseCardinality
     */
    @Test
    public void testParseCardinality() {
        assertNull(AnnotationConvertUtil.getInstance().parseCardinality(null));
        assertNull(AnnotationConvertUtil.getInstance().parseCardinality(""));
        assertEquals(new EnumValueConfigurationSizing(0, 1), AnnotationConvertUtil.getInstance().parseCardinality("1"));
        assertEquals(new EnumValueConfigurationSizing(0, 1), AnnotationConvertUtil.getInstance().parseCardinality("  1  "));
        assertEquals(new EnumValueConfigurationSizing(0, 1), AnnotationConvertUtil.getInstance().parseCardinality("  0..1  "));
        assertEquals(new EnumValueConfigurationSizing(1, 2), AnnotationConvertUtil.getInstance().parseCardinality("  1   .. 2  "));
        assertEquals(new EnumValueConfigurationSizing(0, Integer.MAX_VALUE), AnnotationConvertUtil.getInstance().parseCardinality("*"));
        assertEquals(new EnumValueConfigurationSizing(1, Integer.MAX_VALUE), AnnotationConvertUtil.getInstance().parseCardinality("1..*"));
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationConvertUtil.getInstance().parseCardinality("*..1");
        });
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationConvertUtil.getInstance().parseCardinality("..1");
        });
    }
    
   
    
    @EnumConfiguration(description = "The description")
    public enum MyConfigTest implements IEnumConfiguration {
        @EnumValueConfiguration(description =  "First", defaultValue = "11", exampleValue = "42")
        FIRST,
        
        @EnumValueConfiguration(description =  "Second", defaultValue = "22", exampleValue = "42")
        SECOND;
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/