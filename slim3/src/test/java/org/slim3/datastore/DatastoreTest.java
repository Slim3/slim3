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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class DatastoreTest extends LocalServiceTestCase {

    private HogeMeta meta = new HogeMeta();

    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    /**
     * @throws Exception
     */
    @Test
    public void beginTransaction() throws Exception {
        assertThat(Datastore.beginTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void commit() throws Exception {
        Transaction tx = ds.beginTransaction();
        Datastore.commit(tx);
        assertThat(tx.isActive(), is(false));
        assertThat(ds.getCurrentTransaction(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void rollback() throws Exception {
        Transaction tx = ds.beginTransaction();
        Key key = ds.put(new Entity("Hoge"));
        Datastore.rollback(tx);
        assertThat(tx.isActive(), is(false));
        assertThat(ds.getCurrentTransaction(null), is(nullValue()));
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateId() throws Exception {
        assertThat(Datastore.allocateId("Hoge"), is(not(nullValue())));
        assertThat(Datastore.allocateId(Hoge.class), is(not(nullValue())));
        assertThat(Datastore.allocateId(meta), is(not(nullValue())));
        Key parentKey = KeyFactory.createKey("Parent", 1);
        assertThat(
            Datastore.allocateId(parentKey, "Hoge"),
            is(not(nullValue())));
        assertThat(
            Datastore.allocateId(parentKey, Hoge.class),
            is(not(nullValue())));
        assertThat(Datastore.allocateId(parentKey, meta), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIds() throws Exception {
        KeyRange range = Datastore.allocateIds("Hoge", 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = Datastore.allocateIds(Hoge.class, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = Datastore.allocateIds(meta, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        KeyRange range = Datastore.allocateIds(parentKey, "Hoge", 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = Datastore.allocateIds(parentKey, Hoge.class, 2);
        assertThat(range, is(notNullValue()));
        assertEquals(2, range.getSize());

        range = Datastore.allocateIds(parentKey, meta, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createKey() throws Exception {
        assertThat(Datastore.createKey("Hoge", 1), is(not(nullValue())));
        assertThat(Datastore.createKey(Hoge.class, 1), is(not(nullValue())));
        assertThat(Datastore.createKey(meta, 1), is(not(nullValue())));
        assertThat(Datastore.createKey("Hoge", "aaa"), is(not(nullValue())));
        assertThat(Datastore.createKey(Hoge.class, "aaa"), is(not(nullValue())));
        assertThat(Datastore.createKey(meta, "aaa"), is(not(nullValue())));
        Key parentKey = KeyFactory.createKey("Parent", 1);
        assertThat(
            Datastore.createKey(parentKey, "Hoge", 1),
            is(not(nullValue())));
        assertThat(
            Datastore.createKey(parentKey, Hoge.class, 1),
            is(not(nullValue())));
        assertThat(
            Datastore.createKey(parentKey, meta, 1),
            is(not(nullValue())));
        assertThat(
            Datastore.createKey(parentKey, "Hoge", "aaa"),
            is(not(nullValue())));
        assertThat(
            Datastore.createKey(parentKey, Hoge.class, "aaa"),
            is(not(nullValue())));
        assertThat(
            Datastore.createKey(parentKey, meta, "aaa"),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void keyToString() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        String encodedKey = KeyFactory.keyToString(key);
        assertThat(Datastore.keyToString(key), is(encodedKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void stringToKey() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        String encodedKey = KeyFactory.keyToString(key);
        assertThat(Datastore.stringToKey(encodedKey), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModel() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = Datastore.get(meta, key);
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = Datastore.get(Hoge.class, key);
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelUsingClassAndCache() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> cache = new HashMap<Key, Hoge>();
        Hoge model = Datastore.get(Hoge.class, key, cache);
        assertThat(model, is(notNullValue()));
        assertThat(model, is(sameInstance(cache.get(key))));
        ds.delete(key);
        assertThat(
            model,
            is(sameInstance(Datastore.get(Hoge.class, key, cache))));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelUsingCache() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> cache = new HashMap<Key, Hoge>();
        Hoge model = Datastore.get(meta, key, cache);
        assertThat(model, is(not(nullValue())));
        assertThat(model, is(sameInstance(cache.get(key))));
        ds.delete(key);
        assertThat(model, is(sameInstance(Datastore.get(meta, key, cache))));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getModelUsingClassAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Hoge model = Datastore.get(Hoge.class, key, 1L);
        assertThat(model, is(notNullValue()));
        Datastore.get(Hoge.class, key, 0L);
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getModelAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Hoge model = Datastore.get(meta, key, 1L);
        assertThat(model, is(notNullValue()));
        Datastore.get(meta, key, 0L);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Hoge model = Datastore.get(tx, meta, key);
        tx.rollback();
        assertThat(model, is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelInTxUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Hoge model = Datastore.get(tx, Hoge.class, key);
        tx.rollback();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelInTxUsingClassAndCache() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> cache = new HashMap<Key, Hoge>();
        Transaction tx = ds.beginTransaction();
        Hoge model = Datastore.get(tx, Hoge.class, key, cache);
        assertThat(model, is(notNullValue()));
        assertThat(model, is(sameInstance(cache.get(key))));
        ds.delete(key);
        assertThat(model, is(sameInstance(Datastore.get(
            tx,
            Hoge.class,
            key,
            cache))));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelInTxUsingCache() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> cache = new HashMap<Key, Hoge>();
        Transaction tx = ds.beginTransaction();
        Hoge model = Datastore.get(tx, meta, key, cache);
        assertThat(model, is(not(nullValue())));
        assertThat(model, is(sameInstance(cache.get(key))));
        ds.delete(key);
        assertThat(model, is(sameInstance(Datastore.get(tx, meta, key, cache))));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getModelInTxUsingClassAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Transaction tx = Datastore.beginTransaction();
        Hoge model = Datastore.get(tx, Hoge.class, key, 1L);
        assertThat(model, is(notNullValue()));
        Datastore.get(tx, Hoge.class, key, 0L);
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getModelInTxAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Transaction tx = Datastore.beginTransaction();
        Hoge model = Datastore.get(tx, meta, key, 1L);
        assertThat(model, is(not(nullValue())));
        Datastore.get(tx, meta, key, 0L);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getModelInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        Datastore.get(tx, meta, key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModels() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = Datastore.get(meta, Arrays.asList(key, key2));
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = Datastore.get(meta, key, key2);
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundRuntimeException.class)
    public void getModelsWhenEntityNotFound() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 1);
        Datastore.get(meta, key, key2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models = Datastore.get(tx, meta, Arrays.asList(key, key2));
        tx.rollback();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getModelsInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        Datastore.get(tx, meta, Arrays.asList(key, key2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models = Datastore.get(tx, meta, key, key2);
        tx.rollback();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getModelsInIllegalTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        Datastore.get(tx, meta, key, key2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMap() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map = Datastore.getAsMap(meta, Arrays.asList(key, key2));
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map = Datastore.getAsMap(meta, key, key2);
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map =
            Datastore.getAsMap(tx, meta, Arrays.asList(key, key2));
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map = Datastore.getAsMap(tx, meta, key, key2);
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntity() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Entity entity = Datastore.get(key);
        assertThat(entity, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Entity entity = Datastore.get(tx, key);
        tx.rollback();
        assertThat(entity, is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMap() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Entity> map = Datastore.getAsMap(Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Entity> map = Datastore.getAsMap(key, key2);
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Entity> map = Datastore.getAsMap(tx, Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Entity> map = Datastore.getAsMap(tx, key, key2);
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapForZeroVarargs() throws Exception {
        Map<Key, Entity> map = Datastore.getAsMap();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntities() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Entity> list = Datastore.get(Arrays.asList(key, key2));
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Entity> list = Datastore.get(key, key2);
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundRuntimeException.class)
    public void getEntitiesWhenEntityNotFound() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 1);
        Datastore.get(key, key2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Entity> list = Datastore.get(tx, Arrays.asList(key, key2));
        tx.rollback();
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getEntitiesInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        Datastore.get(tx, Arrays.asList(key, key2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Entity> list = Datastore.get(tx, key, key2);
        tx.rollback();
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getEntitiesInIllegalTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        Datastore.get(tx, key, key2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModel() throws Exception {
        Hoge hoge = new Hoge();
        assertThat(Datastore.put(hoge), is(notNullValue()));
        assertThat(hoge.getKey(), is(notNullValue()));
        assertThat(hoge.getVersion(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putPolyModel() throws Exception {
        Bbb bbb = new Bbb();
        assertThat(Datastore.put(bbb), is(notNullValue()));
        assertThat(bbb.getKey().getKind(), is("Aaa"));
        Entity entity = Datastore.get(bbb.getKey());
        assertThat(
            (String) entity
                .getProperty(ModelMeta.SIMPLE_CLASS_NAME_RESERVED_PROPERTY),
            is("Bbb"));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void putModelInTx() throws Exception {
        Hoge hoge = new Hoge();
        Transaction tx = Datastore.beginTransaction();
        Key key = Datastore.put(tx, hoge);
        tx.rollback();
        assertThat(key, is(not(nullValue())));
        assertThat(hoge.getKey(), is(not(nullValue())));
        assertThat(hoge.getVersion(), is(1L));
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void putPolyModelInTx() throws Exception {
        Bbb bbb = new Bbb();
        Transaction tx = Datastore.beginTransaction();
        assertThat(Datastore.put(tx, bbb), is(notNullValue()));
        assertThat(bbb.getKey().getKind(), is("Aaa"));
        tx.commit();
        Entity entity = Datastore.get(bbb.getKey());
        assertThat(
            (String) entity
                .getProperty(ModelMeta.SIMPLE_CLASS_NAME_RESERVED_PROPERTY),
            is("Bbb"));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void putModelInIllegalTx() throws Exception {
        Transaction tx = Datastore.beginTransaction();
        tx.rollback();
        Datastore.put(tx, new Hoge());
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModels() throws Exception {
        List<Hoge> models = Arrays.asList(new Hoge(), new Hoge());
        List<Key> keys = Datastore.put(models);
        assertThat(keys, is(not(nullValue())));
        assertEquals(2, keys.size());
        for (Hoge hoge : models) {
            assertThat(hoge.getKey(), is(not(nullValue())));
            assertThat(hoge.getVersion(), is(1L));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void putPolyModels() throws Exception {
        Bbb bbb = new Bbb();
        Bbb bbb2 = new Bbb();
        List<Key> keys = Datastore.put(bbb, bbb2);
        assertThat(keys.size(), is(2));
        assertThat(keys.get(0).getKind(), is("Aaa"));
        assertThat(keys.get(1).getKind(), is("Aaa"));
        List<Entity> entities = Datastore.get(keys);
        assertThat((String) entities.get(0).getProperty(
            ModelMeta.SIMPLE_CLASS_NAME_RESERVED_PROPERTY), is("Bbb"));
        assertThat((String) entities.get(1).getProperty(
            ModelMeta.SIMPLE_CLASS_NAME_RESERVED_PROPERTY), is("Bbb"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModelsForVarargs() throws Exception {
        Hoge hoge = new Hoge();
        Hoge hoge2 = new Hoge();
        List<Key> keys = Datastore.put(hoge, hoge2);
        assertThat(keys, is(not(nullValue())));
        assertThat(keys.size(), is(2));
        assertThat(hoge.getKey(), is(not(nullValue())));
        assertThat(hoge2.getKey(), is(not(nullValue())));
        assertThat(hoge.getVersion(), is(1L));
        assertThat(hoge2.getVersion(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModelsInTx() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey(key, "Hoge", 1);
        Hoge hoge = new Hoge();
        hoge.setKey(key);
        Hoge hoge2 = new Hoge();
        hoge2.setKey(key2);
        List<Hoge> models = Arrays.asList(hoge, hoge2);
        Transaction tx = ds.beginTransaction();
        List<Key> keys = Datastore.put(tx, models);
        tx.rollback();
        assertThat(keys, is(not(nullValue())));
        assertThat(keys.size(), is(2));
        assertThat(hoge.getKey(), is(key));
        assertThat(hoge2.getKey(), is(key2));
        assertThat(hoge.getVersion(), is(1L));
        assertThat(hoge2.getVersion(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putPolyModelsInNullTx() throws Exception {
        Bbb bbb = new Bbb();
        Bbb bbb2 = new Bbb();
        List<Key> keys = Datastore.put(null, bbb, bbb2);
        assertThat(keys.size(), is(2));
        assertThat(keys.get(0).getKind(), is("Aaa"));
        assertThat(keys.get(1).getKind(), is("Aaa"));
        List<Entity> entities = Datastore.get(keys);
        assertThat((String) entities.get(0).getProperty(
            ModelMeta.SIMPLE_CLASS_NAME_RESERVED_PROPERTY), is("Bbb"));
        assertThat((String) entities.get(1).getProperty(
            ModelMeta.SIMPLE_CLASS_NAME_RESERVED_PROPERTY), is("Bbb"));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void putModelsInIllegalTx() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey(key, "Hoge", 1);
        Hoge hoge = new Hoge();
        hoge.setKey(key);
        Hoge hoge2 = new Hoge();
        hoge2.setKey(key2);
        List<Hoge> models = Arrays.asList(hoge, hoge2);
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        Datastore.put(tx, models);
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModelsInTxForVarargs() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey(key, "Hoge", 1);
        Hoge hoge = new Hoge();
        hoge.setKey(key);
        Hoge hoge2 = new Hoge();
        hoge2.setKey(key2);
        Transaction tx = ds.beginTransaction();
        List<Key> keys = Datastore.put(tx, hoge, hoge2);
        tx.rollback();
        assertThat(keys, is(not(nullValue())));
        assertThat(keys.size(), is(2));
        assertThat(hoge.getKey(), is(key));
        assertThat(hoge2.getKey(), is(key2));
        assertThat(hoge.getVersion(), is(1L));
        assertThat(hoge2.getVersion(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void putModelsInIllegalTxForVarargs() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey(key, "Hoge", 1);
        Hoge hoge = new Hoge();
        hoge.setKey(key);
        Hoge hoge2 = new Hoge();
        hoge2.setKey(key2);
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        Datastore.put(tx, hoge, hoge2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntity() throws Exception {
        assertThat(Datastore.put(new Entity("Hoge")), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void putEntityInTx() throws Exception {
        Transaction tx = ds.beginTransaction();
        Key key = Datastore.put(tx, new Entity("Hoge"));
        tx.rollback();
        assertThat(key, is(notNullValue()));
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void putEntityInIllegalTx() throws Exception {
        Transaction tx = Datastore.beginTransaction();
        tx.rollback();
        Datastore.put(tx, new Entity("Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntities() throws Exception {
        List<Key> keys =
            Datastore
                .put(Arrays.asList(new Entity("Hoge"), new Entity("Hoge")));
        assertThat(keys, is(notNullValue()));
        assertThat(keys.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntitiesForVarargs() throws Exception {
        List<Key> keys = Datastore.put(new Entity("Hoge"), new Entity("Hoge"));
        assertThat(keys, is(not(nullValue())));
        assertThat(keys.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntitiesInTx() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        Transaction tx = ds.beginTransaction();
        List<Key> keys = Datastore.put(tx, Arrays.asList(entity, entity2));
        tx.rollback();
        assertThat(keys, is(notNullValue()));
        assertThat(keys.size(), is(2));
        assertThat(ds.get(keys).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void putEntitiesInIllegalTx() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        Transaction tx = Datastore.beginTransaction();
        tx.rollback();
        Datastore.put(tx, Arrays.asList(entity, entity2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntitiesInTxForVarargs() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        Transaction tx = Datastore.beginTransaction();
        List<Key> keys = Datastore.put(tx, entity, entity2);
        tx.rollback();
        assertThat(keys, is(not(nullValue())));
        assertThat(keys.size(), is(2));
        assertThat(ds.get(keys).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void putEntitiesInIllegalTxForVarargs() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        Transaction tx = Datastore.beginTransaction();
        tx.rollback();
        Datastore.put(tx, entity, entity2);
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Datastore.delete(key);
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, key);
        tx.rollback();
        assertThat(ds.get(key), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void deleteInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = Datastore.beginTransaction();
        tx.rollback();
        Datastore.delete(tx, key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntities() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Datastore.delete(Arrays.asList(key, key2));
        assertThat(ds.get(Arrays.asList(key, key2)).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Datastore.delete(key, key2);
        assertThat(ds.get(Arrays.asList(key, key2)).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Datastore.delete(tx, Arrays.asList(key));
        tx.rollback();
        assertThat(ds.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void deleteEntitiesInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = Datastore.beginTransaction();
        tx.rollback();
        Datastore.delete(tx, Arrays.asList(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, key, key2);
        tx.rollback();
        assertThat(ds.get(Arrays.asList(key, key2)).size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void deleteEntitiesInIllegalTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = Datastore.beginTransaction();
        tx.rollback();
        Datastore.delete(tx, key, key2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelClass() throws Exception {
        assertThat(Datastore.query(Hoge.class), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelMeta() throws Exception {
        assertThat(Datastore.query(meta), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelClassAndAncestorKey() throws Exception {
        assertThat(Datastore.query(Hoge.class, KeyFactory
            .createKey("Parent", 1)), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelMetaAndAncestorKey() throws Exception {
        assertThat(
            Datastore.query(meta, KeyFactory.createKey("Parent", 1)),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingKind() throws Exception {
        assertThat(Datastore.query("Hoge"), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingKindAndAncestorKey() throws Exception {
        assertThat(
            Datastore.query("Hoge", KeyFactory.createKey("Parent", 1)),
            is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingAncestorKey() throws Exception {
        assertThat(
            Datastore.query(KeyFactory.createKey("Parent", 1)),
            is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void getModelMeta() throws Exception {
        ModelMeta<?> modelMeta = Datastore.getModelMeta(Hoge.class);
        assertThat(modelMeta, is(notNullValue()));
        assertThat(modelMeta, is(sameInstance((ModelMeta) Datastore
            .getModelMeta(Hoge.class))));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getModelMetaWhenModelMetaIsNotFound() throws Exception {
        Datastore.getModelMeta(getClass());
    }

    /**
     * @throws Exception
     */
    @Test
    public void filterInMemory() throws Exception {
        List<Hoge> list = new ArrayList<Hoge>();
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        list.add(hoge);
        hoge = new Hoge();
        hoge.setMyInteger(3);
        list.add(hoge);
        hoge = new Hoge();
        hoge.setMyInteger(2);
        list.add(hoge);

        List<Hoge> filtered =
            Datastore.filterInMemory(
                list,
                meta.myInteger.greaterThanOrEqual(2),
                meta.myInteger.lessThan(3));
        assertThat(filtered.size(), is(1));
        assertThat(filtered.get(0).getMyInteger(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sortInMemory() throws Exception {
        List<Hoge> list = new ArrayList<Hoge>();
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        list.add(hoge);
        hoge = new Hoge();
        hoge.setMyInteger(3);
        list.add(hoge);
        hoge = new Hoge();
        hoge.setMyInteger(2);
        list.add(hoge);

        List<Hoge> sorted = Datastore.sortInMemory(list, meta.myInteger.desc);
        assertThat(sorted.size(), is(3));
        assertThat(sorted.get(0).getMyInteger(), is(3));
        assertThat(sorted.get(1).getMyInteger(), is(2));
        assertThat(sorted.get(2).getMyInteger(), is(1));
    }
}