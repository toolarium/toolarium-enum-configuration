/*
 * EnumKeyConfigurationValidatorFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import com.github.toolarium.enumeration.configuration.validation.impl.DefaultEnumKeyConfigurationValidator;

/**
 * Defines the enum value configuration validator factory.
 * 
 * @author patrick
 */
public final class EnumKeyConfigurationValidatorFactory {
    private IEnumKeyConfigurationValidator validator;

    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final EnumKeyConfigurationValidatorFactory INSTANCE = new EnumKeyConfigurationValidatorFactory();
    }

    
    /**
     * Constructor
     */
    private EnumKeyConfigurationValidatorFactory() {
        validator = new DefaultEnumKeyConfigurationValidator();
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static EnumKeyConfigurationValidatorFactory getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Sets the validator
     *
     * @param validator the validator
     */
    public void setValidator(IEnumKeyConfigurationValidator validator) {
        this.validator = validator;
    }
    
    
    /**
     * Get the validator
     *
     * @return the validator
     */
    public IEnumKeyConfigurationValidator getValidator() {
        return validator;
    }
}
