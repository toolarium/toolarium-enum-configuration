/*
 * EnumConfigurationProcessor.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.processor;

import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;


/**
 * Implements the enumeration confuguration annotation processor 
 * 
 * @author Patrick Meier
 * @version $Revision:  $
 */
public class EnumConfigurationProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(EnumConfiguration.class.getName(), EnumValueConfiguration.class.getName()));
    }


    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    
    /**
     * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set, javax.annotation.processing.RoundEnvironment)
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Process: " + roundEnv.processingOver());
        if (roundEnv.processingOver()) {
            return false;
        }
        
        final List<String> content = new ArrayList<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(EnumConfiguration.class)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Found annotated element: " + element.toString());
            content.add(element.toString());
        }
        
        for (Element element : roundEnv.getElementsAnnotatedWith(EnumValueConfiguration.class)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Found annotated element: " + element.toString());
            content.add(element.toString());
        }
        
        try {
            FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "com", "content.txt");
            try (Writer writer = resource.openWriter()) {
                for (String line : content) {
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        
        return false;
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/