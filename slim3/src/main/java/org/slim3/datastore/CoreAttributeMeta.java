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
     * Returns the "equal" filter criterion.
     * 
     * @param value
     *            the value
     * @return the "equal" filter criterion
     */
    public FilterCriterion equal(A value) {
        if (isEmpty(value)) {
            return null;
        }
        return new EqualCriterion(this, convertValueForDatastore(value));
    }

    /**
     * Returns the "less than" filter criterion.
     * 
     * @param value
     *            the value
     * @return the "less than" filter criterion
     */
    public FilterCriterion lessThan(A value) {
        if (isEmpty(value)) {
            return null;
        }
        return new LessThanCriterion(this, convertValueForDatastore(value));
    }

    /**
     * Returns the "less than or equal" filter criterion.
     * 
     * @param value
     *            the value
     * @return the "less than or equal" filter criterion
     */
    public FilterCriterion lessThanOrEqual(A value) {
        if (isEmpty(value)) {
            return null;
        }
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
    public FilterCriterion greaterThan(A value) {
        if (isEmpty(value)) {
            return null;
        }
        return new GreaterThanCriterion(this, convertValueForDatastore(value));
    }

    /**
     * Returns the "greater than or equal" filter criterion.
     * 
     * @param value
     *            the value
     * @return the "greater than or equal" filter criterion
     */
    public FilterCriterion greaterThanOrEqual(A value) {
        if (isEmpty(value)) {
            return null;
        }
        return new GreaterThanOrEqualCriterion(
            this,
            convertValueForDatastore(value));
    }

    /**
     * Returns the "between" or "greater than or equal" or "less than or equal"
     * filter criterion.
     * 
     * @param start
     *            the start value
     * @param end
     *            the end value
     * @return the "between" or "greater than or equal" or "less than or equal"
     *         filter criterion
     */
    public FilterCriterion between(A start, A end) {
        if (isEmpty(start) && isEmpty(end)) {
            return null;
        }
        if (!isEmpty(start) && !isEmpty(end)) {
            return new BetweenCriterion(
                this,
                convertValueForDatastore(start),
                convertValueForDatastore(end));
        }
        if (!isEmpty(start)) {
            return new GreaterThanOrEqualCriterion(
                this,
                convertValueForDatastore(start));
        }
        return new LessThanOrEqualCriterion(this, convertValueForDatastore(end));
    }

    /**
     * Returns the "is not null" filter criterion.
     * 
     * @return the "is not null" filter criterion
     */
    public IsNotNullCriterion isNotNull() {
        return new IsNotNullCriterion(this);
    }

    /**
     * Returns the "is null" filter criterion.
     * 
     * @return the "is null" filter criterion
     */
    public IsNullCriterion isNull() {
        return new IsNullCriterion(this);
    }
}