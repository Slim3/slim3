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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.apphosting.api.DatastorePb.PutRequest;
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
     * The put property prefix.
     */
    public static final String PUT_PROPERTY_PREFIX = "put";

    /**
     * The delete property prefix.
     */
    public static final String DELETE_PROPERTY_PREFIX = "delete";

    /**
     * The maximum size(bytes) of entity.
     */
    public static final int MAX_ENTITY_SIZE = 1000000;

    /**
     * The extra size.
     */
    public static final int EXTRA_SIZE = 200;

    /**
     * Applies the journals.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     * 
     */
    public static void apply(Key globalTransactionKey)
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
        for (Entity entity : entities) {
            Map<String, Object> properties = entity.getProperties();
            PutRequest putReq = new PutRequest();
            List<Key> deleteKeys = new ArrayList<Key>();
            for (Iterator<String> i = properties.keySet().iterator(); i
                .hasNext();) {
                String name = i.next();
                if (name.startsWith(PUT_PROPERTY_PREFIX)) {
                    Blob blob = (Blob) entity.getProperty(name);
                    if (blob != null) {
                        EntityProto proto = putReq.addEntity();
                        proto.mergeFrom(blob.getBytes());
                    }
                } else if (name.startsWith(DELETE_PROPERTY_PREFIX)) {
                    Key key = (Key) entity.getProperty(name);
                    if (key != null) {
                        deleteKeys.add(key);
                    }
                }
            }
            if (putReq.entitySize() > 0) {
                DatastoreUtil.putInternally(putReq);
            }
            if (deleteKeys.size() > 0) {
                Datastore.deleteWithoutTx(deleteKeys);
            }
            Datastore.deleteWithoutTx(entity.getKey());
        }
    }

    /**
     * Puts the journals to the datastore.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param journalMap
     *            the map of journals
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             journalMap parameter is null
     * 
     */
    public static void put(Key globalTransactionKey, Map<Key, Entity> journalMap)
            throws NullPointerException {
        if (journalMap == null) {
            throw new NullPointerException(
                "The journalMap parameter must not be null.");
        }
        if (journalMap.size() == 0) {
            return;
        }
        int totalSize = 0;
        Entity entity = createEntity(globalTransactionKey);
        int propertyIndex = 0;
        for (Iterator<Key> i = journalMap.keySet().iterator(); i.hasNext();) {
            Key key = i.next();
            Entity targetEntity = journalMap.get(key);
            boolean put = targetEntity != null;
            EntityProto targetProto =
                put ? EntityTranslator.convertToPb(targetEntity) : null;
            int size = put ? targetProto.encodingSize() : 0;
            if (totalSize != 0
                && totalSize + size + EXTRA_SIZE > MAX_ENTITY_SIZE) {
                Datastore.putWithoutTx(entity);
                entity = createEntity(globalTransactionKey);
                totalSize = 0;
            }
            if (put) {
                byte[] content = new byte[targetProto.encodingSize()];
                targetProto.outputTo(content, 0);
                entity.setUnindexedProperty(PUT_PROPERTY_PREFIX
                    + propertyIndex++, new Blob(content));
            } else {
                entity.setUnindexedProperty(DELETE_PROPERTY_PREFIX
                    + propertyIndex++, key);
            }
            totalSize += size + EXTRA_SIZE;
        }
        Datastore.putWithoutTx(entity);
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
     * Returns the keys specified by the global transaction key.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @return a list of keys
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     * 
     */
    protected static List<Key> getKeys(Key globalTransactionKey)
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
     * Creates an entity.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @return an entity
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    protected static Entity createEntity(Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        Entity entity = new Entity(Datastore.allocateId(KIND));
        entity.setProperty(
            GLOBAL_TRANSACTION_KEY_PROPERTY,
            globalTransactionKey);
        return entity;
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
                Entity entity = Datastore.getOrNull(tx, key);
                if (entity != null
                    && globalTransactionKey.equals(entity
                        .getProperty(GLOBAL_TRANSACTION_KEY_PROPERTY))) {
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
     * Constructor.
     * 
     */
    private Journal() {
    }
}