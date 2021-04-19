/*
 * EnumValueConfigurationValueValidatorFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value;

import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.impl.BinaryEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.BooleanEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.CIDREnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.CertificateEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.ColorEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.CronEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.DateEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.DoubleEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.EmailEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.NumberEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.RegExpEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.StringEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.TimeEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.TimestampEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.URIEnumValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.UUIDEnumValueConfigurationValueValidator;


/**
 * Defines the {@link IEnumValueConfigurationValueValidator} factory.
 * 
 * @author patrick
 */
public final class EnumValueConfigurationValueValidatorFactory {
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     */
    private static class HOLDER {
        static final EnumValueConfigurationValueValidatorFactory INSTANCE = new EnumValueConfigurationValueValidatorFactory();
    }

    /**
     * Constructor
     */
    private EnumValueConfigurationValueValidatorFactory() {
        // NOP
    }

    /**
     * Get the instance
     *
     * @return the instance
     */
    public static EnumValueConfigurationValueValidatorFactory getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Create an {@link IEnumValueConfigurationValueValidator}.
     *
     * @param dataType the enum data type
     * @return the resolved type
     */
    public IEnumValueConfigurationValueValidator<?> createEnumValueConfigurationValueValidator(EnumValueConfigurationDataType dataType) {
        try {
            switch (dataType) {
                case NUMBER:      return new NumberEnumValueConfigurationValueValidator(); 
                case DOUBLE:      return new DoubleEnumValueConfigurationValueValidator(); 
                case BOOLEAN:     return new BooleanEnumValueConfigurationValueValidator(); 
                case DATE:        return new DateEnumValueConfigurationValueValidator(); 
                case TIME:        return new TimeEnumValueConfigurationValueValidator();
                case TIMESTAMP:   return new TimestampEnumValueConfigurationValueValidator();
                case REGEXP:      return new RegExpEnumValueConfigurationValueValidator();
                case UUID:        return new UUIDEnumValueConfigurationValueValidator();
                case URI:         return new URIEnumValueConfigurationValueValidator();
                case CIDR:        return new CIDREnumValueConfigurationValueValidator();                
                case EMAIL:       return new EmailEnumValueConfigurationValueValidator();
                case CRON:        return new CronEnumValueConfigurationValueValidator();
                case COLOR:       return new ColorEnumValueConfigurationValueValidator();
                case CERTIFICATE: return new CertificateEnumValueConfigurationValueValidator();
                case BINARY:      return new BinaryEnumValueConfigurationValueValidator();
                case STRING:
                default:
                    return new StringEnumValueConfigurationValueValidator();
            }
        } catch (Exception e) {
            // NOP
            return null;
        }
    }


    /**
     * Create the {@link EnumValueConfigurationSizing}.
     *
     * @param <T> The generic type
     * @param dataType the data type
     * @param minValue the min value
     * @param maxValue the max value
     * @return the {@link EnumValueConfigurationSizing}.
     * @throws ValidationException In case of a validation error
     */
    @SuppressWarnings("unchecked")
    public <T> EnumValueConfigurationSizing<T> createEnumValueConfigurationSizing(EnumValueConfigurationDataType dataType, String minValue, String maxValue) throws ValidationException {
        IEnumValueConfigurationValueValidator<T> type = (IEnumValueConfigurationValueValidator<T>)EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationValueValidator(dataType);
        EnumValueConfigurationSizing<T> result = type.createValueSize(minValue, maxValue); 
        return result; 
    }
}
