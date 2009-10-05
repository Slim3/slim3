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

/**
 * An abstract class for sort criterion.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class AbstractSortCriterion extends AbstractCriterion implements
        SortCriterion {

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     */
    public AbstractSortCriterion(AbstractAttributeMeta<?, ?> attributeMeta) {
        super(attributeMeta);
    }

    /**
     * Returns a first element of the collection.
     * 
     * @param c
     *            the collection
     * @return a first element
     */
    protected Object getFirstElement(Collection<?> c) {
        if (c.size() == 0) {
            return null;
        }
        return c.iterator().next();
    }
}