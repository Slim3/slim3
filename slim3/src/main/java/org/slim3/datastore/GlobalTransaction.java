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
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slim3.util.FutureUtil;
import org.slim3.util.ThrowableUtil;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.apphosting.api.DeadlineExceededException;

/**
 * The global transaction coordinator. If an error occurs during transaction,
 * this transaction will be rolled back automatically.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class GlobalTransaction {

    /**
     * The kind of global transaction entity.
     */
    public static final String KIND = "slim3.GlobalTransaction";

    /**
     * The valid property name.
     */
    public static final String VALID_PROPERTY = "valid";

    /**
     * The queue name.
     */
    protected static final String QUEUE_NAME = "slim3-gtx-queue";

    /**
     * The number of milliseconds delay before execution of the roll-forward
     * task.
     */
    protected static final long ROLL_FORWARD_DELAY = 60000;

    /**
     * The active global transactions.
     */
    protected static final ThreadLocal<Stack<GlobalTransaction>> activeTransactions =
        new ThreadLocal<Stack<GlobalTransaction>>();

    private static final Logger logger =
        Logger.getLogger(GlobalTransaction.class.getName());

    /**
     * The local transaction.
     */
    protected Transaction localTransaction;

    /**
     * The root key of local transaction.
     */
    protected Key localTransactionRootKey;

    /**
     * The global transaction key.
     */
    protected Key globalTransactionKey;

    /**
     * The time-stamp that a process begun.
     */
    protected long timestamp;

    /**
     * The map of {@link Lock}.
     */
    protected Map<Key, Lock> lockMap;

    /**
     * The map of journals for global transaction.
     */
    protected Map<Key, Entity> globalJournalMap;

    /**
     * The map of journals for local transaction.
     */
    protected Map<Key, Entity> localJournalMap;

    /**
     * Whether this global transaction is valid.
     */
    protected boolean valid = true;

    /**
     * The asynchronous datastore service
     */
    protected AsyncDatastoreService ds;

    /**
     * Returns the current transaction stack.
     * 
     * @return the current transaction stack
     */
    protected static Stack<GlobalTransaction> getCurrentTransactionStack() {
        Stack<GlobalTransaction> stack = activeTransactions.get();
        if (stack == null) {
            stack = new Stack<GlobalTransaction>();
            activeTransactions.set(stack);
        }
        return stack;
    }

    /**
     * Returns the current transaction.
     * 
     * @return the current transaction
     */
    protected static GlobalTransaction getCurrentTransaction() {
        Stack<GlobalTransaction> stack = getCurrentTransactionStack();
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
        Stack<GlobalTransaction> stack = getCurrentTransactionStack();
        Stack<GlobalTransaction> copy = new Stack<GlobalTransaction>();
        copy.addAll(stack);
        return copy;
    }

    /**
     * Clears the active transactions.
     */
    protected static void clearActiveTransactions() {
        getCurrentTransactionStack().clear();
    }

    /**
     * Returns a global transaction specified by the key. Returns null if no
     * entity is found.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param key
     *            the global transaction key
     * 
     * @return a global transaction
     * @throws NullPointerException
     *             if the ds parameter is null or if the tx parameter is null or
     *             if the key parameter is null
     */
    protected static GlobalTransaction getOrNull(AsyncDatastoreService ds,
            Transaction tx, Key key) throws NullPointerException {
        Future<Map<Key, Entity>> future =
            DatastoreUtil.getAsMapAsync(ds, tx, Arrays.asList(key));
        Entity entity = FutureUtil.getQuietly(future).get(key);
        if (entity == null) {
            return null;
        }
        return toGlobalTransaction(ds, entity);
    }

    /**
     * Converts the entity to a global transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param entity
     *            the entity
     * 
     * @return a global transaction
     * @throws NullPointerException
     *             if the ds parameter is null or if the entity parameter is
     *             null or if the valid property is null
     */
    protected static GlobalTransaction toGlobalTransaction(
            AsyncDatastoreService ds, Entity entity)
            throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        Boolean valid = (Boolean) entity.getProperty(VALID_PROPERTY);
        return new GlobalTransaction(ds, entity.getKey(), valid);
    }

    /**
     * Puts the global transaction to datastore.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param globalTransaction
     *            the global transaction
     * @throws NullPointerException
     *             if the ds parameter is null or if the tx parameter is null or
     *             if the globalTransaction parameter is null
     */
    protected static void put(AsyncDatastoreService ds, Transaction tx,
            GlobalTransaction globalTransaction) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (tx == null) {
            throw new NullPointerException("The tx parameter must not be null.");
        }
        if (globalTransaction == null) {
            throw new NullPointerException(
                "The globalTransaction parameter must not be null.");
        }
        FutureUtil.getQuietly(DatastoreUtil.putAsync(ds, tx, Arrays
            .asList(globalTransaction.toEntity())));
    }

    /**
     * Rolls forward the transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the ds parameter is null or if the globalTransactionKey
     *             parameter is null
     */
    protected static void rollForward(AsyncDatastoreService ds,
            Key globalTransactionKey) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (globalTransactionKey == null) {
            logger
                .warning("The globalTransactionKey parameter must not be null.");
            return;
        }
        if (DatastoreUtil.getOrNull(ds, null, globalTransactionKey) == null) {
            return;
        }
        Journal.apply(ds, globalTransactionKey);
        Lock.deleteWithoutTx(ds, globalTransactionKey);
        DatastoreUtil.delete(ds, null, globalTransactionKey);
    }

    /**
     * Rolls back the transaction.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    protected static void rollback(AsyncDatastoreService ds,
            Key globalTransactionKey) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (globalTransactionKey == null) {
            logger
                .warning("The globalTransactionKey parameter must not be null.");
            return;
        }
        Journal.deleteInTx(ds, globalTransactionKey);
        Lock.deleteInTx(ds, globalTransactionKey);
    }

    /**
     * Submits a roll-forward job.
     * 
     * @param tx
     *            the transaction
     * @param globalTransactionKey
     *            the global transaction key
     * @param countdownMillis
     *            the number of milliseconds delay before execution of the task
     * @throws NullPointerException
     *             if the tx parameter is null or if the globalTransactionKey
     *             parameter is null
     */
    protected static void submitRollForwardJob(Transaction tx,
            Key globalTransactionKey, long countdownMillis)
            throws NullPointerException {
        if (tx == null) {
            throw new NullPointerException("The tx parameter must not be null.");
        }
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        String encodedKey = KeyFactory.keyToString(globalTransactionKey);
        Queue queue = QueueFactory.getQueue(QUEUE_NAME);
        queue.add(tx, TaskOptions.Builder.withUrl(
            GlobalTransactionServlet.SERVLET_PATH).param(
            GlobalTransactionServlet.COMMAND_NAME,
            GlobalTransactionServlet.ROLLFORWARD_COMMAND).param(
            GlobalTransactionServlet.KEY_NAME,
            encodedKey).countdownMillis(countdownMillis));
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
        String encodedKey = KeyFactory.keyToString(globalTransactionKey);
        Queue queue = QueueFactory.getQueue(QUEUE_NAME);
        queue.add(null, TaskOptions.Builder.withUrl(
            GlobalTransactionServlet.SERVLET_PATH).param(
            GlobalTransactionServlet.COMMAND_NAME,
            GlobalTransactionServlet.ROLLBACK_COMMAND).param(
            GlobalTransactionServlet.KEY_NAME,
            encodedKey));
    }

    /**
     * Constructor.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @throws NullPointerException
     *             if the ds parameter is null
     */
    public GlobalTransaction(AsyncDatastoreService ds)
            throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        this.ds = ds;
    }

    /**
     * Constructor.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @param valid
     *            whether this transaction is valid
     * @throws NullPointerException
     *             if the ds parameter is null or if the globalTransactionKey
     *             parameter is null or if the valid parameter is null
     */
    protected GlobalTransaction(AsyncDatastoreService ds,
            Key globalTransactionKey, Boolean valid)
            throws NullPointerException {
        this(ds);
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (valid == null) {
            throw new NullPointerException(
                "The valid parameter must not be null.");
        }
        this.globalTransactionKey = globalTransactionKey;
        this.valid = valid;
    }

    /**
     * Returns the local transaction.
     * 
     * @return the local transaction
     */
    public Transaction getLocalTransaction() {
        assertActive();
        return localTransaction;
    }

    /**
     * Determines if this transaction is active.
     * 
     * @return whether this transaction is active
     */
    public boolean isActive() {
        if (localTransaction != null) {
            return localTransaction.isActive();
        }
        return false;
    }

    /**
     * Asserts that this transaction is active.if this transaction is not active
     * 
     * @throws IllegalStateException
     *             if this transaction is not active
     */
    protected void assertActive() throws IllegalStateException {
        if (!isActive()) {
            throw new IllegalStateException("This transaction must be active.");
        }
    }

    /**
     * Returns the transaction identifier.
     * 
     * @return the transaction identifier
     * @throws IllegalStateException
     *             if the transaction does not begin
     */
    public String getId() throws IllegalStateException {
        if (localTransaction != null) {
            return localTransaction.getId();
        }
        throw new IllegalStateException("This transaction must begin.");
    }

    /**
     * Begins this global transaction.
     */
    protected void begin() {
        getCurrentTransactionStack().add(this);
        localTransaction = DatastoreUtil.beginTransaction(ds);
        timestamp = System.currentTimeMillis();
        lockMap = new HashMap<Key, Lock>();
        globalJournalMap = new HashMap<Key, Entity>();
        localJournalMap = new HashMap<Key, Entity>();
    }

    /**
     * Gets an entity specified by the key. If locking the entity failed, the
     * other locks that this transaction has are released automatically.
     * 
     * @param key
     *            the key
     * @return an entity
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public Entity get(Key key) throws NullPointerException,
            EntityNotFoundRuntimeException, ConcurrentModificationException,
            IllegalStateException {
        Entity entity = getOrNull(key);
        if (entity == null) {
            throw new EntityNotFoundRuntimeException(key);
        }
        return entity;
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
     *            the key
     * @return a model
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> M get(Class<M> modelClass, Key key) throws NullPointerException,
            EntityNotFoundRuntimeException, ConcurrentModificationException,
            IllegalStateException {
        return get(DatastoreUtil.getModelMeta(modelClass), key);
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
     *            the key
     * @return a model
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> M get(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException,
            ConcurrentModificationException, IllegalStateException {
        Entity entity = get(key);
        ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
        mm.validateKey(key);
        return mm.entityToModel(entity);
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
     *            the key
     * @param version
     *            the version
     * @return a model
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> M get(Class<M> modelClass, Key key, long version)
            throws NullPointerException, EntityNotFoundRuntimeException,
            ConcurrentModificationException, IllegalStateException {
        return get(DatastoreUtil.getModelMeta(modelClass), key, version);
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
     *            the key
     * @param version
     *            the version
     * @return a model
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> M get(ModelMeta<M> modelMeta, Key key, long version)
            throws NullPointerException, EntityNotFoundRuntimeException,
            ConcurrentModificationException, IllegalStateException {
        Entity entity = get(key);
        ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
        mm.validateKey(key);
        M model = mm.entityToModel(entity);
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
     * Gets an entity specified by the key. Returns null if no entity is found.
     * If locking the entity failed, the other locks that this transaction has
     * are released automatically.
     * 
     * @param key
     *            the key
     * @return an entity
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public Entity getOrNull(Key key) throws NullPointerException,
            ConcurrentModificationException, IllegalStateException {
        Key rootKey = DatastoreUtil.getRoot(key);
        if (localTransactionRootKey == null) {
            setLocalTransactionRootKey(rootKey);
            return verifyLockAndGetAsMap(rootKey, Arrays.asList(key)).get(key);
        }
        if (rootKey.equals(localTransactionRootKey)) {
            return DatastoreUtil.getOrNull(ds, localTransaction, key);
        }
        return lockAndGetAsMap(rootKey, Arrays.asList(key)).get(key);
    }

    /**
     * Gets a model specified by the key. Returns null if no entity is found. If
     * locking the entity failed, the other locks that this transaction has are
     * released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> M getOrNull(Class<M> modelClass, Key key)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        return getOrNull(DatastoreUtil.getModelMeta(modelClass), key);
    }

    /**
     * Gets a model specified by the key. Returns null if no entity is found. If
     * locking the entity failed, the other locks that this transaction has are
     * released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> M getOrNull(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        Entity entity = getOrNull(key);
        if (entity == null) {
            return null;
        }
        ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
        mm.validateKey(key);
        return mm.entityToModel(entity);
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
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public List<Entity> get(Iterable<Key> keys) throws NullPointerException,
            ConcurrentModificationException, IllegalStateException {
        return DatastoreUtil.entityMapToEntityList(keys, getAsMap(keys));
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
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> List<M> get(Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        return get(DatastoreUtil.getModelMeta(modelClass), keys);
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
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> List<M> get(ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        return DatastoreUtil.entityMapToModelList(
            modelMeta,
            keys,
            getAsMap(keys));
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
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public List<Entity> get(Key... keys)
            throws ConcurrentModificationException, IllegalStateException {
        return get(Arrays.asList(keys));
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
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> List<M> get(Class<M> modelClass, Key... keys)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        return get(modelClass, Arrays.asList(keys));
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
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> List<M> get(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        return get(modelMeta, Arrays.asList(keys));
    }

    /**
     * Gets entities as map. If locking the entity groups failed, the other
     * locks that this transaction has are released automatically.
     * 
     * @param keys
     *            the keys
     * @return entities
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public Map<Key, Entity> getAsMap(Iterable<Key> keys)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        Map<Key, Entity> map = new HashMap<Key, Entity>();
        Key ltxRootKey = null;
        Set<Key> gtxRootKeys = new HashSet<Key>();
        List<Key> ltxKeys = new ArrayList<Key>();
        List<Key> gtxKeys = new ArrayList<Key>();
        for (Key key : keys) {
            Key rootKey = DatastoreUtil.getRoot(key);
            if (localTransactionRootKey == null) {
                if (ltxRootKey == null) {
                    ltxRootKey = rootKey;
                    ltxKeys.add(key);
                } else if (ltxRootKey.equals(rootKey)) {
                    ltxKeys.add(key);
                } else {
                    gtxRootKeys.add(rootKey);
                    gtxKeys.add(key);
                }
            } else if (localTransactionRootKey.equals(rootKey)) {
                ltxKeys.add(key);
            } else {
                gtxRootKeys.add(rootKey);
                gtxKeys.add(key);
            }
        }
        if (ltxRootKey != null) {
            setLocalTransactionRootKey(ltxRootKey);
            map.putAll(verifyLockAndGetAsMap(ltxRootKey, ltxKeys));
        } else {
            if (ltxKeys.size() > 0) {
                map.putAll(DatastoreUtil
                    .getAsMap(ds, localTransaction, ltxKeys));
            }
        }
        if (gtxKeys.size() > 0) {
            for (Key key : gtxRootKeys) {
                lock(key);
            }
            map.putAll(DatastoreUtil.getAsMap(ds, null, gtxKeys));
        }
        return map;
    }

    /**
     * Gets models as map. If locking the entity groups failed, the other locks
     * that this transaction has are released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return entities
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> Map<Key, M> getAsMap(Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        return getAsMap(DatastoreUtil.getModelMeta(modelClass), keys);
    }

    /**
     * Gets models as map. If locking the entity groups failed, the other locks
     * that this transaction has are released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> Map<Key, M> getAsMap(ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        return DatastoreUtil.entityMapToModelMap(modelMeta, getAsMap(keys));
    }

    /**
     * Gets entities as map. If locking the entity groups failed, the other
     * locks that this transaction has are released automatically.
     * 
     * @param keys
     *            the keys
     * @return entities
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public Map<Key, Entity> getAsMap(Key... keys)
            throws ConcurrentModificationException, IllegalStateException {
        return getAsMap(Arrays.asList(keys));
    }

    /**
     * Gets models as map. If locking the entity groups failed, the other locks
     * that this transaction has are released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return entities
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> Map<Key, M> getAsMap(Class<M> modelClass, Key... keys)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        return getAsMap(modelClass, Arrays.asList(keys));
    }

    /**
     * Gets models as map. If locking the entity groups failed, the other locks
     * that this transaction has are released automatically.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> Map<Key, M> getAsMap(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        return getAsMap(modelMeta, Arrays.asList(keys));
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
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public EntityQuery query(String kind, Key ancestorKey)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        Key rootKey = DatastoreUtil.getRoot(ancestorKey);
        if (localTransactionRootKey == null) {
            setLocalTransactionRootKey(rootKey);
            return new EntityQuery(ds, localTransaction, kind, ancestorKey);
        } else if (rootKey.equals(localTransactionRootKey)) {
            return new EntityQuery(ds, localTransaction, kind, ancestorKey);
        }
        lock(rootKey);
        return new EntityQuery(ds, kind, ancestorKey);
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
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> ModelQuery<M> query(Class<M> modelClass, Key ancestorKey)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        Key rootKey = DatastoreUtil.getRoot(ancestorKey);
        if (localTransactionRootKey == null) {
            setLocalTransactionRootKey(rootKey);
            return new ModelQuery<M>(ds, localTransaction, DatastoreUtil
                .getModelMeta(modelClass), ancestorKey);
        } else if (rootKey.equals(localTransactionRootKey)) {
            return new ModelQuery<M>(ds, localTransaction, DatastoreUtil
                .getModelMeta(modelClass), ancestorKey);
        }
        lock(rootKey);
        return new ModelQuery<M>(
            ds,
            DatastoreUtil.getModelMeta(modelClass),
            ancestorKey);
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
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public <M> ModelQuery<M> query(ModelMeta<M> modelMeta, Key ancestorKey)
            throws NullPointerException, ConcurrentModificationException,
            IllegalStateException {
        Key rootKey = DatastoreUtil.getRoot(ancestorKey);
        if (localTransactionRootKey == null) {
            setLocalTransactionRootKey(rootKey);
            return new ModelQuery<M>(
                ds,
                localTransaction,
                modelMeta,
                ancestorKey);
        } else if (rootKey.equals(localTransactionRootKey)) {
            return new ModelQuery<M>(
                ds,
                localTransaction,
                modelMeta,
                ancestorKey);
        }
        lock(rootKey);
        return new ModelQuery<M>(ds, modelMeta, ancestorKey);
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link KindlessQuery}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     * @throws IllegalStateException
     *             if the number of locks in this global transaction exceeds 100
     */
    public KindlessQuery query(Key ancestorKey) throws NullPointerException,
            ConcurrentModificationException, IllegalStateException {
        Key rootKey = DatastoreUtil.getRoot(ancestorKey);
        if (localTransactionRootKey == null) {
            setLocalTransactionRootKey(rootKey);
            return new KindlessQuery(ds, localTransaction, ancestorKey);
        } else if (rootKey.equals(localTransactionRootKey)) {
            return new KindlessQuery(ds, localTransaction, ancestorKey);
        }
        lock(rootKey);
        return new KindlessQuery(ds, ancestorKey);
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
        DatastoreUtil.assignKeyIfNecessary(ds, entity);
        Key key = entity.getKey();
        Key rootKey = DatastoreUtil.getRoot(key);
        if (localTransactionRootKey == null) {
            setLocalTransactionRootKey(rootKey);
            localJournalMap.put(key, entity);
        } else if (rootKey.equals(localTransactionRootKey)) {
            localJournalMap.put(key, entity);
        } else {
            lock(rootKey);
            globalJournalMap.put(key, entity);
        }
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
        Entity entity = DatastoreUtil.modelToEntity(ds, model);
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
        if (localTransactionRootKey == null) {
            setLocalTransactionRootKey(rootKey);
            localJournalMap.put(key, null);
        } else if (rootKey.equals(localTransactionRootKey)) {
            localJournalMap.put(key, null);
        } else {
            lock(rootKey);
            globalJournalMap.put(key, null);
        }
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
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public void deleteAll(Key ancestorKey) throws NullPointerException,
            ConcurrentModificationException {
        Key rootKey = DatastoreUtil.getRoot(ancestorKey);
        if (localTransactionRootKey == null) {
            setLocalTransactionRootKey(rootKey);
            for (Key key : new KindlessQuery(ds, localTransaction, ancestorKey)
                .asKeyList()) {
                localJournalMap.put(key, null);
            }
        } else if (rootKey.equals(localTransactionRootKey)) {
            for (Key key : new KindlessQuery(ds, localTransaction, ancestorKey)
                .asKeyList()) {
                localJournalMap.put(key, null);
            }
        } else {
            lock(rootKey);
            for (Key key : new KindlessQuery(ds, ancestorKey).asKeyList()) {
                if (key.getKind().equals(Lock.KIND)) {
                    continue;
                }
                globalJournalMap.put(key, null);
            }
        }
    }

    /**
     * Commits this transaction.
     */
    public void commit() {
        assertActive();
        Journal.apply(ds, localTransaction, localJournalMap);
        if (isLocalTransaction()) {
            commitLocalTransaction();
        } else {
            commitGlobalTransaction();
        }
    }

    /**
     * Rolls back this transaction.
     */
    public void rollback() {
        assertActive();
        try {
            if (isLocalTransaction()) {
                rollbackLocalTransaction();
            } else {
                rollbackGlobalTransaction();
            }
        } catch (DeadlineExceededException e) {
            if (lockMap.isEmpty()) {
                return;
            }
            logger
                .info("This rollback process will be executed asynchronously, because a DeadlineExceededException occurred.");
            submitRollbackJob(globalTransactionKey);
        }
    }

    /**
     * Rolls back this transaction asynchronously.
     */
    protected void rollbackAsync() {
        assertActive();
        if (isLocalTransaction()) {
            rollbackLocalTransaction();
        } else {
            rollbackAsyncGlobalTransaction();
        }
    }

    /**
     * Converts this transaction to an entity.
     * 
     * @return an entity
     */
    protected Entity toEntity() {
        Entity entity = new Entity(globalTransactionKey);
        entity.setUnindexedProperty(VALID_PROPERTY, valid);
        return entity;
    }

    /**
     * Sets the root key of local transaction.
     * 
     * @param rootKey
     *            the root key
     * @throws NullPointerException
     *             if the rootKey parameter is null
     */
    protected void setLocalTransactionRootKey(Key rootKey)
            throws NullPointerException {
        if (rootKey == null) {
            throw new NullPointerException(
                "The rootKey parameter must not be null.");
        }
        this.localTransactionRootKey = rootKey;
        globalTransactionKey =
            KeyFactory.createKey(rootKey, KIND, localTransaction.getId());
    }

    /**
     * Verifies lock specified by the root key and returns entities as map.
     * 
     * @param rootKey
     *            the root key
     * @param keys
     *            the keys
     * @return entities as map
     * @throws NullPointerException
     *             if the rootKey parameter is null or if the keys parameter is
     *             null
     */
    protected Map<Key, Entity> verifyLockAndGetAsMap(Key rootKey,
            Collection<Key> keys) throws NullPointerException {
        if (rootKey == null) {
            throw new NullPointerException(
                "The rootKey parameter must not be null.");
        }
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        assertActive();
        return Lock.verifyAndGetAsMap(ds, localTransaction, rootKey, keys);
    }

    /**
     * Locks the entity group and returns entities as map.
     * 
     * @param rootKey
     *            the root key
     * @param keys
     *            the keys
     * @return entities as map
     * @throws NullPointerException
     *             if the rootKey parameter is null or if the keys parameter is
     *             null
     * @throws ConcurrentModificationException
     *             if locking an entity specified by the key failed
     */
    protected Map<Key, Entity> lockAndGetAsMap(Key rootKey, Collection<Key> keys)
            throws NullPointerException, ConcurrentModificationException {
        if (rootKey == null) {
            throw new NullPointerException(
                "The rootKey parameter must not be null.");
        }
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        assertActive();
        if (!lockMap.containsKey(rootKey)) {
            Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
            try {
                Map<Key, Entity> map = lock.lockAndGetAsMap(keys);
                lockMap.put(rootKey, lock);
                return map;
            } catch (ConcurrentModificationException e) {
                unlock();
                throw e;
            }
        }
        return DatastoreUtil.getAsMap(ds, null, keys);
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
            Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
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
        getCurrentTransactionStack().remove(this);
        if (localTransaction.isActive()) {
            localTransaction.rollback();
        }
        if (!lockMap.isEmpty()) {
            Lock.deleteInTx(ds, globalTransactionKey, lockMap.values());
            lockMap.clear();
        }
    }

    /**
     * Determines if this is a local transaction.
     * 
     * @return whether this is a local transaction
     */
    protected boolean isLocalTransaction() {
        return lockMap.isEmpty();
    }

    /**
     * Commits this transaction as local transaction.
     */
    protected void commitLocalTransaction() {
        getCurrentTransactionStack().remove(this);
        try {
            localTransaction.commit();
        } finally {
            if (localTransaction.isActive()) {
                localTransaction.rollback();
            }
        }
    }

    /**
     * Commits this transaction as global transaction.
     */
    protected void commitGlobalTransaction() {
        List<Entity> journals = putJournals();
        commitGlobalTransactionInternally();
        Journal.apply(ds, journals);
        Lock.deleteWithoutTx(ds, lockMap.values());
        DatastoreUtil.delete(ds, null, Arrays.asList(globalTransactionKey));
    }

    /**
     * Puts the journals.
     * 
     * @return journal entities
     */
    protected List<Entity> putJournals() {
        try {
            return Journal.put(ds, globalTransactionKey, globalJournalMap);
        } catch (Throwable cause) {
            try {
                Journal.deleteInTx(ds, globalTransactionKey);
            } catch (Throwable cause2) {
                logger.log(Level.WARNING, cause2.getMessage(), cause2);
            }
            try {
                unlock();
            } catch (Throwable cause2) {
                logger.log(Level.WARNING, cause2.getMessage(), cause2);
            }
            throw ThrowableUtil.wrap(cause);
        }

    }

    /**
     * Commits this global transaction.
     */
    protected void commitGlobalTransactionInternally() {
        getCurrentTransactionStack().remove(this);
        try {
            DatastoreUtil.put(ds, localTransaction, toEntity());
            submitRollForwardJob(
                localTransaction,
                globalTransactionKey,
                ROLL_FORWARD_DELAY);
            localTransaction.commit();
        } catch (Throwable cause) {
            try {
                if (localTransaction.isActive()) {
                    localTransaction.rollback();
                }
            } catch (Throwable cause2) {
                logger.log(Level.WARNING, cause2.getMessage(), cause2);
            }
            try {
                GlobalTransaction gtx =
                    getOrNull(ds, (Transaction) null, globalTransactionKey);
                if (gtx != null && gtx.valid) {
                    return;
                }
            } catch (Throwable cause2) {
                logger.log(Level.WARNING, cause2.getMessage(), cause2);
            }
            try {
                Journal.deleteInTx(ds, globalTransactionKey);
            } catch (Throwable cause2) {
                logger.log(Level.WARNING, cause2.getMessage(), cause2);
            }
            try {
                unlock();
            } catch (Throwable cause2) {
                logger.log(Level.WARNING, cause2.getMessage(), cause2);
            }
            throw ThrowableUtil.wrap(cause);
        }
    }

    /**
     * Rolls back this transaction as local transaction.
     */
    protected void rollbackLocalTransaction() {
        unlock();
    }

    /**
     * Rolls back this transaction as global transaction.
     */
    protected void rollbackGlobalTransaction() {
        Journal.deleteInTx(ds, globalTransactionKey);
        unlock();
    }

    /**
     * Rolls back this transaction as global transaction asynchronously.
     */
    protected void rollbackAsyncGlobalTransaction() {
        submitRollbackJob(globalTransactionKey);
        getCurrentTransactionStack().remove(this);
        if (localTransaction.isActive()) {
            localTransaction.rollback();
        }
    }
}