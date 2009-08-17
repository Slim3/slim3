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

import org.slim3.gen.Constants;
import org.slim3.gen.util.ClassUtil;

/**
 * Creates a model meta description factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelMetaDescFactory {

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /** the attribute meta description factory */
    protected final AttributeMetaDescFactory attributeMetaDescFactory;

    /**
     * Creates a new {@link ModelMetaDescFactory}.
     * 
     * @param processingEnv
     *            the processing environment
     * @param attributeMetaDescFactory
     *            the attribute meta description factory
     */
    public ModelMetaDescFactory(ProcessingEnvironment processingEnv,
            AttributeMetaDescFactory attributeMetaDescFactory) {
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
        ModelMetaDesc modelMetaDesc = new ModelMetaDesc();
        modelMetaDesc.setPackageName(ClassUtil.getPackageName(modelElement
            .getQualifiedName()
            .toString()));
        modelMetaDesc.setSimpleName(modelElement.getSimpleName()
            + Constants.META_SUFFIX);
        modelMetaDesc.setModelClassName(modelElement
            .getQualifiedName()
            .toString());
        handleFields(modelElement, modelMetaDesc);
        return modelMetaDesc;
    }

    /**
     * Handles fields.
     * 
     * @param modelElement
     *            the model element.
     * @param modelMetaDesc
     *            the model meta description
     */
    protected void handleFields(TypeElement modelElement,
            ModelMetaDesc modelMetaDesc) {
        for (VariableElement fieldElement : ElementFilter.fieldsIn(modelElement
            .getEnclosedElements())) {
            handleField(fieldElement, modelMetaDesc);
        }
    }

    /**
     * Handles the field.
     * 
     * @param fieldElement
     * @param modelMetaDesc
     *            the model meta description.
     */
    protected void handleField(VariableElement fieldElement,
            ModelMetaDesc modelMetaDesc) {
        AttributeMetaDesc attributeMetaDesc =
            attributeMetaDescFactory.createAttributeMetaDesc(fieldElement);
        if (attributeMetaDesc != null) {
            modelMetaDesc.addAttributeMetaDesc(attributeMetaDesc);
        }
    }

}
