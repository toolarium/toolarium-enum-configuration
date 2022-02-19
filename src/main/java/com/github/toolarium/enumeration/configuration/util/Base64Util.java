/*
 * Base64Util.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import java.util.Base64;
import java.util.regex.Pattern;

/**
 * Base64 utility
 * 
 * @author patrick
 */
public final class Base64Util {
    
    /** Base64 validation regular expression. */
    //private static final Pattern BASE64_PATTERN = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
    private static final Pattern BASE64_PATTERN = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");

    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     */
    private static class HOLDER {
        static final Base64Util INSTANCE = new Base64Util();
    }

    
    /**
     * Constructor
     */
    private Base64Util() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static Base64Util getInstance() {
        return HOLDER.INSTANCE;
    }


    /**
     * Validates the input string if it is a valid base64 encoding or not. 
     * An empty string is also considered as valid. All whitespace are ignored.
     *
     * @param input to validate.
     * @return true if the string is a valid Base64 encoding.
     */
    public boolean isValidBase64(final String input) {
        if (input == null || input.isBlank()) {
            return false;
        }

        String base64Content = input.trim();
        if (BASE64_PATTERN.matcher(base64Content).matches()) {
            try {
                Base64.getDecoder().decode(base64Content);
                return (base64Content.length() % 4 == 0);
                
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        
        return false;
    }
}
