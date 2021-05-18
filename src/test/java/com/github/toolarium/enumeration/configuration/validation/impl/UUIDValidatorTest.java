/*
 * UUIDValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import java.util.UUID;

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
        super(EnumKeyValueConfigurationDataType.UUID,
              null, /* min value */
              null, /* max value */
              /* valid values */
              new String[] {"f81d4fae-7dec-11d0-a765-00a0c91e6bf6", "  f81d4fae-7dec-11d0-a765-00a0c91a6bf6  ", "" + UUID.randomUUID().toString()},
              /* invalid values */
              new String[] {"f81d4fae-7dec-11d0-00a0c91e6bf6", "fsdsf", "https://www.google.com"},
              new String[] {"f81d4fa"}, /* too small values */
              new String[] {"f81d4fae-7dec-11d0-a765-00a0c91e6bf6-f81d4fae-7dec-11d0-a765-00a0c91e6bf6"} /* too big values */,
              true /* uniqueness */);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#createValueContent(java.lang.String)
     */
    @Override
    protected String createValueContent(String minValueSize) {
        return UUID.randomUUID().toString();
    }
}
