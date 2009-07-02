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
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * The model class.
     */
    protected Class<M> modelClass;

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
     * @param pm
     *            the persistence manager
     * @param modelClass
     *            the model class
     * @throws NullPointerException
     *             if the pm parameter is null or if the modelClass parameter is
     *             null
     */
    public SelectQuery(PersistenceManager pm, Class<M> modelClass)
            throws NullPointerException {

        if (pm == null) {
            throw new NullPointerException("The pm parameter is null.");
        }
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        this.pm = pm;
        this.modelClass = modelClass;
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
     * 
     */
    @SuppressWarnings("unchecked")
    public M getSingleResult() {
        Query query = newQuery();
        query.setUnique(true);
        return (M) query.executeWithArray(getParameters());
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
     * Returns the query string.
     * 
     * @return the query string
     */
    public String getQueryString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("select from ").append(modelClass.getName());
        String filter = getFilter();
        if (filter != null) {
            sb.append(" where ").append(filter);
        }
        String ordering = getOrdering();
        if (ordering != null) {
            sb.append(" order by ").append(ordering);
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
     * Creates a new query.
     * 
     * @return a new query.
     */
    protected Query newQuery() {
        Query query = pm.newQuery(modelClass);
        String filter = getFilter();
        if (filter != null) {
            query.setFilter(filter);
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
     * Returns the filter for the query.
     * 
     * @return the filter for the query
     */
    protected String getFilter() {
        if (filterCriteria == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(100);
        int i = 0;
        for (FilterCriterion c : filterCriteria) {
            if (c == null) {
                continue;
            }
            if (sb.length() == 0) {
                sb.append(c.getQueryString(":" + i++));
            } else {
                sb.append(" && ").append(c.getQueryString(":" + i++));
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
    protected Object[] getParameters() {
        if (filterCriteria == null) {
            return ZERO_ARRAY;
        }
        List<Object> params = new ArrayList<Object>();
        for (FilterCriterion c : filterCriteria) {
            if (c == null) {
                continue;
            }
            for (Object param : c.getParameters()) {
                params.add(param);
            }
        }
        return params.toArray();
    }

    /**
     * Returns the ordering specification.
     * 
     * @return the ordering specification
     */
    protected String getOrdering() {
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
}