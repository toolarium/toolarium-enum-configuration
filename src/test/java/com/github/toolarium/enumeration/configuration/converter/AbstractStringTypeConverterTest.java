/*
 * StringTypeConverterTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import static org.junit.Assert.fail;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;


/**
 * Base converter test class
 * 
 * @author patrick
 */
public abstract class AbstractStringTypeConverterTest {
    private EnumKeyValueConfigurationDataType enumKeyValueConfigurationDataType;
    
    
    /**
     * Constructor for AbstractValidatorTest
     * @param enumKeyValueConfigurationDataType the data type
     */
    AbstractStringTypeConverterTest(EnumKeyValueConfigurationDataType enumKeyValueConfigurationDataType) {
        this.enumKeyValueConfigurationDataType = enumKeyValueConfigurationDataType;
    }

    
    /**
     * Test convert
     */
    public abstract void testConvert();

    
    /**
     * Assert value
     *
     * @param <T> the generic type
     * @param expectedValue the expected value
     * @param input the input
     */
    protected <T> void assertValue(T expectedValue, String input)  {
        try {
            Assert.assertEquals(expectedValue, StringTypeConverterFactory.getInstance().getStringTypeConverter().convert(enumKeyValueConfigurationDataType, input));
        } catch (ValidationException ve) {
            fail("[" + expectedValue + "] != [" + input + "]" + ve.getMessage());
        }
    }
    
    
    /**
     * Assert exception
     *
     * @param <T> the generic type
     * @param clazz the expected exception
     * @param input the input
     */
    protected <T extends Throwable> void assertException(Class<T> clazz, String input) {
        Assertions.assertThrows(clazz, () -> {
            StringTypeConverterFactory.getInstance().getStringTypeConverter().convert(enumKeyValueConfigurationDataType, input);
        });
    }


    /**
     * Assert exception
     *
     * @param <T> the generic type
     * @param exceptionMessage the exception message 
     * @param input the input
     */
    protected <T extends Throwable> void assertException(String exceptionMessage, String input) {
        Throwable exception = Assertions.assertThrows(ValidationException.class, () -> {
            StringTypeConverterFactory.getInstance().getStringTypeConverter().convert(enumKeyValueConfigurationDataType, input);
        });
        
        Assert.assertEquals(exceptionMessage, exception.getMessage().replace("\r", ""));
    }
}
