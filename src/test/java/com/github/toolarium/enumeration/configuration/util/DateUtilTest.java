/*
 * DateUtilTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link DateUtil}.
 *
 * @author patrick
 */
public class DateUtilTest {

    /**
     * Test parseDate with valid input
     */
    @Test
    public void testParseDateValid() {
        LocalDate date = DateUtil.getInstance().parseDate("2024-01-15");
        assertNotNull(date);
        assertEquals(2024, date.getYear());
        assertEquals(1, date.getMonthValue());
        assertEquals(15, date.getDayOfMonth());
    }


    /**
     * Test parseDate with null returns null
     */
    @Test
    public void testParseDateNull() {
        assertNull(DateUtil.getInstance().parseDate(null));
    }


    /**
     * Test parseDate with invalid input throws exception
     */
    @Test
    public void testParseDateInvalid() {
        assertThrows(DateTimeParseException.class, () -> {
            DateUtil.getInstance().parseDate("not-a-date");
        });
    }


    /**
     * Test parseDate with whitespace is trimmed
     */
    @Test
    public void testParseDateTrimmed() {
        LocalDate date = DateUtil.getInstance().parseDate("  2024-06-01  ");
        assertNotNull(date);
        assertEquals(6, date.getMonthValue());
    }


    /**
     * Test parseTime with valid input
     */
    @Test
    public void testParseTimeValid() {
        LocalTime time = DateUtil.getInstance().parseTime("14:30:00");
        assertNotNull(time);
        assertEquals(14, time.getHour());
        assertEquals(30, time.getMinute());
    }


    /**
     * Test parseTime with null returns null
     */
    @Test
    public void testParseTimeNull() {
        assertNull(DateUtil.getInstance().parseTime(null));
    }


    /**
     * Test parseTime with invalid input throws exception
     */
    @Test
    public void testParseTimeInvalid() {
        assertThrows(DateTimeParseException.class, () -> {
            DateUtil.getInstance().parseTime("not-a-time");
        });
    }


    /**
     * Test parseTimestamp with valid input
     */
    @Test
    public void testParseTimestampValid() {
        Instant ts = DateUtil.getInstance().parseTimestamp("2024-01-15T10:30:00.000Z");
        assertNotNull(ts);
    }


    /**
     * Test parseTimestamp with null returns null
     */
    @Test
    public void testParseTimestampNull() {
        assertNull(DateUtil.getInstance().parseTimestamp(null));
    }


    /**
     * Test parseTimestamp with invalid input throws exception
     */
    @Test
    public void testParseTimestampInvalid() {
        assertThrows(DateTimeParseException.class, () -> {
            DateUtil.getInstance().parseTimestamp("not-a-timestamp");
        });
    }


    /**
     * Test MAX constants are initialized correctly
     */
    @Test
    public void testMaxConstants() {
        assertNotNull(DateUtil.MAX_DATE);
        assertNotNull(DateUtil.MAX_TIME);
        assertNotNull(DateUtil.MAX_TIMESTAMP);
        assertEquals(9999, DateUtil.MAX_DATE.getYear());
        assertEquals(23, DateUtil.MAX_TIME.getHour());
    }
}
