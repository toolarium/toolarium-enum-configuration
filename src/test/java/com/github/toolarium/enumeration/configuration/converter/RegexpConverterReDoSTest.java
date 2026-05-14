/*
 * RegexpConverterReDoSTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import org.junit.jupiter.api.Test;


/**
 * Test ReDoS protection in regexp converter
 *
 * @author patrick
 */
public class RegexpConverterReDoSTest extends AbstractStringTypeConverterTest {

    /**
     * Constructor for RegexpConverterReDoSTest
     */
    RegexpConverterReDoSTest() {
        super(EnumKeyValueConfigurationDataType.REGEXP);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.converter.AbstractStringTypeConverterTest#testConvert()
     */
    @Override
    @Test
    public void testConvert() {
        // Valid patterns should still work
        assertValue("^.*[a-b]*$", "^.*[a-b]*$");
        assertValue("[0-9]+", "[0-9]+");
        assertValue("\\d{3}-\\d{4}", "\\d{3}-\\d{4}");
    }


    /**
     * Test that nested quantifiers are rejected
     */
    @Test
    public void testNestedQuantifiersRejected() {
        assertException(ValidationException.class, "(a+)+");
        assertException(ValidationException.class, "(a*)*");
        assertException(ValidationException.class, "(a+)*");
        assertException(ValidationException.class, "(a|b+)+");
    }


    /**
     * Test that patterns exceeding max length are rejected
     */
    @Test
    public void testMaxLengthRejected() {
        // Build a pattern that exceeds 2048 chars
        StringBuilder longPattern = new StringBuilder();
        for (int i = 0; i < 2049; i++) {
            longPattern.append("a");
        }
        assertException(ValidationException.class, longPattern.toString());
    }


    /**
     * Test that patterns at exactly max length are accepted
     */
    @Test
    public void testExactMaxLengthAccepted() {
        // Build a pattern that is exactly 2048 chars
        StringBuilder exactPattern = new StringBuilder();
        for (int i = 0; i < 2048; i++) {
            exactPattern.append("a");
        }
        assertValue(exactPattern.toString(), exactPattern.toString());
    }
}
