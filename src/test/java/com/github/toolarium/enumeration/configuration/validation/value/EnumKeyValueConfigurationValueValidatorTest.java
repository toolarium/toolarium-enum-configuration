/*
 * EnumKeyValueConfigurationValueValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.toolarium.enumeration.configuration.dto.EnumConfigurationComplianceResult;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.impl.StringEnumKeyValueConfigurationValueValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test the string enum key value configuration value validator
 * 
 * @author patrick
 */
public class EnumKeyValueConfigurationValueValidatorTest {
    private static final String ZERO_STRING = "0";
    private static final String ONE_STRING = "1";
    private static final String THREE_STRING = "3";

    /**
     * Test the prepareValueSize
     * 
     * @throws EmptyValueException In case of an empty value
     * @throws ValidationException In case of a validation error 
     */
    @Test
    public void testPrepareValueSize() throws EmptyValueException, ValidationException {
        
        //EnumKeyValueConfigurationSizing<?> valueSize = EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, null, null);

        //IEnumKeyValueConfigurationValueValidator<?> validator = EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationValueValidator(EnumKeyValueConfigurationDataType.STRING);
        //validator.validateValue(valueSize, null);
        
        new StringEnumKeyValueConfigurationValueValidator().validateValue(null, null);
        assertNull(EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, null, null));
        assertNull(EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "", ""));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, ONE_STRING, 1L), 
                     EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, ZERO_STRING, ONE_STRING));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, ONE_STRING, 1L), 
                     EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "", "  1  "));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, ONE_STRING, 1L), 
                     EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "  0 ", " 1  "));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>(ONE_STRING, 1L, "2", 2L), EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "  1", " 2  "));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, EnumKeyValueConfigurationSizing.MAX_CARDINALITY, Long.MAX_VALUE),
                     EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "", EnumKeyValueConfigurationSizing.MAX_CARDINALITY));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>(ONE_STRING, 1L, EnumKeyValueConfigurationSizing.MAX_CARDINALITY, Long.MAX_VALUE),
                     EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, ONE_STRING, EnumKeyValueConfigurationSizing.MAX_CARDINALITY));
        
        EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, null, "4");
        EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "*", null);

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "*", "4");
        });
    }
    

    /**
     * Test compliance of sizing
     *
     * @throws ValidationException In case of a validation error 
     */
    @Test
    public void testComliance() throws ValidationException {
        // 0..1 - 0..1
        assertCompliance(true, new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, ONE_STRING, 1L),
                EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, ZERO_STRING, ONE_STRING));
        // 0..1 - 1..1
        assertCompliance(true, new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, ONE_STRING, 1L),
                EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "1", ONE_STRING));
        // 0..3 - 0..2
        assertCompliance(true, new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, THREE_STRING, 3L),
                EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, ZERO_STRING, "2"));
        // 0..3 - 1..1
        assertCompliance(true, new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, THREE_STRING, 3L),
                EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "1", ONE_STRING));
        // 0..3 - 2..3
        assertCompliance(true, new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, THREE_STRING, 3L),
                EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "2", THREE_STRING));
        // 0..3 - 0..3
        assertCompliance(true, new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, THREE_STRING, 3L),
                EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, ZERO_STRING, THREE_STRING));

        // in-compliant
        
        // 0..3 - 0..4
        assertCompliance(false, new EnumKeyValueConfigurationSizing<Long>(ZERO_STRING, 0L, THREE_STRING, 3L),
                EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, ZERO_STRING, "4"));
        // 1..1 - 0..1
        assertCompliance(false, new EnumKeyValueConfigurationSizing<Long>("1", 1L, ONE_STRING, 1L),
                EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, ZERO_STRING, ONE_STRING));
    }
    

    /**
     * Assert compliance
     *
     * @param value the expected value
     * @param v1 the enum key valze configuration sizing
     * @param v2 the enum key valze configuration sizing
     */
    void assertCompliance(boolean value, EnumKeyValueConfigurationSizing<?> v1, EnumKeyValueConfigurationSizing<?> v2) {
        String str = v1.toStringExpression() + " - " + v2.toStringExpression();
        
        EnumConfigurationComplianceResult result = v1.isCompliant(v2);
        if (result.getReason() == null || result.getReason().isBlank()) {
            assertEquals(value, result.isValid(), str);
        } else {
            assertEquals(value, result.isValid(), str + ":" + result.getReason());
        }
    }
}
