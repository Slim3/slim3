/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.ArrayList;
import java.util.List;

import org.slim3.util.AppEngineUtil;
import org.slim3.util.ByteUtil;
import org.slim3.util.ThrowableUtil;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.SortPredicate;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.common.util.Base64DecoderException;

/**
 * An abstract query.
 * 
 * @param <SUB>
 *            the sub type
 * @author higa
 * @since 1.0.0
 * 
 */
public abstract class AbstractQuery<SUB> {

    /**
     * The datastore query.
     */
    protected Query query;

    /**
     * The transaction.
     */
    protected Transaction tx;

    /**
     * Whether the transaction was set.
     */
    protected boolean txSet = false;

    /**
     * The fetch options.
     */
    protected FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();

    /**
     * Constructor.
     * 
     */
    public AbstractQuery() {
        setUpQuery();
    }

    /**
     * Constructor.
     * 
     * @param kind
     *            the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     * 
     */
    public AbstractQuery(String kind) throws NullPointerException {
        setUpQuery(kind);
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
    public AbstractQuery(String kind, Key ancestorKey)
            throws NullPointerException {
        setUpQuery(kind, ancestorKey);
    }

    /**
     * Constructor.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public AbstractQuery(Key ancestorKey) throws NullPointerException {
        setUpQuery(ancestorKey);
    }

    /**
     * Sets up an internal query.
     * 
     */
    protected void setUpQuery() {
        query = new Query();
    }

    /**
     * Sets up an internal query.
     * 
     * @param kind
     *            the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     * 
     */
    protected void setUpQuery(String kind) throws NullPointerException {
        if (kind == null) {
            throw new NullPointerException(
                "The kind parameter must not be null.");
        }
        query = new Query(kind);
    }

    /**
     * Sets up an internal query.
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
    protected void setUpQuery(String kind, Key ancestorKey)
            throws NullPointerException {
        if (kind == null) {
            throw new NullPointerException(
                "The kind parameter must not be null.");
        }
        if (ancestorKey == null) {
            throw new NullPointerException(
                "The ancestorKey parameter must not be null.");
        }
        query = new Query(kind, ancestorKey);
    }

    /**
     * Sets up an internal query.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    protected void setUpQuery(Key ancestorKey) throws NullPointerException {
        if (ancestorKey == null) {
            throw new NullPointerException(
                "The ancestorKey parameter must not be null.");
        }
        query = new Query(ancestorKey);
    }

    /**
     * Specified the transaction.
     * 
     * @param tx
     *            the transaction
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    protected void setTx(Transaction tx) throws IllegalStateException {
        if (tx != null && !tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        this.tx = tx;
        txSet = true;
    }

    /**
     * Specifies the offset.
     * 
     * @param offset
     *            the offset
     * @return this instance
     */
    @SuppressWarnings("unchecked")
    public SUB offset(int offset) {
        fetchOptions.offset(offset);
        return (SUB) this;
    }

    /**
     * Specifies the limit.
     * 
     * @param limit
     *            the limit
     * @return this instance
     */
    @SuppressWarnings("unchecked")
    public SUB limit(int limit) {
        fetchOptions.limit(limit);
        return (SUB) this;
    }

    /**
     * Specifies the size of prefetch.
     * 
     * @param prefetchSize
     *            the size of prefetch
     * @return this instance
     */
    @SuppressWarnings("unchecked")
    public SUB prefetchSize(int prefetchSize) {
        fetchOptions.prefetchSize(prefetchSize);
        return (SUB) this;
    }

    /**
     * Specifies the size of chunk.
     * 
     * @param chunkSize
     *            the size of chunk
     * @return this instance
     */
    @SuppressWarnings("unchecked")
    public SUB chunkSize(int chunkSize) {
        fetchOptions.chunkSize(chunkSize);
        return (SUB) this;
    }

    /**
     * Specifies the cursor.
     * 
     * @param cursor
     *            the cursor
     * @return this instance
     * @throws NullPointerException
     *             if the cursor parameter is null
     */
    @SuppressWarnings("unchecked")
    public SUB cursor(Cursor cursor) throws NullPointerException {
        if (cursor == null) {
            throw new NullPointerException(
                "The cursor parameter must not be null.");
        }
        fetchOptions.cursor(cursor);
        return (SUB) this;
    }

    /**
     * Specifies the encoded cursor.
     * 
     * @param encodedCursor
     *            the encoded cursor
     * @return this instance
     * @throws NullPointerException
     *             if the encodedCursor parameter is null
     */
    @SuppressWarnings("unchecked")
    public SUB encodedCursor(String encodedCursor) throws NullPointerException {
        if (encodedCursor == null) {
            throw new NullPointerException(
                "The encodedCursor parameter must not be null.");
        }
        fetchOptions.cursor(Cursor.fromWebSafeString(encodedCursor));
        return (SUB) this;
    }

    /**
     * Adds the filter.
     * 
     * @param propertyName
     *            the property name
     * @param operator
     *            the {@link FilterOperator}
     * @param value
     *            the value
     * 
     * @return this instance
     * @throws NullPointerException
     *             if the propertyName parameter is null or if the operator
     *             parameter is null
     */
    @SuppressWarnings("unchecked")
    public SUB filter(String propertyName, FilterOperator operator, Object value)
            throws NullPointerException {
        if (propertyName == null) {
            throw new NullPointerException(
                "The propertyName parameter must not be null.");
        }
        if (operator == null) {
            throw new NullPointerException(
                "The operator parameter must not be null.");
        }
        query.addFilter(propertyName, operator, value);
        return (SUB) this;
    }

    /**
     * Adds the filters.
     * 
     * @param filters
     *            the filters
     * @return this instance
     * @throws NullPointerException
     *             if the element of the filters parameter is null
     */
    @SuppressWarnings("unchecked")
    public SUB filter(Filter... filters) throws NullPointerException {
        for (Filter f : filters) {
            if (f == null) {
                throw new NullPointerException(
                    "The element of the filters parameter must not be null.");
            }
            query.addFilter(f.getPropertyName(), f.getOperator(), f.getValue());
        }
        return (SUB) this;
    }

    /**
     * Specifies the encoded filters.
     * 
     * @param encodedFilters
     *            the encoded filters
     * @return this instance
     * @throws NullPointerException
     *             if the encodedFilters parameter is null
     */
    public SUB encodedFilters(String encodedFilters)
            throws NullPointerException {
        if (encodedFilters == null) {
            throw new NullPointerException(
                "The encodedFilters parameter must not be null.");
        }
        try {
            Filter[] filters =
                (Filter[]) ByteUtil.toObject(Base64.decode(encodedFilters));
            return filter(filters);
        } catch (Base64DecoderException e) {
            throw ThrowableUtil.wrap(e);
        }
    }

    /**
     * Adds the sorts.
     * 
     * @param sorts
     *            the array of sorts
     * @return this instance
     * @throws NullPointerException
     *             if the element of the sorts parameter is null
     */
    @SuppressWarnings("unchecked")
    public SUB sort(Sort... sorts) throws NullPointerException {
        for (Sort s : sorts) {
            if (s == null) {
                throw new NullPointerException(
                    "The element of the sorts parameter must not be null.");
            }
            query.addSort(s.getPropertyName(), s.getDirection());
        }
        return (SUB) this;
    }

    /**
     * Adds the sort.
     * 
     * @param propertyName
     *            the property name
     * @return this instance
     * @throws NullPointerException
     *             if the propertyName parameter is null
     */
    public SUB sort(String propertyName) throws NullPointerException {
        return sort(propertyName, SortDirection.ASCENDING);
    }

    /**
     * Adds the sort.
     * 
     * @param propertyName
     *            the property name
     * @param direction
     *            the sort direction
     * @return this instance
     * @throws NullPointerException
     *             if the propertyName parameter is null or if the direction
     *             parameter is null
     */
    @SuppressWarnings("unchecked")
    public SUB sort(String propertyName, SortDirection direction)
            throws NullPointerException {
        if (propertyName == null) {
            throw new NullPointerException(
                "The propertyName parameter must not be null.");
        }
        if (direction == null) {
            throw new NullPointerException(
                "The direction parameter must not be null.");
        }
        query.addSort(propertyName, direction);
        return (SUB) this;
    }

    /**
     * Specifies the encoded sorts.
     * 
     * @param encodedSorts
     *            the encoded sorts
     * @return this instance
     * @throws NullPointerException
     *             if the encodedSorts parameter is null
     */
    public SUB encodedSorts(String encodedSorts) throws NullPointerException {
        if (encodedSorts == null) {
            throw new NullPointerException(
                "The encodedSorts parameter must not be null.");
        }
        try {
            Sort[] sorts =
                (Sort[]) ByteUtil.toObject(Base64.decode(encodedSorts));
            return sort(sorts);
        } catch (Base64DecoderException e) {
            throw ThrowableUtil.wrap(e);
        }
    }

    /**
     * Returns the filters.
     * 
     * @return the filters
     */
    public Filter[] getFilters() {
        List<FilterPredicate> list = query.getFilterPredicates();
        Filter[] filters = new Filter[list.size()];
        for (int i = 0; i < list.size(); i++) {
            FilterPredicate f = list.get(i);
            filters[i] =
                new Filter(f.getPropertyName(), f.getOperator(), f.getValue());
        }
        return filters;
    }

    /**
     * Returns the encoded filters.
     * 
     * @return the encoded filters
     */
    public String getEncodedFilters() {
        return Base64.encode(ByteUtil.toByteArray(getFilters()));
    }

    /**
     * Returns the sorts.
     * 
     * @return the sorts
     */
    public Sort[] getSorts() {
        List<SortPredicate> list = query.getSortPredicates();
        Sort[] sorts = new Sort[list.size()];
        for (int i = 0; i < list.size(); i++) {
            SortPredicate s = list.get(i);
            sorts[i] = new Sort(s.getPropertyName(), s.getDirection());
        }
        return sorts;
    }

    /**
     * Returns the encoded sorts.
     * 
     * @return the encoded sorts
     */
    public String getEncodedSorts() {
        return Base64.encode(ByteUtil.toByteArray(getSorts()));
    }

    /**
     * Returns entities as list.
     * 
     * @return entities as list
     */
    protected List<Entity> asEntityList() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        if (!AppEngineUtil.isProduction() && query.getKind() == null) {
            List<Entity> list = new ArrayList<Entity>();
            List<String> kinds = DatastoreUtil.getKinds();
            Key ancestor = query.getAncestor();
            if (ancestor != null) {
                for (String kind : kinds) {
                    Query q = new Query(kind, ancestor);
                    list.addAll(asEntityList(ds, q));
                }
            } else {
                for (String kind : kinds) {
                    Query q = new Query(kind);
                    list.addAll(asEntityList(ds, q));
                }
            }
            return list;
        }
        return asEntityList(ds, query);
    }

    /**
     * Returns entities as list.
     * 
     * @param ds
     *            the datastore service
     * @param qry
     *            the query
     * @return entities as list
     */
    protected List<Entity> asEntityList(DatastoreService ds, Query qry) {
        PreparedQuery pq =
            txSet ? DatastoreUtil.prepare(ds, tx, qry) : DatastoreUtil.prepare(
                ds,
                qry);
        return DatastoreUtil.asList(pq, fetchOptions);
    }

    /**
     * Returns entities as query result list.
     * 
     * @return entities as query result list
     */
    protected QueryResultList<Entity> asQueryResultEntityList() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq =
            txSet ? DatastoreUtil.prepare(ds, tx, query) : DatastoreUtil
                .prepare(ds, query);
        return DatastoreUtil.asQueryResultList(pq, fetchOptions);
    }

    /**
     * Returns entities as query result iterator.
     * 
     * @return entities as query result iterator
     */
    protected QueryResultIterator<Entity> asQueryResultEntityIterator() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq =
            txSet ? DatastoreUtil.prepare(ds, tx, query) : DatastoreUtil
                .prepare(ds, query);
        return DatastoreUtil.asQueryResultIterator(pq, fetchOptions);
    }

    /**
     * Returns entities as query result iterable.
     * 
     * @return entities as query result iterable
     */
    protected QueryResultIterable<Entity> asQueryResultEntityIterable() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq =
            txSet ? DatastoreUtil.prepare(ds, tx, query) : DatastoreUtil
                .prepare(ds, query);
        return DatastoreUtil.asQueryResultIterable(pq, fetchOptions);
    }

    /**
     * Returns a single entity.
     * 
     * @return a single entity
     */
    protected Entity asSingleEntity() {
        if (!AppEngineUtil.isProduction() && query.getKind() == null) {
            List<Entity> list = asEntityList();
            if (list.size() == 0) {
                return null;
            }
            if (list.size() > 1) {
                throw new PreparedQuery.TooManyResultsException();
            }
            return list.get(0);
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq =
            txSet ? DatastoreUtil.prepare(ds, tx, query) : DatastoreUtil
                .prepare(ds, query);
        return DatastoreUtil.asSingleEntity(pq);
    }

    /**
     * Returns a list of keys.
     * 
     * @return a list of keys
     */
    public List<Key> asKeyList() {
        query.setKeysOnly();
        List<Entity> entityList = asEntityList();
        List<Key> ret = new ArrayList<Key>(entityList.size());
        for (Entity e : entityList) {
            ret.add(e.getKey());
        }
        return ret;
    }

    /**
     * Returns a number of entities.
     * 
     * @return a number of entities
     */
    public int count() {
        query.setKeysOnly();
        List<Entity> entityList = asEntityList();
        return entityList.size();
    }

    /**
     * Returns a number of entities. This method can only return up to 1,000
     * results, but this method can return the results quickly.
     * 
     * @return a number of entities
     */
    public int countQuickly() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq =
            txSet ? DatastoreUtil.prepare(ds, tx, query) : DatastoreUtil
                .prepare(ds, query);
        return DatastoreUtil.countEntities(pq);
    }

    /**
     * Return a minimum value of the property. The value does not include null.
     * 
     * @param <T>
     *            the property type
     * @param propertyName
     *            the property name
     * @return a minimum value of the property
     * @throws NullPointerException
     *             if the propertyName parameter is null
     */
    @SuppressWarnings("unchecked")
    protected <T> T min(String propertyName) throws NullPointerException {
        if (propertyName == null) {
            throw new NullPointerException(
                "The propertyName parameter must not be null.");
        }
        query.addFilter(propertyName, FilterOperator.GREATER_THAN, null);
        query.addSort(propertyName, SortDirection.ASCENDING);
        fetchOptions.offset(0).limit(1);
        List<Entity> list = asEntityList();
        if (list.size() == 0) {
            return null;
        }
        return (T) list.get(0).getProperty(propertyName);
    }

    /**
     * Return a maximum value of the property.
     * 
     * @param <T>
     *            the property type
     * @param propertyName
     *            the property name
     * @return a maximum value of the property
     * @throws NullPointerException
     *             if the propertyName parameter is null
     */
    @SuppressWarnings("unchecked")
    protected <T> T max(String propertyName) throws NullPointerException {
        if (propertyName == null) {
            throw new NullPointerException(
                "The propertyName parameter must not be null.");
        }
        query.addSort(propertyName, SortDirection.DESCENDING);
        fetchOptions.offset(0).limit(1);
        List<Entity> list = asEntityList();
        if (list.size() == 0) {
            return null;
        }
        return (T) list.get(0).getProperty(propertyName);
    }

    /**
     * Returns entities as {@link Iterable}.
     * 
     * @return entities as {@link Iterable}
     */
    protected Iterable<Entity> asIterableEntities() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq =
            txSet ? DatastoreUtil.prepare(ds, tx, query) : DatastoreUtil
                .prepare(ds, query);
        return DatastoreUtil.asIterable(pq, fetchOptions);
    }
}