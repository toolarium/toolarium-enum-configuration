/*
 * EnumUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import java.lang.reflect.Field;


/**
 * Enum utility
 * 
 * @author patrick
 */
public final class EnumUtil {
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     */
    private static class HOLDER {
        static final EnumUtil INSTANCE = new EnumUtil();
    }

    
    /**
     * Constructor
     */
    private EnumUtil() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static EnumUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Get the {@link EnumKeyConfiguration} from an enumeration configuration
     *
     * @param <T> the generic type
     * @param e the enumeration value
     * @return the parsed content
     * @throws IllegalArgumentException In case the annotation could not be resolved
     */
    public <T extends Enum<T>> EnumKeyConfiguration getEnumKeyConfigurationAnnotationInformation(T e) throws IllegalArgumentException {
        if (e == null) {
            return null;
        }
        
        try {
            Field field = e.getClass().getField(e.name());
            EnumKeyConfiguration enumKeyConfiguration = AnnotationConvertUtil.getInstance().convert(field.getAnnotation(com.github.toolarium.enumeration.configuration.annotation.EnumKeyConfiguration.class));
            if (enumKeyConfiguration == null) {
                return null;
            }

            enumKeyConfiguration.setKey(e.name());

            EnumConfiguration<EnumKeyConfiguration> enumConfiguration = AnnotationConvertUtil.getInstance().convert(e.getClass().getAnnotation(com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration.class));
            if (enumConfiguration != null) {
                enumKeyConfiguration = enumConfiguration.add(enumKeyConfiguration);
            }
            
            return enumKeyConfiguration;
            
        } catch (Exception ex) {
            // NOP
            IllegalArgumentException ie = new IllegalArgumentException("Could not find annotation: " + ex.getMessage());
            ie.setStackTrace(ex.getStackTrace());
            throw ie;
        } 
    }

    
    /**
     * Get the {@link EnumKeyValueConfiguration} from an enumeration configuration
     *
     * @param <T> the generic type
     * @param e the enumeration value
     * @return the parsed content
     * @throws IllegalArgumentException In case the annotation could not be resolved
     */
    public <T extends Enum<T>> EnumKeyValueConfiguration getEnumKeyValueConfigurationAnnotationInformation(T e) throws IllegalArgumentException {
        if (e == null) {
            return null;
        }
        
        try {
            Field field = e.getClass().getField(e.name());
            EnumKeyValueConfiguration enumKeyValueConfiguration = AnnotationConvertUtil.getInstance().convert(field.getAnnotation(com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration.class));
            if (enumKeyValueConfiguration == null) {
                return null;
            }

            enumKeyValueConfiguration.setKey(e.name());

            EnumConfiguration<EnumKeyValueConfiguration> enumConfiguration = AnnotationConvertUtil.getInstance().convert(e.getClass().getAnnotation(com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration.class));
            if (enumConfiguration != null) {
                enumKeyValueConfiguration = enumConfiguration.add(enumKeyValueConfiguration);
            }
            
            return enumKeyValueConfiguration;
            
        } catch (Exception ex) {
            // NOP
            IllegalArgumentException ie = new IllegalArgumentException("Could not find annotation: " + ex.getMessage());
            ie.setStackTrace(ex.getStackTrace());
            throw ie;
        } 
    }

        
    /**
     * Returns the enum constant of the specified enum type with the specified name.  
     * The name must match exactly an identifier used to declare an enum constant in this type.  
     * (Extraneous whitespace characters are not permitted.)
     *
     * @param <T> the type
     * @param enumType the <tt>Class</tt> object of the enum type from which to return a constant
     * @param name the name of the constant to return
     * @return the enum constant of the specified enum type with the specified name
     */
    public <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
        if (name == null || name.trim().length() == 0) {
            return null;
        }
        
        T[] types = enumType.getEnumConstants();
        String nameToParse = name.trim();

        for (int i = 0; i < types.length; i++) {
            T type = types[i];

            if (type.name().equalsIgnoreCase(nameToParse)) {
                return type;
            }
        }

        return null;
    }


    /**
     * Returns the enum constant of the specified enum type with the specified name.  
     * The name must match exactly an identifier used to declare an enum constant in this type.  
     * (Extraneous whitespace characters are not permitted.)
     *
     * @param <T> the type
     * @param enumType the <tt>Class</tt> object of the enum type from which to return a constant
     * @param inputEnum the input enum to map
     * @return the enum constant of the specified enum type with the specified name
     */
    public <T extends Enum<T>> T mapEnum(Class<T> enumType, Enum<?> inputEnum) {
        if (inputEnum == null) {
            return null;
        }
        
        return valueOf(enumType, inputEnum.name());
    }
}
