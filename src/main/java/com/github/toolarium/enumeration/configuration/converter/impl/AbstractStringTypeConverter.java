/*
 * AbstractStringTypeConverter.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter.impl;

import com.github.toolarium.enumeration.configuration.converter.IStringTypeConverter;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.IEnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.util.CIDRUtil;
import com.github.toolarium.enumeration.configuration.util.EnumKeyValueConfigurationBinaryObjectParser;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * Implements an abstract string type converter
 * 
 * @author patrick
 */
public abstract class AbstractStringTypeConverter implements IStringTypeConverter {
    protected static final String HEX_WEBCOLOR_PATTERN = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";
    protected static final Pattern hexWebColorPattern = Pattern.compile(HEX_WEBCOLOR_PATTERN);


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
    protected Number getNumber(String input) throws NumberFormatException {
        String value = checkBase(input);
        if (value == null) {
            return null;
        }

        return Long.valueOf(value);
    }


    /**
     * Get the number as string
     * 
     * @param value the input 
     * @return the number as string
     * @throws NumberFormatException in case its not a number
     */
    protected String getNumberAsString(Object value) throws NumberFormatException {
        if (value == null) {
            return null;
        }

        if (value instanceof Short) {
            return Short.toString((Short)value);
        }

        if (value instanceof Integer) {
            return Integer.toString((Integer)value);
        }

        if (value instanceof Long) {
            return Long.toString((Long)value);
        }

        return getObjectsString(value);
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
     * Get the double as string
     * 
     * @param value the input
     * @return the double as string
     * @throws NumberFormatException in case its not a double
     */
    protected String getDoubleAsString(Object value) throws NumberFormatException {
        if (value == null) {
            return null;
        }
        
        if (value instanceof Float) {
            return Float.toString((Float)value);
        }

        if (value instanceof Double) {
            return Double.toString((Double)value);
        }

        return getObjectsString(value);
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
        
        if ("yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        }

        if ("no".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value)) {
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
     * @throws IllegalArgumentException In case of invalid range.
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
        if (cronSplit.length < 5) {
            throw new IllegalArgumentException("Invalid cron entry [" + input + "]!");
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
     * Get the object as string
     * 
     * @param value the input 
     * @return the object
     */
    protected String getObjectsString(Object value) {
        if (value == null) {
            return null;
        }
        
        return value.toString();
    }

    
    /**
     * Get the binary content.
     * Format: <code>[name]|[mime-type]|[timestamp]|[content base64 encoded].</code>
     * The name, mime-type and timestamp (according to RFC 3339) are optional values. If they are present, they must be separated by a pipe character (|).
     * Only the content is not optional and must be either empty or a valid base64 content, 
     * e.g. <code>myfile.txt|text/plain|2021-03-15T08:59:22.123Z|VGV4dAo=</code> or <code>myfile.txt|||VGV4dAo=</code> or <code>VGV4dAo=</code>.   
     * 
     * @param input the input to parse
     * @return the file
     * @throws IllegalArgumentException in case its not a file
     */
    protected IEnumKeyValueConfigurationBinaryObject getBinary(String input) throws IllegalArgumentException {
        if (checkBase(input) == null) {
            return null;
        }
        
        return EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(input);
    }


    /**
     * Prepare exception message
     *
     * @param dataType the data type
     * @param inputToTest the input to test
     * @param e the exception
     * @return the message
     */
    protected String prepareExceptionMessage(EnumKeyValueConfigurationDataType dataType, String inputToTest, Exception e) {
        StringBuilder msg = new StringBuilder();
        msg.append("Invalid value [");
        msg.append(inputToTest);
        msg.append("], it can not be converted into a ");
        msg.append(dataType);
        msg.append(" data type");
        
        if (e instanceof DateTimeParseException || e instanceof PatternSyntaxException || e instanceof URISyntaxException) {
            msg.append(": ");
            msg.append(e.getMessage());
        }
     
        msg.append(".");
        return msg.toString();
        
    }
}
