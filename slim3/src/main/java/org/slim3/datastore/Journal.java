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

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.storage.onestore.v3.OnestoreEntity.EntityProto;

/**
 * A class for journal.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class Journal {

    /**
     * The kind of journal entity.
     */
    public static final String KIND = "slim3.Journal";

    /**
     * The globalTransactionKey property name.
     */
    public static final String GLOBAL_TRANSACTION_KEY_PROPERTY =
        "globalTransactionKey";

    /**
     * The content property name.
     */
    public static final String CONTENT_PROPERTY = "content";

    /**
     * The deleteAll property name.
     */
    public static final String DELETE_ALL_PROPERTY = "deleteAll";

    /**
     * The maximum size(bytes) of content.
     */
    public static final int MAX_CONTENT_SIZE = 1000000;

    /**
     * The extra size.
     */
    public static final int EXTRA_SIZE = 200;

    /**
     * The maximum size of journals.
     */
    public static final int MAX_SIZE_JOURNALS = 100;

    /**
     * 
     */
    protected Key globalTransactionKey;

    /**
     * The key.
     */
    protected Key key;

    /**
     * The target key.
     */
    protected Key targetKey;

    /**
     * The target entity.
     */
    protected Entity targetEntity;

    /**
     * The target entity as protocol buffer.
     */
    protected EntityProto targetEntityProto;

    /**
     * The content size of target entity.
     */
    protected int contentSize;

    /**
     * The content of target entity.
     */
    protected byte[] content;

    /**
     * Whether this journal is "deleteAll()".
     */
    protected boolean deleteAll = false;

    /**
     * Creates a key.
     * 
     * @param targetKey
     *            the target key
     * @return a key
     * @throws NullPointerException
     *             if the targetKey parameter is null
     */
    public static Key createKey(Key targetKey) throws NullPointerException {
        if (targetKey == null) {
            throw new NullPointerException("The target key must not be null.");
        }
        return KeyFactory.createKey(targetKey, KIND, 1);
    }

    /**
     * Applies the journals within the local transaction.
     * 
     * @param tx
     *            the transaction
     * @param journals
     *            the journals
     * @throws NullPointerException
     *             if the tx parameter is null or if the journals parameter is
     *             null
     * 
     */
    public static void applyWithinLocalTransaction(Transaction tx,
            Iterable<Journal> journals) throws NullPointerException {
        if (tx == null) {
            throw new NullPointerException("The tx parameter must not be null.");
        }
        if (journals == null) {
            throw new NullPointerException(
                "The journals parameter must not be null.");
        }
        if (journals instanceof Collection<?>
            && ((Collection<?>) journals).size() == 0) {
            return;
        }
        int totalSize = 0;
        List<Entity> putEntities = new ArrayList<Entity>();
        List<Key> deleteKeys = new ArrayList<Key>();
        for (Journal journal : journals) {
            if (journal.deleteAll) {
                deleteAllInternally(tx, journal.targetKey);
            } else if (journal.contentSize == 0) {
                if (deleteKeys.size() >= MAX_SIZE_JOURNALS) {
                    deleteInternally(tx, deleteKeys);
                    deleteKeys.clear();
                }
                deleteKeys.add(journal.targetKey);
            } else {
                if (totalSize + journal.contentSize > MAX_CONTENT_SIZE
                    || putEntities.size() >= MAX_SIZE_JOURNALS) {
                    putInternally(tx, putEntities);
                    putEntities.clear();
                    totalSize = 0;
                }
                totalSize += journal.contentSize;
                putEntities.add(journal.getTargetEntity());
            }
        }
        if (deleteKeys.size() > 0) {
            deleteInternally(tx, deleteKeys);
        }
        if (putEntities.size() > 0) {
            putInternally(tx, putEntities);
        }
    }

    /**
     * Applies the journals within global transaction.
     * 
     * @param journals
     *            the journals
     * @throws NullPointerException
     *             if the journals parameter is null
     * 
     */
    public static void applyWithinGlobalTransaction(Iterable<Journal> journals)
            throws NullPointerException {
        if (journals == null) {
            throw new NullPointerException(
                "The journals parameter must not be null.");
        }
        if (journals instanceof Collection<?>
            && ((Collection<?>) journals).size() == 0) {
            return;
        }
        int totalSize = 0;
        List<Entity> putEntities = new ArrayList<Entity>();
        List<Key> deleteKeys = new ArrayList<Key>();
        List<Key> putJournalKeys = new ArrayList<Key>();
        List<Key> deleteJournalKeys = new ArrayList<Key>();
        for (Journal journal : journals) {
            if (journal.deleteAll) {
                deleteAllInternally(null, journal.targetKey);
                deleteAllInternally(null, journal.key);
            } else if (journal.contentSize == 0) {
                if (deleteKeys.size() >= MAX_SIZE_JOURNALS) {
                    deleteInternally(null, deleteKeys);
                    deleteInternally(null, deleteJournalKeys);
                    deleteKeys.clear();
                    deleteJournalKeys.clear();
                }
                deleteKeys.add(journal.targetKey);
                deleteJournalKeys.add(journal.key);
            } else {
                if (totalSize + journal.contentSize > MAX_CONTENT_SIZE
                    || putEntities.size() >= MAX_SIZE_JOURNALS) {
                    putInternally(null, putEntities);
                    deleteInternally(null, putJournalKeys);
                    putEntities.clear();
                    putJournalKeys.clear();
                    totalSize = 0;
                }
                totalSize += journal.contentSize;
                putEntities.add(journal.getTargetEntity());
                putJournalKeys.add(journal.key);
            }
        }
        if (deleteKeys.size() > 0) {
            deleteInternally(null, deleteKeys);
            deleteInternally(null, deleteJournalKeys);
        }
        if (putEntities.size() > 0) {
            putInternally(null, putEntities);
            deleteInternally(null, putJournalKeys);
        }
    }

    /**
     * Puts the journals to the datastore.
     * 
     * @param journals
     *            the journals
     * @throws NullPointerException
     *             if the journals parameter is null
     * 
     */
    public static void put(Iterable<Journal> journals)
            throws NullPointerException {
        if (journals == null) {
            throw new NullPointerException(
                "The journals parameter must not be null.");
        }
        if (journals instanceof Collection<?>
            && ((Collection<?>) journals).size() == 0) {
            return;
        }
        int totalSize = 0;
        List<Entity> entities = new ArrayList<Entity>();
        for (Journal journal : journals) {
            if (totalSize + journal.contentSize + EXTRA_SIZE > MAX_CONTENT_SIZE
                || entities.size() >= MAX_SIZE_JOURNALS) {
                putInternally(null, entities);
                entities.clear();
                totalSize = 0;
            }
            totalSize += journal.contentSize + EXTRA_SIZE;
            entities.add(journal.toEntity());
        }
        if (entities.size() > 0) {
            putInternally(null, entities);
        }
    }

    /**
     * Deletes the entities internally.
     * 
     * @param tx
     *            the transaction
     * @param entities
     *            the entities
     * @return keys
     */
    protected static List<Key> putInternally(Transaction tx,
            Iterable<Entity> entities) {
        ConcurrentModificationException cme = null;
        for (int i = 0; i < GlobalTransaction.MAX_RETRY; i++) {
            try {
                return Datastore.put(tx, entities);
            } catch (ConcurrentModificationException e) {
                cme = e;
            }
        }
        throw cme;
    }

    /**
     * Deletes the entities internally.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     */
    protected static void deleteInternally(Transaction tx, Iterable<Key> keys) {
        ConcurrentModificationException cme = null;
        for (int i = 0; i < GlobalTransaction.MAX_RETRY; i++) {
            try {
                Datastore.delete(tx, keys);
                return;
            } catch (ConcurrentModificationException e) {
                cme = e;
            }
        }
        throw cme;
    }

    /**
     * Deletes all descendant entities internally.
     * 
     * @param tx
     *            the transaction
     * @param ancestorKey
     *            the ancestor key
     */
    protected static void deleteAllInternally(Transaction tx, Key ancestorKey) {
        ConcurrentModificationException cme = null;
        for (int i = 0; i < GlobalTransaction.MAX_RETRY; i++) {
            try {
                Datastore.deleteAll(tx, ancestorKey);
                return;
            } catch (ConcurrentModificationException e) {
                cme = e;
            }
        }
        throw cme;
    }

    /**
     * Constructor for "put".
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param targetEntity
     *            the targetEntity
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             targetEntity parameter is null
     * @throws IllegalArgumentException
     *             if the size of target entity is more than 1,000,000 bytes
     */
    public Journal(Key globalTransactionKey, Entity targetEntity)
            throws NullPointerException, IllegalArgumentException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (targetEntity == null) {
            throw new NullPointerException(
                "The targetEntity parameter must not be null.");
        }
        this.globalTransactionKey = globalTransactionKey;
        this.targetEntity = targetEntity;
        this.targetKey = targetEntity.getKey();
        this.key = createKey(targetKey);
        targetEntityProto = EntityTranslator.convertToPb(targetEntity);
        contentSize = targetEntityProto.encodingSize();
        if (contentSize > MAX_CONTENT_SIZE) {
            throw new IllegalArgumentException(
                "The size of target entity must be less than or equals to 1,000,000 bytes.");
        }
    }

    /**
     * Constructor for "delete".
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param targetKey
     *            the targetKey
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             targetKey parameter is null
     */
    public Journal(Key globalTransactionKey, Key targetKey)
            throws NullPointerException {
        this(globalTransactionKey, targetKey, false);
    }

    /**
     * Constructor for "delete".
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param targetKey
     *            the targetKey
     * @param deleteAll
     *            whether this journal is "deleteAll()"
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             targetKey parameter is null
     */
    public Journal(Key globalTransactionKey, Key targetKey, boolean deleteAll)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (targetKey == null) {
            throw new NullPointerException(
                "The targetKey parameter must not be null.");
        }
        this.globalTransactionKey = globalTransactionKey;
        this.targetKey = targetKey;
        this.key = createKey(targetKey);
        this.deleteAll = deleteAll;
    }

    /**
     * Constructor.
     * 
     * @param key
     *            the key
     * @param content
     *            the content of target entity
     * 
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Journal(Key key, byte[] content) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        this.key = key;
        this.targetKey = key.getParent();
        this.content = content;
        this.targetEntity = DatastoreUtil.bytesToEntity(content);

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
        if (contentSize > 0) {
            entity.setUnindexedProperty(
                CONTENT_PROPERTY,
                new Blob(getContent()));
        }
        entity.setUnindexedProperty(DELETE_ALL_PROPERTY, deleteAll);
        return entity;
    }

    /**
     * Returns the global transaction key.
     * 
     * @return the global transactionKey
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
     * Returns the target key.
     * 
     * @return the target key
     */
    public Key getTargetKey() {
        return targetKey;
    }

    /**
     * Returns the target entity.
     * 
     * @return the target entity
     */
    public Entity getTargetEntity() {
        return targetEntity;
    }

    /**
     * Returns the target entity as protocol buffer.
     * 
     * @return the target entity as protocol buffer
     */
    public EntityProto getTargetEntityProto() {
        return targetEntityProto;
    }

    /**
     * @return the contentSize
     */
    public int getContentSize() {
        return contentSize;
    }

    /**
     * Returns the content.
     * 
     * @return the content
     */
    public byte[] getContent() {
        if (targetEntityProto != null && content == null) {
            content = new byte[contentSize];
            targetEntityProto.outputTo(content, 0);
        }
        return content;
    }

    /**
     * Determines if this journal is "deleteAll()".
     * 
     * @return the deleteAll whether this journal is "deleteAll()"
     */
    public boolean isDeleteAll() {
        return deleteAll;
    }
}