/*
 * JSONUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Simple JSON util
 *
 * @author patrick
 */
public final class JSONUtil {
    private ObjectMapper objectMapper;


    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     */
    private static class HOLDER {
        static final JSONUtil INSTANCE = new JSONUtil();
    }


    /**
     * Constructor
     */
    private JSONUtil() {
        // NOP
    }


    /**
     * Get the instance
     *
     * @return the instance
     */
    public static JSONUtil getInstance() {
        return HOLDER.INSTANCE;
    }


    /**
     * Reads an object from an input stream.
     *
     * @param <T> the generic type
     * @param type the class
     * @param inputstream the input stream
     * @return the read object
     * @throws IOException In case of an error
     */
    public <T> T read(Class<T> type, InputStream inputstream) throws IOException {
        if (inputstream == null) {
            return null;
        }

        try {
            return getMapper().readValue(inputstream, type);
        } catch (JsonMappingException | JsonParseException e) {
            final String msg = e.getMessage();

            if (msg != null && msg.startsWith("No content")) {
                return null;
            }

            IOException ex = new IOException(e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        } catch (IOException e) {
            throw e;
        }
    }


    /**
     * Writes an object to an output stream.
     *
     * @param <T> the generic type
     * @param type the object to write
     * @param outputStream the output stream
     * @throws IOException In case of an error
     */
    public <T> void write(T type, OutputStream outputStream) throws IOException {
        if (outputStream == null || type == null) {
            return;
        }

        try {
            getMapper().writerWithDefaultPrettyPrinter().writeValue(outputStream, type);
        } catch (JsonMappingException | JsonParseException e) {
            IOException ex = new IOException(e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }


    /**
     * Convert a list of strings into a JSON array
     *
     * @param list the string list
     * @return the list as json array
     * @throws IllegalArgumentException In case of invalid json format
     */
    public String convert(Collection<String> list) throws IllegalArgumentException {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            write(list, outputStream);
            return outputStream.toString();
        } catch (Exception e) {
            IllegalArgumentException ex = new IllegalArgumentException(e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }


    /**
     * Convert a JSON array into a list of strings
     *
     * @param json the json array
     * @return the json array as string list
     * @throws IllegalArgumentException In case of invalid json format
     */
    public List<String> convert(String json) throws IllegalArgumentException {

        if (json == null) {
            return null;
        }

        try {
            if (!json.startsWith("[") && !json.endsWith("]")) {
                return Arrays.asList(json);
            }

            String[] elements = getMapper().readValue(json, String[].class);
            if (elements != null && elements.length > 0) {
                return Arrays.asList(elements);
            }
        } catch (Exception e) {
            IllegalArgumentException ex = new IllegalArgumentException(e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }

        return null;
    }


    /**
     * Get the object mapper
     *
     * @return the object mapper
     */
    private ObjectMapper getMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return objectMapper;
    }
}
