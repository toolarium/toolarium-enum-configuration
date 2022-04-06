/*
 * IEnumConfigurationResourceResolver.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import java.io.InputStream;


/**
 * Defines the  {@link EnumConfigurations} resolver.
 *   
 * @author patrick
 */
public interface IEnumConfigurationResourceResolver {
    
    /**
     * Gets the input stream of the {@link EnumConfigurations}.
     *
     * @param configurationName the configuration name, e.g. my.sample.SampleEnum
     * @param ignoreCase true to ignore case sensitivity on configuration name
     * @return the stream
     */
    InputStream getEnumConfigurationResourceStream(String configurationName, boolean ignoreCase);
}
