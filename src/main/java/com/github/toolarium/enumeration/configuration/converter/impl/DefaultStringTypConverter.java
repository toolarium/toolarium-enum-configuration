/*
 * DefaultStringTypConverter.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.converter.impl;

import com.github.toolarium.enumeration.configuration.converter.IStringTypeConverter;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.util.ExceptionUtil;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;


/**
 * Implements a {@link IStringTypeConverter}.
 * 
 * @author patrick
 */
public class DefaultStringTypConverter extends AbstractStringTypeConverter {
    /**
     * @see com.github.toolarium.enumeration.configuration.converter.IStringTypeConverter#convert(com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(EnumKeyValueConfigurationDataType dataType, String inputToTest) throws ValidationException {
        try {
            switch (dataType) {
                case NUMBER:      return (T)getNumber(inputToTest); 
                case DOUBLE:      return (T)getDouble(inputToTest); 
                case BOOLEAN:     return (T)getBoolean(inputToTest); 
                case DATE:        return (T)getDate(inputToTest); 
                case TIME:        return (T)getTime(inputToTest);
                case TIMESTAMP:   return (T)getTimestamp(inputToTest);
                case REGEXP:      return (T)getRegExp(inputToTest);
                case UUID:        return (T)getUUID(inputToTest);
                case URI:         return (T)getURI(inputToTest);
                case CIDR:        return (T)getCIDR(inputToTest);                
                case EMAIL:       return (T)getEmail(inputToTest);
                case CRON:        return (T)getCron(inputToTest);
                case COLOR:       return (T)getColor(inputToTest);
                case CERTIFICATE: return (T)getCertificate(inputToTest);
                case BINARY:      return (T)getBinary(inputToTest);
                case STRING:
                default:
                    return (T) inputToTest;
            }
        } catch (Exception e) {
            throw ExceptionUtil.getInstance().throwsException(e, ValidationException.class, true);
        }
    }
}
