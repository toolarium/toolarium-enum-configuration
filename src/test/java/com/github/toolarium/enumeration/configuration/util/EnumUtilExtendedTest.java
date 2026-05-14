/*
 * EnumUtilExtendedTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Extended tests for {@link EnumUtil}.
 *
 * @author patrick
 */
public class EnumUtilExtendedTest {

    /**
     * Test valueOf with exact case match
     */
    @Test
    public void testValueOfExactCase() {
        assertEquals(EnumKeyValueConfigurationDataType.STRING,
                     EnumUtil.getInstance().valueOf(EnumKeyValueConfigurationDataType.class, "STRING"));
    }


    /**
     * Test valueOf with case insensitive match
     */
    @Test
    public void testValueOfCaseInsensitive() {
        assertEquals(EnumKeyValueConfigurationDataType.STRING,
                     EnumUtil.getInstance().valueOf(EnumKeyValueConfigurationDataType.class, "string"));
        assertEquals(EnumKeyValueConfigurationDataType.NUMBER,
                     EnumUtil.getInstance().valueOf(EnumKeyValueConfigurationDataType.class, "Number"));
    }


    /**
     * Test valueOf with null returns null
     */
    @Test
    public void testValueOfNull() {
        assertNull(EnumUtil.getInstance().valueOf(EnumKeyValueConfigurationDataType.class, null));
    }


    /**
     * Test valueOf with empty string returns null
     */
    @Test
    public void testValueOfEmpty() {
        assertNull(EnumUtil.getInstance().valueOf(EnumKeyValueConfigurationDataType.class, ""));
        assertNull(EnumUtil.getInstance().valueOf(EnumKeyValueConfigurationDataType.class, "   "));
    }


    /**
     * Test valueOf with nonexistent name returns null
     */
    @Test
    public void testValueOfNonexistent() {
        assertNull(EnumUtil.getInstance().valueOf(EnumKeyValueConfigurationDataType.class, "NONEXISTENT"));
    }


    /**
     * Test mapEnum with null returns null
     */
    @Test
    public void testMapEnumNull() {
        assertNull(EnumUtil.getInstance().mapEnum(EnumKeyValueConfigurationDataType.class, null));
    }


    /**
     * Test mapEnum with matching enum
     */
    @Test
    public void testMapEnumValid() {
        assertEquals(EnumKeyValueConfigurationDataType.STRING,
                     EnumUtil.getInstance().mapEnum(EnumKeyValueConfigurationDataType.class, EnumKeyValueConfigurationDataType.STRING));
    }


    /**
     * Test valueOf with whitespace around name
     */
    @Test
    public void testValueOfWithWhitespace() {
        assertEquals(EnumKeyValueConfigurationDataType.BOOLEAN,
                     EnumUtil.getInstance().valueOf(EnumKeyValueConfigurationDataType.class, "  BOOLEAN  "));
    }
}
