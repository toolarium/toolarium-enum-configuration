/*
 * ExceptionUtilTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test the {@link ExceptionUtil}.
 * 
 * @author patrick
 */
public class ExceptionUtilTest {

    /**
     * Test
     */
    @Test
    public void test() {

        assertEquals("null\n" + "java.lang.IllegalAccessException:\n" + "  at com.github.toolarium.enumeration.configuration.util.ExceptionUtilTest.test(ExceptionUtilTest.java:26)", 
                     ExceptionUtil.getInstance().prepareExceptionWithStacktraceInMessage(new IllegalAccessException()));
    }
}
