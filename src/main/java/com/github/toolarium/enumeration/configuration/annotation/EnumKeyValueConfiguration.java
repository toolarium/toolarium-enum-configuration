/*
 * EnumKeyValueConfiguration.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Defines the enumeration as a value based configuration.
 * 
 * @author patrick
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EnumKeyValueConfiguration {
    
    /**
     * Define the configuration value data type
     */
    enum DataType {
        
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

        /** A certficate or key, e.g. X509 certificate <code></code> */
        CERTIFICATE,
        
        /**
         * <p>A binary content which represents as example an entire file, format: <code>[name]|[timestamp]|{mime-type}[content base64 encoded].</code></p>
         * 
         * <p>The name, timestamp (according to RFC 3339), mime-type and the content  (base64 encoded content) are optional values. If the name, timestamp or content are present, 
         * they must be separated by a pipe character (|). The mime-type can be declared as header of the content. If there is no pipe character it's assumed its the content part.</p>
         * 
         * <p>Best practise is to set the name and the mime-type in case of a file as a default as example: <code>defaultValue = "defaultname.txt|{plain/text}"</code>. At runtime 
         * only the base64 encoded content will be stored, e.g. <code>VGV4dAo=</code>. The default value information will then be merged and end up as <code>defaultname.txt|{plain/text}VGV4dAo=</code>
         *   
         * <p>sample entries:
         * e.g. <code>myfile.txt|2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo=</code> 
         *   or <code>myfile.txt|2021-03-15T08:59:22.123Z|{text/plain}</code> 
         *   or <code>myfile.txt|2021-03-15T08:59:22.123Z|VGV4dAo=</code> 
         *   or <code>2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo=</code>
         *   or <code>2021-03-15T08:59:22.123Z|{text/plain}</code>
         *   or <code>2021-03-15T08:59:22.123Z|VGV4dAo=</code>
         *   or <code>myfile.txt|{text/plain}VGV4dAo=</code>
         *   or <code>myfile.txt|{text/plain}</code>  
         *   or <code>myfile.txt|VGV4dAo=</code> 
         *   or <code>{text/plain}VGV4dAo=</code>
         *   or <code>{text/plain}</code>  
         *   or <code>VGV4dAo=</code>
         *   </p>  
         */
        BINARY
    }
    
    
    /**
     * Defines the description of the enumeration configuration.
     * 
     * @return The description of the enumeration configuration.
     */
    String description();

    
    /**
     * The configuration value data type.
     *
     * @return The configuration value data type..
     */
    DataType dataType() default DataType.STRING;

    
    /**
     * The default value.
     * The default value belongs to the dataType, minValue/maxValue and the cardinality.
     * 
     * @return The default value.
     */
    String defaultValue() default "";

    
    /**
     * An example value. 
     * The example value belongs to the dataType, minValue/maxValue and the cardinality.
     *
     * @return An example value.
     */
    String exampleValue();

    
    /**
     * An enumeration value: If this is defined, only an enumeration value is accepted for defaultValue, exampleValue and value.  
     * The enumeration value belongs to the dataType, minValue/maxValue and the cardinality.
     *
     * @return An example value.
     */
    String enumerationValue() default "";

    
    /**
     * The minimum value, e.g. by a string it corresponds to the minimum length of the string. In case of a number its the minimum value.
     * Empty means no minimum restriction.
     *
     * @return The minimum value.
     */
    String minValue() default "";
    
    
    /**
     * The maximal value, e.g. by a string it corresponds to the maximal length of the string. In case of a number its the maximal value.
     * Empty means no maximal restriction.
     * 
     * @return The maximal value.
     */
    String maxValue() default "";
    
    
    /**
     * <p>The cardinality expression for an entry. If there is more than one element, the content of the representation corresponds to a
     * JSON array, e.g. <code>[ "My string array", "with 3", "elements (cardinality = 3)" ]</code>. This means the cardinality can be 
     * combined with the different data types.</p>
     * 
     * <p>The cardinality is defined as a range: <code>[min]..[max]</code>:
     * <ul> 
     * <li>If the cardinality is empty (=blank string), it must be interpreted as <code>1..1</code> (min = 1 and max = 1).</li>
     * <li>The min value is optional and can be ignored, e.g. <code>5</code> (this corresponds to <code>1..5</code>)</li>
     * <li>If the max has no limit it can be defined as <code>*</code> or <code>1..*</code></li>
     * <li>If the max has no limit and its optional it can be defined as <code>0..*</code></li>
     * </ul>
     * </p>
     * @return The cardinality expression.
     */
    String cardinality() default "1..1";

    
    /**
     * Specifies that the input value is unique. This is only relevant if you have a cardinality.
     * 
     * @return True if it is unique; otherwise false, which means that the same value can occur more than once.
     */
    boolean isUniqueness() default false;

    
    /**
     * Determines whether the configuration value is confidential. In case of persistence it has to be secured.
     * 
     * @return Define if the value is confidential.
     */
    boolean isConfidential() default false;

    
    /**
     * Defines the valid from date of the enumeration configuration. By default it's empty which means the current timestamp:
     * <code>DateTimeFormatter.ISO_INSTANT.format(Instant.now().truncatedTo(ChronoUnit.MILLIS))</code>.
     * 
     * @return The valid from information
     */
    String validFrom() default "";

    
    /**
     * Defines the valid till date of the enumeration configuration. By default it's the "max" timestamp.
     * 
     * @return The valid till information
     */
    String validTill() default "9999-12-31T12:00:00.000Z";
}
