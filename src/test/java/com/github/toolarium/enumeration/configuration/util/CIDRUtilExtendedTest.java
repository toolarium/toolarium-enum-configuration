/*
 * CIDRUtilExtendedTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Extended tests for {@link CIDRUtil} edge cases.
 *
 * @author patrick
 */
public class CIDRUtilExtendedTest {

    /**
     * Test IPv4 boundary: /0 range covers everything
     *
     * @throws UnknownHostException in case of invalid host
     */
    @Test
    public void testIPv4SlashZero() throws UnknownHostException {
        assertTrue(CIDRUtil.getInstance().parse("0.0.0.0/0").isInRange("192.168.1.1"));
        assertTrue(CIDRUtil.getInstance().parse("0.0.0.0/0").isInRange("10.0.0.1"));
        assertTrue(CIDRUtil.getInstance().parse("0.0.0.0/0").isInRange("255.255.255.255"));
    }


    /**
     * Test IPv4 /32 is a single host
     *
     * @throws UnknownHostException in case of invalid host
     */
    @Test
    public void testIPv4Slash32SingleHost() throws UnknownHostException {
        assertTrue(CIDRUtil.getInstance().parse("10.0.0.1/32").isInRange("10.0.0.1"));
        assertFalse(CIDRUtil.getInstance().parse("10.0.0.1/32").isInRange("10.0.0.2"));
    }


    /**
     * Test IPv4 invalid prefix length > 32 is rejected
     */
    @Test
    public void testIPv4InvalidPrefixLength() {
        assertFalse(CIDRUtil.getInstance().isIPv4Range("10.0.0.1/33"));
        assertFalse(CIDRUtil.getInstance().isIPv4Range("10.0.0.1/99"));
    }


    /**
     * Test IPv6 compressed loopback ::1
     */
    @Test
    public void testIPv6CompressedLoopback() {
        assertTrue(CIDRUtil.getInstance().isIPv6Address("::1"));
        assertTrue(CIDRUtil.getInstance().isValidAddress("::1"));
    }


    /**
     * Test IPv6 full vs compressed equality
     *
     * @throws UnknownHostException in case of invalid host
     */
    @Test
    public void testIPv6FullVsCompressed() throws UnknownHostException {
        assertTrue(CIDRUtil.getInstance().isInRange("::1", "0:0:0:0:0:0:0:1"));
    }


    /**
     * Test invalid CIDR strings
     */
    @Test
    public void testInvalidCIDRStrings() {
        assertFalse(CIDRUtil.getInstance().isValidRange("not-a-cidr"));
        assertFalse(CIDRUtil.getInstance().isValidRange("192.168.1.1"));  // no prefix
        assertFalse(CIDRUtil.getInstance().isValidRange("/24"));          // no address
        assertFalse(CIDRUtil.getInstance().isValidRange("999.999.999.999/24")); // invalid octets
    }


    /**
     * Test parse with invalid input throws exception
     */
    @Test
    public void testParseInvalidThrows() {
        Assertions.assertThrows(UnknownHostException.class, () -> {
            CIDRUtil.getInstance().parse("invalid");
        });
    }
}
