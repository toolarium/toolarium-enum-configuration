/*
 * UUIDConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Test uri converter test
 * 
 * @author patrick
 */
public class UUIDConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for UUIDValidatorTest
     */
    UUIDConverterTest() {
        super(EnumValueConfigurationDataType.UUID);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6"), "f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        assertException("Invalid UUID string: httds://www.sbb.ch", "httds://www.sbb.ch");
    }
}
