/*
 * EnumKeyConfigurationComplianceTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.enumeration.configuration.dto.EnumConfigurationComplianceResult;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;


/**
 * Test enum key configuration compliance
 *  
 * @author patrick
 */
public class EnumKeyConfigurationComplianceTest {
    /**
     * Test 
     */
    @Test 
    public void testComliance() {
        assertCompliance(true, new EnumKeyValueConfiguration(), new EnumKeyValueConfiguration());
        
        EnumKeyValueConfiguration enumKeyValueConfiguration1 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration1.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration1.setDefaultValue("2022-03-30 ");
        enumKeyValueConfiguration1.setKey("com.github.toolarium.10");
        enumKeyValueConfiguration1.setDescription("This is a description 10.");
        enumKeyValueConfiguration1.setExampleValue("2022-03-30");

        EnumKeyValueConfiguration enumKeyValueConfiguration2 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration2.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration2.setDefaultValue("2021-03-29");
        enumKeyValueConfiguration2.setKey("com.github.toolarium.10");
        enumKeyValueConfiguration2.setDescription("Other description 10.");
        enumKeyValueConfiguration2.setExampleValue("2021-04-30");
        
        assertCompliance(true, enumKeyValueConfiguration1, enumKeyValueConfiguration2);
    }

    
    /**
     * Test 
     */
    @Test 
    public void testInComplianceKeyChange() {
        EnumKeyValueConfiguration enumKeyValueConfiguration1 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration1.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration1.setDefaultValue("2023-03-30 ");
        enumKeyValueConfiguration1.setKey("com.github.toolarium.11");
        enumKeyValueConfiguration1.setDescription("This is a description 10.");
        enumKeyValueConfiguration1.setExampleValue("2023-03-30");

        EnumKeyValueConfiguration enumKeyValueConfiguration2 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration1.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration2.setDefaultValue("2021-03-30 ");
        enumKeyValueConfiguration2.setKey("com.github.toolarium.12");
        enumKeyValueConfiguration2.setDescription("This is a description 11.");
        enumKeyValueConfiguration2.setExampleValue("2021-03-30");
        
        assertCompliance(false, enumKeyValueConfiguration1, enumKeyValueConfiguration2);
    }

    
    /**
     * Test 
     */
    @Test 
    public void testInComplianceDataTypeChange() {
        EnumKeyValueConfiguration enumKeyValueConfiguration1 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration1.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration1.setDefaultValue("2021-03-30 ");
        enumKeyValueConfiguration1.setKey("com.github.toolarium.11");
        enumKeyValueConfiguration1.setDescription("This is a description 11.");
        enumKeyValueConfiguration1.setExampleValue("2021-03-30");

        EnumKeyValueConfiguration enumKeyValueConfiguration2 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration2.setDataType(EnumKeyValueConfigurationDataType.STRING);
        enumKeyValueConfiguration2.setDefaultValue("2021-03-30 ");
        enumKeyValueConfiguration2.setKey("com.github.toolarium.11");
        enumKeyValueConfiguration2.setDescription("This is a description 11.");
        enumKeyValueConfiguration2.setExampleValue("2021-03-30");
        
        assertCompliance(false, enumKeyValueConfiguration1, enumKeyValueConfiguration2);
    }

    
    /**
     * Test 
     */
    @Test 
    public void testInComplianceValidFromChange() {
        EnumKeyValueConfiguration enumKeyValueConfiguration1 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration1.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration1.setDefaultValue("2021-03-30 ");
        enumKeyValueConfiguration1.setKey("com.github.toolarium.11");
        enumKeyValueConfiguration1.setDescription("This is a description 11.");
        enumKeyValueConfiguration1.setExampleValue("2021-03-30");
        enumKeyValueConfiguration1.setValidFrom(Instant.now());

        EnumKeyValueConfiguration enumKeyValueConfiguration2 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration2.setDataType(EnumKeyValueConfigurationDataType.DATE);
        enumKeyValueConfiguration2.setDefaultValue("2021-03-30 ");
        enumKeyValueConfiguration2.setKey("com.github.toolarium.11");
        enumKeyValueConfiguration2.setDescription("This is a description 11.");
        enumKeyValueConfiguration2.setExampleValue("2021-03-30");
        enumKeyValueConfiguration2.setValidFrom(Instant.now());
        
        // equal
        assertCompliance(true, enumKeyValueConfiguration1, enumKeyValueConfiguration2);
    
        // valid from is after
        enumKeyValueConfiguration2.setValidFrom(enumKeyValueConfiguration2.getValidFrom().plus(1, ChronoUnit.HOURS));
        assertCompliance(true, enumKeyValueConfiguration1, enumKeyValueConfiguration2);

        // valid from is before -> fail
        enumKeyValueConfiguration2.setValidFrom(enumKeyValueConfiguration2.getValidFrom().minus(2, ChronoUnit.HOURS));
        assertCompliance(false, enumKeyValueConfiguration1, enumKeyValueConfiguration2);
        
        // reset
        enumKeyValueConfiguration2.setValidFrom(enumKeyValueConfiguration2.getValidFrom().plus(2, ChronoUnit.HOURS));
        assertCompliance(true, enumKeyValueConfiguration1, enumKeyValueConfiguration2);

        
        // valid till is before
        enumKeyValueConfiguration2.setValidTill(enumKeyValueConfiguration2.getValidTill().minus(1, ChronoUnit.HOURS));
        assertCompliance(true, enumKeyValueConfiguration1, enumKeyValueConfiguration2);
        
        // valid till is after  -> fail
        enumKeyValueConfiguration2.setValidTill(enumKeyValueConfiguration2.getValidTill().plus(2, ChronoUnit.HOURS));
        assertCompliance(false, enumKeyValueConfiguration1, enumKeyValueConfiguration2);

        // reset
        enumKeyValueConfiguration2.setValidTill(enumKeyValueConfiguration2.getValidTill().minus(2, ChronoUnit.HOURS));
        assertCompliance(true, enumKeyValueConfiguration1, enumKeyValueConfiguration2);
    }

    
    /**
     * Assert compliance
     *
     * @param value the expected value
     * @param v1 the enum key valze configuration sizing
     * @param v2 the enum key valze configuration sizing
     */
    void assertCompliance(boolean value, EnumKeyValueConfiguration v1, EnumKeyValueConfiguration v2) {
        String str = ""; //v1.toString() + " - " + v2.toString();
        
        EnumConfigurationComplianceResult result = v1.isCompliant(v2);
        if (result.getReason() == null || result.getReason().isBlank()) {
            assertEquals(value, result.isValid(), str);
        } else {
            assertEquals(value, result.isValid(), str + ":" + result.getReason());
        }
    }
}
