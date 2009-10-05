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

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * An implementation class for "between" filter criterion.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class BetweenCriterion extends AbstractCriterion implements
        FilterCriterion {

    /**
     * The start value;
     */
    protected Object start;

    /**
     * The end value;
     */
    protected Object end;

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     * @param start
     *            the start value
     * @param end
     *            the end value
     * @throws NullPointerException
     *             if the start parameter is null or if the end parameter is
     *             null
     */
    public BetweenCriterion(CoreAttributeMeta<?, ?> attributeMeta,
            Object start, Object end) throws NullPointerException {
        super(attributeMeta);
        if (start == null) {
            throw new NullPointerException("The start parameter is null.");
        }
        if (end == null) {
            throw new NullPointerException("The end parameter is null.");
        }
        this.start = start;
        this.end = end;
    }

    public void apply(Query query) {
        query.addFilter(
            attributeMeta.getName(),
            FilterOperator.GREATER_THAN_OR_EQUAL,
            start);
        query.addFilter(
            attributeMeta.getName(),
            FilterOperator.LESS_THAN_OR_EQUAL,
            end);
    }

    public boolean accept(Object model) {
        Object v = attributeMeta.getValue(model);
        if (v == null) {
            return false;
        }
        return compareValue(v, start) >= 0 && compareValue(v, end) <= 0;
    }
}