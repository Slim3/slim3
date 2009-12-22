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

/**
 * A meta data of collection attribute.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @param <A>
 *            the attribute type
 * @param <E>
 *            the element type of collection
 * @since 3.0
 * 
 */
public class CollectionAttributeMeta<M, A, E> extends
        AttributeMeta<M, A> {

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @param fieldName
     *            the field name
     * @param attributeClass
     *            the attribute class
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null or if the attributeClass parameter is null or if the
     *             fieldName parameter is null
     */
    public CollectionAttributeMeta(ModelMeta<M> modelMeta, String name,
            String fieldName, Class<? super A> attributeClass)
            throws NullPointerException {
        super(modelMeta, name, fieldName, attributeClass);
    }

    /**
     * Returns the "equal" filter.
     * 
     * @param value
     *            the value
     * @return the "equal" filter
     */
    public FilterCriterion equal(E value) {
        return new EqualCriterion(this, value);
    }

    /**
     * Returns the "less than" filter.
     * 
     * @param value
     *            the value
     * @return the "less than" filter
     */
    public FilterCriterion lessThan(E value) {
        return new LessThanCriterion(this, value);
    }

    /**
     * Returns the "less than or equal" filter.
     * 
     * @param value
     *            the value
     * @return the "less than or equal" filter
     */
    public FilterCriterion lessThanOrEqual(E value) {
        return new LessThanOrEqualCriterion(this, value);
    }

    /**
     * Returns the "greater than" filter.
     * 
     * @param value
     *            the value
     * @return the "greater than" filter
     */
    public FilterCriterion greaterThan(E value) {
        return new GreaterThanCriterion(this, value);
    }

    /**
     * Returns the "greater than or equal" filter.
     * 
     * @param value
     *            the value
     * @return the "greater than or equal" filter
     */
    public FilterCriterion greaterThanOrEqual(E value) {
        return new GreaterThanOrEqualCriterion(this, value);
    }
}