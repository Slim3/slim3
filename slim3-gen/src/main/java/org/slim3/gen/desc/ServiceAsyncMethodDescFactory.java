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

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.slim3.gen.util.TypeUtil;

/**
 * Represents a service async method description factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ServiceAsyncMethodDescFactory {

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /**
     * Creates a new {@link ServiceAsyncMethodDescFactory}.
     * 
     * @param processingEnv
     *            the processing environment
     */
    public ServiceAsyncMethodDescFactory(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    /**
     * Creates a {@link ServiceAsyncMethodDesc}.
     * 
     * @param methodElement
     *            the method element
     * @param typeParameterMap
     *            the type parameter map which maps a formula parameter and an
     *            actual parameter.
     * @return a service async method description
     */
    protected ServiceAsyncMethodDesc createServiceAsyncMethodDesc(
            ExecutableElement methodElement,
            Map<TypeMirror, TypeMirror> typeParameterMap) {
        if (methodElement == null) {
            throw new NullPointerException(
                "The methodElement parameter is null.");
        }
        if (typeParameterMap == null) {
            throw new NullPointerException(
                "The typeParameterMap parameter is null.");
        }
        ServiceAsyncMethodDesc methodDesc = new ServiceAsyncMethodDesc();
        methodDesc.setName(methodElement.getSimpleName().toString());
        methodDesc.setMethodElement(methodElement);
        handleTypeParameters(methodElement, methodDesc, typeParameterMap);
        handleReturnType(methodElement, methodDesc, typeParameterMap);
        handleParameters(methodElement, methodDesc, typeParameterMap);
        handleThrownTypes(methodElement, methodDesc, typeParameterMap);
        return methodDesc;
    }

    /**
     * Handles type parameters.
     * 
     * @param methodElement
     *            the method element
     * @param methodDesc
     *            the service async method description
     * @param typeParameterMap
     *            the type parameter map which maps a formal parameter and an
     *            actual parameter.
     */
    protected void handleTypeParameters(ExecutableElement methodElement,
            ServiceAsyncMethodDesc methodDesc,
            Map<TypeMirror, TypeMirror> typeParameterMap) {
        for (TypeParameterElement typeParameterElement : methodElement
            .getTypeParameters()) {
            String typeName =
                TypeUtil.getTypeName(
                    typeParameterElement.asType(),
                    typeParameterMap,
                    processingEnv);
            methodDesc.addTypeParameterName(typeName);
        }
    }

    /**
     * Handles the return type.
     * 
     * @param methodElement
     *            the method element
     * @param methodDesc
     *            the service async method description
     * @param typeParameterMap
     *            the type parameter map which maps a formal parameter and an
     *            actual parameter.
     */
    protected void handleReturnType(ExecutableElement methodElement,
            ServiceAsyncMethodDesc methodDesc,
            Map<TypeMirror, TypeMirror> typeParameterMap) {
        TypeMirror wrapperType =
            TypeUtil.toWrapperTypeIfPrimitive(
                methodElement.getReturnType(),
                processingEnv);
        String typeName =
            TypeUtil.getTypeName(wrapperType, typeParameterMap, processingEnv);
        methodDesc.setReturnTypeName(typeName);
    }

    /**
     * Handles parameters.
     * 
     * @param methodElement
     *            the method element
     * @param methodDesc
     *            the service async method description
     * @param typeParameterMap
     *            the type parameter map which maps a formal parameter and an
     *            actual parameter.
     */
    protected void handleParameters(ExecutableElement methodElement,
            ServiceAsyncMethodDesc methodDesc,
            Map<TypeMirror, TypeMirror> typeParameterMap) {
        for (VariableElement parameterElement : methodElement.getParameters()) {
            String name = parameterElement.getSimpleName().toString();
            String typeName =
                TypeUtil.getTypeName(
                    parameterElement.asType(),
                    typeParameterMap,
                    processingEnv);
            methodDesc.addParameterName(name, typeName);
        }
    }

    /**
     * Handles thrown types.
     * 
     * @param methodElement
     *            the method element
     * @param methodDesc
     *            the service async method description
     * @param typeParameterMap
     *            the type parameter map which maps a formal parameter and an
     *            actual parameter.
     */
    protected void handleThrownTypes(ExecutableElement methodElement,
            ServiceAsyncMethodDesc methodDesc,
            Map<TypeMirror, TypeMirror> typeParameterMap) {
        for (TypeMirror thrownType : methodElement.getThrownTypes()) {
            String typeName =
                TypeUtil.getTypeName(
                    thrownType,
                    typeParameterMap,
                    processingEnv);
            methodDesc.addThrownTypeName(typeName);
        }
    }
}
