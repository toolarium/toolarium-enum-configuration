/*
 * EnumConfigurationResourceFactoryPathTraversalTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.resource;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test path traversal protection in EnumConfigurationResourceFactory
 *
 * @author patrick
 */
public class EnumConfigurationResourceFactoryPathTraversalTest {

    /**
     * Test that path traversal attempts are rejected
     */
    @Test
    public void testPathTraversalRejected() {
        Assertions.assertThrows(IOException.class, () -> {
            EnumConfigurationResourceFactory.getInstance().load("/tmp/safe", "../../etc/passwd");
        });
    }


    /**
     * Test that path traversal with backslashes is rejected
     */
    @Test
    public void testPathTraversalBackslashRejected() {
        Assertions.assertThrows(IOException.class, () -> {
            EnumConfigurationResourceFactory.getInstance().load("/tmp/safe", "..\\..\\etc\\passwd");
        });
    }


    /**
     * Test that normal filenames within the base path are not rejected
     */
    @Test
    public void testNormalPathNotRejected() {
        // This will throw FileNotFoundException (which is IOException) because the file doesn't exist,
        // but it should NOT throw "path traversal detected"
        IOException ex = Assertions.assertThrows(IOException.class, () -> {
            EnumConfigurationResourceFactory.getInstance().load("/tmp/nonexistent", "config.json");
        });
        Assertions.assertFalse(ex.getMessage().contains("path traversal"), "Normal path should not trigger path traversal detection");
    }
}
