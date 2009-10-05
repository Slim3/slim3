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

import java.util.Collection;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * An implementation class for "descending" sort criterion.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class DescCriterion extends AbstractSortCriterion {

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     */
    public DescCriterion(AbstractAttributeMeta<?, ?> attributeMeta) {
        super(attributeMeta);
    }

    public void apply(Query query) {
        query.addSort(attributeMeta.getName(), SortDirection.DESCENDING);

    }

    public int compare(Object model1, Object model2) {
        Object v1 = attributeMeta.getValue(model1);
        if (v1 instanceof Collection<?>) {
            v1 = getFirstElement(Collection.class.cast(v1));
        }
        Object v2 = attributeMeta.getValue(model2);
        if (v2 instanceof Collection<?>) {
            v2 = getFirstElement(Collection.class.cast(v2));
        }
        if (v1 == null && v2 == null) {
            return 0;
        }
        if (v1 == null) {
            return -1;
        }
        if (v2 == null) {
            return 1;
        }
        return -1 * compareValue(v1, v2);
    }

}