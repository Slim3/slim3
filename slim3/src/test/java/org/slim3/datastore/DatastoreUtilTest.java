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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.Test;
import org.slim3.datastore.meta.AaaMeta;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Aaa;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.shared.model.Ccc;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Transaction;
import com.google.storage.onestore.v3.OnestoreEntity.Path;
import com.google.storage.onestore.v3.OnestoreEntity.Path.Element;
import com.google.storage.onestore.v3.OnestoreEntity.Reference;

/**
 * @author higa
 * 
 */
public class DatastoreUtilTest extends AppEngineTestCase {

    private AsyncDatastoreService ds = DatastoreServiceFactory
        .getAsyncDatastoreService();

    private HogeMeta meta = new HogeMeta();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CipherFactory.getFactory().setGlobalKey("xxxxxxxxxxxxxxxx");
    }

    @Override
    public void tearDown() throws Exception {
        CipherFactory.getFactory().clearGlobalKey();
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    @Test
    public void beginTransaction() throws Exception {
        assertThat(DatastoreUtil.beginTransaction(ds), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsAsync() throws Exception {
        Future<KeyRange> future = DatastoreUtil.allocateIdsAsync(ds, "Hoge", 2);
        assertThat(future, is(notNullValue()));
        assertThat(future.get().getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsAsyncWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Future<KeyRange> future =
            DatastoreUtil.allocateIdsAsync(ds, parentKey, "Child", 2);
        assertThat(future, is(notNullValue()));
        assertThat(future.get().getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateId() throws Exception {
        DatastoreUtil.keysCache.remove("Hoge");
        Key key = DatastoreUtil.allocateId(ds, "Hoge");
        assertThat(key, is(notNullValue()));
        Iterator<Key> keys = DatastoreUtil.keysCache.get("Hoge");
        assertThat(keys, is(notNullValue()));
        for (int i = 0; i < 49; i++) {
            DatastoreUtil.allocateId(ds, "Hoge");
            assertThat(
                DatastoreUtil.keysCache.get("Hoge"),
                is(sameInstance(keys)));
        }
        DatastoreUtil.allocateId(ds, "Hoge");
        assertThat(
            DatastoreUtil.keysCache.get("Hoge"),
            is(not(sameInstance(keys))));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Key key = DatastoreUtil.allocateId(ds, parentKey, "Child");
        assertThat(key, is(notNullValue()));
        assertThat(key.isComplete(), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void assignKeyIfNecessary() throws Exception {
        Entity entity = new Entity("Hoge");
        DatastoreUtil.assignKeyIfNecessary(ds, entity);
        assertThat(entity.getKey().getId(), is(not(0L)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void assignKeyIfNecessaryWhenParentExists() throws Exception {
        DatastoreUtil.allocateId(ds, "Child");
        Key dummyKey2 = DatastoreUtil.allocateId(ds, "Child");
        Key parentKey = DatastoreUtil.allocateId(ds, "Parent");
        Entity entity = new Entity("Child", parentKey);
        DatastoreUtil.assignKeyIfNecessary(ds, entity);
        assertThat(entity.getKey().getId(), is(not(dummyKey2.getId() + 1)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void assignKeyIfNecessaryForEntities() throws Exception {
        Entity entity = new Entity("Hoge");
        DatastoreUtil.assignKeyIfNecessary(ds, Arrays.asList(entity));
        assertThat(entity.getKey().getId(), is(not(0L)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOrNull() throws Exception {
        Key key = ds.put(new Entity("Hoge")).get();
        Transaction tx = ds.beginTransaction().get();
        assertThat(DatastoreUtil.getOrNull(ds, tx, key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOrNullNoEntityIsFound() throws Exception {
        Transaction tx = ds.beginTransaction().get();
        assertThat(DatastoreUtil.getOrNull(
            ds,
            tx,
            KeyFactory.createKey("Hoge", "xxx")), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        Key key = ds.put(new Entity("Hoge")).get();
        Transaction tx = ds.beginTransaction().get();
        assertThat(DatastoreUtil.get(ds, tx, key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundRuntimeException.class)
    public void getNoEntityIsFound() throws Exception {
        Transaction tx = ds.beginTransaction().get();
        DatastoreUtil.get(ds, tx, KeyFactory.createKey("Hoge", "xxx"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapAsync() throws Exception {
        Key key = ds.put(new Entity("Hoge")).get();
        Key key2 = ds.put(new Entity("Hoge", key)).get();
        Transaction tx = ds.beginTransaction().get();
        Map<Key, Entity> map =
            DatastoreUtil.getAsMapAsync(ds, tx, Arrays.asList(key, key2)).get();
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getAsMapAsyncTxIsIllegal() throws Exception {
        Key key = ds.put(new Entity("Hoge")).get();
        Key key2 = ds.put(new Entity("Hoge", key)).get();
        Transaction tx = ds.beginTransaction().get();
        tx.rollback();
        DatastoreUtil.getAsMapAsync(ds, tx, Arrays.asList(key, key2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapAsyncNoTx() throws Exception {
        Key key = ds.put(new Entity("Hoge")).get();
        Key key2 = ds.put(new Entity("Hoge", key)).get();
        Map<Key, Entity> map =
            DatastoreUtil
                .getAsMapAsync(ds, null, Arrays.asList(key, key2))
                .get();
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAsync() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        Transaction tx = ds.beginTransaction().get();
        List<Key> keys =
            DatastoreUtil
                .putAsync(ds, tx, Arrays.asList(entity, entity2))
                .get();
        tx.rollback();
        assertThat(keys, is(notNullValue()));
        assertThat(keys.size(), is(2));
        assertThat(tester.count("Hoge"), is(0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void putAsyncIllegalTx() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        Transaction tx = ds.beginTransaction().get();
        tx.rollback();
        DatastoreUtil.putAsync(ds, tx, Arrays.asList(entity, entity2)).get();
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAsyncNoTx() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        List<Key> keys =
            DatastoreUtil
                .putAsync(ds, null, Arrays.asList(entity, entity2))
                .get();
        assertThat(keys, is(notNullValue()));
        assertThat(keys.size(), is(2));
        assertThat(tester.count("Hoge"), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAsync() throws Exception {
        Key key = ds.put(new Entity("Hoge")).get();
        Transaction tx = ds.beginTransaction().get();
        DatastoreUtil.deleteAsync(ds, tx, Arrays.asList(key)).get();
        tx.rollback();
        assertThat(tester.count("Hoge"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void deleteAsyncIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge")).get();
        Transaction tx = ds.beginTransaction().get();
        tx.rollback();
        DatastoreUtil.deleteAsync(ds, tx, Arrays.asList(key)).get();
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAsyncNoTx() throws Exception {
        Key key = ds.put(new Entity("Hoge")).get();
        DatastoreUtil.deleteAsync(ds, null, Arrays.asList(key)).get();
        assertThat(tester.count("Hoge"), is(0));
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
            DatastoreUtil.filterInMemory(list, Arrays.asList(
                (InMemoryFilterCriterion) meta.myInteger.greaterThanOrEqual(2),
                (InMemoryFilterCriterion) meta.myInteger.lessThan(3)));
        assertThat(filtered.size(), is(1));
        assertThat(filtered.get(0).getMyInteger(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void filterInMemoryForEnum() throws Exception {
        List<Hoge> list = new ArrayList<Hoge>();
        Hoge hoge = new Hoge();
        hoge.setMyEnum(SortDirection.ASCENDING);
        list.add(hoge);
        hoge = new Hoge();
        hoge.setMyEnum(SortDirection.DESCENDING);
        list.add(hoge);

        List<Hoge> filtered =
            DatastoreUtil.filterInMemory(
                list,
                Arrays.asList(meta.myEnum.equal(SortDirection.ASCENDING)));
        assertThat(filtered.size(), is(1));
        assertThat(filtered.get(0).getMyEnum(), is(SortDirection.ASCENDING));
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

        List<Hoge> sorted =
            DatastoreUtil.sortInMemory(
                list,
                Arrays.asList((InMemorySortCriterion) meta.myInteger.desc));
        assertThat(sorted.size(), is(3));
        assertThat(sorted.get(0).getMyInteger(), is(3));
        assertThat(sorted.get(1).getMyInteger(), is(2));
        assertThat(sorted.get(2).getMyInteger(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sortInMemoryForEnumWhenAscending() throws Exception {
        List<Hoge> list = new ArrayList<Hoge>();
        Hoge hoge = new Hoge();
        hoge.setMyEnum(SortDirection.ASCENDING);
        list.add(hoge);
        hoge = new Hoge();
        hoge.setMyEnum(SortDirection.DESCENDING);
        list.add(hoge);

        List<Hoge> sorted =
            DatastoreUtil.sortInMemory(
                list,
                Arrays.asList((InMemorySortCriterion) meta.myEnum.asc));
        assertThat(sorted.size(), is(2));
        assertThat(sorted.get(0).getMyEnum(), is(SortDirection.ASCENDING));
        assertThat(sorted.get(1).getMyEnum(), is(SortDirection.DESCENDING));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sortInMemoryForEnumWhenDescending() throws Exception {
        List<Hoge> list = new ArrayList<Hoge>();
        Hoge hoge = new Hoge();
        hoge.setMyEnum(SortDirection.ASCENDING);
        list.add(hoge);
        hoge = new Hoge();
        hoge.setMyEnum(SortDirection.DESCENDING);
        list.add(hoge);

        List<Hoge> sorted =
            DatastoreUtil.sortInMemory(
                list,
                Arrays.asList((InMemorySortCriterion) meta.myEnum.desc));
        assertThat(sorted.size(), is(2));
        assertThat(sorted.get(0).getMyEnum(), is(SortDirection.DESCENDING));
        assertThat(sorted.get(1).getMyEnum(), is(SortDirection.ASCENDING));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes" })
    @Test
    public void getModelMeta() throws Exception {
        ModelMeta<Hoge> modelMeta = DatastoreUtil.getModelMeta(Hoge.class);
        assertThat(modelMeta, is(notNullValue()));
        assertThat(
            modelMeta,
            is(sameInstance((ModelMeta) Datastore.getModelMeta(Hoge.class))));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelMetaWithEntity() throws Exception {
        AaaMeta aaaMeta = new AaaMeta();
        Entity entity = new Entity("Aaa");
        entity.setProperty(
            aaaMeta.getClassHierarchyListName(),
            Arrays.asList(Bbb.class.getName()));
        ModelMeta<Aaa> modelMeta = DatastoreUtil.getModelMeta(aaaMeta, entity);
        assertThat(modelMeta, is(notNullValue()));
        assertThat(modelMeta.getModelClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getModelMetaWithEntityForIllegalClass() throws Exception {
        AaaMeta aaaMeta = new AaaMeta();
        Entity entity = new Entity("Aaa");
        entity.setProperty(
            aaaMeta.getClassHierarchyListName(),
            Arrays.asList(Bbb.class.getName()));
        DatastoreUtil.getModelMeta(meta, entity);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getModelMetaWhenModelMetaIsNotFound() throws Exception {
        DatastoreUtil.getModelMeta(getClass());
    }

    /**
     * @throws Exception
     */
    @Test
    public void createModelMetaWhenGWT() throws Exception {
        ModelMeta<?> modelMeta = DatastoreUtil.getModelMeta(Ccc.class);
        assertThat(modelMeta, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void replacePackageName() throws Exception {
        assertThat(
            DatastoreUtil.replacePackageName("abc.model.Hoge", "model", "meta"),
            is("abc.meta.Hoge"));
        assertThat(DatastoreUtil.replacePackageName(
            "abc.model.xxx.model.Hoge",
            "model",
            "meta"), is("abc.model.xxx.meta.Hoge"));
        assertThat(
            DatastoreUtil.replacePackageName("abc.Hoge", "model", "meta"),
            is("abc.Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void entityToBytes() throws Exception {
        assertThat(
            DatastoreUtil.entityToBytes(new Entity("Hoge")),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void bytesToEntity() throws Exception {
        Entity entity = new Entity(Datastore.createKey("Hoge", 1));
        byte[] bytes = DatastoreUtil.entityToBytes(entity);
        Entity entity2 = DatastoreUtil.bytesToEntity(bytes);
        assertThat(entity2, is(notNullValue()));
        assertThat(entity2, is(entity));
    }

    /**
     * @throws Exception
     */
    @Test
    public void modelToEntity() throws Exception {
        Hoge hoge = new Hoge();
        Entity entity = DatastoreUtil.modelToEntity(ds, hoge);
        assertThat((Long) entity.getProperty("version"), is(1L));
        assertThat(hoge.getKey(), is(notNullValue()));
        assertThat(hoge.getKey(), is(entity.getKey()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void modelsToEntities() throws Exception {
        Hoge hoge = new Hoge();
        Entity entity = new Entity("Hoge");
        List<Entity> entities =
            DatastoreUtil.modelsToEntities(ds, Arrays.asList(hoge, entity));
        assertThat(entities.size(), is(2));
        assertThat((Long) entities.get(0).getProperty("version"), is(1L));
        assertThat(hoge.getKey(), is(notNullValue()));
        assertThat(hoge.getKey(), is(entities.get(0).getKey()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void entityMapToEntityList() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        Key key3 = Datastore.createKey("Hoge", 3);
        Entity entity = new Entity(key);
        Entity entity2 = new Entity(key2);
        Entity entity3 = new Entity(key3);
        Map<Key, Entity> map = new HashMap<Key, Entity>();
        map.put(key3, entity3);
        map.put(key2, entity2);
        map.put(key, entity);
        List<Entity> list =
            DatastoreUtil.entityMapToEntityList(
                Arrays.asList(key, key2, key3),
                map);
        assertThat(list.size(), is(3));
        assertThat(list.get(0), is(entity));
        assertThat(list.get(1), is(entity2));
        assertThat(list.get(2), is(entity3));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundRuntimeException.class)
    public void entityMapToEntityListWhenNoEntityIsFound() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        Key key3 = Datastore.createKey("Hoge", 3);
        Entity entity = new Entity(key);
        Entity entity2 = new Entity(key2);
        Map<Key, Entity> map = new HashMap<Key, Entity>();
        map.put(key2, entity2);
        map.put(key, entity);
        DatastoreUtil
            .entityMapToEntityList(Arrays.asList(key, key2, key3), map);
    }

    /**
     * @throws Exception
     */
    @Test
    public void entityMapToModelList() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        Key key3 = Datastore.createKey("Hoge", 3);
        Entity entity = new Entity(key);
        Entity entity2 = new Entity(key2);
        Entity entity3 = new Entity(key3);
        Map<Key, Entity> map = new HashMap<Key, Entity>();
        map.put(key3, entity3);
        map.put(key2, entity2);
        map.put(key, entity);
        List<Hoge> list =
            DatastoreUtil.entityMapToModelList(
                meta,
                Arrays.asList(key, key2, key3),
                map);
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getKey(), is(key));
        assertThat(list.get(1).getKey(), is(key2));
        assertThat(list.get(2).getKey(), is(key3));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundRuntimeException.class)
    public void entityMapToModelListWhenNoEntityIsFound() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        Key key3 = Datastore.createKey("Hoge", 3);
        Entity entity = new Entity(key);
        Entity entity2 = new Entity(key2);
        Map<Key, Entity> map = new HashMap<Key, Entity>();
        map.put(key2, entity2);
        map.put(key, entity);
        DatastoreUtil.entityMapToModelList(
            meta,
            Arrays.asList(key, key2, key3),
            map);
    }

    /**
     * @throws Exception
     */
    @Test
    public void entityMapToModelMap() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        Key key3 = Datastore.createKey("Hoge", 3);
        Entity entity = new Entity(key);
        Entity entity2 = new Entity(key2);
        Entity entity3 = new Entity(key3);
        Map<Key, Entity> map = new HashMap<Key, Entity>();
        map.put(key3, entity3);
        map.put(key2, entity2);
        map.put(key, entity);
        Map<Key, Hoge> map2 = DatastoreUtil.entityMapToModelMap(meta, map);
        assertThat(map2.size(), is(3));
        assertThat(map2.get(key).getKey(), is(key));
        assertThat(map2.get(key2).getKey(), is(key2));
        assertThat(map2.get(key3).getKey(), is(key3));
    }

    /**
     * @throws Exception
     */
    @Test
    public void refereceToKeyForId() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Reference reference = new Reference();
        Path path = new Path();
        reference.setPath(path);
        Element element = path.addElement();
        element.setType("Hoge");
        element.setId(1);
        assertThat(DatastoreUtil.referenceToKey(reference), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void refereceToKeyForMinusId() throws Exception {
        Key key = KeyFactory.createKey("Hoge", -1);
        Reference reference = new Reference();
        Path path = new Path();
        reference.setPath(path);
        Element element = path.addElement();
        element.setType("Hoge");
        element.setId(-1);
        assertThat(DatastoreUtil.referenceToKey(reference), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void refereceToKeyForName() throws Exception {
        Key key = KeyFactory.createKey("Hoge", "aaa");
        Reference reference = new Reference();
        Path path = new Path();
        reference.setPath(path);
        Element element = path.addElement();
        element.setType("Hoge");
        element.setName("aaa");
        assertThat(DatastoreUtil.referenceToKey(reference), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void refereceToKeyForParent() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Key childKey = KeyFactory.createKey(parentKey, "Child", 1);
        Reference reference = new Reference();
        Path path = new Path();
        reference.setPath(path);
        Element element = path.addElement();
        element.setType("Parent");
        element.setId(1);
        element = path.addElement();
        element.setType("Child");
        element.setId(1);
        assertThat(DatastoreUtil.referenceToKey(reference), is(childKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getRoot() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Key childKey = KeyFactory.createKey(parentKey, "Child", 1);
        Key grandChildKey = KeyFactory.createKey(childKey, "GrandChild", 1);
        assertThat(DatastoreUtil.getRoot(parentKey), is(parentKey));
        assertThat(DatastoreUtil.getRoot(childKey), is(parentKey));
        assertThat(DatastoreUtil.getRoot(grandChildKey), is(parentKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toFilters() throws Exception {
        List<Filter> filters =
            DatastoreUtil.toFilters(meta, meta.myString.equal("aaa"));
        assertThat(filters.size(), is(1));
        assertThat(filters.get(0), is(Query.FilterPredicate.class));
    }
}