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
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slim3.util.ClassUtil;
import org.slim3.util.Cleanable;
import org.slim3.util.Cleaner;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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
        return DatastoreUtil.beginTransaction();
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
        DatastoreUtil.commit(tx);
    }

    /**
     * Rolls back the transaction.
     * 
     * @param tx
     *            the transaction
     * @throws NullPointerException
     *             if the tx parameter is null
     */
    public static void rollback(Transaction tx) throws NullPointerException {
        DatastoreUtil.rollback(tx);
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
     * @param modelClass
     *            the model class
     * @return a key within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static Key allocateId(Class<?> modelClass)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateId(getModelMeta(modelClass));
    }

    /**
     * Allocates a key within a namespace defined by the kind of the model.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @return a key within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static Key allocateId(ModelMeta<?> modelMeta)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateId(modelMeta.getKind());
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
     * @param modelClass
     *            the model class
     * @return a key within a namespace defined by the parent key and the kind
     *         of the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public static Key allocateId(Key parentKey, Class<?> modelClass)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateId(parentKey, getModelMeta(modelClass));
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
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateId(parentKey, modelMeta.getKind());
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
        return DatastoreUtil.allocateIds(kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model.
     * 
     * @param modelClass
     *            the model class
     * @param num
     *            the number of allocated keys
     * @return keys within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static KeyRange allocateIds(Class<?> modelClass, long num)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateIds(getModelMeta(modelClass), num);
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
        return allocateIds(modelMeta.getKind(), num);
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
        return DatastoreUtil.allocateIds(parentKey, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param num
     * @return keys within a namespace defined by the parent key and the kind of
     *         the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public static KeyRange allocateIds(Key parentKey, Class<?> modelClass,
            int num) throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateIds(parentKey, getModelMeta(modelClass), num);
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
        return allocateIds(parentKey, modelMeta.getKind(), num);
    }

    /**
     * Creates a key.
     * 
     * @param kind
     *            the kind of entity
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public static Key createKey(String kind, long id)
            throws NullPointerException {
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        return KeyFactory.createKey(kind, id);
    }

    /**
     * Creates a key.
     * 
     * @param modelClass
     *            the model class
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static Key createKey(Class<?> modelClass, long id)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return createKey(getModelMeta(modelClass), id);
    }

    /**
     * Creates a key.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static Key createKey(ModelMeta<?> modelMeta, long id)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return createKey(modelMeta.getKind(), id);
    }

    /**
     * Creates a key.
     * 
     * @param kind
     *            the kind of entity
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the kind parameter is null or if the name parameter is
     *             null
     */
    public static Key createKey(String kind, String name)
            throws NullPointerException {
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        return KeyFactory.createKey(kind, name);
    }

    /**
     * Creates a key.
     * 
     * @param modelClass
     *            the model class
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the modeClass parameter is null or if the name parameter
     *             is null
     */
    public static Key createKey(Class<?> modelClass, String name)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return createKey(getModelMeta(modelClass), name);
    }

    /**
     * Creates a key.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null
     */
    public static Key createKey(ModelMeta<?> modelMeta, String name)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return createKey(modelMeta.getKind(), name);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind of entity
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public static Key createKey(Key parentKey, String kind, long id)
            throws NullPointerException {
        if (parentKey == null) {
            throw new NullPointerException("The parentKey parameter is null.");
        }
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        return KeyFactory.createKey(parentKey, kind, id);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public static Key createKey(Key parentKey, Class<?> modelClass, long id)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return createKey(parentKey, getModelMeta(modelClass), id);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public static Key createKey(Key parentKey, ModelMeta<?> modelMeta, long id)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return createKey(parentKey, modelMeta.getKind(), id);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind of entity
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null or if the name parameter is null
     */
    public static Key createKey(Key parentKey, String kind, String name)
            throws NullPointerException {
        if (parentKey == null) {
            throw new NullPointerException("The parentKey parameter is null.");
        }
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        return KeyFactory.createKey(parentKey, kind, name);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the parentKey is null or if the modelClass parameter is
     *             null or if the name parameter is null
     */
    public static Key createKey(Key parentKey, Class<?> modelClass, String name)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return createKey(parentKey, getModelMeta(modelClass), name);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the parentKey is null or if the modelMeta parameter is
     *             null or if the name parameter is null
     */
    public static Key createKey(Key parentKey, ModelMeta<?> modelMeta,
            String name) throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return createKey(parentKey, modelMeta.getKind(), name);
    }

    /**
     * Converts the key to an encoded string.
     * 
     * @param key
     *            the key
     * @return an encoded string
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public static String keyToString(Key key) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException("The key parameter is null.");
        }
        return KeyFactory.keyToString(key);
    }

    /**
     * Converts the encoded string to a key.
     * 
     * @param encodedKey
     *            the encoded string
     * @return a key
     * @throws NullPointerException
     *             if the encodedKey parameter is null
     */
    public static Key stringToKey(String encodedKey)
            throws NullPointerException {
        if (encodedKey == null) {
            throw new NullPointerException("The encodedKey parameter is null.");
        }
        return KeyFactory.stringToKey(encodedKey);
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
        return DatastoreUtil.get(key);
    }

    /**
     * Returns a model specified by the key. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> M get(Class<M> modelClass, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        Entity entity = get(key);
        ModelMeta<M> modelMeta = getModelMeta(modelClass);
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
     * Returns a model specified by the key and checks the version. If there is
     * a current transaction, this operation will execute within that
     * transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the version
     *             parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public static <M> M get(Class<M> modelClass, Key key, Long version)
            throws NullPointerException, EntityNotFoundRuntimeException,
            ConcurrentModificationException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        ModelMeta<M> modelMeta = getModelMeta(modelClass);
        return get(modelMeta, key, version);
    }

    /**
     * Returns a model specified by the key and checks the version. If there is
     * a current transaction, this operation will execute within that
     * transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the version
     *             parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public static <M> M get(ModelMeta<M> modelMeta, Key key, Long version)
            throws NullPointerException, EntityNotFoundRuntimeException,
            ConcurrentModificationException {
        if (version == null) {
            throw new NullPointerException("The version parameter is null.");
        }
        M model = get(modelMeta, key);
        if (version != modelMeta.getVersion(model)) {
            throw new ConcurrentModificationException(
                "Failed optimistic lock by key("
                    + key
                    + ") and version("
                    + version
                    + ").");
        }
        return model;
    }

    /**
     * Returns a model specified by the key. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param cache
     *            the cache of models. This cache can not be shared with
     *            multiple threads.
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the cache parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> M get(Class<M> modelClass, Key key, Map<Key, M> cache)
            throws NullPointerException, EntityNotFoundRuntimeException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        ModelMeta<M> modelMeta = getModelMeta(modelClass);
        return get(modelMeta, key, cache);
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
    public static <M> M get(ModelMeta<M> modelMeta, Key key, Map<Key, M> cache)
            throws NullPointerException, EntityNotFoundRuntimeException {
        if (cache == null) {
            throw new NullPointerException("The cache parameter is null.");
        }
        M model = cache.get(key);
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
        return DatastoreUtil.get(tx, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> M get(Transaction tx, Class<M> modelClass, Key key)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        ModelMeta<M> modelMeta = getModelMeta(modelClass);
        return get(tx, modelMeta, key);
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
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public static <M> M get(Transaction tx, Class<M> modelClass, Key key,
            Long version) throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, ConcurrentModificationException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        ModelMeta<M> modelMeta = getModelMeta(modelClass);
        return get(tx, modelMeta, key, version);
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
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public static <M> M get(Transaction tx, ModelMeta<M> modelMeta, Key key,
            Long version) throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, ConcurrentModificationException {
        if (version == null) {
            throw new NullPointerException("The version parameter is null.");
        }
        M model = get(tx, modelMeta, key);
        if (version != modelMeta.getVersion(model)) {
            throw new ConcurrentModificationException(
                "Failed optimistic lock by key("
                    + key
                    + ") and version("
                    + version
                    + ").");
        }
        return model;
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param cache
     *            the cache of models. This cache can not be shared with
     *            multiple threads.
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the cache parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> M get(Transaction tx, Class<M> modelClass, Key key,
            Map<Key, M> cache) throws NullPointerException,
            IllegalStateException, EntityNotFoundRuntimeException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        ModelMeta<M> modelMeta = getModelMeta(modelClass);
        return get(tx, modelMeta, key, cache);
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
    public static <M> M get(Transaction tx, ModelMeta<M> modelMeta, Key key,
            Map<Key, M> cache) throws NullPointerException,
            IllegalStateException, EntityNotFoundRuntimeException {
        if (cache == null) {
            throw new NullPointerException("The cache parameter is null.");
        }
        M model = cache.get(key);
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
        return DatastoreUtil.get(keys);
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
        return DatastoreUtil.get(tx, keys);
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
            Map<Key, Entity> map) throws EntityNotFoundRuntimeException {
        List<Entity> list = new ArrayList<Entity>(map.size());
        for (Key key : keys) {
            Entity entity = map.get(key);
            if (entity == null) {
                throw new EntityNotFoundRuntimeException(key);
            }
            list.add(entity);
        }
        return list;
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
        return DatastoreUtil.put(entity);
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
        Entity entity = updatePropertiesAndConvertToEntity(modelMeta, model);
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
        return DatastoreUtil.put(tx, entity);
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
        Entity entity = updatePropertiesAndConvertToEntity(modelMeta, model);
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
            DatastoreUtil.put(updatePropertiesAndConvertToEntities(
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
            DatastoreUtil.put(tx, updatePropertiesAndConvertToEntities(
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

    private static Entity updatePropertiesAndConvertToEntity(
            ModelMeta<?> modelMeta, Object model) throws NullPointerException {
        modelMeta.incrementVersion(model);
        Entity entity = modelMeta.modelToEntity(model);
        if (modelMeta.getSimpleClassName() != null) {
            entity.setProperty(
                ModelMeta.SIMPLE_CLASS_NAME_RESERVED_PROPERTY,
                modelMeta.getSimpleClassName());
        }
        return entity;
    }

    private static List<Entity> updatePropertiesAndConvertToEntities(
            Iterable<?> models, List<ModelMeta<?>> modelMetaList)
            throws NullPointerException {
        List<Entity> entities = new ArrayList<Entity>();
        int i = 0;
        for (Object model : models) {
            ModelMeta<?> modelMeta = modelMetaList.get(i);
            if (modelMeta == null) {
                entities.add(Entity.class.cast(model));
            } else {
                Entity entity =
                    updatePropertiesAndConvertToEntity(modelMeta, model);
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
        DatastoreUtil.delete(keys);
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
        DatastoreUtil.delete(tx, keys);
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

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> ModelQuery<M> query(Class<M> modelClass)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return new ModelQuery<M>(getModelMeta(modelClass));
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static <M> ModelQuery<M> query(ModelMeta<M> modelMeta)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return new ModelQuery<M>(modelMeta);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the ancestorKey
     *             parameter is null
     */
    public static <M> ModelQuery<M> query(Class<M> modelClass, Key ancestorKey)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        if (ancestorKey == null) {
            throw new NullPointerException("The ancestorKey parameter is null.");
        }
        return new ModelQuery<M>(getModelMeta(modelClass), ancestorKey);
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
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the ancestorKey
     *             parameter is null
     */
    public static <M> ModelQuery<M> query(ModelMeta<M> modelMeta,
            Key ancestorKey) throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        if (ancestorKey == null) {
            throw new NullPointerException("The ancestorKey parameter is null.");
        }
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
     * Returns a {@link KindlessAncestorQuery}.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link KindlessAncestorQuery}
     */
    public static KindlessAncestorQuery query(Key ancestorKey) {
        return new KindlessAncestorQuery(ancestorKey);
    }

    /**
     * Returns a meta data of the model
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @return a meta data of the model
     */
    @SuppressWarnings("unchecked")
    public static <M> ModelMeta<M> getModelMeta(Class<M> modelClass) {
        if (!initialized) {
            initialize();
        }
        ModelMeta<M> modelMeta =
            (ModelMeta<M>) modelMetaCache.get(modelClass.getName());
        if (modelMeta != null) {
            return modelMeta;
        }
        modelMeta = createModelMeta(modelClass);
        ModelMeta<?> old =
            modelMetaCache.putIfAbsent(modelClass.getName(), modelMeta);
        return old != null ? (ModelMeta<M>) old : modelMeta;
    }

    /**
     * Creates a meta data of the model
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @return a meta data of the model
     */
    private static <M> ModelMeta<M> createModelMeta(Class<M> modelClass) {
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
     * Filters the list in memory.
     * 
     * @param <M>
     *            the model type
     * @param list
     *            the model list
     * @param criteria
     *            the filter criteria
     * @return the filtered list.
     * @throws NullPointerException
     *             if the list parameter is null or if the model of the list is
     *             null
     */
    public static <M> List<M> filterInMemory(List<M> list,
            FilterCriterion... criteria) throws NullPointerException {
        return DatastoreUtil.filterInMemory(list, Arrays.asList(criteria));
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
     *             if the list parameter is null or if the criteria parameter is
     *             null
     */
    public static <M> List<M> sortInMemory(List<M> list,
            SortCriterion... criteria) throws NullPointerException {
        return DatastoreUtil.sortInMemory(list, Arrays.asList(criteria));
    }

    private Datastore() {
    }
}