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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * @author higa
 * 
 */
public class JournalTest extends AppEngineTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void createEntity() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Entity entity = Journal.createEntity(globalTransactionKey);
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
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        Datastore.putWithoutTx(new Entity(key2));
        Map<Key, Entity> journalMap = new LinkedHashMap<Key, Entity>();
        Entity putEntity = new Entity(key);
        journalMap.put(key, putEntity);
        journalMap.put(key2, null);
        Journal.put(globalTransactionKey, journalMap);
        Journal.apply(globalTransactionKey);
        assertThat(Datastore.getOrNull(key), is(notNullValue()));
        assertThat(Datastore.getOrNull(key2), is(nullValue()));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyEntities() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        Datastore.putWithoutTx(new Entity(key2));
        Map<Key, Entity> journalMap = new LinkedHashMap<Key, Entity>();
        Entity putEntity = new Entity(key);
        journalMap.put(key, putEntity);
        journalMap.put(key2, null);
        Journal.put(globalTransactionKey, journalMap);
        List<Entity> entities =
            Datastore.query(Journal.KIND).filter(
                Journal.GLOBAL_TRANSACTION_KEY_PROPERTY,
                FilterOperator.EQUAL,
                globalTransactionKey).asList();
        Journal.apply(entities);
        assertThat(Datastore.getOrNull(key), is(notNullValue()));
        assertThat(Datastore.getOrNull(key2), is(nullValue()));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getKeys() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Map<Key, Entity> journalMap = new HashMap<Key, Entity>();
        journalMap.put(targetKey, null);
        Journal.put(globalTransactionKey, journalMap);
        List<Key> keys = Journal.getKeys(globalTransactionKey);
        assertThat(keys.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void put() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        Map<Key, Entity> journalMap = new LinkedHashMap<Key, Entity>();
        Entity putEntity = new Entity(key);
        journalMap.put(key, putEntity);
        journalMap.put(key2, null);
        List<Entity> entities = Journal.put(globalTransactionKey, journalMap);
        assertThat(entities.size(), is(1));
        Entity entity =
            Datastore.query(Journal.KIND).filter(
                Journal.GLOBAL_TRANSACTION_KEY_PROPERTY,
                FilterOperator.EQUAL,
                globalTransactionKey).asSingleEntity();
        assertThat(entity, is(notNullValue()));
        assertThat(entity.hasProperty("put0"), is(true));
        Blob blob = (Blob) entity.getProperty("put0");
        assertThat(DatastoreUtil.bytesToEntity(blob.getBytes()), is(putEntity));
        assertThat(entity.hasProperty("delete1"), is(true));
        assertThat((Key) entity.getProperty("delete1"), is(key2));
        entity = entities.get(0);
        assertThat(entity, is(notNullValue()));
        assertThat(entity.hasProperty("put0"), is(true));
        blob = (Blob) entity.getProperty("put0");
        assertThat(DatastoreUtil.bytesToEntity(blob.getBytes()), is(putEntity));
        assertThat(entity.hasProperty("delete1"), is(true));
        assertThat((Key) entity.getProperty("delete1"), is(key2));

    }

    /**
     * @throws Exception
     */
    @Test
    public void putBigEntities() throws Exception {
        Blob blob = new Blob(new byte[Journal.MAX_ENTITY_SIZE]);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        Map<Key, Entity> journalMap = new LinkedHashMap<Key, Entity>();
        Entity e = new Entity(key);
        e.setUnindexedProperty("aaa", blob);
        Entity e2 = new Entity(key2);
        e2.setUnindexedProperty("aaa", blob);
        journalMap.put(key, e);
        journalMap.put(key2, e2);
        Journal.put(globalTransactionKey, journalMap);
        List<Entity> entities =
            Datastore.query(Journal.KIND).filter(
                Journal.GLOBAL_TRANSACTION_KEY_PROPERTY,
                FilterOperator.EQUAL,
                globalTransactionKey).asList();
        assertThat(entities.size(), is(2));
        Entity entity = entities.get(0);
        assertThat(entity, is(notNullValue()));
        assertThat(entity.hasProperty("put0"), is(true));
        Blob blob2 = (Blob) entity.getProperty("put0");
        assertThat(DatastoreUtil.bytesToEntity(blob2.getBytes()), is(e));
        entity = entities.get(1);
        assertThat(entity, is(notNullValue()));
        assertThat(entity.hasProperty("put1"), is(true));
        blob2 = (Blob) entity.getProperty("put1");
        assertThat(DatastoreUtil.bytesToEntity(blob2.getBytes()), is(e2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteInTxByGlobalTransactionKey() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Map<Key, Entity> journalMap = new HashMap<Key, Entity>();
        journalMap.put(targetKey, null);
        Journal.put(globalTransactionKey, journalMap);
        Journal.deleteInTx(globalTransactionKey);
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteInTx() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Map<Key, Entity> journalMap = new HashMap<Key, Entity>();
        journalMap.put(targetKey, null);
        Journal.put(globalTransactionKey, journalMap);
        List<Key> keys = Journal.getKeys(globalTransactionKey);
        Journal.deleteInTx(globalTransactionKey, keys.get(0));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }
}