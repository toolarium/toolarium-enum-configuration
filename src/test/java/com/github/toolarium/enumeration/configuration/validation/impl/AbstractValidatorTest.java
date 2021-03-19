/*
 * AbstractValidatorTEst.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.EnumValueConfigurationValidatorFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;


/**
 * Base test class
 * 
 * @author patrick
 */
public abstract class AbstractValidatorTest {
    private EnumValueConfigurationDataType enumValueConfigurationDataType;
    
    /**
     * Constructor for AbstractValidatorTest
     * @param enumValueConfigurationDataType the data type
     */
    AbstractValidatorTest(EnumValueConfigurationDataType enumValueConfigurationDataType) {
        this.enumValueConfigurationDataType = enumValueConfigurationDataType;
    }
    
    
    /**
     * Test validation
     */
    public abstract void testValidate();

    
    /**
     * Test convert
     */
    public abstract void testConvert();

    
    /**
     * Check if it is valid
     *
     * @param input the input to check
     */
    protected void isValid(String input) {
        String comment = EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfigurationDataType, input).getComment();
        Assert.assertTrue(comment, EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfigurationDataType, input).isValid());
    }

    
    /**
     * Check if it is invalid
     *
     * @param input the input to check
     */
    protected void isInValid(String input) {
        Assert.assertFalse(EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfigurationDataType, input).isValid());
    }
    

    /**
     * Assert value
     *
     * @param <T> the generic type
     * @param expectedValue the expected value
     * @param input the input
     */
    protected <T> void assertValue(T expectedValue, String input) {
        Assert.assertEquals(expectedValue, EnumValueConfigurationValidatorFactory.getInstance().getValidator().convert(enumValueConfigurationDataType, input));
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
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().convert(enumValueConfigurationDataType, input);
        });
    }


    /**
     * Assert exception
     *
     * @param <T> the generic type
     * @param clazz the expected exception
     * @param exceptionMessage the exception message 
     * @param input the input
     */
    protected <T extends Throwable> void assertException(Class<T> clazz, String exceptionMessage, String input) {
        Throwable exception = Assertions.assertThrows(clazz, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().convert(enumValueConfigurationDataType, input);
        });
        
        Assert.assertEquals(exceptionMessage, exception.getMessage().replace("\r", ""));
    }
}
