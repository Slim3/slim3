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

/**
 * An abstract query.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class AbstractQuery {

    static final int MAX_RETRY = 10;

    private static Logger logger =
        Logger.getLogger(AbstractQuery.class.getName());

    /**
     * The datastore query.
     */
    protected Query query;

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
        initialize(kind);
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
        initialize(kind, ancestorKey);
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
        initialize(ancestorKey);
    }

    /**
     * Constructor.
     * 
     */
    protected AbstractQuery() {
    }

    /**
     * Initializes this class.
     * 
     * @param kind
     *            the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     * 
     */
    protected void initialize(String kind) throws NullPointerException {
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        query = new Query(kind);
    }

    /**
     * Initializes this class.
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
    protected void initialize(String kind, Key ancestorKey)
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
     * Initializes this class.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    protected void initialize(Key ancestorKey) throws NullPointerException {
        if (ancestorKey == null) {
            throw new NullPointerException("The ancestorKey parameter is null.");
        }
        query = new Query(ancestorKey);
    }

    /**
     * Returns the result as a list of entities.
     * 
     * @return the result as a list of entities
     */
    public List<Entity> asEntityList() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq = prepareInternal(ds);
        return asListInternal(pq);
    }

    /**
     * Returns the single entity.
     * 
     * @return the single entity
     */
    public Entity asSingleEntity() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq = prepareInternal(ds);
        return asSingleEntityInternal(pq);
    }

    /**
     * Returns the result as a list of key.
     * 
     * @return the result as a list of key
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
     * Returns the iterable entities.
     * 
     * @return the iterable entities
     */
    public Iterable<Entity> asIterableEntities() {
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
            return ds.prepare(query);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
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
}