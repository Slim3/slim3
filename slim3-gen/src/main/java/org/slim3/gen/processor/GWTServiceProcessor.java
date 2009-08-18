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

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.desc.GWTServiceAsyncDesc;
import org.slim3.gen.desc.GWTServiceAsyncDescFactory;
import org.slim3.gen.desc.GWTServiceAsyncMethodDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.GWTServiceAsyncGenerator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;

/**
 * Processes GWT service classes which annotated with the {@code
 * com.google.gwt.user.client.rpc.RemoteServiceRelativePat} class.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes(ClassConstants.RemoteServiceRelativePath)
@SupportedOptions( { Options.DEBUG })
public class GWTServiceProcessor extends AbstractProcessor {

    /** the support for generating */
    protected GenerateSupport generateSupport;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        generateSupport = new GenerateSupport(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        for (TypeElement annotation : annotations) {
            for (TypeElement element : ElementFilter.typesIn(roundEnv
                .getElementsAnnotatedWith(annotation))) {
                try {
                    handleTypeElement(element);
                } catch (RuntimeException e) {
                    Logger.error(processingEnv, element, MessageFormatter
                        .getMessage(
                            MessageCode.SILM3GEN0001,
                            ClassConstants.RemoteServiceRelativePath));
                    throw e;
                }
            }
        }
        return true;
    }

    /**
     * Handles a type element represents a GWT service class.
     * 
     * @param element
     *            the element represents a GWT service class.
     */
    protected void handleTypeElement(TypeElement element) {
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, MessageFormatter.getMessage(
                MessageCode.SILM3GEN0002,
                element));
        }
        if (element.getKind().isInterface()) {
            GWTServiceAsyncMethodDescFactory serviceAsyncMethodDescFactory =
                createAsyncMethodDescFactory();
            GWTServiceAsyncDescFactory serviceAsyncDescFactory =
                createServiceAsyncDescFactory(serviceAsyncMethodDescFactory);
            GWTServiceAsyncDesc serviceAsyncDesc =
                serviceAsyncDescFactory.createServiceAsyncDesc(element);
            Generator generator = createGenerator(serviceAsyncDesc);
            generateSupport.generate(generator, serviceAsyncDesc, element);
        } else {
            Logger.error(processingEnv, element, MessageFormatter.getMessage(
                MessageCode.SILM3GEN0011,
                element));
        }
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, MessageFormatter.getMessage(
                MessageCode.SILM3GEN0003,
                element));
        }
    }

    /**
     * Creates a {@link GWTServiceAsyncMethodDescFactory}.
     * 
     * @return a service async method description factory
     */
    protected GWTServiceAsyncMethodDescFactory createAsyncMethodDescFactory() {
        return new GWTServiceAsyncMethodDescFactory(processingEnv);
    }

    /**
     * Creates a {@link GWTServiceAsyncDescFactory}.
     * 
     * @param serviceAsyncMethodDescFactory
     *            the service async method description factory
     * @return a service async description factory
     */
    protected GWTServiceAsyncDescFactory createServiceAsyncDescFactory(
            GWTServiceAsyncMethodDescFactory serviceAsyncMethodDescFactory) {
        return new GWTServiceAsyncDescFactory(
            processingEnv,
            serviceAsyncMethodDescFactory);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param serviceAsyncDesc
     *            the service async description.
     * @return a generator object.
     */
    protected Generator createGenerator(GWTServiceAsyncDesc serviceAsyncDesc) {
        return new GWTServiceAsyncGenerator(serviceAsyncDesc);
    }

}
