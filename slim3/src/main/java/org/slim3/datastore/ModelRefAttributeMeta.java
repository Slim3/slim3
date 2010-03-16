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

import com.google.appengine.api.datastore.Key;

/**
 * A meta data of attribute for {@link ModelRef}.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @param <A>
 *            the attribute type
 * @param <RM>
 *            the reference model type
 * @since 1.0.0
 * 
 */
public class ModelRefAttributeMeta<M, A, RM> extends
        AttributeMeta<M, A> {

    /**
     * The "is not null" filter.
     */
    protected IsNotNullCriterion isNotNull = new IsNotNullCriterion(this);

    /**
     * The reference model class.
     */
    protected Class<RM> referenceModelClass;

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
     * @param referenceModelClass
     *            the reference model class
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null or if the attributeClass parameter is null or if the
     *             fieldName parameter is null or if the referenceModelClass
     *             parameter is null
     */
    public ModelRefAttributeMeta(ModelMeta<M> modelMeta, String name,
            String fieldName, Class<? super A> attributeClass,
            Class<RM> referenceModelClass) throws NullPointerException {
        super(modelMeta, name, fieldName, attributeClass);
        if (referenceModelClass == null) {
            throw new NullPointerException(
                "The referenceModelClass parameter must not be null.");
        }
        this.referenceModelClass = referenceModelClass;
    }

    /**
     * Returns the "equal" filter.
     * 
     * @param value
     *            the value
     * @return the "equal" filter
     */
    public FilterCriterion equal(Key value) {
        return new EqualCriterion(this, value);
    }

    /**
     * Returns the "less than" filter.
     * 
     * @param value
     *            the value
     * @return the "less than" filter
     */
    public FilterCriterion lessThan(Key value) {
        return new LessThanCriterion(this, value);
    }

    /**
     * Returns the "less than or equal" filter.
     * 
     * @param value
     *            the value
     * @return the "less than or equal" filter
     */
    public FilterCriterion lessThanOrEqual(Key value) {
        return new LessThanOrEqualCriterion(this, value);
    }

    /**
     * Returns the "greater than" filter.
     * 
     * @param value
     *            the value
     * @return the "greater than" filter
     */
    public FilterCriterion greaterThan(Key value) {
        return new GreaterThanCriterion(this, value);
    }

    /**
     * Returns the "greater than or equal" filter.
     * 
     * @param value
     *            the value
     * @return the "greater than or equal" filter
     */
    public FilterCriterion greaterThanOrEqual(Key value) {
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