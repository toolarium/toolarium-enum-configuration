/*
 * EnumValueConfiguration.java
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
 * Defines the enumeration as a value based configuration.
 * 
 * @author Patrick Meier
 * @version $Revision:  $
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EnumValueConfiguration {
    
    /**
     * Defines the description of the enumeration configuration.
     * 
     * @return The description of the enumeration configuration.
     */
    String description();

    
    /**
     * The default value.
     *
     * @return The default value.
     */
    String defaultValue() default "";

    
    /**
     * Determines whether the configuration is optional. If the default value is empty, no configuration value is available. 
     * An empty configuration value with non-optional parameter marks a configuration value that must be defined.
     * 
     * @return Define if the value is confidential.
     */
    boolean isOptional() default false;

    
    /**
     * Determines whether the configuration value is confidential. In case of persistence it has to be secured.
     * 
     * @return Define if the value is confidential.
     */
    boolean isConfidential() default false;

    
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
