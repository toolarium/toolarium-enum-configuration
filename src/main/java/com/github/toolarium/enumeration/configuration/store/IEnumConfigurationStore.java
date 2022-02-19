/*
 * IEnumConfigurationStore.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import java.util.Properties;


/**
 * Defines the enum configuration store interface.
 * 
 * @author patrick
 */
public interface IEnumConfigurationStore {
    
    /**
     * Read a configuration value.
     *
     * @param <D> the configuration value type
     * @param configurationKey The unique configuration key, started by the name in lower case and dot notation, separated by <code>#</code> and the key. 
     * @return value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be read 
     */
    <D> IEnumConfigurationValue<D> readConfigurationValue(String configurationKey) throws EnumConfigurationStoreException;   

    
    /**
     * Read a configuration value.
     *
     * @param <D> the configuration value type
     * @param <T> the generic configuration name
     * @param configurationKey the configuration key
     * @return value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be read 
     */
    <D, T extends Enum<T>> IEnumConfigurationValue<D> readConfigurationValue(T configurationKey) throws EnumConfigurationStoreException;   

    
    /**
     * Write a configuration value.
     *
     * @param <D> the configuration value type
     * @param configurationKey the configuration key.
     * @param value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be written 
     */
    <D> void writeConfigurationValue(String configurationKey, String value) throws EnumConfigurationStoreException;

    
    /**
     * Write a configuration value.
     *
     * @param <D> the configuration value type
     * @param <T> the generic configuration name  
     * @param configurationKey the configuration key
     * @param value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be written
     */
    <D, T extends Enum<T>> void writeConfigurationValue(T configurationKey, String value) throws EnumConfigurationStoreException;

    
    /**
     * Write a configuration value.
     *
     * @param <T> the generic configuration name  
     * @param configurationKey the configuration key
     * @param value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be written
     */
    <T extends Enum<T>> void writeConfigurationValue(String configurationKey, Object value) throws EnumConfigurationStoreException;

    
    /**
     * Write a configuration value.
     *
     * @param <T> the generic configuration name  
     * @param configurationKey the configuration key
     * @param value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be written
     */
    <T extends Enum<T>> void writeConfigurationValue(T configurationKey, Object value) throws EnumConfigurationStoreException;

    
    /**
     * Read a list of configuration values.
     *
     * @param configurationKeys the configuration keys
     * @return the read configuration properties
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be read 
     */
    Properties readConfigurationValueList(String... configurationKeys) throws EnumConfigurationStoreException;   

    
    /**
     * Read a list of configuration values.
     *
     * @param <T> the generic configuration name  
     * @param configurationKeys the configuration keys
     * @return the read configuration properties where the key corresponds to a valid configuration key
     * @throws EnumConfigurationStoreException in case the configuration cannot be read 
     */
    <T extends Enum<T>> Properties readConfigurationValueList(T[] configurationKeys) throws EnumConfigurationStoreException;   


    /**
     * Write a list of configuration values.
     *
     * @param configuration the configuration properties where the key corresponds to a valid configuration key
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be read 
     */
    void writeConfigurationValueList(Properties configuration) throws EnumConfigurationStoreException;   

    
    /**
     * Defines if the configuration store returns the default value in case of a missing value.
     *
     * @return true if it will return the default value in case of a missing value.
     */
    boolean supportReturnDefaultValueIfMissing();
}
