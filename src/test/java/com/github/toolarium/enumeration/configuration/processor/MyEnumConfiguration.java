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
    @EnumValueConfiguration(description = "The hostname", dataType = DataType.STRING, defaultValue = "hostname", exampleValue = "myhost.com", minValue = "0", maxValue = "10")
    HOSTNAME,

    @EnumValueConfiguration(description = "The port.", exampleValue = "8080")
    PORT,

    @EnumValueConfiguration(description = "This is the date.", dataType = DataType.DATE, defaultValue = "2021-03-01", exampleValue = "2021-01-01", minValue = "2000-01-01", maxValue = "2036-12-31")
    DATE,

    @EnumValueConfiguration(description = "The hint", exampleValue = "hint")
    HINT,
    
    @EnumValueConfiguration(description = "An number array example.",  dataType = DataType.NUMBER, cardinality = "2..3", defaultValue = "[\"1\", \"2\" ]", exampleValue = "[\"1\", \"2\"]")
    ARRAY_SAMPLE, 

    @EnumValueConfiguration(description = "My value B.", dataType = DataType.BINARY, exampleValue = "myfile.txt|||VGV4dAo=")
    VALUE_B,

    @EnumValueConfiguration(description = "My value C.", dataType = DataType.NUMBER, minValue = "100", exampleValue = "123", cardinality = "0..1")
    VALUE_C,

    @EnumValueConfiguration(description = "My value F.",  dataType = DataType.NUMBER, cardinality = "2..3", defaultValue = "[\"1\", \"2\" ]", exampleValue = "[\"1\", \"2\" ]")
    VALUE_F,

    @EnumValueConfiguration(description = "My value H.", exampleValue = "2", dataType = DataType.NUMBER, cardinality = "2")
    VALUE_H,

    @EnumValueConfiguration(description = "My value I.", exampleValue = "1",  dataType = DataType.NUMBER, cardinality = "0..1")
    VALUE_I,
    
    @EnumValueConfiguration(description = "An number array example.",  dataType = DataType.NUMBER, cardinality = "2..3", defaultValue = "[\"2\", \"1\"]", exampleValue = "[\"6\", \"7\"]", maxValue = "8")
    SAMPLE1;
}
