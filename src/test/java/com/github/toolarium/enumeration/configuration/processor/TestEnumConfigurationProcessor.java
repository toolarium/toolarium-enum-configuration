/*
 * TestEnumConfigurationProcessor.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.processor;

import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Extends the enumeration configuration annotation processor to have the parsed result available. 
 * 
 * @author patrick
 */
public class TestEnumConfigurationProcessor extends EnumConfigurationProcessor {
    private String content;
    

    /**
     * @see com.github.toolarium.enumeration.configuration.processor.EnumConfigurationProcessor#generateFileContent(java.lang.String, com.github.toolarium.enumeration.configuration.dto.EnumConfigurations, java.io.OutputStream)
     */
    @Override
    protected void generateFileContent(String name, EnumConfigurations enumConfigurations, OutputStream stream) throws IOException {
        ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
        super.generateFileContent(name, enumConfigurations, tempStream);
        
        content = tempStream.toString();
        stream.write(tempStream.toByteArray());
    }
    
    
    /**
     * Get the generated enum-configuration
     *
     * @return the ernum configurations
     */
    public String  getGeneratedEnumConfiguration() {
        return content;
    }
}
