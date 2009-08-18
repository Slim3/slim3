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

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.slim3.gen.Constants;
import org.slim3.gen.util.ClassUtil;
import org.slim3.gen.util.TypeUtil;

/**
 * Creates a GWT service async description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class GWTServiceAsyncDescFactory {

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /** the service async description method factory */
    protected final GWTServiceAsyncMethodDescFactory serviceAsyncMethodDescFactory;

    /**
     * Creates a new {@link GWTServiceAsyncDescFactory}.
     * 
     * @param processingEnv
     *            the processing environment
     * @param serviceAsyncMethodDescFactory
     *            he service async description method factory
     */
    public GWTServiceAsyncDescFactory(ProcessingEnvironment processingEnv,
            GWTServiceAsyncMethodDescFactory serviceAsyncMethodDescFactory) {
        if (processingEnv == null) {
            throw new NullPointerException(
                "The processingEnv parameter is null.");
        }
        if (serviceAsyncMethodDescFactory == null) {
            throw new NullPointerException(
                "The serviceAsyncMethodDescFactory parameter is null.");
        }
        this.processingEnv = processingEnv;
        this.serviceAsyncMethodDescFactory = serviceAsyncMethodDescFactory;
    }

    /**
     * Creates a {@link GWTServiceAsyncDesc}.
     * 
     * @param serviceElement
     *            the service element
     * @return a service async description
     */
    public GWTServiceAsyncDesc createServiceAsyncDesc(TypeElement serviceElement) {
        if (serviceElement == null) {
            throw new NullPointerException(
                "The serviceElement parameter is null.");
        }
        GWTServiceAsyncDesc serviceAsyncDesc = new GWTServiceAsyncDesc();
        serviceAsyncDesc.setPackageName(ClassUtil.getPackageName(serviceElement
            .getQualifiedName()
            .toString()));
        serviceAsyncDesc.setSimpleName(serviceElement.getSimpleName()
            + Constants.ASYNC_SUFFIX);
        serviceAsyncDesc.setServiceClassName(serviceElement
            .getQualifiedName()
            .toString());
        serviceAsyncDesc.setServiceElement(serviceElement);
        handleTypeParameters(serviceElement, serviceAsyncDesc);
        handleSuperInterfaceMethods(serviceElement, serviceAsyncDesc);
        handleMethods(serviceElement, serviceAsyncDesc, Collections
            .<TypeMirror, TypeMirror> emptyMap());
        return serviceAsyncDesc;
    }

    /**
     * Handles type parameters.
     * 
     * @param serviceElement
     *            the service element
     * @param serviceAsyncDesc
     *            the service async description
     */
    protected void handleTypeParameters(TypeElement serviceElement,
            GWTServiceAsyncDesc serviceAsyncDesc) {
        for (TypeParameterElement typeParameterElement : serviceElement
            .getTypeParameters()) {
            String typeName =
                TypeUtil.getTypeName(typeParameterElement.asType(), Collections
                    .<TypeMirror, TypeMirror> emptyMap(), processingEnv);
            serviceAsyncDesc.addTypeParameterName(typeName);
        }
    }

    /**
     * Handles super interface methods.
     * 
     * @param typeElement
     *            the type element
     * @param serviceAsyncDesc
     *            the service async description
     */
    protected void handleSuperInterfaceMethods(TypeElement typeElement,
            GWTServiceAsyncDesc serviceAsyncDesc) {
        for (TypeMirror superInterfaceType : processingEnv
            .getTypeUtils()
            .directSupertypes(typeElement.asType())) {
            TypeElement superInterfaceElement =
                TypeUtil.toTypeElement(superInterfaceType, processingEnv);
            if (superInterfaceElement == null
                || !superInterfaceElement.getKind().isInterface()) {
                continue;
            }
            Map<TypeMirror, TypeMirror> typeParameterMap =
                TypeUtil.createTypeParameterMap(
                    superInterfaceElement,
                    superInterfaceType,
                    processingEnv);
            handleSuperInterfaceMethods(superInterfaceElement, serviceAsyncDesc);
            handleMethods(
                superInterfaceElement,
                serviceAsyncDesc,
                typeParameterMap);
            serviceAsyncDesc.addServiceSuperInterfaceType(superInterfaceType);
        }
    }

    /**
     * Handles methods.
     * 
     * @param typeElement
     *            the type element
     * @param serviceAsyncDesc
     *            the service async description
     * @param typeParameterMap
     *            the type parameter map which maps a formal parameter and an
     *            actual parameter.
     */
    protected void handleMethods(TypeElement typeElement,
            GWTServiceAsyncDesc serviceAsyncDesc,
            Map<TypeMirror, TypeMirror> typeParameterMap) {
        for (ExecutableElement methodElement : ElementFilter
            .methodsIn(typeElement.getEnclosedElements())) {
            if (!serviceAsyncDesc.getServiceSuperInterfaceTypes().isEmpty()) {
                removeOverridenMethodDesc(methodElement, serviceAsyncDesc);
            }
            GWTServiceAsyncMethodDesc methodDesc =
                serviceAsyncMethodDescFactory.createServiceAsyncMethodDesc(
                    methodElement,
                    typeParameterMap);
            serviceAsyncDesc.addServiceAsyncMethodDesc(methodDesc);
        }
    }

    /**
     * Removes overriden method description.
     * 
     * @param overrider
     *            the overrider
     * @param serviceAsyncDesc
     *            the service async description
     */
    protected void removeOverridenMethodDesc(ExecutableElement overrider,
            GWTServiceAsyncDesc serviceAsyncDesc) {
        for (Iterator<GWTServiceAsyncMethodDesc> it =
            serviceAsyncDesc.getServiceAsyncMethodDescs().iterator(); it
            .hasNext();) {
            ExecutableElement overriden = it.next().getMethodElement();
            if (processingEnv.getElementUtils().overrides(
                overrider,
                overriden,
                serviceAsyncDesc.getServiceElement())) {
                it.remove();
                break;
            }
        }
    }

}
