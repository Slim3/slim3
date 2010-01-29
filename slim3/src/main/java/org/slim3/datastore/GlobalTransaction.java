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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.slim3.util.ThrowableUtil;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;

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
     * The timestamp property name.
     */
    public static final String TIMESTAMP_PROPERTY = "timestamp";

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
     * The logger.
     */
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
     * The set of root keys.
     */
    protected Set<Key> rootKeySet = new HashSet<Key>();

    /**
     * Determines if the global transaction exists.
     * 
     * @param key
     *            the global transaction key
     * @return whether the global transaction exists
     */
    public static boolean exists(Key key) {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        if (!KIND.equals(key.getKind())) {
            throw new IllegalArgumentException("The kind("
                + key.getKind()
                + ") of the key("
                + key
                + ") must be "
                + KIND
                + ".");
        }
        return !Datastore.getAsMapWithoutTx(key).isEmpty();
    }

    /**
     * Rolls forward the transaction.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             targetKeyList property is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from
     *             slim3.GlobalTransaction
     */
    public static void rollForward(Key globalTransactionKey)
            throws NullPointerException, IllegalArgumentException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The gtxKey parameter must not be null.");
        }
        if (!KIND.equals(globalTransactionKey.getKind())) {
            throw new IllegalArgumentException("The kind of the key("
                + globalTransactionKey
                + ") must be "
                + KIND
                + ".");
        }
    }

    /**
     * Rolls back the transaction.
     * 
     * @param gtxKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the gtxKey parameter is null or if the targetKeyList
     *             property is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key is found
     * @throws IllegalStateException
     *             if deleting journals failed
     */
    protected static void rollback(Key gtxKey) throws NullPointerException,
            EntityNotFoundRuntimeException, IllegalStateException {
        if (gtxKey == null) {
            throw new NullPointerException(
                "The gtxKey parameter must not be null.");
        }
        if (!KIND.equals(gtxKey.getKind())) {
            throw new IllegalArgumentException("The kind of the key("
                + gtxKey
                + ") must be "
                + KIND
                + ".");
        }
    }

    /**
     * Constructor.
     */
    public GlobalTransaction() {
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
        lock(key);
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
        lock(key);
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
        lock(key);
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
            lock(key);
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
            lock(key);
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
            lock(key);
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
            lock(key);
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
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public <M> List<M> get(Class<M> modelClass, Key... keys)
            throws ConcurrentModificationException {
        for (Key key : keys) {
            lock(key);
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
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public <M> List<M> get(ModelMeta<M> modelMeta, Key... keys)
            throws ConcurrentModificationException {
        for (Key key : keys) {
            lock(key);
        }
        return Datastore.getWithoutTx(modelMeta, keys);
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
     *             if the key is incomplete or if the size of the entity is more
     *             than 980,000 bytes
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public Key put(Entity entity) throws NullPointerException,
            IllegalArgumentException, ConcurrentModificationException {
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        Key key = entity.getKey();
        if (DatastoreUtil.isIncomplete(key)) {
            throw new IllegalArgumentException("The key is incomplete.");
        }
        lock(key);
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
     *             if the key is incomplete or if the size of the entity is more
     *             than 980,000 bytes
     * @throws ConcurrentModificationException
     *             if locking the entity specified by the key failed
     */
    public Key put(Object model) throws NullPointerException,
            IllegalArgumentException, ConcurrentModificationException {
        if (model == null) {
            throw new NullPointerException(
                "The model parameter must not be null.");
        }
        ModelMeta<?> modelMeta = DatastoreUtil.getModelMeta(model.getClass());
        if (modelMeta.getKey(model) == null) {
            modelMeta.setKey(model, Datastore.allocateId(modelMeta));
        }
        Entity entity =
            DatastoreUtil.updatePropertiesAndConvertToEntity(modelMeta, model);
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
     *             if the key is incomplete or if the size of the entity is more
     *             than 980,000 bytes
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
     *             if the key is incomplete or if the size of the entity is more
     *             than 980,000 bytes
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
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        lock(key);
        journalMap.remove(key);
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
     * Commits this transaction asynchronously using TaskQueue.
     */
    public void commitAsync() {
        active = false;
        if (lockMap.size() > 0) {
            Journal.put(journalMap.values());
            commitAsyncInternally();
        } else {
            Datastore.deleteWithoutTx(globalTransactionKey);
        }
    }

    /**
     * Lock the entity. If locking the entity failed, the other locks that this
     * transaction has are released automatically.
     * 
     * @param key
     *            a key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws ConcurrentModificationException
     *             if locking an entity specified by the key failed
     */
    protected void lock(Key key) throws NullPointerException,
            ConcurrentModificationException {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        assertActive();
        Lock lock = new Lock(globalTransactionKey, key, timestamp);
        Lock other = lockMap.get(key);
        if (other == null) {
            try {
                lock.lock();
            } catch (ConcurrentModificationException e) {
                active = false;
                unlock();
                throw e;
            }
            lockMap.put(key, lock);
        }
    }

    /**
     * Unlocks entities.
     */
    protected void unlock() {
        active = false;
        List<Key> keys = new ArrayList<Key>();
        for (Lock lock : lockMap.values()) {
            keys.add(lock.getKey());
        }
        Datastore.deleteWithoutTx(keys);
        lockMap.clear();
    }

    /**
     * Commits this transaction asynchronously.
     */
    protected void commitAsyncInternally() {
        Transaction tx = Datastore.beginTransaction();
        try {
            Queue queue = QueueFactory.getQueue(QUEUE_NAME);
            String encodedKey =
                URLEncoder.encode(
                    Datastore.keyToString(globalTransactionKey),
                    "UTF-8");
            queue.add(tx, TaskOptions.Builder
                .url(ROLLFORWARD_PATH + encodedKey));
            Datastore.commit(tx);
        } catch (UnsupportedEncodingException e) {
            ThrowableUtil.wrapAndThrow(e);
        } finally {
            if (tx.isActive()) {
                Datastore.rollback(tx);
            }
        }
    }
}