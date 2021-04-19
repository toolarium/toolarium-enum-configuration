/*
 * DateUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.github.toolarium.enumeration.configuration.dto.EnumConfiguration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 * Defines the date utility
 * 
 * @author patrick
 */
public final class DateUtil {
    public static final LocalDate MAX_DATE = DateUtil.getInstance().parseDate(EnumConfiguration.MAX_DATE_STRING);
    public static final LocalTime MAX_TIME = DateUtil.getInstance().parseTime(EnumConfiguration.MAX_TIME_STRING);
    public static final Instant MAX_TIMESTAMP = DateUtil.getInstance().parseTimestamp(EnumConfiguration.MAX_TIMESTAMP_STRING);

    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final DateUtil INSTANCE = new DateUtil();
    }

    
    /**
     * Constructor
     */
    private DateUtil() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static DateUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Parse a date
     * 
     * @param input the input date as string
     * @return the Instant 
     */
    public LocalDate parseDate(String input) {
        if (input == null) {
            return null;
        }
        
        return LocalDate.parse(input.trim());
    }

    
    /**
     * Parse a date
     * 
     * @param input the input date as string
     * @return the Instant 
     */
    public LocalTime parseTime(String input) {
        if (input == null) {
            return null;
        }
        
        return LocalTime.parse(input.trim());
    }

    
    /**
     * Parse a date
     * 
     * @param input the input date as string
     * @return the Instant 
     */
    public Instant parseTimestamp(String input) {
        if (input == null) {
            return null;
        }
        
        return Instant.parse(input.trim());
    }
}
