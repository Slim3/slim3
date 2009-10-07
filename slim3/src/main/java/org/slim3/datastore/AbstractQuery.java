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
package org.slim3.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * An abstract query.
 * 
 * @param <SUB>
 *            the sub type
 * @author higa
 * @since 3.0
 * 
 */
public abstract class AbstractQuery<SUB> {

    static final int MAX_RETRY = 10;

    private static Logger logger =
        Logger.getLogger(AbstractQuery.class.getName());

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
    protected FetchOptions fetchOptions = FetchOptions.Builder.withOffset(0);

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
     * Constructor.
     * 
     */
    protected AbstractQuery() {
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
            throw new NullPointerException("The kind parameter is null.");
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
            throw new NullPointerException("The kind parameter is null.");
        }
        if (ancestorKey == null) {
            throw new NullPointerException("The ancestorKey parameter is null.");
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
            throw new NullPointerException("The ancestorKey parameter is null.");
        }
        query = new Query(ancestorKey);
    }

    /**
     * Specified the transaction.
     * 
     * @param tx
     *            the transaction
     * @return this instance
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    @SuppressWarnings("unchecked")
    public SUB tx(Transaction tx) throws IllegalStateException {
        if (tx != null && !tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        this.tx = tx;
        txSet = true;
        return (SUB) this;
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
     * Returns entities as a list.
     * 
     * @return entities as a list
     */
    protected List<Entity> asEntityList() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq = prepareInternal(ds);
        return asListInternal(pq);
    }

    /**
     * Returns a single entity.
     * 
     * @return a single entity
     */
    protected Entity asSingleEntity() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq = prepareInternal(ds);
        return asSingleEntityInternal(pq);
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
        PreparedQuery pq = prepareInternal(ds);
        return countEntitiesInternal(pq);
    }

    /**
     * Return a minimum value of the property. It does not include null.
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
                "The propertyName parameter is null.");
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
                "The propertyName parameter is null.");
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
        PreparedQuery pq = prepareInternal(ds);
        return asIterableInternal(pq);
    }

    /**
     * Prepares the query internally.
     * 
     * @param ds
     *            the datastore
     * @return a prepared query.
     */
    protected PreparedQuery prepareInternal(DatastoreService ds) {
        try {
            if (txSet) {
                return ds.prepare(tx, query);
            }
            return ds.prepare(query);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    if (txSet) {
                        return ds.prepare(tx, query);
                    }
                    return ds.prepare(query);
                } catch (DatastoreTimeoutException e2) {
                    logger.log(Level.WARNING, "Retry("
                        + i
                        + "): "
                        + e2.getMessage(), e2);
                }
            }
            throw e;
        }
    }

    /**
     * Returns a list of entities internally.
     * 
     * @param preparedQuery
     *            the prepared query
     * 
     * @return a list of entities
     */
    protected List<Entity> asListInternal(PreparedQuery preparedQuery) {
        try {
            return preparedQuery.asList(fetchOptions);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return preparedQuery.asList(fetchOptions);
                } catch (DatastoreTimeoutException e2) {
                    logger.log(Level.WARNING, "Retry("
                        + i
                        + "): "
                        + e2.getMessage(), e2);
                }
            }
            throw e;
        }
    }

    /**
     * Returns a single entity internally.
     * 
     * @param preparedQuery
     *            the query
     * 
     * @return a single entity
     */
    protected Entity asSingleEntityInternal(PreparedQuery preparedQuery) {
        try {
            return preparedQuery.asSingleEntity();
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return preparedQuery.asSingleEntity();
                } catch (DatastoreTimeoutException e2) {
                    logger.log(Level.WARNING, "Retry("
                        + i
                        + "): "
                        + e2.getMessage(), e2);
                }
            }
            throw e;
        }
    }

    /**
     * Returns a single entity internally.
     * 
     * @param preparedQuery
     *            the query
     * 
     * @return a single entity
     */
    protected Iterable<Entity> asIterableInternal(PreparedQuery preparedQuery) {
        try {
            return preparedQuery.asIterable(fetchOptions);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return preparedQuery.asIterable(fetchOptions);
                } catch (DatastoreTimeoutException e2) {
                    logger.log(Level.WARNING, "Retry("
                        + i
                        + "): "
                        + e2.getMessage(), e2);
                }
            }
            throw e;
        }
    }

    /**
     * Returns a number of entities internally.
     * 
     * @param preparedQuery
     *            the prepared query
     * 
     * @return a number of entities
     */
    protected int countEntitiesInternal(PreparedQuery preparedQuery) {
        try {
            return preparedQuery.countEntities();
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return preparedQuery.countEntities();
                } catch (DatastoreTimeoutException e2) {
                    logger.log(Level.WARNING, "Retry("
                        + i
                        + "): "
                        + e2.getMessage(), e2);
                }
            }
            throw e;
        }
    }
}