/*
 * CIDRUtilTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.UnknownHostException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Test CIDR utility
 * 
 * @author patrick
 */
public class CIDRUtilTest {

    /**
     * Test ip4 address
     */
    @Test
    public void testIP4Address() {
        assertFalse(CIDRUtil.getInstance().isIPv4Address(null));
        assertFalse(CIDRUtil.getInstance().isIPv4Address(""));
        assertFalse(CIDRUtil.getInstance().isIPv4Address("192.168.0.1907"));
        assertFalse(CIDRUtil.getInstance().isIPv4Address("adsdsb"));

        assertTrue(CIDRUtil.getInstance().isIPv4Address("192.168.0.1"));
    }


    /**
     * Test ip6 address
     */
    @Test
    public void testIP6Address() {
        assertFalse(CIDRUtil.getInstance().isIPv6Address(null));
        assertFalse(CIDRUtil.getInstance().isIPv6Address(""));
        assertFalse(CIDRUtil.getInstance().isIPv6Address("192.168.0.1909"));
        assertFalse(CIDRUtil.getInstance().isIPv6Address("adsdsa"));

        assertTrue(CIDRUtil.getInstance().isIPv6Address("FE80:0000:0000:0000:0202:B3FF:FE1E:8329"));
        assertTrue(CIDRUtil.getInstance().isIPv6Address("FE80::0202:B3FF:FE1E:8329"));
    }


    /**
     * Test address
     */
    @Test
    public void testAddress() {
        assertFalse(CIDRUtil.getInstance().isValidAddress(null));
        assertFalse(CIDRUtil.getInstance().isValidAddress(""));
        assertFalse(CIDRUtil.getInstance().isValidAddress("192.168.0.1909"));
        assertFalse(CIDRUtil.getInstance().isValidAddress("adsdsa"));

        assertTrue(CIDRUtil.getInstance().isValidAddress("FE80:0000:0000:0000:0202:B3FF:FE1E:8329"));
        assertTrue(CIDRUtil.getInstance().isValidAddress("FE80::0202:B3FF:FE1E:8329"));
        assertTrue(CIDRUtil.getInstance().isValidAddress("192.168.0.1"));
    }


    /**
     * Test ip4 address
     */
    @Test
    public void testIP4AddressRange() {
        assertFalse(CIDRUtil.getInstance().isIPv4Range(null));
        assertFalse(CIDRUtil.getInstance().isIPv4Range(""));
        assertFalse(CIDRUtil.getInstance().isIPv4Range("192.168.0.1909"));
        assertFalse(CIDRUtil.getInstance().isIPv4Range("adsdsa"));

        assertTrue(CIDRUtil.getInstance().isIPv4Range("192.168.0.1/10"));
        assertTrue(CIDRUtil.getInstance().isIPv4Range("192.168.0.1/31"));
        assertTrue(CIDRUtil.getInstance().isIPv4Range("192.168.0.1/32"));
        assertTrue(CIDRUtil.getInstance().isIPv4Range("192.109.190.18/32"));
    }


    /**
     * Test ip6 address
     */
    @Test
    public void testIP6AddressRange() {
        assertFalse(CIDRUtil.getInstance().isIPv6Range(null));
        assertFalse(CIDRUtil.getInstance().isIPv6Range(""));
        assertFalse(CIDRUtil.getInstance().isIPv6Range("192.168.0.1909"));
        assertFalse(CIDRUtil.getInstance().isIPv6Range("adsdsa"));

        assertTrue(CIDRUtil.getInstance().isIPv6Range("FE80:0000:0000:0000:0202:B3FF:FE1E:8329/10"));
        assertTrue(CIDRUtil.getInstance().isIPv6Range("FE80::0202:B3FF:FE1E:8329/10"));
    }


    /**
     * Test address
     */
    @Test
    public void testAddressRange() {
        assertFalse(CIDRUtil.getInstance().isValidRange(null));
        assertFalse(CIDRUtil.getInstance().isValidRange(""));
        assertFalse(CIDRUtil.getInstance().isValidRange("192.168.0.1909"));
        assertFalse(CIDRUtil.getInstance().isValidRange("adsdsa"));

        assertTrue(CIDRUtil.getInstance().isValidRange("FE80:0000:0000:0000:0202:B3FF:FE1E:8329/10"));
        assertTrue(CIDRUtil.getInstance().isValidRange("FE80::0202:B3FF:FE1E:8329/10"));
        assertTrue(CIDRUtil.getInstance().isValidRange("192.168.0.1/10"));
    }


    /**
     * Test if an address is in a range
     */
    @Test
    public void testIsInRange() {
        assertFalse(CIDRUtil.getInstance().isInRange(null, null));
        assertFalse(CIDRUtil.getInstance().isInRange("", null));
        assertFalse(CIDRUtil.getInstance().isInRange(null, ""));
        assertFalse(CIDRUtil.getInstance().isInRange("", ""));

        assertFalse(CIDRUtil.getInstance().isInRange("localhostadas", "10.41.20.82"));
        assertTrue(CIDRUtil.getInstance().isInRange("10.41.20.82", "10.41.20.82"));
        assertFalse(CIDRUtil.getInstance().isInRange("localhost", "10.41.20.82"));
        assertTrue(CIDRUtil.getInstance().isInRange("localhost", "127.0.0.1"));
        assertTrue(CIDRUtil.getInstance().isInRange("127.0.0.1", "127.0.0.1"));
        assertTrue(CIDRUtil.getInstance().isInRange("localhost", "localhost"));

        assertTrue(CIDRUtil.getInstance().isInRange("160.46.252.0/24", "160.46.252.16"));
        assertTrue(CIDRUtil.getInstance().isInRange("192.109.190.18/32", "192.109.190.18"));
        assertTrue(CIDRUtil.getInstance().isInRange("195.141.185.168/32", "195.141.185.168"));
        assertTrue(CIDRUtil.getInstance().isInRange("192.109.190.18/32", "192.109.190.18"));

        assertFalse(CIDRUtil.getInstance().isInRange("10.41.20.205", "10.41.20.82"));
        assertTrue(CIDRUtil.getInstance().isInRange("10.0.0.0/24 ", "10.0.0.0"));
        assertTrue(CIDRUtil.getInstance().isInRange("10.0.0.0/24 ", "10.0.0.1"));
        assertTrue(CIDRUtil.getInstance().isInRange("10.0.0.0/24 ", "10.0.0.255"));
        assertFalse(CIDRUtil.getInstance().isInRange("10.0.0.0/24", "10.0.1.1"));
        assertTrue(CIDRUtil.getInstance().isInRange("10.0.0.0/28 ", "10.0.0.1"));
        assertTrue(CIDRUtil.getInstance().isInRange("10.0.0.0/28 ", "10.0.0.2"));
        assertTrue(CIDRUtil.getInstance().isInRange("10.0.0.0/28 ", "10.0.0.15"));
        assertTrue(CIDRUtil.getInstance().isInRange("10.0.0.0/28 ", "10.0.0.15"));
        assertFalse(CIDRUtil.getInstance().isInRange("10.0.0.0/28 ", "10.0.0.16"));
    }
    
    
    /**
     * Test the CIDR for ipv4
     *
     * @throws UnknownHostException In case of invalid host address
     */
    @Test
    public void testIP4() throws UnknownHostException {
        Assertions.assertThrows(UnknownHostException.class, () -> {
            CIDRUtil.getInstance().parse(null).getNetworkAddress();
        });

        Assertions.assertThrows(UnknownHostException.class, () -> {
            CIDRUtil.getInstance().parse("   ").getNetworkAddress();
        });

        Assertions.assertThrows(UnknownHostException.class, () -> {
            CIDRUtil.getInstance().parse("10.77.12.11").getNetworkAddress();
        });

        assertEquals("10.77.0.0", CIDRUtil.getInstance().parse("10.77.12.11/18").getNetworkAddress());
        assertEquals("10.77.63.255", CIDRUtil.getInstance().parse("10.77.12.11/18").getBroadcastAddress());
        assertTrue(CIDRUtil.getInstance().parse("10.77.12.11/18").isInRange("10.77.12.22"));
    }

    
    /**
     * Test the CIDR for ipv6
     *
     * @throws UnknownHostException In case of invalid host address
     */
    @Test
    public void testIP6() throws UnknownHostException {
        assertEquals("435:23f:0:0:0:0:0:0", CIDRUtil.getInstance().parse("435:23f::45:23/101").getNetworkAddress());
        assertEquals("435:23f:0:0:0:0:7ff:ffff", CIDRUtil.getInstance().parse("435:23f::45:23/101").getBroadcastAddress());
        assertTrue(CIDRUtil.getInstance().parse("435:23f::45:23/101").isInRange("435:23f::45:27"));
        
        assertEquals("fe80:0:0:0:0:0:0:0", CIDRUtil.getInstance().parse("FE80:0000:0000:0000:0202:B3FF:FE1E:8329/10").getNetworkAddress());
        assertEquals("febf:ffff:ffff:ffff:ffff:ffff:ffff:ffff", CIDRUtil.getInstance().parse("FE80:0000:0000:0000:0202:B3FF:FE1E:8329/10").getBroadcastAddress());
        assertTrue(CIDRUtil.getInstance().parse("FE80:0000:0000:0000:0202:B3FF:FE1E:8329/10").isInRange("fe80:0:0:0:0:0:0:1"));

        assertEquals("fe80:0:0:0:0:0:0:0", CIDRUtil.getInstance().parse("FE80::0202:B3FF:FE1E:8329/10").getNetworkAddress());
        assertEquals("febf:ffff:ffff:ffff:ffff:ffff:ffff:ffff", CIDRUtil.getInstance().parse("FE80::0202:B3FF:FE1E:8329/10").getBroadcastAddress());
        assertTrue(CIDRUtil.getInstance().parse("FE80::0202:B3FF:FE1E:8329/10").isInRange("fe80:0:0:0:0:0:0:1"));
    }


    /**
     * Test in range
     *
     * @throws UnknownHostException In case of invalid host address
     */
    @Test
    public void testIP4InRange() throws UnknownHostException {
        assertTrue(CIDRUtil.getInstance().parse("192.168.0.1/10").isInRange("192.168.0.2"));
        assertTrue(CIDRUtil.getInstance().parse("160.46.252.0/24").isInRange("160.46.252.16"));
        assertTrue(CIDRUtil.getInstance().parse("192.109.190.18/32").isInRange("192.109.190.18"));
        assertTrue(CIDRUtil.getInstance().parse("195.141.185.168/32").isInRange("195.141.185.168"));
        assertTrue(CIDRUtil.getInstance().parse("192.109.190.18/32").isInRange("192.109.190.18"));
        assertTrue(CIDRUtil.getInstance().parse("10.0.0.0/24").isInRange("10.0.0.0"));
        assertTrue(CIDRUtil.getInstance().parse("10.0.0.0/24").isInRange("10.0.0.1"));
        assertTrue(CIDRUtil.getInstance().parse("10.0.0.0/24").isInRange("10.0.0.255"));
        assertFalse(CIDRUtil.getInstance().parse("10.0.0.0/24").isInRange("10.0.1.1"));
        assertTrue(CIDRUtil.getInstance().parse("10.0.0.0/28").isInRange("10.0.0.1"));
        assertTrue(CIDRUtil.getInstance().parse("10.0.0.0/28").isInRange("10.0.0.2"));
        assertTrue(CIDRUtil.getInstance().parse("10.0.0.0/28").isInRange("10.0.0.15"));
        assertTrue(CIDRUtil.getInstance().parse("10.0.0.0/28").isInRange("10.0.0.15"));
        assertFalse(CIDRUtil.getInstance().parse("10.0.0.0/28").isInRange("10.0.0.16"));
    }
}
