/*
 * EnumValueConfigurationValidatorFactoryTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link EnumValueConfigurationValidatorFactory}.
 * @author patrick
 *
 */
public class EnumValueConfigurationValidatorFactoryTest {

    /**
     * Test empty {@link EnumValueConfiguration}.
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testEmptyEnumValueConfiguration() throws ValidationException {
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(null);
        });

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate((EnumValueConfiguration)null, "");
        });

        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setKey("com.github.toolarium.1");
        enumValueConfiguration.setDescription("This is a description 1.");
        enumValueConfiguration.setExampleValue("example");
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "");
        });
    }


    /**
     * Test minimal input.
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testMinimalDefition() throws ValidationException {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setKey("com.github.toolarium.1");
        enumValueConfiguration.setDescription("This is a description 1.");        
        enumValueConfiguration.setExampleValue("2021-03-20");
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        
        // because by default it's not optional and we have to default value
        assertTrue(enumValueConfiguration.isMandatory());
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "");
        });

        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "2021-03-20");
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testMandatoryInputValueDefaultBehaviorWithoutDefaultValue() throws ValidationException {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setKey("com.github.toolarium.2");
        enumValueConfiguration.setDescription("This is a description 2.");
        enumValueConfiguration.setExampleValue("2021-03-21");

        // because by default it's not optional and we have no default value
        assertTrue(enumValueConfiguration.isMandatory());
        assertFalse(enumValueConfiguration.isOptional());

        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "2021-03-21");
        
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "");
        });
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testMandatoryInputValueDefaultBehaviorWithDefaultValue() throws ValidationException {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setDefaultValue("2021-03-22 ");
        enumValueConfiguration.setKey("com.github.toolarium.3");
        enumValueConfiguration.setDescription("This is a description 3.");
        enumValueConfiguration.setExampleValue("2021-03-22");
        
        // because by default it's not optional and we have to default value
        assertFalse(enumValueConfiguration.isMandatory());
        assertFalse(enumValueConfiguration.isOptional());
        
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "2021-03-22");
        
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "");
        });
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testMandatoryInputValueSetOptionalToFalseWithDefaultValue() throws ValidationException {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setDefaultValue("2021-03-23 ");
        enumValueConfiguration.setKey("com.github.toolarium.4");
        enumValueConfiguration.setDescription("This is a description 4.");
        enumValueConfiguration.setExampleValue("2021-03-23");
        enumValueConfiguration.setOptional(false);
        
        assertFalse(enumValueConfiguration.isMandatory());
        assertFalse(enumValueConfiguration.isOptional());
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "2021-03-23");
        
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "");
        });
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testMandatoryInputValueSetOptionalToFalseWithoutDefaultValue() throws ValidationException {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setKey("com.github.toolarium.5");
        enumValueConfiguration.setDescription("This is a description 5.");
        enumValueConfiguration.setExampleValue("2021-03-24");
        enumValueConfiguration.setOptional(false);
        
        assertTrue(enumValueConfiguration.isMandatory());
        assertFalse(enumValueConfiguration.isOptional());
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "2021-03-24");
        
        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "");
        });
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testOptionalInputValueWithDefaultValue() throws ValidationException {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setDefaultValue("2021-03-22 ");
        enumValueConfiguration.setKey("com.github.toolarium.6");
        enumValueConfiguration.setDescription("This is a description 6.");
        enumValueConfiguration.setOptional(true);
        
        assertFalse(enumValueConfiguration.isMandatory());
        assertTrue(enumValueConfiguration.isOptional());
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "2021-03-25");
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "");
    }

    
    /**
     * Test mandatory input value
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testOptionalInputValueWithoutDefaultValue() throws ValidationException {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setKey("com.github.toolarium.7");
        enumValueConfiguration.setDescription("This is a description 7.");
        enumValueConfiguration.setOptional(true);
        
        assertFalse(enumValueConfiguration.isMandatory());
        assertTrue(enumValueConfiguration.isOptional());
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "2021-03-26");
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "");
    }

    
    /**
     * Test invalid input
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testInvalidInputValue() throws ValidationException {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);

        enumValueConfiguration.setDescription("Test");
        enumValueConfiguration.setKey("com.github.toolarium.8");
        enumValueConfiguration.setDescription("This is a description 8.");
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setDefaultValue("2021-03-27");
        enumValueConfiguration.setExampleValue("2021-03-27");
        enumValueConfiguration.setValueSize(new EnumValueConfigurationSizing(null, null));       
        enumValueConfiguration.setCardinality(new EnumValueConfigurationSizing(null, null));
        enumValueConfiguration.setOptional(true);
        enumValueConfiguration.setConfidential(false);

        enumValueConfiguration.setExampleValue("2021-03-27");
        enumValueConfiguration.setDefaultValue("2021-03-27");
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "2021-35-27");
        });
    }


    /**
     * Test valid exampleValue
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testValidExampleValue() throws ValidationException {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setExampleValue("2021-03-28 ");
        enumValueConfiguration.setKey("com.github.toolarium.9");
        enumValueConfiguration.setDescription("This is a description 9.");
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, " 2021-03-28");
    }


    /**
     * Test invalid exampleValue
     */
    @Test 
    public void testInvalidExampleValue() {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setExampleValue("2021-35-29");
        enumValueConfiguration.setKey("com.github.toolarium.10");
        enumValueConfiguration.setDescription("This is a description 10.");

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        });

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "2021-35-29");
        });
    }

    
    /**
     * Test valid defaultValue
     * 
     * @throws ValidationException In case of a validation exception
     */
    @Test 
    public void testValidDefaultValue() throws ValidationException {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setDefaultValue("2021-03-30 ");
        enumValueConfiguration.setKey("com.github.toolarium.11");
        enumValueConfiguration.setDescription("This is a description 11.");
        enumValueConfiguration.setExampleValue("2021-03-30");

        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, " 2021-03-30");
    }


    /**
     * Test invalid defaultValue
     */
    @Test 
    public void testInvalidDefaultValue() {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDataType(EnumValueConfigurationDataType.DATE);
        enumValueConfiguration.setDefaultValue("2021-35-31");
        enumValueConfiguration.setKey("com.github.toolarium 12");
        enumValueConfiguration.setDescription("This is a description 12.");

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration);
        });

        Assertions.assertThrows(ValidationException.class, () -> {
            EnumValueConfigurationValidatorFactory.getInstance().getValidator().validate(enumValueConfiguration, "2021-35-31");
        });
    }
}
