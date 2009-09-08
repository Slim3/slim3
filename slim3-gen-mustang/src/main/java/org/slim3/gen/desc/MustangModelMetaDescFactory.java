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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import org.slim3.gen.processor.MustangOptions;

/**
 * Creates a model meta description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class MustangModelMetaDescFactory {

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /** the attribute meta description factory */
    protected final MustangAttributeMetaDescFactory attributeMetaDescFactory;

    /**
     * Creates a new {@link MustangModelMetaDescFactory}.
     * 
     * @param processingEnv
     *            the processing environment
     * @param attributeMetaDescFactory
     *            the attribute meta description factory
     */
    public MustangModelMetaDescFactory(ProcessingEnvironment processingEnv,
            MustangAttributeMetaDescFactory attributeMetaDescFactory) {
        if (processingEnv == null) {
            throw new NullPointerException(
                "The processingEnv parameter is null.");
        }
        if (attributeMetaDescFactory == null) {
            throw new NullPointerException(
                "The attributeMetaDescFactory parameter is null.");
        }
        this.processingEnv = processingEnv;
        this.attributeMetaDescFactory = attributeMetaDescFactory;
    }

    /**
     * Creates a model meta description.
     * 
     * @param modelElement
     *            the model element.
     * @return a model description
     */
    public ModelMetaDesc createModelMetaDesc(TypeElement modelElement) {
        if (modelElement == null) {
            throw new NullPointerException(
                "The modelElement parameter is null.");
        }
        String modelClassName = modelElement.getQualifiedName().toString();
        ModelMetaClassName modelMetaClassName =
            createModelMetaClassName(modelClassName);
        ModelMetaDesc modelMetaDesc = new ModelMetaDesc();
        modelMetaDesc.setPackageName(modelMetaClassName.getPackageName());
        modelMetaDesc.setSimpleName(modelMetaClassName.getSimpleName());
        modelMetaDesc.setModelClassName(modelClassName);
        handleAttributes(modelElement, modelMetaDesc);
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
        return new ModelMetaClassName(modelClassName, MustangOptions
            .getModelPackage(processingEnv), MustangOptions
            .getMetaPackage(processingEnv), MustangOptions
            .getSharedPackage(processingEnv), MustangOptions
            .getServerPackage(processingEnv));
    }

    /**
     * Handles attributes.
     * 
     * @param modelElement
     *            the model element.
     * @param modelMetaDesc
     *            the model meta description
     */
    protected void handleAttributes(TypeElement modelElement,
            ModelMetaDesc modelMetaDesc) {
        for (VariableElement attributeElement : ElementFilter
            .fieldsIn(modelElement.getEnclosedElements())) {
            handleAttribute(attributeElement, modelMetaDesc);
        }
    }

    /**
     * Handles an attribute.
     * 
     * @param attributeElement
     *            an attribute element.
     * @param modelMetaDesc
     *            the model meta description.
     */
    protected void handleAttribute(VariableElement attributeElement,
            ModelMetaDesc modelMetaDesc) {
        AttributeMetaDesc attributeMetaDesc =
            attributeMetaDescFactory.createAttributeMetaDesc(attributeElement);
        if (attributeMetaDesc != null) {
            modelMetaDesc.addAttributeMetaDesc(attributeMetaDesc);
        }
    }

}
