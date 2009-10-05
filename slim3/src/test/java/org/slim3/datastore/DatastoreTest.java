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
    public void testGetModelsAsMapInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map =
            Datastore.getAsMap(tx, meta, Arrays.asList(key, key2));
        tx.rollback();
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetModelsAsMapInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map = Datastore.getAsMap(tx, meta, key, key2);
        tx.rollback();
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntity() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Entity entity = Datastore.get(key);
        assertNotNull(entity);
    }

    /**
     * @throws Exception
     */
    public void testGetEntityForNotFound() throws Exception {
        try {
            Datastore.get(KeyFactory.createKey("Aaa", 1));
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
        Entity entity = Datastore.get(tx, key);
        tx.rollback();
        assertNotNull(entity);
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesAsMap() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Entity> map = Datastore.getAsMap(Arrays.asList(key, key2));
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesAsMapForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Entity> map = Datastore.getAsMap(key, key2);
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
        Map<Key, Entity> map = Datastore.getAsMap(tx, Arrays.asList(key, key2));
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
        Map<Key, Entity> map = Datastore.getAsMap(tx, key, key2);
        tx.rollback();
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesAsMapForZeroVarargs() throws Exception {
        Map<Key, Entity> map = Datastore.getAsMap();
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntities() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Entity> list = Datastore.get(Arrays.asList(key, key2));
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    /**
     * @throws Exception
     */
    public void testGetEntitiesForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Entity> list = Datastore.get(key, key2);
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
        List<Entity> list = Datastore.get(tx, Arrays.asList(key, key2));
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
        List<Entity> list = Datastore.get(tx, key, key2);
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
    public void testPutModels() throws Exception {
        List<Hoge> models = Arrays.asList(new Hoge(), new Hoge());
        List<Key> keys = Datastore.put(models);
        assertNotNull(keys);
        assertEquals(2, keys.size());
    }

    /**
     * @throws Exception
     */
    public void testPutModelsForVarargs() throws Exception {
        List<Key> keys = Datastore.put(new Hoge(), new Hoge());
        assertNotNull(keys);
        assertEquals(2, keys.size());
    }

    /**
     * @throws Exception
     */
    public void testPutModelsInTx() throws Exception {
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
        assertNotNull(keys);
        assertEquals(2, keys.size());
    }

    /**
     * @throws Exception
     */
    public void testPutModelsInTxForVarargs() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey(key, "Hoge", 1);
        Hoge hoge = new Hoge();
        hoge.setKey(key);
        Hoge hoge2 = new Hoge();
        hoge2.setKey(key2);
        Transaction tx = ds.beginTransaction();
        List<Key> keys = Datastore.put(tx, hoge, hoge2);
        tx.rollback();
        assertNotNull(keys);
        assertEquals(2, keys.size());
    }

    /**
     * @throws Exception
     */
    public void testPutEntity() throws Exception {
        assertNotNull(Datastore.put(new Entity("Hoge")));
    }

    /**
     * @throws Exception
     */
    public void testPutEntityInTx() throws Exception {
        Transaction tx = Datastore.beginTransaction();
        Key key = Datastore.put(tx, new Entity("Hoge"));
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
            Datastore
                .put(Arrays.asList(new Entity("Hoge"), new Entity("Hoge")));
        assertNotNull(keys);
        assertEquals(2, keys.size());
    }

    /**
     * @throws Exception
     */
    public void testPutEntitiesForVarargs() throws Exception {
        List<Key> keys = Datastore.put(new Entity("Hoge"), new Entity("Hoge"));
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
        List<Key> keys = Datastore.put(tx, Arrays.asList(entity, entity2));
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
        List<Key> keys = Datastore.put(tx, entity, entity2);
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
    public void testDelete() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Datastore.delete(key);
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
    public void testDeleteInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, key);
        tx.rollback();
        assertNotNull(ds.get(key));
    }

    /**
     * @throws Exception
     */
    public void testDeleteEntities() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Datastore.delete(Arrays.asList(key, key2));
        try {
            ds.get(key);
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            ds.get(key2);
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testDeleteEntitiesForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Datastore.delete(key, key2);
        try {
            ds.get(key);
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            ds.get(key2);
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testDeleteEntitiesInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, Arrays.asList(key));
        tx.rollback();
        assertNotNull(ds.get(key));
    }

    /**
     * @throws Exception
     */
    public void testDeleteEntitiesInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(key, key2);
        tx.rollback();
        assertNotNull(ds.get(key));
        assertNotNull(ds.get(key2));
    }

    /**
     * @throws Exception
     */
    public void testQueryUsingModelMeta() throws Exception {
        assertNotNull(Datastore.query(meta));
    }

    /**
     * @throws Exception
     */
    public void testQueryUsingModelMetaAndAncestorKey() throws Exception {
        assertNotNull(Datastore.query(meta, KeyFactory.createKey("Parent", 1)));
    }

    /**
     * @throws Exception
     */
    public void testQueryUsingKind() throws Exception {
        assertNotNull(Datastore.query("Hoge"));
    }

    /**
     * @throws Exception
     */
    public void testQueryUsingKindAndAncestorKey() throws Exception {
        assertNotNull(Datastore
            .query("Hoge", KeyFactory.createKey("Parent", 1)));
    }

    /**
     * @throws Exception
     */
    public void testQueryUsingAncestorKey() throws Exception {
        assertNotNull(Datastore.query(KeyFactory.createKey("Parent", 1)));
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

    /**
     * @throws Exception
     */
    public void testFilter() throws Exception {
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
            Datastore.filter(
                list,
                meta.myInteger.greaterThanOrEqual(2),
                meta.myInteger.lessThan(3));
        assertEquals(1, filtered.size());
        assertEquals(Integer.valueOf(2), filtered.get(0).getMyInteger());
    }

    /**
     * @throws Exception
     */
    public void testFilterForNullCriterion() throws Exception {
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
            Datastore.filter(
                list,
                meta.myInteger.greaterThanOrEqual(null),
                meta.myInteger.lessThan(3));
        assertEquals(2, filtered.size());
        assertEquals(Integer.valueOf(1), filtered.get(0).getMyInteger());
        assertEquals(Integer.valueOf(2), filtered.get(1).getMyInteger());
    }
}