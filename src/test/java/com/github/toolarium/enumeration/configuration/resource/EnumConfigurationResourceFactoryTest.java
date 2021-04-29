/*
 * EnumConfigurationResourceFactoryTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
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
            new EnumConfigurations().add(new EnumConfiguration<>(""));
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new EnumConfigurations().add(new EnumConfiguration<>("  "));
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
        e.add(new EnumConfiguration<>("myName"));
        Assert.assertEquals(e, writeAndRead(e));

        e = new EnumConfigurations();
        EnumConfiguration<? super EnumKeyConfiguration> ec = new EnumConfiguration<>("myName");
        ec.setDescription("My description");
        
        EnumKeyValueConfiguration enumKeyValueConfiguration1 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration1.setKey("myKey1");
        enumKeyValueConfiguration1.setConfidential(true);
        enumKeyValueConfiguration1.setDescription("My key 1 description");
        enumKeyValueConfiguration1.setDefaultValue("default value 1");
        enumKeyValueConfiguration1.setValidFrom(null);
        enumKeyValueConfiguration1.setValidTill(null);
        ec.add(enumKeyValueConfiguration1);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration2 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration2.setKey("myKey2");
        enumKeyValueConfiguration2.setConfidential(false);
        enumKeyValueConfiguration2.setDescription("My key 2 description");
        enumKeyValueConfiguration2.setDefaultValue("default value 2");
        enumKeyValueConfiguration2.setValidFrom(Instant.now().plus(24, ChronoUnit.HOURS));
        enumKeyValueConfiguration2.setValidTill(DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec.add(enumKeyValueConfiguration2);

        EnumKeyValueConfiguration enumKeyValueConfiguration3 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration3.setKey("myKey3");
        enumKeyValueConfiguration3.setConfidential(false);
        enumKeyValueConfiguration3.setDescription("My key 3 description");
        enumKeyValueConfiguration3.setDefaultValue("default value 3");
        enumKeyValueConfiguration3.setValidFrom(Instant.now().minus(24, ChronoUnit.HOURS));
        enumKeyValueConfiguration3.setValidTill(DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec.add(enumKeyValueConfiguration3);

        EnumKeyConfiguration enumKeyConfiguration4 = new EnumKeyConfiguration();
        enumKeyConfiguration4.setKey("myKey4");
        enumKeyConfiguration4.setConfidential(false);
        enumKeyConfiguration4.setDescription("My key 4 description");
        enumKeyConfiguration4.setValidFrom(Instant.now().minus(24, ChronoUnit.HOURS));
        enumKeyConfiguration4.setValidTill(DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec.add(enumKeyConfiguration4);

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
        EnumConfiguration<? super EnumKeyConfiguration> ec1 = new EnumConfiguration<>("myName1");
        ec1.setDescription("My description 1");

        EnumKeyValueConfiguration enumKeyValueConfiguration1 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration1.setKey("myKey1");
        enumKeyValueConfiguration1.setConfidential(true);
        enumKeyValueConfiguration1.setDescription("My key 1 description");
        enumKeyValueConfiguration1.setExampleValue("example value 1");
        enumKeyValueConfiguration1.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        enumKeyValueConfiguration1.setValidFrom(null);
        enumKeyValueConfiguration1.setValidTill(null);
        ec1.add(enumKeyValueConfiguration1);

        EnumKeyValueConfiguration enumKeyValueConfiguration2 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration2.setKey("myKey2");
        enumKeyValueConfiguration2.setConfidential(true);
        enumKeyValueConfiguration2.setDescription("My key 2 description");
        enumKeyValueConfiguration2.setDefaultValue("default value 2");
        enumKeyValueConfiguration2.setExampleValue("example value 2");
        enumKeyValueConfiguration2.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        enumKeyValueConfiguration2.setValidFrom(null);
        enumKeyValueConfiguration2.setValidTill(null);
        ec1.add(enumKeyValueConfiguration2);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration3 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration3.setKey("myKey3");
        enumKeyValueConfiguration3.setConfidential(false);
        enumKeyValueConfiguration3.setDescription("My key 3 description");
        enumKeyValueConfiguration3.setDefaultValue("default value 3");
        enumKeyValueConfiguration3.setExampleValue("example value 3");
        enumKeyValueConfiguration3.setValidFrom(Instant.now().plus(24, ChronoUnit.HOURS));
        enumKeyValueConfiguration3.setValidTill(DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec1.add(enumKeyValueConfiguration3);

        EnumKeyValueConfiguration enumKeyValueConfiguration4 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration4.setKey("myKey4");
        enumKeyValueConfiguration4.setConfidential(false);
        enumKeyValueConfiguration4.setDescription("My key 4 description");
        enumKeyValueConfiguration4.setExampleValue("example value 4");
        enumKeyValueConfiguration4.setValidFrom(Instant.now().minus(24, ChronoUnit.HOURS));
        enumKeyValueConfiguration4.setValidTill(DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec1.add(enumKeyValueConfiguration4);

        EnumKeyConfiguration enumKeyConfiguration1 = new EnumKeyConfiguration();
        enumKeyConfiguration1.setKey("myKeyOnly");
        enumKeyConfiguration1.setConfidential(false);
        enumKeyConfiguration1.setDescription("My key only description");
        enumKeyConfiguration1.setValidFrom(Instant.now().minus(24, ChronoUnit.HOURS));
        enumKeyConfiguration1.setValidTill(DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec1.add(enumKeyConfiguration1);

        EnumConfigurations e = new EnumConfigurations();
        e.add(ec1);
        
        // 
        
        EnumConfiguration<? super EnumKeyConfiguration> ec2 = new EnumConfiguration<>("myName2");
        ec2.setDescription("My description 2");
        
        EnumKeyValueConfiguration enumKeyValueConfiguration5 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration5.setKey("myKey5");
        enumKeyValueConfiguration5.setConfidential(true);
        enumKeyValueConfiguration5.setDescription("My key 5 description");
        enumKeyValueConfiguration5.setExampleValue("example value 5");
        enumKeyValueConfiguration5.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        enumKeyValueConfiguration5.setValidFrom(null);
        enumKeyValueConfiguration5.setValidTill(null);
        ec2.add(enumKeyValueConfiguration5);

        EnumKeyValueConfiguration enumKeyValueConfiguration6 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration6.setKey("myKey6");
        enumKeyValueConfiguration6.setConfidential(true);
        enumKeyValueConfiguration6.setDescription("My key 6 description");
        enumKeyValueConfiguration6.setDefaultValue("default value 6");
        enumKeyValueConfiguration6.setExampleValue("example value 6");
        enumKeyValueConfiguration6.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality("0..1"));
        enumKeyValueConfiguration6.setValidFrom(null);
        enumKeyValueConfiguration6.setValidTill(null);
        ec2.add(enumKeyValueConfiguration6);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration7 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration7.setKey("myKey7");
        enumKeyValueConfiguration7.setConfidential(false);
        enumKeyValueConfiguration7.setDescription("My key 7 description");
        enumKeyValueConfiguration7.setDefaultValue("default value 7");
        enumKeyValueConfiguration7.setExampleValue("example value 7");
        enumKeyValueConfiguration7.setValidFrom(Instant.now().plus(24, ChronoUnit.HOURS));
        enumKeyValueConfiguration7.setValidTill(DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec2.add(enumKeyValueConfiguration7);

        EnumKeyValueConfiguration enumKeyValueConfiguration8 = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration8.setKey("myKey8");
        enumKeyValueConfiguration8.setConfidential(false);
        enumKeyValueConfiguration8.setDescription("My key 8 description");
        enumKeyValueConfiguration8.setExampleValue("example value 8");
        enumKeyValueConfiguration8.setValidFrom(Instant.now().minus(24, ChronoUnit.HOURS));
        enumKeyValueConfiguration8.setValidTill(DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec2.add(enumKeyValueConfiguration8);
        
        e.add(ec2);
        Assert.assertEquals(e, writeAndRead(e));

        ByteArrayOutputStream outputstream1 = new ByteArrayOutputStream();
        EnumConfigurationResourceFactory.getInstance().store(e, outputstream1);
        ByteArrayOutputStream outputstream2 = new ByteArrayOutputStream();
        EnumConfigurationResourceFactory.getInstance().store(writeAndRead(e), outputstream2);
        Assert.assertEquals(new String(outputstream1.toByteArray()), new String(outputstream2.toByteArray()));
        
        EnumConfiguration<? super EnumKeyConfiguration> eref1 = new EnumConfiguration<>(ec1.getName());
        eref1.setDescription(ec1.getDescription());
        eref1.setValidFrom(ec1.getValidFrom());
        eref1.setValidTill(ec1.getValidTill());
        eref1.add(enumKeyValueConfiguration3);     
        eref1.add(enumKeyValueConfiguration4);     
        eref1.add(enumKeyConfiguration1);     
        Set<EnumConfiguration<? super EnumKeyValueConfiguration>> set1 = new LinkedHashSet<>();
        set1.add(eref1);

        EnumConfiguration<EnumKeyValueConfiguration> eref2 = new EnumConfiguration<EnumKeyValueConfiguration>(ec2.getName());
        eref2.setDescription(ec2.getDescription());
        eref2.setValidFrom(ec2.getValidFrom());
        eref2.setValidTill(ec2.getValidTill());
        eref2.add(enumKeyValueConfiguration7);     
        eref2.add(enumKeyValueConfiguration8);     
        set1.add(eref2);

        EnumConfiguration<EnumKeyValueConfiguration> eref3 = new EnumConfiguration<EnumKeyValueConfiguration>(ec1.getName());
        eref3.setDescription(ec1.getDescription());
        eref3.setValidFrom(ec1.getValidFrom());
        eref3.setValidTill(ec1.getValidTill());
        eref3.add(enumKeyValueConfiguration4);
        Set<EnumConfiguration<EnumKeyValueConfiguration>> set2 = new LinkedHashSet<EnumConfiguration<EnumKeyValueConfiguration>>();
        set2.add(eref3);
        
        EnumConfiguration<EnumKeyValueConfiguration> eref4 = new EnumConfiguration<EnumKeyValueConfiguration>(ec2.getName());
        eref4.setDescription(ec2.getDescription());
        eref4.setValidFrom(ec2.getValidFrom());
        eref4.setValidTill(ec2.getValidTill());
        eref4.add(enumKeyValueConfiguration8);
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
        //out.println("==>"+new String(outputstream.toByteArray()));
        return EnumConfigurationResourceFactory.getInstance().load(inputStream);
    }
}
