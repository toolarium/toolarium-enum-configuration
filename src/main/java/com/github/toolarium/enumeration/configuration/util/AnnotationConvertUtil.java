/*
 * AnnotationConvertUtil.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.github.toolarium.enumeration.configuration.converter.StringTypeConverterFactory;
import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationSizing;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumKeyValueConfigurationValueValidatorFactory;
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
     * @param <T> The generic type
     * @param enumConfigurationAnnotation the {@link com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration}.
     * @return the {@link EnumConfiguration}.
     */
    public <T extends EnumKeyConfiguration> EnumConfiguration<T> convert(com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration enumConfigurationAnnotation) {
        if (enumConfigurationAnnotation == null) {
            return null;            
        }
        
        EnumConfiguration<T> enumConfiguration = new EnumConfiguration<T>();
        enumConfiguration.setDescription(enumConfigurationAnnotation.description());

        if (enumConfigurationAnnotation.tag() != null && !enumConfigurationAnnotation.tag().trim().isEmpty()) {
            enumConfiguration.setTag(enumConfigurationAnnotation.tag());
        }

        if (enumConfigurationAnnotation.validFrom() != null && !enumConfigurationAnnotation.validFrom().trim().isEmpty()) {
            enumConfiguration.setValidFrom(DateUtil.getInstance().parseTimestamp(enumConfigurationAnnotation.validFrom()));
        }
        
        if (enumConfigurationAnnotation.validTill() != null && !enumConfigurationAnnotation.validTill().trim().isEmpty()) {
            enumConfiguration.setValidTill(DateUtil.getInstance().parseTimestamp(enumConfigurationAnnotation.validTill()));
        }
        
        return enumConfiguration;
    }


    /**
     * Convert a {@link com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration} into a
     * {@link EnumKeyValueConfiguration}.
     *
     * @param enumKeyConfigurationAnnotation the {@link com.github.toolarium.enumeration.configuration.annotation.EnumKeyConfiguration}.
     * @return the {@link EnumKeyValueConfiguration}.
     * @throws ValidationException In case if a validation error
     */
    public EnumKeyConfiguration convert(com.github.toolarium.enumeration.configuration.annotation.EnumKeyConfiguration enumKeyConfigurationAnnotation) throws ValidationException {
        if (enumKeyConfigurationAnnotation == null) {
            return null;            
        }

        EnumKeyConfiguration enumKeyConfiguration = null;
        enumKeyConfiguration = new EnumKeyValueConfiguration();
        enumKeyConfiguration.setDescription(enumKeyConfigurationAnnotation.description());
        enumKeyConfiguration.setConfidential(enumKeyConfigurationAnnotation.isConfidential());
        
        if (enumKeyConfigurationAnnotation.validFrom() != null && !enumKeyConfigurationAnnotation.validFrom().trim().isEmpty()) {
            enumKeyConfiguration.setValidFrom(DateUtil.getInstance().parseTimestamp(enumKeyConfigurationAnnotation.validFrom()));
        }
        
        if (enumKeyConfigurationAnnotation.validTill() != null && !enumKeyConfigurationAnnotation.validTill().trim().isEmpty()) {
            enumKeyConfiguration.setValidTill(DateUtil.getInstance().parseTimestamp(enumKeyConfigurationAnnotation.validTill()));
        }
        
        return enumKeyConfiguration;
    }

    
    /**
     * Convert a {@link com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration} into a
     * {@link EnumKeyValueConfiguration}.
     *
     * @param enumKeyValueConfigurationAnnotation the {@link com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration}.
     * @return the {@link EnumKeyValueConfiguration}.
     * @throws ValidationException In case if a validation error
     */
    public EnumKeyValueConfiguration convert(com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration enumKeyValueConfigurationAnnotation) throws ValidationException {
        if (enumKeyValueConfigurationAnnotation == null) {
            return null;            
        }

        EnumKeyValueConfiguration enumKeyValueConfiguration = null;
        enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDescription(enumKeyValueConfigurationAnnotation.description());
        
        EnumKeyValueConfigurationDataType type = EnumUtil.getInstance().mapEnum(EnumKeyValueConfigurationDataType.class, enumKeyValueConfigurationAnnotation.dataType());
        enumKeyValueConfiguration.setDataType(type);
        enumKeyValueConfiguration.setDefaultValue(enumKeyValueConfigurationAnnotation.defaultValue());
        enumKeyValueConfiguration.setValueSize(EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(enumKeyValueConfiguration.getDataType(), 
                                                                                                                                                  enumKeyValueConfigurationAnnotation.minValue(), 
                                                                                                                                                  enumKeyValueConfigurationAnnotation.maxValue()));
        enumKeyValueConfiguration.setExampleValue(enumKeyValueConfigurationAnnotation.exampleValue());
        enumKeyValueConfiguration.setEnumerationValue(enumKeyValueConfigurationAnnotation.enumerationValue());
        enumKeyValueConfiguration.setCardinality(parseCardinality(enumKeyValueConfigurationAnnotation.cardinality()));
        enumKeyValueConfiguration.setUniqueness(enumKeyValueConfigurationAnnotation.isUniqueness());
        enumKeyValueConfiguration.setConfidential(enumKeyValueConfigurationAnnotation.isConfidential());
        
        if (enumKeyValueConfigurationAnnotation.validFrom() != null && !enumKeyValueConfigurationAnnotation.validFrom().trim().isEmpty()) {
            enumKeyValueConfiguration.setValidFrom(DateUtil.getInstance().parseTimestamp(enumKeyValueConfigurationAnnotation.validFrom()));
        }
        
        if (enumKeyValueConfigurationAnnotation.validTill() != null && !enumKeyValueConfigurationAnnotation.validTill().trim().isEmpty()) {
            enumKeyValueConfiguration.setValidTill(DateUtil.getInstance().parseTimestamp(enumKeyValueConfigurationAnnotation.validTill()));
        }
        
        return enumKeyValueConfiguration;
    }

    
    /**
     * Parse the cardinality expression
     *
     * @param inputCardinality the cardinality
     * @return the parsed cardinality
     * @throws IllegalArgumentException In case of invalid cardinality.
     */
    public EnumKeyValueConfigurationSizing<Integer> parseCardinality(String inputCardinality) {

        if (inputCardinality == null || inputCardinality.trim().isEmpty()) {
            return null;
        }
        
        EnumKeyValueConfigurationSizing<Integer> cardinality = new EnumKeyValueConfigurationSizing<Integer>(1, 1);
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
                    
                    if (EnumKeyValueConfigurationSizing.MAX_CARDINALITY.equals(value)) {
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
                
                if (EnumKeyValueConfigurationSizing.MAX_CARDINALITY.equals(value)) {
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
     * @param <T> the generic size type
     * @param dataType the data type
     * @param input the string input, either number or *. In case it's null it will return null 
     * @return the parsed result
     * @throws IllegalArgumentException In case of invalid size.
     */
    public <T> T parseSizeValue(EnumKeyValueConfigurationDataType dataType, String input) {
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