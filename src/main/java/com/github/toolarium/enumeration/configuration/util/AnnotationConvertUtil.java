/*
 * AnnotationConvertUtil.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.github.toolarium.enumeration.configuration.converter.StringTypeConverterFactory;
import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumValueConfigurationValueValidatorFactory;
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

        if (enumConfigurationAnnotation.validFrom() != null && !enumConfigurationAnnotation.validFrom().trim().isEmpty()) {
            enumConfiguration.setValidFrom(DateUtil.getInstance().parseTimestamp(enumConfigurationAnnotation.validFrom()));
        }
        
        if (enumConfigurationAnnotation.validTill() != null && !enumConfigurationAnnotation.validTill().trim().isEmpty()) {
            enumConfiguration.setValidTill(DateUtil.getInstance().parseTimestamp(enumConfigurationAnnotation.validTill()));
        }
        
        return enumConfiguration;
    }


    /**
     * Convert a {@link com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration} into a
     * {@link EnumValueConfiguration}.
     *
     * @param enumValueConfigurationAnnotation the {@link com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration}.
     * @return the {@link EnumValueConfiguration}.
     * @throws ValidationException In case if a validation error
     */
    public EnumValueConfiguration convert(com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration enumValueConfigurationAnnotation) throws ValidationException {
        if (enumValueConfigurationAnnotation == null) {
            return null;            
        }

        EnumValueConfiguration enumValueConfiguration = null;
        enumValueConfiguration = new EnumValueConfiguration();
        enumValueConfiguration.setDescription(enumValueConfigurationAnnotation.description());
        
        EnumValueConfigurationDataType type = EnumUtil.getInstance().mapEnum(EnumValueConfigurationDataType.class, enumValueConfigurationAnnotation.dataType());
        enumValueConfiguration.setDataType(type);
        enumValueConfiguration.setDefaultValue(enumValueConfigurationAnnotation.defaultValue());
        enumValueConfiguration.setValueSize(EnumValueConfigurationValueValidatorFactory.getInstance().createEnumValueConfigurationSizing(enumValueConfiguration.getDataType(), 
                                                                                                                                         enumValueConfigurationAnnotation.minValue(), 
                                                                                                                                         enumValueConfigurationAnnotation.maxValue()));
        enumValueConfiguration.setExampleValue(enumValueConfigurationAnnotation.exampleValue());
        enumValueConfiguration.setCardinality(parseCardinality(enumValueConfigurationAnnotation.cardinality()));
        enumValueConfiguration.setConfidential(enumValueConfigurationAnnotation.isConfidential());
        
        if (enumValueConfigurationAnnotation.validFrom() != null && !enumValueConfigurationAnnotation.validFrom().trim().isEmpty()) {
            enumValueConfiguration.setValidFrom(DateUtil.getInstance().parseTimestamp(enumValueConfigurationAnnotation.validFrom()));
        }
        
        if (enumValueConfigurationAnnotation.validTill() != null && !enumValueConfigurationAnnotation.validTill().trim().isEmpty()) {
            enumValueConfiguration.setValidTill(DateUtil.getInstance().parseTimestamp(enumValueConfigurationAnnotation.validTill()));
        }
        
        return enumValueConfiguration;
    }

    
    /**
     * Parse the cardinality expression
     *
     * @param inputCardinality the cardinality
     * @return the parsed cardinality
     */
    public EnumValueConfigurationSizing<Integer> parseCardinality(String inputCardinality) {

        if (inputCardinality == null || inputCardinality.trim().isEmpty()) {
            return null;
        }
        
        EnumValueConfigurationSizing<Integer> cardinality = new EnumValueConfigurationSizing<Integer>(1, 1);
        String cardinalityString = inputCardinality.trim();
        int idx = cardinalityString.indexOf("..");
        if (idx >= 0) {
            try {
                String value = cardinalityString.substring(0, idx).trim();
                cardinality.setMinSizeAsString(value);
                cardinality.setMinSize(Integer.valueOf(value));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid min cardinality value [" + cardinalityString.substring(0, idx) + "], expected [min]..[max]!");                
            }
            
            if (cardinalityString.length() > (idx + 2)) {
                String value = cardinalityString.substring(idx + 2).trim();
                try {
                    cardinality.setMaxSizeAsString(value);
                    
                    if (EnumValueConfigurationSizing.MAX_CARDINALITY.equals(value)) {
                        cardinality.setMaxSize(Integer.MAX_VALUE);
                    } else {
                        cardinality.setMaxSize(Integer.valueOf(value));
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid max cardinality value [" + value + "], expected [min]..[max]!");                
                }
            }
        } else {
            try {
                String value = cardinalityString.trim();
                cardinality.setMaxSizeAsString(value);
                
                if (EnumValueConfigurationSizing.MAX_CARDINALITY.equals(value)) {
                    cardinality.setMaxSize(Integer.MAX_VALUE);
                } else {
                    cardinality.setMaxSize(Integer.valueOf(value));
                }
                
                if (cardinality.getMaxSize() < cardinality.getMinSize()) {
                    cardinality.setMinSize(cardinality.getMaxSize());
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid cardinality value [" + cardinalityString + "], expected [min]..[max]!");                
            }
        }
        
        return cardinality;
    }
    

    /**
     * Parse the size value
     *
     * @param <T> the generic type
     * @param dataType the data type
     * @param input the string input, either number or *. In case it's null it will return null 
     * @return the parsed result
     */
    public <T> T parseSizeValue(EnumValueConfigurationDataType dataType, String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        
        String value = input.trim();

        try {
            return StringTypeConverterFactory.getInstance().getStringTypeConverter().convert(dataType, value);
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