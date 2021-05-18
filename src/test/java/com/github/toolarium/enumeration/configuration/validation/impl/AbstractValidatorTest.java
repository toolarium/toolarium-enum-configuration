/*
 * AbstractValidatorTEst.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.util.AnnotationConvertUtil;
import com.github.toolarium.enumeration.configuration.util.ExceptionUtil;
import com.github.toolarium.enumeration.configuration.validation.EnumKeyConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumKeyValueConfigurationValueValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;


/**
 * Base test class
 * 
 * @author patrick
 */
public abstract class AbstractValidatorTest {
    private EnumKeyValueConfigurationDataType enumKeyValueConfigurationDataType;
    private String minValueSize;
    private String maxValueSize;
    private String[] validValues; 
    private String[] invalidValues; 
    private String[] tooSmallValues; 
    private String[] tooBigValues;
    private boolean runUniqnessTest;

    
    /**
     * Constructor for AbstractValidatorTest
     * @param enumKeyValueConfigurationDataType the data type
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     * @param validValues the valid values
     * @param invalidValues the invalid values
     * @param tooSmallValues the too small values
     * @param tooBigValues the too big values
     * @param runUniqnessTest true to run uniqness test
     */
    AbstractValidatorTest(EnumKeyValueConfigurationDataType enumKeyValueConfigurationDataType, 
                          String minValueSize, 
                          String maxValueSize, 
                          String[] validValues,
                          String[] invalidValues,
                          String[] tooSmallValues, 
                          String[] tooBigValues,
                          boolean runUniqnessTest) {
        this.enumKeyValueConfigurationDataType = enumKeyValueConfigurationDataType;
        this.minValueSize = minValueSize;
        this.maxValueSize = maxValueSize;
        this.validValues = validValues;
        this.invalidValues = invalidValues;
        this.tooSmallValues = tooSmallValues;
        this.tooBigValues = tooBigValues;
        this.runUniqnessTest = runUniqnessTest;
        
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
            isValid("", null, false, minValueSize, maxValueSize, null);
        } else {
            // test no value, no cardinality --> min value > than empty string!
            isInValid("", null, false, minValueSize, maxValueSize, null);
        }

        // test no value --> min value > than empty string!
        isInValid("", "1..1", false, minValueSize, maxValueSize, null);

        // test valid values, no cardinality
        if (validValues != null) {
            for (String validValue : validValues) {
                isValid(validValue, null, false, minValueSize, maxValueSize, null);
            }
        }
        
        // test invalid values, no cardinality
        if (invalidValues != null) {
            for (String invalidValue : invalidValues) {
                isInValid(invalidValue, null, false, minValueSize, maxValueSize, null);
            }
        }
        
        // test too small value, no cardinality
        if (tooSmallValues != null) {
            for (String tooSmallValue : tooSmallValues) {
                if (isTooSmallValue(tooSmallValue, minValueSize)) {
                    isInValid(tooSmallValue, null, false, minValueSize, maxValueSize, null);
                } else {
                    // in case too small value is equals to the min value
                    isValid(tooSmallValue, null, false, minValueSize, maxValueSize, null);
                }
            }
        }

        // test too big value, no cardinality
        if (tooBigValues != null) {
            for (String tooBigValue : tooBigValues) {
                isInValid(tooBigValue, null, false, minValueSize, maxValueSize, null);
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
            isValid("", cardinality, false, minValueSize, maxValueSize, null);
        } else {
            // test no value, no cardinality
            isInValid("", cardinality, false, minValueSize, maxValueSize, null);
        }

        // test min, no cardinality
        isValid(createValueContent(minValueSize), cardinality, false, minValueSize, maxValueSize, null);
        
        // test max, no cardinality
        isValid(createValueContent(maxValueSize), cardinality, false, minValueSize, maxValueSize, null);

        // test valid value, no cardinality
        for (String validValue : validValues) {
            isValid(validValue, cardinality, false, minValueSize, maxValueSize, null);
        }

        // test invalid value, no cardinality
        for (String invalidValue : invalidValues) {
            isInValid(invalidValue, cardinality, false, minValueSize, maxValueSize, null);
        }

        // test too small value, no cardinality
        if (tooSmallValues != null) {
            for (String tooSmallValue : tooSmallValues) {
                if (isTooSmallValue(tooSmallValue, minValueSize)) {
                    isInValid(tooSmallValue, cardinality, false, minValueSize, maxValueSize, null);
                } else {
                    // in case too small value is equals to the min value
                    isValid(tooSmallValue, cardinality, false, minValueSize, maxValueSize, null);
                }
            }
        }

        // test too big value, no cardinality
        if (tooBigValues != null) {
            for (String tooBigValue : tooBigValues) {
                isInValid(tooBigValue, cardinality, false, minValueSize, maxValueSize, null);
            }
        }
    }


    /**
     * Test object in range.
     */
    @Test
    public void testCardinalityWithValidatorMinSize() {
        String[] duplicatedValidValues = Stream.concat(Arrays.stream(validValues), Arrays.stream(validValues)).toArray(String[]::new);
        testCardinality(getValidatorMinValueSizeAsString(), maxValueSize, duplicatedValidValues, false);
        
        if (runUniqnessTest) {
            testCardinality(getValidatorMinValueSizeAsString(), maxValueSize, validValues, true);
        }
    }

    
    /**
     * Test object in range.
     */
    @Test
    public void testCardinalityWithGivenMinSize() {
        String[] duplicatedValidValues = Stream.concat(Arrays.stream(validValues), Arrays.stream(validValues)).toArray(String[]::new);
        testCardinality(minValueSize, maxValueSize, duplicatedValidValues, false);
        
        if (runUniqnessTest) {
            testCardinality(minValueSize, maxValueSize, validValues, true);
        }
    }

    
    /**
     * Test cardinality.
     * 
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     * @param validValues the valid values
     * @param uniqueness true for uniquness
     */
    public void testCardinality(String minValueSize, String maxValueSize, String[] validValues, boolean uniqueness) {
        
        //protected void isValid(String input, String inputCardinality, String minValueSize, String maxValueSize, boolean isOptional) {
        
        assertNotNull(validValues);
        assertTrue(validValues.length > 2);
        
        // check if valid values are unique
        if (uniqueness) {
            assertTrue(areUnique(validValues));
        }
        
        // test no value
        isValid("", "0..1", uniqueness, minValueSize, maxValueSize, null);
        isInValid("", "1..1", uniqueness, minValueSize, maxValueSize, null);
        isValid("", "0..0", uniqueness, minValueSize, maxValueSize, null);
        isValid("", "0", uniqueness, minValueSize, maxValueSize, null);
        isInValid("", "1", uniqueness, minValueSize, maxValueSize, null);

        List<String> list = new ArrayList<String>(); 
        for (String validValue : validValues) {
            isValid(validValue, "1", false, minValueSize, maxValueSize, null);
            list.add(validValue);
        }
        
        String validValueList = "[\"" + String.join("\", \"", list) + "\" ]";
        //String[] validValueList = list.toArray(String[]::new);

        // test max cardinality
        isValid(validValueList, "" + validValues.length, uniqueness, minValueSize, maxValueSize, null);
        isValid(validValueList, "*", uniqueness, minValueSize, maxValueSize, null);
        isValid(validValueList, "0..*", uniqueness, minValueSize, maxValueSize, null);
        isValid(validValueList, "1..*", uniqueness, minValueSize, maxValueSize, null);
        isValid(validValueList, "" + (validValues.length - 1) + ".." + validValues.length, uniqueness, minValueSize, maxValueSize, null);
        isValid(validValueList, "" + (validValues.length - 1) + "..*", uniqueness, minValueSize, maxValueSize, null);
        isValid(validValueList, "" + validValues.length + ".." + validValues.length, uniqueness, minValueSize, maxValueSize, null);

        // test too long cardinality elements
        isInValid(validValueList, "" + (validValues.length - 1), uniqueness, minValueSize, maxValueSize, null);

        // test too short cardinality elements
        isInValid("[" + validValues[0] + "]", "2.." + validValues.length, uniqueness, minValueSize, maxValueSize, null);
    }

    
    /**
     * Test object in range.
     */
    @Test
    public void testEnumeration() {
        
        assertNotNull(validValues);
        assertTrue(validValues.length > 2);
        
        // test no enumeration
        isValid("", "0..1", true, minValueSize, maxValueSize, "");
        isValid("", "0..1", true, minValueSize, maxValueSize, null);
        isValid(null, "0..1", true, minValueSize, maxValueSize, null);

        List<String> list = new ArrayList<String>(); 
        for (String validValue : validValues) {
            list.add(validValue);
        }
        
        isValid(list.get(0), "1", true, minValueSize, maxValueSize, list.get(0));
        isInValid("[ \"" + list.get(0) + "\", \"" + list.get(1) + "\"  ]", "*", true, minValueSize, maxValueSize, list.get(0));
        isValid("[ \"" + list.get(0) + "\" , \"" + list.get(1) + "\"  ]", "*", runUniqnessTest, minValueSize, maxValueSize, "[\"" + list.get(0) + "\", \"" + list.get(1) + "\" ]");

        String validValueList = "[\"" + String.join("\", \"", list) + "\" ]";
        isValid(validValueList, "1..*", runUniqnessTest, minValueSize, maxValueSize, validValueList);
        
        if (runUniqnessTest) {
            isInValid(validValueList, "1..*", runUniqnessTest, minValueSize, maxValueSize, "[\"" + String.join("\", \"", list.subList(0, list.size() - 1)) + "\" ]");
        }
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
            EnumKeyValueConfigurationSizing<?> size = EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(enumKeyValueConfigurationDataType, minValueSize, maxValueSize);

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
        AbstractEnumKeyValueConfigurationValueValidator<?, T> validator = (AbstractEnumKeyValueConfigurationValueValidator<?, T>) EnumKeyValueConfigurationValueValidatorFactory
                .getInstance().createEnumKeyValueConfigurationValueValidator(enumKeyValueConfigurationDataType);
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
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     */
    protected void isValid(String input, String minValueSize, String maxValueSize) {
        isValid(input, null, false, minValueSize, maxValueSize, null);
    }

    
    /**
     * Check if it is valid
     *
     * @param input the input to check
     * @param inputCardinality the input cardinality
     * @param uniqueness True if it is unique; otherwise false, which means that the same value can occur more than once. 
     * @param enumerationValue In case the input has to be inside the enumeration
     */
    protected void isValid(String input, String inputCardinality, boolean uniqueness, String enumerationValue) {
        isValid(input, inputCardinality, uniqueness, minValueSize, maxValueSize, enumerationValue);
    }

    
    /**
     * Check if it is valid
     *
     * @param input the input to check
     * @param inputCardinality the input cardinality
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     * @param uniqueness True if it is unique; otherwise false, which means that the same value can occur more than once. 
     * @param enumerationValue In case the input has to be inside the enumeration
     */
    protected void isValid(String input, String inputCardinality, boolean uniqueness, String minValueSize, String maxValueSize, String enumerationValue) {
        try {
            EnumKeyValueConfigurationSizing<Integer> cardinality = AnnotationConvertUtil.getInstance().parseCardinality(inputCardinality);
            EnumKeyValueConfigurationSizing<?> valueSize = EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(enumKeyValueConfigurationDataType, minValueSize, maxValueSize);
            
            EnumKeyConfigurationValidatorFactory.getInstance().getValidator().validate(enumKeyValueConfigurationDataType, cardinality, uniqueness, valueSize, enumerationValue, input);
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
     * @param uniqueness True if it is unique; otherwise false, which means that the same value can occur more than once. 
     * @param enumerationValue In case the input has to be inside the enumeration
     */
    protected void isInValid(String input, String inputCardinality, boolean uniqueness, String enumerationValue) {
        isInValid(input, inputCardinality, uniqueness, minValueSize, maxValueSize, enumerationValue);
    }

    
    /**
     * Check if it is invalid
     *
     * @param input the input to check
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     */
    protected void isInValid(String input, String minValueSize, String maxValueSize) {
        isInValid(input, null, true, minValueSize, maxValueSize, null);
    }

    
    /**
     * Check if it is in valid
     *
     * @param input the input to check
     * @param inputCardinality the input cardinality
     * @param uniqueness True if it is unique; otherwise false, which means that the same value can occur more than once. 
     * @param minValueSize the min value size
     * @param maxValueSize the max value size
     * @param enumerationValue In case the input has to be inside the enumeration
     */
    protected void isInValid(String input, String inputCardinality, boolean uniqueness, String minValueSize, String maxValueSize, String enumerationValue) {
        try {
            EnumKeyValueConfigurationSizing<Integer> cardinality = AnnotationConvertUtil.getInstance().parseCardinality(inputCardinality);
            EnumKeyValueConfigurationSizing<?> valueSize = EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(enumKeyValueConfigurationDataType, minValueSize, maxValueSize);
            
            EnumKeyConfigurationValidatorFactory.getInstance().getValidator().validate(enumKeyValueConfigurationDataType, cardinality, uniqueness, valueSize, enumerationValue, input);
            fail("Input [" + input + "], cardinality [" + inputCardinality + "], min: " + minValueSize + ", max: " + maxValueSize);
        } catch (ValidationException ex) {
            // NOP
        }
    }


    /**
     * Are unique array
     *
     * @param <T> the generic type
     * @param array the array
     * @return true if there are duplicates
     */
    protected static <T> boolean areUnique(T[] array) {
        final Stream<T> stream = Arrays.asList(array).stream();
        return stream.allMatch(new HashSet<>()::add);
    }


    /**
     * Are unique string array
     *
     * @param array the array
     * @return true if there are duplicates
     */
    protected static boolean areUnique(String[] array) {
        final Stream<String> stream = Arrays.asList(array).stream();
        return stream.map(String::trim).allMatch(new HashSet<>()::add);
    }
}

