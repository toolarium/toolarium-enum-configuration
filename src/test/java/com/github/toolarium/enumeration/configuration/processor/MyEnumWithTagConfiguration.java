/*
 * MyEnumWithTagConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.processor;

import com.github.toolarium.enumeration.configuration.IEnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration.DataType;

/**
 * Sample configuration.
 *  
 * @author patrick
 */
@EnumConfiguration(description = "The system configuration.", tag = "myTag")
public enum MyEnumWithTagConfiguration implements IEnumConfiguration, IMyEnumConfiguration {
    @EnumKeyValueConfiguration(description = "The hostname", dataType = DataType.STRING, defaultValue = "hostname", exampleValue = "myhost.com", minValue = "0", maxValue = "10")
    HOSTNAME,

    @EnumKeyValueConfiguration(description = "The port.", exampleValue = "8080")
    PORT,

    @EnumKeyValueConfiguration(description = "This is the date.", dataType = DataType.DATE, defaultValue = "2021-03-01", exampleValue = "2021-01-01", minValue = "2000-01-01", maxValue = "2036-12-31")
    DATE,

    @EnumKeyValueConfiguration(description = "The hint", exampleValue = "hint")
    HINT,
    
    @EnumKeyValueConfiguration(description = "An number array example.",  dataType = DataType.NUMBER, cardinality = "2..3", defaultValue = "[\"1\", \"2\" ]", exampleValue = "[\"1\", \"2\"]")
    ARRAY_SAMPLE, 

    @EnumKeyValueConfiguration(description = "My value B.", dataType = DataType.BINARY, exampleValue = "myfile.txt|VGV4dAo=")
    VALUE_B,

    @EnumKeyValueConfiguration(description = "My value C.", dataType = DataType.NUMBER, minValue = "100", exampleValue = "123", cardinality = "0..1")
    VALUE_C,

    @EnumKeyValueConfiguration(description = "My value F.",  dataType = DataType.NUMBER, cardinality = "2..3", defaultValue = "[\"1\", \"2\" ]", exampleValue = "[\"1\", \"2\" ]", isUniqueness = true)
    VALUE_F,

    @EnumKeyValueConfiguration(description = "My value G.",  dataType = DataType.STRING, cardinality = "2..3", exampleValue = "[\"A\", \"1\"]")
    VALUE_G,

    @EnumKeyValueConfiguration(description = "My value H.", exampleValue = "2", dataType = DataType.NUMBER, cardinality = "2")
    VALUE_H,

    @EnumKeyValueConfiguration(description = "My value I.", exampleValue = "1",  dataType = DataType.NUMBER, cardinality = "0..1")
    VALUE_I,
    
    @EnumKeyValueConfiguration(description = "An number array example.",  dataType = DataType.NUMBER, cardinality = "2..3", defaultValue = "[\"2\", \"1\"]", exampleValue = "[\"6\", \"7\"]", maxValue = "8")
    SAMPLE1,
    
    @EnumKeyConfiguration(description = "The system configuration.")
    DD,
    
    @EnumKeyValueConfiguration(description = "An number array example.",  dataType = DataType.BINARY, defaultValue = "defaultname.txt|{plain/text}", exampleValue = "ICBteSBmaWxlIGNvbnRlbnQyICA=")
    BINARY_SAMPLE,
    
    @EnumKeyValueConfiguration(description = "An number array example.",  dataType = DataType.BINARY, exampleValue = "ICBteSBmaWxlIGNvbnRlbnQyICA=")
    BINARY_WITHOUT_DEFAULT_SAMPLE,
    
    @EnumKeyValueConfiguration(description = "Defines the delay of an echo.", defaultValue = "2", exampleValue = "3", dataType = DataType.NUMBER, minValue = "0", maxValue = "10")
    DELAY,
    
    @EnumKeyValueConfiguration(description = "Defines the delay precision.", defaultValue = "SECONDS", exampleValue = "MILLITSECONDS", enumerationValue = " SECONDS, MILLITSECONDS ", isUniqueness = true)
    DELAY_PRECISION_A,

    @EnumKeyValueConfiguration(description = "Defines the delay precision.", defaultValue = "SECONDS", exampleValue = "MILLITSECONDS", enumerationValue = "[ \"SECONDS\", \"MILLITSECONDS\" ]", isUniqueness = true)
    DELAY_PRECISION_B;
}
