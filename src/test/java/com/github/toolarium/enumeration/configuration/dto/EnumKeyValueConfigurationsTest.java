/*
 * EnumKeyValueConfigurationsTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import com.github.toolarium.enumeration.configuration.util.AnnotationConvertUtil;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Test {@link EnumConfigurations}.
 * 
 * @author patrick
 *
 */
public class EnumKeyValueConfigurationsTest {


    /**
     * Test empty {@link EnumKeyValueConfiguration}.
     */
    @Test
    public void testEmptyKeyEnumKeyValueConfiguration() {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        Assert.assertNull(enumKeyValueConfiguration.getDescription());
        Assert.assertNotNull(enumKeyValueConfiguration.getValidFrom());
        Assert.assertEquals(DateUtil.MAX_TIMESTAMP, enumKeyValueConfiguration.getValidTill());
        Assert.assertEquals("", enumKeyValueConfiguration.getDefaultValue());
        Assert.assertFalse(enumKeyValueConfiguration.isConfidential());
        Assert.assertTrue(enumKeyValueConfiguration.isMandatory());
    }

    
    /**
     * Test empty {@link EnumKeyValueConfiguration}.
     */
    @Test
    public void testMandatoryEnumKeyValueConfiguration() {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setCardinality(new EnumKeyValueConfigurationSizing<Integer>(1, 1));
        Assert.assertNull(enumKeyValueConfiguration.getDescription());
        Assert.assertNotNull(enumKeyValueConfiguration.getValidFrom());
        Assert.assertEquals(DateUtil.MAX_TIMESTAMP, enumKeyValueConfiguration.getValidTill());
        Assert.assertEquals("", enumKeyValueConfiguration.getDefaultValue());
        Assert.assertFalse(enumKeyValueConfiguration.isConfidential());
        Assert.assertTrue(enumKeyValueConfiguration.isMandatory());
    }

    
    /**
     * Test empty {@link EnumKeyValueConfiguration}.
     */
    @Test
    public void testIsMandatoryEnumKeyValueConfiguration() {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        Assert.assertNull(enumKeyValueConfiguration.getDescription());
        Assert.assertNotNull(enumKeyValueConfiguration.getValidFrom());
        Assert.assertEquals(DateUtil.MAX_TIMESTAMP, enumKeyValueConfiguration.getValidTill());
        Assert.assertEquals("", enumKeyValueConfiguration.getDefaultValue());
        Assert.assertFalse(enumKeyValueConfiguration.isConfidential());
        Assert.assertFalse(enumKeyValueConfiguration.isMandatory());
        
        enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDefaultValue("20");
        Assert.assertNull(enumKeyValueConfiguration.getDescription());
        Assert.assertNotNull(enumKeyValueConfiguration.getValidFrom());
        Assert.assertEquals(DateUtil.MAX_TIMESTAMP, enumKeyValueConfiguration.getValidTill());
        Assert.assertEquals("20", enumKeyValueConfiguration.getDefaultValue());
        Assert.assertFalse(enumKeyValueConfiguration.isConfidential());
        Assert.assertTrue(enumKeyValueConfiguration.isMandatory());        
    }
    

    /**
     * Test empty {@link EnumConfiguration}.
     */
    @Test
    public void testEmptyEnumConfiguration() {
        EnumConfiguration<? extends EnumKeyConfiguration> enumConfiguration = new EnumConfiguration<>();
        Assert.assertNull(enumConfiguration.getDescription());
        Assert.assertNotNull(enumConfiguration.getValidFrom());
        Assert.assertEquals(DateUtil.MAX_TIMESTAMP, enumConfiguration.getValidTill());
        Assert.assertNull(enumConfiguration.getName());
        Assert.assertTrue(enumConfiguration.getKeyList().isEmpty());
        Assert.assertTrue(enumConfiguration.getInterfaceList().isEmpty());
        Assert.assertTrue(enumConfiguration.getMarkerInterfaceList().isEmpty());
    }
}
