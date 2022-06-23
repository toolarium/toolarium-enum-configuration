/*
 * EnumConfigurationKeyResolverTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.store.impl.EnumConfigurationKeyResolver;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test the IEnumConfigurationKeyResolver.
 * @author patrick
 */
public class EnumConfigurationKeyResolverTest extends AbstractEnumConfigurationStoreTest {
    private static final Logger LOG = LoggerFactory.getLogger(EnumConfigurationKeyResolverTest.class);
    private static final com.github.toolarium.enumeration.configuration.store.EnumConfigurationStoreTest.SimpleConfigTest REFERENCE = com.github.toolarium.enumeration.configuration.store.EnumConfigurationStoreTest.SimpleConfigTest.FIRST;
    private static final String DOT = ".";
    private static final String HASH = "#";
    private static final String ENUM_CONFIGURATION_KEY_NAME = "FIRST";
    private static final String PACKAGENAME = "com.github.toolarium.enumeration.configuration.store";
    private static final String CLASSNAME = PACKAGENAME + DOT + "EnumConfigurationStoreTest.SimpleConfigTest";
    private static final String CONFIGURATION_KEY_FIRST  = PACKAGENAME + DOT + "enumconfigurationstoretest$simpleconfigtest" + HASH + ENUM_CONFIGURATION_KEY_NAME.toLowerCase();
    
    /**
     * Test the 
     *
     * @throws IOException In case of an I/O error 
     * @throws ValidationException In case of a validation error
     */
    @Test
    public void testCreateConfigurationKeyName() throws ValidationException, IOException {
        // String createConfigurationKeyName(String configurationName, String keyName);
        IEnumConfigurationKeyResolver resolver = new EnumConfigurationKeyResolver();
        String configurationKeyName = resolver.createConfigurationKeyName(CLASSNAME, ENUM_CONFIGURATION_KEY_NAME);
        assertEquals((CLASSNAME + HASH + ENUM_CONFIGURATION_KEY_NAME).toLowerCase(), configurationKeyName);
        
        // <T extends Enum<T>> String resolveConfigurationKeyName(T configurationKey);
        LOG.info("Resolve configuration key name...");
        resolver = new EnumConfigurationKeyResolver();
        assertEquals(CONFIGURATION_KEY_FIRST, resolver.resolveConfigurationKeyName(REFERENCE));

        // <T extends Enum<T>> T resolveConfigurationKey(String configurationKeyName);
        // test exact naming
        LOG.info("Resolve configuration key {}...", PACKAGENAME + DOT + "EnumConfigurationStoreTest$SimpleConfigTest" + HASH + ENUM_CONFIGURATION_KEY_NAME);
        resolver = new EnumConfigurationKeyResolver();
        assertEquals(REFERENCE, resolver.resolveConfigurationKey(PACKAGENAME + DOT + "EnumConfigurationStoreTest$SimpleConfigTest" + HASH + ENUM_CONFIGURATION_KEY_NAME));
        
        // test with lower case
        LOG.info("Resolve configuration lowercase key {}...", CONFIGURATION_KEY_FIRST);
        resolver = new EnumConfigurationKeyResolver();
        assertEquals(REFERENCE, resolver.resolveConfigurationKey(CONFIGURATION_KEY_FIRST));

        // EnumKeyValueConfiguration getEnumKeyValueConfiguration(String inputConfigurationKeyName) throws EnumConfigurationStoreException;
        com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration enumKeyValueConfiguration = createEnumKeyValueConfiguration(ENUM_CONFIGURATION_KEY_NAME, EnumKeyValueConfigurationDataType.NUMBER, "0", "10", "0", "0"); 
        resolver = new EnumConfigurationKeyResolver(createEnumConfigurationMock(PACKAGENAME + DOT + "enumconfigurationstoretest$simpleconfigtest", enumKeyValueConfiguration));
        com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration result = resolver.getEnumKeyValueConfiguration(CONFIGURATION_KEY_FIRST);
        assertEquals(result.getKey(), ENUM_CONFIGURATION_KEY_NAME);
        assertEquals(result.getDataType(), EnumKeyValueConfigurationDataType.NUMBER);

        resolver = new EnumConfigurationKeyResolver();
        result = resolver.getEnumKeyValueConfiguration(PACKAGENAME + DOT + "enumconfigurationstoretest$simpleconfigtest");
        assertNull(result);
    }
}
