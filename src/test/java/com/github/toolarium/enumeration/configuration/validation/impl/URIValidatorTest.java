/*
 * URIValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;


import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;


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
        super(EnumValueConfigurationDataType.URI);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testValidate()
     */
    @Override
    @Test
    public void testValidate() {
        isValid("https://www.sbb.ch");
        isValid("  https://www.sbb.ch  ");
        isInValid("httds://www.sbb.ch\files");
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.AbstractValidatorTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        try {
            assertValue(new URI("https://www.sbb.ch"), "https://www.sbb.ch");
        } catch (URISyntaxException e) {
            Assert.fail();
        }
        assertException(IllegalArgumentException.class, "Illegal character in authority at index 8: httds://www.sbb.ch\\d", "httds://www.sbb.ch\\d");
    }
}
