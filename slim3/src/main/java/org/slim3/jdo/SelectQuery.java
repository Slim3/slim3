/*
 * Copyright the original author or authors.
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
package org.slim3.jdo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * A query class for select clause.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 3.0
 * 
 */
public class SelectQuery<M> {

    /**
     * The zero array.
     */
    protected static final Object[] ZERO_ARRAY = new Object[0];

    /**
     * The meta data of model.
     */
    protected ModelMeta<M> modelMeta;

    /**
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * The filter criteria.
     */
    protected FilterCriterion[] filterCriteria;

    /**
     * The order criteria.
     */
    protected OrderCriterion[] orderCriteria;

    /**
     * the 0-based inclusive start index for range.
     */
    protected long startIndex = -1;

    /**
     * the 0-based exclusive end index for range.
     */
    protected long endIndex = -1;

    /**
     * The timeout interval (milliseconds).
     */
    protected int timeoutMillis = -1;

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param pm
     *            the persistence manager
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the pm parameter is
     *             null
     */
    public SelectQuery(ModelMeta<M> modelMeta, PersistenceManager pm)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        if (pm == null) {
            throw new NullPointerException("The pm parameter is null.");
        }
        this.modelMeta = modelMeta;
        this.pm = pm;
    }

    /**
     * Specifies where clause.
     * 
     * @param criteria
     *            the filter criteria
     * @return this instance
     */
    public SelectQuery<M> where(FilterCriterion... criteria) {
        this.filterCriteria = criteria;
        return this;
    }

    /**
     * Specifies order by clause.
     * 
     * @param criteria
     *            the order criteria
     * @return this instance
     */
    public SelectQuery<M> orderBy(OrderCriterion... criteria) {
        this.orderCriteria = criteria;
        return this;
    }

    /**
     * Specifies the range of results.
     * 
     * @param startIndex
     *            the 0-based inclusive start index for range
     * @param endIndex
     *            the 0-based exclusive end index for range
     * 
     * @return this instance
     */
    public SelectQuery<M> range(long startIndex, long endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        return this;
    }

    /**
     * Specifies the timeout interval (milliseconds).
     * 
     * @param timeoutMillis
     *            the timeout interval (milliseconds)
     * @return this instance
     */
    public SelectQuery<M> timeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
        return this;
    }

    /**
     * Creates a new query.
     * 
     * @return a new query.
     */
    public Query newQuery() {
        Query query = pm.newQuery(modelMeta.getModelClass());
        String filter = getFilter();
        if (filter != null) {
            query.setFilter(filter);
        }
        String parametersDeclaration = getParametersDeclaration();
        if (parametersDeclaration != null) {
            query.declareParameters(parametersDeclaration);
        }
        String ordering = getOrdering();
        if (ordering != null) {
            query.setOrdering(ordering);
        }
        if (startIndex > -1 && endIndex > -1) {
            query.setRange(startIndex, endIndex);
        }
        if (timeoutMillis > -1) {
            query.setTimeoutMillis(timeoutMillis);
        }
        return query;
    }

    /**
     * Returns the query string.
     * 
     * @return the query string
     */
    public String getQueryString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("select from ").append(modelMeta.getModelClass().getName());
        String filter = getFilter();
        if (filter != null) {
            sb.append(" where ").append(filter);
        }
        String ordering = getOrdering();
        if (ordering != null) {
            sb.append(" order by ").append(ordering);
        }
        String parametersDeclaration = getParametersDeclaration();
        if (parametersDeclaration != null) {
            sb.append(" parameters ").append(parametersDeclaration);
        }

        if (startIndex > -1 && endIndex > -1) {
            sb.append(" range ").append(startIndex).append(", ").append(
                    endIndex);
        }
        return sb.toString();
    }

    /**
     * Returns the query string with parameters.
     * 
     * @return the query string with parameters
     */
    public String getQueryStringWithParameters() {
        return getQueryString() + " with " + Arrays.asList(getParameters());
    }

    /**
     * Returns the results as list.
     * 
     * @return the results as list
     */
    @SuppressWarnings("unchecked")
    public List<M> getResultList() {
        Query query = newQuery();
        return (List<M>) query.executeWithArray(getParameters());
    }

    /**
     * Returns the single result for the query.
     * 
     * @return the single result for the query
     * @throws JDONonUniqueResultException
     *             if the results for the query is not unique
     */
    public M getSingleResult() throws JDONonUniqueResultException {
        range(0, 2);
        List<M> list = getResultList();
        if (list.size() > 1) {
            throw new JDONonUniqueResultException("The results for the query("
                    + getQueryStringWithParameters() + ") is not unique.");
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Returns the first result for the query.
     * 
     * @return the first result for the query
     * 
     */
    public M getFirstResult() {
        range(0, 1);
        List<M> list = getResultList();
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
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
                sb.append(c.getQueryString());
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
     * Returns the declaration for parameters.
     * 
     * @return the declaration for parameters
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
     * Returns the ordering specification.
     * 
     * @return the ordering specification
     */
    public String getOrdering() {
        if (orderCriteria == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(50);
        for (OrderCriterion c : orderCriteria) {
            if (c == null) {
                throw new NullPointerException(
                        "The order criterion must not be null.");
            }
            if (sb.length() == 0) {
                sb.append(c.getQueryString());
            } else {
                sb.append(", ").append(c.getQueryString());
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    /**
     * Returns the filter criteria.
     * 
     * @return the filter criteria
     */
    public FilterCriterion[] getFilterCriteria() {
        return filterCriteria;
    }

    /**
     * Returns the order criteria.
     * 
     * @return the order criteria
     */
    public OrderCriterion[] getOrderCriteria() {
        return orderCriteria;
    }

    /**
     * Returns the 0-based inclusive start index. If the start index is not set,
     * returns -1.
     * 
     * @return the start index
     */
    public long getStartIndex() {
        return startIndex;
    }

    /**
     * Returns the 0-based exclusive end index. If the end index is not set,
     * returns -1.
     * 
     * @return the end index
     */
    public long getEndIndex() {
        return endIndex;
    }

    /**
     * Returns the timeout interval (milliseconds). If the timeout interval is
     * not set, returns -1.
     * 
     * @return the timeout interval (milliseconds)
     */
    public int getTimeoutMillis() {
        return timeoutMillis;
    }
}