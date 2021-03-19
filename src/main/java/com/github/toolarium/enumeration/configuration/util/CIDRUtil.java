/*
 * CIDRUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * CIDR utility
 * 
 * @author patrick
 */
public final class CIDRUtil {
    /** Regular expression: ipv4 address pattern */
    public static final String IPV4_EXPRESSION = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    
    /** Regular expression: ipv6 address pattern */
    public static final String IPV6_EXPRESSION_STD = "(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}";

    /** Regular expression: ipv6 address pattern */
    public static final String IPV6_EXPRESSION_HEX_COMPRESSED = "((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)";

    /** Regular expression: ipv6 address pattern */
    public static final String IPV6_EXPRESSION = "^((" + IPV6_EXPRESSION_STD + ")|(" + IPV6_EXPRESSION_HEX_COMPRESSED + "))$";

    /** Regular expression: ipv4 address range pattern */
    public static final  String IPV4_RANGE_EXPRESSION = "^([0-9]{1,3}\\.){3}[0-9]{1,3}" + "/([0-9]|[1-2][0-9]|3[0-2])$";
    
    /** Regular expression: ipv6 address range pattern */
    public static final  String IPV6_RANGE_EXPRESSION = "^s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}"
                                                        + "(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}"
                                                        + "(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}"
                                                        + "(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}"
                                                        + "(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}"
                                                        + "(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}"
                                                        + "(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|"
                                                        + "(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:)))(%.+)?s*"
                                                        + "/([0-9]|[1-9][0-9]|1[0-1][0-9]|12[0-8])$";
    
    private Pattern ipv4Expression;
    private Pattern ipv6Expression;
    private Pattern ipv4RangeExpression;
    private Pattern ipv6RangeExpression;
    
    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     */
    private static class HOLDER {
        static final CIDRUtil INSTANCE = new CIDRUtil();
    }

    
    /**
     * Constructor
     */
    private CIDRUtil() {
        ipv4Expression = Pattern.compile(IPV4_EXPRESSION);
        ipv6Expression = Pattern.compile(IPV6_EXPRESSION);
        ipv4RangeExpression = Pattern.compile(IPV4_RANGE_EXPRESSION);
        ipv6RangeExpression = Pattern.compile(IPV6_RANGE_EXPRESSION);
                
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static CIDRUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Validate if the host string is instance of ipv4 or ipv6 address
     *
     * @param host the host
     * @return true if it is valid otherwise false
     */
    public boolean isValidAddress(String host) {
        if (host == null || host.trim().length() == 0) {
            return false;
        }
        
        if (isIPv4Address(host) || isIPv6Address(host)) {
            return  true;
        } 
        
        // try to resolve address
        InetAddress ipAddress = parseAddress(host);
        if (ipAddress != null) {
            String address = ipAddress.getHostAddress();
            if (address != null && (isIPv4Address(address) || isIPv6Address(address))) {
                return true;
            }
        }
        
        return false;
    }

    
    /**
     * Validate if the given host is an instance of ipv4 or ipv6 address
     *
     * @param host the host
     * @return true if it is valid otherwise false
     */
    public boolean isValidAddressRange(String host) {
        if (host == null || host.trim().length() == 0) {
            return false;
        }

        if (isIPv4Range(host) || isIPv6Range(host)) {
            return true;
        }
        
        // try to resolve address
        InetAddress ipAddress = parseAddress(host);
        if (ipAddress != null) {
            String address = ipAddress.getHostAddress();
            if (address != null && (isIPv4Range(address) || isIPv6Range(address))) {
                return true;
            }
        }
        
        return false;
    }

    
    /**
     * Validate if the given host is an instance of ipv4
     *
     * @param host the host
     * @return true if it is valid otherwise false
     */
    public boolean isIPv4Address(String host) {
        if (host == null || host.trim().length() == 0) {
            return false;
        }

        return ipv4Expression.matcher(host).matches();
    }


    /**
     * Validate if the given host is an instance of ipv6 address
     *
     * @param host the host
     * @return true if it is valid otherwise false
     */
    public boolean isIPv6Address(String host) {
        if (host == null || host.trim().length() == 0) {
            return false;
        }
        
        return ipv6Expression.matcher(host).matches();
    }

    
    /**
     * Check if the host string is a valid IP address (cidr notation)
     *
     * @param host the host
     * @return true if it is
     */
    public boolean isValidRange(String host) {
        if (host == null || host.trim().length() == 0) {
            return false;
        }
        
        if (isIPv4Range(host) || isIPv6Range(host)) {
            return true;
        }

        return false;
    }

    
    /**
     * Check if the host string is instance of IPv4 range (cidr notation)
     *
     * @param host the host
     * @return true if it is
     */
    public boolean isIPv4Range(String host) {
        if (host == null || host.trim().length() == 0) {
            return false;
        }

        return ipv4RangeExpression.matcher(host).matches();
    }

    
    /**
     * Check if the host string is instance of IPv6 range (cidr notation)
     *
     * @param host the host
     * @return true if it is
     */
    public boolean isIPv6Range(String host) {
        if (host == null || host.trim().length() == 0) {
            return false;
        }

        return ipv6RangeExpression.matcher(host).matches();
    }

    
    /**
     * Check if the address is an ip address or an ip address range. IPv4 and IPv6 are supported.
     *
     * @param hostRange the host range
     * @param host the host to check
     * @return true if the host name is belong to given range
     */
    public boolean isInRange(String hostRange, String host) {
        // check host
        if (!isValidAddress(host)) {
            //log.debug("Invalid host [" + host + "]!");
            return false;
        }

        InetAddress inetAdress = parseAddress(host);
        if (inetAdress == null) {
            //log.debug("Could not resolve host [" + host + "]!");
            return false;
        }

        String remoteAddress = inetAdress.getHostAddress();
        if (remoteAddress == null || !isValidAddress(remoteAddress)) {
            //log.debug("Invalid remote host [" + host + "]!");
            return false;
        }

        if (!remoteAddress.equals(host)) {
            //log.debug("Resolved host address [" + host + "] as [" + remoteAddress + "].");
        }

        String hostRangeAddress = hostRange.trim();
        if (hostRange.equals(host) || hostRange.equals(remoteAddress)) {
            if (hostRange.equals(host)) {
                //log.debug("Host [" + host + "] and host range [" + hostRange + "] are equals.");
            } else if (hostRange.equals(remoteAddress)) {
                //log.debug("Remote host [" + remoteAddress + "] and host range [" + hostRange + "] are equals.");
            }

            return true;
        }

        if (!isValidRange(hostRangeAddress)) {
            // it could be a hostname
            inetAdress = parseAddress(hostRangeAddress);
            if (inetAdress == null) {
                //log.debug("Invalid host range [" + hostRangeAddress + "]!");
                return false;
            }

            hostRangeAddress = inetAdress.getHostAddress();
            if (hostRangeAddress == null || !isValidAddress(hostRangeAddress)) {
                //log.debug("Invalid host range address [" + hostRangeAddress + "]!");
                return false;
            }

            //log.debug("Host [" + remoteAddress + "] and host range [" + hostRangeAddress + "] are equals.");
            return hostRangeAddress.equals(remoteAddress);
        }

        boolean result = false;
        try {
            CIDR cidr = parse(hostRangeAddress);
            result = cidr.isInRange(remoteAddress) || cidr.getBroadcastAddress().equals(remoteAddress) || cidr.getNetworkAddress().equals(remoteAddress);
        } catch (Exception e) {
            //log.error("Error occured while check address [" + remoteAddress + "] in range [" + hostRangeAddress + "]: " + e.getMessage(), e);
        }

        //log.debug("Check remote address [" + remoteAddress + "] in host range [" + hostRangeAddress + "]: " + result);
        return result;
    }
    
    
    /**
     * Parse CIDR expression
     * 
     * @param cidrExpression the CIDR expression
     * @return the processed CIDR
     * @throws UnknownHostException In case of invalid address
     */
    public CIDR parse(String cidrExpression) throws UnknownHostException {

        if (cidrExpression == null || cidrExpression.trim().isEmpty() || !cidrExpression.contains("/")) {
            throw new UnknownHostException("Invalid CIDR format: [" + cidrExpression + "]!");
        }

        final String cidr = cidrExpression.trim(); 
        int index = cidr.indexOf("/");
        String addressPart = cidr.substring(0, index);
        String networkPart = cidr.substring(index + 1);
        return prepareCIDR(InetAddress.getByName(addressPart), Integer.parseInt(networkPart));
    }


    /**
     * Prepare CIDR 
     *
     * @param inetAddress the internet address
     * @param prefixLength the prefix length
     * @return the CIDR
     * @throws UnknownHostException In case of invalid address
     */
    private CIDR prepareCIDR(InetAddress inetAddress, int prefixLength) throws UnknownHostException {
        ByteBuffer maskBuffer;
        int targetSize;
        if (inetAddress.getAddress().length == 4) {
            maskBuffer = ByteBuffer.allocate(4).putInt(-1);
            targetSize = 4;
        } else {
            maskBuffer = ByteBuffer.allocate(16).putLong(-1L).putLong(-1L);
            targetSize = 16;
        }

        BigInteger mask = (new BigInteger(1, maskBuffer.array())).not().shiftRight(prefixLength);

        ByteBuffer buffer = ByteBuffer.wrap(inetAddress.getAddress());
        BigInteger ipVal = new BigInteger(1, buffer.array());

        BigInteger startIp = ipVal.and(mask);
        BigInteger endIp = startIp.add(mask.not());

        byte[] startIpArr = toBytes(startIp.toByteArray(), targetSize);
        byte[] endIpArr = toBytes(endIp.toByteArray(), targetSize);

        return new CIDR(InetAddress.getByAddress(startIpArr), InetAddress.getByAddress(endIpArr));
    }

    
    /**
     * Prepare byte array
     *
     * @param input the input bytes
     * @param targetSize the target size
     * @return the prepared byte array
     */
    private byte[] toBytes(byte[] input, int targetSize) {
        int counter = 0;
        List<Byte> newArr = new ArrayList<Byte>();
        while (counter < targetSize && (input.length - 1 - counter >= 0)) {
            newArr.add(0, input[input.length - 1 - counter]);
            counter++;
        }

        int size = newArr.size();
        for (int i = 0; i < (targetSize - size); i++) {

            newArr.add(0, (byte) 0);
        }

        byte[] ret = new byte[newArr.size()];
        for (int i = 0; i < newArr.size(); i++) {
            ret[i] = newArr.get(i);
        }
        
        return ret;
    }
    
    
    /**
     * Parse the ip address
     *
     * @param address the address to parse
     * @return the inet addres
     */
    private InetAddress parseAddress(String address) {
        try {
            return InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            return null;
        }
    }
    
    /**
     * CIDR
     * 
     * @author patrick
     */
    class CIDR {
        private InetAddress startAddress;
        private InetAddress endAddress;
    
        
        /**
         * Constructor for CIDR
         * 
         * @param startAddress the start address
         * @param endAddress the end address
         */
        CIDR(InetAddress startAddress, InetAddress endAddress) {
            this.startAddress = startAddress;
            this.endAddress = endAddress;
        }
        
        
        /**
         * The network address
         *
         * @return the network address
         */
        public String getNetworkAddress() {
            return this.startAddress.getHostAddress();
        }

        
        /**
         * The broadcast address
         *
         * @return tThe broadcast address
         */
        public String getBroadcastAddress() {
            return this.endAddress.getHostAddress();
        }
    
        
        /**
         * Check if a given address is in range
         *
         * @param ipAddress the address to check
         * @return true if it is in range.
         * @throws UnknownHostException In case if an invalid address
         */
        public boolean isInRange(String ipAddress) throws UnknownHostException {
            InetAddress address = InetAddress.getByName(ipAddress);
            BigInteger start = new BigInteger(1, this.startAddress.getAddress());
            BigInteger end = new BigInteger(1, this.endAddress.getAddress());
            BigInteger target = new BigInteger(1, address.getAddress());
    
            int st = start.compareTo(target);
            int te = target.compareTo(end);
    
            return (st == -1 || st == 0) && (te == -1 || te == 0);
        }
    }
}
