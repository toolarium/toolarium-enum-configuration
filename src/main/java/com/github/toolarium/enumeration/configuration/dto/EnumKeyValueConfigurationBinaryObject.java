/*
 * EnumKeyValueConfigurationBinaryObject.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import com.github.toolarium.enumeration.configuration.util.EnumKeyValueConfigurationBinaryObjectParser;
import java.io.Serializable;
import java.time.Instant;

/**
 * Defines the binary object
 * 
 * @author patrick
 */
public class EnumKeyValueConfigurationBinaryObject implements IEnumKeyValueConfigurationBinaryObject, Serializable {
    private static final long serialVersionUID = 2915537029181701495L;
    
    private String name;
    private Instant timestamp;
    private String mimetype;
    private String data;
    
    
    /**
     * Constructor for EnumKeyValueConfigurationBinaryObject
     */
    public EnumKeyValueConfigurationBinaryObject() {
    }

    
    /**
     * Constructor for EnumKeyValueConfigurationBinaryObject
     * 
     * @param enumKeyValueConfigurationBinaryObject the enum key value configuration
     */
    public EnumKeyValueConfigurationBinaryObject(IEnumKeyValueConfigurationBinaryObject enumKeyValueConfigurationBinaryObject) {
        this.name = enumKeyValueConfigurationBinaryObject.getName();
        this.timestamp = enumKeyValueConfigurationBinaryObject.getTimestamp();
        this.mimetype = enumKeyValueConfigurationBinaryObject.getMimetype();
        this.data = enumKeyValueConfigurationBinaryObject.getData();
    }

    
    /**
     * Constructor for EnumKeyValueConfigurationBinaryObject
     * 
     * @param name the name
     * @param timestamp the timestamp
     * @param mimetype the mime type
     * @param data the data
     */
    public EnumKeyValueConfigurationBinaryObject(String name, Instant timestamp, String mimetype, String data) {
        this.name = name;
        this.timestamp = timestamp;
        this.mimetype = mimetype;
        this.data = data;
    }

    
    /**
     * Set all properties which are null or empty from the current with the information from the given object.
     * The data will always be taken from the given object except it empty!
     *
     * @param enumKeyValueConfigurationBinaryObject the data to merge
     */
    public void merge(IEnumKeyValueConfigurationBinaryObject enumKeyValueConfigurationBinaryObject) {

        if (enumKeyValueConfigurationBinaryObject.getName() != null && !enumKeyValueConfigurationBinaryObject.getName().isBlank()) {
            name = enumKeyValueConfigurationBinaryObject.getName();
        }
        
        if (enumKeyValueConfigurationBinaryObject.getTimestamp() != null) {
            timestamp = enumKeyValueConfigurationBinaryObject.getTimestamp();
        }
               
        if (enumKeyValueConfigurationBinaryObject.getMimetype() != null && !enumKeyValueConfigurationBinaryObject.getMimetype().isBlank()) {
            mimetype = enumKeyValueConfigurationBinaryObject.getMimetype();
        }

        if (enumKeyValueConfigurationBinaryObject.getData() != null) {
            data = enumKeyValueConfigurationBinaryObject.getData();
        }
    }

    
    /**
     * @see com.github.toolarium.enumeration.configuration.dto.IEnumKeyValueConfigurationBinaryObject#getName()
     */
    @Override
    public String getName() {
        return name;
    }


    /**
     * Set the name
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.dto.IEnumKeyValueConfigurationBinaryObject#getTimestamp()
     */
    @Override
    public Instant getTimestamp() {
        return timestamp;
    }


    /**
     * Set the timestamp
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.dto.IEnumKeyValueConfigurationBinaryObject#getMimetype()
     */
    @Override
    public String getMimetype() {
        return mimetype;
    }


    /**
     * Set the mime type
     *
     * @param mimetype the mime type
     */
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }


    /**
     * @see com.github.toolarium.enumeration.configuration.dto.IEnumKeyValueConfigurationBinaryObject#getData()
     */
    @Override
    public String getData() {
        return data;
    }


    /**
     * Set the data
     *
     * @param data the data
     */
    public void setData(String data) {
        this.data = data;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        result = prime * result;
        if (name != null) {
            result += name.hashCode();
        }

        result = prime * result;
        if (timestamp != null) {
            result += timestamp.hashCode();
        }

        result = prime * result;
        if (mimetype != null) {
            result += mimetype.hashCode();
        }

        result = prime * result;
        if (data != null) {
            result += data.hashCode();
        }

        return result;
    }

    
    /**
     * Equals
     *
     * @param other the element to compare
     * @return true if equals
     */
    public boolean equals(IEnumKeyValueConfigurationBinaryObject other) {
        if (this == other) {
            return true;
        }
        
        if (other == null) {
            return false;
        }
       
        if (name == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!name.equals(other.getName())) {
            return false;
        }
        
        if (timestamp == null) {
            if (other.getTimestamp() != null) {
                return false;
            }
        } else if (!timestamp.equals(other.getTimestamp())) {
            return false;
        }
        
        if (mimetype == null) {
            if (other.getMimetype() != null) {
                return false;
            }
        } else if (!mimetype.equals(other.getMimetype())) {
            return false;
        }
        
        if (data == null) {
            if (other.getData() != null && !other.getData().isBlank()) {
                return false;
            }
        } else if (!data.equals(other.getData())) {
            return false;
        }
        return true;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        EnumKeyValueConfigurationBinaryObject other = (EnumKeyValueConfigurationBinaryObject) obj;
        return equals(other);
    }


    /**
     * The string representation of the binary type:
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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return EnumKeyValueConfigurationBinaryObjectParser.getInstance().format(this);
        /*
        int dataHash = 0;
        if (data != null) {
            dataHash = data.hashCode();
        }
        return "EnumKeyValueConfigurationBinaryObject [name=" + name + ", timestamp=" + timestamp + ", mimetype="
                + mimetype + ", dataHash=" + dataHash + "]";
                */
    }
}
