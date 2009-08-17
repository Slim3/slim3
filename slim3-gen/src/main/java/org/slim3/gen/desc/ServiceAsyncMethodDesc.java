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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;

/**
 * Represents a service async method description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ServiceAsyncMethodDesc {

    /** the method name */
    protected String name;

    /** the method element */
    protected ExecutableElement methodElement;

    /** the list of type parameter name */
    protected List<String> typeParameterNames = new ArrayList<String>();

    /** the return type name */
    protected String returnTypeName;

    /**
     * the map whose key is parameter name and whose value is parameter type
     * name
     */
    protected Map<String, String> parameterNames =
        new HashMap<String, String>();

    /** the list of thrown type names */
    protected List<String> thrownTypeNames = new ArrayList<String>();

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the method element.
     * 
     * @return the methodElement
     */
    public ExecutableElement getMethodElement() {
        return methodElement;
    }

    /**
     * Sets the method element.
     * 
     * @param methodElement
     *            the methodElement to set
     */
    public void setMethodElement(ExecutableElement methodElement) {
        this.methodElement = methodElement;
    }

    /**
     * Returns the list of parameter name.
     * 
     * @return the typeParameterNames
     */
    public List<String> getTypeParameterNames() {
        return typeParameterNames;
    }

    /**
     * Adds the parameter name.
     * 
     * @param typeParameterName
     *            the parameterName to set
     */
    public void addTypeParameterName(String typeParameterName) {
        this.typeParameterNames.add(typeParameterName);
    }

    /**
     * Returns the returnTypeName.
     * 
     * @return the returnTypeName
     */
    public String getReturnTypeName() {
        return returnTypeName;
    }

    /**
     * Sets the returnTypeName.
     * 
     * @param returnTypeName
     *            the returnTypeName to set
     */
    public void setReturnTypeName(String returnTypeName) {
        this.returnTypeName = returnTypeName;
    }

    /**
     * Returns the parameter name.
     * 
     * @return the parameterNames
     */
    public Map<String, String> getParameterNames() {
        return parameterNames;
    }

    /**
     * Adds a pair of the parameter name and the parameter type name.
     * 
     * @param parameterName
     *            the parameter name
     * @param parameterTypeName
     *            the parameter type name
     */
    public void addParameterName(String parameterName, String parameterTypeName) {
        this.parameterNames.put(parameterName, parameterTypeName);
    }

    /**
     * Returns the list of thrown type name.
     * 
     * @return the thrownTypeNames
     */
    public List<String> getThrownTypeNames() {
        return thrownTypeNames;
    }

    /**
     * Adds the thrown type name.
     * 
     * @param thrownTypeName
     *            the thrown type name.
     */
    public void addThrownTypeName(String thrownTypeName) {
        this.thrownTypeNames.add(thrownTypeName);
    }

}
