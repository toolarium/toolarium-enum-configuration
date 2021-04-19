/*
 * URIEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;
import java.net.URI;

/**
 * Defines the uri {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class URIEnumValueConfigurationValueValidator extends AbstractEnumValueConfigurationValueValidator<URI, Long> {
    private static final long serialVersionUID = -5184680687177329611L;

    
    /**
     * Constructor for URIEnumValueConfigurationDataType
     */
    public URIEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.URI, URI.class, EnumValueConfigurationDataType.NUMBER);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, java.lang.String)
     */
    @Override
    public void validateValue(EnumValueConfigurationSizing<Long> valueSize, String inputValue) throws ValidationException {
        
        URI inputURI = parseValue(inputValue);
        MinMaxValue<Long> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return;
        }
        
        if (inputURI == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return;
            }
            
            throw new ValidationException("Empty value: invalid timestamp, should be at least [" + valueSize.getMinSizeAsString() + "]!");
        }
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.datatype.IEnumValueConfigurationValueValidator#createEnumValueConfigurationSizing()
     */
    @Override
    public EnumValueConfigurationSizing<Long> createEnumValueConfigurationSizing() {
        return new EnumValueConfigurationSizing<Long>();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.datatype.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 3L;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.impl.datatype.impl.AbstractEnumValueConfigurationValueValidator#getMaxSize()
     */
    @Override
    public Long getMaxSize() {
        return Long.MAX_VALUE;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#isGreaterThan(java.lang.Object, java.lang.Object)
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
