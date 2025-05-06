/*
 * EnumConfigurationValidatorFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import com.github.toolarium.enumeration.configuration.validation.impl.DefaultEnumConfigurationValidator;
import java.util.HashMap;
import java.util.Map;


/**
 * Defines the enum-configuration validator factory.
 * 
 * @author patrick
 */
public final class EnumConfigurationValidatorFactory {
    private IEnumConfigurationStructureValidator structureValidator;
    private IEnumConfigurationValueValidator valueValidator;
    private Map<String, IEnumConfigurationStructureValidator> validatorMap;    

    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final EnumConfigurationValidatorFactory INSTANCE = new EnumConfigurationValidatorFactory();
    }

    
    /**
     * Constructor
     */
    private EnumConfigurationValidatorFactory() {
        structureValidator = new DefaultEnumConfigurationValidator();
        valueValidator = new DefaultEnumConfigurationValidator();
        validatorMap = new HashMap<String, IEnumConfigurationStructureValidator>();
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static EnumConfigurationValidatorFactory getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Get the validator
     *
     * @return the validator
     */
    public IEnumConfigurationStructureValidator getStructureValidator() {
        return structureValidator;
    }

    
    /**
     * Sets the validator
     *
     * @param structureValidator the structure validator
     */
    public void setStructureValidator(IEnumConfigurationStructureValidator structureValidator) {
        this.structureValidator = structureValidator;
    }

    
    /**
     * Get the validator
     *
     * @return the validator
     */
    public IEnumConfigurationValueValidator getValueValidator() {
        return valueValidator;
    }

        
    /**
     * Sets the value validator
     *
     * @param valueValidator the value validator
     */
    public void setValueValidator(IEnumConfigurationValueValidator valueValidator) {
        this.valueValidator = valueValidator;
    }

    
    /**
     * Sets the interface specific validator
     *
     * @param interfaceName the name of the interface
     * @param validator the validator
     */
    public void setInterfaceStructureValidator(String interfaceName, IEnumConfigurationStructureValidator validator) {
        validatorMap.put(interfaceName, validator);
    }

    
    /**
     * Get the validator
     *
     * @param interfaceName the name of the interface
     * @return the validator
     */
    public IEnumConfigurationStructureValidator getInterfaceStructureValidator(String interfaceName) {
        if (interfaceName == null || interfaceName.trim().isEmpty()) {
            return null;
        }
        
        return validatorMap.get(interfaceName);
    }
}
