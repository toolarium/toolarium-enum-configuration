/*
 * JSONUtilExtendedTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Extended tests for {@link JSONUtil} edge cases.
 *
 * @author patrick
 */
public class JSONUtilExtendedTest {

    /**
     * Test convert with special characters (quotes, backslashes)
     */
    @Test
    public void testConvertSpecialCharacters() {
        List<String> list = Arrays.asList("hello \"world\"", "back\\slash", "new\nline");
        String json = JSONUtil.getInstance().convert(list);
        assertNotNull(json);

        // Convert back and verify
        List<String> result = JSONUtil.getInstance().convert(json);
        assertEquals(list, result);
    }


    /**
     * Test convert with single element list
     */
    @Test
    public void testConvertSingleElement() {
        List<String> list = Arrays.asList("only");
        String json = JSONUtil.getInstance().convert(list);
        assertEquals("[ \"only\" ]", json);
        assertEquals(list, JSONUtil.getInstance().convert(json));
    }


    /**
     * Test convert string that is not a JSON array returns list with the string itself
     */
    @Test
    public void testConvertNonArrayString() {
        // A non-array string is returned as a single-element list
        List<String> result = JSONUtil.getInstance().convert("not a json array");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("not a json array", result.get(0));
    }


    /**
     * Test convert with unicode characters
     */
    @Test
    public void testConvertUnicodeCharacters() {
        List<String> list = Arrays.asList("Zürich", "日本語", "emoji 🎉");
        String json = JSONUtil.getInstance().convert(list);
        assertNotNull(json);
        List<String> result = JSONUtil.getInstance().convert(json);
        assertEquals(list, result);
    }


    /**
     * Test convert null collection
     */
    @Test
    public void testConvertNullCollection() {
        assertThrows(Exception.class, () -> {
            JSONUtil.getInstance().convert((java.util.Collection<String>) null);
        });
    }


    /**
     * Test roundtrip with empty strings
     */
    @Test
    public void testRoundtripEmptyStrings() {
        List<String> list = Arrays.asList("", "a", "");
        String json = JSONUtil.getInstance().convert(list);
        List<String> result = JSONUtil.getInstance().convert(json);
        assertEquals(3, result.size());
        assertEquals("", result.get(0));
        assertEquals("a", result.get(1));
        assertEquals("", result.get(2));
    }


    /**
     * Test that convert(String) with empty JSON array returns empty list
     */
    @Test
    public void testConvertEmptyJsonArray() {
        List<String> result = JSONUtil.getInstance().convert("[]");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
