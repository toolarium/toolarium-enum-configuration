/*
 * EnumValueConfigurationSizing.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import java.io.Serializable;

/**
 * Defines the sizing aspects.
 * 
 * @author patrick
 */
public class EnumValueConfigurationSizing implements Serializable {
    private static final long serialVersionUID = 6729379069243085804L;
    private Integer minSize;
    private Integer maxSize;

    
    /**
     * Constructor for EnumValueConfigurationSizing
     */
    public EnumValueConfigurationSizing() {
        this.minSize = 0;
        this.maxSize = 1;
    }

    
    /**
     * Constructor for EnumValueConfigurationSizing
     * 
     * @param minSize the min size or null if not defined.
     * @param maxSize the max size or null if not defined.
     */
    public EnumValueConfigurationSizing(Integer minSize, Integer maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }


    /**
     * Gets the min size.
     *
     * @return the min size or null if not defined.
     */
    public Integer getMinSize() {
        return minSize;
    }


    /**
     * Sets the min size.
     *
     * @param minSize the min size or null if not defined.
     */
    public void setMinSize(Integer minSize) {
        this.minSize = minSize;
    }


    /**
     * Gets the max size.
     *
     * @return the max size or null if not defined.
     */
    public Integer getMaxSize() {
        return maxSize;
    }


    /**
     * Sets the max size.
     *
     * @param maxSize the max size or null if not defined.
     */
    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        result = prime * result;
        if (minSize != null) {
            result += minSize.hashCode();
        }

        result = prime * result;
        if (maxSize != null) {
            result += maxSize.hashCode();
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
        
        EnumValueConfigurationSizing other = (EnumValueConfigurationSizing) obj;
        if (minSize == null) {
            if (other.minSize != null) {
                return false;
            }
        } else if (!minSize.equals(other.minSize)) {
            return false;
        }
        
        if (maxSize == null) {
            if (other.maxSize != null) {
                return false;
            }
        } else if (!maxSize.equals(other.maxSize)) {
            return false;
        }
        
        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnumValueConfigurationSizing [minSize=" + minSize + ", maxSize=" + maxSize + "]";
    }
}
