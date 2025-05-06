/*
 * AbstractEnumConfigurationValidator.java
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
import com.github.toolarium.enumeration.configuration.validation.IEnumConfigurationStructureValidator;
import com.github.toolarium.enumeration.configuration.validation.IEnumConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumKeyValueConfigurationValueValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


/**
 * Abstract enum-configuration validator base class
 * 
 * @author patrick
 */
public abstract class AbstractEnumConfigurationValidator implements IEnumConfigurationStructureValidator, IEnumConfigurationValueValidator  {

    
    /**
     * Constructor for AbstractEnumConfigurationValidator
     */
    protected AbstractEnumConfigurationValidator() {
    }
    

    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumConfigurationStructureValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration)
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
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumConfigurationStructureValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration)
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
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumConfigurationValueValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration, java.lang.String)
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
     * Validate the data type against an input string
     *
     * @param <D> the validated data type
     * @param <T> the size type
     * @param dataType the data type 
     * @param cardinality the cardinality
     * @param isUniqueness True if it is unique; otherwise false, which means that the same value can occur more than once. 
     * @param valueSize the value size
     * @param enumerationValue In case the input has to be inside the enumeration
     * @param input the input to validate
     * @return the validated values
     * @throws ValidationException In case of a validation error
     */
    protected <D,T> Collection<D> validate(EnumKeyValueConfigurationDataType dataType, 
                                        EnumKeyValueConfigurationSizing<Integer> cardinality, 
                                        boolean isUniqueness, 
                                        EnumKeyValueConfigurationSizing<T> valueSize,
                                        String enumerationValue,
                                        String input) 
            throws ValidationException {
        try {
            return validateValue("input", dataType, cardinality, isUniqueness, valueSize, enumerationValue, input);
        } catch (ValidationException ex) {
            ValidationException e = ExceptionUtil.getInstance().throwsException(ValidationException.class, "[input] " + ex.getMessage(), ex.getStackTrace()); 
            e.setValue(ex.getConfigurationValue(), ex.getConvertedValueList());
            throw e;
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
            ValidationException e = ExceptionUtil.getInstance().throwsException(ValidationException.class, "[defaultValue] " + ex.getMessage(), ex.getStackTrace());
            e.setValue(ex.getConfigurationValue(), ex.getConvertedValueList());
            throw e;
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
            ValidationException e = ExceptionUtil.getInstance().throwsException(ValidationException.class, "[exampleValue] " + msg, ex.getStackTrace());
            e.setValue(ex.getConfigurationValue(), ex.getConvertedValueList());
            throw e;
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
            enumerationValue = enumerationValue.trim();
            List<String> list = null;
            if (enumerationValue.startsWith("[") && enumerationValue.endsWith("]")) {
                list = JSONUtil.getInstance().convert(enumerationValue);
            } else {
                list = new ArrayList<>();
                for (String e : enumerationValue.split(",")) {
                    list.add(e.trim());
                }
            }
            
            Collection<D> result = null;
            for (String e :  list) {
                Collection<D> validateValue = validateValue("enumerationValue", dataType, cardinality, isUniqueness, valueSize, null, e);
                if (result == null) {
                    result = validateValue;
                } else {
                    result.addAll(validateValue);
                }
            }
            return result;
        } catch (ValidationException ex) {
            ValidationException e = ExceptionUtil.getInstance().throwsException(ValidationException.class, "[enumerationValue] " + ex.getMessage(), ex.getStackTrace());
            e.setValue(ex.getConfigurationValue(), ex.getConvertedValueList());
            throw e;
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
                throw new ValidationException("Invalid cardinality of [" + inputType + "], the min cardinality [" + cardinality.getMinSize() + "] should be <= then max cardinality [" + cardinality.getMaxSize() + "]! ", null);
            }

            try {
                D value = validateValue(inputType, dataType, valueSize, input);
                if (enumarationValues != null && !enumarationValues.contains(value)) {
                    String validValues = enumarationValues.toString();
                    if (validValues.startsWith("[") && validValues.length() > 1) {
                        validValues = validValues.substring(1);
                    }
                    if (validValues.endsWith("]") && validValues.length() > 1) {
                        validValues = validValues.substring(0, validValues.length() - 1);
                    }
                    
                    throw new ValidationException("Invalid enumeration of [" + inputType + "] for intput [\"" + input + "\"], allowed values are: " + validValues, input, JSONUtil.getInstance().convert((String)value));
                }
                
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
                
                if (inputList != null) {
                    boolean hasUniquenessError = false;
                    boolean hasEnumerationError = false;
                    for (String in : inputList) {
                        try {
                            D value = validateValue(inputType, dataType, valueSize, in);
                            if (enumarationValues != null && !enumarationValues.contains(value)) {
                                hasEnumerationError = true;
                            }
                            
                            if (!collection.add(value)) {
                                hasUniquenessError = true;
                                collection = new ArrayList<D>(collection);
                                collection.add(value);
                            }
                        } catch (EmptyValueException ex) {
                            if (cardinality != null && cardinality.getMinSize() != null && cardinality.getMinSize().intValue() <= 0) {
                                // empty value is valid
                            } else {
                                throw ex;
                            }
                        }
                    }                    
                    
                    if (hasEnumerationError) {
                        String validValues = enumarationValues.toString();
                        if (validValues.startsWith("[") && validValues.length() > 1) {
                            validValues = validValues.substring(1);
                        }
                        if (validValues.endsWith("]") && validValues.length() > 1) {
                            validValues = validValues.substring(0, validValues.length() - 1);
                        }
                        
                        throw new ValidationException("Invalid enumeration of [" + inputType + "] for intput [" + input + "], allowed values are: " + validValues, input, collection);
                    }
                    
                    if (hasUniquenessError) {
                        throw new ValidationException("Invalid isUniqueness of [" + inputType + "] for intput [" + input + "]. Value already exist!", input, collection);
                    }
                }
                
                if (cardinality.getMinSize() != null && length < cardinality.getMinSize().intValue()) {
                    throw new ValidationException("Invalid cardinality of [" + inputType + "], the min cardinality is [" + cardinality.getMinSize() + "].", input, collection);
                }

                if (cardinality.getMaxSize() != null && length > cardinality.getMaxSize().intValue()) {
                    throw new ValidationException("Invalid cardinality of [" + inputType + "], the max cardinality Size is [" + cardinality.getMaxSize() + "].", input, collection);
                }
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid cardinality of [" + inputType + "] for intput [" + input + "]. Expected a JSON array: " + e.getMessage(), input, null);
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

        String maxSize = null;
        if (valueSize != null && valueSize.getMaxSize() != null) {
            maxSize = valueSize.getMaxSize().toString();
        }
        
        @SuppressWarnings("unchecked")
        IEnumKeyConfigurationValueValidator<D, T> validator = (IEnumKeyConfigurationValueValidator<D, T>) EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationValueValidator(dataType, maxSize);
        return validator.validateValue(valueSize, input);
    }    
}
