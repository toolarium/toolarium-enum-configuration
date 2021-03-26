/*
 * EnumValueConfigurationsTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import com.github.toolarium.enumeration.configuration.util.DateUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Test {@link EnumConfigurations}.
 * 
 * @author patrick
 *
 */
public class EnumValueConfigurationsTest {


    /**
     * Test empty {@link EnumValueConfiguration}.
     */
    @Test
    public void testEmptyEnumValueConfiguration() {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        Assert.assertNull(enumValueConfiguration.getDescription());
        Assert.assertNotNull(enumValueConfiguration.getValidFrom());
        Assert.assertEquals(DateUtil.MAX_TIMESTAMP, enumValueConfiguration.getValidTill());
        Assert.assertEquals("", enumValueConfiguration.getDefaultValue());
        Assert.assertFalse(enumValueConfiguration.isConfidential());
        Assert.assertFalse(enumValueConfiguration.isOptional());
        Assert.assertTrue(enumValueConfiguration.isMandatory());
    }

    
    /**
     * Test empty {@link EnumValueConfiguration}.
     */
    @Test
    public void testIsMandatoryEnumValueConfiguration() {
        EnumValueConfiguration enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setOptional(true);
        Assert.assertNull(enumValueConfiguration.getDescription());
        Assert.assertNotNull(enumValueConfiguration.getValidFrom());
        Assert.assertEquals(DateUtil.MAX_TIMESTAMP, enumValueConfiguration.getValidTill());
        Assert.assertEquals("", enumValueConfiguration.getDefaultValue());
        Assert.assertFalse(enumValueConfiguration.isConfidential());
        Assert.assertTrue(enumValueConfiguration.isOptional());
        Assert.assertFalse(enumValueConfiguration.isMandatory());
        
        enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDefaultValue("20");
        Assert.assertNull(enumValueConfiguration.getDescription());
        Assert.assertNotNull(enumValueConfiguration.getValidFrom());
        Assert.assertEquals(DateUtil.MAX_TIMESTAMP, enumValueConfiguration.getValidTill());
        Assert.assertEquals("20", enumValueConfiguration.getDefaultValue());
        Assert.assertFalse(enumValueConfiguration.isConfidential());
        Assert.assertFalse(enumValueConfiguration.isOptional());
        Assert.assertFalse(enumValueConfiguration.isMandatory());        
    }
    

    /**
     * Test empty {@link EnumConfiguration}.
     */
    @Test
    public void testEmptyEnumConfiguration() {
        EnumConfiguration enumConfiguration = new EnumConfiguration();
        Assert.assertNull(enumConfiguration.getDescription());
        Assert.assertNotNull(enumConfiguration.getValidFrom());
        Assert.assertEquals(DateUtil.MAX_TIMESTAMP, enumConfiguration.getValidTill());
        Assert.assertNull(enumConfiguration.getName());
        Assert.assertTrue(enumConfiguration.getKeyList().isEmpty());
        Assert.assertTrue(enumConfiguration.getInterfaceList().isEmpty());
        Assert.assertTrue(enumConfiguration.getMarkerInterfaceList().isEmpty());
    }
}
