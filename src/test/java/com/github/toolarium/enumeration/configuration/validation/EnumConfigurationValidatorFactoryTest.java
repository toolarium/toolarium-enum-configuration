/*
 * EnumKeyConfigurationValidatorFactoryTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.util.AnnotationConvertUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link EnumConfigurationValidatorFactory}.
 * @author patrick
 *
 */
public class EnumConfigurationValidatorFactoryTest {

    /**
     * Test empty {@link EnumKeyValueConfiguration}.
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testEmptyEnumKeyValueConfiguration() throws ValidationException {
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(null);
        });

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate((EnumKeyValueConfiguration)null, "");
        });

        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setKey("com.github.toolarium.1");
        enumKeyValueConfiguration.setDescription("This is a description 1.");
        enumKeyValueConfiguration.setExampleValue("example");
        
        assertFalse(enumKeyValueConfiguration.hasDefaultValue());
        assertTrue(enumKeyValueConfiguration.isMandatory());
        
        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "");
        });
    }


    /**
     * Test minimal input.
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testMinimalDefition() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setKey("com.github.toolarium.1");
        enumKeyValueConfiguration.setDescription("This is a description 1.");        
        enumKeyValueConfiguration.setExampleValue("2021-03-20");
        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        
        assertFalse(enumKeyValueConfiguration.hasDefaultValue());
        assertTrue(enumKeyValueConfiguration.isMandatory());

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "");
        });

        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "2021-03-20");
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testMandatoryInputValueDefaultBehaviorWithoutDefaultValue() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setKey("com.github.toolarium.2");
        enumKeyValueConfiguration.setDescription("This is a description 2.");
        enumKeyValueConfiguration.setExampleValue("2021-03-21");

        assertFalse(enumKeyValueConfiguration.hasDefaultValue());
        assertTrue(enumKeyValueConfiguration.isMandatory());

        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "2021-03-21");
        
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "");
        });
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testMandatoryInputValueDefaultBehaviorWithDefaultValue() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setDefaultValue("2021-03-22 ");
        enumKeyValueConfiguration.setKey("com.github.toolarium.3");
        enumKeyValueConfiguration.setDescription("This is a description 3.");
        enumKeyValueConfiguration.setExampleValue("2021-03-22");
        
        assertTrue(enumKeyValueConfiguration.hasDefaultValue());
        assertTrue(enumKeyValueConfiguration.isMandatory());
        
        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "2021-03-22");
        
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "");
        });
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testMandatoryInputValueWithDefaultValue() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setDefaultValue("2021-03-23 ");
        enumKeyValueConfiguration.setKey("com.github.toolarium.4");
        enumKeyValueConfiguration.setDescription("This is a description 4.");
        enumKeyValueConfiguration.setExampleValue("2021-03-23");
        enumKeyValueConfiguration.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));

        assertTrue(enumKeyValueConfiguration.hasDefaultValue());
        assertFalse(enumKeyValueConfiguration.isMandatory());

        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "2021-03-23");
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "");
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testMandatoryInputValueWithoutDefaultValue() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setKey("com.github.toolarium.5");
        enumKeyValueConfiguration.setDescription("This is a description 5.");
        enumKeyValueConfiguration.setExampleValue("2021-03-24");
        enumKeyValueConfiguration.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        
        assertFalse(enumKeyValueConfiguration.hasDefaultValue());
        assertFalse(enumKeyValueConfiguration.isMandatory());
        
        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "2021-03-24");
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "");
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testOptionalInputValueWithDefaultValue() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setDefaultValue("2021-03-22 ");
        enumKeyValueConfiguration.setKey("com.github.toolarium.6");
        enumKeyValueConfiguration.setDescription("This is a description 6.");
        enumKeyValueConfiguration.setExampleValue(" 2001-09-25 ");
        enumKeyValueConfiguration.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        
        assertTrue(enumKeyValueConfiguration.hasDefaultValue());
        assertFalse(enumKeyValueConfiguration.isMandatory());
        
        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "2021-03-25");
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "");
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testOptionalInputValueWithoutDefaultValue() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setKey("com.github.toolarium.7");
        enumKeyValueConfiguration.setDescription("This is a description 7.");
        enumKeyValueConfiguration.setExampleValue(" 2001-09-25 ");
        enumKeyValueConfiguration.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        
        assertFalse(enumKeyValueConfiguration.hasDefaultValue());
        assertFalse(enumKeyValueConfiguration.isMandatory());
        
        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "2021-03-26");
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "");
    }

    
    /**
     * Test invalid input
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testInvalidInputValue() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);

        enumKeyValueConfiguration.setDescription("Test");
        enumKeyValueConfiguration.setKey("com.github.toolarium.8");
        enumKeyValueConfiguration.setDescription("This is a description 8.");
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setDefaultValue("2021-03-27");
        enumKeyValueConfiguration.setExampleValue("2021-03-27");
        enumKeyValueConfiguration.setConfidential(false);

        enumKeyValueConfiguration.setExampleValue("2021-03-27");
        enumKeyValueConfiguration.setDefaultValue("2021-03-27");
        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "2021-35-27");
        });
    }


    /**
     * Test valid exampleValue
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testValidExampleValue() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setExampleValue("2021-03-28 ");
        enumKeyValueConfiguration.setKey("com.github.toolarium.9");
        enumKeyValueConfiguration.setDescription("This is a description 9.");
        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, " 2021-03-28");
    }


    /**
     * Test invalid exampleValue
     */
    @Test 
    public void testInvalidExampleValue() {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setExampleValue("2021-35-29");
        enumKeyValueConfiguration.setKey("com.github.toolarium.10");
        enumKeyValueConfiguration.setDescription("This is a description 10.");

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        });

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "2021-35-29");
        });
    }

    
    /**
     * Test valid defaultValue
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testValidDefaultValue() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setDefaultValue("2021-03-30 ");
        enumKeyValueConfiguration.setKey("com.github.toolarium.11");
        enumKeyValueConfiguration.setDescription("This is a description 11.");
        enumKeyValueConfiguration.setExampleValue("2021-03-30");

        EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, " 2021-03-30");
    }


    /**
     * Test invalid defaultValue
     */
    @Test 
    public void testInvalidDefaultValue() {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration.setDefaultValue("2021-35-31");
        enumKeyValueConfiguration.setKey("com.github.toolarium 12");
        enumKeyValueConfiguration.setDescription("This is a description 12.");

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);
        });

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, "2021-35-31");
        });
    }
}
