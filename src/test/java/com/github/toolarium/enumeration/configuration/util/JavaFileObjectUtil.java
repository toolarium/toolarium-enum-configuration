/*
 * JavaFileObjectUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.google.testing.compile.JavaFileObjects;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;


/**
 * Defines the utility class to load source.
 * 
 * @author patrick
 */
public final class JavaFileObjectUtil {

    private static final String SLASH = "/";


    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     */
    private static class HOLDER {
        static final JavaFileObjectUtil INSTANCE = new JavaFileObjectUtil();
    }

    /**
     * Constructor
     */
    private JavaFileObjectUtil() {
        // NOP
    }

    /**
     * Get the instance
     *
     * @return the instance
     */
    public static JavaFileObjectUtil getInstance() {
        return HOLDER.INSTANCE;
    }
    
    
    /**
     * Load source
     *
     * @param <T> The generic class type
     * @param clazz the class in the test source
     * @return the {@link JavaFileObject} 
     * @throws IOException In case of loading issue
     */
    public <T> ClassJavaFileObject<T> loadMainSourceByClass(Class<T> clazz) throws IOException {
        //return JavaFileObjects.forSourceString(clazz.getName().replace(".", SLASH), loadSourceByClass(clazz, "main"));
        return new ClassJavaFileObject<T>(clazz, getSourceSubPath("main"), loadSourceByClass(clazz, "main"));    
    }

    
    /**
     * Load test source
     *
     * @param <T> The generic class type
     * @param clazz the class in the test source
     * @return the {@link JavaFileObject} 
     * @throws IOException In case of loading issue
     */
    public <T> JavaFileObject loadTestSourceByClass(Class<T> clazz) throws IOException {
        //return JavaFileObjects.forSourceString(clazz.getName().replace(".", SLASH), loadSourceByClass(clazz, "test"));
        return new ClassJavaFileObject<T>(clazz, getSourceSubPath("test"), loadSourceByClass(clazz, "test"));    
    }

    
    /**
     * Load source
     *
     * @param clazz the class to load the source
     * @param pathType the path type e.g. test or main
     * @return the content
     * @throws IOException In case the file could not be read
     */
    public String loadSourceByClass(Class<?> clazz, String pathType) throws IOException {
        URL resource = getProjectResource(clazz, pathType);
        if (resource == null) {
            return null;
        }
        
        try (InputStream in = resource.openStream()) {
            byte[] bytes = in.readAllBytes();
            return new String(bytes);
        }
    }


    /**
     * Get project resource URL
     *
     * @param clazz the class
     * @param pathType the path type e.g. test or main
     * @return the URL or null
     */
    protected URL getProjectResource(Class<?> clazz, String pathType) {
        Path projectDir = getProjectDir();
        if (projectDir == null) {
            return null;
        }

        String srcPath = getSourceSubPath(pathType);
        String loadPath = projectDir.toString().replace('\\', '/');
        while (!loadPath.trim().isEmpty() && !new File(loadPath + SLASH + srcPath).exists()) {
            loadPath = cutPath(loadPath);
        }

        try {
            return Paths.get(loadPath).resolve(srcPath).resolve(clazz.getName().replace(".", "/") + ".java").toUri().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    
    /**
     * Get source sub path
     *
     * @param pathType the path type e.g. test or main
     * @return the source sub path
     */
    protected String getSourceSubPath(String pathType) {
        String srcPath = "src" + SLASH;
        if (pathType != null && !pathType.trim().isEmpty()) {
            srcPath += pathType.trim();

            if (!srcPath.endsWith(SLASH)) {
                srcPath += SLASH;
            }
        }
        srcPath += "java";
        return srcPath;
    }

    
    /**
     * Get the classes directory
     *
     * @return the classes directory
     */
    protected URL getClassesDir() {
        return getClass().getProtectionDomain().getCodeSource().getLocation();
    }
    
    
    /**
     * Get the project directory
     *
     * @return the project directory
     */
    protected Path getProjectDir() {
        try {
            return Paths.get(getClassesDir().toURI()).getParent().getParent();
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
    
    /**
     * Cut a path
     *
     * @param inputPath the input path
     * @return the shorted path
     */
    private String cutPath(String inputPath) {
        String path = inputPath;
        while (!path.isEmpty() && path.endsWith(SLASH)) {
            path = path.substring(0, path.length() - 1);
        }
        
        int idx = path.lastIndexOf('/');
        if (idx > 0) {
            path = path.substring(0, idx);
        }
        
        return path;
    }
    
    
    
    /**
     * The class java file object reference
     * 
     * @author patrick
     */
    class ClassJavaFileObject<T> implements JavaFileObject {
        private Class<T> clazz;
        private String sourceSubPath;
        private String fileName;
        private JavaFileObject object;
        
        /**
         * Constructor for SimpleJavaFileObject
         * 
         * @param clazz the class
         * @param sourceSubPath the source sub path
         * @param content the content
         */
        ClassJavaFileObject(Class<T> clazz, String sourceSubPath, String content) {
            this.clazz = clazz;
            this.sourceSubPath = sourceSubPath;
            this.fileName = clazz.getName().replace(".", SLASH);
            this.object = JavaFileObjects.forSourceString(fileName, content);
        }

        
        /**
         * Get the java file object class
         *
         * @return the java file object class
         */
        public Class<T> getJavaFileObjectClass() {
            return clazz;
        }
        
        
        /**
         * The source sub path 
         *
         * @return the source sub path
         */
        public String getSourceSubPath() {
            return sourceSubPath;
        }
        
        
        /**
         * @see javax.tools.FileObject#toUri()
         */
        @Override
        public URI toUri() {
            return object.toUri();
        }

        
        /**
         * @see javax.tools.FileObject#getName()
         */
        @Override
        public String getName() {
            return fileName;
        }

        
        /**
         * @see javax.tools.FileObject#openInputStream()
         */
        @Override
        public InputStream openInputStream() throws IOException {
            return object.openInputStream();
        }

        
        /**
         * @see javax.tools.FileObject#openOutputStream()
         */
        @Override
        public OutputStream openOutputStream() throws IOException {
            return object.openOutputStream();
        }

        
        /**
         * @see javax.tools.FileObject#openReader(boolean)
         */
        @Override
        public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
            return object.openReader(ignoreEncodingErrors);
        }

        
        /**
         * @see javax.tools.FileObject#getCharContent(boolean)
         */
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return object.getCharContent(ignoreEncodingErrors);
        }

        
        /**
         * @see javax.tools.FileObject#openWriter()
         */
        @Override
        public Writer openWriter() throws IOException {
            return object.openWriter();
        }

        
        /**
         * @see javax.tools.FileObject#getLastModified()
         */
        @Override
        public long getLastModified() {
            return object.getLastModified();
        }

        
        /**
         * @see javax.tools.FileObject#delete()
         */
        @Override
        public boolean delete() {
            return object.delete();
        }

        
        /**
         * @see javax.tools.JavaFileObject#getKind()
         */
        @Override
        public Kind getKind() {
            return object.getKind();
        }

        
        /**
         * @see javax.tools.JavaFileObject#isNameCompatible(java.lang.String, javax.tools.JavaFileObject.Kind)
         */
        @Override
        public boolean isNameCompatible(String simpleName, Kind kind) {
            return object.isNameCompatible(simpleName, kind);
        }

        
        /**
         * @see javax.tools.JavaFileObject#getNestingKind()
         */
        @Override
        public NestingKind getNestingKind() {
            return object.getNestingKind();
        }

        
        /**
         * @see javax.tools.JavaFileObject#getAccessLevel()
         */
        @Override
        public Modifier getAccessLevel() {
            return object.getAccessLevel();
        }

        
        @Override
        public String toString() {
            return super.toString();
            //return ("" + sourceSubPath + SLASH + fileName).replace('/', File.separatorChar);
        }
    }
}