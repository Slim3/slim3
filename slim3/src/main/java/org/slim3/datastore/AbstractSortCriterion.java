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

import com.google.appengine.api.datastore.Query.SortPredicate;

/**
 * An abstract class for filter.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class AbstractSortCriterion extends AbstractCriterion implements
        SortCriterion {

    /**
     * The {@link SortPredicate}.
     */
    protected SortPredicate sortPredicate;

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     * @throws NullPointerException
     *             if the attributeMeta parameter is null
     */
    public AbstractSortCriterion(AbstractAttributeMeta<?, ?> attributeMeta)
            throws NullPointerException {
        super(attributeMeta);
    }

    public SortPredicate getSortPredicate() {
        return sortPredicate;
    }
}