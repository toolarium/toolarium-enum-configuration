/*
 * EnumKeyValueConfigurationValueValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.impl.StringEnumKeyValueConfigurationValueValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class EnumKeyValueConfigurationValueValidatorTest {
    private static final String ONE_STRING = "1";

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
        assertEquals(new EnumKeyValueConfigurationSizing<Long>("0", 0L, ONE_STRING, 1L), EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "0", ONE_STRING));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>("0", 0L, ONE_STRING, 1L), EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "", "  1  "));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>("0", 0L, ONE_STRING, 1L), EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "  0 ", " 1  "));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>(ONE_STRING, 1L, "2", 2L), EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "  1", " 2  "));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>("0", 0L, EnumKeyValueConfigurationSizing.MAX_CARDINALITY, Long.MAX_VALUE),
                     EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "", EnumKeyValueConfigurationSizing.MAX_CARDINALITY));
        assertEquals(new EnumKeyValueConfigurationSizing<Long>(ONE_STRING, 1L, EnumKeyValueConfigurationSizing.MAX_CARDINALITY, Long.MAX_VALUE),
                     EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, ONE_STRING, EnumKeyValueConfigurationSizing.MAX_CARDINALITY));
        
        EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, null, "4");
        EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "*", null);

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType.STRING, "*", "4");
        });
    }
}
