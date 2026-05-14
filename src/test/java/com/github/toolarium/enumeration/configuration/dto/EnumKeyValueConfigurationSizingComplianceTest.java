/*
 * EnumKeyValueConfigurationSizingComplianceTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * Test numeric comparison and compliance logic in EnumKeyValueConfigurationSizing
 *
 * @author patrick
 */
public class EnumKeyValueConfigurationSizingComplianceTest {

    /**
     * Test numeric comparison: 9 vs 100 (was broken with lexicographic comparison)
     */
    @Test
    public void testNumericComparisonMultiDigit() {
        EnumKeyValueConfigurationSizing<Long> base = new EnumKeyValueConfigurationSizing<>(0L, 100L);
        EnumKeyValueConfigurationSizing<Long> other = new EnumKeyValueConfigurationSizing<>(0L, 9L);
        // base max (100) > other max (9), so base is compliant (it allows a larger range)
        assertTrue(base.isCompliant(other).isValid());
    }


    /**
     * Test compliance when max sizes are equal
     */
    @Test
    public void testEqualSizes() {
        EnumKeyValueConfigurationSizing<Long> base = new EnumKeyValueConfigurationSizing<>(0L, 50L);
        EnumKeyValueConfigurationSizing<Long> other = new EnumKeyValueConfigurationSizing<>(0L, 50L);
        assertTrue(base.isCompliant(other).isValid());
    }


    /**
     * Test compliance with wildcard "*" as max
     */
    @Test
    public void testWildcardMax() {
        EnumKeyValueConfigurationSizing<Long> base = new EnumKeyValueConfigurationSizing<>(0L, 100L);
        base.setMaxSizeAsString(EnumKeyValueConfigurationSizing.MAX_CARDINALITY);

        EnumKeyValueConfigurationSizing<Long> other = new EnumKeyValueConfigurationSizing<>(0L, 100L);
        other.setMaxSizeAsString("999");

        // "*" is largest, so base is compliant
        assertTrue(base.isCompliant(other).isValid());
    }


    /**
     * Test compliance where other has wildcard "*"
     */
    @Test
    public void testOtherWildcardMax() {
        EnumKeyValueConfigurationSizing<Long> base = new EnumKeyValueConfigurationSizing<>(0L, 100L);
        base.setMaxSizeAsString("100");

        EnumKeyValueConfigurationSizing<Long> other = new EnumKeyValueConfigurationSizing<>(0L, 100L);
        other.setMaxSizeAsString(EnumKeyValueConfigurationSizing.MAX_CARDINALITY);

        // other has "*" which is larger than 100, so base max < other max -> not compliant
        assertFalse(base.isCompliant(other).isValid());
    }


    /**
     * Test compliance with both wildcards
     */
    @Test
    public void testBothWildcard() {
        EnumKeyValueConfigurationSizing<Long> base = new EnumKeyValueConfigurationSizing<>(0L, 100L);
        base.setMaxSizeAsString(EnumKeyValueConfigurationSizing.MAX_CARDINALITY);

        EnumKeyValueConfigurationSizing<Long> other = new EnumKeyValueConfigurationSizing<>(0L, 100L);
        other.setMaxSizeAsString(EnumKeyValueConfigurationSizing.MAX_CARDINALITY);

        assertTrue(base.isCompliant(other).isValid());
    }


    /**
     * Test that null sizing returns invalid
     */
    @Test
    public void testNullCompliance() {
        EnumKeyValueConfigurationSizing<Long> base = new EnumKeyValueConfigurationSizing<>(0L, 100L);
        assertFalse(base.isCompliant(null).isValid());
    }


    /**
     * Test min size compliance: base min > other min should fail
     */
    @Test
    public void testMinSizeNotCompliant() {
        EnumKeyValueConfigurationSizing<Long> base = new EnumKeyValueConfigurationSizing<>(10L, 100L);
        EnumKeyValueConfigurationSizing<Long> other = new EnumKeyValueConfigurationSizing<>(5L, 100L);
        assertFalse(base.isCompliant(other).isValid());
    }


    /**
     * Test null propagation fix: setting null size should not create "null" string
     */
    @Test
    public void testSetNullSizeDoesNotCreateNullString() {
        EnumKeyValueConfigurationSizing<Long> sizing = new EnumKeyValueConfigurationSizing<>();
        sizing.setMinSize(null);
        sizing.setMaxSize(null);
        // minSizeAsString/maxSizeAsString should remain null, not become "null"
        assertTrue(sizing.getMinSizeAsString() == null || !sizing.getMinSizeAsString().equals("null"));
        assertTrue(sizing.getMaxSizeAsString() == null || !sizing.getMaxSizeAsString().equals("null"));
    }
}
