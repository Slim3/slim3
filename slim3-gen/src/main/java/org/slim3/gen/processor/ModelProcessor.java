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

import java.util.Set;

import org.slim3.gen.desc.AttributeMetaDescFactory;
import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.desc.ModelMetaDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ModelMetaGenerator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.util.DeclarationFilter;

/**
 * Processes JDO model classes which annotated with the {@code
 * javax.jdo.annotations.PersistenceCapable} class.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
@SuppressWarnings("deprecation")
public class ModelProcessor implements AnnotationProcessor {

    /** the set of annotation type declaration */
    protected final Set<AnnotationTypeDeclaration> annotationTypeDeclarations;

    /** the environment */
    protected final AnnotationProcessorEnvironment env;

    /** the support for generating */
    protected final GenerateSupport generateSupport;

    /**
     * Creates a new {@link ModelProcessor}.
     * 
     * @param annotationTypeDeclarations
     *            the set of annotation type declaration
     * @param env
     *            the environment
     */
    public ModelProcessor(
            Set<AnnotationTypeDeclaration> annotationTypeDeclarations,
            AnnotationProcessorEnvironment env) {
        if (annotationTypeDeclarations == null) {
            throw new NullPointerException(
                "The annotationTypeDeclarations parameter is null.");
        }
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        this.annotationTypeDeclarations = annotationTypeDeclarations;
        this.env = env;
        this.generateSupport = new GenerateSupport(env);
    }

    public void process() {
        for (AnnotationTypeDeclaration annotation : annotationTypeDeclarations) {
            for (ClassDeclaration element : DeclarationFilter.getFilter(
                ClassDeclaration.class).filter(
                env.getDeclarationsAnnotatedWith(annotation),
                ClassDeclaration.class)) {
                try {
                    handleClassDeclaration(element);
                } catch (AptException e) {
                    e.sendError();
                } catch (RuntimeException e) {
                    Logger.error(env, element.getPosition(), MessageFormatter
                        .getMessage(MessageCode.SLIM3GEN0001, annotation
                            .getQualifiedName()));
                    throw e;
                }
            }
        }
    }

    /**
     * Handles a class declaration represents a JDO model class.
     * 
     * @param declaration
     *            the declaration represents a JDO model class.
     */
    protected void handleClassDeclaration(ClassDeclaration declaration) {
        AttributeMetaDescFactory attributeMetaDescFactory =
            createAttributeMetaDescFactory();
        ModelMetaDescFactory modelMetaDescFactory =
            createModelMetaDescFactory(attributeMetaDescFactory);
        ModelMetaDesc modelMetaDesc =
            modelMetaDescFactory.createModelMetaDesc(declaration);
        if (!modelMetaDesc.isError()) {
            Generator modelMetaGenerator =
                createModelMetaGenerator(modelMetaDesc);
            generateSupport.generate(modelMetaGenerator, modelMetaDesc);
        }
    }

    /**
     * Creates an attribute meta description factory.
     * 
     * @return an attribute meta description factory
     */
    protected AttributeMetaDescFactory createAttributeMetaDescFactory() {
        return new AttributeMetaDescFactory(env);
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
        return new ModelMetaDescFactory(env, attributeMetaDescFactory);
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
