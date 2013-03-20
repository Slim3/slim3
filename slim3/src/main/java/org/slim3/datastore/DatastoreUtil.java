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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.slim3.util.ClassUtil;
import org.slim3.util.Cleanable;
import org.slim3.util.Cleaner;
import org.slim3.util.FutureUtil;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.KeyUtil;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;
import com.google.storage.onestore.v3.OnestoreEntity.EntityProto;
import com.google.storage.onestore.v3.OnestoreEntity.Path.Element;
import com.google.storage.onestore.v3.OnestoreEntity.Reference;

/**
 * A utility for {@link DatastoreService}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class DatastoreUtil {

    /**
     * The maximum size(bytes) of entity.
     */
    public static final int MAX_ENTITY_SIZE = 1000000;

    /**
     * The maximum number of entities.
     */
    public static final int MAX_NUMBER_OF_ENTITIES = 500;

    /**
     * The extra size.
     */
    public static final int EXTRA_SIZE = 200;

    private static final int KEY_CACHE_SIZE = 50;

    /**
     * The cache for {@link ModelMeta}.
     */
    protected static ConcurrentHashMap<String, ModelMeta<?>> modelMetaCache =
        new ConcurrentHashMap<String, ModelMeta<?>>(87);

    /**
     * The cache for the result of allocateIds().
     */
    protected static ConcurrentHashMap<String, Iterator<Key>> keysCache =
        new ConcurrentHashMap<String, Iterator<Key>>(87);

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
     * Clears the active transactions.
     */
    public static void clearActiveGlobalTransactions() {
        GlobalTransaction.clearActiveTransactions();
    }

    /**
     * Clears the keys cache.
     */
    public static void clearKeysCache() {
        keysCache.clear();
    }

    /**
     * Begins a transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @return a begun transaction
     */
    public static Transaction beginTransaction(AsyncDatastoreService ds) {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        return FutureUtil.getQuietly(ds.beginTransaction());
    }

    /**
     * Allocates a key within a namespace defined by the kind with caching.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param kind
     *            the kind
     * @return a key within a namespace defined by the kind
     * @throws NullPointerException
     *             if the ds parameter is null or if the kind parameter is null
     */
    public static Key allocateId(AsyncDatastoreService ds, String kind)
            throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (kind == null) {
            throw new NullPointerException(
                "The kind parameter must not be null.");
        }
        Iterator<Key> keys = keysCache.get(kind);
        if (keys != null && keys.hasNext()) {
            return keys.next();
        }
        keys =
            FutureUtil
                .getQuietly(allocateIdsAsync(ds, kind, KEY_CACHE_SIZE))
                .iterator();
        keysCache.put(kind, keys);
        return keys.next();
    }

    /**
     * Allocates a key within a namespace defined by the parentKey and the kind.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @return a key within a namespace defined by the kind
     * @throws NullPointerException
     *             if the ds parameter is null or if the parentKey parameter is
     *             null or if the kind parameter is null
     */
    public static Key allocateId(AsyncDatastoreService ds, Key parentKey,
            String kind) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (parentKey == null) {
            throw new NullPointerException(
                "The parentKey parameter must not be null.");
        }
        if (kind == null) {
            throw new NullPointerException(
                "The kind parameter must not be null.");
        }
        Iterator<Key> keys =
            FutureUtil
                .getQuietly(allocateIdsAsync(ds, parentKey, kind, 1))
                .iterator();
        return keys.next();
    }

    /**
     * Allocates keys within a namespace defined by the kind asynchronously.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param kind
     *            the kind
     * @param num
     *            the number of allocated keys
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the ads parameter is null or if the kind parameter is null
     */
    public static Future<KeyRange> allocateIdsAsync(AsyncDatastoreService ds,
            String kind, long num) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (kind == null) {
            throw new NullPointerException(
                "The kind parameter must not be null.");
        }
        return ds.allocateIds(kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * asynchronously.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @param num
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the ds parameter is null or if the parentKey parameter is
     *             null or if the kind parameter is null
     */
    public static Future<KeyRange> allocateIdsAsync(AsyncDatastoreService ds,
            Key parentKey, String kind, long num) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (parentKey == null) {
            throw new NullPointerException(
                "The parentKey parameter must not be null.");
        }
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        return ds.allocateIds(parentKey, kind, num);
    }

    /**
     * Assigns a new key to the entity if necessary.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param entity
     *            the entity
     * @throws NullPointerException
     *             if the ds parameter is null or if the entity parameter is
     *             null
     */
    public static void assignKeyIfNecessary(AsyncDatastoreService ds,
            Entity entity) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        if (!entity.getKey().isComplete()) {
            long id =
                entity.getParent() == null ? allocateId(ds, entity.getKind())
                    .getId() : allocateId(
                    ds,
                    entity.getParent(),
                    entity.getKind()).getId();
            KeyUtil.setId(entity.getKey(), id);
        }
    }

    /**
     * Assigns a new key to the entity if necessary.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param entities
     *            the entities
     * @throws NullPointerException
     *             if the ds parameter is null or if the entities parameter is
     *             null
     */
    public static void assignKeyIfNecessary(AsyncDatastoreService ds,
            Iterable<Entity> entities) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (entities == null) {
            throw new NullPointerException(
                "The entities parameter must not be null.");
        }
        for (Entity e : entities) {
            assignKeyIfNecessary(ds, e);
        }
    }

    /**
     * Returns entities specified by the keys as map within the provided
     * transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity
     * @throws NullPointerException
     *             if the ds parameter is null or if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity is found
     */
    public static Entity get(AsyncDatastoreService ds, Transaction tx, Key key)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException {
        Entity entity = getOrNull(ds, tx, key);
        if (entity == null) {
            throw new EntityNotFoundRuntimeException(key);
        }
        return entity;
    }

    /**
     * Returns entities specified by the keys as map within the provided
     * transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity
     * @throws NullPointerException
     *             if the ds parameter is null or if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Entity getOrNull(AsyncDatastoreService ds, Transaction tx,
            Key key) throws NullPointerException, IllegalStateException {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        return FutureUtil
            .getQuietly(getAsMapAsync(ds, tx, Arrays.asList(key)))
            .get(key);
    }

    /**
     * Returns entities specified by the keys as map within the provided
     * transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities
     * @throws NullPointerException
     *             if the ds parameter is null or if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Map<Key, Entity> getAsMap(AsyncDatastoreService ds,
            Transaction tx, Iterable<Key> keys) throws NullPointerException,
            IllegalStateException {
        return FutureUtil.getQuietly(getAsMapAsync(ds, tx, keys));
    }

    /**
     * Returns entities specified by the keys as map within the provided
     * transaction asynchronously.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the ds parameter is null or if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Map<Key, Entity>> getAsMapAsync(
            AsyncDatastoreService ds, Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        if (tx != null && !tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        return ds.get(tx, keys);
    }

    /**
     * Puts the entity to datastore within the provided transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param entity
     *            the entity
     * 
     * @return a list of keys
     * @throws NullPointerException
     *             if the ds parameter is null or if the entity parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Key put(AsyncDatastoreService ds, Transaction tx,
            Entity entity) throws NullPointerException, IllegalStateException {
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        List<Key> list = put(ds, tx, Arrays.asList(entity));
        return list.get(0);
    }

    /**
     * Puts the entities to datastore within the provided transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param entities
     *            the entities
     * 
     * @return a list of keys
     * @throws NullPointerException
     *             if the ds parameter is null or if the entities parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static List<Key> put(AsyncDatastoreService ds, Transaction tx,
            Iterable<Entity> entities) throws NullPointerException,
            IllegalStateException {
        return FutureUtil.getQuietly(putAsync(ds, tx, entities));
    }

    /**
     * Puts the entity to datastore within the provided transaction
     * asynchronously.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param entity
     *            the entity
     * 
     * @return a list of keys represented as {@link Future}
     * @throws NullPointerException
     *             if the ds parameter is null or if the entity parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Key putAsync(AsyncDatastoreService ds, Transaction tx,
            Entity entity) throws NullPointerException, IllegalStateException {
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        List<Key> list = put(ds, tx, Arrays.asList(entity));
        return list.get(0);
    }

    /**
     * Puts the entities to datastore within the provided transaction
     * asynchronously.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param entities
     *            the entities
     * 
     * @return a list of keys represented as {@link Future}
     * @throws NullPointerException
     *             if the ds parameter is null or if the entities parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<List<Key>> putAsync(AsyncDatastoreService ds,
            Transaction tx, Iterable<Entity> entities)
            throws NullPointerException, IllegalStateException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (entities == null) {
            throw new NullPointerException(
                "The entities parameter must not be null.");
        }
        if (tx != null && !tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        assignKeyIfNecessary(ds, entities);
        return ds.put(tx, entities);
    }

    /**
     * Deletes the entity specified by the key within the provided transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @throws NullPointerException
     *             if the ds parameter is null or if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static void delete(AsyncDatastoreService ds, Transaction tx, Key key)
            throws NullPointerException, IllegalStateException {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        FutureUtil.getQuietly(deleteAsync(ds, tx, Arrays.asList(key)));
    }

    /**
     * Deletes entities specified by the keys within the provided transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the ds parameter is null or if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static void delete(AsyncDatastoreService ds, Transaction tx,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException {
        FutureUtil.getQuietly(deleteAsync(ds, tx, keys));
    }

    /**
     * Deletes entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the ds parameter is null or if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Void> deleteAsync(AsyncDatastoreService ds,
            Transaction tx, Iterable<Key> keys) throws NullPointerException,
            IllegalStateException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        if (tx != null && !tx.isActive()) {
            throw new IllegalStateException("The transaction must be active.");
        }
        return ds.delete(tx, keys);
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
     *             if the list parameter is null or if the criteria parameter is
     *             null or if the model of the list is null
     */
    public static <M> List<M> filterInMemory(List<M> list,
            List<? extends InMemoryFilterCriterion> criteria)
            throws NullPointerException {
        if (list == null) {
            throw new NullPointerException(
                "The list parameter must not be null.");
        }
        if (criteria == null) {
            throw new NullPointerException(
                "The criteria parameter must not be null.");
        }
        if (criteria.size() == 0) {
            return list;
        }
        List<M> newList = new ArrayList<M>(list.size());
        for (M model : list) {
            if (model == null) {
                throw new NullPointerException(
                    "The element of list must not be null.");
            }
            if (accept(model, criteria)) {
                newList.add(model);
            }
        }
        return newList;
    }

    private static boolean accept(Object model,
            List<? extends InMemoryFilterCriterion> criteria) {
        for (InMemoryFilterCriterion c : criteria) {
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
     * Sorts the list in memory.
     * 
     * @param <M>
     *            the model type
     * @param list
     *            the model list
     * @param criteria
     *            criteria to sort
     * @return the sorted list
     * @throws NullPointerException
     *             if the list parameter is null of if the criteria parameter is
     *             null
     */
    public static <M> List<M> sortInMemory(List<M> list,
            List<InMemorySortCriterion> criteria) throws NullPointerException {
        if (list == null) {
            throw new NullPointerException(
                "The list parameter must not be null.");
        }
        if (criteria == null) {
            throw new NullPointerException(
                "The criteria parameter must not be null.");
        }
        if (criteria.size() == 0) {
            return list;
        }
        Collections.sort(list, new AttributeComparator(criteria));
        return list;
    }

    /**
     * Returns a meta data of the model
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @return a meta data of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    @SuppressWarnings("unchecked")
    public static <M> ModelMeta<M> getModelMeta(Class<M> modelClass)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException(
                "The modelClass parameter must not be null.");
        }
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
     * Returns a meta data of the model
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param entity
     *            the entity
     * @return a meta data of the model
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the entity parameter
     *             is null
     * @throws IllegalArgumentException
     *             if the model class is not assignable from entity class
     */
    @SuppressWarnings("unchecked")
    public static <M> ModelMeta<M> getModelMeta(ModelMeta<M> modelMeta,
            Entity entity) throws NullPointerException,
            IllegalArgumentException {
        if (modelMeta == null) {
            throw new NullPointerException(
                "The modelMeta parameter must not be null.");
        }
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        List<String> classHierarchyList =
            (List<String>) entity.getProperty(modelMeta
                .getClassHierarchyListName());
        if (classHierarchyList == null) {
            return modelMeta;
        }
        Class<M> subModelClass =
            ClassUtil
                .forName(classHierarchyList.get(classHierarchyList.size() - 1));
        if (!modelMeta.getModelClass().isAssignableFrom(subModelClass)) {
            throw new IllegalArgumentException("The model class("
                + modelMeta.getModelClass().getName()
                + ") is not assignable from entity class("
                + subModelClass.getName()
                + ").");
        }
        return getModelMeta(subModelClass);
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
    public static <M> ModelMeta<M> createModelMeta(Class<M> modelClass) {
        try {
            String className = modelClass.getName();
            className = replacePackageName(className, "model", "meta");
            className =
                replacePackageName(className, "shared", "server") + "Meta";
            return ClassUtil.newInstance(className, Thread
                .currentThread()
                .getContextClassLoader());
        } catch (Throwable cause) {
            throw new IllegalArgumentException("The meta data of the model("
                + modelClass.getName()
                + ") is not found.");
        }
    }

    /**
     * Replaces a package name with another one.
     * 
     * @param className
     *            the class name
     * @param fromPackageName
     *            the "from" package name
     * @param toPackageName
     *            the "to" package name
     * @return the converted class name
     * @throws NullPointerException
     *             if the className parameter is null or if the fromPackageName
     *             is null or if the toPackageName parameter is null
     */
    protected static String replacePackageName(String className,
            String fromPackageName, String toPackageName)
            throws NullPointerException {
        if (className == null) {
            throw new NullPointerException(
                "The className parameter must not be null.");
        }
        if (fromPackageName == null) {
            throw new NullPointerException(
                "The fromPackageName parameter must not be null.");
        }
        if (toPackageName == null) {
            throw new NullPointerException(
                "The toPackageName parameter must not be null.");
        }
        int index = className.lastIndexOf("." + fromPackageName + ".");
        if (index < 0) {
            return className;
        }
        return className.substring(0, index)
            + "."
            + toPackageName
            + "."
            + className.substring(index + fromPackageName.length() + 2);
    }

    /**
     * Converts the entity to an array of bytes.
     * 
     * @param entity
     *            the entity
     * @return an array of bytes
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public static byte[] entityToBytes(Entity entity)
            throws NullPointerException {
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        EntityProto pb = EntityTranslator.convertToPb(entity);
        byte[] buf = new byte[pb.encodingSize()];
        pb.outputTo(buf, 0);
        return buf;
    }

    /**
     * Converts the array of bytes to an entity.
     * 
     * @param bytes
     *            the array of bytes
     * @return an entity
     * @throws NullPointerException
     *             if the bytes parameter is null
     */
    public static Entity bytesToEntity(byte[] bytes)
            throws NullPointerException {
        if (bytes == null) {
            throw new NullPointerException(
                "The bytes parameter must not be null.");
        }
        EntityProto pb = new EntityProto();
        pb.mergeFrom(bytes);
        return EntityTranslator.createFromPb(pb);
    }

    /**
     * Converts the reference to a key.
     * 
     * @param reference
     *            the reference object
     * @return a key
     * @throws NullPointerException
     *             if the reference parameter is null
     */
    public static Key referenceToKey(Reference reference)
            throws NullPointerException {
        if (reference == null) {
            throw new NullPointerException(
                "The reference parameter must not be null.");
        }
        Key key = null;
        for (Element e : reference.getPath().elements()) {
            String kind = e.getType();
            long id = e.getId();
            String name = e.getName();
            if (key == null) {
                if (id != 0) {
                    key = KeyFactory.createKey(kind, id);
                } else {
                    key = KeyFactory.createKey(kind, name);
                }
            } else {
                if (id != 0) {
                    key = KeyFactory.createKey(key, kind, id);
                } else {
                    key = KeyFactory.createKey(key, kind, name);
                }
            }
        }
        if (key == null) {
            throw new IllegalArgumentException("The reference("
                + reference
                + ") cannot be converted to Key.");
        }
        return key;
    }

    /**
     * Converts the model to an entity.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param model
     *            the model
     * @return an entity
     * @throws NullPointerException
     *             if the ds parameter is null or if the model parameter is null
     */
    public static Entity modelToEntity(AsyncDatastoreService ds, Object model)
            throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (model == null) {
            throw new NullPointerException(
                "The model parameter must not be null.");
        }
        ModelMeta<?> modelMeta = getModelMeta(model.getClass());
        Key key = modelMeta.getKey(model);
        if (key == null) {
            key = allocateId(ds, modelMeta.getKind());
            modelMeta.setKey(model, key);
        }
        modelMeta.assignKeyToModelRefIfNecessary(ds, model);
        modelMeta.incrementVersion(model);
        modelMeta.prePut(model);
        return modelMeta.modelToEntity(model);
    }

    /**
     * Converts the models to entities.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param models
     *            the models
     * @return entities
     * @throws NullPointerException
     *             if the ds parameter is null or if the models parameter is
     *             null
     */
    public static List<Entity> modelsToEntities(AsyncDatastoreService ds,
            Iterable<?> models) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (models == null) {
            throw new NullPointerException(
                "The models parameter must not be null.");
        }
        List<Entity> entities = new ArrayList<Entity>();
        for (Object model : models) {
            if (model instanceof Entity) {
                Entity entity = (Entity) model;
                assignKeyIfNecessary(ds, entity);
                entities.add(entity);
            } else {
                entities.add(modelToEntity(ds, model));
            }
        }
        return entities;
    }

    /**
     * Converts the map of entities to a list of entities.
     * 
     * @param keys
     *            the keys
     * @param map
     *            the map of entities
     * @return a list of entities
     * @throws NullPointerException
     *             if the keys parameter is null or if the map parameter is null
     *             or if the element of keys is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity bound to a key is found
     */
    public static List<Entity> entityMapToEntityList(Iterable<Key> keys,
            Map<Key, Entity> map) throws NullPointerException,
            EntityNotFoundRuntimeException {
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        if (map == null) {
            throw new NullPointerException(
                "The map parameter must not be null.");
        }
        List<Entity> list = new ArrayList<Entity>(map.size());
        for (Key key : keys) {
            if (key == null) {
                throw new NullPointerException(
                    "The element of keys must not be null.");
            }
            Entity entity = map.get(key);
            if (entity == null) {
                throw new EntityNotFoundRuntimeException(key);
            }
            list.add(entity);
        }
        return list;
    }

    /**
     * Converts the map of entities to a list of models.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @param map
     *            the map of entities
     * @return a list of models
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null or if the map parameter is null or if the element of
     *             keys is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity bound to a key is found
     */
    public static <M> List<M> entityMapToModelList(ModelMeta<M> modelMeta,
            Iterable<Key> keys, Map<Key, Entity> map)
            throws NullPointerException, EntityNotFoundRuntimeException {
        if (modelMeta == null) {
            throw new NullPointerException(
                "The modelMeta parameter must not be null.");
        }
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        if (map == null) {
            throw new NullPointerException(
                "The map parameter must not be null.");
        }
        List<M> list = new ArrayList<M>(map.size());
        for (Key key : keys) {
            if (key == null) {
                throw new NullPointerException(
                    "The element of keys must not be null.");
            }
            Entity entity = map.get(key);
            if (entity == null) {
                throw new EntityNotFoundRuntimeException(key);
            }
            ModelMeta<M> mm = getModelMeta(modelMeta, entity);
            mm.validateKey(key);
            M model = mm.entityToModel(entity);
            mm.postGet(model);
            list.add(model);
        }
        return list;
    }

    /**
     * Converts the map of entities to a map of models.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param map
     *            the map of entities
     * @return a map of models
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the map parameter is
     *             null
     */
    public static <M> Map<Key, M> entityMapToModelMap(ModelMeta<M> modelMeta,
            Map<Key, Entity> map) throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException(
                "The modelMeta parameter must not be null.");
        }
        if (map == null) {
            throw new NullPointerException(
                "The map parameter must not be null.");
        }
        Map<Key, M> modelMap = new HashMap<Key, M>(map.size());
        for (Key key : map.keySet()) {
            Entity entity = map.get(key);
            ModelMeta<M> mm = getModelMeta(modelMeta, entity);
            mm.validateKey(key);
            M model = mm.entityToModel(entity);
            mm.postGet(model);
            modelMap.put(key, model);
        }
        return modelMap;
    }

    /**
     * Returns a root key.
     * 
     * @param key
     *            the key
     * @return a root key
     */
    public static Key getRoot(Key key) {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        while (key != null) {
            Key parent = key.getParent();
            if (parent == null) {
                break;
            }
            key = parent;
        }
        return key;
    }

    private DatastoreUtil() {
    }

    /**
     * Converts the array of filter criterion to the list of filter.
     * 
     * @param modelMeta
     *            the model meta
     * @param criteria
     *            the filter criteria
     * @return the list of filter
     */
    protected static List<Filter> toFilters(ModelMeta<?> modelMeta,
            FilterCriterion... criteria) {
        List<Filter> list = new ArrayList<Filter>(criteria.length);
        for (FilterCriterion c : criteria) {
            if (c == null) {
                throw new NullPointerException(
                    "The element of the criteria parameter must not be null.");
            }
            for (Query.Filter f : c.getFilters()) {
                if (f instanceof Query.FilterPredicate) {
                    Query.FilterPredicate fp = (Query.FilterPredicate) f;
                    Object value = fp.getValue();
                    if (modelMeta.isCipherProperty(fp.getPropertyName())) {
                        if (value instanceof String) {
                            value = modelMeta.encrypt((String) value);
                        } else if (value instanceof Text) {
                            value = modelMeta.encrypt((Text) value);
                        }
                        f =
                            new Query.FilterPredicate(
                                fp.getPropertyName(),
                                fp.getOperator(),
                                value);
                    }
                }
                list.add(f);
            }
        }
        return list;
    }
}