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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class DatastoreTest extends DatastoreTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    public void testBeginTransaction() throws Exception {
        assertNotNull(Datastore.beginTransaction());
    }

    /**
     * @throws Exception
     */
    public void testCommit() throws Exception {
        Transaction tx = Datastore.beginTransaction();
        Datastore.commit(tx);
        assertFalse(tx.isActive());
        assertNull(ds.getCurrentTransaction(null));
    }

    /**
     * @throws Exception
     */
    public void testRollback() throws Exception {
        Transaction tx = Datastore.beginTransaction();
        Key key = ds.put(new Entity("Hoge"));
        Datastore.rollback(tx);
        assertFalse(tx.isActive());
        assertNull(ds.getCurrentTransaction(null));
        try {
            ds.get(key);
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * @throws Exception
     */
    public void testGetModel() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = Datastore.get(meta, key);
        assertNotNull(model);
    }

    /**
     * @throws Exception
     */
    public void testGetModelInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Hoge model = Datastore.get(tx, meta, key);
        tx.rollback();
        assertNotNull(model);
    }

    /**
     * @throws Exception
     */
    public void testGetModels() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = Datastore.get(meta, Arrays.asList(key, key2));
        assertNotNull(models);
        assertEquals(2, models.size());
    }

    /**
     * @throws Exception
     */
    public void testGetModelsForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = Datastore.get(meta, key, key2);
        assertNotNull(models);
        assertEquals(2, models.size());
    }

    /**
     * @throws Exception
     */
    public void testGetModelsInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models = Datastore.get(tx, meta, Arrays.asList(key, key2));
        tx.rollback();
        assertNotNull(models);
        assertEquals(2, models.size());
    }

    /**
     * @throws Exception
     */
    public void testGetModelsInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models = Datastore.get(tx, meta, key, key2);
        tx.rollback();
        assertNotNull(models);
        assertEquals(2, models.size());
    }

    /**
     * @throws Exception
     */
    public void testGetModelsAsMap() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map = Datastore.getAsMap(meta, Arrays.asList(key, key2));
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetModelsAsMapForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map = Datastore.getAsMap(meta, key, key2);
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntity() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Entity entity = Datastore.getEntity(key);
        assertNotNull(entity);
    }

    /**
     * @throws Exception
     */
    public void testGetEntityForNotFound() throws Exception {
        try {
            Datastore.getEntity(KeyFactory.createKey("Aaa", 1));
            fail();
        } catch (EntityNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testGetEntityInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Entity entity = Datastore.getEntity(tx, key);
        tx.rollback();
        assertNotNull(entity);
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesAsMap() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Entity> map =
            Datastore.getEntitiesAsMap(Arrays.asList(key, key2));
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesAsMapForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Entity> map = Datastore.getEntitiesAsMap(key, key2);
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesAsMapInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Entity> map =
            Datastore.getEntitiesAsMap(tx, Arrays.asList(key, key2));
        tx.rollback();
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesAsMapInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Entity> map = Datastore.getEntitiesAsMap(tx, key, key2);
        tx.rollback();
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesAsMapForZeroVarargs() throws Exception {
        Map<Key, Entity> map = Datastore.getEntitiesAsMap();
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntities() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Entity> list = Datastore.getEntities(Arrays.asList(key, key2));
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Entity> list = Datastore.getEntities(key, key2);
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Entity> list = Datastore.getEntities(tx, Arrays.asList(key, key2));
        tx.rollback();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Entity> list = Datastore.getEntities(tx, key, key2);
        tx.rollback();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    /**
     * @throws Exception
     */
    public void testPutModel() throws Exception {
        assertNotNull(Datastore.put(new Hoge()));
    }

    /**
     * @throws Exception
     */
    public void testPutModelInTx() throws Exception {
        Transaction tx = Datastore.beginTransaction();
        Key key = Datastore.put(tx, new Hoge());
        tx.rollback();
        assertNotNull(key);
        try {
            ds.get(key);
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testPutEntity() throws Exception {
        assertNotNull(Datastore.putEntity(new Entity("Hoge")));
    }

    /**
     * @throws Exception
     */
    public void testPutEntityInTx() throws Exception {
        Transaction tx = Datastore.beginTransaction();
        Key key = Datastore.putEntity(tx, new Entity("Hoge"));
        tx.rollback();
        assertNotNull(key);
        try {
            ds.get(key);
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testPutEntities() throws Exception {
        List<Key> keys =
            Datastore.putEntities(Arrays.asList(new Entity("Hoge"), new Entity(
                "Hoge")));
        assertNotNull(keys);
        assertEquals(2, keys.size());
    }

    /**
     * @throws Exception
     */
    public void testPutEntitiesForVarargs() throws Exception {
        List<Key> keys =
            Datastore.putEntities(new Entity("Hoge"), new Entity("Hoge"));
        assertNotNull(keys);
        assertEquals(2, keys.size());
    }

    /**
     * @throws Exception
     */
    public void testPutEntitiesInTx() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        Transaction tx = Datastore.beginTransaction();
        List<Key> keys =
            Datastore.putEntities(tx, Arrays.asList(entity, entity2));
        tx.rollback();
        assertNotNull(keys);
        assertEquals(2, keys.size());
        try {
            ds.get(keys.get(0));
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            ds.get(keys.get(1));
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testPutEntitiesInTxForVarargs() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        Transaction tx = Datastore.beginTransaction();
        List<Key> keys = Datastore.putEntities(tx, entity, entity2);
        tx.rollback();
        assertNotNull(keys);
        assertEquals(2, keys.size());
        try {
            ds.get(keys.get(0));
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            ds.get(keys.get(1));
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testQuery() throws Exception {
        assertNotNull(Datastore.query(meta));
    }

    /**
     * @throws Exception
     */
    public void testCreateModelMeta() throws Exception {
        ModelMeta<?> modelMeta = Datastore.createModelMeta(Hoge.class);
        assertNotNull(modelMeta);
        assertEquals(Hoge.class, modelMeta.getModelClass());
    }

    /**
     * @throws Exception
     */
    public void testCreateModelMetaForNotFound() throws Exception {
        try {
            Datastore.createModelMeta(getClass());
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testGetModelMeta() throws Exception {
        ModelMeta<?> modelMeta = Datastore.getModelMeta(Hoge.class);
        assertNotNull(modelMeta);
        assertSame(modelMeta, Datastore.getModelMeta(Hoge.class));
    }
}