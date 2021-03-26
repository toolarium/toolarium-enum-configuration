/*
 * UUIDValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Test uri validator test
 * 
 * @author patrick
 */
public class UUIDValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for UUIDValidatorTest
     */
    UUIDValidatorTest() {
        super(EnumValueConfigurationDataType.UUID);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        isValid("  f81d4fae-7dec-11d0-a765-00a0c91e6bf6  ");
        isInValid("f81d4fae-7dec-11d0-00a0c91e6bf6");
        isInValid("fsdsf");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        assertValue(UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6"), "f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        assertException("Invalid UUID string: httds://www.sbb.ch", "httds://www.sbb.ch");
    }
}
