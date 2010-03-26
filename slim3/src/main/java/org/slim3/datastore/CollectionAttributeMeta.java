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

import java.util.Arrays;

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
 * @since 1.0.0
 * 
 */
public class CollectionAttributeMeta<M, A, E> extends
        SortableAttributeMeta<M, A> {

    /**
     * The "is not null" filter.
     */
    protected IsNotNullCriterion isNotNull = new IsNotNullCriterion(this);

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
    public EqualCriterion equal(E value) {
        return new EqualCriterion(this, value);
    }

    /**
     * Returns the "not equal" filter.
     * 
     * @param value
     *            the value
     * @return the "not equal" filter
     */
    public NotEqualCriterion notEqual(E value) {
        return new NotEqualCriterion(this, value);
    }

    /**
     * Returns the "less than" filter.
     * 
     * @param value
     *            the value
     * @return the "less than" filter
     */
    public LessThanCriterion lessThan(E value) {
        return new LessThanCriterion(this, value);
    }

    /**
     * Returns the "less than or equal" filter.
     * 
     * @param value
     *            the value
     * @return the "less than or equal" filter
     */
    public LessThanOrEqualCriterion lessThanOrEqual(E value) {
        return new LessThanOrEqualCriterion(this, value);
    }

    /**
     * Returns the "greater than" filter.
     * 
     * @param value
     *            the value
     * @return the "greater than" filter
     */
    public GreaterThanCriterion greaterThan(E value) {
        return new GreaterThanCriterion(this, value);
    }

    /**
     * Returns the "greater than or equal" filter.
     * 
     * @param value
     *            the value
     * @return the "greater than or equal" filter
     */
    public GreaterThanOrEqualCriterion greaterThanOrEqual(E value) {
        return new GreaterThanOrEqualCriterion(this, value);
    }

    /**
     * Returns the "in" filter.
     * 
     * @param value
     *            the value
     * @return the "in" filter
     * @throws NullPointerException
     *             if the value parameter is null
     * @throws IllegalArgumentException
     *             if the value parameter is empty
     */
    public InCriterion in(Iterable<E> value) throws NullPointerException,
            IllegalArgumentException {
        return new InCriterion(this, value);
    }

    /**
     * Returns the "in" filter.
     * 
     * @param value
     *            the value
     * @return the "in" filter
     * @throws IllegalArgumentException
     *             if the value parameter is empty
     */
    public InCriterion in(E... value) throws IllegalArgumentException {
        return new InCriterion(this, Arrays.asList(value));
    }

    /**
     * Returns the "is not null" filter.
     * 
     * @return the "is not null" filter
     */
    public IsNotNullCriterion isNotNull() {
        return isNotNull;
    }
}