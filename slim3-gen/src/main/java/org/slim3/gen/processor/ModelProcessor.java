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
import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.desc.ModelMetaDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ModelMetaGenerator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;
import org.slim3.gen.printer.FilePrinter;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.StringUtil;

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
public class ModelProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (TypeElement element : ElementFilter.typesIn(roundEnv
                .getElementsAnnotatedWith(annotation))) {
                try {
                    handleTypeElement(element);
                } catch (RuntimeException e) {
                    Logger.error(processingEnv, element, MessageFormatter
                        .getMessage(MessageCode.SILM3GEN0001));
                    throw e;
                }
            }
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
            Logger.debug(processingEnv, MessageFormatter.getMessage(
                MessageCode.SILM3GEN0002,
                element));
        }
        ModelMetaDescFactory modelMetaDescFactory =
            createModelMetaDescFactory();
        ModelMetaDesc modelMetaDesc =
            modelMetaDescFactory.createModelMetaDesc(element);
        if (modelMetaDesc.isTopLevel()) {
            generateModelMeta(modelMetaDesc, element);
        }
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, MessageFormatter.getMessage(
                MessageCode.SILM3GEN0003,
                element));
        }
    }

    /**
     * Generates a model meta java file.
     * 
     * @param modelMetaDesc
     *            the model meta description.
     * @param model
     *            the element represents a JDO model class.
     */
    protected void generateModelMeta(ModelMetaDesc modelMetaDesc,
            TypeElement model) {
        Filer filer = processingEnv.getFiler();
        String name = null;
        if (StringUtil.isEmpty(modelMetaDesc.getPackageName())) {
            name = modelMetaDesc.getSimpleName();
        } else {
            name =
                modelMetaDesc.getPackageName()
                    + "."
                    + modelMetaDesc.getSimpleName();
        }
        Printer printer = null;
        try {
            printer = createPrinter(filer.createSourceFile(name, model));
            Generator generator = createGenerator(modelMetaDesc);
            generator.generate(printer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (printer != null) {
                printer.close();
            }
        }
    }

    /**
     * Creates a model meta description factory.
     * 
     * @return a model meta description factory
     */
    protected ModelMetaDescFactory createModelMetaDescFactory() {
        return new ModelMetaDescFactory(new ModelScanner(processingEnv));
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
     * @param modelMetaDesc
     *            the model description.
     * @return a generator object.
     */
    protected Generator createGenerator(ModelMetaDesc modelMetaDesc) {
        return new ModelMetaGenerator(modelMetaDesc);
    }
}
