/*
 * AnnotationConvertUtilCardinalityTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import org.junit.jupiter.api.Test;


/**
 * Test parseCardinality edge cases in {@link AnnotationConvertUtil}.
 *
 * @author patrick
 */
public class AnnotationConvertUtilCardinalityTest {

    /**
     * Test standard cardinality "1..1"
     */
    @Test
    public void testStandardCardinality() {
        EnumKeyValueConfigurationSizing<Integer> sizing = AnnotationConvertUtil.getInstance().parseCardinality("1..1");
        assertNotNull(sizing);
        assertEquals(Integer.valueOf(1), sizing.getMinSize());
        assertEquals(Integer.valueOf(1), sizing.getMaxSize());
    }


    /**
     * Test optional cardinality "0..1"
     */
    @Test
    public void testOptionalCardinality() {
        EnumKeyValueConfigurationSizing<Integer> sizing = AnnotationConvertUtil.getInstance().parseCardinality("0..1");
        assertNotNull(sizing);
        assertEquals(Integer.valueOf(0), sizing.getMinSize());
        assertEquals(Integer.valueOf(1), sizing.getMaxSize());
    }


    /**
     * Test unbounded cardinality "0..*"
     */
    @Test
    public void testUnboundedCardinality() {
        EnumKeyValueConfigurationSizing<Integer> sizing = AnnotationConvertUtil.getInstance().parseCardinality("0..*");
        assertNotNull(sizing);
        assertEquals(Integer.valueOf(0), sizing.getMinSize());
        assertEquals(EnumKeyValueConfigurationSizing.MAX_CARDINALITY, sizing.getMaxSizeAsString());
    }


    /**
     * Test cardinality with larger numbers "1..100"
     */
    @Test
    public void testLargeCardinality() {
        EnumKeyValueConfigurationSizing<Integer> sizing = AnnotationConvertUtil.getInstance().parseCardinality("1..100");
        assertNotNull(sizing);
        assertEquals(Integer.valueOf(1), sizing.getMinSize());
        assertEquals(Integer.valueOf(100), sizing.getMaxSize());
    }


    /**
     * Test null cardinality
     */
    @Test
    public void testNullCardinality() {
        assertNull(AnnotationConvertUtil.getInstance().parseCardinality(null));
    }


    /**
     * Test empty cardinality
     */
    @Test
    public void testEmptyCardinality() {
        assertNull(AnnotationConvertUtil.getInstance().parseCardinality(""));
        assertNull(AnnotationConvertUtil.getInstance().parseCardinality("   "));
    }


    /**
     * Test cardinality with whitespace
     */
    @Test
    public void testCardinalityWithWhitespace() {
        EnumKeyValueConfigurationSizing<Integer> sizing = AnnotationConvertUtil.getInstance().parseCardinality("  1..5  ");
        assertNotNull(sizing);
        assertEquals(Integer.valueOf(1), sizing.getMinSize());
        assertEquals(Integer.valueOf(5), sizing.getMaxSize());
    }
}
