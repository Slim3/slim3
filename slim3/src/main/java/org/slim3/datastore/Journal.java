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
import com.google.appengine.api.datastore.Query.FilterOperator;
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
     * The maximum size(bytes) of content.
     */
    public static final int MAX_CONTENT_SIZE = 1000000;

    /**
     * The extra size.
     */
    public static final int EXTRA_SIZE = 200;

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
        return KeyFactory.createKey(KIND, KeyFactory.keyToString(targetKey));
    }

    /**
     * Applies the journals.
     * 
     * @param journals
     *            the journals
     * @throws NullPointerException
     *             if the journals parameter is null
     * 
     */
    public static void apply(Iterable<Journal> journals)
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
            if (journal.contentSize == 0) {
                deleteKeys.add(journal.targetKey);
                deleteJournalKeys.add(journal.key);
            } else {
                if (totalSize + journal.contentSize > MAX_CONTENT_SIZE) {
                    Datastore.putWithoutTx(putEntities);
                    Datastore.deleteWithoutTx(putJournalKeys);
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
            Datastore.deleteWithoutTx(deleteKeys);
            Datastore.deleteWithoutTx(deleteJournalKeys);
        }
        if (putEntities.size() > 0) {
            Datastore.putWithoutTx(putEntities);
            Datastore.deleteWithoutTx(putJournalKeys);
        }
    }

    /**
     * Returns the keys specified by the global transaction key.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @return a list of keys
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     * 
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
            if (totalSize + journal.contentSize + EXTRA_SIZE > MAX_CONTENT_SIZE) {
                Datastore.putWithoutTx(entities);
                entities.clear();
                totalSize = 0;
            }
            totalSize += journal.contentSize + EXTRA_SIZE;
            entities.add(journal.toEntity());
        }
        if (entities.size() > 0) {
            Datastore.putWithoutTx(entities);
        }
    }

    /**
     * Deletes entities specified by the global transaction key in transaction.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    public static void deleteInTx(Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        List<Key> keys = getKeys(globalTransactionKey);
        for (Key key : keys) {
            deleteInTx(globalTransactionKey, key);
        }
    }

    /**
     * Deletes the journals in transaction.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param journals
     *            the journals
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             journals parameter is null
     */
    public static void deleteInTx(Key globalTransactionKey,
            Iterable<Journal> journals) throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (journals == null) {
            throw new NullPointerException(
                "The journals parameter must not be null.");
        }
        for (Journal journal : journals) {
            deleteInTx(globalTransactionKey, journal.key);
        }
    }

    /**
     * Deletes an entity specified by the key in transaction.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param key
     *            the key
     * 
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the key
     *             parameter is null
     */
    protected static void deleteInTx(Key globalTransactionKey, Key key)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        for (int i = 0; i < DatastoreUtil.MAX_RETRY; i++) {
            Transaction tx = Datastore.beginTransaction();
            try {
                Journal journal = getOrNull(tx, key);
                if (journal != null
                    && globalTransactionKey
                        .equals(journal.globalTransactionKey)) {
                    Datastore.delete(tx, key);
                    tx.commit();
                }
                return;
            } catch (ConcurrentModificationException e) {
                continue;
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        }
    }

    /**
     * Rolls forward the global transaction.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    public static void rollForward(Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        List<Entity> entities =
            Datastore.query(KIND).filter(
                GLOBAL_TRANSACTION_KEY_PROPERTY,
                FilterOperator.EQUAL,
                globalTransactionKey).asList();
        List<Journal> journals = new ArrayList<Journal>(entities.size());
        for (Entity e : entities) {
            journals.add(toJournal(e));
        }
        apply(journals);
    }

    /**
     * Converts the entity to a {@link Journal}.
     * 
     * @param entity
     *            the entity
     * @return a {@link Journal}
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public static Journal toJournal(Entity entity) throws NullPointerException {
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
        byte[] content = null;
        Blob blob = (Blob) entity.getProperty(CONTENT_PROPERTY);
        if (blob != null) {
            content = blob.getBytes();
        }
        return new Journal(entity.getKey(), globalTransactionKey, content);
    }

    /**
     * Returns a {@link Journal} specified by the key. Returns null if no entity
     * is found.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return a {@link Lock} specified by the key
     */
    public static Journal getOrNull(Transaction tx, Key key) {
        Entity entity = Datastore.getOrNull(tx, key);
        if (entity == null) {
            return null;
        }
        return toJournal(entity);
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
    }

    /**
     * Constructor.
     * 
     * @param key
     *            the key
     * @param globalTransactionKey
     *            the global transaction key
     * @param content
     *            the content of target entity
     * 
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Journal(Key key, Key globalTransactionKey, byte[] content)
            throws NullPointerException {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        this.key = key;
        this.targetKey = KeyFactory.stringToKey(key.getName());
        this.globalTransactionKey = globalTransactionKey;
        if (content != null) {
            this.content = content;
            targetEntityProto = new EntityProto();
            targetEntityProto.mergeFrom(content);
            contentSize = targetEntityProto.encodingSize();
            targetEntity = EntityTranslator.createFromPb(targetEntityProto);
        }
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
}