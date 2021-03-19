/*
 * EnumValueConfigurationValidatorFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import com.github.toolarium.enumeration.configuration.validation.impl.DefaultEnumValueConfigurationValidator;

/**
 * Defines the enum value configuration validator factory.
 * 
 * @author patrick
 */
public final class EnumValueConfigurationValidatorFactory {
    private IEnumValueConfigurationValidator validator;

    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final EnumValueConfigurationValidatorFactory INSTANCE = new EnumValueConfigurationValidatorFactory();
    }

    
    /**
     * Constructor
     */
    private EnumValueConfigurationValidatorFactory() {
        validator = new DefaultEnumValueConfigurationValidator();
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static EnumValueConfigurationValidatorFactory getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Sets the validator
     *
     * @param validator the validator
     */
    public void setValidator(IEnumValueConfigurationValidator validator) {
        this.validator = validator;
    }
    
    
    /**
     * Get the validator
     *
     * @return the validator
     */
    public IEnumValueConfigurationValidator getValidator() {
        return validator;
    }
}
