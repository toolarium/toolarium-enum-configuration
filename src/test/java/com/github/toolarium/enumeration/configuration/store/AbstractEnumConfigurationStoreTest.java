/*
 * AbstractEnumConfigurationStoreTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.store;

import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.resource.EnumConfigurationResourceFactory;
import com.github.toolarium.enumeration.configuration.store.impl.EnumConfigurationResourceResolver;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import com.github.toolarium.enumeration.configuration.validation.EnumKeyConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumKeyValueConfigurationValueValidatorFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract enumeration configuration store test
 *  
 * @author patrick
 */
public abstract class AbstractEnumConfigurationStoreTest {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractEnumConfigurationStoreTest.class);
    
    
    /**
     * Create the enum configuration mock
     *
     * @param className the class name
     * @param enumKeyValueConfiguration the enum key value configuration
     * @return the input stream
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    protected IEnumConfigurationResourceResolver createEnumConfigurationMock(String className, EnumKeyValueConfiguration enumKeyValueConfiguration) throws ValidationException, IOException {
        EnumConfiguration<EnumKeyValueConfiguration> enumConfiguration = new EnumConfiguration<EnumKeyValueConfiguration>(className);
        enumConfiguration.add(enumKeyValueConfiguration);
        EnumConfigurations enumConfigurations = new EnumConfigurations();
        enumConfigurations.add(enumConfiguration);
        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        EnumConfigurationResourceFactory.getInstance().store(enumConfigurations, outputstream);
        LOG.debug("Prepared " + enumConfigurations);
        return new EnumConfigurationResourceResolver(new ByteArrayInputStream(outputstream.toByteArray()));
    }


    /**
     * Create the enum configuration
     *
     * @param enumConfigurtionKeyName the key name
     * @param dataType the data type
     * @param minSize the min size
     * @param maxSize the max size
     * @param defaultValue the default size
     * @param exampleValue the example value
     * @return the input stream
     * @throws ValidationException In case of a validation error
     * @throws IOException In case of an I/O error
     */
    protected EnumKeyValueConfiguration createEnumKeyValueConfiguration(String enumConfigurtionKeyName, 
                                                                      EnumKeyValueConfigurationDataType dataType,
                                                                      String minSize,
                                                                      String maxSize,
                                                                      String defaultValue, 
                                                                      String exampleValue) 
            throws ValidationException, IOException {
        // 1) define enum configuration
        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        EnumKeyValueConfigurationSizing<Integer> cardinality = new EnumKeyValueConfigurationSizing<Integer>(1, 1);
        enumKeyValueConfiguration.setCardinality(cardinality);
        enumKeyValueConfiguration.setConfidential(false);
        enumKeyValueConfiguration.setKey(enumConfigurtionKeyName);
        enumKeyValueConfiguration.setDefaultValue(defaultValue);
        enumKeyValueConfiguration.setDescription("The description.");
        enumKeyValueConfiguration.setEnumerationValue("");
        enumKeyValueConfiguration.setExampleValue(exampleValue);
        enumKeyValueConfiguration.setValidFrom(Instant.now());
        enumKeyValueConfiguration.setValidTill(DateUtil.MAX_TIMESTAMP);
        enumKeyValueConfiguration.setDataType(dataType);
        //EnumKeyValueConfigurationSizing<?> valueSize = new EnumKeyValueConfigurationSizing<Long>(0L, 10L); // define long value
        EnumKeyValueConfigurationSizing<?> valueSize = EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(enumKeyValueConfiguration.getDataType(), minSize, maxSize); 
        enumKeyValueConfiguration.setValueSize(valueSize);
        EnumKeyConfigurationValidatorFactory.getInstance().getValidator().validate(enumKeyValueConfiguration);
        return enumKeyValueConfiguration;
    }

}
