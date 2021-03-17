/*
 * AbstractEnumConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import com.github.toolarium.enumeration.configuration.util.AnnotationConvertUtil;
import java.io.Serializable;
import java.time.Instant;


/**
 * Defines the abstract enum configuration
 *
 * @author patrick
 */
public abstract class AbstractEnumConfiguration implements Serializable {
    private static final long serialVersionUID = -5674883381430545491L;

    private String description;
    private Instant validFrom;
    private Instant validTill;

    
    /**
     * Constructor for AbstractEnumConfiguration
     */
    public AbstractEnumConfiguration() {
        //String description();
        //String validFrom() default "";
        //String validTill() default "9999-12-31T12:00:00.000Z";

        description = null;
        validFrom = null;
        validTill = AnnotationConvertUtil.MAX_TIMESTAMP;
    }

    
    /**
     * Get the configuration description
     *
     * @return the configuration description
     */
    public String getDescription() {
        return description;
    }


    /**
     * Set the configuration description
     *
     * @param description the configuration description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Get the valid from
     *
     * @return the valid from
     */
    public Instant getValidFrom() {
        return validFrom;
    }


    /**
     * Set the valid from
     *
     * @param validFrom the valid from
     */
    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }


    /**
     * Get the valid till
     *
     * @return the valid till
     */
    public Instant getValidTill() {
        return validTill;
    }


    /**
     * Set the valid till
     *
     * @param validTill the valid till
     */
    public void setValidTill(Instant validTill) {
        this.validTill = validTill;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result;
        if (description != null) {
            result += description.hashCode();
        }

        result = prime * result;
        if (validFrom != null) {
            result += validFrom.hashCode();
        }

        result = prime * result;
        if (validTill != null) {
            result += validTill.hashCode();
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

        AbstractEnumConfiguration other = (AbstractEnumConfiguration)obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }

        if (validFrom == null) {
            if (other.validFrom != null) {
                return false;
            }
        } else if (!validFrom.equals(other.validFrom)) {
            return false;
        }

        if (validTill == null) {
            if (other.validTill != null) {
                return false;
            }
        } else if (!validTill.equals(other.validTill)) {
            return false;
        }

        return true;
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/
