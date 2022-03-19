/*
 * EnumConfigurationStoreExternalDefinitionTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.processor.MyEnumConfiguration;
import com.github.toolarium.enumeration.configuration.resource.EnumConfigurationResourceFactory;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import com.github.toolarium.enumeration.configuration.store.impl.EnumConfigurationResourceResolver;
import com.github.toolarium.enumeration.configuration.store.impl.PropertiesEnumConfigurationStore;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import com.github.toolarium.enumeration.configuration.util.JSONUtil;
import com.github.toolarium.enumeration.configuration.validation.EnumKeyConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumKeyValueConfigurationValueValidatorFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test the {@link IEnumConfigurationStore} with external enum configuration.
 *  
 * @author patrick
 */
public class EnumConfigurationStoreExternalDefinitionTest implements IEnumConfigurationStoreConstants {
    private static final String ENUM_CONFIGURATION_KEY_NAME = "DELAY";
    private static final String CLASSNAME = "Sample";
    private static final String DEFAULT_VALUE = "2";
    private static final String ZERO = "0";
    private static final String TEN = "10";
    
    private static final String INVALID_ENUM_KEY_CONFIGURATION = "]: Invalid enumKeyConfiguration!";
    private static final Logger LOG = LoggerFactory.getLogger(EnumConfigurationStoreExternalDefinitionTest.class);

    
    /**
     * Test the enum configuration loading and validation 
     *
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    @Test
    public void readWriteDataEnumConfiguration() throws ValidationException, IOException {
        // Initialize the properties configuration store with external source
        EnumKeyValueConfiguration enumKeyValueConfiguration = createEnumKeyValueConfiguration(ENUM_CONFIGURATION_KEY_NAME, EnumKeyValueConfigurationDataType.NUMBER, ZERO, TEN, DEFAULT_VALUE, DEFAULT_VALUE); 
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(createEnumConfigurationMock(CLASSNAME, enumKeyValueConfiguration));
        
        String configurationKeyName = configurationStore.getEnumConfigurationKeyResolver().createConfigurationKeyName(CLASSNAME, ENUM_CONFIGURATION_KEY_NAME);
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(MyEnumConfiguration.ARRAY_SAMPLE));
        assertEquals(DEFAULT_VALUE, configurationStore.readConfigurationValue(configurationKeyName).toString());

        // read not existing value
        EnumConfigurationStoreException exception1 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "your-host");
        });
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + "]: [input] Invalid value [your-host], it can not be converted into a NUMBER data type.", exception1.getMessage());
        
        configurationStore.writeConfigurationValue(configurationKeyName, "3");

        // get back and verify
        IEnumConfigurationValue<String> value = configurationStore.readConfigurationValue(configurationKeyName);
        assertNotNull(value);
        assertEquals("3", value.toString());
        assertEquals(Long.valueOf(3), value.getValue());
        assertEquals("[3]", value.getValueList().toString());

        configurationStore.writeConfigurationValue(configurationKeyName, "4");

        // get back and verify
        value = configurationStore.readConfigurationValue(configurationKeyName);
        assertNotNull(value);
        assertEquals("4", value.toString());
        assertEquals(Long.valueOf(4), value.getValue());
        assertEquals("[4]", value.getValueList().toString());
    }

    
    /**
     * Test invalid class of the enum configuration 
     *
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    @Test
    public void readWriteInvalidArray() throws ValidationException, IOException {
        // Initialize the properties configuration store with external source
        EnumKeyValueConfiguration enumKeyValueConfiguration = createEnumKeyValueConfiguration(ENUM_CONFIGURATION_KEY_NAME, EnumKeyValueConfigurationDataType.NUMBER, ZERO, TEN, DEFAULT_VALUE, DEFAULT_VALUE);
        enumKeyValueConfiguration.setCardinality(new EnumKeyValueConfigurationSizing<Integer>(2, 4));
        
        enumKeyValueConfiguration.setDefaultValue(JSONUtil.getInstance().convert(Arrays.asList("1", "2")));
        enumKeyValueConfiguration.setExampleValue(JSONUtil.getInstance().convert(Arrays.asList("1", "2")));
        
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(createEnumConfigurationMock(CLASSNAME, enumKeyValueConfiguration));

        String configurationKeyName = configurationStore.getEnumConfigurationKeyResolver().createConfigurationKeyName(CLASSNAME, ENUM_CONFIGURATION_KEY_NAME);
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(MyEnumConfiguration.ARRAY_SAMPLE));
        assertEquals(enumKeyValueConfiguration.getDefaultValue(), configurationStore.readConfigurationValue(configurationKeyName).toString());

        EnumConfigurationStoreException exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "6");
        });
        // Invalid configuration found for key [sample#delay]: [input] Invalid cardinality of [input], the minSize is [2].
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + "]: [input] Invalid cardinality of [input], the minSize is [2].", exception.getMessage());

        configurationStore.writeConfigurationValue(configurationKeyName, "[ 6, 7 ]");
    }

    
    /**
     * Test invalid enunm key name of the enum configuration 
     *
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    @Test
    public void readWriteDataInvalidSourceEnumConfiguration() throws ValidationException, IOException {
        // Initialize the properties configuration store with external source
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(new EnumConfigurationResourceResolver(new ByteArrayInputStream(new byte[] {})));
        
        String configurationKeyName = configurationStore.getEnumConfigurationKeyResolver().createConfigurationKeyName(CLASSNAME, ENUM_CONFIGURATION_KEY_NAME);
        assertNull(configurationStore.readConfigurationValue(configurationKeyName));
        
        // read not existing value
        EnumConfigurationStoreException exception1 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "other-host");
        });
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + INVALID_ENUM_KEY_CONFIGURATION, exception1.getMessage());

        EnumConfigurationStoreException exception2 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "5");
        });
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + INVALID_ENUM_KEY_CONFIGURATION, exception2.getMessage());
    }

    
    /**
     * Test invalid enunm key name of the enum configuration 
     *
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    @Test
    public void readWriteDataInvalidSourceContentEnumConfiguration() throws ValidationException, IOException {
        // Initialize the properties configuration store with external source
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(new EnumConfigurationResourceResolver(new ByteArrayInputStream("abcd = gg".getBytes())));
        
        String configurationKeyName = configurationStore.getEnumConfigurationKeyResolver().createConfigurationKeyName(CLASSNAME, ENUM_CONFIGURATION_KEY_NAME);
        assertNull(configurationStore.readConfigurationValue(configurationKeyName));
        
        // read not existing value
        EnumConfigurationStoreException exception1 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "other-host");
        });
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + INVALID_ENUM_KEY_CONFIGURATION, exception1.getMessage());

        EnumConfigurationStoreException exception2 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "5");
        });
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + INVALID_ENUM_KEY_CONFIGURATION, exception2.getMessage());
    }

    
    /**
     * Test invalid enunm key name of the enum configuration 
     *
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    @Test
    public void readWriteDataInvalidEnumKeyNameEnumConfiguration() throws ValidationException, IOException {
        // Initialize the properties configuration store with external source
        EnumKeyValueConfiguration enumKeyValueConfiguration = createEnumKeyValueConfiguration(ENUM_CONFIGURATION_KEY_NAME + "Unknown", EnumKeyValueConfigurationDataType.NUMBER, ZERO, TEN, DEFAULT_VALUE, DEFAULT_VALUE); 
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(createEnumConfigurationMock(CLASSNAME, enumKeyValueConfiguration));

        String configurationKeyName = configurationStore.getEnumConfigurationKeyResolver().createConfigurationKeyName(CLASSNAME, ENUM_CONFIGURATION_KEY_NAME);
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(configurationKeyName));
        assertNull(configurationStore.readConfigurationValue(configurationKeyName));
        
        // read not existing value
        EnumConfigurationStoreException exception1 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "other-host");
        });
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + INVALID_ENUM_KEY_CONFIGURATION, exception1.getMessage());

        EnumConfigurationStoreException exception2 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "5");
        });
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + INVALID_ENUM_KEY_CONFIGURATION, exception2.getMessage());
    }

    
    /**
     * Test invalid class of the enum configuration 
     *
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    @Test
    public void readWriteDataInvalidClassEnumConfiguration() throws ValidationException, IOException {
        // Initialize the properties configuration store with external source
        EnumKeyValueConfiguration enumKeyValueConfiguration = createEnumKeyValueConfiguration(ENUM_CONFIGURATION_KEY_NAME, EnumKeyValueConfigurationDataType.NUMBER, ZERO, TEN, DEFAULT_VALUE, DEFAULT_VALUE); 
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(createEnumConfigurationMock(CLASSNAME + "Unknown", enumKeyValueConfiguration));
        
        String configurationKeyName = configurationStore.getEnumConfigurationKeyResolver().createConfigurationKeyName(CLASSNAME, ENUM_CONFIGURATION_KEY_NAME);
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(configurationKeyName));
        assertNull(configurationStore.readConfigurationValue(configurationKeyName));
        
        // read not existing value
        EnumConfigurationStoreException exception1 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "other-host");
        });
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + INVALID_ENUM_KEY_CONFIGURATION, exception1.getMessage());

        EnumConfigurationStoreException exception2 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "5");
        });
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + INVALID_ENUM_KEY_CONFIGURATION, exception2.getMessage());
    }

    
    /**
     * Test invalid class of the enum configuration 
     *
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    @Test
    public void readWriteDataInvalidTypeEnumConfiguration() throws ValidationException, IOException {
        // Initialize the properties configuration store with external source
        EnumKeyValueConfiguration enumKeyValueConfiguration = createEnumKeyValueConfiguration(ENUM_CONFIGURATION_KEY_NAME, EnumKeyValueConfigurationDataType.NUMBER, ZERO, TEN, DEFAULT_VALUE, DEFAULT_VALUE); 
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(createEnumConfigurationMock(CLASSNAME, enumKeyValueConfiguration));
        
        String configurationKeyName = configurationStore.getEnumConfigurationKeyResolver().createConfigurationKeyName(CLASSNAME, ENUM_CONFIGURATION_KEY_NAME);
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(configurationKeyName));
        assertEquals(enumKeyValueConfiguration.getDefaultValue(), configurationStore.readConfigurationValue(configurationKeyName).toString());
        
        configurationStore.writeConfigurationValue(configurationKeyName, "6");

        Properties prop = configurationStore.getProperties(); 
        prop.setProperty("sample#delay", "7");
        configurationStore.setProperties(prop);
        
        assertEquals(Long.valueOf(7), configurationStore.readConfigurationValue(configurationKeyName).getValue());

        prop.setProperty("sample#delay", "a");
        configurationStore.setProperties(prop);
        EnumConfigurationStoreException exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.readConfigurationValue(configurationKeyName).getValue();
        });
        
        // Invalid configuration found for key [sample#delay]: [input] Invalid value [a], it can not be converted into a NUMBER data type.
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + "]: [input] Invalid value [a], it can not be converted into a NUMBER data type.", exception.getMessage());
    }

    
    /**
     * Test invalid class of the enum configuration 
     *
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    @Test
    public void readWriteDataArrayEnumConfiguration() throws ValidationException, IOException {
        // Initialize the properties configuration store with external source
        EnumKeyValueConfiguration enumKeyValueConfiguration = createEnumKeyValueConfiguration(ENUM_CONFIGURATION_KEY_NAME, EnumKeyValueConfigurationDataType.NUMBER, ZERO, TEN, DEFAULT_VALUE, DEFAULT_VALUE);
        enumKeyValueConfiguration.setCardinality(new EnumKeyValueConfigurationSizing<Integer>(2, 4));
        
        enumKeyValueConfiguration.setDefaultValue(JSONUtil.getInstance().convert(Arrays.asList("1", "2")));
        enumKeyValueConfiguration.setExampleValue(JSONUtil.getInstance().convert(Arrays.asList("1", "2")));
        
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(createEnumConfigurationMock(CLASSNAME, enumKeyValueConfiguration));

        String configurationKeyName = configurationStore.getEnumConfigurationKeyResolver().createConfigurationKeyName(CLASSNAME, ENUM_CONFIGURATION_KEY_NAME);
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(configurationKeyName));
        assertEquals(enumKeyValueConfiguration.getDefaultValue(), configurationStore.readConfigurationValue(configurationKeyName).toString());

        EnumConfigurationStoreException exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(configurationKeyName, "6");
        });
        // Invalid configuration found for key [sample#delay]: [input] Invalid cardinality of [input], the minSize is [2].
        assertEquals(INVALID_CONFIGURATION_FOUND_FOR_KEY + configurationKeyName + "]: [input] Invalid cardinality of [input], the minSize is [2].", exception.getMessage());

        configurationStore.writeConfigurationValue(configurationKeyName, "[ 6, 7 ]");
        
        // get back and verify
        IEnumConfigurationValue<String> value = configurationStore.readConfigurationValue(configurationKeyName);
        assertNotNull(value);
        assertEquals("[ 6, 7 ]", value.toString());
        assertEquals(Long.valueOf(6), value.getValue());
        assertEquals("[6, 7]", value.getValueList().toString());
        assertEquals(Long.valueOf(7), value.getValue());
        
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            value.getValue();
        });
        
        configurationStore.writeConfigurationValue(configurationKeyName, "[ \"6\", \"7\" ]");
        
        // get back and verify
        IEnumConfigurationValue<String> value2 = configurationStore.readConfigurationValue(configurationKeyName);
        assertNotNull(value2);
        assertEquals("[ \"6\", \"7\" ]", value2.toString());
        assertEquals(Long.valueOf(6), value2.getValue());
        assertEquals("[6, 7]", value2.getValueList().toString());
        assertEquals(Long.valueOf(7), value2.getValue());
        
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            value2.getValue();
        });
    }
    
    
    /**
     * Create the enum configuration mock
     *
     * @param className the class name
     * @param enumKeyValueConfiguration the enum key value configuration
     * @return the input stream
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    private IEnumConfigurationResourceResolver createEnumConfigurationMock(String className, EnumKeyValueConfiguration enumKeyValueConfiguration) throws ValidationException, IOException {
        EnumConfiguration<EnumKeyValueConfiguration> enumConfiguration = new EnumConfiguration<EnumKeyValueConfiguration>(className);
        enumConfiguration.add(enumKeyValueConfiguration);
        EnumConfigurations enumConfigurations = new EnumConfigurations();
        enumConfigurations.add(enumConfiguration);
        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        EnumConfigurationResourceFactory.getInstance().store(enumConfigurations, outputstream);
        LOG.debug("Prepared " + enumConfigurations);
        return new EnumConfigurationResourceResolver(new ByteArrayInputStream(outputstream.toByteArray()));
    }


    /**
     * Create the enum configuration
     *
     * @param enumConfigurtionKeyName the key name
     * @param dataType the data type
     * @param minSize the min size
     * @param maxSize the max size
     * @param defaultValue the default size
     * @param exampleValue the example value
     * @return the input stream
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    private EnumKeyValueConfiguration createEnumKeyValueConfiguration(String enumConfigurtionKeyName, 
                                                                      EnumKeyValueConfigurationDataType dataType,
                                                                      String minSize,
                                                                      String maxSize,
                                                                      String defaultValue, 
                                                                      String exampleValue) 
            throws ValidationException, IOException {
        // 1) define enum configuration
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        EnumKeyValueConfigurationSizing<Integer> cardinality = new EnumKeyValueConfigurationSizing<Integer>(1, 1);
        enumKeyValueConfiguration.setCardinality(cardinality);
        enumKeyValueConfiguration.setConfidential(false);
        enumKeyValueConfiguration.setKey(enumConfigurtionKeyName);
        enumKeyValueConfiguration.setDefaultValue(defaultValue);
        enumKeyValueConfiguration.setDescription("The description.");
        enumKeyValueConfiguration.setEnumerationValue("");
        enumKeyValueConfiguration.setExampleValue(exampleValue);
        enumKeyValueConfiguration.setValidFrom(Instant.now());
        enumKeyValueConfiguration.setValidTill(DateUtil.MAX_TIMESTAMP);
        enumKeyValueConfiguration.setDataType(dataType);
        //EnumKeyValueConfigurationSizing<?> valueSize = new EnumKeyValueConfigurationSizing<Long>(0L, 10L); // define long value
        EnumKeyValueConfigurationSizing<?> valueSize = EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(enumKeyValueConfiguration.getDataType(), minSize, maxSize); 
        enumKeyValueConfiguration.setValueSize(valueSize);
        EnumKeyConfigurationValidatorFactory.getInstance().getValidator().validate(enumKeyValueConfiguration);
        return enumKeyValueConfiguration;
    }

}
