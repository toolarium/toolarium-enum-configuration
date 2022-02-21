/*
 * Base64UtilTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test base64 content
 * 
 * @author patrick
 */
public class Base64UtilTest {
    
    /**
     * Test the base64 validation  
     */
    @Test
    public void test() {
        assertFalse(Base64Util.getInstance().isValidBase64(null));
        assertFalse(Base64Util.getInstance().isValidBase64(""));
        assertFalse(Base64Util.getInstance().isValidBase64("    "));
        assertTrue(Base64Util.getInstance().isValidBase64("name"));
    }
}
