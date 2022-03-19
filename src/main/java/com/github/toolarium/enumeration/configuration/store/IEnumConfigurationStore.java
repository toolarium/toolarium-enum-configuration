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
     * In case of missing value a possible defined default value from the enum configuration annotation will be returned. 
     *
     * @param <D> the configuration value type
     * @param configurationKey the unique configuration key (by default started by the name in lower case and dot notation, 
     *        separated by <code>#</code> and the key depending of the IEnumConfigurationKeyResolver). 
     * @return value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    <D> IEnumConfigurationValue<D> readConfigurationValue(String configurationKey) throws EnumConfigurationStoreException;   

    
    /**
     * Read a configuration value.
     * In case of missing value a possible defined default value from the enum configuration annotation will be returned. 
     *
     * @param <D> the configuration value type
     * @param <T> the generic configuration name
     * @param configurationKey the configuration key
     * @return value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    <D, T extends Enum<T>> IEnumConfigurationValue<D> readConfigurationValue(T configurationKey) throws EnumConfigurationStoreException;   

    
    /**
     * Read a configuration value.
     * In case of missing the value a possible defined default value in the enum configuration will be ignored.
     * 
     * @param <D> the configuration value type
     * @param configurationKey the unique configuration key (by default started by the name in lower case and dot notation, 
     *        separated by <code>#</code> and the key depending of the IEnumConfigurationKeyResolver). 
     * @return value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    <D> IEnumConfigurationValue<D> readConfigurationValueIgnoreDefault(String configurationKey) throws EnumConfigurationStoreException;   

    
    /**
     * Read a configuration value. 
     * In case of missing the value a possible defined default value in the enum configuration will be ignored.
     *
     * @param <D> the configuration value type
     * @param <T> the generic configuration name
     * @param configurationKey the configuration key
     * @return value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    <D, T extends Enum<T>> IEnumConfigurationValue<D> readConfigurationValueIgnoreDefault(T configurationKey) throws EnumConfigurationStoreException;   

    
    /**
     * Write a configuration value.
     *
     * @param <D> the configuration value type
     * @param configurationKey the unique configuration key (by default started by the name in lower case and dot notation, 
     *        separated by <code>#</code> and the key depending of the IEnumConfigurationKeyResolver). 
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
     * @param configurationKey the unique configuration key (by default started by the name in lower case and dot notation, 
     *        separated by <code>#</code> and the key depending of the IEnumConfigurationKeyResolver). 
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
     * In case of missing value a possible defined default value from the enum configuration annotation will be returned. 
     *
     * @param configurationKeys the unique configuration keys (by default started by the name in lower case and dot notation, 
     *        separated by <code>#</code> and the key depending of the IEnumConfigurationKeyResolver). 
     * @return the read configuration properties
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    Properties readConfigurationValueList(String... configurationKeys) throws EnumConfigurationStoreException;   

    
    /**
     * Read a list of configuration values.
     * In case of missing value a possible defined default value from the enum configuration annotation will be returned. 
     *
     * @param <T> the generic configuration name  
     * @param configurationKeys the configuration keys
     * @return the read configuration properties where the key corresponds to a valid configuration key
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    <T extends Enum<T>> Properties readConfigurationValueList(T[] configurationKeys) throws EnumConfigurationStoreException;   

    
    /**
     * Read a list of configuration values.
     * In case of missing the value a possible defined default value in the enum configuration will be ignored.
     *
     * @param configurationKeys the unique configuration keys (by default started by the name in lower case and dot notation, 
     *        separated by <code>#</code> and the key depending of the IEnumConfigurationKeyResolver). 
     * @return the read configuration properties
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    Properties readConfigurationValueListIgnoreDefault(String... configurationKeys) throws EnumConfigurationStoreException;   

    
    /**
     * Read a list of configuration values.
     * In case of missing the value a possible defined default value in the enum configuration will be ignored.
     *
     * @param <T> the generic configuration name  
     * @param configurationKeys the configuration keys
     * @return the read configuration properties where the key corresponds to a valid configuration key
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    <T extends Enum<T>> Properties readConfigurationValueListIgnoreDefault(T[] configurationKeys) throws EnumConfigurationStoreException;   


    /**
     * Write a list of configuration values. All other values in the store are not touched. 
     *
     * @param configuration the configuration properties where the key corresponds to a valid configuration key
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    void writeConfigurationValueList(Properties configuration) throws EnumConfigurationStoreException;   


    /**
     * Write a list of configuration values. 
     * Be careful in case of <code>removeEntriesWithMissingKeys = true</code>, it deletes not covered data. 
     * 
     * @param configuration the configuration properties where the key corresponds to a valid configuration key
     * @param removeEntriesWithMissingKey true to remove all entries on store which are not listed in the over given configuration
     * @return the deleted configuration properties
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    Properties writeConfigurationValueList(Properties configuration, boolean removeEntriesWithMissingKey) throws EnumConfigurationStoreException;   


    /**
     * Deletes a configuration value. 
     *
     * @param <D> the configuration value type
     * @param configurationKey the unique configuration key (by default started by the name in lower case and dot notation, 
     *        separated by <code>#</code> and the key depending of the IEnumConfigurationKeyResolver). 
     * @return the deleted configuration value or null
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    <D> IEnumConfigurationValue<D> deleteConfigurationValue(String configurationKey) throws EnumConfigurationStoreException;   

    
    /**
     * Deletes a configuration value.
     *
     * @param <D> the configuration value type
     * @param <T> the generic configuration name
     * @param configurationKey the configuration key
     * @return the deleted configuration value or null
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    <D, T extends Enum<T>> IEnumConfigurationValue<D> deleteConfigurationValue(T configurationKey) throws EnumConfigurationStoreException;   


    /**
     * Deletes a list of configuration values.
     *
     * @param configurationKeys the unique configuration key (by default started by the name in lower case and dot notation, 
     *        separated by <code>#</code> and the key depending of the IEnumConfigurationKeyResolver). 
     * @return the deleted configuration properties where the key corresponds to a valid configuration key
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    Properties deleteConfigurationValueList(String... configurationKeys) throws EnumConfigurationStoreException;   

    
    /**
     * Deletes a list of configuration values.
     *
     * @param <T> the generic configuration name  
     * @param configurationKeys the configuration keys
     * @return the deleted configuration properties where the key corresponds to a valid configuration key
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    <T extends Enum<T>> Properties deleteConfigurationValueList(T[] configurationKeys) throws EnumConfigurationStoreException;
    
    
    /**
     * Get the configuration key resolver.
     *
     * @return the configuration key resolver.
     */
    IEnumConfigurationKeyResolver getEnumConfigurationKeyResolver();
}
