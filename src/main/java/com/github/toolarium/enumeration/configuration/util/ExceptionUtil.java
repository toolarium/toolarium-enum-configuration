/*
 * ExceptionUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.github.toolarium.enumeration.configuration.Version;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Exception utility
 * 
 * @author patrick
 */
public final class ExceptionUtil {
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final ExceptionUtil INSTANCE = new ExceptionUtil();
    }

    /**
     * Constructor
     */
    private ExceptionUtil() {
        // NOP
    }

    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ExceptionUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Prepare the exception message 
     * 
     * @param ex the exception 
     * @return the message 
     */
    public String prepareExceptionWithStacktraceInMessage(Exception ex) {
        return prepareStacktraceInMessage(ex, Version.PACKAGE);
    }

    
    /**
     * Throws a defined exception
     * 
     * @param ex the exception 
     * @param filterPackage the package filter
     * @return the message 
     */
    public String prepareStacktraceInMessage(Exception ex, String filterPackage) {
        if (ex == null) {
            return "";
        }
        
        String msg = ex.getMessage() + "\n" + ex.getClass().getName() + ":";
        StackTraceElement[] occurence = ex.getStackTrace();
        if (occurence != null) {
            for (int i = 0; i < occurence.length; i++) {
                String element = occurence[i].toString();
                if (element.startsWith(filterPackage)) {
                    msg += "\n  at " + element;
                }
            }
        }

        return msg;
    }

    
    /**
     * Throws a defined exception
     * 
     * @param <E> the exception type
     * @param ex the exception 
     * @param exceptionClass the new exception class
     * @return the exception 
     */
    public <E extends Exception> E throwExceptionWithStacktraceInMessage(Exception ex, Class<E> exceptionClass) {
        if (ex == null) {
            return throwsException(exceptionClass, "Invalid exception!");
        }
        
        return throwExceptionWithStacktraceInMessage(ex, exceptionClass, Version.PACKAGE);
    }

    
    /**
     * Throws a defined exception
     * 
     * @param <E> the exception type
     * @param ex the exception 
     * @param exceptionClass the new exception class
     * @param filterPackage the package filter
     * @return the exception 
     */
    public <E extends Exception> E throwExceptionWithStacktraceInMessage(Exception ex, Class<E> exceptionClass, String filterPackage) {
        if (ex == null) {
            return throwsException(exceptionClass, "Invalid exception!");
        }
        
        return throwsException(exceptionClass, prepareStacktraceInMessage(ex, filterPackage), ex.getStackTrace());
    }


    /**
     * Throws an exception with a specific message
     *
     * @param <E> the exception type
     * @param ex the exception 
     * @param exceptionClass the exception class
     * @param withStacktrace true to add the stacktrace
     * @return the exception 
     * @throws IllegalStateException In case of an invalid state
     */
    public <E extends Exception> E throwsException(Exception ex, Class<E> exceptionClass, boolean withStacktrace) {
        if (ex == null) {
            return throwsException(exceptionClass, "Invalid exception!", null);
        }
        
        if (withStacktrace) {
            return throwsException(exceptionClass, ex.getMessage(), ex.getStackTrace());
        } else {
            return throwsException(exceptionClass, ex.getMessage());
        }
    }

    
    /**
     * Throws an exception with a specific message
     *
     * @param <E> the exception type
     * @param exceptionClass the exception class
     * @param message the message
     * @return the exception 
     * @throws IllegalArgumentException In case of an invalid state
     */
    public <E extends Exception> E throwsException(Class<E> exceptionClass, String message) {
        return throwsException(exceptionClass, message, null);
    }
    

    /**
     * Throws an exception with a specific message
     *
     * @param <E> the exception type
     * @param exceptionClass the exception class
     * @param message the message
     * @param stackTraceElement the stacktrace element
     * @return the exception 
     * @throws IllegalArgumentException In case of an invalid state
     */
    public <E extends Exception> E throwsException(Class<E> exceptionClass, String message, StackTraceElement[] stackTraceElement) {
        String msg = "";
        if (message != null) {
            msg = message;
        }
        
        if (exceptionClass == null) {
            throw new IllegalArgumentException("Invalid exception parameter, missing exception type!");
        }

        Constructor<E> constructor = null;
        try {
            constructor = exceptionClass.getDeclaredConstructor(String.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            IllegalArgumentException e = new IllegalArgumentException("Could not found constructor for exception " + exceptionClass + ": " + ex.getMessage() + " for [" + msg + "]");
            ex.setStackTrace(e.getStackTrace());
            throw e;
        }

        E exception = null;
        try {
            exception = constructor.newInstance(msg);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            IllegalArgumentException e = new IllegalArgumentException("Could not instantiate exception " + exceptionClass + ": " + ex.getMessage() + " for [" + msg + "]");
            e.setStackTrace(e.getStackTrace());
            throw e;
        }
        
        if (stackTraceElement != null && stackTraceElement.length > 0) {
            exception.setStackTrace(stackTraceElement);
        }

        return exception;
    }
}
