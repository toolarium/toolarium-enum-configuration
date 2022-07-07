/*
 * ClassPathUtilTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


/**
 * Test the ClassPathUtil.
 * 
 * @author patrick
 */
public class ClassPathUtilTest {

    /**
     * Test
     */
    @Test
    public void test() {
        assertEquals("com.github.toolarium.enumeration.configuration.util.ClassPathUtil", ClassPathUtil.getInstance().search("com.github.toolarium.enumeration.configuration.util.classpathutil", true).get(0).getName());
        assertTrue(ClassPathUtil.getInstance().search("com.github.toolarium.enumeration.configuration.util.classpathutil", false).isEmpty());
        assertTrue(ClassPathUtil.getInstance().search("org.slf4j.Logger", false).isEmpty());
        assertFalse(ClassPathUtil.getInstance().search("com.github.toolarium.enumeration.configuration.util.ClassPathUtil", false).isEmpty());
    }
}
