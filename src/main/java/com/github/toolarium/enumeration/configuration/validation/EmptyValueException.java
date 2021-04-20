/*
 * EmptyValueException.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;


/**
 * Defines an empty value exception
 * 
 * @author patrick
 */
public class EmptyValueException extends ValidationException {
    private static final long serialVersionUID = 2462346085465724118L;

    
    /**
     * Constructor for EmptyValueException
     * @param message the message
     */
    public EmptyValueException(String message) {
        super(message);
    }
}

