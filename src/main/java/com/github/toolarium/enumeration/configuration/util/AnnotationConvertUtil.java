/*
 * AnnotationConvertUtil.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import java.time.Instant;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;


/**
 * Annotation convert utility 
 * 
 * @author patrick
 */
public final class AnnotationConvertUtil {
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
        enumConfiguration.setValidTill(DateUtil.MAX_TIMESTAMP);

        if (enumConfigurationAnnotation.validFrom() != null && !enumConfigurationAnnotation.validFrom().trim().isEmpty()) {
            enumConfiguration.setValidFrom(DateUtil.getInstance().parseDate(enumConfigurationAnnotation.validFrom()));
        }
        
        if (enumConfigurationAnnotation.validTill() != null && !enumConfigurationAnnotation.validTill().trim().isEmpty()) {
            enumConfiguration.setValidTill(DateUtil.getInstance().parseDate(enumConfigurationAnnotation.validTill()));
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
        enumValueConfiguration.setValidTill(DateUtil.MAX_TIMESTAMP);
         
        enumValueConfiguration.setDescription(enumValueConfigurationAnnotation.description());
        enumValueConfiguration.setDataType(EnumUtil.getInstance().mapEnum(EnumValueConfigurationDataType.class, enumValueConfigurationAnnotation.dataType()));
        enumValueConfiguration.setDefaultValue(enumValueConfigurationAnnotation.defaultValue());
        enumValueConfiguration.setValueSize(prepareValueSize(enumValueConfigurationAnnotation.minValue(), enumValueConfigurationAnnotation.maxValue()));
        enumValueConfiguration.setExampleValue(enumValueConfigurationAnnotation.exampleValue());
        enumValueConfiguration.setCardinality(parseCardinality(enumValueConfigurationAnnotation.cardinality()));
        enumValueConfiguration.setOptional(enumValueConfigurationAnnotation.isOptional());
        enumValueConfiguration.setConfidential(enumValueConfigurationAnnotation.isConfidential());
        
        if (enumValueConfigurationAnnotation.validFrom() != null && !enumValueConfigurationAnnotation.validFrom().trim().isEmpty()) {
            enumValueConfiguration.setValidFrom(DateUtil.getInstance().parseDate(enumValueConfigurationAnnotation.validFrom()));
        }
        
        if (enumValueConfigurationAnnotation.validTill() != null && !enumValueConfigurationAnnotation.validTill().trim().isEmpty()) {
            enumValueConfiguration.setValidTill(DateUtil.getInstance().parseDate(enumValueConfigurationAnnotation.validTill()));
        }
        
        return enumValueConfiguration;
    }


    /**
     * Prepare value size
     *
     * @param minValue the min value
     * @param maxValue the max value
     * @return the preapred value
     */
    public EnumValueConfigurationSizing prepareValueSize(String minValue, String maxValue) {
        if ((minValue == null || minValue.trim().isEmpty()) && (maxValue == null || maxValue.trim().isEmpty())) {
            return null;
        }

        EnumValueConfigurationSizing result = new EnumValueConfigurationSizing();
        if (minValue != null && !minValue.trim().isEmpty()) {
            result.setMinSize(parseSizeValue(minValue.trim()));
            
            if (Integer.MAX_VALUE == result.getMinSize().intValue()) {
                throw new IllegalArgumentException("Invalid min value [" + minValue + "]!");                
            }
        }

        if (maxValue != null && !maxValue.trim().isEmpty()) {
            result.setMaxSize(parseSizeValue(maxValue.trim()));
        }

        return result;
    }

    
    /**
     * Parse the cardinality expression
     *
     * @param inputCardinality the cardinality
     * @return the parsed cardinality
     */
    public EnumValueConfigurationSizing parseCardinality(String inputCardinality) {

        if (inputCardinality == null || inputCardinality.trim().isEmpty()) {
            return null;
        }
        
        EnumValueConfigurationSizing result = new EnumValueConfigurationSizing();
        String cardinality = inputCardinality.trim();
        int idx = cardinality.indexOf("..");
        if (idx >= 0) {
            try {
                result.setMinSize(Integer.valueOf(cardinality.substring(0, idx).trim()));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid min cardinality value [" + cardinality.substring(0, idx) + "], expected [min]..[max]!");                
            }
            
            if (cardinality.length() > (idx + 2)) {
                String value = cardinality.substring(idx + 2).trim();
                try {
                    result.setMaxSize(parseSizeValue(value));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid max cardinality value [" + value + "], expected [min]..[max]!");                
                }
            }
        } else {
            try {
                result.setMaxSize(parseSizeValue(cardinality));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid cardinality value [" + cardinality + "], expected [min]..[max]!");                
            }
        }
        
        return result;
    }
    

    /**
     * Parse the size value
     *
     * @param input the string input, either number or *. In case it's null it will return null 
     * @return the parsed result
     */
    public Integer parseSizeValue(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String value = input.trim();
        if ("*".equals(value)) {
            return Integer.MAX_VALUE;
        }

        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid size value [" + value + "]!");
        }
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