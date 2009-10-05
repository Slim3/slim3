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

import java.util.Comparator;

/**
 * A {@link Comparator} for attribute value.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class AttributeComparator implements Comparator<Object> {

    /**
     * The sort criteria.
     */
    protected SortCriterion[] sortCriteria;

    /**
     * Constructor.
     * 
     * @param sortCriteria
     *            the sort criteria
     * @throws NullPointerException
     *             if the sortCriteria parameter is null
     */
    public AttributeComparator(SortCriterion... sortCriteria)
            throws NullPointerException {
        if (sortCriteria == null) {
            throw new NullPointerException(
                "The sortCriteria parameter is null.");
        }
        this.sortCriteria = sortCriteria;
    }

    public int compare(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            throw new NullPointerException("The model is null.");
        }
        for (SortCriterion c : sortCriteria) {
            int compared = c.compare(o1, o2);
            if (compared == 0) {
                continue;
            }
            return compared;
        }
        return 0;
    }
}