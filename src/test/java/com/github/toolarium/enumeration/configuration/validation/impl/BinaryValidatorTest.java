/*
 * BinaryValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;

/**
 * Test binary validator test
 * 
 * @author patrick
 */
public class BinaryValidatorTest extends AbstractValidatorTest {
    private static final String MESSAGE = "  my file content  "; 
    private static final String MESSAGE_ENCODED = "ICBteSBmaWxlIGNvbnRlbnQgIA=="; 
    

    /**
     * Constructor for FileValidatorTest
     */
    BinaryValidatorTest() {
        super(EnumKeyValueConfigurationDataType.BINARY, 
              null, 
              "20",
              /* valid values */
              new String[] {MESSAGE_ENCODED, 
                            "  ICBteSBmaWxlIGNvbnRlbnQwICA= ", 
                            "filename1.txt|    ICBteSBmaWxlIGNvbnRlbnQxICA= ", 
                            "filename2.txt|ICBteSBmaWxlIGNvbnRlbnQyICA=", 
                            "filename3.txt|2021-03-15T08:59:22.123Z|ICBteSBmaWxlIGNvbnRlbnQzICA=",
                            "filename4.txt|2021-03-15T08:59:22.123Z|plain/text|ICBteSBmaWxlIGNvbnRlbnQ0ICA=",
                            "|2021-03-15T08:59:22.123Z|plain/text|ICBteSBmaWxlIGNvbnRlbnQ1ICA="},
              /* invalid values */
              new String[] {"filename.txt|2021-03-15T08:59:22.123Z|plain/text|" + MESSAGE, "filename.txt|2021-33-15T08:59:22.123Z|plain/text|" + MESSAGE_ENCODED},
              /* too small value */
              null,        
              /* too big value */
              null,
              true /* uniqueness */);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#createValueContent(java.lang.String)
     */
    @Override
    protected String createValueContent(String minValueSize) {
        return "";
    }
}
