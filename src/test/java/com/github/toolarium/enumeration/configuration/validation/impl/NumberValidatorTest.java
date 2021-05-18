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
    private static final String ZERO_STRING = "0";
    private static final String ONE_STRING = "1";


    /**
     * Constructor for NumberValidatorTest
     */
    NumberValidatorTest() {
        super(EnumKeyValueConfigurationDataType.NUMBER, 
              ONE_STRING, 
              "123",
              /* valid values */
              new String[] {"2 ", "3", "24", "122", "12"},
              /* invalid values */
              new String[] {"123'45678901234", "1-B-2"},
              /* too small value */
              new String[] {ZERO_STRING, "-1", "-12123232"},
              /* too big value */
              new String[] {"124", "125", "321321321321"},
              true /* uniqueness */);
    }
    
    
    /**
     * Test valid and invalid objects: this is redundant with generic test class. We still keep this test to be sure the test framework works correctly.
     */
    @Test
    public void testValidateStartByZero() {
        testValidate(ZERO_STRING, 
                     "123",
                     /* valid values */
                     new String[] {"2 ", "3", "24", "122", "12"},
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
        testRange(ZERO_STRING, 
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
        testCardinality(ZERO_STRING, "123", new String[] {ZERO_STRING, "2", "3", "24", "122", "12"}, false);
        testCardinality(ZERO_STRING, "123", new String[] {ZERO_STRING, "2", "3", "24", "122", "12"}, true);
    }
    
    
    /**
     * Test cardinality ranges.
     */
    @Test
    public void testCardinalityRanges() {
        // the min and max values are taken by the constructor

        isValid(ONE_STRING, ONE_STRING, false, null);
        isValid(ONE_STRING, "1..1", false, null);
        isValid(ONE_STRING, "0..1", false, null);
        isValid("[1 ,2]", "2", false, null);
        isValid("[1]", "*", false, null);
        isValid("[1 ,2, 3, 4]", "*", false, null);

        isValid("[1]", "1..3 ", false, null);
        isValid("[1,2]", "1..3 ", false, null);
        isValid("[1,2,3]", "1..3 ", false, null);
        isValid("[1,2,2]", "1..3 ", false, null);
        
        isInValid("[1,2,3,4]", "1..3 ", false, null);
        isInValid("[1 ,2]", ONE_STRING, false, null);
    }


    /**
     * Test cardinality ranges.
     */
    @Test
    public void testUniqueCardinalityRanges() {
        // the min and max values are taken by the constructor

        isValid(ONE_STRING, ONE_STRING, true, null);
        isValid(ONE_STRING, "1..1", true, null);
        isValid(ONE_STRING, "0..1", true, null);
        isValid("[1 ,2]", "2", true, null);
        isValid("[1]", "*", true, null);
        isValid("[1 ,2, 3, 4]", "*", true, null);

        isValid("[1]", "1..3", true, null);
        isValid("[1,2]", "1..3", true, null);
        isValid("[1,2,3]", "1..3", true, null);
        isInValid("[1,2,2]", "1..3", true, null);
        
        isInValid("[1,2,3,4]", "1..3", true, null);
        isInValid("[1 ,2]", ONE_STRING, true, null);
    }


    /**
     * Test cardinality ranges.
     */
    /*
    @Test
    public void testEnumerationCardinalityRanges() {
        // the min and max values are taken by the constructor

        isValid(ONE_STRING, ONE_STRING, true, ONE_STRING);
        isValid(ONE_STRING, "1..1", true, ONE_STRING);
        isValid(ONE_STRING, "0..1", true, ONE_STRING);
        isValid("[1 ,2]", "2", true, "[1, 2]");
        isInValid("[1 ,2]", "2", true, "[1]");
        isInValid("[1 ,2]", "2", true, "[4, 4]");
        isValid("[1 ,2]", "2", false, "[4, 4]");
        isValid("[1]", "*", true, "[1,2,3,4,5,6]");
        isValid("[1]", "*", false, "[1,2,3,5,6]");
        isValid("[1 ,2, 3, 4]", "*", true, null);

        isValid("[1]", "1..3", true, null);
        isValid("[1,2]", "1..3", true, null);
        isValid("[1,2,3]", "1..3", true, null);
        
        isInValid("[1,2,3,4]", "1..3", true, "[3,4]");
        
        // test uniq
        isValid("[1,2,3]", "1..3", true, "[3,4]");
        isInValid("[1,2,3]", "1..3", true, "[3,3]");
        
        isInValid("[1 ,2]", ONE_STRING, true, "2");
    }
    */
}
