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
 * A meta data of attribute.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @param <A>
 *            the attribute type
 * @since 3.0
 * 
 */
public class CoreAttributeMeta<M, A> extends AbstractAttributeMeta<M, A> {

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
     * @param attributeClass
     *            the attribute class
     */
    public CoreAttributeMeta(ModelMeta<M> modelMeta, String name,
            Class<A> attributeClass) {
        super(modelMeta, name, attributeClass);
    }

    /**
     * Returns the "equal" filter.
     * 
     * @param value
     *            the value
     * @return the "equal" filter
     */
    public FilterCriterion equal(A value) {
        return new EqualCriterion(this, value);
    }

    /**
     * Returns the "not equal" filter.
     * 
     * @param value
     *            the value
     * @return the "not equal" filter
     */
    public FilterCriterion notEqual(A value) {
        return new NotEqualCriterion(this, value);
    }

    /**
     * Returns the "less than" filter.
     * 
     * @param value
     *            the value
     * @return the "less than" filter
     */
    public FilterCriterion lessThan(A value) {
        return new LessThanCriterion(this, value);
    }

    /**
     * Returns the "less than or equal" filter.
     * 
     * @param value
     *            the value
     * @return the "less than or equal" filter
     */
    public FilterCriterion lessThanOrEqual(A value) {
        return new LessThanOrEqualCriterion(this, value);
    }

    /**
     * Returns the "greater than" filter.
     * 
     * @param value
     *            the value
     * @return the "greater than" filter
     */
    public FilterCriterion greaterThan(A value) {
        return new GreaterThanCriterion(this, value);
    }

    /**
     * Returns the "greater than or equal" filter.
     * 
     * @param value
     *            the value
     * @return the "greater than or equal" filter
     */
    public FilterCriterion greaterThanOrEqual(A value) {
        return new GreaterThanOrEqualCriterion(this, value);
    }

    /**
     * Returns the "is not null" filter.
     * 
     * @return the "is not null" filter
     */
    public FilterCriterion isNotNull() {
        return isNotNull;
    }
}