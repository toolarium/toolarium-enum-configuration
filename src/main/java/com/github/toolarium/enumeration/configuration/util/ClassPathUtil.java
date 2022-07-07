/*
 * ClassPathUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class path util
 * 
 * @author patrick
 */
public final class ClassPathUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ClassPathUtil.class);
    

    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final ClassPathUtil INSTANCE = new ClassPathUtil();
    }

    
    /**
     * Constructor
     */
    private ClassPathUtil() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ClassPathUtil getInstance() {
        return HOLDER.INSTANCE;
    }


    /**
     * Search a class
     *
     * @param inputClassName the class to search
     * @param ignoreCase true to ignore case
     * @return the found classes
     */
    public List<Class<?>> search(String inputClassName, boolean ignoreCase) {
        List<Class<?>> result = new ArrayList<Class<?>>();
        if (inputClassName == null || inputClassName.isBlank()) {
            return result;
        }
    
        String className = inputClassName.trim();
        int idx = className.lastIndexOf('.');
        if (idx <= 0) {
            return result;
        }
        
        final String packageName = className.substring(0, idx);
        final String classToSearch = className;
                
        /* jptools to resolve
        List<String> list = ClassPath.getInstance().searchClassByPackageName(className.substring(0, idx));
        className = list.stream().filter(string -> classLowecaseName.equalsIgnoreCase(string)).findAny().orElse(null);
        */

        /* guava to solve
        Class<?> enumClazz = ClassPath.from(ClassLoader.getSystemClassLoader()).getAllClasses().stream()
                                                .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(packageName))
                                                .map(clazz -> clazz.load())
                                                .collect(Collectors.toSet()).stream().filter(clazz -> classLowecaseName.equalsIgnoreCase(clazz.getName())).findAny().orElse(null);
                                                */

        for (Class<?> clazz : getClassOfPackage(packageName)) {
            if (ignoreCase) {
                if (classToSearch.equalsIgnoreCase(clazz.getName())) {
                    result.add(clazz);
                }
            } else {
                if (classToSearch.equals(clazz.getName())) {
                    result.add(clazz);
                }
            }
        }

        return result;
    }
    
    
    /**
     * Get classes of a package
     *
     * @param packageName the packagename
     * @return the classes
     * @throws ClassNotFoundException In case of a class can't be found
     * @throws IOException In case of an I/O error
     */
    protected List<Class<?>> getClassOfPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        
        // select all from current thread class loader
        List<String> currentThreadClassLoaderDirs = selectResourcesFromClassLoader(Thread.currentThread().getContextClassLoader(), packageName);
        for (String directory : currentThreadClassLoaderDirs) {
            classes.addAll(findClasses(new File(directory), packageName, false));
        }
        
        // select all from system class loader
        List<String> systemClassLoaderDirs = selectResourcesFromClassLoader(ClassLoader.getSystemClassLoader(), packageName);
        for (String directory : systemClassLoaderDirs) {
            classes.addAll(findClasses(new File(directory), packageName, true));
        }
        
        return classes;
    }


    /**
     * Select all directories
     * 
     * @param classLoader the class loader
     * @param packageName the package name
     * @return the read directories
     */
    private List<String> selectResourcesFromClassLoader(ClassLoader classLoader, String packageName) {
        List<String> dirs = new ArrayList<String>();
        
        try {
            Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(resource.getFile());
            }
        } catch (IOException e) {
            LOG.warn("Could not read path: " + packageName + ": " + e.getMessage());
        }
        
        return dirs;
    }

    
    /**
     * Find classes
     *
     * @param directory the directory
     * @param packageName the package name
     * @param preferSystemClassLoader true to preffer system class load
     * @return the classes
     * @throws ClassNotFoundException In case of a class can't be found
     */
    private static List<Class<?>> findClasses(File directory, String packageName, boolean preferSystemClassLoader) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName(), preferSystemClassLoader));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                loadClass(classes, className, preferSystemClassLoader);
            }
        }
        
        return classes;
    }


    /**
     * Load class
     * 
     * @param classes the classes list
     * @param className the class name
     * @param preferSystemClassLoader true to prefer system class loader
     */
    private static void loadClass(List<Class<?>> classes, String className, boolean preferSystemClassLoader) {
        if (preferSystemClassLoader) {
            try {
                // try by system class loader
                classes.add(ClassLoader.getSystemClassLoader().loadClass(className));
                return;
            } catch (ClassNotFoundException | ExceptionInInitializerError | NoClassDefFoundError e) {
                // NOP
            }
        }

        try {
            classes.add(Class.forName(className));
            return;
        } catch (ClassNotFoundException | ExceptionInInitializerError | NoClassDefFoundError e) {
            // NOP
        }

        try {
            classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
            return;
        } catch (ClassNotFoundException | ExceptionInInitializerError | NoClassDefFoundError e) {
            // NOP
        }
        
        if (!preferSystemClassLoader) {
            try {
                // try by system class loader
                classes.add(ClassLoader.getSystemClassLoader().loadClass(className));
                return;
            } catch (ClassNotFoundException | ExceptionInInitializerError | NoClassDefFoundError e) {
                // NOP
            }
        }
    }
}
