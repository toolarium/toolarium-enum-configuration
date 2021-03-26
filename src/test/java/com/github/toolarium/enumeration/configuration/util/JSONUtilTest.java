/*
 * JSONUtilTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link JSONUtil}.
 * @author patrick
 *
 */
public class JSONUtilTest {

    /**
     * Test convert json array
     */
    @Test
    public void convertJSONArray() {
        List<String> list = Arrays.asList("my", "simple", "Test");
        
        String jsonArray = JSONUtil.getInstance().convert(list);
        assertEquals("[ \"my\", \"simple\", \"Test\" ]", jsonArray);
        
        assertEquals(list, JSONUtil.getInstance().convert(jsonArray));
    }
}
