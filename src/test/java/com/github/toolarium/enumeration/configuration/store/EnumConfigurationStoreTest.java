/*
 * EnumConfigurationStoreTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration.DataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.IEnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.processor.MyEnumConfiguration;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import com.github.toolarium.enumeration.configuration.store.impl.EnumConfigurationKeyResolver;
import com.github.toolarium.enumeration.configuration.store.impl.PropertiesEnumConfigurationStore;
import com.github.toolarium.enumeration.configuration.util.EnumKeyValueConfigurationBinaryObjectParser;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link IEnumConfigurationStore} with local enum configuration.
 * 
 * @author patrick
 */
public class EnumConfigurationStoreTest extends AbstractEnumConfigurationStoreTest implements IEnumConfigurationStoreConstants {

    private static final String START_BRACKED = "[";
    private static final String END_BRACKED = "]";
    private static final String BRACE_START = "{";
    private static final String BRACE_END = "}";
    private static final String EQUALS = "=";
    private static final String SEPARATOR = ", ";
    
    // values
    private static final String MESSAGE_ENCODED = "ICBteSBmaWxlIGNvbnRlbnQgIA=="; // "  my file content  " 
    private static final String FIRST  = "com.github.toolarium.enumeration.configuration.store.enumconfigurationstoretest$simpleconfigtest#first";
    private static final String SECOND = "com.github.toolarium.enumeration.configuration.store.enumconfigurationstoretest$simpleconfigtest#second";
    private static final String DATE   = "com.github.toolarium.enumeration.configuration.store.enumconfigurationstoretest$simpleconfigtest#date";
    private static final String PORT   = "com.github.toolarium.enumeration.configuration.store.enumconfigurationstoretest$simpleconfigtest#port";
    private static final String VALUEF = "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#value_f";
    private static final String DELAY_PRECISION_A = "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#delay_precision_a";    
    
    
    /**
     * Test read, write and delete value
     */
    @Test
    public void readWriteDeleteConfigurationValue() {
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();
        
        // readConfigurationValue(String configurationKey)
        assertEquals(Long.valueOf(11), configurationStore.readConfigurationValue(SimpleConfigTest.FIRST).getValue());
        // readConfigurationValueIgnoreDefault(String configurationKey) 
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.FIRST));

        // readConfigurationValue(T configurationKey)
        assertEquals(Long.valueOf(11), configurationStore.readConfigurationValue(FIRST).getValue());
        // readConfigurationValueIgnoreDefault(T configurationKey)
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(FIRST));
        
        // writeConfigurationValue(T configurationKey, Object value)
        configurationStore.writeConfigurationValue(SimpleConfigTest.FIRST, 12);
        assertEquals(Long.valueOf(12), configurationStore.readConfigurationValue(SimpleConfigTest.FIRST).getValue());
        assertEquals(Long.valueOf(12), configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.FIRST).getValue());
        
        // writeConfigurationValue(T configurationKey, String value)
        configurationStore.writeConfigurationValue(SimpleConfigTest.FIRST, "13");
        assertEquals(Long.valueOf(13), configurationStore.readConfigurationValue(SimpleConfigTest.FIRST).getValue());
        assertEquals(Long.valueOf(13), configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.FIRST).getValue());

        // deleteConfigurationValue(T configurationKey)   
        assertEquals(Long.valueOf(13), configurationStore.deleteConfigurationValue(SimpleConfigTest.FIRST).getValue());
        assertEquals(Long.valueOf(11), configurationStore.readConfigurationValue(SimpleConfigTest.FIRST).getValue());
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.FIRST));

        // writeConfigurationValue(String configurationKey, String value)
        configurationStore.writeConfigurationValue(SimpleConfigTest.FIRST, 14);
        assertEquals(Long.valueOf(14), configurationStore.readConfigurationValue(FIRST).getValue());
        assertEquals(Long.valueOf(14), configurationStore.readConfigurationValueIgnoreDefault(FIRST).getValue());

        // deleteConfigurationValue(String configurationKey)   
        assertEquals(Long.valueOf(14), configurationStore.deleteConfigurationValue(FIRST).getValue());
        assertEquals(Long.valueOf(11), configurationStore.readConfigurationValue(SimpleConfigTest.FIRST).getValue());
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.FIRST));

        // void writeConfigurationValue(String configurationKey, Object value)
        configurationStore.writeConfigurationValue(SimpleConfigTest.FIRST, "15");
        assertEquals(Long.valueOf(15), configurationStore.readConfigurationValue(FIRST).getValue());
        assertEquals(Long.valueOf(15), configurationStore.readConfigurationValueIgnoreDefault(FIRST).getValue());
    }

    
    /**
     * Test empty strings: in this case getValue is not null but the internal value is null!
     */
    @Test
    public void testEmptyStrings() {
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();
        assertNull(configurationStore.readConfigurationValue(SimpleConfigTest.EMPTY_STRING));
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.EMPTY_STRING));
        
        // write empty string
        configurationStore.writeConfigurationValue(SimpleConfigTest.EMPTY_STRING, "");
        assertNull(configurationStore.readConfigurationValue(SimpleConfigTest.EMPTY_STRING).getValue());
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.EMPTY_STRING).getValue());

        // write empty string where the configuration has a default value
        assertEquals("a", configurationStore.readConfigurationValue(SimpleConfigTest.EMPTY_STRING_WITH_DEFAULT).getValue());
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.EMPTY_STRING_WITH_DEFAULT));
        configurationStore.writeConfigurationValue(SimpleConfigTest.EMPTY_STRING_WITH_DEFAULT, "");
        assertNull(configurationStore.readConfigurationValue(SimpleConfigTest.EMPTY_STRING_WITH_DEFAULT).getValue());
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.EMPTY_STRING_WITH_DEFAULT).getValue());
    }
    
    
    /**
     * Test read, write and delete value list
     */
    @Test
    public void readWriteDeleteConfigurationValueList() {
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();
        
        // Properties readConfigurationValueList(T[] configurationKeys)
        Properties properties = configurationStore.readConfigurationValueList(new SimpleConfigTest[] {SimpleConfigTest.PORT, SimpleConfigTest.DATE});
        assertEquals(BRACE_START + DATE + EQUALS + "2021-03-01" + SEPARATOR + PORT + EQUALS + BRACE_END, properties.toString());
        
        // Properties readConfigurationValueList(String... configurationKeys)
        properties = configurationStore.readConfigurationValueList(PORT, DATE);
        assertEquals(BRACE_START + DATE + EQUALS + "2021-03-01" + SEPARATOR + PORT + EQUALS + BRACE_END, properties.toString());

        // Properties readConfigurationValueListIgnoreDefault(T[] configurationKeys)
        properties = configurationStore.readConfigurationValueListIgnoreDefault(new SimpleConfigTest[] {SimpleConfigTest.PORT, SimpleConfigTest.DATE});
        assertEquals(BRACE_START + DATE + EQUALS + SEPARATOR + PORT + EQUALS + BRACE_END, properties.toString());
        
        // Properties readConfigurationValueListIgnoreDefault(String... configurationKeys)
        properties = configurationStore.readConfigurationValueListIgnoreDefault(PORT, DATE);
        assertEquals(BRACE_START + DATE + EQUALS + SEPARATOR + PORT + EQUALS + BRACE_END, properties.toString());

        // writeConfigurationValueList(Properties configuration)
        String port = "8082";
        String date = "2022-02-18";
        properties = new Properties();
        properties.setProperty(DATE, date); 
        properties.setProperty(PORT, port);
        configurationStore.writeConfigurationValueList(properties);
        
        properties = configurationStore.readConfigurationValueList(new SimpleConfigTest[] {SimpleConfigTest.PORT, SimpleConfigTest.DATE});
        assertEquals(EnumKeyValueConfigurationBinaryObjectParser.BRACE_START + DATE + EQUALS + date + SEPARATOR + PORT + EQUALS + port + BRACE_END, properties.toString());

        properties = configurationStore.readConfigurationValueListIgnoreDefault(new SimpleConfigTest[] {SimpleConfigTest.PORT, SimpleConfigTest.DATE});
        assertEquals(BRACE_START + DATE + EQUALS + date + SEPARATOR + PORT + EQUALS + port + BRACE_END, properties.toString());
        
        properties = configurationStore.readConfigurationValueList(PORT, DATE);
        assertEquals(BRACE_START + DATE + EQUALS + date + SEPARATOR + PORT + EQUALS + port + EnumKeyValueConfigurationBinaryObjectParser.BRACE_END, properties.toString());
       
        properties = configurationStore.readConfigurationValueListIgnoreDefault(PORT, DATE);
        assertEquals(BRACE_START + DATE + EQUALS + date + SEPARATOR + PORT + EQUALS + port + BRACE_END, properties.toString());

        properties = configurationStore.readConfigurationValueListIgnoreDefault(PORT, DATE);
        assertEquals(BRACE_START + DATE + EQUALS + date + SEPARATOR + PORT + EQUALS + port + BRACE_END, properties.toString());

        // Properties readConfigurationValueList(String... configurationKeys)
        properties = configurationStore.readConfigurationValueListIgnoreDefault(PORT, DATE);
        assertEquals(BRACE_START + DATE + EQUALS + date + SEPARATOR + PORT + EQUALS + port + BRACE_END, properties.toString());

        assertNull(configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.FIRST));
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.SECOND));

        properties = new Properties();
        properties.setProperty(FIRST, "12"); 
        properties.setProperty(SECOND, "13");
        properties = configurationStore.writeConfigurationValueList(properties, true);
        assertEquals(Long.valueOf(12), configurationStore.readConfigurationValue(SimpleConfigTest.FIRST).getValue());
        assertEquals(Long.valueOf(13), configurationStore.readConfigurationValue(SimpleConfigTest.SECOND).getValue());
        assertEquals(date, "" + properties.remove(DATE));
        assertEquals(port, "" + properties.remove(PORT));
        assertTrue(properties.isEmpty());

        // Properties deleteConfigurationValueList(T[] configurationKeys)
        properties = configurationStore.deleteConfigurationValueList(new SimpleConfigTest[] {SimpleConfigTest.FIRST, SimpleConfigTest.SECOND});
        assertEquals("12", "" + properties.remove(FIRST));
        assertEquals("13", "" + properties.remove(SECOND));
        assertTrue(properties.isEmpty());

        properties = new Properties();
        properties.setProperty(FIRST, "12"); 
        properties.setProperty(SECOND, "13");
        properties = configurationStore.writeConfigurationValueList(properties, true);
        assertTrue(properties.isEmpty());

        // Properties deleteConfigurationValueList(String... configurationKeys)
        properties = configurationStore.deleteConfigurationValueList(FIRST, SECOND);
        assertEquals("12", "" + properties.remove(FIRST));
        assertEquals("13", "" + properties.remove(SECOND));
        assertTrue(properties.isEmpty());
    }
    

    /**
     * Test string read / write
     */
    @Test
    public void readWriteSimpleString() {
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();
        
        // read not existing value
        assertNull(configurationStore.readConfigurationValueIgnoreDefault(SimpleConfigTest.HOSTNAME));
        assertEquals("hostname", configurationStore.readConfigurationValue(SimpleConfigTest.HOSTNAME).getValue());
        
        // write value
        configurationStore.writeConfigurationValue(SimpleConfigTest.HOSTNAME, "my-host");
        
        // get back and verify
        IEnumConfigurationValue<String> value = configurationStore.readConfigurationValue(SimpleConfigTest.HOSTNAME);
        assertNotNull(value);
        assertEquals("my-host", value.toString());
        assertEquals("my-host", value.getValue());
        assertEquals("[my-host]", value.getValueList().toString());
    }


    /**
     * Test number
     */
    @Test
    public void readWriteNumber() {
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();

        assertNull(configurationStore.readConfigurationValueIgnoreDefault(MyEnumConfiguration.VALUE_C));
        assertNull(configurationStore.readConfigurationValue(MyEnumConfiguration.VALUE_C));

        // read not existing value: data type
        EnumConfigurationStoreException exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_C, "test 1");
        });
        assertEquals(HEADER + "value_c]: [input] Invalid value [test 1], it can not be converted into a NUMBER data type.", exception.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_C) + END_BRACKED, exception.keySet().toString());
        assertEquals("test 1", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_C)).toString());
        
        // write value: invalid range
        exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_C, 10);
        });
        assertEquals(HEADER + "value_c]: [input] Too small: invalid size of [10], should be at least [100] (now 10)!", exception.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_C) + END_BRACKED, exception.keySet().toString());
        assertEquals("10", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_C)).toString());
        assertEquals(10L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_C)).getValue());

        configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_C, 101);

        // get back and verify
        IEnumConfigurationValue<String> value = configurationStore.readConfigurationValue(MyEnumConfiguration.VALUE_C);
        assertNotNull(value);
        assertEquals("101", value.toString());
        assertEquals(Long.valueOf(101), value.getValue());
        assertEquals("[101]", value.getValueList().toString());
        
        // write value: invalid cardinality
        exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_F, 10);
        });
        assertEquals(HEADER + "value_f]: [input] Invalid cardinality of [input], the min cardinality is [2].", exception.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F) + END_BRACKED, exception.keySet().toString());
        assertEquals("10", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).toString());
        assertEquals(10L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).getValue());

        // write and read value properly
        configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_F, "[ \"2\", \"3\" ]");
        Properties properties = configurationStore.readConfigurationValueList(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F));       
        assertEquals("[ \"2\", \"3\" ]", properties.get(VALUEF));
        
        // set invalid cardinality and read
        properties.setProperty(VALUEF, "[ \"2\", \"3\", \"4\", \"5\" ]");
        configurationStore.setProperties(properties);
        exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.readConfigurationValue(MyEnumConfiguration.VALUE_F);
        });
        
        assertEquals(HEADER + "value_f]: [input] Invalid cardinality of [input], the max cardinality Size is [3].", exception.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F) + END_BRACKED, exception.keySet().toString());
        assertEquals("[ \"2\", \"3\", \"4\", \"5\" ]", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).toString());
        assertEquals(2L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).getValue());
        assertEquals(3L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).getValue());
        assertEquals(4L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).getValue());
        assertEquals(5L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).getValue());
        
        // set invalid isUniqueness error
        properties.setProperty(VALUEF, "[ \"2\", \"3\", \"4\", \"4\" ]");
        configurationStore.setProperties(properties);
        exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.readConfigurationValue(MyEnumConfiguration.VALUE_F);
        });
        
        assertEquals(HEADER + "value_f]: [input] Invalid isUniqueness of [input] for intput [[ \"2\", \"3\", \"4\", \"4\" ]]. Value already exist!", exception.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F) + END_BRACKED, exception.keySet().toString());
        assertEquals("[ \"2\", \"3\", \"4\", \"4\" ]", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).toString());
        assertEquals(2L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).getValue());
        assertEquals(3L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).getValue());
        assertEquals(4L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).getValue());
        assertEquals(4L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_F)).getValue());
    }


    /**
     * Test number
     */
    @Test
    public void readWriteArray() {
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();

        assertNull(configurationStore.readConfigurationValueIgnoreDefault(MyEnumConfiguration.ARRAY_SAMPLE));
        assertEquals("[\"1\", \"2\" ]", configurationStore.readConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE).toString());

        // read not existing value: data type
        EnumConfigurationStoreException exception1 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, "test 2");
        });
                               
        assertEquals(HEADER + "array_sample]: [input] Invalid value [test 2], it can not be converted into a NUMBER data type.", exception1.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.ARRAY_SAMPLE) + END_BRACKED, exception1.keySet().toString());
        assertEquals("test 2", exception1.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.ARRAY_SAMPLE)).toString());

        // write value: invalid range
        EnumConfigurationStoreException exception2 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, 10);
        });
        assertEquals(HEADER + "array_sample]: [input] Invalid cardinality of [input], the min cardinality is [2].", exception2.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.ARRAY_SAMPLE) + END_BRACKED, exception2.keySet().toString());
        assertEquals("10", exception2.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.ARRAY_SAMPLE)).toString());

        // cardinality
        configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, new Long[] {101L, 102L});

        // get back and verify
        IEnumConfigurationValue<String> value = configurationStore.readConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE);
        assertNotNull(value);
        assertEquals("[ \"101\", \"102\" ]", value.toString());
        assertEquals(Long.valueOf(101), value.getValue());
        assertEquals("[101, 102]", value.getValueList().toString());
        assertEquals(Long.valueOf(102), value.getValue());
        
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            value.getValue();
        });        

        configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, new String[] {"103", "104"});
        IEnumConfigurationValue<String> value2 = configurationStore.readConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE);
        assertNotNull(value2);
        assertEquals("[ \"103\", \"104\" ]", value2.toString());
        assertEquals(Long.valueOf(103), value2.getValue());
        assertEquals("[103, 104]", value2.getValueList().toString());
        assertEquals(Long.valueOf(104), value2.getValue());

        configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, Arrays.asList("105", "106"));
        IEnumConfigurationValue<String> value3 = configurationStore.readConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE);
        assertNotNull(value3);
        assertEquals("[105, 106]", value3.toString());
        assertEquals(Long.valueOf(105), value3.getValue());
        assertEquals("[105, 106]", value3.getValueList().toString());
        assertEquals(Long.valueOf(106), value3.getValue());

        EnumConfigurationStoreException exception3 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, new String[] {"107", "a"});
        });
        // Invalid configuration found for key [com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#array_sample]: [input] Invalid value [a], it can not be converted into a NUMBER data type.
        assertEquals(HEADER + "array_sample]: [input] Invalid value [a], it can not be converted into a NUMBER data type.", exception3.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.ARRAY_SAMPLE) + END_BRACKED, exception3.keySet().toString());
        assertEquals("[ \"107\", \"a\" ]", exception3.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.ARRAY_SAMPLE)).toString());

        assertEquals("{com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#array_sample=[105, 106]}", "" + configurationStore.getProperties());
    }

    
    /**
     * Test number
     */
    @Test
    public void readWriteDate() {
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();

        assertNull(configurationStore.readConfigurationValueIgnoreDefault(MyEnumConfiguration.DATE));
        assertEquals("2021-03-01", configurationStore.readConfigurationValue(MyEnumConfiguration.DATE).toString());

        // read not existing value: invalid data type
        EnumConfigurationStoreException exception1 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.DATE, "your-host");
        });
        assertEquals(HEADER + "date]: [input] Invalid value [your-host], it can not be converted into a DATE data type: Text 'your-host' could not be parsed at index 0.", exception1.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DATE) + END_BRACKED, exception1.keySet().toString());
        assertEquals("your-host", exception1.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DATE)).toString());

        // write value: invalid range
        EnumConfigurationStoreException exception2 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.DATE, "1999-12-31");
        });
        assertEquals(HEADER + "date]: [input] Too small: invalid date of [1999-12-31], should be at least [2000-01-01] (now 1999-12-31)!", exception2.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DATE) + END_BRACKED, exception2.keySet().toString());
        assertEquals("1999-12-31", exception2.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DATE)).toString());
        assertEquals(LocalDate.parse("1999-12-31"), exception2.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DATE)).getValue());
        
        configurationStore.writeConfigurationValue(MyEnumConfiguration.DATE, "2022-05-26");

        // get back and verify
        IEnumConfigurationValue<String> value = configurationStore.readConfigurationValue(MyEnumConfiguration.DATE);
        assertNotNull(value);
        assertEquals("2022-05-26", value.toString());
        assertEquals(LocalDate.parse("2022-05-26"), value.getValue());
        assertEquals("[2022-05-26]", value.getValueList().toString());
    }

    
    /**
     * Test number
     */
    @Test
    public void readBinaryDate() {
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();
        String timestamp = "2021-03-15T08:59:22.123Z";

        assertNull(configurationStore.readConfigurationValueIgnoreDefault(MyEnumConfiguration.BINARY_SAMPLE));
        assertEquals("defaultname.txt|{plain/text}", configurationStore.readConfigurationValue(MyEnumConfiguration.BINARY_SAMPLE).toString());
        String content = timestamp + "|" + MESSAGE_ENCODED;
        configurationStore.writeConfigurationValue(MyEnumConfiguration.BINARY_SAMPLE, content);

        // get back and verify
        IEnumConfigurationValue<IEnumKeyValueConfigurationBinaryObject> value = configurationStore.readConfigurationValue(MyEnumConfiguration.BINARY_SAMPLE);
        assertNotNull(value);
        assertEquals("defaultname.txt|" + timestamp + "|{plain/text}" + MESSAGE_ENCODED, value.toString());
        
        IEnumKeyValueConfigurationBinaryObject object = value.getValue();
        
        assertEquals(MESSAGE_ENCODED, object.getData());
        assertEquals("[" + "defaultname.txt|" + timestamp + "|{plain/text}" + MESSAGE_ENCODED + "]", value.getValueList().toString());        
    }

    
    /**
     * Test number
     */
    @Test
    public void writenNullDataObject() {
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();

        EnumConfigurationStoreException exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.DELAY, (Object)null);
        });

        assertEquals(HEADER + "delay]: [input] Missing [input], its mandatory and not optional (cardinality: EnumKeyValueConfigurationSizing [minSize=1, maxSize=1])!", exception.getMessage());
    }

    
    /**
     * Test number
     */
    @Test
    public void readBinaryDataWithoutDefaultValue() {
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();
        String timestamp = "2021-03-15T08:59:22.123Z";

        assertNull(configurationStore.readConfigurationValueIgnoreDefault(MyEnumConfiguration.BINARY_WITHOUT_DEFAULT_SAMPLE));
        assertNull(configurationStore.readConfigurationValue(MyEnumConfiguration.BINARY_WITHOUT_DEFAULT_SAMPLE));
        String content = timestamp + "|" + MESSAGE_ENCODED;
        configurationStore.writeConfigurationValue(MyEnumConfiguration.BINARY_WITHOUT_DEFAULT_SAMPLE, content);

        // get back and verify
        IEnumConfigurationValue<IEnumKeyValueConfigurationBinaryObject> value = configurationStore.readConfigurationValue(MyEnumConfiguration.BINARY_WITHOUT_DEFAULT_SAMPLE);
        assertNotNull(value);
        assertEquals(content, value.toString());
        
        IEnumKeyValueConfigurationBinaryObject object = value.getValue();
        
        assertEquals(MESSAGE_ENCODED, object.getData());
        assertEquals("[" + content + "]", value.getValueList().toString());        
    }

    
    /**
     * Test read, write and delete value list
     * @throws IOException In case of an I/O error 
     * @throws ValidationException In case of a validation error
     */
    @Test
    public void readInvalidKey() throws ValidationException, IOException {
        com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration enumKeyValueConfiguration = createEnumKeyValueConfiguration("FIRST", EnumKeyValueConfigurationDataType.NUMBER, "0", "10", "0", "0"); 
        EnumConfigurationKeyResolver resolver = new EnumConfigurationKeyResolver(createEnumConfigurationMock("com.github.toolarium.enumeration.configuration.store.enumconfigurationstoretest$simpleconfigtest", enumKeyValueConfiguration));

        com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration result = resolver.getEnumKeyValueConfiguration(FIRST);
        assertEquals(result.getKey(), "FIRST");

        result = resolver.getEnumKeyValueConfiguration("com.github.toolarium.enumeration.configuration.store.enumconfigurationstoretest$simpleconfigtest");
        assertNull(result);
    }

    
    /**
     * Test read, write and delete value list
     * @throws IOException In case of an I/O error 
     * @throws ValidationException In case of a validation error
     */
    @Test
    public void validateEnumerationA() throws ValidationException, IOException {
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();

        assertNull(configurationStore.readConfigurationValueIgnoreDefault(MyEnumConfiguration.DELAY_PRECISION_A));
        assertEquals("SECONDS", configurationStore.readConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_A).getValue().toString());
        
        EnumConfigurationStoreException exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_A, "TEST");
        });
        assertEquals(HEADER + "delay_precision_a]: [input] Invalid enumeration of [input] for intput [\"TEST\"], allowed values are: SECONDS, MILLITSECONDS", exception.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DELAY_PRECISION_A) + END_BRACKED, exception.keySet().toString());
        assertEquals("TEST", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DELAY_PRECISION_A)).toString());
        assertEquals("TEST", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DELAY_PRECISION_A)).getValue());
        
        // get back and verify
        IEnumConfigurationValue<IEnumKeyValueConfigurationBinaryObject> value = configurationStore.readConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_A);
        assertNotNull(value);
        assertEquals("SECONDS", value.toString());
        
        configurationStore.writeConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_A, "MILLITSECONDS");
        value = configurationStore.readConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_A);
        assertNotNull(value);
        assertEquals("MILLITSECONDS", value.toString());        
        
        // set invalid isUniqueness error
        Properties properties = new Properties();
        properties.setProperty(DELAY_PRECISION_A, "[ \"TEST\", \"TESTP\" ]");
        configurationStore.setProperties(properties);
        exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.readConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_A);
        });
        
        assertEquals(HEADER + "delay_precision_a]: [input] Invalid enumeration of [input] for intput [\"[ \"TEST\", \"TESTP\" ]\"], allowed values are: SECONDS, MILLITSECONDS", exception.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DELAY_PRECISION_A) + END_BRACKED, exception.keySet().toString());
        assertEquals("[ \"TEST\", \"TESTP\" ]", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DELAY_PRECISION_A)).toString());
        assertEquals("TEST", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DELAY_PRECISION_A)).getValue());
        assertEquals("TESTP", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DELAY_PRECISION_A)).getValue());        
    }

    
    /**
     * Test read, write and delete value list
     * @throws IOException In case of an I/O error 
     * @throws ValidationException In case of a validation error
     */
    @Test
    public void validateEnumerationB() throws ValidationException, IOException {
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();

        assertNull(configurationStore.readConfigurationValueIgnoreDefault(MyEnumConfiguration.DELAY_PRECISION_B));
        assertEquals("SECONDS", configurationStore.readConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_B).getValue().toString());
        
        EnumConfigurationStoreException exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_B, "TESTP");
        }); 
        assertEquals(HEADER + "delay_precision_b]: [input] Invalid enumeration of [input] for intput [\"TESTP\"], allowed values are: SECONDS, MILLITSECONDS", exception.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DELAY_PRECISION_B) + END_BRACKED, exception.keySet().toString());
        assertEquals("TESTP", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DELAY_PRECISION_B)).toString());
        assertEquals("TESTP", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.DELAY_PRECISION_B)).getValue());
        
        // get back and verify
        IEnumConfigurationValue<IEnumKeyValueConfigurationBinaryObject> value = configurationStore.readConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_B);
        assertNotNull(value);
        assertEquals("SECONDS", value.toString());
        
        configurationStore.writeConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_B, "MILLITSECONDS");
        value = configurationStore.readConfigurationValue(MyEnumConfiguration.DELAY_PRECISION_B);
        assertNotNull(value);
        assertEquals("MILLITSECONDS", value.toString());
    }

    
    /**
     * Test number
     */
    @Test
    public void invalidInputData() {
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();

        assertNull(configurationStore.readConfigurationValueIgnoreDefault(MyEnumConfiguration.VALUE_C));
        assertNull(configurationStore.readConfigurationValue(MyEnumConfiguration.VALUE_C));

        // read not existing value: invalid data type
        EnumConfigurationStoreException exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_C, "test 1");
        });
        assertEquals(HEADER + "value_c]: [input] Invalid value [test 1], it can not be converted into a NUMBER data type.", exception.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_C) + END_BRACKED, exception.keySet().toString());
        assertEquals("test 1", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_C)).toString());
        
        // write value: invalid range
        exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_C, 10);
        });
        assertEquals(HEADER + "value_c]: [input] Too small: invalid size of [10], should be at least [100] (now 10)!", exception.getMessage());
        assertEquals(START_BRACKED + configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_C) + END_BRACKED, exception.keySet().toString());
        assertEquals("10", exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_C)).toString());
        assertEquals(10L, (Long)exception.getInvalidConfigurationValue(configurationStore.getEnumConfigurationKeyResolver().resolveConfigurationKeyName(MyEnumConfiguration.VALUE_C)).getValue());

        configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_C, 101);

        // get back and verify
        IEnumConfigurationValue<String> value = configurationStore.readConfigurationValue(MyEnumConfiguration.VALUE_C);
        assertNotNull(value);
        assertEquals("101", value.toString());
        assertEquals(Long.valueOf(101), value.getValue());
        assertEquals("[101]", value.getValueList().toString());
    }


    @EnumConfiguration(description = "The description")
    public enum SimpleConfigTest {
        @EnumKeyValueConfiguration(description =  "First description.", dataType = DataType.NUMBER, defaultValue = "11", exampleValue = "42")
        FIRST,
        
        @EnumKeyValueConfiguration(description =  "Second description.", dataType = DataType.NUMBER, defaultValue = "22", exampleValue = "42")
        SECOND,
        
        @EnumKeyValueConfiguration(description =  "Third description.", exampleValue = "third")
        THIRD,

        @EnumKeyValueConfiguration(description = "The port.", exampleValue = "8080")
        PORT,

        @EnumKeyValueConfiguration(description = "This is the date.", dataType = DataType.DATE, defaultValue = "2021-03-01", exampleValue = "2021-01-01", minValue = "2000-01-01", maxValue = "2036-12-31")
        DATE,

        @EnumKeyValueConfiguration(description =  "Third description.", defaultValue = "", exampleValue = "my entry", cardinality = "0..*")
        EMPTY_STRING,

        @EnumKeyValueConfiguration(description =  "Third description.", defaultValue = "a", exampleValue = "my entry", cardinality = "0..*")
        EMPTY_STRING_WITH_DEFAULT,

        @EnumKeyValueConfiguration(description =  "Hostname description.", defaultValue = "hostname", exampleValue = "second host")
        HOSTNAME;
    }
}
