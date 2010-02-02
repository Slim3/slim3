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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slim3.datastore.meta.AaaMeta;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Aaa;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.shared.model.Ccc;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.apphosting.api.DatastorePb.Schema;
import com.google.storage.onestore.v3.OnestoreEntity.Path;
import com.google.storage.onestore.v3.OnestoreEntity.Reference;
import com.google.storage.onestore.v3.OnestoreEntity.Path.Element;

/**
 * @author higa
 * 
 */
public class DatastoreUtilTest extends AppEngineTestCase {

    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    @Test
    public void beginTransaction() throws Exception {
        assertThat(DatastoreUtil.beginTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void commit() throws Exception {
        Transaction tx = ds.beginTransaction();
        DatastoreUtil.commit(tx);
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
        DatastoreUtil.rollback(tx);
        assertThat(tx.isActive(), is(false));
        assertThat(ds.getCurrentTransaction(null), is(nullValue()));
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIds() throws Exception {
        KeyRange range = DatastoreUtil.allocateIds("Hoge", 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        KeyRange range = DatastoreUtil.allocateIds(parentKey, "Child", 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateId() throws Exception {
        Key key = DatastoreUtil.allocateId("Hoge");
        assertThat(key, is(notNullValue()));
        Iterator<Key> keys = DatastoreUtil.keysCache.get("Hoge");
        assertThat(keys, is(notNullValue()));
        for (int i = 0; i < 50; i++) {
            DatastoreUtil.allocateId("Hoge");
        }
        assertThat(
            DatastoreUtil.keysCache.get("Hoge"),
            is(not(sameInstance(keys))));
    }

    /**
     * @throws Exception
     */
    @Test
    public void assignKeyIfNecessary() throws Exception {
        Entity entity = new Entity("Hoge");
        DatastoreUtil.assignKeyIfNecessary(entity);
        assertThat(entity.getKey().getId(), is(not(0L)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void assignKeyIfNecessaryForEntities() throws Exception {
        Entity entity = new Entity("Hoge");
        DatastoreUtil.assignKeyIfNecessary(Arrays.asList(entity));
        assertThat(entity.getKey().getId(), is(not(0L)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntity() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Entity entity = DatastoreUtil.get(key);
        assertThat(entity, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundRuntimeException.class)
    public void getEntityWhenEntityIsNotFound() throws Exception {
        DatastoreUtil.get(KeyFactory.createKey("Aaa", 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        assertThat(DatastoreUtil.get(tx, key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityInTxWhenTxIsNull() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        assertThat(DatastoreUtil.get(null, key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundRuntimeException.class)
    public void getEntityInTxWhenEntityIsNotFound() throws Exception {
        Transaction tx = ds.beginTransaction();
        DatastoreUtil.get(tx, KeyFactory.createKey("Aaa", 1));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getEntityInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        DatastoreUtil.get(tx, key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMap() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Entity> map = DatastoreUtil.getAsMap(Arrays.asList(key, key2));
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
        Map<Key, Entity> map =
            DatastoreUtil.getAsMap(tx, Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapInTxWhenTxIsNull() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Map<Key, Entity> map =
            DatastoreUtil.getAsMap(null, Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void putEntity() throws Exception {
        Transaction tx = ds.beginTransaction();
        Key key = DatastoreUtil.put(new Entity("Hoge"));
        tx.rollback();
        assertThat(key, is(notNullValue()));
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void putEntityInTx() throws Exception {
        Transaction tx = ds.beginTransaction();
        Key key = DatastoreUtil.put(tx, new Entity("Hoge"));
        tx.rollback();
        assertThat(key, is(notNullValue()));
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntityInTxWhenTxIsNull() throws Exception {
        Key key = DatastoreUtil.put(null, new Entity("Hoge"));
        assertThat(key, is(notNullValue()));
        assertThat(ds.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntities() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        Transaction tx = ds.beginTransaction();
        List<Key> keys = DatastoreUtil.put(Arrays.asList(entity, entity2));
        tx.rollback();
        assertThat(keys, is(notNullValue()));
        assertThat(keys.size(), is(2));
        assertThat(ds.get(keys).size(), is(0));
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
        List<Key> keys = DatastoreUtil.put(tx, Arrays.asList(entity, entity2));
        tx.rollback();
        assertThat(keys, is(notNullValue()));
        assertThat(keys.size(), is(2));
        assertThat(ds.get(keys).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntitiesInTxWhenTxIsNull() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        List<Key> keys =
            DatastoreUtil.put(null, Arrays.asList(entity, entity2));
        assertThat(keys, is(notNullValue()));
        assertThat(keys.size(), is(2));
        assertThat(ds.get(keys).size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntities() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        DatastoreUtil.delete(Arrays.asList(key));
        tx.rollback();
        assertThat(ds.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        DatastoreUtil.delete(tx, Arrays.asList(key));
        tx.rollback();
        assertThat(ds.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void deleteEntitiesInTxWhenTxIsNull() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        DatastoreUtil.delete(null, Arrays.asList(key));
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void deleteEntitiesInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        DatastoreUtil.delete(tx, Arrays.asList(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void prepare() throws Exception {
        Query query = new Query("Hoge");
        assertThat(DatastoreUtil.prepare(ds, query), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void prepareInTx() throws Exception {
        Query query = new Query("Hoge");
        assertThat(DatastoreUtil.prepare(ds, null, query), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asList() throws Exception {
        Query query = new Query("Hoge");
        PreparedQuery pq = DatastoreUtil.prepare(ds, query);
        assertThat(
            DatastoreUtil.asList(pq, FetchOptions.Builder.withOffset(0)),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleEntity() throws Exception {
        ds.put(new Entity("Hoge"));
        Query query = new Query("Hoge");
        PreparedQuery pq = DatastoreUtil.prepare(ds, query);
        assertThat(DatastoreUtil.asSingleEntity(pq), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asIterable() throws Exception {
        Query query = new Query("Hoge");
        PreparedQuery pq = DatastoreUtil.prepare(ds, query);
        assertThat(DatastoreUtil.asIterable(pq, FetchOptions.Builder
            .withOffset(0)), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void countEntities() throws Exception {
        Query query = new Query("Hoge");
        PreparedQuery pq = DatastoreUtil.prepare(ds, query);
        assertThat(DatastoreUtil.countEntities(pq), is(0));
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
            DatastoreUtil.filterInMemory(list, Arrays.asList(meta.myInteger
                .greaterThanOrEqual(2), meta.myInteger.lessThan(3)));
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
            DatastoreUtil.filterInMemory(list, Arrays.asList(meta.myEnum
                .equal(SortDirection.ASCENDING)));
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
            DatastoreUtil
                .sortInMemory(list, Arrays.asList(meta.myInteger.desc));
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
            DatastoreUtil.sortInMemory(list, Arrays.asList(meta.myEnum.asc));
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
            DatastoreUtil.sortInMemory(list, Arrays.asList(meta.myEnum.desc));
        assertThat(sorted.size(), is(2));
        assertThat(sorted.get(0).getMyEnum(), is(SortDirection.DESCENDING));
        assertThat(sorted.get(1).getMyEnum(), is(SortDirection.ASCENDING));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void getModelMeta() throws Exception {
        ModelMeta<Hoge> modelMeta = DatastoreUtil.getModelMeta(Hoge.class);
        assertThat(modelMeta, is(notNullValue()));
        assertThat(modelMeta, is(sameInstance((ModelMeta) Datastore
            .getModelMeta(Hoge.class))));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelMetaWithEntity() throws Exception {
        Entity entity = new Entity("Aaa");
        entity.setProperty(
            ModelMeta.CLASS_HIERARCHY_LIST_RESERVED_PROPERTY,
            Arrays.asList(Bbb.class.getName()));
        ModelMeta<Aaa> modelMeta =
            DatastoreUtil.getModelMeta(new AaaMeta(), entity);
        assertThat(modelMeta, is(notNullValue()));
        assertThat(modelMeta.getModelClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getModelMetaWithEntityForIllegalClass() throws Exception {
        Entity entity = new Entity("Aaa");
        entity.setProperty(
            ModelMeta.CLASS_HIERARCHY_LIST_RESERVED_PROPERTY,
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
        Entity entity = DatastoreUtil.modelToEntity(hoge);
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
            DatastoreUtil.modelsToEntities(Arrays.asList(hoge, entity));
        assertThat(entities.size(), is(2));
        assertThat((Long) entities.get(0).getProperty("version"), is(1L));
        assertThat(hoge.getKey(), is(notNullValue()));
        assertThat(hoge.getKey(), is(entities.get(0).getKey()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isIncomplete() throws Exception {
        assertThat(
            DatastoreUtil.isIncomplete(Datastore.createKey("Hoge", 1)),
            is(false));
        assertThat(DatastoreUtil.isIncomplete(Datastore
            .createKey("Hoge", "aaa")), is(false));
        assertThat(
            DatastoreUtil.isIncomplete(new Entity("Hoge").getKey()),
            is(true));
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
    public void getSchema() throws Exception {
        Schema schema = DatastoreUtil.getSchema();
        assertThat(schema, is(notNullValue()));
        assertThat(schema.kindSize(), is(0));
        Datastore.put(new Entity("Hoge"));
        schema = DatastoreUtil.getSchema();
        assertThat(schema.kindSize(), is(1));
        List<?> path = schema.getKind(0).getKey().getPath().elements();
        Element element = (Element) path.get(path.size() - 1);
        assertThat(element.getType(), is("Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getKinds() throws Exception {
        Datastore.put(new Entity("Hoge"));
        List<String> kinds = DatastoreUtil.getKinds();
        assertThat(kinds.size(), is(1));
        assertThat(kinds.get(0), is("Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getKind() throws Exception {
        Reference key = new Reference();
        Path path = new Path();
        key.setPath(path);
        Element element = path.addElement();
        element.setType("Parent");
        element.setId(1);
        element = path.addElement();
        element.setType("Child");
        element.setId(1);
        assertThat(DatastoreUtil.getKind(key), is("Child"));
    }
}