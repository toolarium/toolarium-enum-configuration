/*
 * EnumNameConfiguration.java
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
 * @author patrick
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EnumKeyConfiguration {
    /**
     * Defines the description of the enumeration configuration.
     * 
     * @return The description of the enumeration configuration.
     */
    String description();

    
    /**
     * Determines whether the configuration value is confidential. In case of persistence it has to be secured.
     * 
     * @return Define if the value is confidential.
     */
    boolean isConfidential() default false;

    
    /**
     * Defines the valid from date of the enumeration configuration. By default it's empty which means the current timestamp:
     * <code>DateTimeFormatter.ISO_INSTANT.format(Instant.now().truncatedTo(ChronoUnit.MILLIS))</code>.
     * 
     * @return The valid from information
     */
    String validFrom() default "";

    
    /**
     * Defines the valid till date of the enumeration configuration. By default it's the "max" timestamp.
     * 
     * @return The valid till information
     */
    String validTill() default "9999-12-31T12:00:00.000Z";
}
