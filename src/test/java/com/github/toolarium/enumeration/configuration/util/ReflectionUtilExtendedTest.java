/*
 * ReflectionUtilExtendedTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


/**
 * Extended tests for {@link ReflectionUtil}.
 *
 * @author patrick
 */
public class ReflectionUtilExtendedTest {

    /**
     * Test newInstance with null class
     */
    @Test
    public void testNewInstanceWithNullClass() {
        assertThrows(InstantiationException.class, () -> {
            ReflectionUtil.getInstance().newInstance(null);
        });
    }


    /**
     * Test newInstance with class that has no-arg constructor
     *
     * @throws Exception in case of error
     */
    @Test
    public void testNewInstanceWithValidClass() throws Exception {
        Object instance = ReflectionUtil.getInstance().newInstance(java.util.ArrayList.class);
        assertNotNull(instance);
    }


    /**
     * Test newInstance with class that has no default constructor
     */
    @Test
    public void testNewInstanceWithNoDefaultConstructor() {
        assertThrows(InstantiationException.class, () -> {
            ReflectionUtil.getInstance().newInstance(Integer.class);
        });
    }


    /**
     * Test newInstance with interface (cannot be instantiated)
     */
    @Test
    public void testNewInstanceWithInterface() {
        assertThrows(Exception.class, () -> {
            ReflectionUtil.getInstance().newInstance(Runnable.class);
        });
    }
}
