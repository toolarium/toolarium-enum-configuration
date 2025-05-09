/*
 * EnumConfigurationStorePropertiesTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration.DataType;
import com.github.toolarium.enumeration.configuration.processor.MyEnumConfiguration;
import com.github.toolarium.enumeration.configuration.store.impl.PropertiesEnumConfigurationStore;
import java.util.Properties;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link IEnumConfigurationStore} properties.
 * 
 * @author patrick
 */
public class EnumConfigurationStorePropertiesTest {
    
    /**
     * Read properties
     */
    @Test
    public void readProperties() {
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();
        
        // read by type and assume we have default values
        Long val = configurationStore.readConfigurationValue(MyConfigTest.FIRST).getValue();
        assertEquals(Long.valueOf(11), val);

        assertEquals(Long.valueOf(11), configurationStore.readConfigurationValue(MyConfigTest.FIRST).getValue());
        assertEquals(Long.valueOf(22), configurationStore.readConfigurationValue(MyConfigTest.SECOND).getValue());
        assertNull(configurationStore.readConfigurationValue(MyConfigTest.THIRD));
        assertNull(configurationStore.readConfigurationValue(MyConfigTest.HOSTNAME));
        assertNull(configurationStore.readConfigurationValue(MyEnumConfiguration.PORT));

        // read by string and assume we have default values
        assertEquals(Long.valueOf(11), configurationStore.readConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#first").getValue());
        assertEquals(Long.valueOf(22), configurationStore.readConfigurationValue(" com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second").getValue());
        assertNull(configurationStore.readConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#third"));
        assertNull(configurationStore.readConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#hostname"));

        // write values by type
        configurationStore.writeConfigurationValue(MyConfigTest.FIRST, "22"); // write type by string
        configurationStore.writeConfigurationValue(MyConfigTest.SECOND, 33); // write type by object
        configurationStore.writeConfigurationValue(MyEnumConfiguration.HINT, "my hint");
        configurationStore.writeConfigurationValue(MyEnumConfiguration.HOSTNAME, "my-host");
        configurationStore.writeConfigurationValue(MyEnumConfiguration.PORT, "8082");
        configurationStore.writeConfigurationValue(MyEnumConfiguration.DATE, "2022-02-17");
        configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, "[\"33\", \"44\"]");

        // write values by string
        configurationStore.writeConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#first", "23");
        assertEquals(Long.valueOf(23), configurationStore.readConfigurationValue(MyConfigTest.FIRST).getValue());

        // write values by object
        configurationStore.writeConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#first", 24);
        assertEquals(Long.valueOf(24), configurationStore.readConfigurationValue(MyConfigTest.FIRST).getValue());

        // read all properties
        String reference = "{com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#array_sample=[\"33\", \"44\"], " 
                          + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#date=2022-02-17, "
                          + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#hint=my hint, "
                          + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#hostname=my-host, "
                          + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#port=8082, "
                          + "com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#first=24, "
                          + "com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second=33}";
        assertEquals(reference, configurationStore.getProperties().toString());
        
        // verify
        assertEquals(Long.valueOf(24), configurationStore.readConfigurationValue(MyConfigTest.FIRST).getValue());
        assertEquals(Long.valueOf(33), configurationStore.readConfigurationValue(MyConfigTest.SECOND).getValue());
        assertNull(configurationStore.readConfigurationValue(MyConfigTest.THIRD));
        assertNull(configurationStore.readConfigurationValue(MyConfigTest.HOSTNAME));
        assertEquals("my-host", configurationStore.readConfigurationValue(MyEnumConfiguration.HOSTNAME).getValue());
        assertEquals("8082", configurationStore.readConfigurationValue(MyEnumConfiguration.PORT).getValue());
        
        // read by some values by type
        Properties properties = configurationStore.readConfigurationValueList(new MyEnumConfiguration[] {MyEnumConfiguration.PORT, MyEnumConfiguration.DATE});
        assertEquals("{com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#date=2022-02-17, "
                    + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#port=8082}",
                    properties.toString());

        // read by value by string
        assertEquals("8082", configurationStore.readConfigurationValue("com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#port").getValue());

        // read by some values by string types
        properties = configurationStore.readConfigurationValueList("com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#port", 
                                                                   " com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second");
        assertEquals("{com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#port=8082, "
                    + "com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second=33}",
                    properties.toString());
        
        Properties newProperties = new Properties();
        newProperties.setProperty("com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#port", "0815");
        newProperties.setProperty("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second", "21");
        configurationStore.writeConfigurationValueList(newProperties);

        String reference2 = "{com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#array_sample=[\"33\", \"44\"], " 
                           + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#date=2022-02-17, "
                           + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#hint=my hint, "
                           + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#hostname=my-host, "
                           + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#port=0815, "
                           + "com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#first=24, "
                           + "com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second=21}";
        assertEquals(reference2, configurationStore.getProperties().toString());
        
        Properties deletedProperties = configurationStore.writeConfigurationValueList(newProperties, true);
        assertEquals("{com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#port=0815, "
                    + "com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second=21}",
                    configurationStore.getProperties().toString());

        String reference3 = "{com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#array_sample=[\"33\", \"44\"], " 
                           + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#date=2022-02-17, "
                           + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#hint=my hint, "
                           + "com.github.toolarium.enumeration.configuration.processor.myenumconfiguration#hostname=my-host, "
                           + "com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#first=24}";
        assertEquals(reference3, deletedProperties.toString());
        
        // default value
        assertEquals(Long.valueOf(11), configurationStore.readConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#first").getValue());
        assertNull(configurationStore.readConfigurationValueIgnoreDefault("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#first"));
        
        // last set value
        assertEquals(Long.valueOf(21), configurationStore.readConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second").getValue());
        assertEquals(Long.valueOf(21), configurationStore.readConfigurationValueIgnoreDefault("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second").getValue());

        // read defaul value
        assertEquals(Long.valueOf(21), configurationStore.readConfigurationValueIgnoreDefault(MyConfigTest.SECOND).getValue());

        // remove
        assertEquals(Long.valueOf(21), configurationStore.deleteConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second").getValue());

        // default value
        assertEquals(Long.valueOf(22), configurationStore.readConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second").getValue());
     
        assertEquals("0815", configurationStore.deleteConfigurationValue(MyEnumConfiguration.PORT).getValue());
        assertNull(configurationStore.readConfigurationValue(MyEnumConfiguration.PORT));

    }

    
    /**
     * Read properties
     */
    @Test
    public void readAllProperties() {
        PropertiesEnumConfigurationStore configurationStore = new PropertiesEnumConfigurationStore();
        
        // read by type and assume we have default values
        Long val = configurationStore.readConfigurationValue(MyConfigTest.FIRST).getValue();
        assertEquals(Long.valueOf(11), val);

        assertEquals(Long.valueOf(11), configurationStore.readConfigurationValue(MyConfigTest.FIRST).getValue());
        assertEquals(Long.valueOf(22), configurationStore.readConfigurationValue(MyConfigTest.SECOND).getValue());
        assertNull(configurationStore.readConfigurationValue(MyConfigTest.THIRD));
        assertNull(configurationStore.readConfigurationValue(MyConfigTest.HOSTNAME));
        assertNull(configurationStore.readConfigurationValue(MyEnumConfiguration.PORT));

        // read by string and assume we have default values
        assertEquals(Long.valueOf(22), configurationStore.readConfigurationValue(" com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#second").getValue());
        assertNull(configurationStore.readConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#third"));
        assertNull(configurationStore.readConfigurationValue("com.github.toolarium.enumeration.configuration.store.enumconfigurationstorepropertiestest$myconfigtest#hostname"));

        // write values by type
        configurationStore.writeConfigurationValue(MyConfigTest.FIRST, "22"); // write type by string
        configurationStore.writeConfigurationValue(MyConfigTest.SECOND, 33); // write type by object
        configurationStore.writeConfigurationValue(MyEnumConfiguration.HINT, "my hint");
        configurationStore.writeConfigurationValue(MyEnumConfiguration.HOSTNAME, "my-host");
        configurationStore.writeConfigurationValue(MyEnumConfiguration.PORT, "8082");
        configurationStore.writeConfigurationValue(MyEnumConfiguration.DATE, "2022-02-17");
        configurationStore.writeConfigurationValue(MyEnumConfiguration.ARRAY_SAMPLE, "[\"33\", \"44\"]");

        // write values by string
        assertEquals(configurationStore.getProperties(), configurationStore.readConfigurationValueList(""));
    }

    
    @EnumConfiguration(description = "The description")
    public enum MyConfigTest {
        @EnumKeyValueConfiguration(description =  "First description.", dataType = DataType.NUMBER, defaultValue = "11", exampleValue = "42")
        FIRST,
        
        @EnumKeyValueConfiguration(description =  "Second description.", dataType = DataType.NUMBER, defaultValue = "22", exampleValue = "42")
        SECOND,
        
        @EnumKeyValueConfiguration(description =  "Third description.", exampleValue = ":-)")
        THIRD,
        
        @EnumKeyValueConfiguration(description =  "Hostname description.", exampleValue = "second host")
        HOSTNAME;
    }
}
