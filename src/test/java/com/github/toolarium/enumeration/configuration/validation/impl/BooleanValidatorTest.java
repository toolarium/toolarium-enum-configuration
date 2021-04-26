/*
 * BooleanValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;


/**
 * Boolean validator test
 * 
 * @author patrick
 */
public class BooleanValidatorTest extends AbstractValidatorTest {

    /**
     * Constructor for BooleanValidatorTest
     */
    BooleanValidatorTest() {
        super(EnumKeyValueConfigurationDataType.BOOLEAN, 
              null, 
              null,
              /* valid values */
              new String[] {"true", "yes", "   true   ", "   yes   ", "True", "Yes", "  True  ", "  Yes  ", "false", "no", "  false  ", "  no  ", "False", "No", "  False  ", " No ", "0", "1", "  0  ", "  1  "},
              /* invalid values */
              new String[] {"trus", "ja", "nein", "2"},
              /* too small value */
              null,        
              /* too big value */
              null);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#createValueContent(java.lang.String)
     */
    @Override
    protected String createValueContent(String minValueSize) {
        return "true";
    }
}
