/*
 * AnnotationConvertUtil.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

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