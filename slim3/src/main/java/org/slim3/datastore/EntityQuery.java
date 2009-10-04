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

import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * A query for datastore entity.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class EntityQuery extends AbstractQuery<EntityQuery> {

    /**
     * Constructor.
     * 
     * @param kind
     *            the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     * 
     */
    public EntityQuery(String kind) throws NullPointerException {
        super(kind);
    }

    /**
     * Constructor.
     * 
     * @param kind
     *            the kind
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the kind parameter is null or if the ancestorKey parameter
     *             is null
     * 
     */
    public EntityQuery(String kind, Key ancestorKey)
            throws NullPointerException {
        super(kind, ancestorKey);
    }

    /**
     * Adds the filter criterion.
     * 
     * @param propertyName
     *            the property name
     * @param operator
     *            the filter operator
     * @param value
     *            the value
     * 
     * @return this instance
     */
    public EntityQuery filter(String propertyName, FilterOperator operator,
            Object value) {
        query.addFilter(propertyName, operator, value);
        return this;
    }

    /**
     * Adds the sort criterion.
     * 
     * @param propertyName
     *            the property name
     * 
     * @return this instance
     */
    public EntityQuery sort(String propertyName) {
        return sort(propertyName, SortDirection.ASCENDING);
    }

    /**
     * Adds the sort criterion.
     * 
     * @param propertyName
     *            the property name
     * @param direction
     *            the sort direction
     * 
     * @return this instance
     */
    public EntityQuery sort(String propertyName, SortDirection direction) {
        query.addSort(propertyName, direction);
        return this;
    }

    /**
     * Returns entities as a list.
     * 
     * @return entities as a list
     */
    public List<Entity> asList() {
        return super.asEntityList();
    }

    @Override
    public Entity asSingleEntity() {
        return super.asSingleEntity();
    }

    /**
     * Returns entities as {@link Iterable}.
     * 
     * @return entities as {@link Iterable}
     */
    public Iterable<Entity> asIterable() {
        return super.asIterableEntities();
    }

    /**
     * Returns entities as {@link Iterator}.
     * 
     * @return entities as {@link Iterator}
     */
    public Iterator<Entity> asIterator() {
        return asIterable().iterator();
    }
}