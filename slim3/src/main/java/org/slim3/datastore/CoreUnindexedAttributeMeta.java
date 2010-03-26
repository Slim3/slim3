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
 * A meta data of unindexed attribute.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @param <A>
 *            the attribute type
 * @since 1.0.1
 * 
 */
public class CoreUnindexedAttributeMeta<M, A> extends
        SortableUnindexedAttributeMeta<M, A> {

    /**
     * The "is not null" in-memory filter.
     */
    protected InMemoryIsNotNullCriterion isNotNull =
        new InMemoryIsNotNullCriterion(this);

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
     * 
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null or if the attributeClass parameter is null or if the
     *             fieldName parameter is null
     */
    public CoreUnindexedAttributeMeta(ModelMeta<M> modelMeta, String name,
            String fieldName, Class<A> attributeClass)
            throws NullPointerException {
        super(modelMeta, name, fieldName, attributeClass);
    }

    /**
     * Returns the "equal" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "equal" in-memory filter
     */
    public InMemoryEqualCriterion equal(A value) {
        return new InMemoryEqualCriterion(this, value);
    }

    /**
     * Returns the "not equal" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "not equal" in-memory filter
     */
    public InMemoryNotEqualCriterion notEqual(A value) {
        return new InMemoryNotEqualCriterion(this, value);
    }

    /**
     * Returns the "less than" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "less than" in-memory filter
     */
    public InMemoryLessThanCriterion lessThan(A value) {
        return new InMemoryLessThanCriterion(this, value);
    }

    /**
     * Returns the "less than or equal" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "less than or equal" in-memory filter
     */
    public InMemoryLessThanOrEqualCriterion lessThanOrEqual(A value) {
        return new InMemoryLessThanOrEqualCriterion(this, value);
    }

    /**
     * Returns the "greater than" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "greater than" in-memory filter
     */
    public InMemoryGreaterThanCriterion greaterThan(A value) {
        return new InMemoryGreaterThanCriterion(this, value);
    }

    /**
     * Returns the "greater than or equal" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "greater than or equal" in-memory filter
     */
    public InMemoryGreaterThanOrEqualCriterion greaterThanOrEqual(A value) {
        return new InMemoryGreaterThanOrEqualCriterion(this, value);
    }

    /**
     * Returns the "in" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "in" in-memory filter
     * @throws NullPointerException
     *             if the value parameter is null
     * @throws IllegalArgumentException
     *             if the value parameter is empty
     */
    public InMemoryInCriterion in(Iterable<A> value)
            throws NullPointerException, IllegalArgumentException {
        return new InMemoryInCriterion(this, value);
    }

    /**
     * Returns the "in" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "in" in-memory filter
     * @throws IllegalArgumentException
     *             if the value parameter is empty
     */
    public InMemoryInCriterion in(A... value) throws IllegalArgumentException {
        return new InMemoryInCriterion(this, Arrays.asList(value));
    }

    /**
     * Returns the "is not null" in-memory filter.
     * 
     * @return the "is not null" in-memory filter
     */
    public InMemoryIsNotNullCriterion isNotNull() {
        return isNotNull;
    }
}