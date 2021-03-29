/*
 * AbstractEnumValueConfigurationValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.util.CIDRUtil;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import com.github.toolarium.enumeration.configuration.util.JSONUtil;
import com.github.toolarium.enumeration.configuration.validation.IEnumValueConfigurationValidator;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * Abstract validator base class
 * 
 * @author patrick
 */
public abstract class AbstractEnumValueConfigurationValidator implements IEnumValueConfigurationValidator  {
    protected static final String HEX_WEBCOLOR_PATTERN = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";
    protected static final Pattern hexWebColorPattern = Pattern.compile(HEX_WEBCOLOR_PATTERN);

    
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
        validate(enumValueConfiguration.getDataType(), enumValueConfiguration.getCardinality(), enumValueConfiguration.getValueSize(), enumValueConfiguration.isOptional(), input);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumValueConfigurationValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration)
     */
    @Override
    public void validate(EnumValueConfiguration enumValueConfiguration) throws ValidationException  {
        if (enumValueConfiguration == null) {
            throw new ValidationException("Invalid enumValueConfiguration!");
        }
        
        validateKey(enumValueConfiguration.getKey());
        validateDescription(enumValueConfiguration.getDescription());
        validateValidity(enumValueConfiguration.getValidFrom(), enumValueConfiguration.getValidTill());
        validateDefaultValue(enumValueConfiguration.getDataType(), enumValueConfiguration.getCardinality(), enumValueConfiguration.getValueSize(), enumValueConfiguration.isOptional(), enumValueConfiguration.getDefaultValue());
        validateExampleValue(enumValueConfiguration.getDataType(), enumValueConfiguration.getCardinality(), enumValueConfiguration.getValueSize(), enumValueConfiguration.isOptional(), enumValueConfiguration.getExampleValue());
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumValueConfigurationValidator#validate(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType,
     *      com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, boolean, java.lang.String)
     */
    @Override
    public void validate(EnumValueConfigurationDataType dataType, EnumValueConfigurationSizing cardinality, EnumValueConfigurationSizing valueSize, boolean isOptional, String input) 
            throws ValidationException {
        validateValue("input", dataType, cardinality, valueSize, isOptional, input);
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
     * @param isOptional true if it is optional
     * @param inputDefaultValue the default value
     * @throws ValidationException In case of a validation violation
     */
    protected void validateDefaultValue(EnumValueConfigurationDataType dataType, EnumValueConfigurationSizing cardinality, EnumValueConfigurationSizing valueSize, boolean isOptional, String inputDefaultValue) 
            throws ValidationException {
        String defaultValue = inputDefaultValue;
        if (defaultValue != null && defaultValue.isEmpty()) {
            defaultValue = null;
        }
        
        validateValue("defaultValue", dataType, cardinality, valueSize, isOptional || defaultValue == null || defaultValue.isEmpty(), defaultValue);
    }

    
    /**
     * Validate example value
     *
     * @param dataType the data type
     * @param cardinality the cardinality
     * @param valueSize the value size
     * @param isOptional true if it is optional
     * @param inputExampleValue the example value
     * @throws ValidationException In case of a validation violation
     */
    protected void validateExampleValue(EnumValueConfigurationDataType dataType, EnumValueConfigurationSizing cardinality, EnumValueConfigurationSizing valueSize, boolean isOptional, String inputExampleValue) 
            throws ValidationException {
        String exampleValue = inputExampleValue;
        if (exampleValue != null && exampleValue.isEmpty()) {
            exampleValue = null;
        }
        
        validateValue("exampleValue", dataType, cardinality, valueSize, isOptional || exampleValue == null || exampleValue.isEmpty(),  exampleValue);
    }

    
    /**
     * Validate example value
     *
     * @param inputType the input type
     * @param dataType the data type
     * @param cardinality the cardinality
     * @param valueSize the value size
     * @param input the default value
     * @param isOptional true if it is optional; otherwise false
     * @throws ValidationException In case of a validation violation
     */
    protected void validateValue(String inputType, EnumValueConfigurationDataType dataType, EnumValueConfigurationSizing cardinality, EnumValueConfigurationSizing valueSize, boolean isOptional, String input)
            throws ValidationException {
        if (dataType == null) {
            throw new ValidationException("Invalid dataType in [" + inputType + "]! ");
        }

        if (!isOptional && (input == null || input.isEmpty())) {
            throw new ValidationException("Missing [" + inputType + "], its not optional!");
        }

        if (cardinality == null || cardinality.getMaxSize() == null || cardinality.getMaxSize().intValue() <= 1) {
            // no cardinality
            
            if (cardinality != null && cardinality.getMinSize() != null && cardinality.getMaxSize().intValue() < cardinality.getMinSize().intValue()) {
                throw new ValidationException("Invalid cardinality of [" + inputType + "], the minSize [" + cardinality.getMinSize() + "] should be <= then maxSize [" + cardinality.getMaxSize() + "]! ");
            }
            
            if (valueSize == null) {
                // NOP
            } else {
                int inputLength = 0;
                if (input != null) {
                    inputLength = input.length();
                }
                
                int max = Integer.MAX_VALUE;
                if (valueSize.getMaxSize() != null) {
                    max = valueSize.getMaxSize().intValue();
                }
                
                int min = 0;
                if (valueSize.getMinSize() != null) {
                    min = valueSize.getMinSize().intValue();
                }

                if (max < min) {
                    throw new ValidationException("Invalid maxValue / minValue of [" + inputType + "], the minValue [" + min + "] should be <= then maxValue [" + max + "]!");
                }
                
                if (min > inputLength) {
                    throw new ValidationException("Too short: invalid length of [" + inputType + "], should be at least [" + min + "] (now " + inputLength + ")!");
                }

                if (inputLength > max) {
                    throw new ValidationException("Too long: invalid length of [" + inputType + "], should be in range of [" + min + ".." + max + "] (now " + inputLength + ")!");
                }
            }
            
            try {
                convert(dataType, input);
            } catch (ValidationException ex) {
                ValidationException e = new ValidationException("Invalid data type of [" + inputType + "] for [" + input + "]: " + ex.getMessage());
                e.setStackTrace(ex.getStackTrace());
                throw e;
            }
        } else {
            try {
                List<String> inputList = JSONUtil.getInstance().convert(input);
                if (inputList == null || inputList.isEmpty()) {
                    if (!isOptional) {
                        throw new ValidationException("Invalid cardinality of [" + inputType + "] for [" + input + "], its not optional!");
                    }
                } else {
                    for (String in : inputList) {
                        validateValue(inputType, dataType, null, valueSize, isOptional, in);    
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid cardinality of [" + inputType + "] for intput [" + input + "]. Expected a JSON array: " + e.getMessage());
            }
        }
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.IEnumValueConfigurationValidator#convert(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(EnumValueConfigurationDataType dataType, String inputToTest) throws ValidationException {
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
            throw new ValidationException(e.getMessage()); 
        }
    }

        
    /**
     * Check if the given value is a number or not
     * 
     * @param value the expression
     * @return true if it is a number
     */
    protected String checkBase(String value) {
        if (value == null) {
            return null;
        }
        
        String exp = value.trim();
        if (exp.length() == 0) {
            return null;
        }
        
        return exp;
    }
    
    
    /**
     * Get the number
     * 
     * @param input the input to parse
     * @return the number
     * @throws NumberFormatException in case its not a number
     */
    protected Long getNumber(String input) throws NumberFormatException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }

        return Long.valueOf(value);
    }


    /**
     * Get the double
     * 
     * @param input the input to parse
     * @return the double
     * @throws NumberFormatException in case its not a double
     */
    protected Double getDouble(String input) throws NumberFormatException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        return Double.valueOf(value);
    }


    /**
     * Get the boolean
     * 
     * @param input the input to parse
     * @return the boolean
     * @throws IllegalArgumentException in case its not a boolean
     */
    protected Boolean getBoolean(String input) throws IllegalArgumentException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        if ("yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        }

        if ("no".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        }

        throw new IllegalArgumentException("Invalid value: [" + input + "].");
    }


    /**
     * Get the date
     * 
     * @param input the input to parse
     * @return the date
     * @throws DateTimeParseException in case its not a date
     */
    protected LocalDate getDate(String input) throws DateTimeParseException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        return LocalDate.parse(value);
    }


    /**
     * Get the time
     * 
     * @param input the input to parse
     * @return the time
     * @throws DateTimeParseException in case its not a time
     */
    protected LocalTime getTime(String input) throws DateTimeParseException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        return LocalTime.parse(value);
    }


    /**
     * Get the timestamp
     * 
     * @param input the input to parse
     * @return the timestamp
     * @throws DateTimeParseException in case its not a timestamp
     */
    protected Instant getTimestamp(String input) throws DateTimeParseException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        return Instant.parse(value);
    }


    /**
     * Get the regexp
     * 
     * @param input the input to parse
     * @return the regexp
     * @throws PatternSyntaxException in case its not a regexp
     */
    protected String getRegExp(String input) throws PatternSyntaxException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        Pattern.compile(value);        
        return value;
    }

    
    /**
     * Get the UUID
     * 
     * @param input the input to parse
     * @return the UUID
     * @throws IllegalArgumentException in case its not a UUID
     */
    protected UUID getUUID(String input) throws IllegalArgumentException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        return UUID.fromString(value);
    }

    
    /**
     * Get the uri
     * 
     * @param input the input to parse
     * @return the uri
     * @throws URISyntaxException in case its not a url
     */
    protected URI getURI(String input) throws URISyntaxException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        return new URI(value);
    }


    /**
     * Get the CIDR
     * 
     * @param input the input to parse
     * @return the CIDR
     * @throws URISyntaxException in case its not a CIDR
     */
    protected String getCIDR(String input) throws URISyntaxException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        if (!CIDRUtil.getInstance().isValidRange(value)) {
            throw new IllegalArgumentException("Invalid value: [" + input + "].");
        }
        
        return value;
    }

    
    /**
     * Get the email
     * 
     * @param input the input to parse
     * @return the email
     * @throws IllegalArgumentException in case its not a email
     */
    protected String getEmail(String input) throws IllegalArgumentException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        String[] split = value.split("@");
        if (split == null || split.length < 2) {
            throw new IllegalArgumentException("Invalid value: [" + input + "].");
        }

        return value;
    }

    
    /**
     * Get the cron
     * 
     * @param input the input to parse
     * @return the cron expression
     * @throws IllegalArgumentException in case its not a cron expression
     */
    protected String getCron(String input) throws IllegalArgumentException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }

        String[] cronSplit = value.split(" ");
        if (cronSplit.length != 5) {
            throw new IllegalArgumentException("Invalid cron entry [" + value + "]!");
        }
        
        return value;
    }
    

    /**
     * Get the color
     * 
     * @param input the input to parse
     * @return the color
     * @throws IllegalArgumentException in case its not a color
     */
    protected String getColor(String input) throws IllegalArgumentException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }
        
        Matcher matcher = hexWebColorPattern.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid value: [" + input + "].");
        }

        return value;
    }


    /**
     * Get the certificate
     * 
     * @param input the input to parse
     * @return the certificate
     * @throws IllegalArgumentException in case its not a certificate
     */
    protected String getCertificate(String input) throws IllegalArgumentException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }

        if (value.length() < 16) {
            throw new IllegalArgumentException("Invalid value: [" + input + "].");
        }

        return value;
    }

    
    /**
     * Get the binary content.
     * Format: <code>[name]|[timestamp]|[mime-type]|[content base64 encoded].</code> 
     * The name, timestamp (as RFC 3339), mimetype are optional, only base64 content is a valid content.,
     * e.g. <code>myfile.txt|2021-03-15T08:59:22.123Z|text/plain|VGV4dAo=</code> or <code>myfile.txt|||VGV4dAo=</code> or <code>VGV4dAo=</code>   
     * 
     * @param input the input to parse
     * @return the file
     * @throws IllegalArgumentException in case its not a file
     */
    protected EnumValueConfigurationBinaryObject getBinary(String input) throws IllegalArgumentException {
        if (checkBase(input) == null) {
            return null;
        }
        
        EnumValueConfigurationBinaryObject result = new EnumValueConfigurationBinaryObject();
        if (input.indexOf('|') < 0) {
            result.setData(input);
            return result;
        }
        
        String[] binaryData = input.split("\\|");
        if (binaryData.length <= 1) {
            result.setData(input);
        } else {
            result.setName(binaryData[0]);
            
            if (binaryData.length > 2) {
                try {
                    result.setTimestamp(DateUtil.getInstance().parseDate(binaryData[1]));
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Invalid timestamp [" + binaryData[1] + "]!");
                }

                if (binaryData.length > 3) {
                    result.setMimetype(binaryData[2]);
                }
            }
            
            result.setData(binaryData[binaryData.length - 1]);
        }
        
        return result;
    }
}
