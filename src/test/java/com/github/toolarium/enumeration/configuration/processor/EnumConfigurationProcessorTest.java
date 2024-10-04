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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import com.github.toolarium.enumeration.configuration.resource.EnumConfigurationResourceFactory;
import com.github.toolarium.enumeration.configuration.util.JavaFileObjectUtil;
import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import java.io.ByteArrayInputStream;
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
               + "import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration;\n"
               + "import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration.DataType;\n"
               + ""
               + "@EnumConfiguration(description = \"The system configuration.\")\n"
               + "enum MyEnumConfiguration implements IEnumConfiguration {\n"
               + "@EnumKeyValueConfiguration(description = \"The hostname\", dataType = DataType.STRING, defaultValue = \"true\", minValue = \"1\", maxValue = \"10\", exampleValue = \"true\")\n"
               + "HOSTNAME,\n"
               + "@EnumKeyValueConfiguration(description = \"The port\", exampleValue = \"8080\")\n"
               + "PORT,\n"
               + "@EnumKeyValueConfiguration(description = \"The hint\", exampleValue = \"hint\")\n"
               + "HINT;\n"
               + "}"));
        assertThat(compilation).succeeded();
    }

    
    /**
     * Test invalid cardinality
     */
    @Test
    public void invalidCardinalityTest() {
        Compilation compilation = compilerWithGenerator().compile(JavaFileObjects.forSourceString("MyEnumConfiguration.java", ""
               + "import com.github.toolarium.enumeration.configuration.IEnumConfiguration;\n"
               + "import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;\n"
               + "import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration;\n"
               + "import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration.DataType;\n"
               + ""
               + "@EnumConfiguration(description = \"The system configuration.\")\n"
               + "enum MyEnumConfiguration implements IEnumConfiguration {\n"
               + "    @EnumKeyValueConfiguration(description = \"My value F.\", dataType = DataType.STRING, cardinality = \"2..3\", exampleValue = \"[\\\"A\\\" ]\")\n"
               + "    VALUE_F;\n"
               + "}"));
        assertThat(compilation).hadErrorContaining("Invalid cardinality of [exampleValue], the min cardinality is [2]");
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
     * Annotation test with source reference
     * 
     * @throws IOException In case source could not be loaded 
     */
    @Test
    public void compileAnnotationTestWithTag() throws IOException {
        
        /*
        assertAbout(javaSources())
        .that(ImmutableList.of(JavaFileObjectUtil.getInstance().loadTestSourceByClass(IMyEnumConfiguration.class),
                               JavaFileObjectUtil.getInstance().loadTestSourceByClass(MyEnumWithTagConfiguration.class)))
        .processedWith(new EnumConfigurationProcessor()).compilesWithoutError();
        */

        TestEnumConfigurationProcessor testEnumConfigurationProcessor = new TestEnumConfigurationProcessor();
        assertAbout(javaSources())
        .that(ImmutableList.of(JavaFileObjectUtil.getInstance().loadTestSourceByClass(IMyEnumConfiguration.class),
                               JavaFileObjectUtil.getInstance().loadTestSourceByClass(MyEnumWithTagConfiguration.class)))
        .processedWith(testEnumConfigurationProcessor).compilesWithoutError();

        String content = testEnumConfigurationProcessor.getGeneratedEnumConfiguration();
        assertNotNull(content);
        EnumConfigurations enumConfigurations = EnumConfigurationResourceFactory.getInstance().load(new ByteArrayInputStream(content.getBytes()));
        assertNotNull(enumConfigurations);
        
        assertEquals("myTag", enumConfigurations.getEnumConfigurationList().iterator().next().getTag());
        
        
        testEnumConfigurationProcessor = new TestEnumConfigurationProcessor();
        assertAbout(javaSources())
        .that(ImmutableList.of(JavaFileObjectUtil.getInstance().loadTestSourceByClass(IMyEnumConfiguration.class),
                               JavaFileObjectUtil.getInstance().loadTestSourceByClass(MyEnumConfiguration.class)))
        .processedWith(testEnumConfigurationProcessor).compilesWithoutError();

        content = testEnumConfigurationProcessor.getGeneratedEnumConfiguration();
        assertNotNull(content);
        enumConfigurations = EnumConfigurationResourceFactory.getInstance().load(new ByteArrayInputStream(content.getBytes()));
        assertNotNull(enumConfigurations);
        
        assertNull(enumConfigurations.getEnumConfigurationList().iterator().next().getTag());
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
