/*
 * EnumConfigurationResourceFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.resource;

import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.processor.EnumConfigurationProcessor;
import com.github.toolarium.enumeration.configuration.util.JSONUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;


/**
 * Defines the enum configuration resource factory
 *  
 * @author patrick
 */
public final class EnumConfigurationResourceFactory {
    
    /**
     * Private class, the only instance of the singleton which will be created by accessing the holder class.
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
     * @param <T> The generic type
     * @param filePath the file path
     * @return the collection
     * @throws IOException In case of an error
     */
    public  <T extends EnumKeyConfiguration> EnumConfigurations load(String filePath) throws IOException {
        return load(filePath, null);
    }

    
    /**
     * Load a collection of {@link EnumConfiguration}.
     * 
     * @param <T> The generic type
     * @param inputFilePath the file path
     * @param inputFileName the file name
     * @return the collection
     * @throws IOException In case of an error
     */
    public  <T extends EnumKeyConfiguration> EnumConfigurations load(String inputFilePath, String inputFileName) throws IOException {
        String filePath = inputFilePath;
        if (filePath == null || filePath.isBlank()) {
            filePath = EnumConfigurationProcessor.TOOLARIUM_ENUM_CONFIGURATION_JSON_PATH;
        }
        
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }

        String fileName = inputFileName; 
        if (inputFileName == null || inputFileName.isBlank()) {
            fileName = EnumConfigurationProcessor.TOOLARIUM_ENUM_CONFIGURATION_JSON_FILENAME;
        }
        
        try (InputStream stream = new FileInputStream(Paths.get(filePath, fileName).toFile())) {
            return load(stream);
        }
    }

    
    /**
     * Load a collection of {@link EnumConfiguration}.
     * 
     * @param <T> The generic type
     * @param inputstream the input stream
     * @return the collection
     * @throws IOException In case of an error
     */
    public  <T extends EnumKeyConfiguration> EnumConfigurations load(InputStream inputstream) throws IOException {
        if (inputstream == null) {
            return null;
        }
        
        return JSONUtil.getInstance().read(EnumConfigurations.class, inputstream);
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

        JSONUtil.getInstance().write(enumConfigurations, outputStream);
    }
}
