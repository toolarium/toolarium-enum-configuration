/*
 * IEnumKeyValueConfigurationBinaryObject.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import java.time.Instant;

/**
 * Define the binary object interface
 * 
 * @author patrick
 */
public interface IEnumKeyValueConfigurationBinaryObject {
    
    /**
     * Get the name
     *
     * @return the name
     */
    String getName();


    /**
     * Get the mime type
     *
     * @return the mime type
     */
    String getMimetype();


    /**
     * Get the timestamp
     *
     * @return the timestamp
     */
    Instant getTimestamp();


    /**
     * Get the data
     *
     * @return the data
     */
    String getData();
    
    
    /**
     * The strign representation of the binary type:
     * Format: <code>[name]|[timestamp]|{mime-type}[content base64 encoded].</code>
     * The name, timestamp (according to RFC 3339), mime-type and the content  (base64 encoded content) are optional values. If the name, timestamp or content are present, 
     * they must be separated by a pipe character (|). The mime-type can be declared as header of the content. If there is no pipe character it's assumed its the content part.  
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
     *
     * @return the string representation
     */
    String toString();
}
