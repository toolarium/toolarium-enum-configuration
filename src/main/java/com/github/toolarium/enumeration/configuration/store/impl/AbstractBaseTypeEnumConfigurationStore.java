/*
 * AbstractBaseTypeEnumConfigurationStore.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.SortedProperties;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import com.github.toolarium.enumeration.configuration.util.EnumUtil;
import com.github.toolarium.enumeration.configuration.validation.EnumConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract base type enum configuration store.
 * 
 * @author patrick
 */
public abstract class AbstractBaseTypeEnumConfigurationStore extends AbstractBaseEnumConfigurationStore {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBaseTypeEnumConfigurationStore.class);
    private Map<Object, EnumKeyValueConfiguration> enumKeyValueConfigurationMap;

    
    /**
     * Constructor for AbstractBaseTypeEnumConfigurationStore
     */
    public AbstractBaseTypeEnumConfigurationStore() {
        super();
        enumKeyValueConfigurationMap = new ConcurrentHashMap<Object, EnumKeyValueConfiguration>();
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValue(java.lang.Enum)
     */
    @Override
    public <D, T extends Enum<T>> IEnumConfigurationValue<D> readConfigurationValue(T configurationKey) throws EnumConfigurationStoreException {
        return readConfigurationValue(configurationKey, true);
    }
    
    
    /**
     * Read a configuration value.
     * In case of missing value a possible defined default value from the enum configuration annotation will be returned. 
     *
     * @param <D> the configuration value type
     * @param <T> the generic configuration name
     * @param configurationKey the configuration key
     * @param supportReturnDefaultValueIfMissing true to support default value
     * @return value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be read 
     */
    protected <D, T extends Enum<T>> IEnumConfigurationValue<D> readConfigurationValue(T configurationKey, boolean supportReturnDefaultValueIfMissing) throws EnumConfigurationStoreException {
        if (configurationKey == null) {
            LOG.debug("Invalid input configuration key!");
            return null;
        }
        
        String configurationKeyName = getEnumConfigurationKeyResolver().resolveConfigurationKeyName(configurationKey);
        String value = prepareValue(getEnumKeyValueConfiguration(configurationKey), loadConfiguration(configurationKeyName), supportReturnDefaultValueIfMissing);
        
        IEnumConfigurationValue<D> result = null;
        if (value != null) {
            result = validate(configurationKey, value);
        }

        return result;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValueIgnoreDefault(java.lang.Enum)
     */
    @Override
    public <D, T extends Enum<T>> IEnumConfigurationValue<D> readConfigurationValueIgnoreDefault(T configurationKey) throws EnumConfigurationStoreException {
        return readConfigurationValue(configurationKey, false);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValueList(java.lang.Enum[])
     */
    @Override
    public <T extends Enum<T>> Properties readConfigurationValueList(T[] configurationKeys) throws EnumConfigurationStoreException {
        Properties result = new SortedProperties();
        if (configurationKeys == null || configurationKeys.length == 0) {
            LOG.debug("Invalid input configuration keys!");
            return result;
        }
        
        for (T configurationKey : configurationKeys) {
            String configurationKeyName = getEnumConfigurationKeyResolver().resolveConfigurationKeyName(configurationKey);
            IEnumConfigurationValue<?> value = readConfigurationValue(configurationKey);
            result.setProperty(configurationKeyName, handlingNullObject(value));
        }
        
        return result;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValueListIgnoreDefault(java.lang.Enum[])
     */
    @Override
    public <T extends Enum<T>> Properties readConfigurationValueListIgnoreDefault(T[] configurationKeys) throws EnumConfigurationStoreException {
        Properties result = new SortedProperties();
        if (configurationKeys == null || configurationKeys.length == 0) {
            LOG.debug("Invalid input configuration keys!");
            return result;
        }
        
        for (T configurationKey : configurationKeys) {
            String configurationKeyName = getEnumConfigurationKeyResolver().resolveConfigurationKeyName(configurationKey);
            IEnumConfigurationValue<?> value = readConfigurationValueIgnoreDefault(configurationKey);
            result.setProperty(configurationKeyName, handlingNullObject(value));
        }
        
        return result;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#writeConfigurationValue(java.lang.Enum, java.lang.String)
     */
    @Override
    public <D, T extends Enum<T>> void writeConfigurationValue(T configurationKey, String value) throws EnumConfigurationStoreException {
        if (configurationKey == null) {
            LOG.debug("Invalid input configuration key!");
            return;
        }
        
        IEnumConfigurationValue<D> val = validate(configurationKey, value);
        if (val != null) {
            writeConfiguration(getEnumConfigurationKeyResolver().resolveConfigurationKeyName(configurationKey), val.toString());
        }
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#writeConfigurationValue(java.lang.Enum, java.lang.Object)
     */
    @Override
    public <T extends Enum<T>> void writeConfigurationValue(T configurationKey, Object configurationValue) throws EnumConfigurationStoreException {
        if (configurationKey == null) {
            LOG.debug("Invalid input configuration key!");
            return;
        }
        
        String configurationKeyName = getEnumConfigurationKeyResolver().resolveConfigurationKeyName(configurationKey);
        String value = convertObjectToString(configurationKeyName, EnumUtil.getInstance().getEnumKeyValueConfigurationAnnotationInformation(configurationKey), configurationValue);
        validate(configurationKey, value);
        writeConfiguration(configurationKeyName, value);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#deleteConfigurationValue(java.lang.Enum)
     */
    @Override
    public <D, T extends Enum<T>> IEnumConfigurationValue<D> deleteConfigurationValue(T configurationKey) throws EnumConfigurationStoreException {
        if (configurationKey == null) {
            LOG.debug("Invalid input configuration key!");
            return null;
        }

        IEnumConfigurationValue<D> result = readConfigurationValueIgnoreDefault(configurationKey);
        String configurationKeyName = getEnumConfigurationKeyResolver().resolveConfigurationKeyName(configurationKey);
        deleteConfiguration(configurationKeyName);
        return result;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#deleteConfigurationValueList(java.lang.Enum[])
     */
    @Override
    public <T extends Enum<T>> Properties deleteConfigurationValueList(T[] configurationKeys) throws EnumConfigurationStoreException {
        Properties result = new SortedProperties();
        if (configurationKeys == null || configurationKeys.length == 0) {
            LOG.debug("Invalid input configuration keys!");
            return result;
        }

        for (T configurationKey : configurationKeys) {
            String configurationKeyName = getEnumConfigurationKeyResolver().resolveConfigurationKeyName(configurationKey);
            result.setProperty(configurationKeyName, handlingNullObject(deleteConfiguration(configurationKeyName)));
        }
        
        return result;
    }

    
    /**
     * Validate the configuration value
     *
     * @param <D> the configuration value type
     * @param <T> the generic type
     * @param configurationKey the configuration key
     * @param value the value
     * @return the converted value
     * @throws EnumConfigurationStoreException In case of a load exception
     */
    protected <D, T extends Enum<T>> IEnumConfigurationValue<D> validate(T configurationKey, String value) throws EnumConfigurationStoreException {
        if (configurationKey == null) {
            throw new EnumConfigurationStoreException("Invalid configuration key!");
        }

        String configurationKeyName = getEnumConfigurationKeyResolver().resolveConfigurationKeyName(configurationKey);

        try {
            EnumKeyValueConfiguration enumKeyValueConfiguration = getEnumKeyValueConfiguration(configurationKey);
            Collection<D> valueList = EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, value);
            return prepareResult(value, valueList);
        } catch (ValidationException ex) {
            String msg = "Invalid configuration found for key [" + configurationKeyName + "]: " + ex.getMessage();
            LOG.debug(msg);
            EnumConfigurationStoreException e = new EnumConfigurationStoreException(msg, ex);
            e.add(configurationKeyName, value, ex.getConvertedValueList());
            throw e;
        }
    }
    

    /**
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractBaseEnumConfigurationStore#getEnumKeyValueConfiguration(java.lang.String)
     */
    @Override
    protected EnumKeyValueConfiguration getEnumKeyValueConfiguration(String inputConfigurationKeyName) throws EnumConfigurationStoreException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = enumKeyValueConfigurationMap.get(inputConfigurationKeyName);
        if (enumKeyValueConfiguration != null) {
            return enumKeyValueConfiguration;
        }
        
        return super.getEnumKeyValueConfiguration(inputConfigurationKeyName);
    }

    
    /**
     * Get the enum key / value configuration information of an enum configuration name / key. 
     * It can be get either by interpreting of the annotation of the enum configuration or to 
     * load the previous generated JSON from internal / external source.
     *
     * @param <T> the generic type
     * @param configurationKey the configuration key
     * @return the enum key value configuration
     * @throws EnumConfigurationStoreException In case of a enum configuration store execption
     */
    protected <T extends Enum<T>> EnumKeyValueConfiguration getEnumKeyValueConfiguration(T configurationKey) throws EnumConfigurationStoreException {
        if (configurationKey == null) {
            throw new EnumConfigurationStoreException("Invalid configuration key!");
        }

        String cacheKey = getEnumConfigurationKeyResolver().createConfigurationKeyName(configurationKey.getClass().getName(), configurationKey.name());
        EnumKeyValueConfiguration enumKeyValueConfiguration = enumKeyValueConfigurationMap.get(cacheKey);
        if (enumKeyValueConfiguration == null) {
            try {
                enumKeyValueConfiguration = EnumUtil.getInstance().getEnumKeyValueConfigurationAnnotationInformation(configurationKey);
                if (enumKeyValueConfiguration != null) {
                    enumKeyValueConfigurationMap.put(cacheKey, enumKeyValueConfiguration);
                }
            } catch (IllegalArgumentException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Could not get enum configuration from annotation of key [" + cacheKey + "]: " + e.getMessage(), e);
                }

                return getEnumKeyValueConfiguration(getEnumConfigurationKeyResolver().resolveConfigurationKeyName(configurationKey));
            }
        }
        
        return enumKeyValueConfiguration;
    }
}
