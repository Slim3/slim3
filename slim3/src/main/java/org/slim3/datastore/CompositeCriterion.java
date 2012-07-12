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

import java.util.List;

import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;

/**
 * @author higa
 * @since 1.0.16
 * 
 */
public class CompositeCriterion implements FilterCriterion {

    /**
     * The model meta data.
     */
    protected ModelMeta<?> modelMeta;

    /**
     * The composite filter operator.
     */
    protected CompositeFilterOperator operator;

    /**
     * The filter criteria.
     */
    protected FilterCriterion[] criteria;

    /**
     * The filters.
     */
    protected Filter[] filters;

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the model meta data
     * 
     * @param operator
     *            the composite filter operator
     * @param criteria
     *            the filter criteria
     * @throws NullPointerException
     *             if the modelMeta parameter is null or the operator parameter
     *             is null
     */
    public CompositeCriterion(ModelMeta<?> modelMeta,
            CompositeFilterOperator operator, FilterCriterion... criteria)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException(
                "The modelMeta parameter must not be null.");
        }
        if (operator == null) {
            throw new NullPointerException(
                "The operator parameter must not be null.");
        }
        this.modelMeta = modelMeta;
        this.operator = operator;
        this.criteria = criteria;
        List<Filter> filters2 = DatastoreUtil.toFilters(modelMeta, criteria);
        switch (filters2.size()) {
        case 0:
            filters = new Filter[0];
            break;
        case 1:
            filters = new Filter[] { filters2.get(0) };
            break;
        default:
            filters = new Filter[] { new CompositeFilter(operator, filters2) };
        }
    }

    /**
     * Returns the model meta data.
     * 
     * @return the model meta data
     */
    public ModelMeta<?> getModelMeta() {
        return modelMeta;
    }

    /**
     * Returns the composite filter operator.
     * 
     * @return the composite filter operator
     */
    public CompositeFilterOperator getOperator() {
        return operator;
    }

    /**
     * Returns the filter criteria.
     * 
     * @return the filter criteria
     */
    public FilterCriterion[] getCriteria() {
        return criteria;
    }

    @Override
    public Filter[] getFilters() {
        return filters;
    }
}
