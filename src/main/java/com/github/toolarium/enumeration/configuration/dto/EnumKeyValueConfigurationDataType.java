/*
 * EnumKeyValueConfigurationDataType.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import java.io.Serializable;


/**
 * Defines the enumeration value configuration data types.
 * 
 * @author patrick
 */
public enum EnumKeyValueConfigurationDataType implements Serializable {
    
    /** A string text, e.g. <code>my text</code> */
    STRING,
    
    /** A positive or negative number without any decimal places, e.g. <code>123456789</code> */
    NUMBER,
    
    /** A positive or negative number with decimal places, e.g. <code>12.34567</code> */
    DOUBLE,
    
    /** Boolean value: <code>true</code> (or <code>yes</code>) / <code>false</code> (or <code>no</code>) */
    BOOLEAN,
    
    /** A date without time information, full-date notation as defined by RFC 3339, section 5.6 (https://tools.ietf.org/html/rfc3339#section-5.6), e.g. <code>2021-03-15</code> */
    DATE,
    
    /** A time without date information, full-time notation as defined by RFC 3339, section 5.6 (https://tools.ietf.org/html/rfc3339#section-5.6), e.g. <code>12:34:56.789</code> */
    TIME,
    
    /** A timestamp, date-time notation as defined by RFC 3339, section 5.6 (https://tools.ietf.org/html/rfc3339#section-5.6), e.g. <code>2021-03-15T08:59:22.123Z</code> */
    TIMESTAMP,

    /** A regular expression (based on java regexp), e.g. <code>.*(jim|joe).*</code> */
    REGEXP,

    /** An Universally Unique IDentifier, notation as defined by RFC 4122 (https://tools.ietf.org/html/rfc4122), e.g. <code>f81d4fae-7dec-11d0-a765-00a0c91e6bf6</code> */
    UUID,

    /** A uri, notation as defined by RFC 2396, section 1.3 (https://tools.ietf.org/html/rfc2396), e.g. <code>https://my.url.com</code> */
    URI,

    /** A cidr, notation as defined by RFC 4632 and 4291 (https://tools.ietf.org/html/rfc4632, https://tools.ietf.org/html/rfc4291), e.g. <code>10.2.0.0/16</code> or <code>FF00::/8</code> */ 
    CIDR,
    
    /** An eamil address, e.g. <code>mail@domain</code> */
    EMAIL,

    /** A cron schedule expression, e.g. <code>* * * * 1</code> */
    CRON,

    /** A color code, e.g. <code>#40394A</code> */
    COLOR,

    /** A certficate or key, e.g. X509 certificate <code></code>*/
    CERTIFICATE,
    
    /** 
     * A binary content which represents as example an entire file, 
     * format: <code>[name]|[timestamp]|[mime-type]|[content base64 encoded].</code> 
     * The name, timestamp (as RFC 3339), mimetype are optional, only base64 content is a valid content.,
     * e.g. <code>myfile.txt|2021-03-15T08:59:22.123Z|text/plain|VGV4dAo=</code> or <code>myfile.txt|||VGV4dAo=</code> or <code>VGV4dAo=</code>   
     */
    BINARY;
}
