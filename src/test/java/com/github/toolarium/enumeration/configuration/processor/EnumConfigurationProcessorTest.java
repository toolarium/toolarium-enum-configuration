/* 
 * EnumConfigurationProcessorTest.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.processor;

import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link EnumConfigurationProcessor}.
 * 
 * @author patrick
 */
public class EnumConfigurationProcessorTest {
    
    /*
    private static final JavaFileObject source1 =
            JavaFileObjects.forSourceLines(
                "test.Source1", // format one per line
                "package test;",
                "",
                "class Source1 {}");
     */
    

    /**
     * 
     */
    @Test
    public void compileTest() {
        Compilation compilation = compilerWithGenerator().compile(
                JavaFileObjects.forSourceLines("test.source", // format one per line
                                               "package test;", 
                                               "", 
                                               "class Source1 {}"));
        assertThat(compilation).succeeded();
        
        try {
            ImmutableList<JavaFileObject> result = compilation.generatedFiles();
            assertEquals(1, result.size());
            
            //JavaFileObject javaFileObject = result.get(0);
            //javaFileObject.
            //assertEquals(1, );
        } catch (Exception expected) {
            fail();
        }
    }

    
    
    /**
     * Compile
     * 
     * @return the compiler
     */
    private Compiler compilerWithGenerator() {
        return javac().withProcessors(new EnumConfigurationProcessor());
    }
}
