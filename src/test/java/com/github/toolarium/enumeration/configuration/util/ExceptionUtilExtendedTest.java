/*
 * ExceptionUtilExtendedTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * Extended tests for {@link ExceptionUtil}.
 *
 * @author patrick
 */
public class ExceptionUtilExtendedTest {

    /**
     * Test prepareStacktraceInMessage with null exception
     */
    @Test
    public void testPrepareStacktraceWithNullException() {
        assertEquals("", ExceptionUtil.getInstance().prepareStacktraceInMessage(null, "com.github"));
    }


    /**
     * Test throwsException creates correct exception type
     */
    @Test
    public void testThrowsExceptionCreatesCorrectType() {
        IllegalArgumentException ex = ExceptionUtil.getInstance().throwsException(IllegalArgumentException.class, "test message");
        assertNotNull(ex);
        assertEquals("test message", ex.getMessage());
    }


    /**
     * Test throwsException with null message
     */
    @Test
    public void testThrowsExceptionWithNullMessage() {
        IllegalArgumentException ex = ExceptionUtil.getInstance().throwsException(IllegalArgumentException.class, null);
        assertNotNull(ex);
        assertEquals("", ex.getMessage());
    }


    /**
     * Test throwsException with null class throws IllegalArgumentException
     */
    @Test
    public void testThrowsExceptionWithNullClass() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExceptionUtil.getInstance().throwsException(null, "test");
        });
    }


    /**
     * Test throwsException with stack trace
     */
    @Test
    public void testThrowsExceptionWithStackTrace() {
        StackTraceElement[] trace = new StackTraceElement[]{
            new StackTraceElement("com.test.MyClass", "myMethod", "MyClass.java", 42)
        };
        IllegalArgumentException ex = ExceptionUtil.getInstance().throwsException(IllegalArgumentException.class, "test", trace);
        assertNotNull(ex);
        assertEquals(1, ex.getStackTrace().length);
        assertEquals("com.test.MyClass", ex.getStackTrace()[0].getClassName());
    }


    /**
     * Test throwExceptionWithStacktraceInMessage with null exception
     */
    @Test
    public void testThrowExceptionWithStacktraceInMessageNullException() {
        IllegalArgumentException ex = ExceptionUtil.getInstance().throwExceptionWithStacktraceInMessage(null, IllegalArgumentException.class);
        assertNotNull(ex);
        assertEquals("Invalid exception!", ex.getMessage());
    }


    /**
     * Test throwsException with exception and withStacktrace flag
     */
    @Test
    public void testThrowsExceptionWithFlag() {
        Exception original = new RuntimeException("original");
        IllegalArgumentException withTrace = ExceptionUtil.getInstance().throwsException(original, IllegalArgumentException.class, true);
        assertNotNull(withTrace);
        assertTrue(withTrace.getStackTrace().length > 0);

        IllegalArgumentException withoutTrace = ExceptionUtil.getInstance().throwsException(original, IllegalArgumentException.class, false);
        assertNotNull(withoutTrace);
        assertEquals("original", withoutTrace.getMessage());
    }
}
