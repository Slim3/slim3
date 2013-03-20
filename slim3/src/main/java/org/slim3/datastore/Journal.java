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
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Transaction;
import com.google.apphosting.api.DatastorePb.PutRequest;
import com.google.storage.onestore.v3.OnestoreEntity.EntityProto;

/**
 * A class for journal.
 * 
 * @author higa
 * @since 1.0.0
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
     * The putList property prefix.
     */
    public static final String PUT_LIST_PROPERTY = "putList";

    /**
     * The deleteList property prefix.
     */
    public static final String DELETE_LIST_PROPERTY = "deleteList";

    /**
     * Applies the journals.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     * 
     */
    public static void apply(AsyncDatastoreService ds, Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        List<Entity> entities =
            new EntityQuery(ds, KIND).filter(
                new Query.FilterPredicate(
                GLOBAL_TRANSACTION_KEY_PROPERTY,
                FilterOperator.EQUAL,
                globalTransactionKey)).asList();
        apply(ds, entities);
    }

    /**
     * Applies the journals.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param entities
     *            the entities
     * @throws NullPointerException
     *             if the ds parameter is null or if the entities parameter is
     *             null
     * 
     */
    @SuppressWarnings("unchecked")
    public static void apply(AsyncDatastoreService ds, List<Entity> entities)
            throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (entities == null) {
            throw new NullPointerException(
                "The entities parameter must not be null.");
        }
        for (Entity entity : entities) {
            PutRequest putReq = new PutRequest();
            List<Blob> putList =
                (List<Blob>) entity.getProperty(PUT_LIST_PROPERTY);
            List<Key> deleteList =
                (List<Key>) entity.getProperty(DELETE_LIST_PROPERTY);
            List<Entity> putEntities = new ArrayList<Entity>();
            if (putList != null) {
                for (Blob blob : putList) {
                    EntityProto proto = putReq.addEntity();
                    proto.mergeFrom(blob.getBytes());
                    putEntities.add(EntityTranslator.createFromPb(proto));
                }
            }
            if (putEntities.size() > 0) {
                DatastoreUtil.put(ds, null, putEntities);
            }
            if (deleteList != null) {
                DatastoreUtil.delete(ds, null, deleteList);
            }
            DatastoreUtil.delete(ds, null, entity.getKey());
        }
    }

    /**
     * Applies the journals.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param journals
     *            the journals
     * @throws NullPointerException
     *             if the ds parameter is null or if the tx parameter is null or
     *             if the journals parameter is null
     * 
     */
    public static void apply(AsyncDatastoreService ds, Transaction tx,
            Map<Key, Entity> journals) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (tx == null) {
            throw new NullPointerException("The tx parameter must not be null.");
        }
        if (journals == null) {
            throw new NullPointerException(
                "The journals parameter must not be null.");
        }
        List<Entity> putList = new ArrayList<Entity>();
        List<Key> deleteList = new ArrayList<Key>();
        for (Key key : journals.keySet()) {
            Entity entity = journals.get(key);
            if (entity != null) {
                putList.add(entity);
            } else {
                deleteList.add(key);
            }
        }
        DatastoreUtil.put(ds, tx, putList);
        DatastoreUtil.delete(ds, tx, deleteList);
    }

    /**
     * Puts the journals to the datastore.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @param journalMap
     *            the map of journals
     * @return journal entities
     * @throws NullPointerException
     *             if the ds parameter is null or if the globalTransactionKey
     *             parameter is null or if the journalMap parameter is null
     * 
     */
    public static List<Entity> put(AsyncDatastoreService ds,
            Key globalTransactionKey, Map<Key, Entity> journalMap)
            throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (journalMap == null) {
            throw new NullPointerException(
                "The journalMap parameter must not be null.");
        }
        List<Entity> entities = new ArrayList<Entity>();
        if (journalMap.size() == 0) {
            return entities;
        }
        int totalSize = 0;
        Entity entity = createEntity(ds, globalTransactionKey);
        List<Blob> putList = new ArrayList<Blob>();
        List<Key> deleteList = new ArrayList<Key>();
        for (Key key : journalMap.keySet()) {
            Entity targetEntity = journalMap.get(key);
            boolean put = targetEntity != null;
            EntityProto targetProto =
                put ? EntityTranslator.convertToPb(targetEntity) : null;
            int size = put ? targetProto.encodingSize() : 0;
            if (totalSize != 0
                && totalSize + size + DatastoreUtil.EXTRA_SIZE > DatastoreUtil.MAX_ENTITY_SIZE) {
                entity.setUnindexedProperty(PUT_LIST_PROPERTY, putList);
                entity.setUnindexedProperty(DELETE_LIST_PROPERTY, deleteList);
                DatastoreUtil.put(ds, null, entity);
                entities.add(entity);
                entity = createEntity(ds, globalTransactionKey);
                putList = new ArrayList<Blob>();
                deleteList = new ArrayList<Key>();
                totalSize = 0;
            }
            if (put) {
                byte[] content = new byte[targetProto.encodingSize()];
                targetProto.outputTo(content, 0);
                putList.add(new Blob(content));
            } else {
                deleteList.add(key);
            }
            totalSize += size + DatastoreUtil.EXTRA_SIZE;
        }
        entity.setUnindexedProperty(PUT_LIST_PROPERTY, putList);
        entity.setUnindexedProperty(DELETE_LIST_PROPERTY, deleteList);
        DatastoreUtil.put(ds, null, entity);
        entities.add(entity);
        return entities;
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
        List<Key> keys = getKeys(ds, globalTransactionKey);
        for (Key key : keys) {
            deleteInTx(ds, globalTransactionKey, key);
        }
    }

    /**
     * Returns the keys specified by the global transaction key.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @return a list of keys
     * @throws NullPointerException
     *             if the ds parameter is null or if the globalTransactionKey
     *             parameter is null
     * 
     */
    protected static List<Key> getKeys(AsyncDatastoreService ds,
            Key globalTransactionKey) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        return new EntityQuery(ds, KIND).filter(
            new Query.FilterPredicate(
            GLOBAL_TRANSACTION_KEY_PROPERTY,
            FilterOperator.EQUAL,
            globalTransactionKey)).asKeyList();
    }

    /**
     * Creates an entity.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param globalTransactionKey
     *            the global transaction key
     * @return an entity
     * @throws NullPointerException
     *             if the ds parameter is null or if the globalTransactionKey
     *             parameter is null
     */
    protected static Entity createEntity(AsyncDatastoreService ds,
            Key globalTransactionKey) throws NullPointerException {
        if (ds == null) {
            throw new NullPointerException("The ds parameter must not be null.");
        }
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        Entity entity = new Entity(DatastoreUtil.allocateId(ds, KIND));
        entity.setProperty(
            GLOBAL_TRANSACTION_KEY_PROPERTY,
            globalTransactionKey);
        return entity;
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
            Entity entity = DatastoreUtil.getOrNull(ds, tx, key);
            if (entity != null
                && globalTransactionKey.equals(entity
                    .getProperty(GLOBAL_TRANSACTION_KEY_PROPERTY))) {
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
     * Constructor.
     * 
     */
    private Journal() {
    }
}