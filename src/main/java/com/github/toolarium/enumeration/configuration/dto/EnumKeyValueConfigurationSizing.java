/*
 * EnumKeyValueConfigurationSizing.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;


/**
 * Defines the sizing aspects.
 * 
 * @author patrick
 */
public class EnumKeyValueConfigurationSizing<T> implements Serializable {
    /** Defines the max cardinality */
    public static final String MAX_CARDINALITY = "*";

    private static final long serialVersionUID = 6729379069243085804L;
    private String minSizeAsString;
    private T minSize;
    private String maxSizeAsString;
    private T maxSize;

    
    /**
     * Constructor for EnumKeyValueConfigurationSizing
     */
    public EnumKeyValueConfigurationSizing() {
        this.minSizeAsString = null;
        this.minSize = null;
        this.maxSizeAsString = null;
        this.maxSize = null;
    }

    
    /**
     * Constructor for EnumKeyValueConfigurationSizing
     * 
     * @param minSize the min size or null if not defined.
     * @param maxSize the max size or null if not defined.
     */
    public EnumKeyValueConfigurationSizing(T minSize, T maxSize) {
        if (minSize != null) {
            this.minSizeAsString = minSize.toString();
        }
        
        this.minSize = minSize;
        
        if (maxSize != null) {
            this.maxSizeAsString = maxSize.toString();
        }
        
        this.maxSize = maxSize;
    }

    
    /**
     * Constructor for EnumKeyValueConfigurationSizing
     * 
     * @param minSizeAsString the min size as string
     * @param minSize the min size or null if not defined.
     * @param maxSizeAsString the max size as string
     * @param maxSize the max size or null if not defined.
     */
    public EnumKeyValueConfigurationSizing(String minSizeAsString, T minSize, String maxSizeAsString, T maxSize) {
        this.minSizeAsString = minSizeAsString;
        this.minSize = minSize;
        this.maxSizeAsString = maxSizeAsString;
        this.maxSize = maxSize;
    }


    /**
     * Gets the min size as string.
     *
     * @return the min size as string or null if not defined.
     */
    @JsonIgnore
    public String getMinSizeAsString() {
        return minSizeAsString;
    }

    
    /**
     * Gets the min size.
     *
     * @return the min size or null if not defined.
     */
    public T getMinSize() {
        return minSize;
    }

    
    /**
     * Sets the min size.
     *
     * @param minSizeAsString the min size as string
     */
    @JsonIgnore
    public void setMinSizeAsString(String minSizeAsString) {
        this.minSizeAsString = minSizeAsString;
    }


    /**
     * Sets the min size.
     *
     * @param minSize the min size or null if not defined.
     */
    public void setMinSize(T minSize) {
        this.minSize = minSize;

        if (minSizeAsString == null && minSize != null) {
            setMinSizeAsString(minSize.toString());
        }
    }

    
    /**
     * Gets the max size as string.
     *
     * @return the max size as string or null if not defined.
     */
    @JsonIgnore
    public String getMaxSizeAsString() {
        return maxSizeAsString;
    }


    /**
     * Gets the max size.
     *
     * @return the max size or null if not defined.
     */
    public T getMaxSize() {
        return maxSize;
    }

    
    /**
     * Sets the max size.
     *
     * @param maxSizeAsString the max size as string
     */
    @JsonIgnore
    public void setMaxSizeAsString(String maxSizeAsString) {
        this.maxSizeAsString = maxSizeAsString;
    }


    /**
     * Sets the max size.
     *
     * @param maxSize the max size or null if not defined.
     */
    public void setMaxSize(T maxSize) {
        this.maxSize = maxSize;

        if (maxSizeAsString == null && maxSize != null) {
            setMaxSizeAsString(maxSize.toString());
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        /*
        result = prime * result;
        if (minSizeAsString != null) {
            result += minSizeAsString.hashCode();
        }
        */

        result = prime * result;
        if (minSize != null) {
            result += minSize.hashCode();
        }

        /*
        result = prime * result;
        if (maxSizeAsString != null) {
            result += maxSizeAsString.hashCode();
        }
        */

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
               
        @SuppressWarnings("unchecked")
        EnumKeyValueConfigurationSizing<T> other = (EnumKeyValueConfigurationSizing<T>) obj;
        /*
        if (minSizeAsString == null) {
            if (other.minSizeAsString != null) {
                return false;
            }
        } else if (!minSizeAsString.equals(other.minSizeAsString)) {
            return false;
        }
        */

        if (minSize == null) {
            if (other.minSize != null) {
                return false;
            }
        } else if (!minSize.equals(other.minSize)) {
            return false;
        }

        /*
        if (maxSizeAsString == null) {
            if (other.maxSizeAsString != null) {
                return false;
            }
        } else if (!maxSizeAsString.equals(other.maxSizeAsString)) {
            return false;
        }
        */

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
     * Check if the enum configuration is compliant with another. 
     *
     * @param o the 
     * @return true if it is compliant
     */
    public EnumConfigurationComplianceResult isCompliant(EnumKeyValueConfigurationSizing<?> o) {
        EnumConfigurationComplianceResult result = EnumConfigurationComplianceResult.isNull(o);
        if (!result.isValid()) {
            return result;
        }

        if (minSizeAsString != null && o.getMinSizeAsString() != null && !minSizeAsString.equals(o.getMinSizeAsString()) && compareSizeStrings(minSizeAsString, o.getMinSizeAsString()) > 0) {
            return new EnumConfigurationComplianceResult("Valid from is smaller than current (" + minSizeAsString + " > " + o.getMinSizeAsString() + "): " + toStringExpression() + " - " + o.toStringExpression());
        }

        if (maxSizeAsString != null && o.getMaxSizeAsString() != null && !maxSizeAsString.equals(o.getMaxSizeAsString()) && compareSizeStrings(maxSizeAsString, o.getMaxSizeAsString()) < 0) {
            return new EnumConfigurationComplianceResult("Valid till is smaller than current (" + maxSizeAsString + " < " + o.getMaxSizeAsString() + "): " + toStringExpression() + " - " + o.toStringExpression());
        }

        return result;
    }
    
    
    /**
     * Compare two size strings numerically. The wildcard "*" is treated as the largest possible value.
     * Falls back to lexicographic comparison if parsing fails.
     *
     * @param a the first size string
     * @param b the second size string
     * @return negative if a &lt; b, zero if equal, positive if a &gt; b
     */
    private static int compareSizeStrings(String a, String b) {
        if (MAX_CARDINALITY.equals(a) && MAX_CARDINALITY.equals(b)) {
            return 0;
        }
        if (MAX_CARDINALITY.equals(a)) {
            return 1;
        }
        if (MAX_CARDINALITY.equals(b)) {
            return -1;
        }

        try {
            return Long.compare(Long.parseLong(a), Long.parseLong(b));
        } catch (NumberFormatException e) {
            return a.compareTo(b);
        }
    }


    /**
     * Convert to a string representation
     *
     * @return the string representation
     */
    public String toStringExpression() {
        String result = ""; // "1..1";
        if (getMinSizeAsString() != null) {
            result = getMinSizeAsString();
            if (getMaxSizeAsString() != null) {
                result += ".." + getMaxSizeAsString();
            }
        } else if (getMaxSizeAsString() != null) {
            result = "1.." + getMaxSizeAsString();
        }
        
        return result;
    }

    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnumKeyValueConfigurationSizing [minSize=" + minSizeAsString + ", maxSize=" + maxSizeAsString + "]";
    }
}
