/*
 * RegexpValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;


/**
 * Test regexp validator test
 * 
 * @author patrick
 */
public class RegexpValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for RegexpValidatorTest
     */
    RegexpValidatorTest() {
        super(EnumKeyValueConfigurationDataType.REGEXP,
              null, /* min value */
              null, /* max value */
              /* valid values */
              new String[] {"^.*[a-b]*$", "  .*[a-b]*  ", "^.*Test$"},
              /* invalid values */
              new String[] {"[b-"},
              null, /* too small values */
              null  /* too big values */);
    }
}
