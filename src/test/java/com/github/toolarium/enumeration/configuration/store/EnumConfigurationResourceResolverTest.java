/*
 * EnumConfigurationResourceResolverTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.github.toolarium.enumeration.configuration.store.impl.EnumConfigurationResourceResolver;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link EnumConfigurationResourceResolver}.
 *
 * @author patrick
 */
public class EnumConfigurationResourceResolverTest {

    /**
     * Test that stream is returned
     */
    @Test
    public void testGetStream() {
        InputStream stream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        EnumConfigurationResourceResolver resolver = new EnumConfigurationResourceResolver(stream);
        InputStream result = resolver.getEnumConfigurationResourceStream("test", false);
        assertNotNull(result);
        assertSame(stream, result);
    }


    /**
     * Test with null stream
     */
    @Test
    public void testNullStream() {
        EnumConfigurationResourceResolver resolver = new EnumConfigurationResourceResolver(null);
        assertNull(resolver.getEnumConfigurationResourceStream("test", false));
    }


    /**
     * Test that stream is returned regardless of configuration name
     */
    @Test
    public void testStreamReturnedForAnyName() {
        InputStream stream = new ByteArrayInputStream("data".getBytes(StandardCharsets.UTF_8));
        stream.mark(1024);
        EnumConfigurationResourceResolver resolver = new EnumConfigurationResourceResolver(stream);
        assertNotNull(resolver.getEnumConfigurationResourceStream("name1", true));
        assertNotNull(resolver.getEnumConfigurationResourceStream("name2", false));
        assertNotNull(resolver.getEnumConfigurationResourceStream(null, false));
    }
}
