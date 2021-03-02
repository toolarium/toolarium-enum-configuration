/*
 * EnumConfigurationProcessor.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurationContent;
import com.github.toolarium.enumeration.configuration.dto.EnumValueConfigurationContent;
import com.github.toolarium.enumeration.configuration.util.AnnotationConvertUtil;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
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
    private static final Instant MAX_TIMESTAMP = AnnotationConvertUtil.getInstance().parseDate("9999-12-31T12:00:00.000Z");    
    private List<Class<? extends Annotation>> annoationClassList;
    
    
    /**
     * Constructor for EnumConfigurationProcessor
     */
    public EnumConfigurationProcessor() {
        annoationClassList = Arrays.asList(EnumConfiguration.class, EnumValueConfiguration.class);
    }

    
    /**
     * @see javax.annotation.processing.AbstractProcessor#getSupportedAnnotationTypes()
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> result = new HashSet<>();
        for (Class<?> c : annoationClassList) {
            result.add(c.getName());
        }
        
        return result;
    }


    /**
     * @see javax.annotation.processing.AbstractProcessor#getSupportedSourceVersion()
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    
    /**
     * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set, javax.annotation.processing.RoundEnvironment)
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            //processingEnv.getMessager().printMessage(Diagnostic.Kind.OTHER, "Processing is over");
            return false;            
        }

        final Map<String, EnumConfigurationContent> enumConfigurationContentMap = new HashMap<>();
        
        for (Element typeElement : roundEnv.getElementsAnnotatedWith(EnumConfiguration.class)) {
            EnumConfigurationContent result = processEnumConfigurationElement(typeElement);
            if (result != null) {
                //String fullQualifiedName = typeElement.toString();
                enumConfigurationContentMap.put(result.getName(), result);
            }
        }

        for (Element typeElement : roundEnv.getElementsAnnotatedWith(EnumValueConfiguration.class)) {
            //String enumName = "" + typeElement.getSimpleName();
            String fullQualifiedName = "" + typeElement.getEnclosingElement();
            
            EnumConfigurationContent enumConfigurationContent = enumConfigurationContentMap.get(fullQualifiedName);
            EnumValueConfigurationContent enumValueConfigurationContent = processEnumValueConfigurationElement(typeElement);
            if (enumValueConfigurationContent != null) {
                enumConfigurationContent.addEnumValueConfiguration(enumValueConfigurationContent);
            }
        }

        if (!enumConfigurationContentMap.isEmpty()) {
            try {
                //processingEnv.getOptions();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                
                FileObject jsonResource = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/toolarium-enum-configuration.json");
                objectMapper.writeValue(jsonResource.openOutputStream(), enumConfigurationContentMap.values());

                /*
                FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "com", "content.txt");
                try (Writer writer = resource.openWriter()) {
                    
                    for (EnumConfigurationContent enumConfigurationContent : enumConfigurationContentMap.values()) {
                        writer.write(enumConfigurationContent + "\n");
                    }
                }
                */
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }


        return false;
    }



    /**
     * Process the enumeration configuration element annotation.
     * 
     * @param typeElement the element
     * @return the created enumeration configuration
     */
    private EnumConfigurationContent processEnumConfigurationElement(Element typeElement) {
        EnumConfigurationContent enumConfigurationContent = new EnumConfigurationContent();
        enumConfigurationContent.setValidFrom(Instant.now());
        enumConfigurationContent.setValidTill(MAX_TIMESTAMP);

        String fullQualifiedName = typeElement.toString();
        enumConfigurationContent.setName(fullQualifiedName);
        //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found enum configuration element: " + fullQualifiedName);
        
        List<? extends AnnotationMirror> annotationElements = typeElement.getAnnotationMirrors();
        if (annotationElements != null) {
            for (AnnotationMirror a : annotationElements) {
                //String declaredType = "" + a.getAnnotationType();
                
                if (a.getElementValues() != null) {
                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> e : a.getElementValues().entrySet()) {
                        
                        String key = AnnotationConvertUtil.getInstance().getName(e.getKey());
                        String value = AnnotationConvertUtil.getInstance().getValue(e.getValue());
                        
                        switch (key) {
                            case "category": 
                                enumConfigurationContent.setCategory(value);
                                break;
                            case "description": 
                                enumConfigurationContent.setDescription(value);
                                break;
                            case "validFrom": 
                                enumConfigurationContent.setValidFrom(AnnotationConvertUtil.getInstance().parseDate(value));
                                break;
                            case "validTill": 
                                enumConfigurationContent.setValidTill(AnnotationConvertUtil.getInstance().parseDate(value));
                                break;
                            default: 
                                //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found unknwon annotation value: " + e.getKey().getClass().getName() + " / " + e.getKey() + "/" + e.getKey().getSimpleName() + "/" + e.getValue());
                        }
                    }
                }
            }
        }

        //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found enum configuration: " + enumConfigurationContent);
        return enumConfigurationContent;
    }

    
    /**
     * Process the annoation element
     * 
     * @param typeElement the element
     * @return the created enumeration configuration
     */
    private EnumValueConfigurationContent processEnumValueConfigurationElement(Element typeElement) {
        EnumValueConfigurationContent enumValueConfigurationContent = new EnumValueConfigurationContent();
        enumValueConfigurationContent.setValidFrom(Instant.now());
        enumValueConfigurationContent.setValidTill(MAX_TIMESTAMP);

        String fullQualifiedName = typeElement.toString();
        String enumName = "" + typeElement.getSimpleName();
        fullQualifiedName = "" + typeElement.getEnclosingElement();
        fullQualifiedName += "#" + enumName;
        enumValueConfigurationContent.setKey(enumName);
        
        //processingEnv.getMessager().printMessage(Diagnostic.Kind.OTHER, "Found enum configuration element: " + fullQualifiedName);
        
        List<? extends AnnotationMirror> annotationElements = typeElement.getAnnotationMirrors();
        if (annotationElements != null) {
            for (AnnotationMirror a : annotationElements) {
                //String declaredType = "" + a.getAnnotationType();
                
                if (a.getElementValues() != null) {
                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> e : a.getElementValues().entrySet()) {
                        
                        String key = AnnotationConvertUtil.getInstance().getName(e.getKey());
                        String value = AnnotationConvertUtil.getInstance().getValue(e.getValue());
                        
                        switch (key) {
                            case "description": 
                                enumValueConfigurationContent.setDescription(value);
                                break;
                            case "isOptional": 
                                enumValueConfigurationContent.setOptional("true".equalsIgnoreCase(value));
                                break;
                            case "isConfidential": 
                                enumValueConfigurationContent.setConfidential("true".equalsIgnoreCase(value));
                                break;
                            case "defaultValue": 
                                enumValueConfigurationContent.setDefaultValue(value);
                                break;
                            case "validFrom": 
                                enumValueConfigurationContent.setValidFrom(AnnotationConvertUtil.getInstance().parseDate(value));
                                break;
                            case "validTill": 
                                enumValueConfigurationContent.setValidTill(AnnotationConvertUtil.getInstance().parseDate(value));
                                break;
                            default: 
                                //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found unknwon annotation value: " + e.getKey().getClass().getName() + "/" + e.getKey() + "/" + e.getKey().getSimpleName() + "/" + e.getValue());
                        }
                    }
                }
            }
        }

        //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found enum value configuration: " + enumValueConfigurationContent);

        return enumValueConfigurationContent;
    }

    
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/