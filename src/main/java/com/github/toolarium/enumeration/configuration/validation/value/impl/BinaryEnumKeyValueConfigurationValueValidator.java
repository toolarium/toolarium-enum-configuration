/*
 * BinaryEnumKeyValueConfigurationValueValidator.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.EmptyValueException;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator;
import java.util.Base64;


/**
 * Defines the binary {@link IEnumKeyConfigurationValueValidator}.
 * 
 * @author patrick
 */
public class BinaryEnumKeyValueConfigurationValueValidator extends AbstractEnumKeyValueConfigurationValueValidator<EnumKeyValueConfigurationBinaryObject, Long> {
    private static final long serialVersionUID = 545511902630603231L;

    
    /**
     * Constructor for BinaryEnumKeyValueConfigurationValueValidator
     */
    public BinaryEnumKeyValueConfigurationValueValidator() {
        super(EnumKeyValueConfigurationDataType.BINARY, EnumKeyValueConfigurationBinaryObject.class, EnumKeyValueConfigurationDataType.NUMBER);
    }
    

    /**
     * @see com.github.toolarium.enumeration.configuration.validation.value.IEnumKeyConfigurationValueValidator#validateValue(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing, java.lang.String)
     */
    @Override
    public EnumKeyValueConfigurationBinaryObject validateValue(EnumKeyValueConfigurationSizing<Long> valueSize, String inputValue) throws EmptyValueException, ValidationException {
        
        EnumKeyValueConfigurationBinaryObject inputBinaryObject = parseValue(inputValue);
        MinMaxValue<Long> minMaxValue = preapreMinMaxValue(valueSize, inputValue);
        if (minMaxValue == null) {
            return inputBinaryObject;
        }

        if (inputBinaryObject == null) {
            if (minMaxValue.getMin() == null || minMaxValue.getMin().equals(getMinSize())) {
                return inputBinaryObject;
            }
            
            throw new EmptyValueException("Empty value: invalid binary, should be at the length of [" + valueSize.getMinSizeAsString() + "]!");
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
        
        return inputBinaryObject;
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
        return 0L;
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
