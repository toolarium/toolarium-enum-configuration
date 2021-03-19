/*
 * ValidationResult.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import java.io.Serializable;


/**
 * Defines the validation result
 * 
 * @author patrick
 */
public class ValidationResult implements Serializable {
    private static final long serialVersionUID = 3303581095781112947L;
    private boolean isValid;
    private String comment;

    
    /**
     * Constructor for ValidationResult
     */
    public ValidationResult() {
        this.isValid = true;
        this.comment = null;
    }

    
    /**
     * Constructor for ValidationResult
     * 
     * @param isValid true if it is valid
     * @param comment an optional comment
     */
    public ValidationResult(boolean isValid, String comment) {
        this.isValid = isValid;
        this.comment = comment;
    }
    
    
    /**
     * Define if it is valid
     *
     * @return true if it is valid
     */
    public boolean isValid() {
        return isValid;
    }
    
    
    /**
     * Gets the comment
     *
     * @return the comment
     */
    public String getComment() {
        return comment;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        result = prime * result;
        if (isValid) {
            result += 1231;
        } else {
            result += 1237;
        }

        result = prime * result;
        if (comment != null) {
            result += comment.hashCode();
        }

        return result;
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
        
        ValidationResult other = (ValidationResult) obj;
        if (isValid != other.isValid) {
            return false;
        }
        
        if (comment == null) {
            if (other.comment != null) {
                return false;
            }
        } else if (!comment.equals(other.comment)) {
            return false;
        }
        
        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ValidationResult [isValid=" + isValid + ", comment=" + comment + "]";
    }
}
