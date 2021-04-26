/*
 * URIConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;


import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;


/**
 * Test uri converter test
 * 
 * @author patrick
 */
public class URIConverterTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for URIValidatorTest
     */
    URIConverterTest() {
        super(EnumKeyValueConfigurationDataType.URI);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        try {
            assertValue(new URI("https://www.sbb.ch"), "https://www.sbb.ch");
        } catch (URISyntaxException e) {
            Assert.fail();
        }
        assertException("Illegal character in authority at index 8: httds://www.sbb.ch\\d", "httds://www.sbb.ch\\d");
    }
}
