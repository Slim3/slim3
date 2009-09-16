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
package org.slim3.datastore;

import org.slim3.util.BeanDesc;
import org.slim3.util.BeanUtil;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * An abstract query for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class AbstractQuery {

    /**
     * The model class.
     */
    protected Class<?> modelClass;

    /**
     * The native query.
     */
    protected Query nativeQuery;

    /**
     * The bean descriptor.
     */
    protected BeanDesc beanDesc;

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * @throws NullPointerException
     *             the modelClass parameter is null
     */
    public AbstractQuery(Class<?> modelClass) throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        this.modelClass = modelClass;
        nativeQuery = new Query(modelClass.getSimpleName());
    }

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * @param ancestor
     *            the ancestor
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the ancestor
     *             parameter is null
     */
    public AbstractQuery(Class<?> modelClass, Key ancestor)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        if (ancestor == null) {
            throw new NullPointerException("The ancestor parameter is null.");
        }
        this.modelClass = modelClass;
        nativeQuery = new Query(modelClass.getSimpleName(), ancestor);
    }

    /**
     * Returns the model class.
     * 
     * @return the model class
     */
    protected Class<?> getModelClass() {
        return modelClass;
    }

    /**
     * Adds the filter criterion.
     * 
     * @param propertyName
     *            the property name
     * @param operator
     *            the filter operator
     * @param value
     *            the property value
     * 
     */
    protected void addFilter(String propertyName, FilterOperator operator,
            Object value) {
        nativeQuery.addFilter(propertyName, operator, value);
    }

    /**
     * Adds the sort criterion.
     * 
     * @param propertyName
     *            the property name
     * @param direction
     *            the sort direction
     * 
     */
    protected void addSort(String propertyName, SortDirection direction) {
        nativeQuery.addSort(propertyName, direction);
    }

    /**
     * Returns the bean descriptor.
     * 
     * @return the bean descriptor
     */
    protected BeanDesc getBeanDesc() {
        if (beanDesc == null) {
            beanDesc = BeanUtil.getBeanDesc(modelClass);
        }
        return beanDesc;
    }
}