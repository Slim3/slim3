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
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slim3.util.ArraySet;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

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
     * The "applied" status.
     */
    protected static final String APPLIED_STATUS = "applied";

    /**
     * The "rolledBack" status.
     */
    protected static final String ROLLED_BACK_STATUS = "rolledBack";

    /**
     * The value for putting.
     */
    protected static final String PUT_TYPE = "put";

    /**
     * The value for deleting.
     */
    protected static final String DELETE_TYPE = "delete";

    /**
     * The 30 seconds as millisecond.
     */
    protected static final long THIRTY_SECONDS = 30 * 1000;

    /**
     * The max retry count.
     */
    protected static final int MAX_RETRY = 10;

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
        Entity gtx = Datastore.get(gtxKey);
        List<Key> targetKeyList =
            (List<Key>) gtx.getProperty(TARGET_KEY_LIST_PROPERTY);
        if (targetKeyList == null) {
            throw new NullPointerException(
                "The targetKeyList property must not be null.");
        }
        List<Key> lockKeyList = toLockKeyList(targetKeyList);
        Map<Key, Entity> map = Datastore.getAsMap(lockKeyList);
        for (Key key : lockKeyList) {
            Entity lockEntity = map.get(key);
            if (lockEntity == null
                || !gtx.getKey().equals(
                    lockEntity.getProperty(GLOBAL_TRANSACTION_KEY_PROPERTY))) {
                continue;
            }
            if (!deleteInTx(key, createJournalKey(key.getParent()))) {
                throw new IllegalStateException(
                    "Rolling back the global transaction("
                        + gtx.getKey()
                        + ") failed.");
            }
        }
    }

    /**
     * Deletes entities specified by the keys.
     * 
     * @param keys
     *            the keys
     * @return whether this operation succeeded
     */
    protected static boolean deleteInTx(Key... keys) {
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
     * Registers an entry for putting.
     * 
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     * @throws IllegalArgumentException
     *             if the key is incomplete
     */
    protected Key put(Entity entity) throws NullPointerException,
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
     * Registers an entry for deleting.
     * 
     * @param key
     *            the key
     * @throws NullPointerException
     *             if the key parameter is null
     */
    protected void delete(Key key) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        entrySet.add(new TxEntry(key));
    }

    /**
     * Commits this transaction.
     * 
     * @throws IllegalStateException
     *             if an error occurs while committing this transaction
     */
    protected void commit() throws IllegalStateException {
        startTransactionInternally();
        if (!writeJournals()) {
            throw new IllegalStateException(
                "Writing journals failed, so this transaction was rolled back automatically.");
        }
    }

    /**
     * Starts a transaction internally.
     */
    protected void startTransactionInternally() {
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
     * @return whether this operation succeeded
     */
    protected boolean writeJournals() {
        for (int i = 0; i < entrySet.size(); i++) {
            if (!entrySet.get(i).writeJournal()) {
                for (int j = 0; j < i; j++) {
                    deleteInTx(
                        entrySet.get(j).lockKey,
                        entrySet.get(j).journalKey);
                }
                deleteInTx(txEntity.getKey());
                return false;
            }
        }
        return true;
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
            return !STARTED_STATUS.equals(status)
                && !ROLLED_BACK_STATUS.equals(status);
        }
    }
}