/*
 * ClassPathUtilCacheTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Test ClassPathUtil search caching
 *
 * @author patrick
 */
public class ClassPathUtilCacheTest {

    /**
     * Test that repeated searches return the same cached result
     */
    @Test
    public void testCacheHit() {
        List<Class<?>> first = ClassPathUtil.getInstance().search("com.github.toolarium.enumeration.configuration.util.ClassPathUtil", false);
        List<Class<?>> second = ClassPathUtil.getInstance().search("com.github.toolarium.enumeration.configuration.util.ClassPathUtil", false);
        assertSame(first, second, "Repeated search should return the same cached list instance");
    }


    /**
     * Test that case sensitivity produces different cache entries
     */
    @Test
    public void testCaseSensitiveCacheKeys() {
        List<Class<?>> caseSensitive = ClassPathUtil.getInstance().search("com.github.toolarium.enumeration.configuration.util.ClassPathUtil", false);
        List<Class<?>> caseInsensitive = ClassPathUtil.getInstance().search("com.github.toolarium.enumeration.configuration.util.ClassPathUtil", true);
        // Both should find results but they are separate cache entries
        assertTrue(caseSensitive.size() >= 1, "Case-sensitive search should find at least one class");
        assertTrue(caseInsensitive.size() >= 1, "Case-insensitive search should find at least one class");
    }


    /**
     * Test null and blank inputs return empty lists
     */
    @Test
    public void testNullAndBlankInputs() {
        assertTrue(ClassPathUtil.getInstance().search(null, false).isEmpty());
        assertTrue(ClassPathUtil.getInstance().search("", false).isEmpty());
        assertTrue(ClassPathUtil.getInstance().search("   ", false).isEmpty());
    }


    /**
     * Test package with no dot returns empty
     */
    @Test
    public void testNoDotInClassName() {
        assertTrue(ClassPathUtil.getInstance().search("NoDotClassName", false).isEmpty());
    }
}
