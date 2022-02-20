/*
 * SortedProperties.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;


/**
 * Implements a sorted properties
 * 
 * @author patrick
 */
public class SortedProperties extends Properties {
    private static final long serialVersionUID = -3092093908855747184L;

    
    /**
     * Constructor
     */
    public SortedProperties() {
    }


    /**
     * Constructor
     *
     * @param unsortedProperties the properties
     */
    public SortedProperties(Properties unsortedProperties) {
        putAll(unsortedProperties);
    }

    
    /**
     * @see java.util.Properties#keys()
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes" })
    public synchronized Enumeration keys() {
        Enumeration<Object> keysEnum = super.keys();
        Vector<String> keyList = new Vector<String>();
        while (keysEnum.hasMoreElements()) {
            keyList.add((String) keysEnum.nextElement());
        }
        
        Collections.sort(keyList);
        return keyList.elements();
    }

    
    /**
     * @see java.util.Properties#entrySet()
     */
    @Override
    public Set<java.util.Map.Entry<Object, Object>> entrySet() {
        // use a TreeMap since in java 9 entrySet() instead of keys() is used in store()
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        Set<Map.Entry<Object, Object>> entrySet = super.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        return Collections.synchronizedSet(treeMap.entrySet());
    }
    
    /**
     * @see java.util.Properties#toString()
     */
    @Override
    public synchronized String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("{");
        int counter = 0; 
        for (Map.Entry<Object, Object> entry : entrySet()) {
            if (counter > 0) {
                builder.append(", ");
            }
            builder.append("" + entry.getKey() + "=" + entry.getValue());
            counter++;
        }
        builder.append("}");
        return builder.toString();
    }
}
