/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.slim3.datastore;

import java.io.Serializable;

import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * A class to define filter.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class Filter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The property name.
     */
    protected String propertyName;

    /**
     * The filter operator.
     */
    protected FilterOperator operator;

    /**
     * The value.
     */
    protected Object value;

    /**
     * Constructor.
     */
    protected Filter() {
    }

    /**
     * Constructor.
     * 
     * @param propertyName
     *            the property name
     * @param operator
     *            the filter operator
     * @param value
     *            the value
     * @throws NullPointerException
     *             if the propertyName parameter is null or if the operator
     *             parameter is null
     */
    public Filter(String propertyName, FilterOperator operator, Object value)
            throws NullPointerException {
        if (propertyName == null) {
            throw new NullPointerException(
                "The propertyName parameter must not be null.");
        }
        if (operator == null) {
            throw new NullPointerException(
                "The operator parameter must not be null.");
        }
        this.propertyName = propertyName;
        this.operator = operator;
        this.value = value;
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
     * Returns the filter operator.
     * 
     * @return the filter operator
     */
    public FilterOperator getOperator() {
        return operator;
    }

    /**
     * Returns the value.
     * 
     * @return the value
     */
    public Object getValue() {
        return value;
    }
}