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
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.slim3.util.ListUtil;

import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * A class to lock a target entity.
 * 
 * @author higa
 * @since 3.0
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
     * The maximum size of locks.
     */
    protected static final int MAX_SIZE_LOCKS = 100;

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
     * @param entity
     *            an entity
     * @return a {@link Lock}
     * @throws NullPointerException
     *             if the entity property is null
     * @throws IllegalArgumentException
     *             if the kind of the entity is not slim3.Lock
     */
    public static Lock toLock(Entity entity) throws NullPointerException,
            IllegalArgumentException {
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
            globalTransactionKey,
            entity.getKey().getParent(),
            timestamp);
    }

    /**
     * Returns a {@link Lock} specified by the key. Returns null if no entity is
     * found.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return a {@link Lock} specified by the key
     */
    public static Lock getOrNull(Transaction tx, Key key) {
        Entity entity = Datastore.getOrNull(tx, key);
        if (entity == null) {
            return null;
        }
        return toLock(entity);
    }

    /**
     * Returns keys specified by the global transaction key.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @return a list of keys
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    public static List<Key> getKeys(Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        return Datastore.query(KIND).filter(
            GLOBAL_TRANSACTION_KEY_PROPERTY,
            FilterOperator.EQUAL,
            globalTransactionKey).asKeyList();
    }

    /**
     * Deletes the locks from the datastore.
     * 
     * @param locks
     *            the locks
     * @throws NullPointerException
     *             if the locks parameter is null
     * 
     */
    public static void delete(Iterable<Lock> locks) throws NullPointerException {
        if (locks == null) {
            throw new NullPointerException(
                "The locks parameter must not be null.");
        }
        if (locks instanceof Collection<?>
            && ((Collection<?>) locks).size() == 0) {
            return;
        }
        List<Key> keys = new ArrayList<Key>();
        for (Lock lock : locks) {
            if (keys.size() >= MAX_SIZE_LOCKS) {
                Datastore.deleteWithoutTx(keys);
                keys.clear();
            }
            keys.add(lock.key);
        }
        if (keys.size() > 0) {
            Datastore.deleteWithoutTx(keys);
        }
    }

    /**
     * Deletes entities specified by the global transaction key.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    public static void delete(Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        List<Key> keys = getKeys(globalTransactionKey);
        List<List<Key>> keysList = ListUtil.split(keys, MAX_SIZE_LOCKS);
        for (List<Key> l : keysList) {
            Datastore.deleteWithoutTx(l);
        }
    }

    /**
     * Constructor.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param rootKey
     *            the root key
     * @param timestamp
     *            the time-stamp
     * 
     * @throws NullPointerException
     *             if the rootKey parameter is null or if the timestamp
     *             parameter is null or if the globalTransactionKey parameter is
     *             null
     */
    public Lock(Key globalTransactionKey, Key rootKey, Long timestamp)
            throws NullPointerException {
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
     * Locks the entity.
     * 
     * @throws ConcurrentModificationException
     *             if locking the entity failed
     */
    public void lock() throws ConcurrentModificationException {
        DatastoreTimeoutException dte = null;
        for (int i = 0; i < GlobalTransaction.MAX_RETRY; i++) {
            Transaction tx = Datastore.beginTransaction();
            try {
                Lock other = getOrNull(tx, key);
                if (other != null) {
                    if (isLockedBy(other)) {
                        throw createConcurrentModificationException();
                    }
                    Journal.delete(
                        tx,
                        other.rootKey,
                        other.globalTransactionKey);
                }
                Datastore.put(tx, toEntity());
                Datastore.commit(tx);
                return;
            } catch (DatastoreTimeoutException e) {
                Lock lock = getOrNull(null, key);
                if (lock != null
                    && lock.globalTransactionKey.equals(globalTransactionKey)) {
                    return;
                }
                dte = e;
            } finally {
                if (tx.isActive()) {
                    Datastore.rollback(tx);
                }
            }
        }
        throw dte;
    }

    /**
     * Determines if this transaction is locked by the other transaction.
     * 
     * @param other
     *            the other {@link Lock}
     * @return whether this transaction is locked
     * @throws NullPointerException
     *             if the other parameter is null
     */
    protected boolean isLockedBy(Lock other) throws NullPointerException {
        if (other == null) {
            throw new NullPointerException(
                "The other parameter must not be null.");
        }
        if (globalTransactionKey.equals(other.globalTransactionKey)) {
            return false;
        }
        if (timestamp <= other.getTimestamp() + TIMEOUT) {
            return true;
        }
        return GlobalTransaction.exists(other.getGlobalTransactionKey());
    }

    /**
     * Creates a {@link ConcurrentModificationException}.
     * 
     * @return a {@link ConcurrentModificationException}
     */
    protected ConcurrentModificationException createConcurrentModificationException() {
        return new ConcurrentModificationException("Locking the entity group("
            + rootKey
            + ") failed.");
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