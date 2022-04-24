/*
 * EnumConfigurationKeyResolver.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.impl;

import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.resource.EnumConfigurationResourceFactory;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationKeyResolver;
import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationResourceResolver;
import com.github.toolarium.enumeration.configuration.store.exception.EnumConfigurationStoreException;
import com.github.toolarium.enumeration.configuration.util.ClassPathUtil;
import com.github.toolarium.enumeration.configuration.util.EnumUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the {@link IEnumConfigurationKeyResolver}.
 * 
 * @author patrick
 */
public class EnumConfigurationKeyResolver implements IEnumConfigurationKeyResolver {
    private static final Logger LOG = LoggerFactory.getLogger(EnumConfigurationKeyResolver.class);
    private static final String CONFIGURATION_KEY_SEPARATOR = "#";
    
    private Map<Object, String> configurationKeyMap;
    private Map<String, Object> configurationKeyNameMap;
    private Map<String, EnumConfigurations> loadedEnumConfigurationsMap;
    private IEnumConfigurationResourceResolver enumConfigurationResourceResolver;
    private boolean ignoreCase;

    
    /**
     * Constructor for EnumConfigurationKeyResolver
     */
    public EnumConfigurationKeyResolver() {
        this(null, true);
    }

    
    /**
     * Constructor for EnumConfigurationKeyResolver
     * 
     * @param enumConfigurationResourceResolver the enum configuration resource resolver
     */
    public EnumConfigurationKeyResolver(IEnumConfigurationResourceResolver enumConfigurationResourceResolver) {
        this(enumConfigurationResourceResolver, true);
    }

    
    /**
     * Constructor for EnumConfigurationKeyResolver
     * 
     * @param enumConfigurationResourceResolver the enum configuration resource resolver
     * @param ignoreCase true or false
     */
    public EnumConfigurationKeyResolver(IEnumConfigurationResourceResolver enumConfigurationResourceResolver, boolean ignoreCase) {
        configurationKeyMap = new ConcurrentHashMap<Object, String>();
        configurationKeyNameMap = new ConcurrentHashMap<String, Object>();
        loadedEnumConfigurationsMap = new ConcurrentHashMap<String, EnumConfigurations>();
        this.enumConfigurationResourceResolver = enumConfigurationResourceResolver;
        this.ignoreCase = ignoreCase;
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationKeyResolver#createConfigurationKeyName(java.lang.String, java.lang.String)
     */
    @Override
    public String createConfigurationKeyName(String configurationName, String keyName) {
        if (configurationName == null || configurationName.isBlank()) {
            return null;
        }

        if (keyName == null || keyName.isBlank()) {
            return null;
        }

        return new StringBuilder(configurationName).append(getConfigurationKeySeparator()).append(keyName).toString().toLowerCase();
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationKeyResolver#resolveConfigurationKeyName(java.lang.Enum)
     */
    @Override
    public <T extends Enum<T>> String resolveConfigurationKeyName(T configurationKey) {
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
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationKeyResolver#resolveConfigurationKey(java.lang.String)
     */
    @Override
    public <T extends Enum<T>> T resolveConfigurationKey(String configurationKeyName) {
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

    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationKeyResolver#getEnumKeyValueConfiguration(java.lang.String)
     * 
     * @param inputConfigurationKeyName the configuration key name
     * @return the enum key value configuration
     * @throws EnumConfigurationStoreException In case of an enum configuration store exception
     */
    @SuppressWarnings("unchecked")
    public EnumKeyValueConfiguration getEnumKeyValueConfiguration(String inputConfigurationKeyName) throws EnumConfigurationStoreException {
        if (inputConfigurationKeyName == null) {
            LOG.debug("Invalid input configuration key name!");
            return null;
        }

        String configurationKeyName = inputConfigurationKeyName;
        String[] configurationKeyNameSplit = splitKeyName(configurationKeyName);
        if (configurationKeyNameSplit == null || configurationKeyNameSplit.length < 2) {
            return null;
        }
        
        EnumConfigurations loadedEnumConfigurations = loadedEnumConfigurationsMap.get(configurationKeyNameSplit[0]);
        if (loadedEnumConfigurations == null) {
            LOG.debug("Try to resolve configuration key [" + configurationKeyNameSplit[0] + "]...");
            
            if (enumConfigurationResourceResolver == null) {
                throw new EnumConfigurationStoreException("Not supported resource input stream!");
            }
            
            InputStream enumConfigurationResourceInputStream = enumConfigurationResourceResolver.getEnumConfigurationResourceStream(configurationKeyNameSplit[0], ignoreCase);
            if (enumConfigurationResourceInputStream != null) {
                LOG.debug("Load enum configuration information for key [" + configurationKeyNameSplit[0] + "]...");

                try {
                    loadedEnumConfigurations = EnumConfigurationResourceFactory.getInstance().load(enumConfigurationResourceInputStream);
                    if (loadedEnumConfigurations != null) {
                        LOG.info("Successful load enum configuration [" + loadedEnumConfigurations.getName() + " v" + loadedEnumConfigurations.getVersion() + "].");
                        loadedEnumConfigurationsMap.put(configurationKeyNameSplit[0], loadedEnumConfigurations);
                        
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Loaded enum configuration information: " + loadedEnumConfigurations);
                        }
                    } else {
                        LOG.warn("Could not load enum configuration information for key [" + configurationKeyNameSplit[0] + "]!");
                    }

                    try {
                        enumConfigurationResourceInputStream.close();                                
                    } catch (Exception ce) {
                        // null
                    }
                    
                } catch (IOException ioex) {
                    LOG.warn("Could not load enum configuration information for key [" + configurationKeyNameSplit[0] + "], invalid content:\n" + ioex.getMessage());
                }
            }
        } else {
            LOG.debug("Found configuration " + configurationKeyNameSplit[0] + "] in cache.");
        }
        
        EnumKeyValueConfiguration enumKeyValueConfiguration = null;
        if (loadedEnumConfigurations != null) {
            LOG.debug("Select configuration [" + configurationKeyNameSplit[0] + "]...");
            EnumConfiguration<EnumKeyValueConfiguration> loadedEnumConfiguration = null;
            if (ignoreCase) {
                Set<EnumConfiguration<? extends EnumKeyConfiguration>> set = loadedEnumConfigurations.getEnumConfigurationList();
                if (set != null) {
                    for (EnumConfiguration<? extends EnumKeyConfiguration> e : set) {
                        if (e.getName().equalsIgnoreCase(configurationKeyNameSplit[0])) {
                            LOG.debug("Configuration key [" + configurationKeyName + "] found (" + e.getName() + ").");
                            loadedEnumConfiguration = (EnumConfiguration<EnumKeyValueConfiguration>)e;
                            break;
                        }
                    }
                }
            } else {
                loadedEnumConfiguration = (EnumConfiguration<EnumKeyValueConfiguration>)loadedEnumConfigurations.get(configurationKeyNameSplit[0]);
                if (loadedEnumConfiguration != null) {
                    LOG.debug("Configuration key [" + configurationKeyName + "] found.");
                }
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
     * Clear cache
     */
    public void clearCache() {
        configurationKeyMap.clear();
        configurationKeyNameMap.clear();
        loadedEnumConfigurationsMap.clear();
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
        return createConfigurationKeyName(configurationKey.getClass().getName().toLowerCase(), configurationKey.name().toLowerCase());
    }


    /**
     * Split the key name
     *
     * @param inputConfigurationKeyName the configuration key name
     * @return the split
     */
    protected String[] splitKeyName(String inputConfigurationKeyName) {
        if (inputConfigurationKeyName == null || inputConfigurationKeyName.isBlank()) {
            return null;
        }

        String configurationKeyName = inputConfigurationKeyName.trim();
        String[] split = configurationKeyName.split(getConfigurationKeySeparator());
        if (split.length < 2) {
            return null;
        }
        
        split[0] = split[0].trim();
        split[1] = split[1].trim();
        return split;
    }


    /**
     * Get the configuration key separator
     *
     * @return the configuration key separator
     */
    protected String getConfigurationKeySeparator() {
        return CONFIGURATION_KEY_SEPARATOR;
    }
}
