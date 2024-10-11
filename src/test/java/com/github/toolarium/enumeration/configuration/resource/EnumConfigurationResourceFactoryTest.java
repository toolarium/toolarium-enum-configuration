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
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.processor.EnumConfigurationProcessor;
import com.github.toolarium.enumeration.configuration.util.AnnotationConvertUtil;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import com.github.toolarium.enumeration.configuration.validation.EnumKeyConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumKeyValueConfigurationValueValidatorFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test the EnumConfigurationResourceFactory
 * 
 * @author patrick
 */
public class EnumConfigurationResourceFactoryTest {
    private static final String OPTIONAL_CARDINALITY = "0..1";


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
        
        EnumKeyValueConfiguration enumKeyValueConfiguration1 = create("myKey1", true, "My key 1 description", null, "example value 1", OPTIONAL_CARDINALITY, null, null);
        ec.add(enumKeyValueConfiguration1);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration2 = create("myKey2", true, "My key 2 desc.", "default value 2", null, OPTIONAL_CARDINALITY, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec.add(enumKeyValueConfiguration2);

        EnumKeyValueConfiguration enumKeyValueConfiguration3 = create("myKey3", false, "My key 3 description", "default value 3", null, null, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec.add(enumKeyValueConfiguration3);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration4 = create("myKey4", false, "My key 4 description", null, "example value 4", null, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec.add(enumKeyValueConfiguration4);

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

        EnumKeyValueConfiguration enumKeyValueConfiguration1 = create("myKey1", true, "My key 1 description", null, "example value 1", OPTIONAL_CARDINALITY, null, null);
        ec1.add(enumKeyValueConfiguration1);

        EnumKeyValueConfiguration enumKeyValueConfiguration2 = create("myKey2", true, "My key 2 description", "default value 2", "example value 2", OPTIONAL_CARDINALITY, null, null);
        ec1.add(enumKeyValueConfiguration2);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration3 = create("myKey3", false, "My key 3 description", "default value 3", "example value 3", null, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec1.add(enumKeyValueConfiguration3);

        EnumKeyValueConfiguration enumKeyValueConfiguration4 = create("myKey4", false, "My key 4 description", null, "example value 4", null, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec1.add(enumKeyValueConfiguration4);

        EnumKeyConfiguration enumKeyConfiguration1 = create("myKeyOnly", false, "My key only description", Instant.now().minus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec1.add(enumKeyConfiguration1);

        EnumConfigurations e = new EnumConfigurations();
        e.add(ec1);
        
        //
        
        EnumConfiguration<? super EnumKeyConfiguration> ec2 = new EnumConfiguration<>("myName2");
        ec2.setDescription("My description 2");
        
        EnumKeyValueConfiguration enumKeyValueConfiguration5 = create("myKey5", true, "My key 5 description", null, "example value 5", OPTIONAL_CARDINALITY, null, null);
        ec2.add(enumKeyValueConfiguration5);

        EnumKeyValueConfiguration enumKeyValueConfiguration6 = create("myKey6", true, "My key 6 description", "default value 6", "example value 6", OPTIONAL_CARDINALITY, null, null);
        ec2.add(enumKeyValueConfiguration6);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration7 = create("myKey7", true, "My key 7 description", "default value 7", "example value 7", null, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec2.add(enumKeyValueConfiguration7);

        EnumKeyValueConfiguration enumKeyValueConfiguration8 = create("myKey8", true, "My key 8 description", null, "example value 8", null, Instant.now().minus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
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
     * Test empty enum configurations
     * 
     * @throws IOException in case of an error
     */
    @Test
    public void testInterface() throws IOException {
        String interfaceA = "this.is.my.interface";
        String markerInterfaceA = "this.is.my.markerinterface";
        
        EnumConfiguration<? super EnumKeyConfiguration> ec1 = new EnumConfiguration<>("myName1");
        ec1.setDescription("My description 1");
        ec1.setInterfaceList(Set.of(interfaceA));
        ec1.setMarkerInterfaceList(Set.of(markerInterfaceA));

        EnumKeyValueConfiguration enumKeyValueConfiguration1 = create("myKey1", true, "My key 1 description", null, "example value 1", OPTIONAL_CARDINALITY, null, null);
        ec1.add(enumKeyValueConfiguration1);

        EnumKeyValueConfiguration enumKeyValueConfiguration2 = create("myKey2", true, "My key 2 description", "default value 2", "example value 2", OPTIONAL_CARDINALITY, null, null);
        ec1.add(enumKeyValueConfiguration2);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration3 = create("myKey3", false, "My key 3 description", "default value 3", "example value 3", null, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec1.add(enumKeyValueConfiguration3);

        EnumKeyValueConfiguration enumKeyValueConfiguration4 = create("myKey4", false, "My key 4 description", null, "example value 4", null, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec1.add(enumKeyValueConfiguration4);

        EnumKeyConfiguration enumKeyConfiguration1 = create("myKeyOnly", false, "My key only description", Instant.now().minus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec1.add(enumKeyConfiguration1);

        EnumConfigurations e = new EnumConfigurations();
        e.add(ec1);

        // 
        
        EnumConfiguration<? super EnumKeyConfiguration> ec2 = new EnumConfiguration<>("myName2");
        ec2.setDescription("My description 2");
        ec2.setInterfaceList(Set.of(interfaceA));
        ec2.setMarkerInterfaceList(Set.of(markerInterfaceA));

        EnumKeyValueConfiguration enumKeyValueConfiguration5 = create("myKey5", true, "My key 5 description", null, "example value 5", OPTIONAL_CARDINALITY, null, null);
        ec2.add(enumKeyValueConfiguration5);

        EnumKeyValueConfiguration enumKeyValueConfiguration6 = create("myKey6", true, "My key 6 description", "default value 6", "example value 6", OPTIONAL_CARDINALITY, null, null);
        ec2.add(enumKeyValueConfiguration6);
        e.add(ec2);
        
        //
        
        String interfaceB = "this.is.my.second.interface";
        String markerInterfaceB = "this.is.my.second.markerinterface";
        
        EnumConfiguration<? super EnumKeyConfiguration> ec3 = new EnumConfiguration<>("myName3");
        ec3.setDescription("My description 3");
        ec3.setInterfaceList(Set.of(interfaceB));
        ec3.setMarkerInterfaceList(Set.of(markerInterfaceB));

        EnumKeyValueConfiguration enumKeyValueConfiguration7 = create("myKey7", true, "My key 7 description", null, "example value 7", OPTIONAL_CARDINALITY, null, null);
        ec3.add(enumKeyValueConfiguration7);

        EnumKeyValueConfiguration enumKeyValueConfiguration8 = create("myKey8", true, "My key 8 description", "default value 8", "example value 8", OPTIONAL_CARDINALITY, null, null);
        ec3.add(enumKeyValueConfiguration8);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration9 = create("myKey9", true, "My key 9 description", "default value 9", "example value 9", null, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec3.add(enumKeyValueConfiguration9);

        EnumKeyValueConfiguration enumKeyValueConfiguration10 = create("myKey10", true, "My key 10 description", null, "example value 10", null, Instant.now().minus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec3.add(enumKeyValueConfiguration10);
        
        e.add(ec3);
        Assert.assertEquals(e, writeAndRead(e));

        assertEquals("[myName1, myName2]", e.selectEnumConfigurationByInterfaceList(Set.of(interfaceA), false).stream().map(p -> p.getName()).collect(Collectors.toList()).toString());
        assertEquals("[myName1, myName2]", e.selectEnumConfigurationByInterfaceList(Set.of(markerInterfaceA), true).stream().map(p -> p.getName()).collect(Collectors.toList()).toString());
        assertEquals("[myName3]", e.selectEnumConfigurationByInterfaceList(Set.of(interfaceB), false).stream().map(p -> p.getName()).collect(Collectors.toList()).toString());
        assertEquals("[myName3]", e.selectEnumConfigurationByInterfaceList(Set.of(markerInterfaceB), true).stream().map(p -> p.getName()).collect(Collectors.toList()).toString());
        
        String testPath = "build";
        File file = Paths.get(testPath, EnumConfigurationProcessor.TOOLARIUM_ENUM_CONFIGURATION_JSON_FILENAME).toFile();
        EnumConfigurationResourceFactory.getInstance().store(e, new FileOutputStream(file));
        assertEquals(e, EnumConfigurationResourceFactory.getInstance().load(testPath, EnumConfigurationProcessor.TOOLARIUM_ENUM_CONFIGURATION_JSON_FILENAME));
        assertEquals(e, EnumConfigurationResourceFactory.getInstance().load(testPath));
        file.delete();
    }

    
    /**
     * Validate sample 
     * 
     * @throws ValidationException in case of a validation error
     * @throws IOException In case of an I/O error 
     */
    @Test
    void validateEnumKeyValueConfigurationWithNumber() throws ValidationException, IOException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        EnumKeyValueConfigurationSizing<Integer> cardinality = new EnumKeyValueConfigurationSizing<Integer>(1, 1);
        enumKeyValueConfiguration.setCardinality(cardinality);
        enumKeyValueConfiguration.setConfidential(false);
        enumKeyValueConfiguration.setKey("DELAY");
        enumKeyValueConfiguration.setDefaultValue("2");
        enumKeyValueConfiguration.setDescription("Defines the delay of an echo.");
        enumKeyValueConfiguration.setEnumerationValue("");
        enumKeyValueConfiguration.setExampleValue("2");
        enumKeyValueConfiguration.setValidFrom(Instant.now());
        enumKeyValueConfiguration.setValidTill(DateUtil.MAX_TIMESTAMP);
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.NUMBER);
        //EnumKeyValueConfigurationSizing<?> valueSize = new EnumKeyValueConfigurationSizing<Long>(0L, 10L); // define long value
        EnumKeyValueConfigurationSizing<?> valueSize = EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(enumKeyValueConfiguration.getDataType(), "0", "10"); 
        enumKeyValueConfiguration.setValueSize(valueSize);
        EnumKeyConfigurationValidatorFactory.getInstance().getValidator().validate(enumKeyValueConfiguration);

        // persiste and store 
        EnumConfiguration<EnumKeyValueConfiguration> enumConfiguration = new EnumConfiguration<EnumKeyValueConfiguration>("sample");
        enumConfiguration.add(enumKeyValueConfiguration);
        EnumConfigurations enumConfigurations = new EnumConfigurations();
        enumConfigurations.add(enumConfiguration);
        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        EnumConfigurationResourceFactory.getInstance().store(enumConfigurations, outputstream);
        
        // restore
        EnumConfigurations restoredEnumConfigurations =  EnumConfigurationResourceFactory.getInstance().load(new ByteArrayInputStream(outputstream.toByteArray()));
        @SuppressWarnings("unchecked")
        EnumConfiguration<EnumKeyValueConfiguration> restoredEnumConfiguration = (EnumConfiguration<EnumKeyValueConfiguration>)restoredEnumConfigurations.get("sample");
        
        EnumKeyValueConfiguration restoredEnumKeyValueConfiguration = restoredEnumConfiguration.getKeyList().iterator().next();
        //@SuppressWarnings("unchecked")
        //EnumKeyValueConfigurationSizing<Long> restoredValueSize = (EnumKeyValueConfigurationSizing<Long>)restoredEnumKeyValueConfiguration.getValueSize();
        // differences: integer
        //assertEquals(valueSize.getMaxSize().getClass(), restoredValueSize.getMaxSize().getClass());
        EnumKeyConfigurationValidatorFactory.getInstance().getValidator().validate(restoredEnumKeyValueConfiguration);
    }

    
    /**
     * Validate sample 
     * 
     * @throws ValidationException in case of a validation error
     */
    @Test
    void validateEnumKeyValueConfigurationWithNumber2() throws ValidationException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        EnumKeyValueConfigurationSizing<Integer> cardinality = new EnumKeyValueConfigurationSizing<Integer>(1, 1);
        enumKeyValueConfiguration.setCardinality(cardinality);
        enumKeyValueConfiguration.setConfidential(false);
        enumKeyValueConfiguration.setKey("DELAY");
        enumKeyValueConfiguration.setDefaultValue("2");
        enumKeyValueConfiguration.setDescription("Defines the delay of an echo.");
        enumKeyValueConfiguration.setEnumerationValue("");
        enumKeyValueConfiguration.setExampleValue("2");
        enumKeyValueConfiguration.setValidFrom(Instant.now());
        enumKeyValueConfiguration.setValidTill(DateUtil.MAX_TIMESTAMP);
        
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.NUMBER);
        EnumKeyValueConfigurationSizing<?> valueSize = new EnumKeyValueConfigurationSizing<Integer>(0, 10); //define Integer value
        enumKeyValueConfiguration.setValueSize(valueSize);

        EnumKeyConfigurationValidatorFactory.getInstance().getValidator().validate(enumKeyValueConfiguration);
    }

    
    /**
     * Test empty enum configurations
     * 
     * @throws IOException in case of an error
     */
    @Test
    public void testTagInEnumConfigurations() throws IOException {
        EnumConfiguration<? super EnumKeyConfiguration> ec = new EnumConfiguration<>("myName");
        ec.setDescription("My description");
        
        EnumKeyValueConfiguration enumKeyValueConfiguration1 = create("myKey1", true, "My key 1 description", null, "example value 1", OPTIONAL_CARDINALITY, null, null);
        ec.add(enumKeyValueConfiguration1);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration2 = create("myKey2", true, "My key 2 desc.", "default value 2", null, OPTIONAL_CARDINALITY, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec.add(enumKeyValueConfiguration2);

        EnumKeyValueConfiguration enumKeyValueConfiguration3 = create("myKey3", false, "My key 3 description", "default value 3", null, null, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.minus(24, ChronoUnit.HOURS));
        ec.add(enumKeyValueConfiguration3);
        
        EnumKeyValueConfiguration enumKeyValueConfiguration4 = create("myKey4", false, "My key 4 description", null, "example value 4", null, Instant.now().plus(24, ChronoUnit.HOURS), DateUtil.MAX_TIMESTAMP.plus(1, ChronoUnit.HOURS));
        ec.add(enumKeyValueConfiguration4);

        EnumConfigurations e = new EnumConfigurations();
        e.add(ec);

        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        EnumConfigurationResourceFactory.getInstance().store(e, outputstream);
        Assert.assertEquals(outputstream.toString().indexOf("    \"tag\""), -1); // there is no tag in output

        EnumConfigurations e2 = EnumConfigurationResourceFactory.getInstance().load(new ByteArrayInputStream(outputstream.toByteArray()));
        Assert.assertEquals(e, e2);
        Assert.assertNull(e2.getEnumConfigurationList().iterator().next().getTag());
        
        String tag = "myTag";
        ec.setTag(tag);
        outputstream = new ByteArrayOutputStream();
        EnumConfigurationResourceFactory.getInstance().store(e, outputstream);
        
        Assert.assertTrue(outputstream.toString().indexOf("    \"tag\" : \"" + tag + "\",") > 0); // there is a tag

        e2 = EnumConfigurationResourceFactory.getInstance().load(new ByteArrayInputStream(outputstream.toByteArray()));
        Assert.assertEquals(e, e2);
        Assert.assertEquals(tag, e2.getEnumConfigurationList().iterator().next().getTag());
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

    
    /**
     * Create an enumeration key configuration
     *
     * @param key the key
     * @param confidential confidential
     * @param description the description
     * @param validFrom the valid from
     * @param validTill the valid till
     * @return the enumeration key / value configuration
     */
    private EnumKeyConfiguration create(String key, boolean confidential, String description, Instant validFrom, Instant validTill) {
        EnumKeyConfiguration enumKeyConfiguration = new EnumKeyConfiguration();
        enumKeyConfiguration.setKey(key);
        enumKeyConfiguration.setConfidential(confidential);
        enumKeyConfiguration.setDescription(description);
        enumKeyConfiguration.setValidFrom(validFrom);
        enumKeyConfiguration.setValidTill(validTill);
        return enumKeyConfiguration;
    }

    
    /**
     * Create an enumeration key / value configuration
     *
     * @param key the key
     * @param confidential confidential
     * @param description the description
     * @param defaultValue the default value
     * @param exampleValue the example value
     * @param cardinality the cardinality
     * @param validFrom the valid from
     * @param validTill the valid till
     * @return the enumeration key / value configuration
     */
    private EnumKeyValueConfiguration create(String key, boolean confidential, String description, String defaultValue, String exampleValue, String cardinality, Instant validFrom, Instant validTill) {
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setKey(key);
        enumKeyValueConfiguration.setConfidential(confidential);
        enumKeyValueConfiguration.setDescription(description);
        enumKeyValueConfiguration.setDefaultValue(defaultValue);       
        enumKeyValueConfiguration.setExampleValue(exampleValue);
        
        if (cardinality != null) {
            enumKeyValueConfiguration.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality(cardinality));
        }
        
        enumKeyValueConfiguration.setValidFrom(validFrom);
        enumKeyValueConfiguration.setValidTill(validTill);
        return enumKeyValueConfiguration;
    }
}
