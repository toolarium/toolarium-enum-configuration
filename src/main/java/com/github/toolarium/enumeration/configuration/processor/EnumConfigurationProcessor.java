/*
 * EnumConfigurationProcessor.java
 * 
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.processor;

import com.github.toolarium.enumeration.configuration.annotation.EnumConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyConfiguration;
import com.github.toolarium.enumeration.configuration.annotation.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumConfigurations;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import com.github.toolarium.enumeration.configuration.resource.EnumConfigurationResourceFactory;
import com.github.toolarium.enumeration.configuration.util.AnnotationConvertUtil;
import com.github.toolarium.enumeration.configuration.util.ClassPathUtil;
import com.github.toolarium.enumeration.configuration.util.DateUtil;
import com.github.toolarium.enumeration.configuration.util.EnumUtil;
import com.github.toolarium.enumeration.configuration.util.ReflectionUtil;
import com.github.toolarium.enumeration.configuration.validation.EnumConfigurationValidatorFactory;
import com.github.toolarium.enumeration.configuration.validation.IEnumConfigurationStructureValidator;
import com.github.toolarium.enumeration.configuration.validation.ValidationException;
import com.github.toolarium.enumeration.configuration.validation.value.EnumKeyValueConfigurationValueValidatorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;


/**
 * Implements the enumeration configuration annotation processor 
 * 
 * @author patrick
 * 
 * @link https://hauchee.blogspot.com/2015/12/compile-time-annotation-processing-getting-class-value.html
 * @link https://medium.com/swlh/all-about-annotations-and-annotation-processors-4af47159f29d
 * @link https://iammert.medium.com/annotation-processing-dont-repeat-yourself-generate-your-code-8425e60c6657
 * @link https://maxexplode.medium.com/creating-annotation-processor-to-generate-getters-and-setters-7b7964c72b3e
 * @link https://github.com/sockeqwe/annotationprocessing101/blob/master/factory/processor/src/main/java/com/hannesdorfmann/annotationprocessing101/factory/processor/FactoryProcessor.java
 */
@SupportedOptions(value = "enumconfiguration.validate.strict")
public class EnumConfigurationProcessor extends AbstractProcessor {
    public static final String TOOLARIUM_ENUM_CONFIGURATION_INTERFACE_VALIDATORS_FILENAME = "toolarium-enum-configuration-validators.properties";

    /** Defines the input path */
    public static final String TOOLARIUM_ENUM_CONFIGURATION_PATH = "/META-INF/";

    /** Defines the interface validators file */
    public static final String TOOLARIUM_ENUM_CONFIGURATION_INTERFACE_VALIDATORS_FILE = TOOLARIUM_ENUM_CONFIGURATION_PATH + TOOLARIUM_ENUM_CONFIGURATION_INTERFACE_VALIDATORS_FILENAME;

    /** Defines the output filename */
    public static final String TOOLARIUM_ENUM_CONFIGURATION_JSON_FILENAME = "toolarium-enum-configuration.json";

    /** Defines the output filename */
    public static final String TOOLARIUM_ENUM_CONFIGURATION_JSON_PATH = "META-INF/";
    
    /** Defines the output file path */
    public static final String TOOLARIUM_ENUM_CONFIGURATION_JSON_OUTPUT_FILE = TOOLARIUM_ENUM_CONFIGURATION_JSON_PATH + TOOLARIUM_ENUM_CONFIGURATION_JSON_FILENAME;

    
    
    private List<Class<? extends Annotation>> annoationClassList;
    private List<String> warnList;
    
    
    /**
     * Constructor for EnumConfigurationProcessor
     */
    public EnumConfigurationProcessor() {
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
     * @see javax.annotation.processing.AbstractProcessor#init(javax.annotation.processing.ProcessingEnvironment)
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        
        annoationClassList = Arrays.asList(EnumConfiguration.class, EnumKeyConfiguration.class, EnumKeyValueConfiguration.class);
        warnList = new ArrayList<String>();
        
        initializeInterfaceValidators(processingEnv);
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

        if (annotations.size() == 0) {
            return true;
        }

        //String parameter = processingEnv.getOptions().get(VALIDATE_STRICT);
        Map<String, com.github.toolarium.enumeration.configuration.dto.EnumConfiguration<? super com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration>> enumConfigurationContentMap = new LinkedHashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(EnumConfiguration.class)) {
            com.github.toolarium.enumeration.configuration.dto.EnumConfiguration<? super com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration> enumConfiguration = processEnumConfigurationElement((TypeElement)element);
            
            String name = enumConfiguration.getName().trim();
            if (!enumConfigurationContentMap.containsKey(name)) {
                enumConfigurationContentMap.put(name, enumConfiguration);
            }            
        }

        Set<? extends Element> enumKeyValueConfigurationSet = roundEnv.getElementsAnnotatedWith(EnumKeyConfiguration.class);
        if (enumKeyValueConfigurationSet != null && !enumKeyValueConfigurationSet.isEmpty()) {
            for (Element element : roundEnv.getElementsAnnotatedWith(EnumKeyConfiguration.class)) {
                //String enumName = "" + typeElement.getSimpleName();
                String fullQualifiedName = "" + element.getEnclosingElement();
                
                com.github.toolarium.enumeration.configuration.dto.EnumConfiguration<? super com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration> enumConfiguration = enumConfigurationContentMap.get(fullQualifiedName);
                if (enumConfiguration == null) {
                    if (!warnList.contains(fullQualifiedName)) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Missing EnumConfiguration reference on enum in " + fullQualifiedName);
                        warnList.add(fullQualifiedName);
                    }
                } else { 
                    com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration enumKeyConfiguration = processEnumKeyConfigurationElement(enumConfiguration.getInterfaceList(), element);
                    if (enumKeyConfiguration != null) {
                        enumConfiguration.add(enumKeyConfiguration);
                    }
                }
            }
        }

        enumKeyValueConfigurationSet = roundEnv.getElementsAnnotatedWith(EnumKeyValueConfiguration.class);
        if (enumKeyValueConfigurationSet != null && !enumKeyValueConfigurationSet.isEmpty()) {
            for (Element element : roundEnv.getElementsAnnotatedWith(EnumKeyValueConfiguration.class)) {
                //String enumName = "" + typeElement.getSimpleName();
                String fullQualifiedName = "" + element.getEnclosingElement();
                   
                com.github.toolarium.enumeration.configuration.dto.EnumConfiguration<? super com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration> enumConfiguration = enumConfigurationContentMap.get(fullQualifiedName);
                if (enumConfiguration == null) {
                    if (!warnList.contains(fullQualifiedName)) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Missing EnumConfiguration reference on enum in " + fullQualifiedName);
                        warnList.add(fullQualifiedName);
                    }
                } else { 
                    com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration enumKeyValueConfiguration = processEnumKeyValueConfigurationElement(enumConfiguration.getInterfaceList(), element);
                    if (enumKeyValueConfiguration != null) {
                        enumConfiguration.add(enumKeyValueConfiguration);
                    }
                }
            }
        }

        EnumConfigurations enumConfigurations = new EnumConfigurations();
        for (Map.Entry<String, com.github.toolarium.enumeration.configuration.dto.EnumConfiguration<? super com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration>> e : enumConfigurationContentMap.entrySet()) {
            enumConfigurations.add(e.getValue());
        }

        if (enumConfigurations.getEnumConfigurationList() != null && !enumConfigurations.getEnumConfigurationList().isEmpty()) {
            try {
                //processingEnv.getOptions();
                FileObject jsonResource = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", TOOLARIUM_ENUM_CONFIGURATION_JSON_OUTPUT_FILE);
                generateFileContent(TOOLARIUM_ENUM_CONFIGURATION_JSON_OUTPUT_FILE, enumConfigurations, jsonResource.openOutputStream());
                
                /*
                FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "com", "content.txt");
                try (Writer writer = resource.openWriter()) {
                    
                    for (EnumConfiguration enumConfiguration : enumConfigurationMap.values()) {
                        writer.write(enumConfiguration + "\n");
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
     * Generate the file content
     *
     * @param name the name
     * @param enumConfigurations the enum-configuration
     * @param stream the stream
     * @throws IOException In case of a stream exception
     */
    protected void generateFileContent(String name, EnumConfigurations enumConfigurations, OutputStream stream) throws IOException {
        EnumConfigurationResourceFactory.getInstance().store(enumConfigurations, stream);
    }
    
    
    /**
     * Process the enumeration configuration element annotation.
     * 
     * @param <T> The generic type
     * @param typeElement the element
     * @return the created enumeration configuration
     */
    private <T extends com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration> com.github.toolarium.enumeration.configuration.dto.EnumConfiguration<T> processEnumConfigurationElement(TypeElement typeElement) {
        String fullQualifiedName = typeElement.getQualifiedName().toString();
        com.github.toolarium.enumeration.configuration.dto.EnumConfiguration<T> enumConfiguration = new com.github.toolarium.enumeration.configuration.dto.EnumConfiguration<T>(fullQualifiedName);
        //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found enum configuration element: " + fullQualifiedName);
                
        for (TypeMirror tm : typeElement.getInterfaces()) {
            try {
                enumConfiguration.getInterfaceList().add(((DeclaredType)tm).asElement().toString());
            } catch (Exception ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, ex.getMessage());
            }
        }
        
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
                                enumConfiguration.setDescription(value);
                                break;
                            case "tag": 
                                enumConfiguration.setTag(value);
                                break;
                            case "validFrom": 
                                enumConfiguration.setValidFrom(DateUtil.getInstance().parseTimestamp(value));
                                break;
                            case "validTill": 
                                enumConfiguration.setValidTill(DateUtil.getInstance().parseTimestamp(value));
                                break;
                            default: 
                                //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found unknwon annotation value: " + e.getKey().getClass().getName() + " / " + e.getKey() + "/" + e.getKey().getSimpleName() + "/" + e.getValue());
                        }
                    }
                }
            }
        }

        //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found enum configuration: " + enumConfiguration);
        return enumConfiguration;
    }

    
    /**
     * Process the enumeration key annotation element
     * 
     * @param interfaceList the interface list
     * @param element the element
     * @return the created key enumeration configuration
     */
    private com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration processEnumKeyConfigurationElement(Set<String> interfaceList, Element element) {
        com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration enumKeyConfiguration = new com.github.toolarium.enumeration.configuration.dto.EnumKeyConfiguration();

        String enumName = "" + element.getSimpleName();
        String fullQualifiedName = element.toString();
        fullQualifiedName = "" + element.getEnclosingElement();
        fullQualifiedName += "#" + enumName;
        enumKeyConfiguration.setKey(enumName);

        //processingEnv.getMessager().printMessage(Diagnostic.Kind.OTHER, "Found enum configuration element: " + fullQualifiedName);
        
        List<? extends AnnotationMirror> annotationElements = element.getAnnotationMirrors();
        if (annotationElements != null) {
            for (AnnotationMirror a : annotationElements) {
                //String declaredType = "" + a.getAnnotationType();
                
                if (a.getElementValues() != null) {
                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> e : a.getElementValues().entrySet()) {
                        
                        String key = AnnotationConvertUtil.getInstance().getName(e.getKey());
                        String value = AnnotationConvertUtil.getInstance().getValue(e.getValue());
                        
                        switch (key) {
                            case "description":
                                enumKeyConfiguration.setDescription(("" + value).trim());
                                break;
                            case "isConfidential": 
                                enumKeyConfiguration.setConfidential("true".equalsIgnoreCase(("" + value).trim()));
                                break;
                            case "validFrom": 
                                enumKeyConfiguration.setValidFrom(DateUtil.getInstance().parseTimestamp(value));
                                break;
                            case "validTill": 
                                enumKeyConfiguration.setValidTill(DateUtil.getInstance().parseTimestamp(value));
                                break;
                            default: 
                                //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found unknwon annotation value: " + e.getKey().getClass().getName() + "/" + e.getKey() + "/" + e.getKey().getSimpleName() + "/" + e.getValue());
                        }
                    }
                }
            }
        }

        try {
            // validate enum-configuration
            EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyConfiguration);
            
            // validate additional custom interfaces
            if ((interfaceList != null && !interfaceList.isEmpty())) {
                for (String interfaceName : interfaceList) {
                    final IEnumConfigurationStructureValidator validator = EnumConfigurationValidatorFactory.getInstance().getInterfaceStructureValidator(interfaceName);
                    if (validator != null) {
                        try {
                            validator.validate(enumKeyConfiguration);
                        } catch (ValidationException ex) {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Please check the annotation of " + fullQualifiedName + ": \n" + ex.getMessage());
                        }
                    }
                }
            }
        } catch (ValidationException ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Please check the annotation of " + fullQualifiedName + ": \n" + ex.getMessage());
        }
        
        //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found enum value configuration: " + enumKeyValueConfiguration);
        return enumKeyConfiguration;
    }

    
    /**
     * Process the enumeration key / value annotation element
     * 
     * @param interfaceList the interface list
     * @param element the element
     * @return the created enumeration key value configuration
     */
    private com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration processEnumKeyValueConfigurationElement(Set<String> interfaceList, Element element) {
        com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration enumKeyValueConfiguration = new com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.STRING);

        String enumName = "" + element.getSimpleName();
        String fullQualifiedName = element.toString();
        fullQualifiedName = "" + element.getEnclosingElement();
        fullQualifiedName += "#" + enumName;
        enumKeyValueConfiguration.setKey(enumName);
        String minValueSize = null;
        String maxValueSize = null;

        //processingEnv.getMessager().printMessage(Diagnostic.Kind.OTHER, "Found enum configuration element: " + fullQualifiedName);
        
        List<? extends AnnotationMirror> annotationElements = element.getAnnotationMirrors();
        if (annotationElements != null) {
            for (AnnotationMirror a : annotationElements) {
                //String declaredType = "" + a.getAnnotationType();
                
                if (a.getElementValues() != null) {
                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> e : a.getElementValues().entrySet()) {
                        
                        String key = AnnotationConvertUtil.getInstance().getName(e.getKey());
                        String value = AnnotationConvertUtil.getInstance().getValue(e.getValue());
                        
                        switch (key) {
                            case "description":
                                enumKeyValueConfiguration.setDescription(("" + value).trim());
                                break;
                                
                            case "dataType": 
                                EnumKeyValueConfigurationDataType type = EnumUtil.getInstance().valueOf(EnumKeyValueConfigurationDataType.class, value);
                                if (type == null) {
                                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Invalid data type [" +  e.getValue() + "]! Please check the annoataion of " + fullQualifiedName + ".");
                                }
                                
                                enumKeyValueConfiguration.setDataType(type);
                                break;
                                
                            case "defaultValue": 
                                enumKeyValueConfiguration.setDefaultValue(value);
                                break;
                                
                            case "minValue": 
                                minValueSize = value;
                                break;
                                
                            case "maxValue": 
                                maxValueSize = value;
                                break;
                                
                            case "exampleValue": 
                                enumKeyValueConfiguration.setExampleValue(value);
                                break;
                                
                            case "enumerationValue": 
                                enumKeyValueConfiguration.setEnumerationValue(value);
                                break;
                                
                            case "cardinality": 
                                try {
                                    enumKeyValueConfiguration.setCardinality(AnnotationConvertUtil.getInstance().parseCardinality(value));
                                } catch (IllegalArgumentException ex) {
                                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "" + ex.getMessage() + " Please check the annotation of " + fullQualifiedName + ".");
                                }
                                break;
                                
                            case "isUniqueness": 
                                enumKeyValueConfiguration.setUniqueness("true".equalsIgnoreCase(("" + value).trim()));
                                break;
                                
                            case "isConfidential": 
                                enumKeyValueConfiguration.setConfidential("true".equalsIgnoreCase(("" + value).trim()));
                                break;
                                
                            case "validFrom": 
                                enumKeyValueConfiguration.setValidFrom(DateUtil.getInstance().parseTimestamp(value));
                                break;
                                
                            case "validTill": 
                                enumKeyValueConfiguration.setValidTill(DateUtil.getInstance().parseTimestamp(value));
                                break;
                                
                            default: 
                                //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found unknwon annotation value: " + e.getKey().getClass().getName() + "/" + e.getKey() + "/" + e.getKey().getSimpleName() + "/" + e.getValue());
                        }
                    }
                }
            }
        }

        try {
            enumKeyValueConfiguration.setValueSize(EnumKeyValueConfigurationValueValidatorFactory.getInstance().createEnumKeyValueConfigurationSizing(enumKeyValueConfiguration.getDataType(), minValueSize, maxValueSize));
        } catch (Exception ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Please check the annotation of " + fullQualifiedName + ": \n" + "" + ex.getMessage());
        }

        try {
            // validate enum-configuration
            EnumConfigurationValidatorFactory.getInstance().getStructureValidator().validate(enumKeyValueConfiguration);

            // validate additional custom interfaces
            if ((interfaceList != null && !interfaceList.isEmpty())) {
                for (String interfaceName : interfaceList) {
                    //processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, ">>> Validate: " + fullQualifiedName + "/" + interfaceName);
                    
                    final IEnumConfigurationStructureValidator validator = EnumConfigurationValidatorFactory.getInstance().getInterfaceStructureValidator(interfaceName);
                    if (validator != null) {
                        try { 
                            validator.validate(enumKeyValueConfiguration);
                        } catch (ValidationException ex) {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Please check the annotation of " + fullQualifiedName + ": \n" + ex.getMessage());
                        }
                    }
                }
            }
        } catch (ValidationException ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Please check the annotation of " + fullQualifiedName + ": \n" + ex.getMessage());
        }
            
        
        //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found enum value configuration: " + enumKeyValueConfiguration);
        return enumKeyValueConfiguration;
    }


    /**
     * Get the input stream for resource
     *
     * @param resource the resouce
     * @return the input stream
    private InputStream getInputStreamForResource(String resource) {
        
        String pkg = "";
        String name = resource;
        if (resource.contains("/")) {
            int idx = resource.lastIndexOf("/");
            pkg = resource.substring(0, idx);
            name = resource.substring(idx + 1);
        }
        
        InputStream ormStream;
        try {
            FileObject fileObject = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, pkg, name);
            ormStream = fileObject.openInputStream();
        } catch (IOException e1) {
            // TODO - METAGEN-12
            // unfortunately, the Filer.getResource API seems not to be able to load from /META-INF. One gets a
            // FilerException with the message with "Illegal name /META-INF". This means that we have to revert to
            // using the classpath. This might mean that we find a persistence.xml which is 'part of another jar.
            // Not sure what else we can do here
            ormStream = this.getClass().getResourceAsStream(resource);
        }
        
        return ormStream;
    }            
     */


    /**
     *Initialize the interface validators
     *
     * @param processingEnv environment to access facilities the tool framework provides to the processor
     */
    private void initializeInterfaceValidators(ProcessingEnvironment processingEnv) {
        //processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Check found configuration:");
        try (InputStream inputStream = EnumConfigurationProcessor.class.getResourceAsStream(TOOLARIUM_ENUM_CONFIGURATION_INTERFACE_VALIDATORS_FILE)) {
            if (inputStream != null) {
                Properties prop = new Properties();
                prop.load(inputStream);
                //processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Found configuration:" + prop);
                prop.entrySet().forEach(entry -> {
                    if (entry.getValue() != null && !entry.getValue().toString().trim().isEmpty()) {
                        final List<Class<?>> classes = ClassPathUtil.getInstance().search("" + entry.getValue().toString().trim(), true);
                        if (classes != null && !classes.isEmpty()) {
                            for (Class<?> clazz : classes) {
                                try {
                                    //processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Found interface:" + entry.getValue().toString().trim());
                                    final IEnumConfigurationStructureValidator validator = (IEnumConfigurationStructureValidator)ReflectionUtil.getInstance().newInstance(clazz);
                                    EnumConfigurationValidatorFactory.getInstance().setInterfaceStructureValidator(entry.getKey().toString().trim(), validator);
                                } catch (Exception e) {
                                    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Invalid configuration of " + entry.getKey() + " in configuration file "
                                                                                                        + TOOLARIUM_ENUM_CONFIGURATION_INTERFACE_VALIDATORS_FILE + ". The class " + entry.getValue() + " can't be initialized: " + e.getMessage());
                                }
                            }
                        } else {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Invalid configuration of " + entry.getKey() + " in configuration file "
                                                                                              + TOOLARIUM_ENUM_CONFIGURATION_INTERFACE_VALIDATORS_FILE + ". The class " + entry.getValue() + " can't be found in class path!");
                        }
                    }
                });
            }
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Could not load the EnumConfiguration configuration file " + TOOLARIUM_ENUM_CONFIGURATION_INTERFACE_VALIDATORS_FILE + ": " + e.getMessage());
        }
    }
}


/****************************************************************************/
/* EOF                                                                      */
/****************************************************************************/