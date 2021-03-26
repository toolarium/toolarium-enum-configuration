/*
 * MyEnumConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.processor;

import com.github.toolarium.enumeration.configuration.IEnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration.DataType;


/**
 * Sample configuration.
 *  
 * @author patrick
 */
@EnumConfiguration(description = "The system configuration.")
public enum MyEnumConfiguration implements IEnumConfiguration, IMyEnumConfiguration {

    @EnumValueConfiguration(description = "The hostname.", dataType = DataType.BOOLEAN, defaultValue = "true", exampleValue = "true", minValue = "1", maxValue = "10")
    HOSTNAME,

    @EnumValueConfiguration(description = "The port.", isOptional = true, exampleValue = "8080")
    PORT,
    
    @EnumValueConfiguration(description = "Hallo.", isOptional = false, isConfidential = true, exampleValue = "hello")
    HALLO,
    
    @EnumValueConfiguration(description = "The port.", isOptional = true, exampleValue = "hint")
    HINT;
    
}
