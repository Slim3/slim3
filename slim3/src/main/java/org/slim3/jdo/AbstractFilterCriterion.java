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
package org.slim3.jdo;

/**
 * An abstract class for filter criterion.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class AbstractFilterCriterion implements FilterCriterion {

    /**
     * The property name.
     */
    protected String propertyName;

    /**
     * The parameter.
     */
    protected Object parameter;

    /**
     * Constructor.
     * 
     * @param propertyName
     *            the property name
     * @param parameter
     *            the parameter
     * @throws NullPointerException
     *             if the propertyName parameter is null or the parameter
     *             parameter is null
     */
    public AbstractFilterCriterion(String propertyName, Object parameter)
            throws NullPointerException {
        if (propertyName == null) {
            throw new NullPointerException(
                "The propertyName parameter is null.");
        }
        if (parameter == null) {
            throw new NullPointerException("The parameter parameter is null.");
        }
        this.propertyName = propertyName;
        this.parameter = parameter;
    }

    /**
     * Returns the parameter.
     * 
     * @return the parameter
     */
    public Object getParameter() {
        return parameter;
    }
}