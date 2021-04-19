/*
 * EnumValueConfigurationValueValidatorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.impl.StringEnumValueConfigurationValueValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class EnumValueConfigurationValueValidatorTest {
    private static final String ONE_STRING = "1";

    /**
     * Test the prepareValueSize
     * @throws ValidationException In case of a validation error 
     */
    @Test
    public void testPrepareValueSize() throws ValidationException {
        
        //EnumValueConfigurationSizing<?> valueSize = EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, null, null);

        //IEnumValueConfigurationValueValidator<?> validator = EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationValueValidator(EnumValueConfigurationDataType.STRING);
        //validator.validateValue(valueSize, null);
        
        new StringEnumValueConfigurationValueValidator().validateValue(null, null);
        assertNull(EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, null, null));
        assertNull(EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, "", ""));
        assertEquals(new EnumValueConfigurationSizing<Long>("0", 0L, ONE_STRING, 1L), EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, "0", ONE_STRING));
        assertEquals(new EnumValueConfigurationSizing<Long>("0", 0L, ONE_STRING, 1L), EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, "", "  1  "));
        assertEquals(new EnumValueConfigurationSizing<Long>("0", 0L, ONE_STRING, 1L), EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, "  0 ", " 1  "));
        assertEquals(new EnumValueConfigurationSizing<Long>(ONE_STRING, 1L, "2", 2L), EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, "  1", " 2  "));
        assertEquals(new EnumValueConfigurationSizing<Long>("0", 0L, EnumValueConfigurationSizing.MAX_CARDINALITY, Long.MAX_VALUE),
                     EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, "", EnumValueConfigurationSizing.MAX_CARDINALITY));
        assertEquals(new EnumValueConfigurationSizing<Long>(ONE_STRING, 1L, EnumValueConfigurationSizing.MAX_CARDINALITY, Long.MAX_VALUE),
                     EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, ONE_STRING, EnumValueConfigurationSizing.MAX_CARDINALITY));
        
        EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, null, "4");
        EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, "*", null);

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(EnumValueConfigurationDataType.STRING, "*", "4");
        });
    }
}
