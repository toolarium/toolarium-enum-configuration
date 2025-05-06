/*
 * AbstractBaseEnumConfigurationStore.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.impl;

import com.github.toolarium.enumeration.configuration.converter.StringTypeConverterFactory;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.SortedProperties;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationKeyResolver;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue;
import com.github.toolarium.enumeration.configuration.store.dto.EnumConfigurationValue;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import com.github.toolarium.enumeration.configuration.util.EnumKeyValueConfigurationBinaryObjectParser;
import com.github.toolarium.enumeration.configuration.util.JSONUtil;
import com.github.toolarium.enumeration.configuration.validation.EnumConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract base enum configuration store.
 * 
 * @author patrick
 */
public abstract class AbstractBaseEnumConfigurationStore implements IEnumConfigurationStore {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBaseEnumConfigurationStore.class);
    private static final String INVALID_INPUT_CONFIGURATION_KEY_NAME = "Invalid input configuration key name!";
    
    private IEnumConfigurationKeyResolver enumConfigurationKeyResolver;    

    
    /**
     * Constructor for AbstractBaseEnumConfigurationStore
     */
    public AbstractBaseEnumConfigurationStore() {
        enumConfigurationKeyResolver = new EnumConfigurationKeyResolver();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValue(java.lang.String)
     */
    @Override
    public <D> IEnumConfigurationValue<D> readConfigurationValue(String configurationKey) throws EnumConfigurationStoreException {
        return readConfigurationValue(configurationKey, true);
    }
    

    /**
     * Read a configuration value. 
     * In case of missing value a possible defined default value from the enum configuration annotation will be returned. 
     *
     * @param <D> the configuration value type
     * @param inputConfigurationKeyName The unique configuration key, started by the name in lower case and dot notation, separated by <code>#</code> and the key.
     * @param supportReturnDefaultValueIfMissing true to support default value
     * @return value the value
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be read 
     */
    protected <D> IEnumConfigurationValue<D> readConfigurationValue(String inputConfigurationKeyName, boolean supportReturnDefaultValueIfMissing) throws EnumConfigurationStoreException {
        if (inputConfigurationKeyName == null || inputConfigurationKeyName.isBlank()) {
            LOG.debug(INVALID_INPUT_CONFIGURATION_KEY_NAME);
            return null;
        }

        String configurationKeyName = inputConfigurationKeyName.trim();
        String value = prepareValue(getEnumKeyValueConfiguration(configurationKeyName), loadConfiguration(configurationKeyName), supportReturnDefaultValueIfMissing);

        IEnumConfigurationValue<D> result = null;
        if (value != null) {
            result = validate(configurationKeyName, value);
        }

        return result; 
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValueIgnoreDefault(java.lang.String)
     */
    @Override
    public <D> IEnumConfigurationValue<D> readConfigurationValueIgnoreDefault(String configurationKey) throws EnumConfigurationStoreException {
        return readConfigurationValue(configurationKey, false);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#writeConfigurationValue(java.lang.String, java.lang.String)
     */
    @Override
    public <D> void writeConfigurationValue(String inputConfigurationKeyName, String value) throws EnumConfigurationStoreException {
        if (inputConfigurationKeyName == null || inputConfigurationKeyName.isBlank()) {
            LOG.debug(INVALID_INPUT_CONFIGURATION_KEY_NAME);
            return;
        }

        String configurationKeyName = inputConfigurationKeyName.trim();
        validate(configurationKeyName, value);
        writeConfiguration(configurationKeyName, value);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#writeConfigurationValue(java.lang.String, java.lang.Object)
     */
    @Override
    public <T extends Enum<T>> void writeConfigurationValue(String inputConfigurationKeyName, Object configurationValue) throws EnumConfigurationStoreException {
        if (inputConfigurationKeyName == null || inputConfigurationKeyName.isBlank()) {
            LOG.debug(INVALID_INPUT_CONFIGURATION_KEY_NAME);
            return;
        }

        String configurationKeyName = inputConfigurationKeyName.trim();
        String value = convertObjectToString(configurationKeyName, getEnumKeyValueConfiguration(configurationKeyName), configurationValue);
        validate(configurationKeyName, value);
        writeConfiguration(configurationKeyName, value);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValueList(java.lang.String[])
     */
    @Override
    public Properties readConfigurationValueList(String... inputConfigurationKeyNames) throws EnumConfigurationStoreException {
        Properties result = new SortedProperties();

        String[] configurationKeyNames = prepareConfigurationNames(inputConfigurationKeyNames);
        if (configurationKeyNames == null) {
            LOG.debug("Invalid input configuration key names!");
            return result;
        }
        
        if (configurationKeyNames != null && configurationKeyNames.length > 0) {
            for (String inputConfigurationKeyName : configurationKeyNames) {
                if (inputConfigurationKeyName != null && !inputConfigurationKeyName.isBlank()) {
                    String configurationKeyName = inputConfigurationKeyName.trim();
                    IEnumConfigurationValue<?> value = readConfigurationValue(configurationKeyName);
                    result.setProperty(configurationKeyName, handlingNullObject(value));
                }
            }
        }
        
        return result;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValueListIgnoreDefault(java.lang.String[])
     */
    @Override
    public Properties readConfigurationValueListIgnoreDefault(String... inputConfigurationKeyNames) throws EnumConfigurationStoreException {
        Properties result = new SortedProperties();
        String[] configurationKeyNames = prepareConfigurationNames(inputConfigurationKeyNames);
        if (configurationKeyNames == null) {
            LOG.debug("Invalid input configuration key names!");
            return result;
        }
        
        if (configurationKeyNames != null && configurationKeyNames.length > 0) {
            for (String inputConfigurationKeyName : configurationKeyNames) {
                if (inputConfigurationKeyName != null && !inputConfigurationKeyName.isBlank()) {
                    String configurationKeyName = inputConfigurationKeyName.trim();
                    IEnumConfigurationValue<?> value = readConfigurationValueIgnoreDefault(configurationKeyName);
                    result.setProperty(configurationKeyName, handlingNullObject(value));
                }
            }
        }
        
        return result;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#writeConfigurationValueList(java.util.Properties)
     */
    @Override
    public void writeConfigurationValueList(Properties configuration) throws EnumConfigurationStoreException {
        writeConfigurationValueList(configuration, false);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#writeConfigurationValueList(java.util.Properties, boolean)
     */
    @Override
    public Properties writeConfigurationValueList(Properties configuration, boolean removeEntriesWithMissingKey) throws EnumConfigurationStoreException {
        if (configuration == null || configuration.isEmpty()) {
            LOG.debug("Invalid input configuration!");
            return new SortedProperties();
        }

        Set<String> keySetToDelete = null;
        if (removeEntriesWithMissingKey) {
            keySetToDelete = readKeys();
        }

        for (Map.Entry<Object, Object> e: configuration.entrySet()) {
            String configurationKeyName = ("" + e.getKey()).trim();
            if (configurationKeyName != null && !configurationKeyName.isBlank()) {

                String valueStr = "" + e.getValue();
                if (valueStr != null && !valueStr.isEmpty()) {
                    // remove the updated key from set
                    if (removeEntriesWithMissingKey && keySetToDelete != null) {
                        keySetToDelete.remove(configurationKeyName);
                    }
                    
                    writeConfigurationValue(configurationKeyName, valueStr);
                } 
            }
        }

        if (!removeEntriesWithMissingKey || keySetToDelete == null || keySetToDelete.isEmpty()) {
            return new SortedProperties();
        }

        String[] arrayOfString = Arrays.copyOf(keySetToDelete.toArray(), keySetToDelete.size(), String[].class);
        return deleteConfigurationValueList(arrayOfString);
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#deleteConfigurationValue(java.lang.String)
     */
    @Override
    public <D> IEnumConfigurationValue<D> deleteConfigurationValue(String inputConfigurationKeyName) throws EnumConfigurationStoreException {
        if (inputConfigurationKeyName == null || inputConfigurationKeyName.isBlank()) {
            LOG.debug(INVALID_INPUT_CONFIGURATION_KEY_NAME);
            return null;
        }

        IEnumConfigurationValue<D> result = readConfigurationValueIgnoreDefault(inputConfigurationKeyName);
        deleteConfiguration(inputConfigurationKeyName.trim());
        return result;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#deleteConfigurationValueList(java.lang.String[])
     */
    @Override
    public Properties deleteConfigurationValueList(String... inputConfigurationKeyNames) throws EnumConfigurationStoreException {
        Properties result = new SortedProperties();
        String[] configurationKeyNames = prepareConfigurationNames(inputConfigurationKeyNames);
        if (configurationKeyNames == null) {
            LOG.debug("Invalid input configuration key names!");
            return result;
        }

        if (configurationKeyNames != null && configurationKeyNames.length > 0) {
            for (String inputConfigurationKeyName : configurationKeyNames) {
                if (inputConfigurationKeyName != null && !inputConfigurationKeyName.isBlank()) {
                    String configurationKeyName = inputConfigurationKeyName.trim();
                    result.setProperty(configurationKeyName, handlingNullObject(deleteConfiguration(configurationKeyName)));
                }
            }
        }
        
        return result;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#getEnumConfigurationKeyResolver()
     */
    @Override
    public IEnumConfigurationKeyResolver getEnumConfigurationKeyResolver() {
        return enumConfigurationKeyResolver;        
    }

    
    /**
     * Sets the {@link IEnumConfigurationKeyResolver}.
     *
     * @param enumConfigurationKeyResolver the resolver
     */
    public void setEnumConfigurationKeyResolver(IEnumConfigurationKeyResolver enumConfigurationKeyResolver) {
        this.enumConfigurationKeyResolver = enumConfigurationKeyResolver;        
    }

    
    /**
     * Prepare the read value
     * 
     * @param enumKeyValueConfiguration the enum key value configuration
     * @param inputValue the read value
     * @param supportReturnDefaultValueIfMissing true to support default value
     * @return value the value
     */
    protected String prepareValue(EnumKeyValueConfiguration enumKeyValueConfiguration, String inputValue, boolean supportReturnDefaultValueIfMissing) {
        String value = inputValue;

        if (value == null && supportReturnDefaultValueIfMissing && enumKeyValueConfiguration != null && enumKeyValueConfiguration.getDefaultValue() != null && !enumKeyValueConfiguration.getDefaultValue().isEmpty()) {
            value = enumKeyValueConfiguration.getDefaultValue();
        }

        // special handling for binary types
        try {
            value = EnumKeyValueConfigurationBinaryObjectParser.getInstance().mergeDefaultValues(enumKeyValueConfiguration, value);
        } catch (EnumConfigurationStoreException ex) {
            LOG.debug(ex.getMessage());
            throw ex;
        }
        
        return value;
    }

    
    /**
     * Validate the configuration value
     *
     * @param <D> the configuration value type
     * @param inputConfigurationKeyName the configuration key name
     * @param value the value
     * @return the converted value
     * @throws EnumConfigurationStoreException In case of a load exception
     */
    protected <D> IEnumConfigurationValue<D> validate(String inputConfigurationKeyName, String value) throws EnumConfigurationStoreException {
        if (inputConfigurationKeyName == null) {
            LOG.debug(INVALID_INPUT_CONFIGURATION_KEY_NAME);
            throw new EnumConfigurationStoreException("Invalid configuration key name!");
        }
        
        String configurationKeyName = inputConfigurationKeyName;
        
        try {
            EnumKeyValueConfiguration enumKeyValueConfiguration = getEnumKeyValueConfiguration(configurationKeyName);
            Collection<D> valueList = EnumConfigurationValidatorFactory.getInstance().getValueValidator().validate(enumKeyValueConfiguration, value);
            return prepareResult(value, valueList);
        } catch (ValidationException ex) {
            String msg = "Invalid configuration found for key [" + configurationKeyName + "]: " + ex.getMessage();
            LOG.warn(msg);
            EnumConfigurationStoreException e = new EnumConfigurationStoreException(msg, ex);
            e.add(configurationKeyName, value, ex.getConvertedValueList());
            throw e;
        }
    }

    
    /**
     * Prepare result
     * 
     * @param <D>    the configuration value type
     * @param result the result to prepare
     * @param values the corresponding values
     * @return the configuration value
     */
    protected <D> IEnumConfigurationValue<D> prepareResult(String result, Collection<D> values) {
        return new EnumConfigurationValue<D>(result, values);
    }
   

    /**
     * Load the configuration from a source. The configurationKeyName and the value are pure string based.
     * 
     * @param configurationKeyName the unique configuration key
     * @return the configuration
     * @throws EnumConfigurationStoreException In case of a load exception
     */
    protected abstract String loadConfiguration(String configurationKeyName) throws EnumConfigurationStoreException;

    
    /**
     * Write the configuration to a source. The configurationKeyName and the value are pure string based.
     * 
     * @param <D> the configuration value type
     * @param configurationKeyName the unique configuration key
     * @param configurationValue the configuration value
     * @throws EnumConfigurationStoreException In case of a load exception
     */
    protected abstract <D> void writeConfiguration(String configurationKeyName, String configurationValue) throws EnumConfigurationStoreException;

    
    /**
     * Delete the configuration from a source. The configurationKeyName and the value are pure string based.
     * 
     * @param <D> the configuration value type
     * @param configurationKeyName the unique configuration key
     * @return the deleted configuration value
     * @throws EnumConfigurationStoreException In case of a load exception
     */
    protected abstract <D> String deleteConfiguration(String configurationKeyName) throws EnumConfigurationStoreException;

    
    /**
     * Read all keys.
     * 
     * @return the unique configuration key list
     * @throws EnumConfigurationStoreException In case of a load exception
     */
    protected abstract Set<String> readKeys() throws EnumConfigurationStoreException;


    /**
     * Get the enum key / value configuration information of an enum configuration name / key. 
     * It can be get either by interpreting of the annotation of the enum configuration or to 
     * load the previous generated JSON from internal / external source.
     *
     * @param inputConfigurationKeyName the configuration key name
     * @return the enum key value configuration
     * @throws EnumConfigurationStoreException In case of an enum configuration store execption
     */
    protected EnumKeyValueConfiguration getEnumKeyValueConfiguration(String inputConfigurationKeyName) throws EnumConfigurationStoreException {
        if (inputConfigurationKeyName == null) {
            LOG.debug(INVALID_INPUT_CONFIGURATION_KEY_NAME);
            return null;
        }

        return getEnumConfigurationKeyResolver().getEnumKeyValueConfiguration(inputConfigurationKeyName);
    }

    
    /**
     * Convert an object into a string
     * 
     * @param <T> the generic type
     * @param configurationName the configuration name
     * @param enumKeyValueConfiguration the enum key / value configuration 
     * @param configurationValue the configuration value
     * @return the converted string
     */
    @SuppressWarnings("unchecked")
    protected <T extends Enum<T>> String convertObjectToString(String configurationName, EnumKeyValueConfiguration enumKeyValueConfiguration, Object configurationValue) {
        String value;
        
        if (configurationValue == null) {
            value = null;
        } else if (configurationValue.getClass().isArray() || configurationValue.getClass().isAssignableFrom(Collection.class)) {
            List<String> list = new ArrayList<>();

            EnumConfigurationStoreException ex = null;
            if (configurationValue.getClass().isArray()) {

                Object[] array = (Object[])configurationValue;
                for (int i = 0; i < array.length; i++) {
                    try {
                        String str = StringTypeConverterFactory.getInstance().getStringTypeConverter().format(enumKeyValueConfiguration.getDataType(), array[i]);
                        if (str != null) {
                            list.add(str);
                        }
                    } catch (ValidationException e) {
                        String msg = "Invalid configuration";
                        if (ex == null) {
                            ex = new EnumConfigurationStoreException(msg, e);
                        }
                        ex.add(configurationName, "" + array[i], e.getConvertedValueList());
                        LOG.warn(msg + " found for key [" + configurationName + "]: " + e.getMessage());
                    }
                }
            } else if (configurationValue.getClass().isAssignableFrom(Collection.class)) {
                for (Object object : (Collection<Object>) configurationValue) {
                    try {
                        list.add(StringTypeConverterFactory.getInstance().getStringTypeConverter().format(enumKeyValueConfiguration.getDataType(), object));
                    } catch (ValidationException e) {
                        String msg = "Invalid configuration";
                        if (ex == null) {
                            ex = new EnumConfigurationStoreException(msg, e);
                        }
                        ex.add(configurationName, "" + object, e.getConvertedValueList());
                        LOG.warn(msg + " found for key [" + configurationName + "]: " + e.getMessage());
                    }
                }
            } 

            if (ex != null) {
                throw ex;
            }
            
            value = JSONUtil.getInstance().convert(list);
        } else {
            value = configurationValue.toString();
        }
        
        return value;
    }
    
    
    /**
     * Handling of null objects
     *
     * @param obj the object
     * @return the string
     */
    protected String handlingNullObject(Object obj) {
        if (obj != null) {
            return obj.toString();
        }

        return "";
    }

    
    /**
     * Prepare configuration key names
     *
     * @param inputConfigurationKeyNames the input name list. In case it's null or empty then all keys will be read
     * @return the configuration key names to read
     * @throws EnumConfigurationStoreException in case the enum configuration cannot be accessed 
     */
    protected String[] prepareConfigurationNames(String... inputConfigurationKeyNames) throws EnumConfigurationStoreException {
        String[] configurationKeyNames = inputConfigurationKeyNames;

        List<String> list = null;
        if (configurationKeyNames != null && configurationKeyNames.length > 0) {
            list = new ArrayList<>();
            for (String configurationKeyName : configurationKeyNames) {
                if (configurationKeyName != null && !configurationKeyName.isBlank()) {
                    list.add(configurationKeyName);
                }
            }
        }
        
        if (list == null || list.isEmpty()) {
            Set<String> keySet = readKeys();
            if (keySet != null) {
                return keySet.toArray(String[]::new);
            } else {
                return null;
            }
        }

        return list.toArray(String[]::new);
    }
}
