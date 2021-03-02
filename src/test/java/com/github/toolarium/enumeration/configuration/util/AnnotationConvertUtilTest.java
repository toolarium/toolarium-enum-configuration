/*
 * AnnotationConvertUtilTest.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/