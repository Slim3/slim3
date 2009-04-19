/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.gae.jdo;

import java.util.ArrayList;
import java.util.List;

/**
 * A query class for select clause.
 * 
 * @author higa
 * @param <T>
 *            the model type
 * @since 3.0
 * 
 */
public class SelectQuery<T> {

    /**
     * The zero array.
     */
    protected static final Object[] ZERO_ARRAY = new Object[0];

    /**
     * The meta data of model.
     */
    protected ModelMeta<T> modelMeta;

    /**
     * The filter criteria.
     */
    protected FilterCriterion[] filterCriteria;

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public SelectQuery(ModelMeta<T> modelMeta) throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        this.modelMeta = modelMeta;
    }

    /**
     * Specifies where clause.
     * 
     * @param criteria
     *            the filter criteria
     * @return this instance
     */
    public SelectQuery<T> where(FilterCriterion... criteria) {
        this.filterCriteria = criteria;
        return this;
    }

    /**
     * Returns the filter for the query.
     * 
     * @return the filter for the query
     */
    public String getFilter() {
        if (filterCriteria == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(100);
        for (FilterCriterion c : filterCriteria) {
            if (c == null) {
                continue;
            }
            if (sb.length() == 0) {
                sb.append(" where ").append(c.getQueryString());
            } else {
                sb.append(" && ").append(c.getQueryString());
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    /**
     * Returns where clause as string.
     * 
     * @return where clause as string
     */
    public String getParametersDeclaration() {
        if (filterCriteria == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(100);
        for (FilterCriterion c : filterCriteria) {
            if (c == null) {
                continue;
            }
            if (sb.length() == 0) {
                sb.append(c.getParameterDeclaration());
            } else {
                sb.append(", ").append(c.getParameterDeclaration());
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    /**
     * Returns the specified parameters.
     * 
     * @return the specified parameters
     */
    public Object[] getParameters() {
        if (filterCriteria == null) {
            return ZERO_ARRAY;
        }
        List<Object> params = new ArrayList<Object>();
        for (FilterCriterion c : filterCriteria) {
            if (c == null) {
                continue;
            }
            params.add(c.getParameter());
        }
        return params.toArray();
    }

    /**
     * Returns the filter criteria.
     * 
     * @return the filter criteria
     */
    public FilterCriterion[] getFilterCriteria() {
        return filterCriteria;
    }
}