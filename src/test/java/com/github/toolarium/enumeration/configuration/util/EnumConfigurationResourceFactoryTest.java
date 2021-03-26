/*
 * EnumConfigurationResourceFactoryTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
        enumValueConfiguration1.setOptional(false);
        enumValueConfiguration1.setValidFrom(null);
        enumValueConfiguration1.setValidTill(null);
        ec.add(enumValueConfiguration1);
        
        EnumValueConfiguration enumValueConfiguration2 = new EnumValueConfiguration();
        enumValueConfiguration2.setKey("myKey2");
        enumValueConfiguration2.setConfidential(false);
        enumValueConfiguration2.setDescription("My key 2 description");
        enumValueConfiguration2.setDefaultValue("default value 2");
        enumValueConfiguration2.setOptional(true);
        enumValueConfiguration2.setValidFrom(Instant.now().plus(24, ChronoUnit.HOURS));
        enumValueConfiguration2.setValidTill(DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec.add(enumValueConfiguration2);

        EnumValueConfiguration enumValueConfiguration3 = new EnumValueConfiguration();
        enumValueConfiguration3.setKey("myKey3");
        enumValueConfiguration3.setConfidential(false);
        enumValueConfiguration3.setDescription("My key 3 description");
        enumValueConfiguration3.setDefaultValue("default value 3");
        enumValueConfiguration3.setOptional(true);
        enumValueConfiguration3.setValidFrom(Instant.now().minus(24, ChronoUnit.HOURS));
        enumValueConfiguration3.setValidTill(DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec.add(enumValueConfiguration3);

        e.add(ec);
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
