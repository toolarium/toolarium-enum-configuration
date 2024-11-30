/*
 * EnumConfigurationComplianceResult.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import java.util.Objects;


/**
 * Defines the compliance result
 * 
 * @author patrick
 */
public class EnumConfigurationComplianceResult {
    /** VALID */ 
    public static final EnumConfigurationComplianceResult VALID = new EnumConfigurationComplianceResult();

    /** IS_NULL */ 
    public static final EnumConfigurationComplianceResult IS_NULL = new EnumConfigurationComplianceResult("Object is null!");

    private final boolean isValid;
    private final String reason;

    
    /**
     * Constructor for EnumConfigurationComplianceResult
     */
    EnumConfigurationComplianceResult() {
        this.isValid = true;
        this.reason = "";
    }

    
    /**
     * Constructor for EnumConfigurationComplianceResult
     *
     * @param reason the reason
     */
    public EnumConfigurationComplianceResult(final String reason) {
        this.isValid = false;
        this.reason = reason;
    }

    
    /**
     * Get id it is valid
     *
     * @return true if it is valid; otherwise false
     */
    public boolean isValid() {
        return isValid;
    }
    
    
    /**
     * Get the reason
     *
     * @return the reason
     */
    public String getReason() {
        return reason;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(isValid, reason);
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        EnumConfigurationComplianceResult other = (EnumConfigurationComplianceResult) obj;
        return isValid == other.isValid && Objects.equals(reason, other.reason);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnumConfigurationComplianceResult [isValid=" + isValid + ", reason=" + reason + "]";
    }


    /**
     * Null check
     *
     * @param o the object
     * @return the result
     */
    public static EnumConfigurationComplianceResult isNull(Object o) {
        if (o == null) {
            return IS_NULL;
        }
        
        return VALID;
    }
}
