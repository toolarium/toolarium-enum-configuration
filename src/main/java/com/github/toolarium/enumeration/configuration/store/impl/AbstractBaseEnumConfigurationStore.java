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
import com.github.toolarium.enumeration.configuration.resource.EnumConfigurationResourceFactory;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationValue;
import com.github.toolarium.enumeration.configuration.store.dto.EnumConfigurationValue;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import com.github.toolarium.enumeration.configuration.util.EnumKeyValueConfigurationBinaryObjectParser;
import com.github.toolarium.enumeration.configuration.validation.EnumKeyConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
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
    
    private EnumConfigurations loadedEnumConfigurations;
    private String configurationKeySeparator;
    private boolean supportReturnDefaultValueIfMissing;
    private boolean ignoreCase;

    
    /**
     * Constructor for AbstractBaseEnumConfigurationStore
     */
    public AbstractBaseEnumConfigurationStore() {
        loadedEnumConfigurations = null;
        configurationKeySeparator = CONFIGURATION_KEY_SEPARATOR;
        supportReturnDefaultValueIfMissing = true;
        ignoreCase = true;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValue(java.lang.String)
     */
    @Override
    public <D> IEnumConfigurationValue<D> readConfigurationValue(String inputConfigurationKeyName) throws EnumConfigurationStoreException {
        if (inputConfigurationKeyName == null || inputConfigurationKeyName.isBlank()) {
            LOG.debug(INVALID_INPUT_CONFIGURATION_KEY_NAME);
            return null;
        }

        String configurationKeyName = inputConfigurationKeyName.trim();
        String value = loadConfiguration(configurationKeyName);
        EnumKeyValueConfiguration enumKeyValueConfiguration = getEnumKeyValueConfiguration(inputConfigurationKeyName, ignoreCase);

        if (value == null && supportReturnDefaultValueIfMissing() && enumKeyValueConfiguration != null && enumKeyValueConfiguration.getDefaultValue() != null && !enumKeyValueConfiguration.getDefaultValue().isEmpty()) {
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
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#readConfigurationValueList(java.lang.String[])
     */
    @Override
    public Properties readConfigurationValueList(String... configurationKeyNames) throws EnumConfigurationStoreException {
        if (configurationKeyNames == null) {
            LOG.debug("Invalid input configuration key names!");
            return null;
        }
        
        Properties result = new Properties();
        if (configurationKeyNames != null && configurationKeyNames.length > 0) {
            for (String inputConfigurationKeyName : configurationKeyNames) {
                if (inputConfigurationKeyName != null && !inputConfigurationKeyName.isBlank()) {
                    String configurationKeyName = inputConfigurationKeyName.trim();
                    result.setProperty(configurationKeyName, readConfigurationValue(configurationKeyName).toString());
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
        if (configuration == null || configuration.size() <= 0) {
            LOG.debug("Invalid input configuration!");
            return;
        }

        for (Map.Entry<Object, Object> e: configuration.entrySet()) {
            String configurationKeyName = ("" + e.getKey()).trim();
            if (configurationKeyName != null && !configurationKeyName.isBlank()) {
                writeConfigurationValue(configurationKeyName, "" + e.getValue());
            }
        }
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationStore#supportReturnDefaultValueIfMissing()
     */
    @Override
    public boolean supportReturnDefaultValueIfMissing() {
        return supportReturnDefaultValueIfMissing;
    }


    /**
     * Defines if the configuration store returns the default value in case of a missing value.
     *
     * @param supportReturnDefaultValueIfMissing true if it will return the default value in case of a missing value.
     */
    protected void setSupportReturnDefaultValueIfMissing(boolean supportReturnDefaultValueIfMissing) {
        this.supportReturnDefaultValueIfMissing = supportReturnDefaultValueIfMissing;
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
     * Get the enum configuration resource input stream
     * 
     * @return the enum configuration resource input stream or null
     * @throws EnumConfigurationStoreException In case of not accessible resource
     */
    protected InputStream getEnumConfigurationResourceInputStream() throws EnumConfigurationStoreException {
        throw new EnumConfigurationStoreException("Not supported resource input stream!");
    }
}
