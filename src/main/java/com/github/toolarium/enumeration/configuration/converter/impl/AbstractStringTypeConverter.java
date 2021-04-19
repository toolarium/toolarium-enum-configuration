/*
 * AbstractStringTypeConverter.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter.impl;

import com.github.toolarium.enumeration.configuration.converter.IStringTypeConverter;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.util.CIDRUtil;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
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
                    result.setTimestamp(DateUtil.getInstance().parseTimestamp(binaryData[1]));
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Invalid timestamp [" + binaryData[1] + "]!");
                }

                if (binaryData.length > 3) {
                    result.setMimetype(binaryData[2].trim());
                }
            }
            
            result.setData(binaryData[binaryData.length - 1]);
        }
        
        return result;
    }
}
