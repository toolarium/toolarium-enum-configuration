/*
 * DefaultEnumValueConfigurationValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;


import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.validation.IEnumValueConfigurationValidator;
import com.github.toolarium.enumeration.configuration.validation.ValidationResult;


/**
 * Implements the {@link IEnumValueConfigurationValidator}.
 * 
 * @author patrick
 */
public class DefaultEnumValueConfigurationValidator extends AbstractEnumValueConfigurationValidator {
    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumValueConfigurationValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType, java.lang.String)
     */
    @Override
    public ValidationResult validate(EnumValueConfigurationDataType dataType, String inputToTest) {
        ValidationResult result = null;
        
        try {
            String input = inputToTest.trim();
            switch (dataType) {
                case NUMBER:      return prepareResult(getNumber(input), input); 
                case DOUBLE:      return prepareResult(getDouble(input), input);
                case BOOLEAN:     return prepareResult(getBoolean(input), input);
                case DATE:        return prepareResult(getDate(input), input);
                case TIME:        return prepareResult(getTime(input), input);
                case TIMESTAMP:   return prepareResult(getTimestamp(input), input);
                case REGEXP:      return prepareResult(getRegExp(input), input);
                case UUID:        return prepareResult(getUUID(input), input);
                case URI:         return prepareResult(getURI(input), input);
                case CIDR:        return prepareResult(getCIDR(input), input);
                case EMAIL:       return prepareResult(getEmail(input), input);
                case CRON:        return prepareResult(getCron(input), input);
                case COLOR:       return prepareResult(getColor(input), input);
                case CERTIFICATE: return prepareResult(getCertificate(input), input);
                case BINARY:      return prepareResult(getBinary(input), input);
                case STRING:      
                default:
                    return prepareResult(input, input);
            }
        } catch (Exception e) {
            result = new ValidationResult(false, e.getMessage());
        }
        
        return result;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumValueConfigurationValidator#convert(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(EnumValueConfigurationDataType dataType, String inputToTest) {
        
        try {
            switch (dataType) {
                case NUMBER:      return (T)getNumber(inputToTest); 
                case DOUBLE:      return (T)getDouble(inputToTest); 
                case BOOLEAN:     return (T)getBoolean(inputToTest); 
                case DATE:        return (T)getDate(inputToTest); 
                case TIME:        return (T)getTime(inputToTest);
                case TIMESTAMP:   return (T)getTimestamp(inputToTest);
                case REGEXP:      return (T)getRegExp(inputToTest);
                case UUID:        return (T)getUUID(inputToTest);
                case URI:         return (T)getURI(inputToTest);
                case CIDR:        return (T)getCIDR(inputToTest);                
                case EMAIL:       return (T)getEmail(inputToTest);
                case CRON:        return (T)getCron(inputToTest);
                case COLOR:       return (T)getColor(inputToTest);
                case CERTIFICATE: return (T)getCertificate(inputToTest);
                case BINARY:      return (T)getBinary(inputToTest);
                case STRING:
                default:
                    return (T) inputToTest;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage()); 
        }
    }
    
    
    /**
     * Prepare result
     *
     * @param <T> the generic type
     * @param type the type
     * @param inputValue the input value
     * @return the validation result
     */
    protected <T> ValidationResult prepareResult(T type, String inputValue) {
        return new ValidationResult();
    }
}
