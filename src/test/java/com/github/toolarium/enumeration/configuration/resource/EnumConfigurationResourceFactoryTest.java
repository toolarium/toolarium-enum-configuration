/*
 * EnumConfigurationResourceFactoryTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.util.AnnotationConvertUtil;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test the EnumConfigurationResourceFactory
 * 
 * @author patrick
 */
public class EnumConfigurationResourceFactoryTest {

    
    /**
     * Test empty enum configurations
     * 
     * @throws IOException in case of an error
     */
    @Test
    public void writeEmptyEnumConfigurations() throws IOException {
        Assert.assertNull(writeAndRead(null));
        
        Assert.assertNull(writeAndRead(new EnumConfigurations()));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new EnumConfigurations().add(null);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new EnumConfigurations().add(new EnumConfiguration(""));
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new EnumConfigurations().add(new EnumConfiguration("  "));
        });
    }

    
    /**
     * Test empty enum configurations
     * 
     * @throws IOException in case of an error
     */
    @Test
    public void writeEnumConfigurations() throws IOException {
        EnumConfigurations e = new EnumConfigurations();
        e.add(new EnumConfiguration("myName"));
        Assert.assertEquals(e, writeAndRead(e));

        e = new EnumConfigurations();
        EnumConfiguration ec = new EnumConfiguration("myName");
        ec.setDescription("My description");
        
        EnumValueConfiguration enumValueConfiguration1 = new EnumValueConfiguration();
        enumValueConfiguration1.setKey("myKey1");
        enumValueConfiguration1.setConfidential(true);
        enumValueConfiguration1.setDescription("My key 1 description");
        enumValueConfiguration1.setDefaultValue("default value 1");
        enumValueConfiguration1.setValidFrom(null);
        enumValueConfiguration1.setValidTill(null);
        ec.add(enumValueConfiguration1);
        
        EnumValueConfiguration enumValueConfiguration2 = new EnumValueConfiguration();
        enumValueConfiguration2.setKey("myKey2");
        enumValueConfiguration2.setConfidential(false);
        enumValueConfiguration2.setDescription("My key 2 description");
        enumValueConfiguration2.setDefaultValue("default value 2");
        enumValueConfiguration2.setValidFrom(Instant.now().plus(24, ChronoUnit.HOURS));
        enumValueConfiguration2.setValidTill(DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec.add(enumValueConfiguration2);

        EnumValueConfiguration enumValueConfiguration3 = new EnumValueConfiguration();
        enumValueConfiguration3.setKey("myKey3");
        enumValueConfiguration3.setConfidential(false);
        enumValueConfiguration3.setDescription("My key 3 description");
        enumValueConfiguration3.setDefaultValue("default value 3");
        enumValueConfiguration3.setValidFrom(Instant.now().minus(24, ChronoUnit.HOURS));
        enumValueConfiguration3.setValidTill(DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec.add(enumValueConfiguration3);

        e.add(ec);
        Assert.assertEquals(e, writeAndRead(e));
    }

    
    /**
     * Test empty enum configurations
     * 
     * @throws IOException in case of an error
     */
    @Test
    public void test() throws IOException {
        EnumConfiguration ec1 = new EnumConfiguration("myName1");
        ec1.setDescription("My description 1");

        EnumValueConfiguration enumValueConfiguration1 = new EnumValueConfiguration();
        enumValueConfiguration1.setKey("myKey1");
        enumValueConfiguration1.setConfidential(true);
        enumValueConfiguration1.setDescription("My key 1 description");
        enumValueConfiguration1.setExampleValue("example value 1");
        enumValueConfiguration1.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        enumValueConfiguration1.setValidFrom(null);
        enumValueConfiguration1.setValidTill(null);
        ec1.add(enumValueConfiguration1);

        EnumValueConfiguration enumValueConfiguration2 = new EnumValueConfiguration();
        enumValueConfiguration2.setKey("myKey2");
        enumValueConfiguration2.setConfidential(true);
        enumValueConfiguration2.setDescription("My key 2 description");
        enumValueConfiguration2.setDefaultValue("default value 2");
        enumValueConfiguration2.setExampleValue("example value 2");
        enumValueConfiguration2.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        enumValueConfiguration2.setValidFrom(null);
        enumValueConfiguration2.setValidTill(null);
        ec1.add(enumValueConfiguration2);
        
        EnumValueConfiguration enumValueConfiguration3 = new EnumValueConfiguration();
        enumValueConfiguration3.setKey("myKey3");
        enumValueConfiguration3.setConfidential(false);
        enumValueConfiguration3.setDescription("My key 3 description");
        enumValueConfiguration3.setDefaultValue("default value 3");
        enumValueConfiguration3.setExampleValue("example value 3");
        enumValueConfiguration3.setValidFrom(Instant.now().plus(24, ChronoUnit.HOURS));
        enumValueConfiguration3.setValidTill(DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec1.add(enumValueConfiguration3);

        EnumValueConfiguration enumValueConfiguration4 = new EnumValueConfiguration();
        enumValueConfiguration4.setKey("myKey4");
        enumValueConfiguration4.setConfidential(false);
        enumValueConfiguration4.setDescription("My key 4 description");
        enumValueConfiguration4.setExampleValue("example value 4");
        enumValueConfiguration4.setValidFrom(Instant.now().minus(24, ChronoUnit.HOURS));
        enumValueConfiguration4.setValidTill(DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec1.add(enumValueConfiguration4);

        EnumConfigurations e = new EnumConfigurations();
        e.add(ec1);
        
        // 
        
        EnumConfiguration ec2 = new EnumConfiguration("myName2");
        ec2.setDescription("My description 2");
        
        EnumValueConfiguration enumValueConfiguration5 = new EnumValueConfiguration();
        enumValueConfiguration5.setKey("myKey5");
        enumValueConfiguration5.setConfidential(true);
        enumValueConfiguration5.setDescription("My key 5 description");
        enumValueConfiguration5.setExampleValue("example value 5");
        enumValueConfiguration5.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        enumValueConfiguration5.setValidFrom(null);
        enumValueConfiguration5.setValidTill(null);
        ec2.add(enumValueConfiguration5);

        EnumValueConfiguration enumValueConfiguration6 = new EnumValueConfiguration();
        enumValueConfiguration6.setKey("myKey6");
        enumValueConfiguration6.setConfidential(true);
        enumValueConfiguration6.setDescription("My key 6 description");
        enumValueConfiguration6.setDefaultValue("default value 6");
        enumValueConfiguration6.setExampleValue("example value 6");
        enumValueConfiguration6.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        enumValueConfiguration6.setValidFrom(null);
        enumValueConfiguration6.setValidTill(null);
        ec2.add(enumValueConfiguration6);
        
        EnumValueConfiguration enumValueConfiguration7 = new EnumValueConfiguration();
        enumValueConfiguration7.setKey("myKey7");
        enumValueConfiguration7.setConfidential(false);
        enumValueConfiguration7.setDescription("My key 7 description");
        enumValueConfiguration7.setDefaultValue("default value 7");
        enumValueConfiguration7.setExampleValue("example value 7");
        enumValueConfiguration7.setValidFrom(Instant.now().plus(24, ChronoUnit.HOURS));
        enumValueConfiguration7.setValidTill(DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec2.add(enumValueConfiguration7);

        EnumValueConfiguration enumValueConfiguration8 = new EnumValueConfiguration();
        enumValueConfiguration8.setKey("myKey8");
        enumValueConfiguration8.setConfidential(false);
        enumValueConfiguration8.setDescription("My key 8 description");
        enumValueConfiguration8.setExampleValue("example value 8");
        enumValueConfiguration8.setValidFrom(Instant.now().minus(24, ChronoUnit.HOURS));
        enumValueConfiguration8.setValidTill(DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec2.add(enumValueConfiguration8);
        
        e.add(ec2);
        Assert.assertEquals(e, writeAndRead(e));
        
        EnumConfiguration eref1 = new EnumConfiguration(ec1.getName());
        eref1.setDescription(ec1.getDescription());
        eref1.setValidFrom(ec1.getValidFrom());
        eref1.setValidTill(ec1.getValidTill());
        eref1.add(enumValueConfiguration3);     
        eref1.add(enumValueConfiguration4);     
        Set<EnumConfiguration> set1 = new LinkedHashSet<EnumConfiguration>();
        set1.add(eref1);

        EnumConfiguration eref2 = new EnumConfiguration(ec2.getName());
        eref2.setDescription(ec2.getDescription());
        eref2.setValidFrom(ec2.getValidFrom());
        eref2.setValidTill(ec2.getValidTill());
        eref2.add(enumValueConfiguration7);     
        eref2.add(enumValueConfiguration8);     
        set1.add(eref2);

        EnumConfiguration eref3 = new EnumConfiguration(ec1.getName());
        eref3.setDescription(ec1.getDescription());
        eref3.setValidFrom(ec1.getValidFrom());
        eref3.setValidTill(ec1.getValidTill());
        eref3.add(enumValueConfiguration4);
        Set<EnumConfiguration> set2 = new LinkedHashSet<EnumConfiguration>();
        set2.add(eref3);
        
        EnumConfiguration eref4 = new EnumConfiguration(ec2.getName());
        eref4.setDescription(ec2.getDescription());
        eref4.setValidFrom(ec2.getValidFrom());
        eref4.setValidTill(ec2.getValidTill());
        eref4.add(enumValueConfiguration8);
        set2.add(eref4);

        assertEquals(set1, e.selectMandatoryConfigurationList());
        assertEquals(set2, e.selectMandatoryConfigurationListWithMissingDefaultValue());
        
        Assert.assertEquals(e, writeAndRead(e));
    }

    
    /**
     * Read and write 
     *
     * @param inputEnumConfigurations the input enum configurations
     * @return the read input configuration
     * @throws IOException In case of an error
     */
    protected EnumConfigurations writeAndRead(EnumConfigurations inputEnumConfigurations) throws IOException {
        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        EnumConfigurationResourceFactory.getInstance().store(inputEnumConfigurations, outputstream);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputstream.toByteArray());
        return EnumConfigurationResourceFactory.getInstance().load(inputStream);
    }
}
