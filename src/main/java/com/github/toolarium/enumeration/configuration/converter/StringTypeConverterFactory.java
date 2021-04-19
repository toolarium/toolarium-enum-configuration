/*
 * StringTypeConverterFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter;

import com.github.toolarium.enumeration.configuration.converter.impl.DefaultStringTypConverter;

/**
 * Defines the string value converter factory.
 * 
 * @author patrick
 */
public final class StringTypeConverterFactory {
    private IStringTypeConverter stringTypeConverter;
    
    
    /**
     * Private class, the only instance of the singelton which will be created by
     * accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final StringTypeConverterFactory INSTANCE = new StringTypeConverterFactory();
    }

    
    /**
     * Constructor
     */
    private StringTypeConverterFactory() {
        stringTypeConverter = new DefaultStringTypConverter();
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static StringTypeConverterFactory getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * The {@link IStringTypeConverter} 
     *
     * @return the {@link IStringTypeConverter}.
     */
    public IStringTypeConverter getStringTypeConverter() {
        return stringTypeConverter;
    }
}
