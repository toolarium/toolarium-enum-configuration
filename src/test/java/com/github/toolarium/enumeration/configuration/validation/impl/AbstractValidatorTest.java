/*
 * AbstractValidatorTEst.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.util.AnnotationConvertUtil;
import com.github.toolarium.enumeration.configuration.util.ExceptionUtil;
import com.github.toolarium.enumeration.configuration.validation.EnumValueConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumValueConfigurationValueValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Base test class
 * 
 * @author patrick
 */
public abstract class AbstractValidatorTest {
    private EnumValueConfigurationDataType enumValueConfigurationDataType;
    private String minValueSize;
    private String maxValueSize;
    private String[] validValues; 
    private String[] invalidValues; 
    private String[] tooSmallValues; 
    private String[] tooBigValues; 

    
    /**
     * Constructor for AbstractValidatorTest
     * @param enumValueConfigurationDataType the data type
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     * @param validValues the valid values
     * @param invalidValues the invalid values
     * @param tooSmallValues the too small values
     * @param tooBigValues the too big values
     */
    AbstractValidatorTest(EnumValueConfigurationDataType enumValueConfigurationDataType, 
                          String minValueSize, 
                          String maxValueSize, 
                          String[] validValues,
                          String[] invalidValues,
                          String[] tooSmallValues, 
                          String[] tooBigValues) {
        this.enumValueConfigurationDataType = enumValueConfigurationDataType;
        this.minValueSize = minValueSize;
        this.maxValueSize = maxValueSize;
        this.validValues = validValues;
        this.invalidValues = invalidValues;
        this.tooSmallValues = tooSmallValues;
        this.tooBigValues = tooBigValues;
        
        assertNotNull(validValues);
        assertTrue(validValues.length > 2);
    }

    
    /**
     * Test valid and invalid objects.
     */
    @Test
    public void testValidateWithValidatorMinSize() {
        testValidate(getValidatorMinValueSizeAsString(), maxValueSize, validValues, invalidValues, tooSmallValues, tooBigValues);
    }

    
    /**
     * Test valid and invalid objects.
     */
    @Test
    public void testValidateGivenMinSize() {
        testValidate(minValueSize, maxValueSize, validValues, invalidValues, tooSmallValues, tooBigValues);
    }

    
    /**
     * Test valid and invalid objects.
     * 
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     * @param validValues the valid values
     * @param invalidValues the invalid values
     * @param tooSmallValues the too small values
     * @param tooBigValues the too big values
     */
    public void testValidate(String minValueSize, String maxValueSize, String[] validValues, String[] invalidValues, String[] tooSmallValues, String[] tooBigValues) {
        
        boolean allowEmptyValue = allowEmptyValue(minValueSize, maxValueSize);
        if (allowEmptyValue) {
            // test no value, no cardinality --> min value > than empty string!
            isValid("", null, minValueSize, maxValueSize);
    
            // test no value, no cardinality --> min value > than empty string!
            isValid("", "0..1", minValueSize, maxValueSize);
        } else {
            // test no value, no cardinality --> min value > than empty string!
            isInValid("", null, minValueSize, maxValueSize);
    
            // test no value, no cardinality --> min value > than empty string!
            isInValid("", "0..1", minValueSize, maxValueSize);
        }

        // test valid values, no cardinality
        if (validValues != null) {
            for (String validValue : validValues) {
                isValid(validValue, null, minValueSize, maxValueSize);
            }
        }
        
        // test invalid values, no cardinality
        if (invalidValues != null) {
            for (String invalidValue : invalidValues) {
                isInValid(invalidValue, null, minValueSize, maxValueSize);
            }
        }
        
        // test too small value, no cardinality
        if (tooSmallValues != null) {
            for (String tooSmallValue : tooSmallValues) {
                if (isTooSmallValue(tooSmallValue, minValueSize)) {
                    isInValid(tooSmallValue, null, minValueSize, maxValueSize);
                } else {
                    // in case too small value is equals to the min value
                    isValid(tooSmallValue, null, minValueSize, maxValueSize);
                }
            }
        }

        // test too big value, no cardinality
        if (tooBigValues != null) {
            for (String tooBigValue : tooBigValues) {
                isInValid(tooBigValue, null, minValueSize, maxValueSize);
            }
        }
    }

    
    /**
     * Test object in range.
     */
    @Test
    public void testRangeWithValidatorMinSize() {
        testRange(getValidatorMinValueSizeAsString(), maxValueSize, validValues, invalidValues, tooSmallValues, tooBigValues);
    }

    
    /**
     * Test object in range.
     */
    @Test
    public void testRangeWithGivenMinSize() {
        testRange(minValueSize, maxValueSize, validValues, invalidValues, tooSmallValues, tooBigValues);
    }

    
    /**
     * Test object in range.
     * 
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     * @param validValues the valid values
     * @param invalidValues the invalid values
     * @param tooSmallValues the too small values
     * @param tooBigValues the too big values
     */
    public void testRange(String minValueSize, String maxValueSize, String[] validValues, String[] invalidValues, String[] tooSmallValues, String[] tooBigValues) {
        final String cardinality = null; // test wihtout cardinality!

        boolean allowEmptyValue = allowEmptyValue(minValueSize, maxValueSize);
        if (allowEmptyValue) {
            // test no value, no cardinality
            isValid("", cardinality, minValueSize, maxValueSize);
        } else {
            // test no value, no cardinality
            isInValid("", cardinality, minValueSize, maxValueSize);
        }

        // test min, no cardinality
        isValid(createValueContent(minValueSize), cardinality, minValueSize, maxValueSize);
        
        // test max, no cardinality
        isValid(createValueContent(maxValueSize), cardinality, minValueSize, maxValueSize);

        // test valid value, no cardinality
        for (String validValue : validValues) {
            isValid(validValue, cardinality, minValueSize, maxValueSize);
        }

        // test invalid value, no cardinality
        for (String invalidValue : invalidValues) {
            isInValid(invalidValue, cardinality, minValueSize, maxValueSize);
        }

        // test too small value, no cardinality
        if (tooSmallValues != null) {
            for (String tooSmallValue : tooSmallValues) {
                if (isTooSmallValue(tooSmallValue, minValueSize)) {
                    isInValid(tooSmallValue, cardinality, minValueSize, maxValueSize);
                } else {
                    // in case too small value is equals to the min value
                    isValid(tooSmallValue, cardinality, minValueSize, maxValueSize);
                }
            }
        }

        // test too big value, no cardinality
        if (tooBigValues != null) {
            for (String tooBigValue : tooBigValues) {
                isInValid(tooBigValue, cardinality, minValueSize, maxValueSize);
            }
        }
    }


    /**
     * Test object in range.
     */
    @Test
    public void testCardinalityWithValidatorMinSize() {
        testCardinality(getValidatorMinValueSizeAsString(), maxValueSize, validValues);
    }

    
    /**
     * Test object in range.
     */
    @Test
    public void testCardinalityWithGivenMinSize() {
        testCardinality(minValueSize, maxValueSize, validValues);
    }

    
    /**
     * Test cardinality.
     * 
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     * @param validValues the valid values
     */
    public void testCardinality(String minValueSize, String maxValueSize, String[] validValues) {
        
        //protected void isValid(String input, String inputCardinality, String minValueSize, String maxValueSize, boolean isOptional) {
        
        assertNotNull(validValues);
        assertTrue(validValues.length > 2);
        
        // test no value
        boolean allowEmptyValue = allowEmptyValue(minValueSize, maxValueSize);
        if (allowEmptyValue) {
            isValid("", "0..1", minValueSize, maxValueSize);
            isValid("", "0..0", minValueSize, maxValueSize);
            isValid("", "0", minValueSize, maxValueSize);
        } else {
            isInValid("", "0..1", minValueSize, maxValueSize);
            isInValid("", "0..0", minValueSize, maxValueSize);
            isInValid("", "0", minValueSize, maxValueSize);
        }

        List<String> list = new ArrayList<String>(); 
        for (String validValue : validValues) {
            isValid(validValue, "1", minValueSize, maxValueSize);
            list.add(validValue);
        }
        
        String validValueList = "[\"" + String.join("\", \"", list) + "\" ]";
        //String[] validValueList = list.toArray(String[]::new);

        // test max cardinality
        isValid(validValueList, "" + validValues.length, minValueSize, maxValueSize);
        isValid(validValueList, "*", minValueSize, maxValueSize);
        isValid(validValueList, "0..*", minValueSize, maxValueSize);
        isValid(validValueList, "1..*", minValueSize, maxValueSize);
        isValid(validValueList, "" + (validValues.length - 1) + ".." + validValues.length, minValueSize, maxValueSize);
        isValid(validValueList, "" + (validValues.length - 1) + "..*", minValueSize, maxValueSize);
        isValid(validValueList, "" + validValues.length + ".." + validValues.length, minValueSize, maxValueSize);

        // test too long cardinality elements
        isInValid(validValueList, "" + (validValues.length - 1), minValueSize, maxValueSize);

        // test too short cardinality elements
        isInValid("[" + validValues[0] + "]", "2.." + validValues.length, minValueSize, maxValueSize);
    }

    
    /**
     * Check if the value1 is too small in comparison to value2
     *
     * @param value1 the value 1
     * @param value2 the value 2
     * @return true if it is too small
     */
    protected boolean isTooSmallValue(String value1, String value2) {
        return !value1.equals(value2);        
    }
    
    
    /**
     * Create a value content
     *
     * @param minValueSize the min value size
     * @return the content
     */
    protected String createValueContent(String minValueSize) {
        return minValueSize;
    }


    /**
     * Check if empty vlaue is allowed
     *
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     * @return true if it is allowed
     */
    protected boolean allowEmptyValue(String minValueSize, String maxValueSize) {        
        try {
            EnumValueConfigurationSizing<?> size = EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(enumValueConfigurationDataType, minValueSize, maxValueSize);

            if (getValidatorMinValueSize() == null || size == null || size.getMinSize() == null) {
                return true;
            }
        
            return getValidatorMinValueSize().equals(size.getMinSize());            
        } catch (ValidationException e) {
            fail(ExceptionUtil.getInstance().prepareExceptionWithStacktraceInMessage(e));            
            return false;
        }
    }

    
    /**
     * Get the min value size as string
     *
     * @return the min value size as string
     */
    protected String getValidatorMinValueSizeAsString() {
        return "" + getValidatorMinValueSize();
    }

    
    /**
     * Get the min value size as string
     *
     * @param <T> the generic type
     * @return the min value size
     */
    @SuppressWarnings("unchecked")
    protected <T> T getValidatorMinValueSize() {
        AbstractEnumValueConfigurationValueValidator<?, T> validator = (AbstractEnumValueConfigurationValueValidator<?, T>) EnumValueConfigurationValueValidatorFactory
                .getInstance().createEnumValueConfigurationValueValidator(enumValueConfigurationDataType);
        return validator.getMinSize();
    }

    
    /**
     * Check if it is valid
     *
     * @param input the input to check
     */
    protected void isValid(String input) {
        isValid(input, minValueSize, maxValueSize);
    }

    
    /**
     * Check if it is valid
     *
     * @param input the input to check
     * @param inputCardinality the input cardinality
     */
    protected void isValid(String input, String inputCardinality) {
        isValid(input, inputCardinality, minValueSize, maxValueSize);
    }

    
    /**
     * Check if it is valid
     *
     * @param input the input to check
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     */
    protected void isValid(String input, String minValueSize, String maxValueSize) {
        isValid(input, null, minValueSize, maxValueSize);
    }

    
    /**
     * Check if it is valid
     *
     * @param input the input to check
     * @param inputCardinality the input cardinality
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     */
    protected void isValid(String input, String inputCardinality, String minValueSize, String maxValueSize) {
        try {
            EnumValueConfigurationSizing<Integer> cardinality = AnnotationConvertUtil.getInstance().parseCardinality(inputCardinality);
            EnumValueConfigurationSizing<?> valueSize = EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(enumValueConfigurationDataType, minValueSize, maxValueSize);
            
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfigurationDataType, cardinality, valueSize, input);
        } catch (ValidationException ex) {
            fail(ExceptionUtil.getInstance().prepareExceptionWithStacktraceInMessage(ex));
        }
    }


    /**
     * Check if it is invalid
     *
     * @param input the input to check
     */
    protected void isInValid(String input) {
        isInValid(input, minValueSize, maxValueSize);
    }

    
    /**
     * Check if it is valid
     *
     * @param input the input to check
     * @param inputCardinality the input cardinality
     */
    protected void isInValid(String input, String inputCardinality) {
        isInValid(input, inputCardinality, minValueSize, maxValueSize);
    }

    
    /**
     * Check if it is invalid
     *
     * @param input the input to check
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     */
    protected void isInValid(String input, String minValueSize, String maxValueSize) {
        isInValid(input, null, minValueSize, maxValueSize);
    }

    
    /**
     * Check if it is in valid
     *
     * @param input the input to check
     * @param inputCardinality the input cardinality
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     */
    protected void isInValid(String input, String inputCardinality, String minValueSize, String maxValueSize) {
        try {
            EnumValueConfigurationSizing<Integer> cardinality = AnnotationConvertUtil.getInstance().parseCardinality(inputCardinality);
            EnumValueConfigurationSizing<?> valueSize = EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(enumValueConfigurationDataType, minValueSize, maxValueSize);
            
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfigurationDataType, cardinality, valueSize, input);
            fail("Input [" + input + "], cardinality [" + inputCardinality + "], min: " + minValueSize + ", max: " + maxValueSize);
        } catch (ValidationException ex) {
            // NOP
        }
    }
}
