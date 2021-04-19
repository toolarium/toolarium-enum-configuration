/*
 * BinaryEnumValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator;
import java.util.Base64;


/**
 * Defines the binary {@link IEnumValueConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class BinaryEnumValueConfigurationValueValidator extends AbstractEnumValueConfigurationValueValidator<EnumValueConfigurationBinaryObject, Long> {
    private static final long serialVersionUID = 545511902630603231L;

    
    /**
     * Constructor for BinaryEnumValueConfigurationDataType
     */
    public BinaryEnumValueConfigurationValueValidator() {
        super(EnumValueConfigurationDataType.BINARY, EnumValueConfigurationBinaryObject.class, EnumValueConfigurationDataType.NUMBER);
    }
    

    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing, java.lang.String)
     */
    @Override
    public void validateValue(EnumValueConfigurationSizing<Long> valueSize, String inputValue) throws ValidationException {
        
        EnumValueConfigurationBinaryObject inputBinaryObject = parseValue(inputValue);
        MinMaxValue<Long> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return;
        }

        if (inputBinaryObject == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return;
            }
            
            throw new ValidationException("Empty value: invalid binary, should be at the length of [" + valueSize.getMinSizeAsString() + "]!");
        }

        if (inputBinaryObject.getName() != null) {
            // NOP
        }
        
        if (inputBinaryObject.getMimetype() != null) {
            // NOP
        }
        
        if (inputBinaryObject.getTimestamp() != null) {
            // NOP
        }


        Long length = 0L;
        try {
            final byte[] decodedBytes = Base64.getDecoder().decode(inputBinaryObject.getData().trim());
            if (decodedBytes != null) {
                length = Long.valueOf(decodedBytes.length);
            }
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Could not decode file content: " + ex.getMessage() + "\n -> [" + inputValue + "]!");
        }
        
        if (length.compareTo(minMaxValue.getMin()) < 0) {
            throw new ValidationException("Too small: invalid size of file, should be at least [" + valueSize.getMinSizeAsString() + "] (now " + inputValue + ")!");
        }
    
        if (length.compareTo(minMaxValue.getMax()) > 0) {
            throw new ValidationException("Too big: invalid size of file, should be in range of [" + valueSize.getMinSizeAsString() + ".." + valueSize.getMaxSizeAsString() + "] (now " + inputValue + ", encoded size: " + length + ")!");
        }
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumValueConfigurationValueValidator#createEnumValueConfigurationSizing()
     */
    @Override
    public EnumValueConfigurationSizing<Long> createEnumValueConfigurationSizing() {
        return new EnumValueConfigurationSizing<Long>();
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMinSize()
     */
    @Override
    public Long getMinSize() {
        return 0L;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.impl.AbstractEnumValueConfigurationValueValidator#getMaxSize()
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
