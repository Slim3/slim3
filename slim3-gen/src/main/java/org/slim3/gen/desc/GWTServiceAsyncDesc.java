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

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.slim3.gen.util.ClassUtil;

/**
 * Represents a GWT service async description.
 * 
 * @author taedium
 * @since 3.0
 */
public class GWTServiceAsyncDesc implements ClassDesc {

    /** the package name */
    protected String packageName;

    /** the simple name */
    protected String simpleName;

    /** the service class name */
    protected String serviceClassName;

    /** the list of service async method description */
    protected List<GWTServiceAsyncMethodDesc> serviceAsyncMethodDescs =
        new ArrayList<GWTServiceAsyncMethodDesc>();

    /** the list of service super interface type */
    protected List<TypeMirror> serviceSuperInterfaceTypes =
        new ArrayList<TypeMirror>();

    /** the service element */
    protected TypeElement serviceElement;

    /** the list of type parameter name */
    protected List<String> typeParameterNames = new ArrayList<String>();

    /**
     * Returns the packageName.
     * 
     * @return the packageName
     */
    @Override
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sets the packageName.
     * 
     * @param packageName
     *            the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Returns the simpleName.
     * 
     * @return the simpleName
     */
    @Override
    public String getSimpleName() {
        return simpleName;
    }

    /**
     * Sets the simpleName.
     * 
     * @param simpleName
     *            the simpleName to set
     */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    @Override
    public String getQualifiedName() {
        return ClassUtil.getQualifiedName(packageName, simpleName);
    }

    /**
     * Returns the serviceClassName.
     * 
     * @return the serviceClassName
     */
    public String getServiceClassName() {
        return serviceClassName;
    }

    /**
     * Sets the serviceClassName.
     * 
     * @param serviceClassName
     *            the serviceClassName to set
     */
    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    /**
     * Returns the list of service async method description.
     * 
     * @return the serviceAsyncMethodDescs
     */
    public List<GWTServiceAsyncMethodDesc> getServiceAsyncMethodDescs() {
        return serviceAsyncMethodDescs;
    }

    /**
     * Adds the service async method description.
     * 
     * @param serviceAsyncMethodDesc
     *            the service async method description
     */
    public void addServiceAsyncMethodDesc(
            GWTServiceAsyncMethodDesc serviceAsyncMethodDesc) {
        serviceAsyncMethodDescs.add(serviceAsyncMethodDesc);
    }

    /**
     * Returns the list of service super interface type.
     * 
     * @return the serviceSuperInterfaceTypes
     */
    public List<TypeMirror> getServiceSuperInterfaceTypes() {
        return serviceSuperInterfaceTypes;
    }

    /**
     * Adds the list of service super interface type.
     * 
     * @param serviceSuperInterfaceTypes
     *            the list of service super interface type
     */
    public void addServiceSuperInterfaceType(
            TypeMirror serviceSuperInterfaceTypes) {
        this.serviceSuperInterfaceTypes.add(serviceSuperInterfaceTypes);
    }

    /**
     * Returns the service element.
     * 
     * @return the typeElement
     */
    public TypeElement getServiceElement() {
        return serviceElement;
    }

    /**
     * Sets the typeElement.
     * 
     * @param serviceElement
     *            the typeElement to set
     */
    public void setServiceElement(TypeElement serviceElement) {
        this.serviceElement = serviceElement;
    }

    /**
     * Returns the list of type parameter name.
     * 
     * @return the typeParameterNames
     */
    public List<String> getTypeParameterNames() {
        return typeParameterNames;
    }

    /**
     * Adds the list of type parameter name.
     * 
     * @param typeParameterName
     *            the list of type parameter name
     */
    public void addTypeParameterName(String typeParameterName) {
        this.typeParameterNames.add(typeParameterName);
    }

}
