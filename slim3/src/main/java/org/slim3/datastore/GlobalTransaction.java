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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slim3.util.ArraySet;
import org.slim3.util.ThrowableUtil;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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
    protected static final String GLOBAL_TRANSACTION_KIND =
        "slim3.GlobalTransaction";

    /**
     * The kind of lock entity.
     */
    protected static final String LOCK_KIND = "slim3.Lock";

    /**
     * The kind of journal entity.
     */
    protected static final String JOURNAL_KIND = "slim3.Journal";

    /**
     * The startTime property name.
     */
    protected static final String START_TIME_PROPERTY = "startTime";

    /**
     * The targetKeyList property name.
     */
    protected static final String TARGET_KEY_LIST_PROPERTY = "targetKeyList";

    /**
     * The status property name.
     */
    protected static final String STATUS_PROPERTY = "status";

    /**
     * The globalTransactionKey property name.
     */
    protected static final String GLOBAL_TRANSACTION_KEY_PROPERTY =
        "globalTransactionKey";

    /**
     * The content property name.
     */
    protected static final String CONTENT_PROPERTY = "content";

    /**
     * The "started" status.
     */
    protected static final String STARTED_STATUS = "started";

    /**
     * The "committed" status.
     */
    protected static final String COMMITTED_STATUS = "committed";

    /**
     * The 30 seconds as millisecond.
     */
    protected static final long THIRTY_SECONDS = 30 * 1000;

    /**
     * The max retry count.
     */
    protected static final int MAX_RETRY = 10;

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
     * The logger.
     */
    private static final Logger logger =
        Logger.getLogger(GlobalTransaction.class.getName());
    /**
     * The time that this transaction started.
     */
    protected long startTime = System.currentTimeMillis();

    /**
     * The transaction entity.
     */
    protected Entity txEntity;

    /**
     * The set of {@link TxEntry}.
     */
    protected ArraySet<TxEntry> entrySet = new ArraySet<TxEntry>();

    /**
     * Creates a key for lock.
     * 
     * @param targetKey
     *            the target key
     * @return a key for lock
     * @throws NullPointerException
     *             if the targetKey parameter is null
     */
    protected static Key createLockKey(Key targetKey)
            throws NullPointerException {
        if (targetKey == null) {
            throw new NullPointerException("The target key must not be null.");
        }
        return KeyFactory.createKey(targetKey, LOCK_KIND, 1);
    }

    /**
     * Creates a key for journal.
     * 
     * @param targetKey
     *            the target key
     * @return a key for journal
     * @throws NullPointerException
     *             if the targetKey parameter is null
     */
    protected static Key createJournalKey(Key targetKey)
            throws NullPointerException {
        if (targetKey == null) {
            throw new NullPointerException("The target key must not be null.");
        }
        return KeyFactory.createKey(targetKey, JOURNAL_KIND, 1);
    }

    /**
     * Converts the list of target keys to a list of keys for lock.
     * 
     * @param targetKeyList
     *            the list of target keys
     * @return a list of keys for lock
     * @throws NullPointerException
     *             if the targetKeyList parameter is null
     */
    protected static List<Key> toLockKeyList(List<Key> targetKeyList)
            throws NullPointerException {
        if (targetKeyList == null) {
            throw new NullPointerException(
                "The targetKeyList parameter must not be null.");
        }
        List<Key> lockKeyList = new ArrayList<Key>(targetKeyList.size());
        for (Key key : targetKeyList) {
            lockKeyList.add(createLockKey(key));
        }
        return lockKeyList;
    }

    /**
     * Converts the list of target keys to a list of keys for journal.
     * 
     * @param targetKeyList
     *            the list of target keys
     * @return a list of keys for journal
     * @throws NullPointerException
     *             if the targetKeyList parameter is null
     */
    protected static List<Key> toJournalKeyList(List<Key> targetKeyList)
            throws NullPointerException {
        if (targetKeyList == null) {
            throw new NullPointerException(
                "The targetKeyList parameter must not be null.");
        }
        List<Key> journalKeyList = new ArrayList<Key>(targetKeyList.size());
        for (Key key : targetKeyList) {
            journalKeyList.add(createJournalKey(key));
        }
        return journalKeyList;
    }

    /**
     * Rolls forward the transaction.
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
    @SuppressWarnings("unchecked")
    protected static void rollForward(Key gtxKey) throws NullPointerException,
            EntityNotFoundRuntimeException, IllegalStateException {
        if (gtxKey == null) {
            throw new NullPointerException(
                "The gtxKey parameter must not be null.");
        }
        if (!GLOBAL_TRANSACTION_KIND.equals(gtxKey.getKind())) {
            throw new IllegalArgumentException("The kind of the key("
                + gtxKey
                + ") must be "
                + GLOBAL_TRANSACTION_KIND
                + ".");
        }
        Transaction tx = Datastore.beginTransaction();
        try {
            Entity gtx = Datastore.get(tx, gtxKey);
            if (!COMMITTED_STATUS.equals(gtx.getProperty(STATUS_PROPERTY))) {
                return;
            }
            List<Key> targetKeyList =
                (List<Key>) gtx.getProperty(TARGET_KEY_LIST_PROPERTY);
            if (targetKeyList == null) {
                throw new NullPointerException(
                    "The targetKeyList property must not be null.");
            }
            List<Key> journalKeyList = toJournalKeyList(targetKeyList);
            Map<Key, Entity> map =
                Datastore.getAsMap((Transaction) null, journalKeyList);
            for (Key key : journalKeyList) {
                Entity journalEntity = map.get(key);
                if (journalEntity == null
                    || !gtxKey.equals(journalEntity
                        .getProperty(GLOBAL_TRANSACTION_KEY_PROPERTY))) {
                    continue;
                }
                if (!applyJournal(journalEntity)) {
                    throw new IllegalStateException(
                        "Rolling forward the global transaction("
                            + gtx.getKey()
                            + ") failed.");
                }
            }
            Datastore.delete(tx, gtxKey);
            Datastore.commit(tx);
        } finally {
            if (tx.isActive()) {
                Datastore.rollback(tx);
            }
        }
    }

    /**
     * Applies a journal in transaction.
     * 
     * @param journalEntity
     *            the entity for journal
     * @return whether this operation succeeded
     */
    protected static boolean applyJournal(Entity journalEntity) {
        Blob blob = (Blob) journalEntity.getProperty(CONTENT_PROPERTY);
        Key journalKey = journalEntity.getKey();
        Key targetKey = journalKey.getParent();
        for (int i = 0; i < MAX_RETRY; i++) {
            Transaction tx = Datastore.beginTransaction();
            try {
                if (blob != null) {
                    Entity targetEntity =
                        DatastoreUtil.bytesToEntity(blob.getBytes());
                    Datastore.put(tx, targetEntity);
                    Datastore.delete(tx, createLockKey(targetKey), journalKey);
                } else {
                    Datastore.delete(
                        tx,
                        targetKey,
                        createLockKey(targetKey),
                        journalKey);
                }
                Datastore.commit(tx);
                return true;
            } catch (ConcurrentModificationException ignore) {
            } catch (Throwable cause) {
                logger.log(Level.WARNING, cause.getMessage(), cause);
                return false;
            } finally {
                if (tx.isActive()) {
                    Datastore.rollback(tx);
                }
            }
        }
        return false;
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
    @SuppressWarnings("unchecked")
    protected static void rollback(Key gtxKey) throws NullPointerException,
            EntityNotFoundRuntimeException, IllegalStateException {
        if (gtxKey == null) {
            throw new NullPointerException(
                "The gtxKey parameter must not be null.");
        }
        if (!GLOBAL_TRANSACTION_KIND.equals(gtxKey.getKind())) {
            throw new IllegalArgumentException("The kind of the key("
                + gtxKey
                + ") must be "
                + GLOBAL_TRANSACTION_KIND
                + ".");
        }
        Transaction tx = Datastore.beginTransaction();
        try {
            Entity gtx = Datastore.get(tx, gtxKey);
            if (!STARTED_STATUS.equals(gtx.getProperty(STATUS_PROPERTY))) {
                return;
            }
            List<Key> targetKeyList =
                (List<Key>) gtx.getProperty(TARGET_KEY_LIST_PROPERTY);
            if (targetKeyList == null) {
                throw new NullPointerException(
                    "The targetKeyList property must not be null.");
            }
            List<Key> lockKeyList = toLockKeyList(targetKeyList);
            Map<Key, Entity> map =
                Datastore.getAsMap((Transaction) null, lockKeyList);
            for (Key key : lockKeyList) {
                Entity lockEntity = map.get(key);
                if (lockEntity == null
                    || !gtxKey.equals(lockEntity
                        .getProperty(GLOBAL_TRANSACTION_KEY_PROPERTY))) {
                    continue;
                }
                if (!deleteJournal(key, createJournalKey(key.getParent()))) {
                    throw new IllegalStateException(
                        "Rolling back the global transaction("
                            + gtx.getKey()
                            + ") failed.");
                }
            }
            Datastore.delete(tx, gtxKey);
            Datastore.commit(tx);
        } finally {
            if (tx.isActive()) {
                Datastore.rollback(tx);
            }
        }
    }

    /**
     * Deletes entities for lock and journal.
     * 
     * @param keys
     *            the keys
     * @return whether this operation succeeded
     */
    protected static boolean deleteJournal(Key... keys) {
        for (int i = 0; i < MAX_RETRY; i++) {
            Transaction tx = Datastore.beginTransaction();
            try {
                Datastore.delete(tx, keys);
                Datastore.commit(tx);
                return true;
            } catch (ConcurrentModificationException ignore) {
            } catch (Throwable cause) {
                logger.log(Level.WARNING, cause.getMessage(), cause);
                return false;
            } finally {
                if (tx.isActive()) {
                    Datastore.rollback(tx);
                }
            }
        }
        return false;
    }

    /**
     * Constructor.
     */
    public GlobalTransaction() {
    }

    /**
     * Puts the model.
     * 
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     * @throws IllegalArgumentException
     *             if the key is incomplete
     */
    public Key put(Object model) throws NullPointerException,
            IllegalArgumentException {
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
     * Puts the entity.
     * 
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     * @throws IllegalArgumentException
     *             if the key is incomplete
     */
    public Key put(Entity entity) throws NullPointerException,
            IllegalArgumentException {
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        Key key = entity.getKey();
        if (DatastoreUtil.isIncomplete(key)) {
            throw new IllegalArgumentException("The key is incomplete.");
        }
        entrySet.add(new TxEntry(key, DatastoreUtil.entityToBytes(entity)));
        return key;
    }

    /**
     * Puts the models.
     * 
     * @param models
     *            the models
     * @return a list of keys
     * @throws NullPointerException
     *             if the models parameter is null
     * @throws IllegalArgumentException
     *             if the key is incomplete
     */
    public List<Key> put(Iterable<?> models) throws NullPointerException,
            IllegalArgumentException {
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
     * Puts the models.
     * 
     * @param models
     *            the models
     * @return a list of keys
     * @throws IllegalArgumentException
     *             if the key is incomplete
     */
    public List<Key> put(Object... models) throws IllegalArgumentException {
        return put(Arrays.asList(models));
    }

    /**
     * Deletes the entity.
     * 
     * @param key
     *            the key
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public void delete(Key key) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        entrySet.add(new TxEntry(key));
    }

    /**
     * Deletes the models.
     * 
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public void delete(Iterable<Key> keys) throws NullPointerException {
        if (keys == null) {
            throw new NullPointerException(
                "The keys parameter must not be null.");
        }
        for (Key key : keys) {
            delete(key);
        }
    }

    /**
     * Deletes the models.
     * 
     * @param keys
     *            the keys
     */
    public void delete(Key... keys) {
        delete(Arrays.asList(keys));
    }

    /**
     * Commits this transaction. If an exception occurred while committing, the
     * transaction will be rolled back automatically.
     * 
     * @throws IllegalStateException
     *             if writing journals failed or if this transaction has already
     *             begun
     */
    public void commit() throws IllegalStateException {
        startTransactionInternally();
        writeJournals();
        commitTransactionInternally();
    }

    /**
     * Starts a transaction internally.
     * 
     * @throws IllegalStateException
     *             if this transaction has already begun
     */
    protected void startTransactionInternally() throws IllegalStateException {
        if (txEntity != null) {
            throw new IllegalStateException(
                "This transaction has already begun.");
        }
        txEntity = new Entity(GLOBAL_TRANSACTION_KIND);
        txEntity.setProperty(START_TIME_PROPERTY, startTime);
        List<Key> targetKeyList = new ArrayList<Key>();
        for (TxEntry e : entrySet) {
            targetKeyList.add(e.targetKey);
        }
        txEntity.setUnindexedProperty(TARGET_KEY_LIST_PROPERTY, targetKeyList);
        txEntity.setProperty(STATUS_PROPERTY, STARTED_STATUS);
        Datastore.put(txEntity);
    }

    /**
     * Writes journals.
     * 
     */
    protected void writeJournals() {
        for (int i = 0; i < entrySet.size(); i++) {
            if (!entrySet.get(i).writeJournal()) {
                for (int j = 0; j < i; j++) {
                    deleteJournal(
                        entrySet.get(j).lockKey,
                        entrySet.get(j).journalKey);
                }
                delete(txEntity.getKey());
                throw new IllegalStateException(
                    "Writing journal(key:"
                        + entrySet.get(i).targetKey
                        + ") failed, so this transaction was rolled back automatically.");
            }
        }
    }

    /**
     * Starts a transaction internally.
     */
    protected void commitTransactionInternally() {
        Transaction tx = Datastore.beginTransaction();
        try {
            txEntity.setProperty(STATUS_PROPERTY, COMMITTED_STATUS);
            Datastore.put(tx, txEntity);
            Queue queue = QueueFactory.getQueue(QUEUE_NAME);
            String encodedKey =
                URLEncoder.encode(
                    Datastore.keyToString(txEntity.getKey()),
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

    /**
     * An entry for global transaction.
     * 
     */
    protected class TxEntry {

        /**
         * The target key.
         */
        protected Key targetKey;

        /**
         * The target content.
         */
        protected byte[] targetContent;

        /**
         * The key for lock.
         */
        protected Key lockKey;

        /**
         * The key for journal.
         */
        protected Key journalKey;

        /**
         * Constructor.
         * 
         * @param targetKey
         *            the target key
         * @throws NullPointerException
         *             if the targetKey parameter is null
         */
        public TxEntry(Key targetKey) throws NullPointerException {
            this(targetKey, null);
        }

        /**
         * Constructor.
         * 
         * @param targetKey
         *            the target key
         * @param targetContent
         *            the target content
         * @throws NullPointerException
         *             if the targetKey parameter is null
         */
        public TxEntry(Key targetKey, byte[] targetContent)
                throws NullPointerException {
            if (targetKey == null) {
                throw new NullPointerException(
                    "The targetKey parameter must not be null.");
            }
            this.targetKey = targetKey;
            this.targetContent = targetContent;
            lockKey = createLockKey(targetKey);
            journalKey = createJournalKey(targetKey);
        }

        /**
         * Writes a journal.
         * 
         * @return whether Writing a journal succeeded.
         */
        protected boolean writeJournal() {
            Transaction tx = Datastore.beginTransaction();
            try {
                try {
                    if (isLocked(Datastore.get(tx, lockKey))) {
                        return false;
                    }
                } catch (EntityNotFoundRuntimeException ignore) {
                }
                Datastore.put(tx, createLockEntity(), createJournalEntity());
                Datastore.commit(tx);
                return true;
            } catch (Throwable cause) {
                logger.log(Level.WARNING, cause.getMessage(), cause);
                return false;
            } finally {
                if (tx.isActive()) {
                    Datastore.rollback(tx);
                }
            }
        }

        /**
         * Creates an entity for lock.
         * 
         * @return an entity for lock
         */
        protected Entity createLockEntity() {
            Entity entity = new Entity(lockKey);
            entity.setProperty(START_TIME_PROPERTY, startTime);
            entity.setProperty(GLOBAL_TRANSACTION_KEY_PROPERTY, txEntity
                .getKey());
            return entity;
        }

        /**
         * Creates an entity for journal.
         * 
         * @return an entity for journal
         * @throws NullPointerException
         *             if the targetContent field is null
         */
        protected Entity createJournalEntity() throws NullPointerException {
            Entity entity = new Entity(journalKey);
            if (targetContent != null) {
                entity.setUnindexedProperty(CONTENT_PROPERTY, new Blob(
                    targetContent));
            }
            entity.setProperty(GLOBAL_TRANSACTION_KEY_PROPERTY, txEntity
                .getKey());
            return entity;
        }

        /**
         * Determines if the entity is locked.
         * 
         * @param lockEntity
         *            the entity for lock
         * @return whether the entity is locked
         * @throws NullPointerException
         *             if the lockEntity parameter is null or if the startTime
         *             property is null or if the globalTransactionKey property
         *             is null
         */
        protected boolean isLocked(Entity lockEntity)
                throws NullPointerException {
            if (lockEntity == null) {
                throw new NullPointerException(
                    "The lockEntity parameter must not be null.");
            }
            Long lockStartTime =
                (Long) lockEntity.getProperty(START_TIME_PROPERTY);
            if (lockStartTime == null) {
                throw new NullPointerException(
                    "The startTime property must not be null.");
            }
            if (startTime <= lockStartTime + THIRTY_SECONDS) {
                return true;
            }
            Key gtxKey =
                (Key) lockEntity.getProperty(GLOBAL_TRANSACTION_KEY_PROPERTY);
            if (gtxKey == null) {
                throw new NullPointerException(
                    "The globalTransactionKey property must not be null.");
            }
            Entity gtx = Datastore.get((Transaction) null, gtxKey);
            String status = (String) gtx.getProperty(STATUS_PROPERTY);
            return !STARTED_STATUS.equals(status);
        }
    }
}