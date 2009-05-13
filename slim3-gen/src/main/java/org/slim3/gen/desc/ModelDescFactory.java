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

import org.slim3.gen.util.ClassUtil;

/**
 * Creates a model description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelDescFactory {

    /** the element scanner */
    protected final ElementScanner6<Void, ModelDesc> scanner;

    /**
     * Creates a new {@link ModelDescFactory}.
     * 
     * @param scanner
     *            the element scanner
     */
    public ModelDescFactory(ElementScanner6<Void, ModelDesc> scanner) {
        this.scanner = scanner;
    }

    /**
     * Creates a model description.
     * 
     * @param model
     *            the model element.
     * @return a model description
     */
    public ModelDesc createModelDesc(TypeElement model) {
        ModelDesc modelDesc = new ModelDesc();
        modelDesc.setPackageName(ClassUtil.getPackageName(model
                .getQualifiedName().toString()));
        modelDesc.setSimpleName(model.getSimpleName().toString());
        scanner.scan(model, modelDesc);
        return modelDesc;
    }
}
