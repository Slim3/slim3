/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.gen.processor;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.FileObject;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.desc.ModelDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ModelMetaGenerator;
import org.slim3.gen.printer.FilePrinter;
import org.slim3.gen.printer.Printer;

/**
 * Processes JDO model classes which annotated with the {@code
 * javax.jdo.annotations.PersistenceCapable} class.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes(ClassConstants.PersistenceCapable)
@SupportedOptions( { Options.DEBUG })
public class JDOModelProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        long startTime = 0L;
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] Started.", getClass().getName());
            startTime = System.nanoTime();
        }
        for (TypeElement annotation : annotations) {
            for (TypeElement element : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(annotation))) {
                handleTypeElement(element);
            }
        }
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] Ended. elapsed=%d(nano)",
                    getClass().getName(), System.nanoTime() - startTime);
        }
        return true;
    }

    /**
     * Handles a type element represents a JDO model class.
     * 
     * @param element
     *            the element represents a JDO model class.
     */
    protected void handleTypeElement(TypeElement element) {
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] Element(%s) is handling.",
                    getClass().getName(), element.getQualifiedName());
        }
        ModelDescFactory modelDescFactory = createModelDescFactory();
        ModelDesc modelDesc = modelDescFactory.createModelDesc(element);
        if (modelDesc.isTopLevel()) {
            generateModelMeta(modelDesc, element);
        }
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] Element(%s) is handled.",
                    getClass().getName(), element.getQualifiedName());
        }
    }

    /**
     * Generates a model meta java file.
     * 
     * @param modelDesc
     *            the model description.
     * @param model
     *            the element represents a JDO model class.
     */
    protected void generateModelMeta(ModelDesc modelDesc, TypeElement model) {
        Filer filer = processingEnv.getFiler();
        String name = modelDesc.getPackageName() + "."
                + modelDesc.getSimpleName() + Constants.META_SUFFIX;
        Printer printer = null;
        try {
            printer = createPrinter(filer.createSourceFile(name, model));
            Generator generator = createGenerator(modelDesc);
            generator.generate(printer);
        } catch (IOException e) {
            Logger.error(processingEnv, model, "[%s] Failed to generate.",
                    getClass().getName());
            throw new RuntimeException(e);
        } finally {
            if (printer != null) {
                printer.close();
            }
        }
    }

    /**
     * Creates a model description factory.
     * 
     * @return a model description factory
     */
    protected ModelDescFactory createModelDescFactory() {
        return new ModelDescFactory(new JDOModelScanner(processingEnv));
    }

    /**
     * Creates a printer object.
     * 
     * @param file
     *            the file object.
     * @return a printer object.
     * @throws IOException
     *             if an I/O error occurred
     */
    protected Printer createPrinter(FileObject file) throws IOException {
        return new FilePrinter(file);
    }

    /**
     * Creates a generator object.
     * 
     * @param modelDesc
     *            the model description.
     * @return a generator object.
     */
    protected Generator createGenerator(ModelDesc modelDesc) {
        return new ModelMetaGenerator(modelDesc);
    }
}
