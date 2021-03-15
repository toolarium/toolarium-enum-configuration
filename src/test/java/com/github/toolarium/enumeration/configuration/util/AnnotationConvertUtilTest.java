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
import org.junit.jupiter.api.Test;


/**
 * Test the AnnotationConvertUtil
 * 
 * @author Meier Patrick
 * @version $Revision:  $
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
        assertNull(AnnotationConvertUtil.getInstance().parseDate(null));
        assertNotNull(AnnotationConvertUtil.getInstance().parseDate("9999-12-31T12:00:00.000Z"));
        assertNotNull(AnnotationConvertUtil.getInstance().parseDate("2020-05-26T12:34:56Z"));
    }
    
    
    /**
     * Test parse annotation
     */
    @Test
    public void testParseAnnotation() {
        com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration value = AnnotationConvertUtil.getInstance().getAnnotationInformation(MyConfigTest.FIRST);
        assertNotNull(value);
        assertEquals("FIRST", value.getKey());
        assertEquals("First", value.getDescription());
        assertEquals("11", value.getDefaultValue());
        //assertEquals("11", value.getValidFrom());
        //assertEquals("11", value.getValidTill());

        value = AnnotationConvertUtil.getInstance().getAnnotationInformation(MyConfigTest.SECOND);
        assertNotNull(value);
        assertEquals("SECOND", value.getKey());
        assertEquals("Second", value.getDescription());
        assertEquals("22", value.getDefaultValue());
        
    }
    
    
    
    @EnumConfiguration(description = "The description")
    public enum MyConfigTest implements IEnumConfiguration {
        @EnumValueConfiguration(description =  "First", defaultValue = "11")
        FIRST,
        
        @EnumValueConfiguration(description =  "Second", defaultValue = "22")
        SECOND;
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/