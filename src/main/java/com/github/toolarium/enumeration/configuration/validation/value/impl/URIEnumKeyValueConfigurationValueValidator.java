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
public class URIEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<URI, Long> {
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
    public void validateValue(EnumKeyValueConfigurationSizing<Long> valueSize, String inputValue) throws EmptyValueException, ValidationException {
        
        URI inputURI = parseValue(inputValue);
        MinMaxValue<Long> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return;
        }
        
        if (inputURI == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return;
            }
            
            throw new EmptyValueException("Empty value: invalid timestamp, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#createEnumKeyValueConfigurationSizing()
     */
    @Override
    public EnumKeyValueConfigurationSizing<Long> createEnumKeyValueConfigurationSizing() {
        return new EnumKeyValueConfigurationSizing<Long>();
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
    protected boolean isGreaterThan(Long first, Long second) {
        if (first == null) {
            if (second == null) {
                return false;
            }
            
            return true;
        } else if (second == null) {
            return false;
        }
        
        return first.compareTo(second) > 0;
    }
}
