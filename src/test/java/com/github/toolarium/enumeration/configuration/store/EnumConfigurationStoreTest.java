/*
 * EnumConfigurationStoreTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.enumeration.configuration.dto.IEnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.processor.MyEnumConfiguration;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import com.github.toolarium.enumeration.configuration.store.impl.PropertiesEnumConfigurationStore;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link IEnumConfigurationStore} with local enum configuration.
 * 
 * @author patrick
 */
public class EnumConfigurationStoreTest implements IEnumConfigurationStoreConstants {

    // values
    private static final String MESSAGE_ENCODED = "ICBteSBmaWxlIGNvbnRlbnQgIA=="; // "  my file content  " 
    
    /**
     * Test string read / write
     */
    @Test
    public void readWriteSimpleString() {
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(true);
        
        // read not existing value
        assertNull(new PropertiesEnumConfigurationStore(false).readConfigurationValue(MyEnumConfiguration.HOSTNAME));
        assertEquals("hostname", configurationStore.readConfigurationValue(MyEnumConfiguration.HOSTNAME).getValue());
        
        // write value
        configurationStore.writeConfigurationValue(MyEnumConfiguration.HOSTNAME, "my-host");
        
        // get back and verify
        IEnumConfigurationValue<String> value = configurationStore.readConfigurationValue(MyEnumConfiguration.HOSTNAME);
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
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(true);

        assertNull(new PropertiesEnumConfigurationStore(false).readConfigurationValue(MyEnumConfiguration.VALUE_C));
        assertNull(configurationStore.readConfigurationValue(MyEnumConfiguration.VALUE_C));

        // read not existing value
        EnumConfigurationStoreException exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_C, "test 1");
        });
        assertEquals(HEADER + "value_c]: [input] Invalid value [test 1], it can not be converted into a NUMBER data type.", exception.getMessage());
 
        // write value
        exception = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_C, 10);
        });
        assertEquals(HEADER + "value_c]: [input] Too small: invalid size of [10], should be at least [100] (now 10)!", exception.getMessage());

        configurationStore.writeConfigurationValue(MyEnumConfiguration.VALUE_C, 101);

        // get back and verify
        IEnumConfigurationValue<String> value = configurationStore.readConfigurationValue(MyEnumConfiguration.VALUE_C);
        assertNotNull(value);
        assertEquals("101", value.toString());
        assertEquals(Long.valueOf(101), value.getValue());
        assertEquals("[101]", value.getValueList().toString());
    }


    /**
     * Test number
     */
    @Test
    public void readWriteArray() {
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(true);

        assertNull(new PropertiesEnumConfigurationStore(false).readConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE));
        assertEquals("[\"1\", \"2\" ]", configurationStore.readConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE).toString());

        // read not existing value
        EnumConfigurationStoreException exception1 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, "test 2");
        });
        assertEquals(HEADER + "array_sample]: [input] Invalid cardinality of [input], the minSize is [2].", exception1.getMessage());
        
        // write value
        EnumConfigurationStoreException exception2 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, 10);
        });
        assertEquals(HEADER + "array_sample]: [input] Invalid cardinality of [input], the minSize is [2].", exception2.getMessage());
        
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
        
        EnumConfigurationStoreException exception3 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, new String[] {"105", "a"});
        });
        // Invalid configuration found for key [com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#array_sample]: [input] Invalid value [a], it can not be converted into a NUMBER data type.
        assertEquals(HEADER + "array_sample]: [input] Invalid value [a], it can not be converted into a NUMBER data type.", exception3.getMessage());
        
        assertEquals("{com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#array_sample=[ \"103\", \"104\" ]}", "" + configurationStore.getProperties());
    }

    
    /**
     * Test number
     */
    @Test
    public void readWriteDate() {
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(true);

        assertNull(new PropertiesEnumConfigurationStore(false).readConfigurationValue(MyEnumConfiguration.DATE));
        assertEquals("2021-03-01", configurationStore.readConfigurationValue(MyEnumConfiguration.DATE).toString());

        // read not existing value
        EnumConfigurationStoreException exception1 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.DATE, "your-host");
        });
        assertEquals(HEADER + "date]: [input] Invalid value [your-host], it can not be converted into a DATE data type: Text 'your-host' could not be parsed at index 0.", exception1.getMessage());
        
        // write value
        EnumConfigurationStoreException exception2 = Assertions.assertThrows(EnumConfigurationStoreException.class, () -> {
            configurationStore.writeConfigurationValue(MyEnumConfiguration.DATE, "1999-12-31");
        });
        assertEquals(HEADER + "date]: [input] Too small: invalid date of [1999-12-31], should be at least [2000-01-01] (now 1999-12-31)!", exception2.getMessage());
        
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
        IEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore(true);
        String timestamp = "2021-03-15T08:59:22.123Z";

        assertNull(new PropertiesEnumConfigurationStore(false).readConfigurationValue(MyEnumConfiguration.BINARY_SAMPLE));
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
}
