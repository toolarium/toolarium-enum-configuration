/*
 * EnumKeyValueConfigurationValueValidatorFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.validation.value;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.impl.BinaryEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.BooleanEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.CIDREnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.CertificateEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.ColorEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.CronEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.DateEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.DoubleEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.EmailEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.NumberEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.RegExpEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.StringEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.TimeEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.TimestampEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.URIEnumKeyValueConfigurationValueValidator;
import com.github.toolarium.enumeration.configuration.validation.value.impl.UUIDEnumKeyValueConfigurationValueValidator;


/**
 * Defines the {@link IEnumKeyConfigurationValueValidator} factory.
 * 
 * @author patrick
 */
public final class EnumKeyValueConfigurationValueValidatorFactory {
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     */
    private static class HOLDER {
        static final EnumKeyValueConfigurationValueValidatorFactory INSTANCE = new EnumKeyValueConfigurationValueValidatorFactory();
    }

    /**
     * Constructor
     */
    private EnumKeyValueConfigurationValueValidatorFactory() {
        // NOP
    }

    /**
     * Get the instance
     *
     * @return the instance
     */
    public static EnumKeyValueConfigurationValueValidatorFactory getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Create an {@link IEnumKeyConfigurationValueValidator}.
     *
     * @param dataType the enum data type
     * @return the resolved type
     */
    public IEnumKeyConfigurationValueValidator<?, ?> createEnumKeyValueConfigurationValueValidator(EnumKeyValueConfigurationDataType dataType) {
        try {
            switch (dataType) {
                case NUMBER:      return new NumberEnumKeyValueConfigurationValueValidator(); 
                case DOUBLE:      return new DoubleEnumKeyValueConfigurationValueValidator(); 
                case BOOLEAN:     return new BooleanEnumKeyValueConfigurationValueValidator(); 
                case DATE:        return new DateEnumKeyValueConfigurationValueValidator(); 
                case TIME:        return new TimeEnumKeyValueConfigurationValueValidator();
                case TIMESTAMP:   return new TimestampEnumKeyValueConfigurationValueValidator();
                case REGEXP:      return new RegExpEnumKeyValueConfigurationValueValidator();
                case UUID:        return new UUIDEnumKeyValueConfigurationValueValidator();
                case URI:         return new URIEnumKeyValueConfigurationValueValidator();
                case CIDR:        return new CIDREnumKeyValueConfigurationValueValidator();                
                case EMAIL:       return new EmailEnumKeyValueConfigurationValueValidator();
                case CRON:        return new CronEnumKeyValueConfigurationValueValidator();
                case COLOR:       return new ColorEnumKeyValueConfigurationValueValidator();
                case CERTIFICATE: return new CertificateEnumKeyValueConfigurationValueValidator();
                case BINARY:      return new BinaryEnumKeyValueConfigurationValueValidator();
                case STRING:
                default:
                    return new StringEnumKeyValueConfigurationValueValidator();
            }
        } catch (Exception e) {
            // NOP
            return null;
        }
    }


    /**
     * Create the {@link EnumKeyValueConfigurationSizing}.
     *
     * @param <D> the validated data type
     * @param <T> the size type
     * @param dataType the data type
     * @param minValue the min value
     * @param maxValue the max value
     * @return the {@link EnumKeyValueConfigurationSizing}.
     * @throws ValidationException In case of a validation error
     */
    @SuppressWarnings("unchecked")
    public <D, T> EnumKeyValueConfigurationSizing<T> createEnumKeyValueConfigurationSizing(EnumKeyValueConfigurationDataType dataType, String minValue, String maxValue) throws ValidationException {
        IEnumKeyConfigurationValueValidator<D, T> type = (IEnumKeyConfigurationValueValidator<D, T>)EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationValueValidator(dataType);
        EnumKeyValueConfigurationSizing<T> result = type.createValueSize(minValue, maxValue); 
        return result; 
    }
}
