/*
 * URIEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;
import java.net.URI;

/**
 * Defines the uri {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class URIEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<URI, Number> {
    private static final long serialVersionUID = -5184680687177329611L;

    
    /**
     * Constructor for URIEnumKeyValueConfigurationValueValidator
     */
    public URIEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.URI, URI.class, EnumKeyValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String)
     */
    @Override
    public URI validateValue(EnumKeyValueConfigurationSizing<Number> valueSize, String inputValue) throws EmptyValueException, ValidationException {
        
        URI inputURI = parseValue(inputValue);
        MinMaxValue<Number> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return inputURI;
        }
        
        if (inputURI == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return inputURI;
            }
            
            throw new EmptyValueException("Empty value: invalid timestamp, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }
        
        return inputURI;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createEnumKeyValueConfigurationSizing()
     */
    @Override
    public EnumKeyValueConfigurationSizing<Number> createEnumKeyValueConfigurationSizing() {
        return new EnumKeyValueConfigurationSizing<Number>();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 3L;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public Long getMaxSize() {
        return Long.MAX_VALUE;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumKeyValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
     */
    @Override
    protected boolean isGreaterThan(Number first, Number second) {
        return isGreaterThanValue(first, second);
    }
}
