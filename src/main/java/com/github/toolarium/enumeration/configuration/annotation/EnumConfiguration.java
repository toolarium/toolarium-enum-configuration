/*
 * EnumConfiguration.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Defines the enumeration as a simple configuration as a set.
 * 
 * @author Patrick Meier
 * @version $Revision:  $
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EnumConfiguration {

    /**
     * Defines the enumeration configuration category. 
     */
    enum Category {
        SYSTEM,
        PRODUCT,
        USER,
        EVENT,
        PERMISSION,
        METRICS
    }

    
    /**
     * Defines the category of the enumeration configuration.
     * 
     * @return The category of the enumeration configuration.
     */
    Category category();
    
    
    /**
     * Defines the description of the enumeration configuration.
     * 
     * @return The description of the enumeration configuration.
     */
    String description();

    
    /**
     * Defines the valid from date of the enumeration configuration. By default it's empty which means the current timestamp:
     * <code>DateTimeFormatter.ISO_INSTANT.format(Instant.now().truncatedTo(ChronoUnit.SECONDS))</code>.
     * 
     * @return The valid from information
     */
    String validFrom() default "";

    
    /**
     * Defines the valid till date of the enumeration configuration. By default it's the "max" timestamp.
     * 
     * @return The valid till information
     */
    String validTill() default "9999-12-31T12:00:00:00Z";
}
