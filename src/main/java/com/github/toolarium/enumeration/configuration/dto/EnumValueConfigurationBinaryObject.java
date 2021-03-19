/*
 * EnumValueConfigurationBinaryObject.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * Defines the binary object
 * 
 * @author patrick
 */
public class EnumValueConfigurationBinaryObject implements Serializable {
    private static final long serialVersionUID = 2915537029181701495L;
    
    private String name;
    private Instant timestamp;
    private String mimetype;
    private String data;
    
    
    /**
     * Constructor for EnumValueConfigurationBinaryObject
     */
    public EnumValueConfigurationBinaryObject() {        
    }

    
    /**
     * Constructor for EnumValueConfigurationBinaryObject
     * 
     * @param name the name
     * @param timestamp the timestamp
     * @param mimetype the mime type
     * @param data the data
     */
    public EnumValueConfigurationBinaryObject(String name, Instant timestamp, String mimetype, String data) {
        this.name = name;
        this.timestamp = timestamp;
        this.mimetype = mimetype;
        this.data = data;
    }


    /**
     * Get the name
     *
     * @return the name
     */
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
     * Get the timestamp
     *
     * @return the timestamp
     */
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
     * Get the mime type
     *
     * @return the mime type
     */
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
     * Get the data
     *
     * @return the data
     */
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
        
        EnumValueConfigurationBinaryObject other = (EnumValueConfigurationBinaryObject) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        
        if (timestamp == null) {
            if (other.timestamp != null) {
                return false;
            }
        } else if (!timestamp.equals(other.timestamp)) {
            return false;
        }
        
        
        if (mimetype == null) {
            if (other.mimetype != null) {
                return false;
            }
        } else if (!mimetype.equals(other.mimetype)) {
            return false;
        }
        
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnumValueConfigurationBinaryObject [name=" + name + ", timestamp=" + timestamp + ", mimetype="
                + mimetype + ", dataHash=" + ("" + data).hashCode() + "]";
    }
}
