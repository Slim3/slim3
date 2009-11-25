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

import org.slim3.gen.Constants;
import org.slim3.gen.util.ClassUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.ClassDeclaration;

/**
 * Creates a model ref description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelRefDescFactory {

    /** the environment */
    protected final AnnotationProcessorEnvironment env;

    /**
     * Creates a new {@link ModelRefDescFactory}.
     * 
     * @param env
     *            the environment
     */
    public ModelRefDescFactory(AnnotationProcessorEnvironment env) {
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        this.env = env;
    }

    /**
     * Creates a model meta description.
     * 
     * @param classDeclaration
     *            the model declaration.
     * @return a model description
     */
    public ModelRefDesc createModelRefDesc(ClassDeclaration classDeclaration) {
        if (classDeclaration == null) {
            throw new NullPointerException(
                "The classDeclaration parameter is null.");
        }
        String qualifiedName = classDeclaration.getQualifiedName();
        String packageName = ClassUtil.getPackageName(qualifiedName);
        String simpleName = ClassUtil.getSimpleName(qualifiedName);
        ModelRefDesc modelRefDesc =
            new ModelRefDesc(
                packageName,
                simpleName + Constants.REF_SUFFIX,
                qualifiedName);
        return modelRefDesc;
    }

}
