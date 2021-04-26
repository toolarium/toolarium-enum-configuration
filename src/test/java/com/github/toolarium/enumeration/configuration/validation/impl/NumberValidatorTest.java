/*
 * NumberValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import org.junit.jupiter.api.Test;


/**
 * Test number validator test
 * 
 * @author patrick
 */
public class NumberValidatorTest extends AbstractValidatorTest {
    private static final String ONE_STRING = "1";


    /**
     * Constructor for NumberValidatorTest
     */
    NumberValidatorTest() {
        super(EnumKeyValueConfigurationDataType.NUMBER, 
              ONE_STRING, 
              "123",
              /* valid values */
              new String[] {"2", "3", "24", "122", "12"},
              /* invalid values */
              new String[] {"123'45678901234", "1-B-2"},
              /* too small value */
              new String[] {"0", "-1", "-12123232"},        
              /* too big value */
              new String[] {"124", "125", "321321321321"});
    }
    
    
    /**
     * Test valid and invalid objects: this is redundant with generic test class. We still keep this test to be sure the test framework works correctly.
     */
    @Test
    public void testValidateStartByZero() {
        testValidate("0", 
                     "123",
                     /* valid values */
                     new String[] {"2", "3", "24", "122", "12"},
                     /* invalid values */
                     new String[] {"123'45678901234", "1-B-2"},
                     /* too small value */
                     new String[] {"-1", "-12123232"},        
                     /* too big value */
                     new String[] {"124", "125", "321321321321"});
    }

    
    /**
     * Test object in range: this is redundant with generic test class. We still keep this test to be sure the test framework works correctly.
     */
    @Test
    public void testRangeStartByZero() {
        testRange("0", 
                  "123",
                  /* valid values */
                  new String[] {"2", "3", "24", "122", "12"},
                  /* invalid values */
                  new String[] {"123'45678901234", "1-B-2"},
                  /* too small value */
                  new String[] {"-1", "-12123232"},        
                  /* too big value */
                  new String[] {"124", "125", "321321321321"});
    }

    
    /**
     * Test object cardinality: : this is redundant with generic test class. We still keep this test to be sure the test framework works correctly.
     */
    @Test
    public void testCardinalityStartByZero() {
        testCardinality("0", 
                        "123", 
                        new String[] {"0", "2", "3", "24", "122", "12"});
    }
    
    
    /**
     * Test cardinality ranges.
     */
    @Test
    public void testCardinalityRanges() {
        // the min and max values are taken by the constructor

        isValid(ONE_STRING, ONE_STRING);
        isValid(ONE_STRING, "1..1");
        isValid(ONE_STRING, "0..1");
        isValid("[1 ,2]", "2");
        isValid("[1]", "*");
        isValid("[1 ,2, 3, 4]", "*");

        isValid("[1]", "1..3");
        isValid("[1,2]", "1..3");
        isValid("[1,2,3]", "1..3");
        
        isInValid("[1,2,3,4]", "1..3");
        isInValid("[1 ,2]", ONE_STRING);
    }
}
