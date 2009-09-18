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

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.SortPredicate;

/**
 * A meta data of attribute.
 * 
 * @author higa
 * @param <T>
 *            the attribute type
 * @since 3.0
 * 
 */
public class AttributeMeta<T> {

    /**
     * The meta data of model.
     */
    protected ModelMeta<?> modelMeta;

    /**
     * The name.
     */
    protected String name;

    /**
     * The attribute class.
     */
    protected Class<?> attributeClass;

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @param attributeClass
     *            the attribute class
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null of if the attributeClass parameter is null
     */
    public AttributeMeta(ModelMeta<?> modelMeta, String name,
            Class<?> attributeClass) {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        if (attributeClass == null) {
            throw new NullPointerException(
                "The attributeClass parameter is null.");
        }
        this.modelMeta = modelMeta;
        this.name = name;
        this.attributeClass = attributeClass;
    }

    /**
     * Returns the "equal" filter predicate.
     * 
     * @param value
     *            the value
     * @return the "equal" filter predicate
     */
    public FilterPredicate equal(T value) {
        if (isEmpty(value)) {
            return null;
        }
        return new FilterPredicate(name, FilterOperator.EQUAL, value);
    }

    /**
     * Returns the "less than" filter predicate.
     * 
     * @param value
     *            the value
     * @return the "less than" filter predicate
     */
    public FilterPredicate lessThan(T value) {
        if (isEmpty(value)) {
            return null;
        }
        return new FilterPredicate(name, FilterOperator.LESS_THAN, value);
    }

    /**
     * Returns the "ascending" sort predicate.
     * 
     * @return the "ascending" sort predicate
     */
    public SortPredicate asc() {
        return new SortPredicate(name, SortDirection.ASCENDING);
    }

    /**
     * Returns the "descending" sort predicate.
     * 
     * @return the "descending" sort predicate
     */
    public SortPredicate desc() {
        return new SortPredicate(name, SortDirection.DESCENDING);
    }

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the attribute class
     * 
     * @return the attribute class
     */
    public Class<?> getAttributeClass() {
        return attributeClass;
    }

    /**
     * Determines if the parameter is empty.
     * 
     * @param parameter
     *            the parameter
     * @return whether the parameter is empty
     */
    protected boolean isEmpty(Object parameter) {
        return parameter == null || "".equals(parameter);
    }
}