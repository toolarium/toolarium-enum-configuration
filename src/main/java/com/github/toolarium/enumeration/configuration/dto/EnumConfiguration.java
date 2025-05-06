/*
 * EnumConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Defines the enum configuration
 *
 * @author patrick
 */
public class EnumConfiguration<T extends EnumKeyConfiguration> extends AbstractEnumConfiguration {
    /** Defines the max timestamp */
    public static final String MAX_TIMESTAMP_STRING = "9999-12-31T12:00:00.000Z";

    /** Defines the max date */
    public static final String MAX_DATE_STRING = "9999-12-31";

    /** Defines the max time */
    public static final String MAX_TIME_STRING = "23:59:59.999";

    private static final long serialVersionUID = -5016414165364299512L;
    private String name;

    @JsonInclude(Include.NON_NULL)
    private String tag;

    private Map<String, T> keyList;
    private Set<String> interfaceList;
    
    @JsonInclude(Include.NON_NULL)
    private Set<String> markerInterfaceList;

    
    /**
     * Constructor
     */
    public EnumConfiguration() {
        super();
        name = null;
        tag = null;
        keyList = new LinkedHashMap<String, T>();
        interfaceList = new LinkedHashSet<String>();
        markerInterfaceList = null;
    }

    
    /**
     * Constructor
     * 
     * @param name the name
     */
    public EnumConfiguration(String name) {
        this();
        setName(name);
    }
    
    
    /**
     * Get the configuration name
     * 
     * @return the configuration name
     */
    public String getName() {
        return name;
    }
    
    
    /**
     * Set the configuration name
     * 
     * @param name the configuration name
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * Get the tag of the enumeration configuration, which is optional and can be used for identify specific enum configurations.
     *
     * @return the configuration tag
     */
    public String getTag() {
        return tag;
    }


    /**
     * Set the tag of the enumeration configuration, which is optional and can be used for identify specific enum configurations.
     *
     * @param tag the configuration tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    
    /**
     * Set the key list
     * 
     * @param <K> The generic type
     * @param keyList the key list.
     */
    @SuppressWarnings("unchecked")
    @JsonDeserialize(as = LinkedHashSet.class)
    public <K extends EnumKeyConfiguration> void setKeyList(Set<K> keyList) {
        this.keyList = new LinkedHashMap<String, T>();
        
        if (keyList != null) {
            for (K k : keyList) {
                add((T)k);
            }
        }
    }

    
    /**
     * Get the key list
     * 
     * @return the key list
     */
    public Set<T> getKeyList() {
        Set<T> result = new LinkedHashSet<T>();
        for (Map.Entry<String, T> e : keyList.entrySet()) {
            result.add(e.getValue());
        }
        
        return result;
    }

    
    /**
     * Set the interface list
     * 
     * @param interfaceList the interface list
     */
    public void setInterfaceList(Set<String> interfaceList) {
        this.interfaceList = interfaceList;
    }

    
    /**
     * Get the interface list
     * 
     * @return the interface list
     */
    public Set<String> getInterfaceList() {
        return interfaceList;
    }

    
    /**
     * Set the marker interface list
     * 
     * @param markerInterfaceList the marker interface list
     */
    public void setMarkerInterfaceList(Set<String> markerInterfaceList) {
        this.markerInterfaceList = markerInterfaceList;
    }

    
    /**
     * Get the marker interface list
     * 
     * @return the marker interface list
     */
    public Set<String> getMarkerInterfaceList() {
        return markerInterfaceList;
    }
    
    
    /**
     * Adds an {@link EnumKeyConfiguration} and corrects validFrom / validTill in case it's not consistent regarding the parent element.
     * 
     * @param enumKeyConfiguration the {@link EnumKeyConfiguration}
     * @return the added enum value configuration
     * @throws IllegalArgumentException In case the key doesn't exist
     */
    public T add(T enumKeyConfiguration) {
        
        if (enumKeyConfiguration == null || enumKeyConfiguration.getKey() == null || enumKeyConfiguration.getKey().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid enum value configuration:" + enumKeyConfiguration);
        }
        
        if (enumKeyConfiguration.getValidFrom() == null || getValidFrom().isAfter(enumKeyConfiguration.getValidFrom())) {
            enumKeyConfiguration.setValidFrom(getValidFrom());
        }
        
        if (enumKeyConfiguration.getValidTill() == null || getValidTill().isBefore(enumKeyConfiguration.getValidTill())) {
            enumKeyConfiguration.setValidTill(getValidTill());
        }

        String key = enumKeyConfiguration.getKey().trim();
        if (!keyList.containsKey(key)) {
            this.keyList.put(key, enumKeyConfiguration);
            return enumKeyConfiguration;
        }
        
        return null;
    }


    /**
     * Returns a list of mandatory {@link EnumKeyValueConfiguration}.
     *
     * @return the list of mandatory {@link EnumKeyValueConfiguration}.
     */
    public Set<T> selectMandatoryConfigurationList() {
        Set<T> result = new LinkedHashSet<>();
        for (Map.Entry<String, T> e : keyList.entrySet()) {
            
            if (EnumKeyValueConfiguration.class.isInstance(e.getValue())) {
                if (((EnumKeyValueConfiguration)e.getValue()).isMandatory()) {
                    result.add(e.getValue());
                }
            } else {
                result.add(e.getValue());
            }
        }
        
        return result;
    }

    
    /**
     * Returns a list of mandatory {@link EnumKeyValueConfiguration} with no default value
     *
     * @return the list of mandatory {@link EnumKeyValueConfiguration}.
     */
    public Set<T> selectMandatoryListWithMissingDefaultValue() {
        Set<T> result = new LinkedHashSet<>();
        for (Map.Entry<String, T> e : keyList.entrySet()) {
            
            if (EnumKeyValueConfiguration.class.isInstance(e.getValue())) {
                if (((EnumKeyValueConfiguration)e.getValue()).isMandatory() && !((EnumKeyValueConfiguration)e.getValue()).hasDefaultValue()) {
                    result.add(e.getValue());
                }
            } 
        }
        
        return result;
    }

    
    /**
     * Check if interfaces matching 
     *
     * @param interfacesToCompare the interfaces to compare
     * @return true if they are matching or not
     */
    public boolean matchInterfaces(Set<String> interfacesToCompare) {
        return listEqualsIgnoreOrder(interfaceList, interfacesToCompare);
    }

    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();

        result = prime * result;
        if (name != null) {
            result += name.hashCode();
        }
        
        if (tag != null) {
            result += tag.hashCode();
        }

        result = prime * result;
        if (keyList != null) {
            result += keyList.hashCode();
        }

        result = prime * result;
        if (interfaceList != null) {
            result += interfaceList.hashCode();
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

        if (!super.equals(obj)) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        EnumConfiguration<T> other = (EnumConfiguration<T>)obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        if (tag == null) {
            if (other.tag != null) {
                return false;
            }
        } else if (!tag.equals(other.tag)) {
            return false;
        }

        if (keyList == null) {
            if (other.keyList != null) {
                return false;
            }
        } else if (!keyList.equals(other.keyList)) {
            return false;
        }

        if (interfaceList == null) {
            if (other.interfaceList != null) {
                return false;
            }
        } else if (!interfaceList.equals(other.interfaceList)) {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EnumConfiguration [name=" + name 
               + ", tag=" + getTag()
               + ", description=" + getDescription()
               + ", validFrom=" + getValidFrom() 
               + ", validTill=" + getValidTill() 
               + ", keyList=" + getKeyList()
               + ", interfaceList=" + getInterfaceList()
               + "]";
    }


    /**
     * Compare
     *
     * @param set1 the first set to compare
     * @param set2 the second set to compare
     * @return true if they are equals
     */
    private boolean listEqualsIgnoreOrder(Set<String> set1, Set<String> set2) {
        if (set1 == null) {
            return set2 == null;
        }
        
        if (set2 == null) {
            return set1 == null;
        }
        
        return new HashSet<>(set1).equals(new HashSet<>(set2));
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/
