/*
 * AbstractBaseEnumConfigurationStore.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.impl;

import com.github.toolarium.enumeration.configuration.converter.StringTypeConverterFactory;
import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.IEnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.SortedProperties;
import com.github.toolarium.enumeration.configuration.resource.EnumConfigurationResourceFactory;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationResourceResolver;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue;
import com.github.toolarium.enumeration.configuration.store.dto.EnumConfigurationValue;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import com.github.toolarium.enumeration.configuration.util.EnumKeyValueConfigurationBinaryObjectParser;
import com.github.toolarium.enumeration.configuration.util.JSONUtil;
import com.github.toolarium.enumeration.configuration.validation.EnumKeyConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;
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
    
    private static final String CONFIGURATION_KEY_SEPARATOR = "#";
    private static final String INVALID_INPUT_CONFIGURATION_KEY_NAME = "Invalid input configuration key name!";
    
    private IEnumConfigurationResourceResolver enumConfigurationResourceResolver;
    private EnumConfigurations loadedEnumConfigurations;
    private String configurationKeySeparator;
    private boolean ignoreCase;

    
    /**
     * Constructor for AbstractBaseEnumConfigurationStore
     */
    public AbstractBaseEnumConfigurationStore() {
        enumConfigurationResourceResolver = null;
        loadedEnumConfigurations = null;
        configurationKeySeparator = CONFIGURATION_KEY_SEPARATOR;
        ignoreCase = true;
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
        String value = loadConfiguration(configurationKeyName);
        EnumKeyValueConfiguration enumKeyValueConfiguration = getEnumKeyValueConfiguration(configurationKeyName, ignoreCase);

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
        String value = convertObjectToString(configurationKeyName, getEnumKeyValueConfiguration(configurationKeyName, ignoreCase), configurationValue);
        validate(configurationKeyName, value);
        writeConfiguration(configurationKeyName, value);
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValueList(java.lang.String[])
     */
    @Override
    public Properties readConfigurationValueList(String... configurationKeyNames) throws EnumConfigurationStoreException {
        Properties result = new SortedProperties();
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
    public Properties readConfigurationValueListIgnoreDefault(String... configurationKeyNames) throws EnumConfigurationStoreException {
        Properties result = new SortedProperties();
        if (configurationKeyNames == null || configurationKeyNames.length == 0) {
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
    public Properties deleteConfigurationValueList(String... configurationKeyNames) throws EnumConfigurationStoreException {
        Properties result = new SortedProperties();
        if (configurationKeyNames == null || configurationKeyNames.length == 0) {
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
     * Sets the {@link IEnumConfigurationResourceResolver}.
     *
     * @param enumConfigurationResourceResolver the resolver
     */
    public void setEnumConfigurationResourceResolver(IEnumConfigurationResourceResolver enumConfigurationResourceResolver) {
        this.enumConfigurationResourceResolver = enumConfigurationResourceResolver;
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
            EnumKeyValueConfiguration enumKeyValueConfiguration = getEnumKeyValueConfiguration(configurationKeyName, ignoreCase);
            Collection<D> valueList = EnumKeyConfigurationValidatorFactory.getInstance().getValidator().validate(enumKeyValueConfiguration, value);
            return prepareResult(value, valueList);
        } catch (ValidationException e) {
            StringBuilder msg = new StringBuilder();
            msg.append("Invalid configuration found for key [");
            msg.append(configurationKeyName);
            msg.append("]: ");                
            msg.append(e.getMessage());                
                
            EnumConfigurationStoreException ex = new EnumConfigurationStoreException(msg.toString(), e);
            ex.add(configurationKeyName, value);
            
            LOG.warn(ex.getMessage());
            throw ex;
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
     * @param ignoreCase true to ignore case
     * @return the enum key value configuration
     * @throws EnumConfigurationStoreException In case of an enum configuration store execption
     */
    @SuppressWarnings("unchecked")
    protected EnumKeyValueConfiguration getEnumKeyValueConfiguration(String inputConfigurationKeyName, boolean ignoreCase) throws EnumConfigurationStoreException {
        if (inputConfigurationKeyName == null) {
            LOG.debug(INVALID_INPUT_CONFIGURATION_KEY_NAME);
            return null;
        }

        String configurationKeyName = inputConfigurationKeyName;
        if (loadedEnumConfigurations == null) {
            LOG.debug("Try to resolve configuration key [" + inputConfigurationKeyName + "]...");
            InputStream enumConfigurationResourceInputStream = getEnumConfigurationResourceInputStream();
            if (enumConfigurationResourceInputStream != null) {
                LOG.debug("Load enum configuration information for key [" + configurationKeyName + "]...");

                try {
                    loadedEnumConfigurations = EnumConfigurationResourceFactory.getInstance().load(enumConfigurationResourceInputStream);
                    if (loadedEnumConfigurations != null) {
                        LOG.info("Successful load enum configuration [" + loadedEnumConfigurations.getName() + " v" + loadedEnumConfigurations.getVersion() + "].");
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Loaded enum configuration information: " + loadedEnumConfigurations);
                        }
                    } else {
                        LOG.warn("Could not load enum configuration information for key [" + configurationKeyName + "]!");
                    }

                    try {
                        enumConfigurationResourceInputStream.close();                                
                    } catch (Exception ce) {
                        // null
                    }
                    
                } catch (IOException ioex) {
                    LOG.warn("Could not load enum configuration information for key [" + configurationKeyName + "], invalid content:\n" + ioex.getMessage());
                }
            }
        }
        
        EnumKeyValueConfiguration enumKeyValueConfiguration = null;
        if (loadedEnumConfigurations != null) {
            String[] configurationKeyNameSplit = splitKeyName(configurationKeyName);
            
            EnumConfiguration<EnumKeyValueConfiguration> loadedEnumConfiguration = null;
            if (ignoreCase) {
                Set<EnumConfiguration<? extends EnumKeyConfiguration>> set = loadedEnumConfigurations.getEnumConfigurationList();
                if (set != null) {
                    for (EnumConfiguration<? extends EnumKeyConfiguration> e : set) {
                        if (e.getName().equalsIgnoreCase(configurationKeyNameSplit[0])) {
                            //@SuppressWarnings("unchecked")
                            loadedEnumConfiguration = (EnumConfiguration<EnumKeyValueConfiguration>)e;
                            break;
                        }
                    }
                }
            } else {
                loadedEnumConfiguration = (EnumConfiguration<EnumKeyValueConfiguration>)loadedEnumConfigurations.get(configurationKeyNameSplit[0]);
            }
            
            if (loadedEnumConfiguration != null) {
                for (EnumKeyValueConfiguration restoredEnumKeyValueConfiguration : loadedEnumConfiguration.getKeyList()) {
                    if ((ignoreCase && configurationKeyNameSplit[1].equalsIgnoreCase(restoredEnumKeyValueConfiguration.getKey())) 
                        || (!ignoreCase && configurationKeyNameSplit[1].equals(restoredEnumKeyValueConfiguration.getKey()))) {
                        // found;
                        LOG.debug("Resolved enum configuration for key [" + configurationKeyName + "].");
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Resolved enum configuration for key [" + configurationKeyName + "]: " + loadedEnumConfiguration);
                        }
                        
                        enumKeyValueConfiguration = restoredEnumKeyValueConfiguration;
                        break;
                    }
                }
            }
            
            if (enumKeyValueConfiguration == null) {
                LOG.warn("Could not resolve enum configuration for key [" + configurationKeyName + "]!");
            }
        }                
        
        return enumKeyValueConfiguration;
    }

    
    /**
     * Split the key name
     *
     * @param inputConfigurationKeyName the configuration key name
     * @return the split
     */
    public String[] splitKeyName(String inputConfigurationKeyName) {
        if (inputConfigurationKeyName == null || inputConfigurationKeyName.isBlank()) {
            LOG.debug(INVALID_INPUT_CONFIGURATION_KEY_NAME);
            return null;
        }

        String configurationKeyName = inputConfigurationKeyName.trim();
        String[] split = configurationKeyName.split(configurationKeySeparator);
        if (split.length < 2) {
            return null;
        }
        
        split[0] = split[0].trim();
        split[1] = split[1].trim();
        return split;
    }

    
    /**
     * Combine key name
     *
     * @param configurationName the configuration name
     * @param keyName the key name
     * @return the combined key
     */
    public String combineKeyName(String configurationName, String keyName) {
        if (configurationName == null || configurationName.isBlank()) {
            LOG.debug("Invalid input configuration name!");
            return null;
        }

        if (keyName == null || keyName.isBlank()) {
            LOG.debug(INVALID_INPUT_CONFIGURATION_KEY_NAME);
            return null;
        }

        return new StringBuilder(configurationName).append(configurationKeySeparator).append(keyName).toString();
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
        if (configurationValue.getClass().isArray() || configurationValue.getClass().isAssignableFrom(Collection.class)) {
            List<String> list = new ArrayList<>();

            
            EnumConfigurationStoreException ex = null;
            if (configurationValue.getClass().isArray()) {

                Object[] array = (Object[])configurationValue;
                for (int i = 0; i < array.length; i++) {
                    try {
                        list.add(StringTypeConverterFactory.getInstance().getStringTypeConverter().format(enumKeyValueConfiguration.getDataType(), array[i]));
                    } catch (ValidationException e) {
                        String msg = "Invalid configuration";
                        if (ex == null) {
                            ex = new EnumConfigurationStoreException(msg, e);
                        }
                        
                        ex.add(configurationName, "" + array[i]);
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
                        ex.add(configurationName, "" + object);
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
     * Get the enum configuration resource input stream
     * 
     * @return the enum configuration resource input stream or null
     * @throws EnumConfigurationStoreException In case of not accessible resource
     */
    protected InputStream getEnumConfigurationResourceInputStream() throws EnumConfigurationStoreException {
        if (enumConfigurationResourceResolver == null) {
            throw new EnumConfigurationStoreException("Not supported resource input stream!");
        }
        
        return enumConfigurationResourceResolver.getEnumConfigurationResourceStream();
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
}