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
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.apphosting.api.DeadlineExceededException;

/**
 * The global transaction coordinator. If an error occurs during transaction,
 * this transaction will be rolled back automatically.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class GlobalTransaction {

    /**
     * The kind of global transaction entity.
     */
    public static final String KIND = "slim3.GlobalTransaction";

    /**
     * The version property name.
     */
    public static final String VERSION_PROPERTY = "version";

    /**
     * The queue name.
     */
    protected static final String QUEUE_NAME = "slim3-gtx-queue";

    /**
     * The path for rolling forward.
     */
    protected static final String ROLLFORWARD_PATH = "/slim3/gtx/rollforward/";

    /**
     * The path for rolling back
     */
    protected static final String ROLLBACK_PATH = "/slim3/gtx/rollback/";

    /**
     * The maximum retry count.
     */
    protected static final int MAX_RETRY = 10;

    /**
     * The active global transactions.
     */
    protected static final ThreadLocal<Stack<GlobalTransaction>> activeTransactions =
        new ThreadLocal<Stack<GlobalTransaction>>() {

            @Override
            protected Stack<GlobalTransaction> initialValue() {
                return new Stack<GlobalTransaction>();
            }
        };

    private static final Logger logger =
        Logger.getLogger(GlobalTransaction.class.getName());

    /**
     * Whether this transaction is active.
     */
    protected boolean active = true;

    /**
     * The global transaction key.
     */
    protected Key globalTransactionKey = Datastore.allocateId(KIND);

    /**
     * The time-stamp that a process begun.
     */
    protected long timestamp = System.currentTimeMillis();

    /**
     * The map of {@link Lock}.
     */
    protected Map<Key, Lock> lockMap = new HashMap<Key, Lock>();

    /**
     * The map of {@link Journal}.
     */
    protected Map<Key, Journal> journalMap = new HashMap<Key, Journal>();

    /**
     * Returns the current transaction.
     * 
     * @return the current transaction
     */
    protected static GlobalTransaction getCurrentTransaction() {
        Stack<GlobalTransaction> stack = activeTransactions.get();
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    /**
     * Returns the active transactions.
     * 
     * @return the active transactions
     */
    protected static Collection<GlobalTransaction> getActiveTransactions() {
        Stack<GlobalTransaction> stack = activeTransactions.get();
        Stack<GlobalTransaction> copy = new Stack<GlobalTransaction>();
        copy.addAll(stack);
        return copy;
    }

    /**
     * Clears the active transactions.
     */
    protected static void clearActiveTransactions() {
        activeTransactions.get().clear();
    }

    /**
     * Determines if the global transaction exists.
     * 
     * @param key
     *            the global transaction key
     * @return whether the global transaction exists
     */
    protected static boolean exists(Key key) {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        return !Datastore.getAsMapWithoutTx(key).isEmpty();
    }

    /**
     * Rolls forward the transaction.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param version
     *            the version
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    protected static void rollForward(Key globalTransactionKey, long version)
            throws NullPointerException {
        version = updateVersion(globalTransactionKey, version);
        if (version < 0) {
            return;
        }
        try {
            Journal.rollForward(globalTransactionKey);
            Lock.delete(globalTransactionKey);
            Datastore.deleteWithoutTx(globalTransactionKey);
        } catch (DeadlineExceededException e) {
            logger
                .info("This roll-forward process will be retried, because a DeadlineExceededException occurred.");
            submitRollForwardJob(null, globalTransactionKey, version);
        } catch (Throwable t) {
            logger.log(
                Level.WARNING,
                "An unexpected exception occurred: " + t,
                t);
        }
    }

    /**
     * Rolls back the transaction.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    protected static void rollback(Key globalTransactionKey)
            throws NullPointerException {
        try {
            List<Key> lockKeys = Lock.getKeys(globalTransactionKey);
            for (Key lockKey : lockKeys) {
                rollback(globalTransactionKey, lockKey);
            }
        } catch (DeadlineExceededException e) {
            logger
                .info("This rollback process will be retried, because a DeadlineExceededException occurred.");
            submitRollbackJob(globalTransactionKey);
        } catch (Throwable t) {
            logger.log(
                Level.WARNING,
                "An unexpected exception occurred: " + t,
                t);
        }
    }

    /**
     * Rolls back the transaction.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param lockKey
     *            the lock key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             lockKey parameter is null
     */
    protected static void rollback(Key globalTransactionKey, Key lockKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (lockKey == null) {
            throw new NullPointerException(
                "The lockKey parameter must not be null.");
        }
        for (int i = 0; i < MAX_RETRY; i++) {
            Transaction tx = Datastore.beginTransaction();
            try {
                Lock lock = Lock.getOrNull(tx, lockKey);
                if (lock == null
                    || !globalTransactionKey.equals(lock.globalTransactionKey)) {
                    return;
                }
                List<Key> keys =
                    Journal.getKeys(tx, lock.rootKey, globalTransactionKey);
                keys.add(lockKey);
                Datastore.delete(tx, keys);
                Datastore.commit(tx);
                return;
            } catch (ConcurrentModificationException ignore) {
            } finally {
                if (tx.isActive()) {
                    Datastore.rollback(tx);
                }
            }
        }
    }

    /**
     * Updates the version of the transaction. Returns -1 if the version is
     * different from the version of argument.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param version
     *            the version
     * @return the updated version
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    protected static long updateVersion(Key globalTransactionKey, long version)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        Transaction tx = Datastore.beginTransaction();
        try {
            Entity entity = Datastore.get(tx, globalTransactionKey);
            long lastVersion = (Long) entity.getProperty(VERSION_PROPERTY);
            if (version != lastVersion) {
                return -1;
            }
            version++;
            entity.setUnindexedProperty(VERSION_PROPERTY, version);
            Datastore.put(entity);
            Datastore.commit(tx);
            return version;
        } catch (ConcurrentModificationException ignore) {
            return -1;
        } finally {
            if (tx.isActive()) {
                Datastore.rollback(tx);
            }
        }
    }

    /**
     * Submits a roll-forward job.
     * 
     * @param tx
     *            the transaction
     * @param globalTransactionKey
     *            the global transaction key
     * @param version
     *            the version
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    protected static void submitRollForwardJob(Transaction tx,
            Key globalTransactionKey, long version) throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        String encodedKey = Datastore.keyToString(globalTransactionKey);
        Queue queue = QueueFactory.getQueue(QUEUE_NAME);
        queue.add(tx, TaskOptions.Builder.url(ROLLFORWARD_PATH
            + encodedKey
            + "/"
            + version));
    }

    /**
     * Submits a rollback job.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    protected static void submitRollbackJob(Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        String encodedKey = Datastore.keyToString(globalTransactionKey);
        Queue queue = QueueFactory.getQueue(QUEUE_NAME);
        queue.add(TaskOptions.Builder.url(ROLLBACK_PATH + encodedKey));
    }

    /**
     * Cleans up unprocessed transactions.
     */
    protected static void cleanUp() {
        for (Entity entity : Datastore.query(KIND).asList()) {
            submitRollForwardJob(null, entity.getKey(), (Long) entity
                .getProperty(VERSION_PROPERTY));
        }

    }

    /**
     * Constructor.
     */
    public GlobalTransaction() {
        activeTransactions.get().add(this);
    }

    /**
     * Determines if this transaction is active.
     * 
     * @return whether this transaction is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Asserts that this transaction is active.if this transaction is not active
     * 
     * @throws IllegalStateException
     *             if this transaction is not active
     */
    protected void assertActive() throws IllegalStateException {
        if (!active) {
            throw new IllegalStateException("This transaction must be active.");
        }
    }

    /**
     * Returns the global transaction identifier.
     * 
     * @return the global transaction identifier
     */
    public long getId() {
        return globalTransactionKey.getId();
    }

    /**
     * Gets an entity specified by the key. If locking the entity failed, the
     * other locks that this transaction has are released automatically.
     * 
     * @param key
     *            a key
     * @return an entity
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public Entity get(Key key) throws NullPointerException,
            ConcurrentModificationException {
        Key rootKey = DatastoreUtil.getRoot(key);
        lock(rootKey);
        return Datastore.getWithoutTx(key);
    }

    /**
     * Gets a model specified by the key. If locking the entity failed, the
     * other locks that this transaction has are released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            a key
     * @return a model
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public <M> M get(Class<M> modelClass, Key key) throws NullPointerException,
            ConcurrentModificationException {
        Key rootKey = DatastoreUtil.getRoot(key);
        lock(rootKey);
        return Datastore.getWithoutTx(modelClass, key);
    }

    /**
     * Gets a model specified by the key. If locking the entity failed, the
     * other locks that this transaction has are released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            a key
     * @return a model
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public <M> M get(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, ConcurrentModificationException {
        Key rootKey = DatastoreUtil.getRoot(key);
        lock(rootKey);
        return Datastore.getWithoutTx(modelMeta, key);
    }

    /**
     * Gets entities specified by the keys. If locking the entities failed, the
     * other locks that this transaction has are released automatically.
     * 
     * @param keys
     *            keys
     * @return entities
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public List<Entity> get(Iterable<Key> keys) throws NullPointerException,
            ConcurrentModificationException {
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        for (Key key : keys) {
            Key rootKey = DatastoreUtil.getRoot(key);
            lock(rootKey);
        }
        return Datastore.getWithoutTx(keys);
    }

    /**
     * Gets models specified by the keys. If locking the entities failed, the
     * other locks that this transaction has are released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            keys
     * @return models
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public <M> List<M> get(Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, ConcurrentModificationException {
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        for (Key key : keys) {
            Key rootKey = DatastoreUtil.getRoot(key);
            lock(rootKey);
        }
        return Datastore.getWithoutTx(modelClass, keys);
    }

    /**
     * Gets models specified by the keys. If locking the entities failed, the
     * other locks that this transaction has are released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            keys
     * @return models
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public <M> List<M> get(ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, ConcurrentModificationException {
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        for (Key key : keys) {
            Key rootKey = DatastoreUtil.getRoot(key);
            lock(rootKey);
        }
        return Datastore.getWithoutTx(modelMeta, keys);
    }

    /**
     * Gets entities specified by the keys. If locking the entities failed, the
     * other locks that this transaction has are released automatically.
     * 
     * @param keys
     *            keys
     * @return entities
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public List<Entity> get(Key... keys) throws ConcurrentModificationException {
        for (Key key : keys) {
            Key rootKey = DatastoreUtil.getRoot(key);
            lock(rootKey);
        }
        return Datastore.getWithoutTx(keys);
    }

    /**
     * Gets models specified by the keys. If locking the entities failed, the
     * other locks that this transaction has are released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            keys
     * @return models
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public <M> List<M> get(Class<M> modelClass, Key... keys)
            throws NullPointerException, ConcurrentModificationException {
        for (Key key : keys) {
            Key rootKey = DatastoreUtil.getRoot(key);
            lock(rootKey);
        }
        return Datastore.getWithoutTx(modelClass, keys);
    }

    /**
     * Gets models specified by the keys. If locking the entities failed, the
     * other locks that this transaction has are released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            keys
     * @return models
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public <M> List<M> get(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, ConcurrentModificationException {
        for (Key key : keys) {
            Key rootKey = DatastoreUtil.getRoot(key);
            lock(rootKey);
        }
        return Datastore.getWithoutTx(modelMeta, keys);
    }

    /**
     * Returns an {@link EntityQuery}.
     * 
     * @param kind
     *            the kind
     * @param ancestorKey
     *            the ancestor key
     * @return an {@link EntityQuery}
     * @throws NullPointerException
     *             if the kind parameter is null or if the ancestorKey parameter
     *             is null
     */
    public EntityQuery query(String kind, Key ancestorKey)
            throws NullPointerException {
        Key rootKey = DatastoreUtil.getRoot(ancestorKey);
        lock(rootKey);
        return Datastore.query(kind, ancestorKey);
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
    public <M> ModelQuery<M> query(Class<M> modelClass, Key ancestorKey)
            throws NullPointerException {
        Key rootKey = DatastoreUtil.getRoot(ancestorKey);
        lock(rootKey);
        return Datastore.query(modelClass, ancestorKey);
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
    public <M> ModelQuery<M> query(ModelMeta<M> modelMeta, Key ancestorKey)
            throws NullPointerException {
        Key rootKey = DatastoreUtil.getRoot(ancestorKey);
        lock(rootKey);
        return Datastore.query(modelMeta, ancestorKey);
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link KindlessQuery}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public KindlessQuery query(Key ancestorKey) throws NullPointerException {
        Key rootKey = DatastoreUtil.getRoot(ancestorKey);
        lock(rootKey);
        return Datastore.query(ancestorKey);
    }

    /**
     * Puts the entity. If locking the entity failed, the other locks that this
     * transaction has are released automatically.
     * 
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     * @throws IllegalArgumentException
     *             if the size of the entity is more than 1,000,000 bytes
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public Key put(Entity entity) throws NullPointerException,
            IllegalArgumentException, ConcurrentModificationException {
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        DatastoreUtil.assignKeyIfNecessary(entity);
        Key key = entity.getKey();
        Key rootKey = DatastoreUtil.getRoot(key);
        lock(rootKey);
        journalMap.put(key, new Journal(globalTransactionKey, entity));
        return key;
    }

    /**
     * Puts the model. If locking the entity failed, the other locks that this
     * transaction has are released automatically.
     * 
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     * @throws IllegalArgumentException
     *             if the size of the entity is more than 1,000,000 bytes
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public Key put(Object model) throws NullPointerException,
            IllegalArgumentException, ConcurrentModificationException {
        Entity entity = DatastoreUtil.modelToEntity(model);
        return put(entity);
    }

    /**
     * Puts the models. If locking the entities failed, the other locks that
     * this transaction has are released automatically.
     * 
     * @param models
     *            the models
     * @return a list of keys
     * @throws NullPointerException
     *             if the models parameter is null
     * @throws IllegalArgumentException
     *             if the size of the entity is more than 1,000,000 bytes
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public List<Key> put(Iterable<?> models) throws NullPointerException,
            IllegalArgumentException, ConcurrentModificationException {
        if (models == null) {
            throw new NullPointerException(
                "The models parameter must not be null.");
        }
        List<Key> keys = new ArrayList<Key>();
        for (Object model : models) {
            if (model instanceof Entity) {
                keys.add(put((Entity) model));
            } else {
                keys.add(put(model));
            }
        }
        return keys;
    }

    /**
     * Puts the models. If locking the entities failed, the other locks that
     * this transaction has are released automatically.
     * 
     * @param models
     *            the models
     * @return a list of keys
     * @throws IllegalArgumentException
     *             if the size of the entity is more than 1,000,000 bytes
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public List<Key> put(Object... models) throws IllegalArgumentException,
            ConcurrentModificationException {
        return put(Arrays.asList(models));
    }

    /**
     * Deletes the entity. If locking the entity failed, the other locks that
     * this transaction has are released automatically.
     * 
     * @param key
     *            the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public void delete(Key key) throws NullPointerException,
            ConcurrentModificationException {
        Key rootKey = DatastoreUtil.getRoot(key);
        lock(rootKey);
        journalMap.put(key, new Journal(globalTransactionKey, key));
    }

    /**
     * Deletes the models. If locking the entities failed, the other locks that
     * this transaction has are released automatically.
     * 
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public void delete(Iterable<Key> keys) throws NullPointerException,
            ConcurrentModificationException {
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        for (Key key : keys) {
            delete(key);
        }
    }

    /**
     * Deletes the models. If locking the entities failed, the other locks that
     * this transaction has are released automatically.
     * 
     * @param keys
     *            the keys
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public void delete(Key... keys) throws ConcurrentModificationException {
        delete(Arrays.asList(keys));
    }

    /**
     * Deletes all descendant entities. If locking the entity failed, the other
     * locks that this transaction has are released automatically.
     * 
     * @param key
     *            the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public void deleteAll(Key key) throws NullPointerException,
            ConcurrentModificationException {
        Key rootKey = DatastoreUtil.getRoot(key);
        lock(rootKey);
        journalMap.put(key, new Journal(globalTransactionKey, key, true));
    }

    /**
     * Commits this transaction.
     */
    public void commit() {
        assertActive();
        if (isLocalTransaction()) {
            commitLocalTransaction();
        } else {
            commitGlobalTransaction();
        }
    }

    /**
     * Commits this transaction asynchronously.
     */
    public void commitAsync() {
        assertActive();
        commitAsyncGlobalTransaction();
    }

    /**
     * Rolls back this transaction.
     */
    public void rollback() {
        assertActive();
        if (isLocalTransaction()) {
            rollbackLocalTransaction();
        } else {
            rollbackGlobalTransaction();
        }
    }

    /**
     * Rolls back this transaction asynchronously.
     */
    public void rollbackAsync() {
        assertActive();
        if (isLocalTransaction()) {
            rollbackLocalTransaction();
        } else {
            rollbackAsyncGlobalTransaction();
        }
    }

    /**
     * Lock the entity. If locking the entity failed, the other locks that this
     * transaction has are released automatically.
     * 
     * @param rootKey
     *            a root key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking an entity specified by the key failed
     */
    protected void lock(Key rootKey) throws NullPointerException,
            ConcurrentModificationException {
        if (rootKey == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        assertActive();
        if (!lockMap.containsKey(rootKey)) {
            Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
            try {
                lock.lock();
            } catch (ConcurrentModificationException e) {
                unlock();
                throw e;
            }
            lockMap.put(rootKey, lock);
        }
    }

    /**
     * Unlocks entities.
     */
    protected void unlock() {
        active = false;
        activeTransactions.get().remove(this);
        Lock.delete(lockMap.values());
        lockMap.clear();
    }

    /**
     * Determines if this is a local transaction.
     * 
     * @return whether this is a local transaction
     */
    protected boolean isLocalTransaction() {
        return lockMap.size() <= 1;
    }

    /**
     * Commits this transaction as local transaction.
     */
    protected void commitLocalTransaction() {
        Transaction tx = Datastore.beginTransaction();
        try {
            Journal.applyWithinLocalTransaction(tx, journalMap.values());
            Datastore.commit(tx);
        } finally {
            if (tx.isActive()) {
                Datastore.rollback(tx);
            }
            active = false;
            activeTransactions.get().remove(this);
            unlock();
        }
    }

    /**
     * Commits this transaction as global transaction.
     */
    protected void commitGlobalTransaction() {
        Journal.put(journalMap.values());
        commitGlobalTransactionInternally();
        Journal.applyWithinGlobalTransaction(journalMap.values());
        unlock();
        Datastore.deleteWithoutTx(globalTransactionKey);
    }

    /**
     * Commits this global transaction.
     */
    protected void commitGlobalTransactionInternally() {
        try {
            Entity entity = new Entity(globalTransactionKey);
            entity.setUnindexedProperty(VERSION_PROPERTY, 1);
            Datastore.putWithoutTx(entity);
        } finally {
            active = false;
            activeTransactions.get().remove(this);
        }
    }

    /**
     * Commits this transaction as global transaction asynchronously.
     */
    protected void commitAsyncGlobalTransaction() {
        if (!lockMap.isEmpty()) {
            Journal.put(journalMap.values());
            commitAsyncGlobalTransactionInternally();
        }
    }

    /**
     * Commits this global transaction asynchronously.
     */
    protected void commitAsyncGlobalTransactionInternally() {
        Transaction tx = Datastore.beginTransaction();
        try {
            Entity entity = new Entity(globalTransactionKey);
            entity.setUnindexedProperty(VERSION_PROPERTY, 1);
            Datastore.put(tx, entity);
            submitRollForwardJob(tx, globalTransactionKey, 1);
            Datastore.commit(tx);
        } finally {
            if (tx.isActive()) {
                Datastore.rollback(tx);
            }
            active = false;
            activeTransactions.get().remove(this);
        }
    }

    /**
     * Rolls back this transaction as local transaction.
     */
    protected void rollbackLocalTransaction() {
        active = false;
        activeTransactions.get().remove(this);
        unlock();
    }

    /**
     * Rolls back this transaction as global transaction.
     */
    protected void rollbackGlobalTransaction() {
        active = false;
        activeTransactions.get().remove(this);
        Journal.delete(journalMap.values());
        unlock();
    }

    /**
     * Rolls back this transaction as global transaction asynchronously.
     */
    protected void rollbackAsyncGlobalTransaction() {
        active = false;
        activeTransactions.get().remove(this);
        submitRollbackJob(globalTransactionKey);
    }
}