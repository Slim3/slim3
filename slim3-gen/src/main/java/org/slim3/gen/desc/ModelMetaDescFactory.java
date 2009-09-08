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
package org.slim3.gen.desc;

import org.slim3.gen.processor.Options;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;

/**
 * Creates a model meta description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelMetaDescFactory {

    /** the environment */
    protected final AnnotationProcessorEnvironment env;

    /** the attribute meta description factory */
    protected final AttributeMetaDescFactory attributeMetaDescFactory;

    /**
     * Creates a new {@link ModelMetaDescFactory}.
     * 
     * @param env
     *            the environment
     * @param attributeMetaDescFactory
     *            the attribute meta description factory
     */
    public ModelMetaDescFactory(AnnotationProcessorEnvironment env,
            AttributeMetaDescFactory attributeMetaDescFactory) {
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        if (attributeMetaDescFactory == null) {
            throw new NullPointerException(
                "The attributeMetaDescFactory parameter is null.");
        }
        this.env = env;
        this.attributeMetaDescFactory = attributeMetaDescFactory;
    }

    /**
     * Creates a model meta description.
     * 
     * @param modelDeclaration
     *            the model declaration.
     * @return a model description
     */
    public ModelMetaDesc createModelMetaDesc(ClassDeclaration modelDeclaration) {
        if (modelDeclaration == null) {
            throw new NullPointerException(
                "The classDeclaration parameter is null.");
        }
        String modelClassName = modelDeclaration.getQualifiedName().toString();
        ModelMetaClassName modelMetaClassName =
            createModelMetaClassName(modelClassName);
        ModelMetaDesc modelMetaDesc = new ModelMetaDesc();
        modelMetaDesc.setPackageName(modelMetaClassName.getPackageName());
        modelMetaDesc.setSimpleName(modelMetaClassName.getSimpleName());
        modelMetaDesc.setModelClassName(modelClassName);
        handleAttributes(modelDeclaration, modelMetaDesc);
        return modelMetaDesc;
    }

    /**
     * Creates a model meta class name.
     * 
     * @param modelClassName
     *            a model class name
     * @return a model meta class name
     */
    protected ModelMetaClassName createModelMetaClassName(String modelClassName) {
        return new ModelMetaClassName(modelClassName, Options
            .getModelPackage(env), Options.getMetaPackage(env), Options
            .getSharedPackage(env), Options.getServerPackage(env));
    }

    /**
     * Handles attributes.
     * 
     * @param modelDeclaration
     *            the model declaration.
     * @param modelMetaDesc
     *            the model meta description
     */
    protected void handleAttributes(ClassDeclaration modelDeclaration,
            ModelMetaDesc modelMetaDesc) {
        for (FieldDeclaration attributeDeclaration : modelDeclaration
            .getFields()) {
            handleAttribute(attributeDeclaration, modelMetaDesc);
        }
    }

    /**
     * Handles an attribute.
     * 
     * @param attributeDeclaration
     *            an attribute declaration.
     * @param modelMetaDesc
     *            the model meta description.
     */
    protected void handleAttribute(FieldDeclaration attributeDeclaration,
            ModelMetaDesc modelMetaDesc) {
        AttributeMetaDesc attributeMetaDesc =
            attributeMetaDescFactory
                .createAttributeMetaDesc(attributeDeclaration);
        if (attributeMetaDesc != null) {
            modelMetaDesc.addAttributeMetaDesc(attributeMetaDesc);
        }
    }
}
