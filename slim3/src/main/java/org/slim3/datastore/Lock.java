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
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * A class to lock a target entity.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class Lock {

    /**
     * The kind of lock entity.
     */
    public static final String KIND = "slim3.Lock";

    /**
     * The globalTransactionKey property name.
     */
    public static final String GLOBAL_TRANSACTION_KEY_PROPERTY =
        "globalTransactionKey";

    /**
     * The timestamp property name.
     */
    public static final String TIMESTAMP_PROPERTY = "timestampType";

    /**
     * The timeout.
     */
    protected static final long TIMEOUT = 30 * 1000;

    /**
     * The asynchronous datastore service.
     */
    protected AsyncDatastoreService ds;

    /**
     * The key.
     */
    protected Key key;

    /**
     * The root key.
     */
    protected Key rootKey;

    /**
     * The time-stamp.
     */
    protected long timestamp;

    /**
     * The global transaction key.
     */
    protected Key globalTransactionKey;

    /**
     * Creates a key for lock.
     * 
     * @param rootKey
     *            the root key
     * @return a key
     * @throws NullPointerException
     *             if the targetKey parameter is null
     */
    public static Key createKey(Key rootKey) throws NullPointerException {
        if (rootKey == null) {
            throw new NullPointerException("The target key must not be null.");
        }
        if (rootKey.getParent() != null) {
            throw new IllegalArgumentException("The key("
                + rootKey
                + ") must be a root.");
        }
        return KeyFactory.createKey(rootKey, KIND, 1);
    }

    /**
     * Converts the entity to a {@link Lock}.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param entity
     *            an entity
     * 
     * @return a {@link Lock}
     * @throws NullPointerException
     *             if the ds parameter is null or if the entity property is null
     * @throws IllegalArgumentException
     *             if the kind of the entity is not slim3.Lock
     */
    public static Lock toLock(AsyncDatastoreService ds, Entity entity)
            throws NullPointerException, IllegalArgumentException {
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        if (!KIND.equals(entity.getKind())) {
            throw new IllegalArgumentException("The kind("
                + entity.getKind()
                + ") of the entity("
                + entity.getKey()
                + ") must be "
                + KIND
                + ".");
        }
        Key globalTransactionKey =
            (Key) entity.getProperty(GLOBAL_TRANSACTION_KEY_PROPERTY);
        Long timestamp = (Long) entity.getProperty(TIMESTAMP_PROPERTY);
        return new Lock(
            ds,
            globalTransactionKey,
            entity.getKey().getParent(),
            timestamp);
    }

    /**
     * Returns a {@link Lock} specified by the key. Returns null if no entity is
     * found.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * 
     * @return a {@link Lock} specified by the key
     * @throws NullPointerException
     *             if the ds parameter is null
     */
    public static Lock getOrNull(AsyncDatastoreService ds, Transaction tx,
            Key key) throws NullPointerException {
        Entity entity = DatastoreUtil.getOrNull(ds, tx, key);
        if (entity == null) {
            return null;
        }
        return toLock(ds, entity);
    }

    /**
     * Returns keys specified by the global transaction key.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @return a list of keys
     * @throws NullPointerException
     *             if the ds parameter is null or if the globalTransactionKey
     *             parameter is null
     */
    public static List<Key> getKeys(AsyncDatastoreService ds,
            Key globalTransactionKey) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        return new EntityQuery(ds, KIND).filter(
            GLOBAL_TRANSACTION_KEY_PROPERTY,
            FilterOperator.EQUAL,
            globalTransactionKey).asKeyList();
    }

    /**
     * Deletes entities specified by the global transaction key in transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the ds parameter is null or if the globalTransactionKey
     *             parameter is null
     * 
     */
    public static void deleteInTx(AsyncDatastoreService ds,
            Key globalTransactionKey) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        for (Key key : getKeys(ds, globalTransactionKey)) {
            deleteInTx(ds, globalTransactionKey, key);
        }
    }

    /**
     * Deletes the locks from the datastore in transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @param locks
     *            the locks
     * @throws NullPointerException
     *             if the ds parameter is null or if the globalTransactionKey
     *             parameter is null or if the locks parameter is null
     * 
     */
    public static void deleteInTx(AsyncDatastoreService ds,
            Key globalTransactionKey, Iterable<Lock> locks)
            throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (locks == null) {
            throw new NullPointerException(
                "The locks parameter must not be null.");
        }
        for (Lock lock : locks) {
            deleteInTx(ds, globalTransactionKey, lock.key);
        }
    }

    /**
     * Deletes an entity specified by the key in transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @param key
     *            the key
     * 
     * @throws NullPointerException
     *             if the ds parameter is null or if the globalTransactionKey
     *             parameter is null or if the key parameter is null
     */
    protected static void deleteInTx(AsyncDatastoreService ds,
            Key globalTransactionKey, Key key) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        try {
            Lock lock = getOrNull(ds, tx, key);
            if (lock != null
                && globalTransactionKey.equals(lock.globalTransactionKey)) {
                DatastoreUtil.delete(ds, tx, key);
                tx.commit();
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Deletes entities specified by the global transaction key without
     * transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the ds parameter is null or if the globalTransactionKey
     *             parameter is null
     */
    public static void deleteWithoutTx(AsyncDatastoreService ds,
            Key globalTransactionKey) throws NullPointerException {
        DatastoreUtil.delete(ds, null, getKeys(ds, globalTransactionKey));
    }

    /**
     * Deletes the locks without transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param locks
     *            the locks
     * @throws NullPointerException
     *             if the ds parameter is null or if the locks parameter is null
     */
    public static void deleteWithoutTx(AsyncDatastoreService ds,
            Iterable<Lock> locks) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (locks == null) {
            throw new NullPointerException(
                "The locks parameter must not be null.");
        }
        List<Key> keys = new ArrayList<Key>();
        for (Lock lock : locks) {
            keys.add(lock.key);
        }
        if (keys.isEmpty()) {
            return;
        }
        DatastoreUtil.delete(ds, null, keys);
    }

    /**
     * Verifies lock specified by the root key and returns entities specified by
     * the keys as map.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param rootKey
     *            the root key
     * @param keys
     *            the keys
     * @return an entity
     * @throws NullPointerException
     *             if the ds parameter is null or if the tx parameter is null or
     *             if the rootKey parameter is null or if the keys parameter is
     *             null
     * 
     * @throws ConcurrentModificationException
     *             if locking the entity group failed
     */
    public static Map<Key, Entity> verifyAndGetAsMap(AsyncDatastoreService ds,
            Transaction tx, Key rootKey, Collection<Key> keys)
            throws NullPointerException, ConcurrentModificationException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (tx == null) {
            throw new NullPointerException("The tx parameter must not be null.");
        }
        if (rootKey == null) {
            throw new NullPointerException(
                "The rootKey parameter must not be null.");
        }
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        Key lockKey = createKey(rootKey);
        List<Key> keyList = new ArrayList<Key>(keys.size() + 1);
        keyList.addAll(keys);
        keyList.add(lockKey);
        Map<Key, Entity> map = DatastoreUtil.getAsMap(ds, tx, keyList);
        if (map.containsKey(lockKey)) {
            throw createConcurrentModificationException(rootKey);
        }
        return map;
    }

    /**
     * Creates a {@link ConcurrentModificationException}.
     * 
     * @param rootKey
     *            the root key
     * 
     * @return a {@link ConcurrentModificationException}
     * @throws NullPointerException
     *             if the cause parameter is null
     */
    protected static ConcurrentModificationException createConcurrentModificationException(
            Key rootKey) throws NullPointerException {
        if (rootKey == null) {
            throw new NullPointerException(
                "The rootKey parameter must not be null.");
        }
        return new ConcurrentModificationException("Locking the entity group("
            + rootKey
            + ") failed.");
    }

    /**
     * Creates a {@link ConcurrentModificationException}.
     * 
     * @param rootKey
     *            the root key
     * @param cause
     *            the cause
     * 
     * @return a {@link ConcurrentModificationException}
     * @throws NullPointerException
     *             if the rootKey parameter is null or if the cause parameter is
     *             null
     */
    protected static ConcurrentModificationException createConcurrentModificationException(
            Key rootKey, Throwable cause) throws NullPointerException {
        if (rootKey == null) {
            throw new NullPointerException(
                "The rootKey parameter must not be null.");
        }
        if (cause == null) {
            throw new NullPointerException(
                "The cause parameter must not be null.");
        }
        ConcurrentModificationException cme =
            new ConcurrentModificationException("Locking the entity group("
                + rootKey
                + ") failed.");
        cme.initCause(cause);
        return cme;
    }

    /**
     * Constructor.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @param rootKey
     *            the root key
     * @param timestamp
     *            the time-stamp
     * 
     * @throws NullPointerException
     *             if the ds parameter is null or if the rootKey parameter is
     *             null or if the timestamp parameter is null or if the
     *             globalTransactionKey parameter is null
     */
    public Lock(AsyncDatastoreService ds, Key globalTransactionKey,
            Key rootKey, Long timestamp) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTranactionKey parameter must not be null.");
        }
        if (rootKey == null) {
            throw new NullPointerException(
                "The rootKey parameter must not be null.");
        }
        if (timestamp == null) {
            throw new NullPointerException(
                "The timestamp parameter must not be null.");
        }
        this.ds = ds;
        this.globalTransactionKey = globalTransactionKey;
        this.key = createKey(rootKey);
        this.rootKey = rootKey;
        this.timestamp = timestamp;
    }

    /**
     * Converts this instance to an entity.
     * 
     * @return an entity
     */
    public Entity toEntity() {
        Entity entity = new Entity(key);
        entity.setProperty(
            GLOBAL_TRANSACTION_KEY_PROPERTY,
            globalTransactionKey);
        entity.setProperty(TIMESTAMP_PROPERTY, timestamp);
        return entity;
    }

    /**
     * Locks the entity group.
     * 
     * @throws ConcurrentModificationException
     *             if locking the entity failed
     */
    public void lock() throws ConcurrentModificationException {
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        try {
            Lock other = getOrNull(ds, tx, key);
            if (other != null) {
                verify(other);
            }
            DatastoreUtil.put(ds, tx, toEntity());
            tx.commit();
            return;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Locks the entity group and returns entities specified by the target keys.
     * 
     * @param targetKeys
     *            the target keys
     * @return an entity specified by the target keys
     * @throws NullPointerException
     *             if the targetKeys parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity failed
     */
    public Map<Key, Entity> lockAndGetAsMap(Collection<Key> targetKeys)
            throws NullPointerException, ConcurrentModificationException {
        Map<Key, Entity> map = null;
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        try {
            List<Key> keyList = new ArrayList<Key>(targetKeys.size() + 1);
            keyList.addAll(targetKeys);
            keyList.add(key);
            map = DatastoreUtil.getAsMap(ds, tx, keyList);
            Entity otherEntity = map.remove(key);
            if (otherEntity != null) {
                Lock other = toLock(ds, otherEntity);
                verify(other);
            }
            DatastoreUtil.put(ds, tx, toEntity());
            tx.commit();
            return map;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Verifies the other {@link Lock}.
     * 
     * @param other
     *            the other {@link Lock}
     * @throws NullPointerException
     *             if the other parameter is null
     * @throws ConcurrentModificationException
     *             if the entity group is locked by the other one
     */
    protected void verify(Lock other) throws NullPointerException,
            ConcurrentModificationException {
        if (other == null) {
            throw new NullPointerException(
                "The other parameter must not be null.");
        }
        if (globalTransactionKey.equals(other.globalTransactionKey)) {
            return;
        }
        if (timestamp <= other.getTimestamp() + TIMEOUT) {
            throw createConcurrentModificationException(rootKey);
        }
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        try {
            GlobalTransaction gtx =
                GlobalTransaction.getOrNull(ds, tx, other.globalTransactionKey);
            if (gtx != null) {
                if (gtx.valid) {
                    throw createConcurrentModificationException(rootKey);
                }
                return;
            }
            gtx = new GlobalTransaction(ds, other.globalTransactionKey, false);
            GlobalTransaction.put(ds, tx, gtx);
            tx.commit();
        } catch (ConcurrentModificationException e) {
            throw createConcurrentModificationException(rootKey, e);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Returns the global transaction key.
     * 
     * @return the global transaction key
     */
    public Key getGlobalTransactionKey() {
        return globalTransactionKey;
    }

    /**
     * Returns the key.
     * 
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Returns the root key.
     * 
     * @return the root key
     */
    public Key getRootKey() {
        return rootKey;
    }

    /**
     * Returns the time-stamp.
     * 
     * @return the time-stamp
     */
    public long getTimestamp() {
        return timestamp;
    }
}