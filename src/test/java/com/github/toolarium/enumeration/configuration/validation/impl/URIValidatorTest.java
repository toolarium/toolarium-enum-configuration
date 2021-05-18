/*
 * URIValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;


import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;


/**
 * Test uri validator test
 * 
 * @author patrick
 */
public class URIValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for URIValidatorTest
     */
    URIValidatorTest() {
        super(EnumKeyValueConfigurationDataType.URI,
              null, /* min value */
              null, /* max value */
              /* valid values */
              new String[] {"https://www.sbb.ch", "  https://www.github.com   ", "https://www.google.com"},
              /* invalid values */
              new String[] {"httds://www.sbb.ch\\files"},
              null, /* too small values */
              null  /* too big values */,
              true /* uniqueness */);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#createValueContent(java.lang.String)
     */
    @Override
    protected String createValueContent(String minValueSize) {
        return "a:b";
    }
}
