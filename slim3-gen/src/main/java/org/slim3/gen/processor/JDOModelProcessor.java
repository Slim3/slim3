/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.slim3.gen.annotation.Annotations;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.JDOModelMetaGenerator;
import org.slim3.gen.option.Options;
import org.slim3.gen.printer.FileObjectPrinter;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.Logger;

/**
 * Processes JDO model classes which annotated with the {@code
 * javax.jdo.annotations.PersistenceCapable} class.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes(Annotations.PersistenceCapable)
@SupportedOptions( { Options.DEBUG })
public class JDOModelProcessor extends AbstractProcessor {

    /** the suffix for JDO model meta classes. */
    protected static final String suffix = "Meta";

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
        Filer filer = processingEnv.getFiler();
        String name = element.getQualifiedName() + suffix;
        Printer printer = null;
        try {
            printer = createPrinter(filer.createSourceFile(name, element));
            Generator<Printer> generator = createGenerator(element, name);
            generator.generate(printer);
        } catch (IOException e) {
            Logger.error(processingEnv, element, "[%s] Failed to generate.",
                    getClass().getName());
            throw new RuntimeException(e);
        } finally {
            if (printer != null) {
                printer.close();
            }
        }
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] Element(%s) is handled.",
                    getClass().getName(), element.getQualifiedName());
        }
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
        return new FileObjectPrinter(file);
    }

    /**
     * Creates a generator object.
     * 
     * @param element
     *            the element object.
     * @param qualifiedName
     *            qualified name of the class to be generated.
     * @return a generator object.
     */
    protected Generator<Printer> createGenerator(TypeElement element,
            String qualifiedName) {
        return new JDOModelMetaGenerator(processingEnv, element, qualifiedName);
    }
}
