/*
 * AbstractEnumKeyConfigurationValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.util.ExceptionUtil;
import com.github.toolarium.enumeration.configuration.util.JSONUtil;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.IEnumKeyConfigurationValidator;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumKeyValueConfigurationValueValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


/**
 * Abstract validator base class
 * 
 * @author patrick
 */
public abstract class AbstractEnumKeyConfigurationValidator implements IEnumKeyConfigurationValidator  {

    
    /**
     * Constructor for AbstractEnumKeyConfigurationValidator
     */
    protected AbstractEnumKeyConfigurationValidator() {
    }
    

    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumKeyConfigurationValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration, java.lang.String)
     */
    @Override
    public void validate(EnumKeyConfiguration enumKeyConfiguration, String input) throws ValidationException {
        validate(enumKeyConfiguration);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumKeyConfigurationValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration, java.lang.String)
     */
    @Override
    public <D> Collection<D> validate(EnumKeyValueConfiguration enumKeyValueConfiguration, String input) throws ValidationException {
        
        validate(enumKeyValueConfiguration);
        
        return validate(enumKeyValueConfiguration.getDataType(), 
                        enumKeyValueConfiguration.getCardinality(),
                        enumKeyValueConfiguration.isUniqueness(),
                        enumKeyValueConfiguration.getValueSize(),
                        enumKeyValueConfiguration.getEnumerationValue(),
                        input);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumKeyConfigurationValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration)
     */
    @Override
    public void validate(EnumKeyConfiguration enumKeyConfiguration) throws ValidationException {
        
        if (enumKeyConfiguration == null) {
            throw new ValidationException("Invalid enumKeyConfiguration!");
        }
        
        validateKey(enumKeyConfiguration.getKey());
        
        validateDescription(enumKeyConfiguration.getDescription());
        
        validateValidity(enumKeyConfiguration.getValidFrom(), enumKeyConfiguration.getValidTill());
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumKeyConfigurationValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration)
     */
    @Override
    public void validate(EnumKeyValueConfiguration enumKeyValueConfiguration) throws ValidationException {
        
        if (enumKeyValueConfiguration == null) {
            throw new ValidationException("Invalid enumKeyConfiguration!");
        }
        
        validateKey(enumKeyValueConfiguration.getKey());
        
        validateDescription(enumKeyValueConfiguration.getDescription());
        
        validateValidity(enumKeyValueConfiguration.getValidFrom(), enumKeyValueConfiguration.getValidTill());
        
        validateDefaultValue(enumKeyValueConfiguration.getDataType(), 
                             enumKeyValueConfiguration.getCardinality(),
                             enumKeyValueConfiguration.isUniqueness(),
                             enumKeyValueConfiguration.getValueSize(),
                             enumKeyValueConfiguration.getEnumerationValue(),
                             enumKeyValueConfiguration.getDefaultValue());
        
        validateExampleValue(enumKeyValueConfiguration.getDataType(), 
                             enumKeyValueConfiguration.getCardinality(), 
                             enumKeyValueConfiguration.isUniqueness(),
                             enumKeyValueConfiguration.getValueSize(), 
                             enumKeyValueConfiguration.getEnumerationValue(),
                             enumKeyValueConfiguration.getExampleValue());
        
        validateEnumerationValue(enumKeyValueConfiguration.getDataType(), 
                                 enumKeyValueConfiguration.getCardinality(), 
                                 enumKeyValueConfiguration.isUniqueness(),
                                 enumKeyValueConfiguration.getValueSize(), 
                                 enumKeyValueConfiguration.getEnumerationValue());
        
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumKeyConfigurationValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType, 
     * com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, boolean, com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String, java.lang.String)
     */
    @Override
    public <D,T> Collection<D> validate(EnumKeyValueConfigurationDataType dataType, 
                                        EnumKeyValueConfigurationSizing<Integer> cardinality, 
                                        boolean isUniqueness, 
                                        EnumKeyValueConfigurationSizing<T> valueSize,
                                        String enumerationValue,
                                        String input) 
            throws ValidationException {
        try {
            return validateValue("input", dataType, cardinality, isUniqueness, valueSize, enumerationValue, input);
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
     * @param <D> the validated data type
     * @param <T> the size type
     * @param dataType the data type
     * @param cardinality the cardinality
     * @param isUniqueness True if it is unique; otherwise false, which means that the same value can occur more than once. 
     * @param valueSize the value size
     * @param enumerationValue In case the input has to be inside the enumeration
     * @param inputDefaultValue the default value
     * @return the validated values
     * @throws ValidationException In case of a validation violation
     */
    protected <D, T> Collection<D> validateDefaultValue(EnumKeyValueConfigurationDataType dataType, 
                                                        EnumKeyValueConfigurationSizing<Integer> cardinality,
                                                        boolean isUniqueness,
                                                        EnumKeyValueConfigurationSizing<T> valueSize,
                                                        String enumerationValue,
                                                        String inputDefaultValue) 
            throws ValidationException {

        String defaultValue = inputDefaultValue;
        if (defaultValue == null || defaultValue.isBlank()) {
            return null; // in case we have no default value; ignore this check
        }

        try {
            return validateValue("defaultValue", dataType, cardinality, isUniqueness, valueSize, enumerationValue, defaultValue);
        } catch (ValidationException ex) {
            throw ExceptionUtil.getInstance().throwsException(ValidationException.class, "[defaultValue] " + ex.getMessage(), ex.getStackTrace());
        }
    }

    
    /**
     * Validate example value
     *
     * @param <D> the validated data type
     * @param <T> the size type
     * @param dataType the data type
     * @param cardinality the cardinality
     * @param isUniqueness True if it is unique; otherwise false, which means that the same value can occur more than once. 
     * @param valueSize the value size
     * @param enumerationValue In case the input has to be inside the enumeration
     * @param inputExampleValue the example value
     * @return the validated values
     * @throws ValidationException In case of a validation violation
     */
    protected <D, T> Collection<D> validateExampleValue(EnumKeyValueConfigurationDataType dataType, 
                                                        EnumKeyValueConfigurationSizing<Integer> cardinality,
                                                        boolean isUniqueness,
                                                        EnumKeyValueConfigurationSizing<?> valueSize, 
                                                        String enumerationValue,
                                                        String inputExampleValue) 
            throws ValidationException {
        
        String exampleValue = inputExampleValue;
        if (exampleValue == null || exampleValue.isBlank()) {
            exampleValue = null;
        }

        EnumKeyValueConfigurationSizing<Integer> exampleValueCardinality = cardinality;
        if (exampleValueCardinality.getMinSize() == null || exampleValueCardinality.getMinSize().intValue() < 0) {
            exampleValueCardinality = new EnumKeyValueConfigurationSizing<Integer>(1, cardinality.getMaxSize());
            exampleValueCardinality.setMaxSizeAsString(cardinality.getMaxSizeAsString());
        }

        try {
            return validateValue("exampleValue", dataType, exampleValueCardinality, isUniqueness, valueSize, enumerationValue, exampleValue);
        } catch (ValidationException ex) {
            String msg = ex.getMessage();
            msg = msg.replace("minSize=1", "minSize=" + cardinality.getMinSizeAsString());
            throw ExceptionUtil.getInstance().throwsException(ValidationException.class, "[exampleValue] " + msg, ex.getStackTrace());
        }
    }

    
    /**
     * Validate enumeration value
     *
     * @param <D> the validated data type
     * @param <T> the size type
     * @param dataType the data type
     * @param cardinality the cardinality
     * @param isUniqueness True if it is unique; otherwise false, which means that the same value can occur more than once. 
     * @param valueSize the value size
     * @param inputEnumerationValue the enumeration value
     * @return the validated values
     * @throws ValidationException In case of a validation violation
     */
    protected <D, T> Collection<D> validateEnumerationValue(EnumKeyValueConfigurationDataType dataType, 
                                                            EnumKeyValueConfigurationSizing<Integer> cardinality, 
                                                            boolean isUniqueness,
                                                            EnumKeyValueConfigurationSizing<T> valueSize, 
                                                            String inputEnumerationValue) 
            throws ValidationException {
        
        String enumerationValue = inputEnumerationValue;
        if (enumerationValue == null || enumerationValue.isBlank()) {
            return null; // in case we have no enumeration value; ignore this check
        }

        try {
            return validateValue("enumerationValue", dataType, cardinality, isUniqueness, valueSize, null, enumerationValue);
        } catch (ValidationException ex) {
            throw ExceptionUtil.getInstance().throwsException(ValidationException.class, "[enumerationValue] " + ex.getMessage(), ex.getStackTrace());
        }
    }

    
    /**
     * Validate example value
     *
     * @param <D> the validated data type
     * @param <T> the size type
     * @param inputType the input type
     * @param dataType the data type
     * @param cardinality the cardinality
     * @param isUniqueness True if it is unique; otherwise false, which means that the same value can occur more than once. 
     * @param valueSize the value size
     * @param enumerationValue In case the input has to be inside the enumeration
     * @param input the collection of validated values
     * @return the validated values
     * @throws EmptyValueException In case of an empty value
     * @throws ValidationException In case of a validation violation
     */
    protected <D, T> Collection<D> validateValue(String inputType, 
                                                 EnumKeyValueConfigurationDataType dataType, 
                                                 EnumKeyValueConfigurationSizing<Integer> cardinality,
                                                 boolean isUniqueness,
                                                 EnumKeyValueConfigurationSizing<T> valueSize,
                                                 String enumerationValue,
                                                 String input)
            throws EmptyValueException, ValidationException {
        
        if (dataType == null) {
            throw new ValidationException("Invalid dataType in [" + inputType + "]! ");
        }

        final boolean isMandatory = (cardinality != null && cardinality.getMinSize() != null && cardinality.getMinSize().intValue() > 0);
        if ((input == null || input.isEmpty()) && isMandatory) {
            throw new ValidationException("Missing [" + inputType + "], its mandatory and not optional (cardinality: " + cardinality +  ")!");
        }
        
        // prepare enumeration values
        Collection<D> enumarationValues = validateEnumerationValue(dataType, cardinality, isUniqueness, valueSize, enumerationValue);
        Collection<D> collection = null;
        if (isUniqueness) {
            collection = new HashSet<D>();
        } else {
            collection = new ArrayList<D>();
        }
        
        if (cardinality == null || cardinality.getMaxSize() == null || cardinality.getMaxSize().intValue() <= 1) {
            // no cardinality
            
            if (cardinality != null && cardinality.getMinSize() != null && cardinality.getMaxSize().intValue() < cardinality.getMinSize().intValue()) {
                throw new ValidationException("Invalid cardinality of [" + inputType + "], the minSize [" + cardinality.getMinSize() + "] should be <= then maxSize [" + cardinality.getMaxSize() + "]! ");
            }

            try {
                D value = validateValue(inputType, dataType, valueSize, input);
                collection.add(value);
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
                            D value = validateValue(inputType, dataType, valueSize, in);
                            if (enumarationValues != null && !enumarationValues.contains(value)) {
                                throw new ValidationException("Invalid enumeration of [" + inputType + "] for intput [" + input + "], allowed values are: " + enumerationValue);
                            }
                            
                            if (!collection.add(value)) {
                                throw new ValidationException("Invalid isUniqueness of [" + inputType + "] for intput [" + input + "]. Value already exist!");
                            }

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
        
        return collection;
    }
    

    /**
     * Validate example value
     *
     * @param <D> the validated data type
     * @param <T> the size type
     * @param inputType the input type
     * @param dataType the data type
     * @param valueSize the value size
     * @param input the input value
     * @return the validated value
     * @throws EmptyValueException In case of an empty value
     * @throws ValidationException In case of a validation violation
     */
    protected <D, T> D validateValue(String inputType, EnumKeyValueConfigurationDataType dataType, EnumKeyValueConfigurationSizing<T> valueSize, String input)
            throws EmptyValueException, ValidationException {
        
        if (dataType == null) {
            throw new ValidationException("Invalid dataType in [" + inputType + "]! ");
        }

        @SuppressWarnings("unchecked")
        IEnumKeyConfigurationValueValidator<D, T> validator = (IEnumKeyConfigurationValueValidator<D, T>) EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationValueValidator(dataType);
        return validator.validateValue(valueSize, input);
    }    
}
