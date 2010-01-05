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

import java.util.Arrays;
import java.util.Collection;

import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * An implementation class for "descending" sort.
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
     * @throws NullPointerException
     *             if the attributeMeta parameter is null
     */
    public DescCriterion(AttributeMeta<?, ?> attributeMeta)
            throws NullPointerException {
        super(attributeMeta);
        sort = new Sort(attributeMeta.getName(), SortDirection.DESCENDING);
    }

    public int compare(Object model1, Object model2) {
        Object v1 = convertValueForDatastore(attributeMeta.getValue(model1));
        if (v1 instanceof Collection<?>) {
            v1 = getGreatestValue((Collection<?>) v1);
        }
        Object v2 = convertValueForDatastore(attributeMeta.getValue(model2));
        if (v2 instanceof Collection<?>) {
            v2 = getGreatestValue((Collection<?>) v2);
        }
        return -1 * compareValue(v1, v2);
    }

    @Override
    public String toString() {
        return attributeMeta.getName() + " desc";
    }

    /**
     * Returns the greatest value of the collection.
     * 
     * @param collection
     *            the collection
     * @return the greatest value of the collection
     */
    protected Object getGreatestValue(Collection<?> collection) {
        if (collection.size() == 0) {
            return null;
        }
        if (collection.size() == 1) {
            return collection.iterator().next();
        }
        Object[] array = collection.toArray();
        Arrays.sort(array);
        return array[array.length - 1];
    }
}