/*
 * AnnotationConvertUtil.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration;
import java.lang.reflect.Field;
import java.time.Instant;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;


/**
 * Annotation convert utility 
 * 
 * @author Meier Patrick
 * @version $Revision:  $
 */
public final class AnnotationConvertUtil {
    public static final String MAX_TIMESTAMP_STRING = "9999-12-31T12:00:00.000Z";
    public static final Instant MAX_TIMESTAMP = AnnotationConvertUtil.getInstance().parseDate(MAX_TIMESTAMP_STRING);

    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final AnnotationConvertUtil INSTANCE = new AnnotationConvertUtil();
    }

    
    /**
     * Constructor
     */
    private AnnotationConvertUtil() {
        // NOP
    }


    /**
     * Get the instance
     *
     * @return the instance
     */
    public static AnnotationConvertUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Parse a date
     * 
     * @param input the input date as string
     * @return the Instant 
     */
    public Instant parseDate(String input) {
        if (input == null) {
            return null;
        }
        
        return Instant.parse(input);
    }
    
    
    /**
     * Read the enum
     *
     * @param <T> the generic type
     * @param e the enumeration value
     * @return the parsed content
     * @throws IllegalArgumentException In case the annotation could not be resolved
     */
    public <T extends Enum<T>> EnumValueConfiguration getAnnotationInformation(T e) throws IllegalArgumentException {
        if (e == null) {
            return null;
        }
        
        try {
            Field field = e.getClass().getField(e.name());
            EnumValueConfiguration enumValueConfiguration = convert(field.getAnnotation(com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration.class));
            if (enumValueConfiguration == null) {
                return null;
            }

            enumValueConfiguration.setKey(e.name());

            EnumConfiguration enumConfiguration = convert(e.getClass().getAnnotation(com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration.class));
            if (enumConfiguration != null) {
                enumValueConfiguration = enumConfiguration.addEnumValueConfiguration(enumValueConfiguration);
            }
            
            return enumValueConfiguration;
            
        } catch (Exception ex) {
            // NOP
            IllegalArgumentException ie = new IllegalArgumentException("Could not find annotation: " + ex.getMessage());
            ie.setStackTrace(ex.getStackTrace());
            throw ie;
        } 
    }

    
    /**
     * Convert a {@link com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration} into a
     * {@link EnumConfiguration}.
     *
     * @param enumConfigurationAnnotation the {@link com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration}.
     * @return the {@link EnumConfiguration}.
     */
    public EnumConfiguration convert(com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration enumConfigurationAnnotation) {
        if (enumConfigurationAnnotation == null) {
            return null;            
        }
        
        EnumConfiguration enumConfiguration = new EnumConfiguration();
        enumConfiguration.setDescription(enumConfigurationAnnotation.description());
        enumConfiguration.setValidFrom(Instant.now());
        enumConfiguration.setValidTill(AnnotationConvertUtil.MAX_TIMESTAMP);

        if (enumConfigurationAnnotation.validFrom() != null && !enumConfigurationAnnotation.validFrom().trim().isEmpty()) {
            enumConfiguration.setValidFrom(AnnotationConvertUtil.getInstance().parseDate(enumConfigurationAnnotation.validFrom()));
        }
        
        if (enumConfigurationAnnotation.validTill() != null && !enumConfigurationAnnotation.validTill().trim().isEmpty()) {
            enumConfiguration.setValidTill(AnnotationConvertUtil.getInstance().parseDate(enumConfigurationAnnotation.validTill()));
        }
        
        return enumConfiguration;
    }


    /**
     * Convert a {@link com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration} into a
     * {@link EnumValueConfiguration}.
     *
     * @param enumValueConfigurationAnnotation the {@link com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration}.
     * @return the {@link EnumValueConfiguration}.
     */
    public EnumValueConfiguration convert(com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration enumValueConfigurationAnnotation) {
        if (enumValueConfigurationAnnotation == null) {
            return null;            
        }

        EnumValueConfiguration enumValueConfiguration = null;
        enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setValidFrom(Instant.now());
        enumValueConfiguration.setValidTill(AnnotationConvertUtil.MAX_TIMESTAMP);
         
        enumValueConfiguration.setDescription(enumValueConfigurationAnnotation.description());
        enumValueConfiguration.setDefaultValue(enumValueConfigurationAnnotation.defaultValue());
        enumValueConfiguration.setOptional(enumValueConfigurationAnnotation.isOptional());
        enumValueConfiguration.setConfidential(enumValueConfigurationAnnotation.isConfidential());
        
        if (enumValueConfigurationAnnotation.validFrom() != null && !enumValueConfigurationAnnotation.validFrom().trim().isEmpty()) {
            enumValueConfiguration.setValidFrom(AnnotationConvertUtil.getInstance().parseDate(enumValueConfigurationAnnotation.validFrom()));
        }
        
        if (enumValueConfigurationAnnotation.validTill() != null && !enumValueConfigurationAnnotation.validTill().trim().isEmpty()) {
            enumValueConfiguration.setValidTill(AnnotationConvertUtil.getInstance().parseDate(enumValueConfigurationAnnotation.validTill()));
        }
        return enumValueConfiguration;
    }



    
    /**
     * Get the name 
     * 
     * @param <T> the generic type
     * @param type the type
     * @return the type as name
     */
    public <T extends ExecutableElement> String getName(T type) {
        if (type == null) {
            return "";
        }
        
        return trimQuotationMarks(type.getSimpleName().toString());
    }


    /**
     * Get the name 
     * 
     * @param <T> the generic type
     * @param type the type
     * @return the type as name
     */
    public <T extends AnnotationValue> String  getValue(T type) {
        if (type == null) {
            return "";
        }
        
        return "" + type.getValue();
    }

    
    /**
     * Trim quotation marks 
     * 
     * @param input the input
     * @return the trimmed output
     */
    public String trimQuotationMarks(String input) {
        String result = input;
        if (result == null) {
            return result;
        }
        
        if (result.length() > 1 && result.startsWith("\"")) {
            result = result.substring(1);
        }

        if (result.length() > 1 && result.endsWith("\"")) {
            result = result.substring(0, result.length() - 1);
        }
                
        return result;
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/