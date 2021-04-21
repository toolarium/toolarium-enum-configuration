/*
 * AbstractEnumValueConfigurationValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.util.ExceptionUtil;
import com.github.toolarium.enumeration.configuration.util.JSONUtil;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.IEnumValueConfigurationValidator;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumValueConfigurationValueValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;
import java.time.Instant;
import java.util.List;


/**
 * Abstract validator base class
 * 
 * @author patrick
 */
public abstract class AbstractEnumValueConfigurationValidator implements IEnumValueConfigurationValidator  {

    
    /**
     * Constructor for AbstractEnumValueConfigurationValidator
     */
    protected AbstractEnumValueConfigurationValidator() {
    }
    

    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumValueConfigurationValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration, java.lang.String)
     */
    @Override
    public void validate(EnumValueConfiguration enumValueConfiguration, String input) throws ValidationException {
        
        validate(enumValueConfiguration);
        
        validate(enumValueConfiguration.getDataType(), 
                 enumValueConfiguration.getCardinality(), 
                 enumValueConfiguration.getValueSize(),
                 input);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumValueConfigurationValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration)
     */
    @Override
    public void validate(EnumValueConfiguration enumValueConfiguration) throws ValidationException {
        
        if (enumValueConfiguration == null) {
            throw new ValidationException("Invalid enumValueConfiguration!");
        }
        
        validateKey(enumValueConfiguration.getKey());
        
        validateDescription(enumValueConfiguration.getDescription());
        
        validateValidity(enumValueConfiguration.getValidFrom(), enumValueConfiguration.getValidTill());
        
        validateDefaultValue(enumValueConfiguration.getDataType(), 
                             enumValueConfiguration.getCardinality(), 
                             enumValueConfiguration.getValueSize(), 
                             enumValueConfiguration.getDefaultValue());
        
        validateExampleValue(enumValueConfiguration.getDataType(), 
                             enumValueConfiguration.getCardinality(), 
                             enumValueConfiguration.getValueSize(), 
                             enumValueConfiguration.getExampleValue());
    }


    @Override
    public void validate(EnumValueConfigurationDataType dataType, EnumValueConfigurationSizing<Integer> cardinality, EnumValueConfigurationSizing<?> valueSize, String input) throws ValidationException {
        try {
            validateValue("input", dataType, cardinality, valueSize, input);
        } catch (ValidationException ex) {
            throw ExceptionUtil.getInstance().throwsException(ValidationException.class, "[input] " + ex.getMessage(), ex.getStackTrace());
        }
    }


    /**
     * Validate description
     *
     * @param description the description
     * @throws ValidationException In case of a validation violation
     */
    protected void validateDescription(String description) throws ValidationException {
        
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Invalid description!");
        }
        
        String[] descriptionSplit = description.split(" ");
        if (descriptionSplit == null || descriptionSplit.length <= 1) {
            throw new ValidationException("Invalid description, the description is too short!");
        }
        
        if (descriptionSplit[0].toUpperCase().charAt(0) != descriptionSplit[0].charAt(0)) {
            throw new ValidationException("Invalid description, it must begin with a capital letter!");
        }
        
        if (descriptionSplit.length > 2) {
            char lastCharacter = description.charAt(description.length() - 1);
            if (lastCharacter != '.' && lastCharacter != '!' && lastCharacter != '?') {
                throw new ValidationException("Invalid description, it don't ends with a punctuation mark!");
            }
        }
    }

    
    /**
     * Validate key
     *
     * @param key the key
     * @throws ValidationException In case of a validation violation
     */
    protected void validateKey(String key) throws ValidationException {
        
        if (key == null || key.trim().isEmpty()) {
            throw new ValidationException("Invalid key!");
        }
    }

    
    /**
     * Validate validity
     *
     * @param validFrom the valid from
     * @param validTill the valid till
     * @throws ValidationException In case of a validation violation
     */
    protected void validateValidity(Instant validFrom, Instant validTill) throws ValidationException {
        
        if (validFrom == null) {
            throw new ValidationException("Invalid validFrom date!");
        }
        
        if (validTill == null) {
            throw new ValidationException("Invalid validTill date!");
        }
        
        if (!validFrom.isBefore(validTill)) {
            throw new ValidationException("Invalid validFrom / validTill date!");
        }
    }

    
    /**
     * Validate default value
     *
     * @param dataType the data type
     * @param cardinality the cardinality
     * @param valueSize the value size
     * @param inputDefaultValue the default value
     * @throws ValidationException In case of a validation violation
     */
    protected void validateDefaultValue(EnumValueConfigurationDataType dataType, EnumValueConfigurationSizing<Integer> cardinality, EnumValueConfigurationSizing<?> valueSize, String inputDefaultValue) 
            throws ValidationException {

        String defaultValue = inputDefaultValue;
        if (defaultValue != null && defaultValue.isEmpty()) {
            return; // in case we have no default value; ignore this check
        }

        try {
            validateValue("defaultValue", dataType, cardinality, valueSize, defaultValue);
        } catch (ValidationException ex) {
            throw ExceptionUtil.getInstance().throwsException(ValidationException.class, "[defaultValue] " + ex.getMessage(), ex.getStackTrace());
        }
    }

    
    /**
     * Validate example value
     *
     * @param dataType the data type
     * @param cardinality the cardinality
     * @param valueSize the value size
     * @param inputExampleValue the example value
     * @throws ValidationException In case of a validation violation
     */
    protected void validateExampleValue(EnumValueConfigurationDataType dataType, EnumValueConfigurationSizing<Integer> cardinality, EnumValueConfigurationSizing<?> valueSize, String inputExampleValue) 
            throws ValidationException {
        
        String exampleValue = inputExampleValue;
        if (exampleValue != null && exampleValue.isEmpty()) {
            exampleValue = null;
        }

        EnumValueConfigurationSizing<Integer> exampleValueCardinality = cardinality;
        if (exampleValueCardinality.getMinSize() == null || exampleValueCardinality.getMinSize().intValue() < 0) {
            exampleValueCardinality = new EnumValueConfigurationSizing<Integer>(1, cardinality.getMaxSize());
            exampleValueCardinality.setMaxSizeAsString(cardinality.getMaxSizeAsString());
        }

        try {
            validateValue("exampleValue", dataType, exampleValueCardinality, valueSize, exampleValue);
        } catch (ValidationException ex) {
            String msg = ex.getMessage();
            msg = msg.replace("minSize=1", "minSize=" + cardinality.getMinSizeAsString());
            throw ExceptionUtil.getInstance().throwsException(ValidationException.class, "[exampleValue] " + msg, ex.getStackTrace());
        }
    }

    
    /**
     * Validate example value
     *
     * @param <T> the generic type
     * @param inputType the input type
     * @param dataType the data type
     * @param cardinality the cardinality
     * @param valueSize the value size
     * @param input the input value
     * @return the number of elements
     * @throws EmptyValueException In case of an empty value
     * @throws ValidationException In case of a validation violation
     */
    protected <T> int validateValue(String inputType, EnumValueConfigurationDataType dataType, EnumValueConfigurationSizing<Integer> cardinality, EnumValueConfigurationSizing<T> valueSize, String input)
            throws EmptyValueException, ValidationException {
        
        if (dataType == null) {
            throw new ValidationException("Invalid dataType in [" + inputType + "]! ");
        }

        final boolean isMandatory = (cardinality != null && cardinality.getMinSize() != null && cardinality.getMinSize().intValue() > 0);
        if ((input == null || input.isEmpty()) && isMandatory) {
            throw new ValidationException("Missing [" + inputType + "], its mandatory and not optional (cardinality: " + cardinality +  ")!");
        }

        int numberOfElements = 0;
        if (cardinality == null || cardinality.getMaxSize() == null || cardinality.getMaxSize().intValue() <= 1) {
            // no cardinality
            
            if (cardinality != null && cardinality.getMinSize() != null && cardinality.getMaxSize().intValue() < cardinality.getMinSize().intValue()) {
                throw new ValidationException("Invalid cardinality of [" + inputType + "], the minSize [" + cardinality.getMinSize() + "] should be <= then maxSize [" + cardinality.getMaxSize() + "]! ");
            }

            try {
                validateValue(inputType, dataType, valueSize, input);
                numberOfElements++;
            } catch (EmptyValueException ex) {
                if (cardinality != null && cardinality.getMinSize() != null && cardinality.getMinSize().intValue() <= 0) {
                    // empty value is valid
                } else {
                    throw ex;
                }
            }
        } else {
            try {
                int length = 0;
                List<String> inputList = JSONUtil.getInstance().convert(input);
                if (inputList != null) {
                    length = inputList.size();
                }
                
                if (cardinality.getMinSize() != null && length < cardinality.getMinSize().intValue()) {
                    throw new ValidationException("Invalid cardinality of [" + inputType + "], the minSize is [" + cardinality.getMinSize() + "]! ");
                }

                if (cardinality.getMaxSize() != null && length > cardinality.getMaxSize().intValue()) {
                    throw new ValidationException("Invalid cardinality of [" + inputType + "], the maxSize is [" + cardinality.getMaxSize() + "]! ");
                }
                
                if (inputList != null) {
                    for (String in : inputList) {
                        try {
                            validateValue(inputType, dataType, valueSize, in);
                            numberOfElements++;
                        } catch (EmptyValueException ex) {
                            if (cardinality != null && cardinality.getMinSize() != null && cardinality.getMinSize().intValue() <= 0) {
                                // empty value is valid
                            } else {
                                throw ex;
                            }
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid cardinality of [" + inputType + "] for intput [" + input + "]. Expected a JSON array: " + e.getMessage());
            }
        }
        
        return numberOfElements;
    }
    

    /**
     * Validate example value
     *
     * @param <T> the generic type
     * @param inputType the input type
     * @param dataType the data type
     * @param valueSize the value size
     * @param input the input value
     * @throws EmptyValueException In case of an empty value
     * @throws ValidationException In case of a validation violation
     */
    protected <T> void validateValue(String inputType, EnumValueConfigurationDataType dataType, EnumValueConfigurationSizing<T> valueSize, String input)
            throws EmptyValueException, ValidationException {
        
        if (dataType == null) {
            throw new ValidationException("Invalid dataType in [" + inputType + "]! ");
        }

        @SuppressWarnings("unchecked")
        IEnumValueConfigurationValueValidator<T> validator = (IEnumValueConfigurationValueValidator<T>) EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationValueValidator(dataType);
        validator.validateValue(valueSize, input);
    }    
}
