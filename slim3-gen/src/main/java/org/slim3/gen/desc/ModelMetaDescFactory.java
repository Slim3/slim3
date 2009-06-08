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

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementScanner6;

import org.slim3.gen.Constants;
import org.slim3.gen.util.ClassUtil;

/**
 * Creates a model meta description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelMetaDescFactory {

    /** the element scanner */
    protected final ElementScanner6<Void, ModelMetaDesc> scanner;

    /**
     * Creates a new {@link ModelMetaDescFactory}.
     * 
     * @param scanner
     *            the element scanner
     */
    public ModelMetaDescFactory(ElementScanner6<Void, ModelMetaDesc> scanner) {
        this.scanner = scanner;
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
        scanner.scan(modelElement, modelMetaDesc);
        return modelMetaDesc;
    }
}
