/*
 * EnumConfigurationResourceFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Defines the enum configuration resource factory
 *  
 * @author patrick
 */
public final class EnumConfigurationResourceFactory {
    private ObjectMapper objectMapper;
    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final EnumConfigurationResourceFactory INSTANCE = new EnumConfigurationResourceFactory();
    }

    
    /**
     * Constructor
     */
    private EnumConfigurationResourceFactory() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static EnumConfigurationResourceFactory getInstance() {
        return HOLDER.INSTANCE;
    }

    
    
    /**
     * Load a collection of {@link EnumConfiguration}.
     * 
     * @param inputstream the input stream
     * @return the collection
     * @throws IOException In case of an error
     */
    public EnumConfigurations load(InputStream inputstream) throws IOException {
        if (inputstream == null) {
            return null;
        }
        
        try {
            return getMapper().readValue(inputstream, EnumConfigurations.class);
        } catch (JsonMappingException | JsonParseException e) {
            final String msg = e.getMessage();

            if (msg != null && msg.startsWith("No content")) {
                return null;
            }

            IOException ex = new IOException(e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        } catch (IOException e) {
            throw e;
        }
    }
    

    /**
     * Store a collection of {@link EnumConfiguration}.
     * 
     * @param enumConfigurations the collection
     * @param outputStream the output stream
     * @throws IOException In case of an error
     */
    public void store(EnumConfigurations enumConfigurations, OutputStream outputStream) throws IOException {
        if (outputStream == null || enumConfigurations == null || enumConfigurations.getEnumConfigurationList() == null || enumConfigurations.getEnumConfigurationList().isEmpty()) {
            return;
        }
                
        try {
            getMapper().writerWithDefaultPrettyPrinter().writeValue(outputStream, enumConfigurations);
        } catch (JsonMappingException | JsonParseException e) {
            IOException ex = new IOException(e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }


    /**
     * Get the object mapper
     *
     * @return the object mapper
     */
    private ObjectMapper getMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        }
        return objectMapper;
    }
}
