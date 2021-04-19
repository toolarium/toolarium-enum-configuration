/*
 * ReflectionUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Defines the reflection utility.
 * 
 * @author patrick
 */
public final class ReflectionUtil {
    private volatile boolean defaultDisabledAccessWarnings;
    private volatile boolean disabledAccessWarnings;

    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final ReflectionUtil INSTANCE = new ReflectionUtil();
    }

    /**
     * Constructor
     */
    private ReflectionUtil() {
        disabledAccessWarnings = false;
        defaultDisabledAccessWarnings = false;
        
        String prop = System.getProperty("disabledAccessWarnings");
        if (prop != null && prop.trim().equalsIgnoreCase("true")) {
            defaultDisabledAccessWarnings = true;
        }
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ReflectionUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Create a new instance of a given class
     * 
     * @param <T> the generic type
     * @param clazz the class
     * @return the instance
     * @throws SecurityException in case of error
     * @throws InvocationTargetException in case of error
     * @throws IllegalArgumentException in case of error
     * @throws IllegalAccessException in case of error
     * @throws InstantiationException in case of error
     */
    @SuppressWarnings("unchecked")
    public <T> T newInstance(Class<T> clazz) throws SecurityException, InstantiationException, IllegalAccessException {
        if (clazz == null) {
            throw new InstantiationException("Invalid empty class!");
        }
        
        try {
            disableAccessWarnings();

            Constructor<?> constructor;
            constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            InstantiationException ex = new InstantiationException(e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }
    
    
    /**
     * Disable access warnings
     */
    public void disableAccessWarnings() {
        if (defaultDisabledAccessWarnings == disabledAccessWarnings) {
            return;
        }
        
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);

            Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class,
                    Object.class);
            Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);
            Class<?> loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = loggerClass.getDeclaredField("logger");
            Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
            disabledAccessWarnings = true;
        } catch (Exception ignored) {
            // NOP
        }
    }
}
