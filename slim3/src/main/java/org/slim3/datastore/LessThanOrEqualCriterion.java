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
 * An implementation class for "less than or equal" filter.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class LessThanOrEqualCriterion extends AbstractCriterion implements
        FilterCriterion {

    /**
     * The value;
     */
    protected Object value;

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     * @param value
     *            the value
     * @see AbstractCriterion#AbstractCriterion(AbstractAttributeMeta)
     */
    public LessThanOrEqualCriterion(AbstractAttributeMeta<?, ?> attributeMeta,
            Object value) {
        super(attributeMeta);
        this.value = value;
    }

    public void apply(Query query) {
        query.addFilter(
            attributeMeta.getName(),
            FilterOperator.LESS_THAN_OR_EQUAL,
            convertValueForDatastore(value));
    }

    public boolean accept(Object model) {
        Object v = attributeMeta.getValue(model);
        return compareValue(v, value) <= 0;
    }
}