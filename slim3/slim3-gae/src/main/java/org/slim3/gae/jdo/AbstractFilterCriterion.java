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
package org.slim3.gae.jdo;

/**
 * An abstract class for filter criterion.
 * 
 * @author higa
 * @param <T>
 *            the parameter type
 * @since 3.0
 * 
 */
public abstract class AbstractFilterCriterion<T> implements FilterCriterion {

    /**
     * The property name.
     */
    protected String propertyName;

    /**
     * The parameter name.
     */
    protected String parameterName;

    /**
     * The parameter.
     */
    protected T parameter;

    /**
     * Constructor.
     * 
     * @param propertyName
     *            the property name
     * @param parameterName
     *            the parameter name
     * @param parameter
     *            the parameter
     * @throws NullPointerException
     *             if the propertyName parameter is null or if the parameterName
     *             parameter is null or if the parameter parameter is null
     */
    public AbstractFilterCriterion(String propertyName, String parameterName,
            T parameter) throws NullPointerException {
        if (propertyName == null) {
            throw new NullPointerException(
                    "The propertyName parameter is null.");
        }
        if (parameterName == null) {
            throw new NullPointerException(
                    "The parameterName parameter is null.");
        }
        if (parameter == null) {
            throw new NullPointerException("The parameter parameter is null.");
        }
        this.propertyName = propertyName;
        this.parameterName = parameterName;
        this.parameter = parameter;
    }

    /**
     * Returns the property name.
     * 
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns the parameter name.
     * 
     * @return the parameter name
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * Returns the parameter.
     * 
     * @return the parameter
     */
    public T getParameter() {
        return parameter;
    }

    @Override
    public String getParameterDeclaration() {
        return parameter.getClass().getName() + " " + parameterName;
    }
}