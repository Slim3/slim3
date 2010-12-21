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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * @author higa
 * 
 */
public class JournalTest extends AppEngineTestCase {

    private AsyncDatastoreService ds =
        DatastoreServiceFactory.getAsyncDatastoreService();

    /**
     * @throws Exception
     */
    @Test
    public void createEntity() throws Exception {
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Entity entity = Journal.createEntity(ds, globalTransactionKey);
        assertThat(entity, is(notNullValue()));
        assertThat(entity.getKey().isComplete(), is(true));
        assertThat(
            (Key) entity.getProperty(Journal.GLOBAL_TRANSACTION_KEY_PROPERTY),
            is(globalTransactionKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void apply() throws Exception {
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        DatastoreUtil.put(ds, null, new Entity(key2));
        Map<Key, Entity> journalMap = new LinkedHashMap<Key, Entity>();
        Entity putEntity = new Entity(key);
        journalMap.put(key, putEntity);
        journalMap.put(key2, null);
        Journal.put(ds, globalTransactionKey, journalMap);
        Journal.apply(ds, globalTransactionKey);
        assertThat(DatastoreUtil.getOrNull(ds, null, key), is(notNullValue()));
        assertThat(DatastoreUtil.getOrNull(ds, null, key2), is(nullValue()));
        assertThat(tester.count(Journal.KIND), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyEntities() throws Exception {
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        DatastoreUtil.put(ds, null, new Entity(key2));
        Map<Key, Entity> journalMap = new LinkedHashMap<Key, Entity>();
        Entity putEntity = new Entity(key);
        journalMap.put(key, putEntity);
        journalMap.put(key2, null);
        Journal.put(ds, globalTransactionKey, journalMap);
        List<Entity> entities =
            new EntityQuery(ds, Journal.KIND).filter(
                Journal.GLOBAL_TRANSACTION_KEY_PROPERTY,
                FilterOperator.EQUAL,
                globalTransactionKey).asList();
        Journal.apply(ds, entities);
        assertThat(DatastoreUtil.getOrNull(ds, null, key), is(notNullValue()));
        assertThat(DatastoreUtil.getOrNull(ds, null, key2), is(nullValue()));
        assertThat(tester.count(Journal.KIND), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithLocalTransaction() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey(key, "Hoge", 2);
        DatastoreUtil.put(ds, null, new Entity(key2));
        Map<Key, Entity> journalMap = new LinkedHashMap<Key, Entity>();
        Entity putEntity = new Entity(key);
        journalMap.put(key, putEntity);
        journalMap.put(key2, null);
        Transaction tx = ds.beginTransaction().get();
        Journal.apply(ds, tx, journalMap);
        tx.commit();
        assertThat(DatastoreUtil.getOrNull(ds, null, key), is(notNullValue()));
        assertThat(DatastoreUtil.getOrNull(ds, null, key2), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getKeys() throws Exception {
        Key targetKey = KeyFactory.createKey("Hoge", 1);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Map<Key, Entity> journalMap = new HashMap<Key, Entity>();
        journalMap.put(targetKey, null);
        Journal.put(ds, globalTransactionKey, journalMap);
        List<Key> keys = Journal.getKeys(ds, globalTransactionKey);
        assertThat(keys.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void put() throws Exception {
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        Map<Key, Entity> journalMap = new LinkedHashMap<Key, Entity>();
        Entity putEntity = new Entity(key);
        journalMap.put(key, putEntity);
        journalMap.put(key2, null);
        List<Entity> entities =
            Journal.put(ds, globalTransactionKey, journalMap);
        assertThat(entities.size(), is(1));
        entities =
            new EntityQuery(ds, Journal.KIND).filter(
                Journal.GLOBAL_TRANSACTION_KEY_PROPERTY,
                FilterOperator.EQUAL,
                globalTransactionKey).asList();
        assertThat(entities.size(), is(1));
        Entity entity = entities.get(0);
        List<Blob> putList =
            (List<Blob>) entity.getProperty(Journal.PUT_LIST_PROPERTY);
        assertThat(putList.size(), is(1));
        assertThat(
            DatastoreUtil.bytesToEntity(putList.get(0).getBytes()),
            is(putEntity));
        List<Key> deleteList =
            (List<Key>) entity.getProperty(Journal.DELETE_LIST_PROPERTY);
        assertThat(deleteList.size(), is(1));
        assertThat(deleteList.get(0), is(key2));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void putBigEntities() throws Exception {
        Blob blob = new Blob(new byte[DatastoreUtil.MAX_ENTITY_SIZE]);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        Map<Key, Entity> journalMap = new LinkedHashMap<Key, Entity>();
        Entity e = new Entity(key);
        e.setUnindexedProperty("aaa", blob);
        Entity e2 = new Entity(key2);
        e2.setUnindexedProperty("aaa", blob);
        journalMap.put(key, e);
        journalMap.put(key2, e2);
        Journal.put(ds, globalTransactionKey, journalMap);
        List<Entity> entities =
            new EntityQuery(ds, Journal.KIND).filter(
                Journal.GLOBAL_TRANSACTION_KEY_PROPERTY,
                FilterOperator.EQUAL,
                globalTransactionKey).asList();
        assertThat(entities.size(), is(2));
        Entity entity = entities.get(0);
        assertThat(entity, is(notNullValue()));
        List<Blob> putList =
            (List<Blob>) entity.getProperty(Journal.PUT_LIST_PROPERTY);
        assertThat(putList.size(), is(1));
        assertThat(
            DatastoreUtil.bytesToEntity(putList.get(0).getBytes()),
            is(e));
        entity = entities.get(1);
        assertThat(entity, is(notNullValue()));
        putList = (List<Blob>) entity.getProperty(Journal.PUT_LIST_PROPERTY);
        assertThat(putList.size(), is(1));
        assertThat(
            DatastoreUtil.bytesToEntity(putList.get(0).getBytes()),
            is(e2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteInTxByGlobalTransactionKey() throws Exception {
        Key targetKey = KeyFactory.createKey("Hoge", 1);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Map<Key, Entity> journalMap = new HashMap<Key, Entity>();
        journalMap.put(targetKey, null);
        Journal.put(ds, globalTransactionKey, journalMap);
        Journal.deleteInTx(ds, globalTransactionKey);
        assertThat(tester.count(Journal.KIND), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteInTx() throws Exception {
        Key targetKey = KeyFactory.createKey("Hoge", 1);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Map<Key, Entity> journalMap = new HashMap<Key, Entity>();
        journalMap.put(targetKey, null);
        Journal.put(ds, globalTransactionKey, journalMap);
        List<Key> keys = Journal.getKeys(ds, globalTransactionKey);
        Journal.deleteInTx(ds, globalTransactionKey, keys.get(0));
        assertThat(tester.count(Journal.KIND), is(0));
    }
}