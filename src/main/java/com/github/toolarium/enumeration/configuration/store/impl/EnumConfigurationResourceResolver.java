/*
 * EnumConfigurationResourceResolver.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store.impl;

import com.github.toolarium.enumeration.configuration.store.IEnumConfigurationResourceResolver;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements a {@link IEnumConfigurationResourceResolver}.
 *  
 * @author patrick
 */
public class EnumConfigurationResourceResolver implements IEnumConfigurationResourceResolver {
    private static final Logger LOG = LoggerFactory.getLogger(EnumConfigurationResourceResolver.class);
    private InputStream stream;

    
    /**
     * Constructor for EnumConfigurationResourceResolver
     *
     * @param stream the stream
     */
    public EnumConfigurationResourceResolver(InputStream stream) {
        this.stream = stream;
    }
    
    
    /**
     * @see com.github.toolarium.enumeration.configuration.store.IEnumConfigurationResourceResolver#getEnumConfigurationResourceStream(java.lang.String, boolean)
     */
    @Override
    public InputStream getEnumConfigurationResourceStream(String configurationName, boolean ignoreCase) {
        try {
            if (stream != null) {
                stream.reset();
            }
        } catch (IOException e) {
            LOG.warn("Error occured: " + e.getMessage(), e);
        }
        
        return stream;
    }

}
