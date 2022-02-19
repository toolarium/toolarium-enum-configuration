/*
 * ValidationException.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

/**
 * Defines a validation exception
 * 
 * @author patrick
 */
public class ValidationException extends Exception {
    private static final long serialVersionUID = 2462346085465724118L;

    
    /**
     * Constructor for ValidationException
     * 
     * @param message the message
     */
    public ValidationException(String message) {
        super(message);
    }


    /**
     * Constructor for ValidationException
     * 
     * @param message the message
     * @param e the nested exception
     */
    public ValidationException(String message, Exception e) {
        super(message, e);
    }
}
