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
        AbstractAttributeMeta<M, A> {

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @param attributeClass
     *            the attribute class
     */
    public CollectionAttributeMeta(ModelMeta<M> modelMeta, String name,
            Class<? super A> attributeClass) {
        super(modelMeta, name, attributeClass);
    }

    /**
     * Returns the "contains" filter criterion.
     * 
     * @param value
     *            the value
     * @return the "contains" filter criterion
     */
    public FilterCriterion contains(E value) {
        return new ContainsCriterion(this, convertValueForDatastore(value));
    }

    /**
     * Returns the "equal" filter criterion.
     * 
     * @param value
     *            the value
     * @return the "equal" filter criterion
     */
    public FilterCriterion equal(E value) {
        return new EqualCriterion(this, convertValueForDatastore(value));
    }

    /**
     * Returns the "less than" filter criterion.
     * 
     * @param value
     *            the value
     * @return the "less than" filter criterion
     */
    public FilterCriterion lessThan(E value) {
        return new LessThanCriterion(this, convertValueForDatastore(value));
    }

    /**
     * Returns the "less than or equal" filter criterion.
     * 
     * @param value
     *            the value
     * @return the "less than or equal" filter criterion
     */
    public FilterCriterion lessThanOrEqual(E value) {
        return new LessThanOrEqualCriterion(
            this,
            convertValueForDatastore(value));
    }

    /**
     * Returns the "greater than" filter criterion.
     * 
     * @param value
     *            the value
     * @return the "greater than" filter criterion
     */
    public FilterCriterion greaterThan(E value) {
        return new GreaterThanCriterion(this, convertValueForDatastore(value));
    }

    /**
     * Returns the "greater than or equal" filter criterion.
     * 
     * @param value
     *            the value
     * @return the "greater than or equal" filter criterion
     */
    public FilterCriterion greaterThanOrEqual(E value) {
        return new GreaterThanOrEqualCriterion(
            this,
            convertValueForDatastore(value));
    }
}