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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slim3.util.ClassUtil;
import org.slim3.util.Cleanable;
import org.slim3.util.Cleaner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Transaction;

/**
 * A class to access datastore.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class Datastore {

    static final int MAX_RETRY = 10;

    private static Logger logger = Logger.getLogger(Datastore.class.getName());

    private static ConcurrentHashMap<String, ModelMeta<?>> modelMetaCache =
        new ConcurrentHashMap<String, ModelMeta<?>>(87);

    private static volatile boolean initialized = false;

    static {
        initialize();
    }

    private static void initialize() {
        Cleaner.add(new Cleanable() {
            public void clean() {
                modelMetaCache.clear();
                initialized = false;
            }
        });
        initialized = true;
    }

    /**
     * Begins a transaction.
     * 
     * @return a begun transaction
     */
    public static Transaction beginTransaction() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return ds.beginTransaction();
    }

    /**
     * Commits the transaction.
     * 
     * @param tx
     *            the transaction
     * @throws NullPointerException
     *             if the tx parameter is null
     * @throws IllegalStateException
     *             if the transaction is not active
     */
    public static void commit(Transaction tx) throws NullPointerException,
            IllegalStateException {
        if (tx == null) {
            throw new NullPointerException("The tx parameter is null.");
        }
        if (!tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        commitInternal(tx);
    }

    private static void commitInternal(Transaction tx) {
        try {
            tx.commit();
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    tx.commit();
                    return;
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
     * Rolls back the transaction.
     * 
     * @param tx
     *            the transaction
     * @throws NullPointerException
     *             if the tx parameter is null
     * @throws IllegalStateException
     *             if the transaction is not active
     */
    public static void rollback(Transaction tx) throws NullPointerException,
            IllegalStateException {
        if (tx == null) {
            throw new NullPointerException("The tx parameter is null.");
        }
        if (!tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        rollbackInternal(tx);
    }

    private static void rollbackInternal(Transaction tx) {
        try {
            tx.rollback();
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    tx.rollback();
                    return;
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
     * Allocates a key within a namespace defined by the kind.
     * 
     * @param kind
     *            the kind
     * @return keys within a namespace defined by the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public static Key allocateId(String kind) throws NullPointerException {
        return allocateIds(kind, 1).iterator().next();
    }

    /**
     * Allocates a key within a namespace defined by the kind of the model.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @return a key within a namespace defined by the kind of the model
     */
    public static Key allocateId(ModelMeta<?> modelMeta) {
        return allocateIds(modelMeta, 1).iterator().next();
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the
     * kind.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @return a key within a namespace defined by the parent key and the kind
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public static Key allocateId(Key parentKey, String kind)
            throws NullPointerException {
        return allocateIds(parentKey, kind, 1).iterator().next();
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @return a key within a namespace defined by the parent key and the kind
     *         of the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public static Key allocateId(Key parentKey, ModelMeta<?> modelMeta)
            throws NullPointerException {
        return allocateIds(parentKey, modelMeta, 1).iterator().next();
    }

    /**
     * Allocates keys within a namespace defined by the kind.
     * 
     * @param kind
     *            the kind
     * @param num
     *            the number of allocated keys
     * @return keys within a namespace defined by the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public static KeyRange allocateIds(String kind, long num)
            throws NullPointerException {
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return allocateIdsInternal(ds, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param num
     *            the number of allocated keys
     * @return keys within a namespace defined by the kind of the model
     */
    public static KeyRange allocateIds(ModelMeta<?> modelMeta, long num) {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateIds(modelMeta.getModelClass().getSimpleName(), num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @param num
     * @return keys within a namespace defined by the parent key and the kind
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public static KeyRange allocateIds(Key parentKey, String kind, int num)
            throws NullPointerException {
        if (parentKey == null) {
            throw new NullPointerException("The parentKey parameter is null.");
        }
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return allocateIdsInternal(ds, parentKey, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param num
     * @return keys within a namespace defined by the parent key and the kind of
     *         the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public static KeyRange allocateIds(Key parentKey, ModelMeta<?> modelMeta,
            int num) throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateIds(
            parentKey,
            modelMeta.getModelClass().getSimpleName(),
            num);
    }

    private static KeyRange allocateIdsInternal(DatastoreService ds,
            String kind, long num) {
        try {
            return ds.allocateIds(kind, num);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return ds.allocateIds(kind, num);
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

    private static KeyRange allocateIdsInternal(DatastoreService ds,
            Key parentKey, String kind, long num) {
        try {
            return ds.allocateIds(parentKey, kind, num);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return ds.allocateIds(parentKey, kind, num);
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
     * Returns an entity specified by the key. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static Entity get(Key key) throws NullPointerException,
            EntityNotFoundRuntimeException {
        if (key == null) {
            throw new NullPointerException("The key parameter is null.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return getInternal(ds, key);
    }

    /**
     * Returns a model specified by the key. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> M get(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        Entity entity = get(key);
        return modelMeta.entityToModel(entity);
    }

    /**
     * Returns a model specified by the key. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param cache
     *            the cache of models. This cache can not be shared with
     *            multiple threads.
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the cache parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    @SuppressWarnings("unchecked")
    public static <M> M get(ModelMeta<M> modelMeta, Key key,
            Map<Key, Object> cache) throws NullPointerException,
            EntityNotFoundRuntimeException {
        if (cache == null) {
            throw new NullPointerException("The cache parameter is null.");
        }
        M model = (M) cache.get(key);
        if (model != null) {
            return model;
        }
        model = get(modelMeta, key);
        cache.put(key, model);
        return model;
    }

    /**
     * Returns an entity specified by the key within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static Entity get(Transaction tx, Key key)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException {
        if (key == null) {
            throw new NullPointerException("The key parameter is null.");
        }
        if (tx != null && !tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return getInternal(ds, tx, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> M get(Transaction tx, ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        Entity entity = get(tx, key);
        return modelMeta.entityToModel(entity);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param cache
     *            the cache of models. This cache can not be shared with
     *            multiple threads.
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the cache parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    @SuppressWarnings("unchecked")
    public static <M> M get(Transaction tx, ModelMeta<M> modelMeta, Key key,
            Map<Key, Object> cache) throws NullPointerException,
            IllegalStateException, EntityNotFoundRuntimeException {
        if (cache == null) {
            throw new NullPointerException("The cache parameter is null.");
        }
        M model = (M) cache.get(key);
        if (model != null) {
            return model;
        }
        model = get(tx, modelMeta, key);
        cache.put(key, model);
        return model;
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static List<Entity> get(Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException {
        return mapToList(keys, getAsMap(keys));
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static List<Entity> get(Key... keys)
            throws EntityNotFoundRuntimeException {
        return get(Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> List<M> get(ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException {
        return mapToList(modelMeta, keys, getAsMap(keys));
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> List<M> get(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, EntityNotFoundRuntimeException {
        return mapToList(modelMeta, Arrays.asList(keys), getAsMap(keys));
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static List<Entity> get(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException {
        return mapToList(keys, getAsMap(tx, keys));
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static List<Entity> get(Transaction tx, Key... keys)
            throws IllegalStateException, EntityNotFoundRuntimeException {
        return get(tx, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> List<M> get(Transaction tx, ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException, EntityNotFoundRuntimeException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return mapToList(modelMeta, keys, getAsMap(tx, keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> List<M> get(Transaction tx, ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return mapToList(modelMeta, Arrays.asList(keys), getAsMap(tx, keys));
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static Map<Key, Entity> getAsMap(Iterable<Key> keys)
            throws NullPointerException {
        if (keys == null) {
            throw new NullPointerException("The keys parameter is null.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return getInternal(ds, keys);
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the keys
     */
    public static Map<Key, Entity> getAsMap(Key... keys) {
        return getAsMap(Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static <M> Map<Key, M> getAsMap(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException {
        return mapToMap(modelMeta, getAsMap(keys));
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static <M> Map<Key, M> getAsMap(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException {
        return mapToMap(modelMeta, getAsMap(keys));
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Map<Key, Entity> getAsMap(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        if (keys == null) {
            throw new NullPointerException("The keys parameter is null.");
        }
        if (!tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return getInternal(ds, tx, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Map<Key, Entity> getAsMap(Transaction tx, Key... keys)
            throws IllegalStateException {
        return getAsMap(tx, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Map<Key, M> getAsMap(Transaction tx,
            ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return mapToMap(modelMeta, getAsMap(tx, keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Map<Key, M> getAsMap(Transaction tx,
            ModelMeta<M> modelMeta, Key... keys) throws NullPointerException,
            IllegalStateException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return mapToMap(modelMeta, getAsMap(tx, keys));
    }

    private static <M> List<M> mapToList(ModelMeta<M> modelMeta,
            Iterable<Key> keys, Map<Key, Entity> map)
            throws EntityNotFoundRuntimeException {
        List<M> list = new ArrayList<M>(map.size());
        for (Key key : keys) {
            Entity entity = map.get(key);
            if (entity == null) {
                throw new EntityNotFoundRuntimeException(key);
            }
            list.add(modelMeta.entityToModel(entity));
        }
        return list;
    }

    private static <M> Map<Key, M> mapToMap(ModelMeta<M> modelMeta,
            Map<Key, Entity> map) {
        Map<Key, M> modelMap = new HashMap<Key, M>(map.size());
        for (Key key : map.keySet()) {
            Entity entity = map.get(key);
            modelMap.put(key, modelMeta.entityToModel(entity));
        }
        return modelMap;
    }

    private static List<Entity> mapToList(Iterable<Key> keys,
            Map<Key, Entity> map) {
        List<Entity> list = new ArrayList<Entity>(map.size());
        for (Key key : keys) {
            Entity entity = map.get(key);
            if (entity != null) {
                list.add(entity);
            }
        }
        return list;
    }

    private static Entity getInternal(DatastoreService ds, Key key) {
        try {
            try {
                return ds.get(key);
            } catch (DatastoreTimeoutException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                for (int i = 0; i < MAX_RETRY; i++) {
                    try {
                        return ds.get(key);
                    } catch (DatastoreTimeoutException e2) {
                        logger.log(Level.WARNING, "Retry("
                            + i
                            + "): "
                            + e2.getMessage(), e2);
                    }
                }
                throw e;
            }
        } catch (EntityNotFoundException cause) {
            throw new EntityNotFoundRuntimeException(key, cause);
        }
    }

    private static Entity getInternal(DatastoreService ds, Transaction tx,
            Key key) {
        try {
            try {
                return ds.get(tx, key);
            } catch (DatastoreTimeoutException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                for (int i = 0; i < MAX_RETRY; i++) {
                    try {
                        return ds.get(tx, key);
                    } catch (DatastoreTimeoutException e2) {
                        logger.log(Level.WARNING, "Retry("
                            + i
                            + "): "
                            + e2.getMessage(), e2);
                    }
                }
                throw e;
            }
        } catch (EntityNotFoundException cause) {
            throw new EntityNotFoundRuntimeException(key, cause);
        }
    }

    private static Map<Key, Entity> getInternal(DatastoreService ds,
            Iterable<Key> keys) {
        try {
            return ds.get(keys);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return ds.get(keys);
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

    private static Map<Key, Entity> getInternal(DatastoreService ds,
            Transaction tx, Iterable<Key> keys) {
        try {
            return ds.get(tx, keys);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return ds.get(tx, keys);
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
     * Puts the entity to datastore. If there is a current transaction, this
     * operation will execute within that transaction.
     * 
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public static Key put(Entity entity) throws NullPointerException {
        if (entity == null) {
            throw new NullPointerException("The entity parameter is null.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return putInternal(ds, entity);
    }

    /**
     * Puts the model to datastore. If there is a current transaction, this
     * operation will execute within that transaction.
     * 
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     */
    public static Key put(Object model) throws NullPointerException {
        if (model == null) {
            throw new NullPointerException("The model parameter is null.");
        }
        ModelMeta<?> modelMeta = getModelMeta(model.getClass());
        modelMeta.incrementVersion(model);
        Entity entity = modelMeta.modelToEntity(model);
        Key key = put(entity);
        modelMeta.setKey(model, key);
        return key;
    }

    /**
     * Puts the entity to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Key put(Transaction tx, Entity entity)
            throws NullPointerException, IllegalStateException {
        if (entity == null) {
            throw new NullPointerException("The entity parameter is null.");
        }
        if (!tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return putInternal(ds, tx, entity);
    }

    /**
     * Puts the model to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Key put(Transaction tx, Object model)
            throws NullPointerException, IllegalStateException {
        if (model == null) {
            throw new NullPointerException("The model parameter is null.");
        }
        ModelMeta<?> modelMeta = getModelMeta(model.getClass());
        modelMeta.incrementVersion(model);
        Entity entity = modelMeta.modelToEntity(model);
        Key key = put(tx, entity);
        modelMeta.setKey(model, key);
        return key;
    }

    /**
     * Puts the models or entities to datastore. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws NullPointerException
     *             if the models parameter is null
     */
    public static List<Key> put(Iterable<?> models) throws NullPointerException {
        if (models == null) {
            throw new NullPointerException("The models parameter is null.");
        }
        List<ModelMeta<?>> modelMetaList = getModelMetaList(models);
        List<Key> keys =
            putEntities(incrementVersionAndConvertToEntities(
                models,
                modelMetaList));
        setKeys(models, keys, modelMetaList);
        return keys;
    }

    /**
     * Puts the models or entities to datastore. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys
     */
    public static List<Key> put(Object... models) {
        return put(Arrays.asList(models));
    }

    /**
     * Puts the models or entities to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws NullPointerException
     *             if the models parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static List<Key> put(Transaction tx, Iterable<?> models)
            throws NullPointerException, IllegalStateException {
        if (models == null) {
            throw new NullPointerException("The models parameter is null.");
        }
        List<ModelMeta<?>> modelMetaList = getModelMetaList(models);
        List<Key> keys =
            putEntities(tx, incrementVersionAndConvertToEntities(
                models,
                modelMetaList));
        setKeys(models, keys, modelMetaList);
        return keys;
    }

    /**
     * Puts the models or entities to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static List<Key> put(Transaction tx, Object... models)
            throws IllegalStateException {
        return put(tx, Arrays.asList(models));
    }

    private static List<ModelMeta<?>> getModelMetaList(Iterable<?> models)
            throws NullPointerException {
        List<ModelMeta<?>> list = new ArrayList<ModelMeta<?>>();
        for (Object model : models) {
            if (model == null) {
                throw new NullPointerException(
                    "The element of the models is null.");
            }
            if (model instanceof Entity) {
                list.add(null);
            } else {
                list.add(getModelMeta(model.getClass()));
            }
        }
        return list;
    }

    private static List<Entity> incrementVersionAndConvertToEntities(
            Iterable<?> models, List<ModelMeta<?>> modelMetaList)
            throws NullPointerException {
        List<Entity> entities = new ArrayList<Entity>();
        int i = 0;
        for (Object model : models) {
            ModelMeta<?> modelMeta = modelMetaList.get(i);
            if (modelMeta == null) {
                entities.add(Entity.class.cast(model));
            } else {
                modelMeta.incrementVersion(model);
                Entity entity = modelMeta.modelToEntity(model);
                entities.add(entity);
            }
            i++;
        }
        return entities;
    }

    private static void setKeys(Iterable<?> models, List<Key> keys,
            List<ModelMeta<?>> modelMetaList) {
        int i = 0;
        for (Object model : models) {
            ModelMeta<?> modelMeta = modelMetaList.get(i);
            if (modelMeta != null) {
                modelMeta.setKey(model, keys.get(i));
            }
            i++;
        }
    }

    private static List<Key> putEntities(Iterable<Entity> entities)
            throws NullPointerException {
        if (entities == null) {
            throw new NullPointerException("The entities parameter is null.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return putInternal(ds, entities);
    }

    private static List<Key> putEntities(Transaction tx,
            Iterable<Entity> entities) throws NullPointerException,
            IllegalArgumentException {
        if (entities == null) {
            throw new NullPointerException("The entities parameter is null.");
        }
        if (!tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return putInternal(ds, tx, entities);
    }

    private static Key putInternal(DatastoreService ds, Entity entity) {
        try {
            return ds.put(entity);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return ds.put(entity);
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

    private static Key putInternal(DatastoreService ds, Transaction tx,
            Entity entity) {
        try {
            return ds.put(tx, entity);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return ds.put(tx, entity);
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

    private static List<Key> putInternal(DatastoreService ds,
            Iterable<Entity> entities) {
        try {
            return ds.put(entities);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return ds.put(entities);
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

    private static List<Key> putInternal(DatastoreService ds, Transaction tx,
            Iterable<Entity> entities) {
        try {
            return ds.put(tx, entities);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    return ds.put(tx, entities);
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
     * Deletes entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static void delete(Iterable<Key> keys) throws NullPointerException {
        if (keys == null) {
            throw new NullPointerException("The keys parameter is null.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        deleteInternal(ds, keys);
    }

    /**
     * Deletes entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     */
    public static void delete(Key... keys) {
        delete(Arrays.asList(keys));
    }

    /**
     * Deletes entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static void delete(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        if (keys == null) {
            throw new NullPointerException("The keys parameter is null.");
        }
        if (!tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        deleteInternal(ds, tx, keys);
    }

    /**
     * Deletes entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static void delete(Transaction tx, Key... keys)
            throws IllegalStateException {
        delete(tx, Arrays.asList(keys));
    }

    private static void deleteInternal(DatastoreService ds, Iterable<Key> keys) {
        try {
            ds.delete(keys);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    ds.delete(keys);
                    return;
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

    private static void deleteInternal(DatastoreService ds, Transaction tx,
            Iterable<Key> keys) {
        try {
            ds.delete(tx, keys);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    ds.delete(tx, keys);
                    return;
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
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @return a {@link ModelQuery}
     * @see ModelQuery#ModelQuery(ModelMeta)
     */
    public static <M> ModelQuery<M> query(ModelMeta<M> modelMeta) {
        return new ModelQuery<M>(modelMeta);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @see ModelQuery#ModelQuery(ModelMeta, Key)
     */
    public static <M> ModelQuery<M> query(ModelMeta<M> modelMeta,
            Key ancestorKey) {
        return new ModelQuery<M>(modelMeta, ancestorKey);
    }

    /**
     * Returns a {@link EntityQuery}.
     * 
     * @param kind
     *            the kind
     * @return a {@link EntityQuery}
     * @see EntityQuery#EntityQuery(String)
     */
    public static EntityQuery query(String kind) {
        return new EntityQuery(kind);
    }

    /**
     * Returns a {@link EntityQuery}.
     * 
     * @param kind
     *            the kind
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link EntityQuery}
     * @see EntityQuery#EntityQuery(String, Key)
     */
    public static EntityQuery query(String kind, Key ancestorKey) {
        return new EntityQuery(kind, ancestorKey);
    }

    /**
     * Returns a {@link AllKindAncestorQuery}.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link AllKindAncestorQuery}
     */
    public static AllKindAncestorQuery query(Key ancestorKey) {
        return new AllKindAncestorQuery(ancestorKey);
    }

    /**
     * Returns a meta data of the model
     * 
     * @param modelClass
     *            the model class
     * @return a meta data of the model
     */
    protected static ModelMeta<?> getModelMeta(Class<?> modelClass) {
        if (!initialized) {
            initialize();
        }
        ModelMeta<?> modelMeta = modelMetaCache.get(modelClass.getName());
        if (modelMeta != null) {
            return modelMeta;
        }
        modelMeta = createModelMeta(modelClass);
        ModelMeta<?> old =
            modelMetaCache.putIfAbsent(modelClass.getName(), modelMeta);
        return old != null ? old : modelMeta;
    }

    /**
     * Creates a meta data of the model
     * 
     * @param modelClass
     *            the model class
     * @return a meta data of the model
     */
    protected static ModelMeta<?> createModelMeta(Class<?> modelClass) {
        try {
            String metaClassName =
                modelClass.getName().replace(".model.", ".meta.") + "Meta";
            return ClassUtil.newInstance(metaClassName, Thread
                .currentThread()
                .getContextClassLoader());
        } catch (Throwable cause) {
            throw new IllegalArgumentException("The meta data of the model("
                + modelClass.getName()
                + ") is not found.");
        }
    }

    /**
     * Filters the list.
     * 
     * @param <M>
     *            the model type
     * @param list
     *            the model list
     * @param criteria
     *            the filter criteria
     * @return the filtered list.
     * @throws NullPointerException
     *             if the list parameter is null or if the model is null
     */
    public static <M> List<M> filter(List<M> list, FilterCriterion... criteria)
            throws NullPointerException {
        if (list == null) {
            throw new NullPointerException("The list parameter is null.");
        }
        if (criteria.length == 0) {
            return list;
        }
        List<M> newList = new ArrayList<M>(list.size());
        for (M model : list) {
            if (model == null) {
                throw new NullPointerException("The model is null.");
            }
            if (accept(model, criteria)) {
                newList.add(model);
            }
        }
        return newList;
    }

    private static boolean accept(Object model, FilterCriterion... criteria) {
        for (FilterCriterion c : criteria) {
            if (c == null) {
                continue;
            }
            if (!c.accept(model)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sorts the list.
     * 
     * @param <M>
     *            the model type
     * @param list
     *            the model list
     * @param criteria
     *            criteria to sort
     * @return the sorted list
     * @throws NullPointerException
     *             if the list parameter is null
     */
    public static <M> List<M> sort(List<M> list, SortCriterion... criteria)
            throws NullPointerException {
        if (list == null) {
            throw new NullPointerException("The list parameter is null.");
        }
        if (criteria.length == 0) {
            return list;
        }
        Collections.sort(list, new AttributeComparator(criteria));
        return list;
    }

    private Datastore() {
    }
}