/*
 * AbstractBaseTypeEnumConfigurationStore.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.impl;

import com.github.toolarium.enumeration.configuration.converter.StringTypeConverterFactory;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.IEnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.SortedProperties;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import com.github.toolarium.enumeration.configuration.util.ClassPathUtil;
import com.github.toolarium.enumeration.configuration.util.EnumKeyValueConfigurationBinaryObjectParser;
import com.github.toolarium.enumeration.configuration.util.EnumUtil;
import com.github.toolarium.enumeration.configuration.validation.EnumKeyConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import java.util.Collection;
import java.util.List;
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
    private Map<Object, String> configurationKeyMap;
    private Map<String, Object> configurationKeyNameMap;
    private Map<Object, EnumKeyValueConfiguration> enumKeyValueConfigurationMap;

    
    /**
     * Constructor for AbstractBaseTypeEnumConfigurationStore
     */
    public AbstractBaseTypeEnumConfigurationStore() {
        super();
        configurationKeyMap = new ConcurrentHashMap<Object, String>();
        configurationKeyNameMap = new ConcurrentHashMap<String, Object>();
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
        
        String configurationKeyName = convertConfiguration(configurationKey);
        String value = loadConfiguration(configurationKeyName);
        EnumKeyValueConfiguration enumKeyValueConfiguration = getEnumKeyValueConfiguration(configurationKey);

        if (value == null && supportReturnDefaultValueIfMissing && enumKeyValueConfiguration != null && enumKeyValueConfiguration.getDefaultValue() != null && !enumKeyValueConfiguration.getDefaultValue().isEmpty()) {
            value = enumKeyValueConfiguration.getDefaultValue();
        }

        // special handling for binary types
        if (enumKeyValueConfiguration != null && EnumKeyValueConfigurationDataType.BINARY.equals(enumKeyValueConfiguration.getDataType()) && enumKeyValueConfiguration.getDefaultValue() != null) {
            try {
                if (!enumKeyValueConfiguration.getDefaultValue().equals(value)) {
                    IEnumKeyValueConfigurationBinaryObject valueData = StringTypeConverterFactory.getInstance().getStringTypeConverter().convert(enumKeyValueConfiguration.getDataType(), value);
                    if (valueData != null) {
                        // merge default value and value
                        IEnumKeyValueConfigurationBinaryObject defaultValueData = StringTypeConverterFactory.getInstance().getStringTypeConverter().convert(enumKeyValueConfiguration.getDataType(), enumKeyValueConfiguration.getDefaultValue());
                        EnumKeyValueConfigurationBinaryObject mergedValueData = new EnumKeyValueConfigurationBinaryObject(defaultValueData);
                        mergedValueData.merge(valueData);
                        value = EnumKeyValueConfigurationBinaryObjectParser.getInstance().format(mergedValueData);
                    }
                }
            } catch (ValidationException e) {
                String msg = "Invalid configuration found for key [" + configurationKeyName + "]: " + e.getMessage();
                LOG.debug(msg);
                
                EnumConfigurationStoreException ex = new EnumConfigurationStoreException(msg, e);
                ex.add(configurationKeyName, value);
                throw ex;
            }
        }

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
            String configurationKeyName = convertConfiguration(configurationKey);
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
            String configurationKeyName = convertConfiguration(configurationKey);
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
            writeConfiguration(convertConfiguration(configurationKey), val.toString());
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
        
        String configurationKeyName = convertConfiguration(configurationKey);
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
        String configurationKeyName = convertConfiguration(configurationKey);
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
            String configurationKeyName = convertConfiguration(configurationKey);
            result.setProperty(configurationKeyName, handlingNullObject(deleteConfiguration(configurationKeyName)));
        }
        
        return result;
    }

    
    /**
     * Cache the configuration key name
     * 
     * @param <T> the generic type
     * @param configurationKeyName the configuration key name
     * @return the configuration key
     * @throws EnumConfigurationStoreException In case of an invalid configuration key
     */
    /*
    protected <T extends Enum<T>> T convertConfiguration(String configurationKeyName) {
        if (configurationKeyName == null || configurationKeyName.isBlank()) {
            throw new EnumConfigurationStoreException("Invalid configuration name!");
        }
        
        // cache converted type
        @SuppressWarnings("unchecked")
        T configurationKey = (T)configurationKeyNameMap.get(configurationKeyName);
        if (configurationKey == null) {
            configurationKey = convertToConfigurationKey(configurationKeyName);
            
            if (configurationKey == null) {
                return null;
            }
            
            configurationKeyMap.put(configurationKey, configurationKeyName);
            configurationKeyNameMap.put(configurationKeyName, configurationKey);
        }
        
        return configurationKey;
    }
    */

    
    /**
     * Cache the configuration key
     *
     * @param <T> the generic type
     * @param configurationKey the configuration key
     * @return the configuration key name
     * @throws EnumConfigurationStoreException In case of an invalid configuration key
     */
    protected <T extends Enum<T>> String convertConfiguration(T configurationKey) {
        if (configurationKey == null) {
            throw new EnumConfigurationStoreException("Invalid configuration key!");
        }
        
        // cache converted type
        String configurationKeyName = configurationKeyMap.get(configurationKey);
        if (configurationKeyName == null) {
            configurationKeyName = convertToConfigurationKeyName(configurationKey);
            
            if (configurationKeyName == null) {
                return null;
            }
            
            configurationKeyMap.put(configurationKey, configurationKeyName);
            configurationKeyNameMap.put(configurationKeyName, configurationKey);
        }
        
        return configurationKeyName;
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

        String configurationKeyName = convertConfiguration(configurationKey);

        try {
            EnumKeyValueConfiguration enumKeyValueConfiguration = getEnumKeyValueConfiguration(configurationKey);
            Collection<D> valueList = EnumKeyConfigurationValidatorFactory.getInstance().getValidator().validate(enumKeyValueConfiguration, value);
            return prepareResult(value, valueList);
        } catch (ValidationException e) {
            String msg = "Invalid configuration found for key [" + configurationKeyName + "]: " + e.getMessage();
            LOG.debug(msg);
            EnumConfigurationStoreException ex = new EnumConfigurationStoreException(msg, e);
            ex.add(configurationKeyName, value);
            throw ex;
        }
    }

    
    /**
     * Convert the unique configuration key name into an enum configuration key type 
     *
     * @param <T> the generic type
     * @param configurationKeyName the configuration key name
     * @return the configuration key type
     */
    protected <T extends Enum<T>> T convertToConfigurationKey(String configurationKeyName) {
        if (configurationKeyName == null || configurationKeyName.isBlank()) {
            return null;
        }
        
        String[] split = splitKeyName(configurationKeyName); 
        if (split == null || split[0] == null || split[1] == null) {
            return null;
        }
        
        String className = split[0].trim();
        String enumName =  split[1].trim();
        
        List<Class<?>> classes = ClassPathUtil.getInstance().search(className, false);
        if (classes != null && !classes.isEmpty()) {
            for (Class<?> clazz : classes) {
                @SuppressWarnings("unchecked")
                T t = EnumUtil.getInstance().valueOf((Class<T>) clazz, enumName);
                if (t != null) {
                    return t;
                }
            }
        }
        
        return null;
    }

    
    /**
     * Convert configuration key type into a unique configuration key name
     *
     * @param <T> the generic type
     * @param configurationKey the configuration key
     * @return the configuration key name
     */
    protected <T extends Enum<T>> String convertToConfigurationKeyName(T configurationKey) {
        // in this sample we lowercase the class name. To resolve the class we need some extra effort
        return combineKeyName(configurationKey.getClass().getName().toLowerCase(), configurationKey.name().toLowerCase());
    }
    
    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.impl.AbstractBaseEnumConfigurationStore#getEnumKeyValueConfiguration(java.lang.String, boolean)
     */
    @Override
    protected EnumKeyValueConfiguration getEnumKeyValueConfiguration(String inputConfigurationKeyName, boolean ignoreCase) throws EnumConfigurationStoreException {
        EnumKeyValueConfiguration enumKeyValueConfiguration = enumKeyValueConfigurationMap.get(inputConfigurationKeyName);
        if (enumKeyValueConfiguration != null) {
            return enumKeyValueConfiguration;
        }
        
        return super.getEnumKeyValueConfiguration(inputConfigurationKeyName, ignoreCase);
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

        String cacheKey = combineKeyName(configurationKey.getClass().getName(), configurationKey.name()).toLowerCase();
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

                return getEnumKeyValueConfiguration(convertConfiguration(configurationKey), true);
            }
        }
        
        return enumKeyValueConfiguration;
    }
}
