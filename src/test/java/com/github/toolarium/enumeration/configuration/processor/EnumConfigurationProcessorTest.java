/* 
 * EnumConfigurationProcessorTest.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.processor;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.toolarium.enumeration.configuration.util.JavaFileObjectUtil;
import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import java.io.IOException;
import javax.tools.JavaFileObject;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link EnumConfigurationProcessor}.
 * 
 * @author patrick
 */
public class EnumConfigurationProcessorTest {
    
    /**
     * Compile test 
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
        } catch (Exception expected) {
            fail();
        }
    }

    
    /**
     * Test source without any annotations 
     */
    @Test
    public void compileTestUnannotatedSource() {
        assertAbout(javaSources())
        .that(ImmutableList.of(
                forSourceString("HelloWorld", "final class HelloWorld {}")))
        .processedWith(new EnumConfigurationProcessor()).compilesWithoutError();
    }

    
    /**
     * Simple annotation test
     */
    @Test
    public void compileAnnotationTest2() {
        Compilation compilation = compilerWithGenerator().compile(JavaFileObjects.forSourceString("MyEnumConfiguration.java", ""
               + "import com.github.toolarium.enumeration.configuration.IEnumConfiguration;\n"
               + "import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;\n"
               + "import com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration;\n"
               + "import com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration.DataType;\n"
               + ""
               + "@EnumConfiguration(description = \"The system configuration.\")\n"
               + "enum MyEnumConfiguration implements IEnumConfiguration {\n"
               + "@EnumValueConfiguration(description = \"The hostname.\", dataType = DataType.BOOLEAN, defaultValue = \"true\", minValue = \"1\", maxValue = \"10\", exampleValue = \"true\")\n"
               + "HOSTNAME,\n"
               + "@EnumValueConfiguration(description = \"The port.\", isOptional = true, exampleValue = \"8080\")\n"
               + "PORT,\n"
               + "@EnumValueConfiguration(description = \"Hallo.\", isOptional = false, isConfidential = true, exampleValue = \"hello\")\n"
               + "HALLO,\n"
               + "@EnumValueConfiguration(description = \"The port.\", isOptional = true, exampleValue = \"hint\")\n"
               + "HINT;\n"
               + "}"));
        assertThat(compilation).succeeded();
    }

    
    /**
     * Annotation test with source reference
     * 
     * @throws IOException In case source could not be loaded 
     */
    @Test
    public void compileAnnotationTest() throws IOException {
        /*
        JavaFileObject myEnumConfig = JavaFileObjectUtil.getInstance().loadTestSourceObject(MyEnumConfiguration.class);
        assertAbout(javaSources())
        .that(
          ImmutableList.of(myEnumConfig,
                           JavaFileObjectUtil.getInstance().loadTestSourceObject(MyEnumConfiguration.class)))
        .processedWith(new EnumConfigurationProcessor())
        .failsToCompile().withErrorCount(1).withErrorContaining("").in(myEnumConfig).onLine(23).atColumn(5);
        */

        assertAbout(javaSources())
        .that(ImmutableList.of(JavaFileObjectUtil.getInstance().loadTestSourceByClass(IMyEnumConfiguration.class),
                               JavaFileObjectUtil.getInstance().loadTestSourceByClass(MyEnumConfiguration.class)))
        .processedWith(new EnumConfigurationProcessor()).compilesWithoutError();
        

        //assertThat(compilation).hadErrorContaining("No types named HelloWorld!").inFile(helloWorld).onLine(23).atColumn(5);
        //assertThat(compilation).generatedSourceFile("GeneratedHelloWorld").hasSourceEquivalentTo(JavaFileObjects.forResource("GeneratedHelloWorld.java"));
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
