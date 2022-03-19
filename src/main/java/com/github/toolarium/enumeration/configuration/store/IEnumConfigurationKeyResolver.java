/*
 * IEnumConfigurationKeyResolver.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;

/**
 * Defines the configuration key resolver.
 * 
 * @author patrick
 */
public interface IEnumConfigurationKeyResolver {
    
    /**
     * Create the configuration key name
     *
     * @param configurationName the configuration name, e.g. the package and class name
     * @param keyName the key name, e.g. the enumeration name
     * @return the unique configuration key name
     */
    String createConfigurationKeyName(String configurationName, String keyName);
   
   
    /**
     * Resolves the configuration key name
     *
     * @param <T> the generic type
     * @param configurationKey the configuration key
     * @return the configuration key name
     * @throws EnumConfigurationStoreException In case of an invalid configuration key
     */
    <T extends Enum<T>> String resolveConfigurationKeyName(T configurationKey);

    
    /**
     * Resolves the configuration key
     * 
     * @param <T> the generic type
     * @param configurationKeyName the configuration key name
     * @return the configuration key
     * @throws EnumConfigurationStoreException In case of an invalid configuration key
     */
    <T extends Enum<T>> T resolveConfigurationKey(String configurationKeyName);

    
    /**
     * Get the enum key / value configuration information of an enum configuration name / key. 
     * It can be get either by interpreting of the annotation of the enum configuration or to 
     * load the previous generated JSON from internal / external source.
     *
     * @param inputConfigurationKeyName the configuration key name
     * @return the enum key value configuration
     * @throws EnumConfigurationStoreException In case of an enum configuration store exception
     */
    EnumKeyValueConfiguration getEnumKeyValueConfiguration(String inputConfigurationKeyName) throws EnumConfigurationStoreException;
}
