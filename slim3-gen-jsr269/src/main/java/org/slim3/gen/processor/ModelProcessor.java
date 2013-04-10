/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import static javax.lang.model.util.ElementFilter.*;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import org.slim3.datastore.Model;
import org.slim3.gen.desc.AttributeMetaDescFactory;
import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.desc.ModelMetaDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ModelMetaGenerator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;
import org.slim3.gen.util.FieldDeclarationUtil;
import org.slim3.gen.util.TypeUtil;

/**
 * Represents a {@code ModelProcessor} factory.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.slim3.datastore.Model")
public class ModelProcessor extends AbstractProcessor {

    RoundEnvironment roundEnv;

    /** the support for generating */
    protected GenerateSupport generateSupport;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Logger.init(processingEnv.getMessager());
        FieldDeclarationUtil.init(processingEnv);
        TypeUtil.init(processingEnv);

        Logger.debug("init ModelProcessor");
        this.generateSupport = new GenerateSupport(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {

        this.roundEnv = roundEnv;

        for (TypeElement element : typesIn(roundEnv
            .getElementsAnnotatedWith(Model.class))) {
            try {
                handleClassDeclaration(element);
            } catch (AptException e) {
                e.sendError();
            } catch (RuntimeException e) {
                Logger.error(element, MessageFormatter.getMessage(
                    MessageCode.SLIM3GEN0001,
                    Model.class.getCanonicalName()));
                throw e;
            }
        }
        return true;
    }

    /**
     * Handles a class declaration represents a JDO model class.
     * 
     * @param classElement
     *            the declaration represents a JDO model class.
     */
    protected void handleClassDeclaration(TypeElement classElement) {
        AttributeMetaDescFactory attributeMetaDescFactory =
            createAttributeMetaDescFactory();
        ModelMetaDescFactory modelMetaDescFactory =
            createModelMetaDescFactory(attributeMetaDescFactory);
        ModelMetaDesc modelMetaDesc =
            modelMetaDescFactory.createModelMetaDesc(classElement);
        if (!modelMetaDesc.isError()) {
            Generator modelMetaGenerator =
                createModelMetaGenerator(modelMetaDesc);
            generateSupport.generate(
                modelMetaGenerator,
                modelMetaDesc,
                classElement);
        }
    }

    /**
     * Creates an attribute meta description factory.
     * 
     * @return an attribute meta description factory
     */
    protected AttributeMetaDescFactory createAttributeMetaDescFactory() {
        return new AttributeMetaDescFactory(processingEnv, roundEnv);
    }

    /**
     * Creates a model meta description factory.
     * 
     * @param attributeMetaDescFactory
     *            the attribute meta description factory.
     * @return a model meta description factory
     */
    protected ModelMetaDescFactory createModelMetaDescFactory(
            AttributeMetaDescFactory attributeMetaDescFactory) {
        return new ModelMetaDescFactory(
            processingEnv,
            roundEnv,
            attributeMetaDescFactory);
    }

    /**
     * Creates a model meta generator object.
     * 
     * @param modelMetaDesc
     *            the model meta description.
     * @return a model meta generator object.
     */
    protected ModelMetaGenerator createModelMetaGenerator(
            ModelMetaDesc modelMetaDesc) {
        return new ModelMetaGenerator(modelMetaDesc);
    }
}
