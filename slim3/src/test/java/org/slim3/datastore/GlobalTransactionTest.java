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

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.TaskQueuePb.TaskQueueAddRequest;

/**
 * @author higa
 * 
 */
public class GlobalTransactionTest extends AppEngineTestCase {

    private AsyncDatastoreService ds =
        DatastoreServiceFactory.getAsyncDatastoreService();

    private GlobalTransaction gtx;

    private HogeMeta meta = HogeMeta.get();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        gtx = new GlobalTransaction(ds);
        gtx.begin();
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
    public void getOrNull() throws Exception {
        Key globalTransactionKey =
            KeyFactory.createKey(GlobalTransaction.KIND, 1);
        Boolean valid = false;
        Entity entity = new Entity(globalTransactionKey);
        entity.setUnindexedProperty(GlobalTransaction.VALID_PROPERTY, valid);
        DatastoreUtil.put(ds, null, entity);
        GlobalTransaction gtx2 =
            GlobalTransaction.getOrNull(
                ds,
                DatastoreUtil.beginTransaction(ds),
                globalTransactionKey);
        assertThat(gtx2.globalTransactionKey, is(globalTransactionKey));
        assertThat(gtx2.valid, is(valid));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toGlobalTransaction() throws Exception {
        Key globalTransactionKey =
            KeyFactory.createKey(GlobalTransaction.KIND, 1);
        Boolean valid = false;
        Entity entity = new Entity(globalTransactionKey);
        entity.setUnindexedProperty(GlobalTransaction.VALID_PROPERTY, valid);
        GlobalTransaction gtx2 =
            GlobalTransaction.toGlobalTransaction(ds, entity);
        assertThat(gtx2.globalTransactionKey, is(globalTransactionKey));
        assertThat(gtx2.valid, is(valid));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putGlobalTransaction() throws Exception {
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        Key globalTransactionKey =
            KeyFactory.createKey(GlobalTransaction.KIND, 1);
        Boolean valid = false;
        GlobalTransaction gtx2 =
            new GlobalTransaction(ds, globalTransactionKey, valid);
        GlobalTransaction.put(ds, tx, gtx2);
        tx.commit();
        Entity entity = DatastoreUtil.get(ds, null, globalTransactionKey);
        assertThat(entity, is(notNullValue()));
        assertThat((Boolean) entity
            .getProperty(GlobalTransaction.VALID_PROPERTY), is(valid));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getActiveTransactions() throws Exception {
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(1));
        gtx.rollback();
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getCurrentTransactionStack() throws Exception {
        Stack<GlobalTransaction> stack =
            GlobalTransaction.getCurrentTransactionStack();
        assertThat(stack, is(notNullValue()));
        assertThat(
            GlobalTransaction.getCurrentTransactionStack(),
            is(sameInstance(stack)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getCurrentTransaction() throws Exception {
        assertThat(GlobalTransaction.getCurrentTransaction(), is(gtx));
    }

    /**
     * @throws Exception
     */
    @Test
    public void clearActiveTransactions() throws Exception {
        GlobalTransaction.clearActiveTransactions();
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructor() throws Exception {
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        boolean valid = false;
        GlobalTransaction gtx2 =
            new GlobalTransaction(ds, globalTransactionKey, valid);
        assertThat(gtx2.ds, is(ds));
        assertThat(gtx2.globalTransactionKey, is(globalTransactionKey));
        assertThat(gtx2.valid, is(valid));
    }

    /**
     * @throws Exception
     */
    @Test
    public void begin() throws Exception {
        assertThat(gtx.localTransaction, is(notNullValue()));
        assertThat(gtx.globalTransactionKey, is(nullValue()));
        assertThat(gtx.isActive(), is(true));
        assertThat(gtx.timestamp, is(not(0L)));
        assertThat(gtx.lockMap, is(notNullValue()));
        assertThat(gtx.globalJournalMap, is(notNullValue()));
        assertThat(GlobalTransaction.getCurrentTransaction(), is(gtx));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getId() throws Exception {
        assertThat(gtx.getId(), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getIdAndRollback() throws Exception {
        gtx.rollback();
        assertThat(gtx.getId(), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyLockAndGetAsMap() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.verifyLockAndGetAsMap(rootKey, Arrays.asList(key)).get(
            key), is(notNullValue()));
        assertThat(gtx.lockMap.get(rootKey), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyLockAndGetAsMapWhenNoEntityIsFound() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        assertThat(gtx.verifyLockAndGetAsMap(rootKey, Arrays.asList(key)).get(
            key), is(nullValue()));
        assertThat(gtx.lockMap.get(rootKey), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockAndGetAsMap() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        gtx.setLocalTransactionRootKey(rootKey);
        assertThat(
            gtx.lockAndGetAsMap(rootKey, Arrays.asList(key)).get(key),
            is(notNullValue()));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(notNullValue()));
        assertThat(
            DatastoreUtil.getOrNull(ds, null, lock.key),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockAndGetAsMapWhenNoEntityIsFound() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        gtx.setLocalTransactionRootKey(rootKey);
        assertThat(
            gtx.lockAndGetAsMap(rootKey, Arrays.asList(key)).get(key),
            is(nullValue()));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(notNullValue()));
        assertThat(
            DatastoreUtil.getOrNull(ds, null, lock.key),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockAndGetAsMapWhenConcurrentModificationExceptionOccurred()
            throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        GlobalTransaction otherGtx = new GlobalTransaction(ds);
        otherGtx.begin();
        otherGtx.setLocalTransactionRootKey(key2);
        otherGtx.lock(key2);
        gtx.setLocalTransactionRootKey(key);
        try {
            gtx.lockAndGetAsMap(key, Arrays.asList(key));
            gtx.lockAndGetAsMap(key2, Arrays.asList(key2));
            fail();
        } catch (ConcurrentModificationException e) {
            assertThat(gtx.localTransaction.isActive(), is(false));
            assertThat(gtx.isActive(), is(false));
            assertThat(gtx.lockMap.size(), is(0));
            assertThat(
                DatastoreUtil.getOrNull(ds, null, Lock.createKey(key)),
                is(nullValue()));
            Lock lock = Lock.getOrNull(ds, null, Lock.createKey(key2));
            assertThat(lock, is(notNullValue()));
            assertThat(
                lock.globalTransactionKey,
                is(otherGtx.globalTransactionKey));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void lock() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        gtx.getAsMap(key);
        gtx.lock(key);
        Lock lock = gtx.lockMap.get(key);
        assertThat(lock, is(notNullValue()));
        assertThat(DatastoreUtil.get(ds, null, lock.key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void lockForChildKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Key childKey = KeyFactory.createKey(parentKey, "Child", 1);
        gtx.setLocalTransactionRootKey(parentKey);
        gtx.lock(childKey);
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockForSameKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        gtx.setLocalTransactionRootKey(key);
        gtx.lock(key);
        gtx.lock(key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockWhenConcurrentModificationExceptionOccurred()
            throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        GlobalTransaction otherGtx = new GlobalTransaction(ds);
        otherGtx.begin();
        otherGtx.setLocalTransactionRootKey(key2);
        otherGtx.lock(key2);
        try {
            gtx.setLocalTransactionRootKey(key);
            gtx.lock(key);
            gtx.lock(key2);
            fail();
        } catch (ConcurrentModificationException e) {
            assertThat(gtx.lockMap.get(key), is(nullValue()));
            assertThat(
                DatastoreUtil.getOrNull(ds, null, Lock.createKey(key)),
                is(nullValue()));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void unlock() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        gtx.getAsMap(key);
        gtx.lock(key);
        gtx.unlock();
        assertThat(gtx.isActive(), is(false));
        assertThat(gtx.lockMap.get(key), is(nullValue()));
        assertThat(
            DatastoreUtil.getOrNull(ds, null, Lock.createKey(key)),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntity() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.get(key), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(key));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundRuntimeException.class)
    public void getEntityWhenNoEntityIsFound() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        gtx.get(key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityUsingModelClass() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.get(Hoge.class, key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityUsingModelMeta() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.get(HogeMeta.get(), key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityUsingModelClassAndVersion() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setVersion(1L);
        hoge.setKey(DatastoreUtil.allocateId(ds, "Hoge"));
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        assertThat(gtx.get(Hoge.class, hoge.getKey(), 1), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getEntityUsingModelClassAndVersionWhenConcurrentModificationExceptinOccurred()
            throws Exception {
        Hoge hoge = new Hoge();
        hoge.setVersion(1L);
        hoge.setKey(DatastoreUtil.allocateId(ds, "Hoge"));
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        gtx.get(Hoge.class, hoge.getKey(), 2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityUsingModelMetaAndVersion() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setVersion(1L);
        hoge.setKey(DatastoreUtil.allocateId(ds, "Hoge"));
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        assertThat(gtx.get(meta, hoge.getKey(), 1), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getEntityUsingModelMetaAndVersionWhenConcurrentModificationExceptinOccurred()
            throws Exception {
        Hoge hoge = new Hoge();
        hoge.setVersion(1L);
        hoge.setKey(DatastoreUtil.allocateId(ds, "Hoge"));
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        gtx.get(meta, hoge.getKey(), 2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNull() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.getOrNull(key), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullWhenNoEntityIsFound() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        assertThat(gtx.getOrNull(key), is(nullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullWhenEntityWithoutTxIsNotFound() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.getAsMap(ds, gtx.localTransaction, Arrays.asList(key));
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.getOrNull(key), is(nullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullWhenLocalTransactionRootKeyIsSame()
            throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        Key key2 = KeyFactory.createKey(rootKey, "Hoge", 2);
        DatastoreUtil.put(ds, null, new Entity(key));
        DatastoreUtil.put(ds, null, new Entity(key2));
        gtx.getOrNull(key);
        assertThat(gtx.getOrNull(key2), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullWhenLocalTransactionRootKeyIsSameAndEntityWithoutTxIsNotFound()
            throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        gtx.getOrNull(key);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.getOrNull(key), is(nullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullWhenLocalTransactionRootKeyIsDifferent()
            throws Exception {
        Key otherKey = KeyFactory.createKey("Hoge", 2);
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        DatastoreUtil.put(ds, null, new Entity(otherKey));
        gtx.getOrNull(otherKey);
        assertThat(gtx.getOrNull(key), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(otherKey));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(notNullValue()));
        assertThat(DatastoreUtil
            .getAsMap(ds, null, Arrays.asList(lock.key))
            .size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullWhenLocalTransactionRootKeyIsDifferentAndNoEntityIsFound()
            throws Exception {
        Key otherKey = KeyFactory.createKey("Hoge", 2);
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        gtx.getOrNull(otherKey);
        assertThat(gtx.getOrNull(key), is(nullValue()));
        assertThat(gtx.localTransactionRootKey, is(otherKey));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(notNullValue()));
        assertThat(DatastoreUtil
            .getAsMap(ds, null, Arrays.asList(lock.key))
            .size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNulUsingModelClass() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.getOrNull(Hoge.class, key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNulUsingModelClassWhenNoEntityIsFound()
            throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        assertThat(gtx.getOrNull(Hoge.class, key), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullUsingModelMeta() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.getOrNull(meta, key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullUsingModelMetaWhenNoEntityIsFound()
            throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        assertThat(gtx.getOrNull(meta, key), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntities() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.get(Arrays.asList(key)), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesUsingModelClass() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.get(Hoge.class, Arrays.asList(key)), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesUsingModelMeta() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.get(meta, Arrays.asList(key)), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesVarargs() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.get(key, key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesVarargsUsingModelClass() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.get(Hoge.class, key, key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesVarargsUsingModelMeta() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        assertThat(gtx.get(HogeMeta.get(), key, key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapForOneLocalKey() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        Map<Key, Entity> map = gtx.getAsMap(Arrays.asList(key));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapForMultipleLocalKeys() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        Key key2 = KeyFactory.createKey(rootKey, "Hoge", 2);
        DatastoreUtil.put(ds, null, new Entity(key));
        DatastoreUtil.put(ds, null, new Entity(key2));
        Map<Key, Entity> map = gtx.getAsMap(Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(map.get(key2), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapForLocalKeyAndGlobalKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        DatastoreUtil.put(ds, null, new Entity(key));
        DatastoreUtil.put(ds, null, new Entity(key2));
        Map<Key, Entity> map = gtx.getAsMap(Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(map.get(key2), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(key));
        assertThat(gtx.lockMap.size(), is(1));
        assertThat(gtx.lockMap.get(key2), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapForLocalKeyAndGlobalKeyWhenLocalTransactionRootKeyIsAlreadySet()
            throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        DatastoreUtil.put(ds, null, new Entity(key));
        DatastoreUtil.put(ds, null, new Entity(key2));
        gtx.getAsMap(Arrays.asList(rootKey));
        Map<Key, Entity> map = gtx.getAsMap(Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(map.get(key2), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.lockMap.size(), is(1));
        assertThat(gtx.lockMap.get(key2), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapForLocalKeyWhenLocalTransactionRootKeyIsAlreadySet()
            throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        gtx.getAsMap(Arrays.asList(rootKey));
        Map<Key, Entity> map = gtx.getAsMap(Arrays.asList(key));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapForGlobalKeyWhenLocalTransactionRootKeyIsAlreadySet()
            throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        DatastoreUtil.put(ds, null, new Entity(key2));
        gtx.getAsMap(Arrays.asList(rootKey));
        Map<Key, Entity> map = gtx.getAsMap(Arrays.asList(key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(1));
        assertThat(map.get(key2), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.lockMap.size(), is(1));
        assertThat(gtx.lockMap.get(key2), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapUsingModelClass() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        Map<Key, Hoge> map = gtx.getAsMap(Hoge.class, Arrays.asList(key));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapUsingModelMeta() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        Map<Key, Hoge> map = gtx.getAsMap(HogeMeta.get(), Arrays.asList(key));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapForVarargs() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        Map<Key, Entity> map = gtx.getAsMap(key);
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapUsingModelClassForVarargs() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        Map<Key, Hoge> map = gtx.getAsMap(Hoge.class, key);
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapUsingModelMetaForVarargs() throws Exception {
        Key rootKey = KeyFactory.createKey("Root", 1);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        Map<Key, Hoge> map = gtx.getAsMap(HogeMeta.get(), key);
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingKindAndAncestorKeyAsLocalTransaction()
            throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        EntityQuery query = gtx.query("Hoge", ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(ancestorKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingKindAndAncestorKeyWhenSameRootKeyAsLocalTransaction()
            throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        gtx.getAsMap(ancestorKey);
        EntityQuery query = gtx.query("Hoge", ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(ancestorKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingKindAndAncestorKeyWithGlobalTransaction()
            throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        gtx.getAsMap(key2);
        EntityQuery query = gtx.query("Hoge", ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(nullValue()));
        assertThat(gtx.localTransactionRootKey, is(key2));
        assertThat(gtx.lockMap.size(), is(1));
        assertThat(gtx.lockMap.get(ancestorKey), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelClassAndAncestorKeyAsLocalTransaction()
            throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        ModelQuery<Hoge> query = gtx.query(Hoge.class, ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(ancestorKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelClassAndAncestorKeyWhenSameRootKeyAsLocalTransaction()
            throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        gtx.getAsMap(ancestorKey);
        ModelQuery<Hoge> query = gtx.query(Hoge.class, ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(ancestorKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelClassAndAncestorKeyWithGlobalTransaction()
            throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        gtx.getAsMap(key2);
        ModelQuery<Hoge> query = gtx.query(Hoge.class, ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(nullValue()));
        assertThat(gtx.localTransactionRootKey, is(key2));
        assertThat(gtx.lockMap.size(), is(1));
        assertThat(gtx.lockMap.get(ancestorKey), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelMetaAndAncestorKeyAsLocalTransaction()
            throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        ModelQuery<Hoge> query = gtx.query(HogeMeta.get(), ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(ancestorKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelMetaAndAncestorKeyWhenSameRootKeyAsLocalTransaction()
            throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        gtx.getAsMap(ancestorKey);
        ModelQuery<Hoge> query = gtx.query(HogeMeta.get(), ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(ancestorKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelMetaAndAncestorKeyWithGlobalTransaction()
            throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        gtx.getAsMap(key2);
        ModelQuery<Hoge> query = gtx.query(HogeMeta.get(), ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(nullValue()));
        assertThat(gtx.localTransactionRootKey, is(key2));
        assertThat(gtx.lockMap.size(), is(1));
        assertThat(gtx.lockMap.get(ancestorKey), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingAncestorKeyAsLocalTransaction() throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        KindlessQuery query = gtx.query(ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(ancestorKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingAncestorKeyWhenSameRootKeyAsLocalTransaction()
            throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        gtx.getAsMap(ancestorKey);
        KindlessQuery query = gtx.query(ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(notNullValue()));
        assertThat(gtx.localTransactionRootKey, is(ancestorKey));
        assertThat(gtx.lockMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingAncestorKeyWithGlobalTransaction() throws Exception {
        Key ancestorKey = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        gtx.getAsMap(key2);
        KindlessQuery query = gtx.query(ancestorKey);
        assertThat(query, is(notNullValue()));
        assertThat(query.tx, is(nullValue()));
        assertThat(gtx.localTransactionRootKey, is(key2));
        assertThat(gtx.lockMap.size(), is(1));
        assertThat(gtx.lockMap.get(ancestorKey), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntityAsLocalTransaction() throws Exception {
        Key rootKey = KeyFactory.createKey("Parent", 1);
        Key key = KeyFactory.createKey(rootKey, "Child", 1);
        Entity entity = new Entity(key);
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        assertThat(gtx.put(entity), is(entity.getKey()));
        tx.commit();
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(DatastoreUtil.getOrNull(ds, null, key), is(nullValue()));
        assertThat(gtx.lockMap.size(), is(0));
        assertThat(gtx.globalJournalMap.size(), is(0));
        assertThat(gtx.localJournalMap.size(), is(1));
        assertThat(gtx.localJournalMap.get(entity.getKey()), is(entity));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntityForSameRootKeyAsLocalTransaction() throws Exception {
        Key rootKey = KeyFactory.createKey("Parent", 1);
        Key key = KeyFactory.createKey(rootKey, "Child", 1);
        gtx.getAsMap(rootKey);
        Entity entity = new Entity(key);
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        assertThat(gtx.put(entity), is(entity.getKey()));
        tx.commit();
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(new KindlessQuery(ds, key).count(), is(0));
        assertThat(gtx.lockMap.size(), is(0));
        assertThat(gtx.globalJournalMap.size(), is(0));
        assertThat(gtx.localJournalMap.size(), is(1));
        assertThat(gtx.localJournalMap.get(entity.getKey()), is(entity));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntityAsGlobalTransaction() throws Exception {
        Key rootKey = KeyFactory.createKey("Parent", 1);
        Key rootKey2 = KeyFactory.createKey("Parent", 2);
        Key key = KeyFactory.createKey(rootKey, "Hoge", 1);
        Key key2 = KeyFactory.createKey(rootKey2, "Hoge", 2);
        gtx.getAsMap(key2);
        Entity entity = new Entity(key);
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        assertThat(gtx.put(entity), is(entity.getKey()));
        tx.commit();
        assertThat(gtx.localTransactionRootKey, is(rootKey2));
        assertThat(new KindlessQuery(ds, key).count(), is(0));
        assertThat(gtx.lockMap.size(), is(1));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(notNullValue()));
        assertThat(lock.globalTransactionKey, is(gtx.globalTransactionKey));
        assertThat(lock.timestamp, is(not(0L)));
        assertThat(gtx.globalJournalMap.size(), is(1));
        assertThat(gtx.globalJournalMap.get(key), is(entity));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModel() throws Exception {
        Hoge hoge = new Hoge();
        assertThat(gtx.put(hoge), is(hoge.getKey()));
        assertThat(gtx.localTransactionRootKey, is(hoge.getKey()));
        assertThat(new KindlessQuery(ds, hoge.getKey()).count(), is(0));
        assertThat(gtx.lockMap.size(), is(0));
        assertThat(gtx.globalJournalMap.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModelAndEntity() throws Exception {
        Hoge hoge = new Hoge();
        Entity entity = new Entity("Hoge");
        List<Key> keys = gtx.put(Arrays.asList(hoge, entity));
        assertThat(keys, is(notNullValue()));
        assertThat(keys.size(), is(2));
        assertThat(gtx.lockMap.size(), is(1));
        assertThat(gtx.lockMap.get(entity.getKey()), is(notNullValue()));
        assertThat(gtx.globalJournalMap.size(), is(1));
        assertThat(
            gtx.globalJournalMap.get(entity.getKey()),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModelAndEntityVarargs() throws Exception {
        Hoge hoge = new Hoge();
        Entity entity = new Entity("Hoge");
        List<Key> keys = gtx.put(hoge, entity);
        assertThat(keys, is(notNullValue()));
        assertThat(keys.size(), is(2));
        assertThat(gtx.lockMap.size(), is(1));
        assertThat(gtx.lockMap.get(entity.getKey()), is(notNullValue()));
        assertThat(gtx.globalJournalMap.size(), is(1));
        assertThat(
            gtx.globalJournalMap.get(entity.getKey()),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntityAsLocalTransaction() throws Exception {
        Key rootKey = KeyFactory.createKey("Parent", 1);
        Key key = KeyFactory.createKey(rootKey, "Child", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        gtx.delete(key);
        tx.commit();
        assertThat(tester.count("Child"), is(1));
        assertThat(gtx.lockMap.size(), is(0));
        assertThat(gtx.globalJournalMap.size(), is(0));
        assertThat(gtx.localJournalMap.size(), is(1));
        assertThat(gtx.localJournalMap.containsKey(key), is(true));
        assertThat(gtx.localJournalMap.get(key), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntityAsLocalTransactionForSameRootKey() throws Exception {
        Key rootKey = KeyFactory.createKey("Parent", 1);
        Key key = KeyFactory.createKey(rootKey, "Child", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        gtx.getAsMap(key);
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        gtx.delete(key);
        tx.commit();
        assertThat(tester.count("Child"), is(1));
        assertThat(gtx.lockMap.size(), is(0));
        assertThat(gtx.globalJournalMap.size(), is(0));
        assertThat(gtx.localJournalMap.size(), is(1));
        assertThat(gtx.localJournalMap.containsKey(key), is(true));
        assertThat(gtx.localJournalMap.get(key), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntityAsGlobalTransaction() throws Exception {
        Key rootKey = KeyFactory.createKey("Parent", 1);
        Key key = KeyFactory.createKey(rootKey, "Child", 1);
        Key key2 = KeyFactory.createKey("Hoge", 1);
        gtx.put(new Entity(key2));
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        gtx.delete(key);
        tx.commit();
        assertThat(gtx.lockMap.size(), is(1));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(notNullValue()));
        assertThat(lock.globalTransactionKey, is(gtx.globalTransactionKey));
        assertThat(lock.timestamp, is(not(0L)));
        assertThat(gtx.globalJournalMap.size(), is(1));
        assertThat(gtx.globalJournalMap.get(key), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntities() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        DatastoreUtil.put(ds, null, new Entity(key));
        gtx.delete(Arrays.asList(key, key2));
        assertThat(tester.count("Hoge"), is(1));
        assertThat(gtx.lockMap.size(), is(1));
        Lock lock = gtx.lockMap.get(key2);
        assertThat(lock, is(notNullValue()));
        assertThat(lock.globalTransactionKey, is(gtx.globalTransactionKey));
        assertThat(lock.timestamp, is(not(0L)));
        assertThat(gtx.globalJournalMap.size(), is(1));
        assertThat(gtx.globalJournalMap.containsKey(key2), is(true));
        assertThat(gtx.globalJournalMap.get(key2), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesVarargs() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 2);
        DatastoreUtil.put(ds, null, new Entity(key));
        gtx.delete(key, key2);
        assertThat(tester.count("Hoge"), is(1));
        assertThat(gtx.lockMap.size(), is(1));
        Lock lock = gtx.lockMap.get(key2);
        assertThat(lock, is(notNullValue()));
        assertThat(lock.globalTransactionKey, is(gtx.globalTransactionKey));
        assertThat(lock.timestamp, is(not(0L)));
        assertThat(gtx.globalJournalMap.size(), is(1));
        assertThat(gtx.globalJournalMap.containsKey(key2), is(true));
        assertThat(gtx.globalJournalMap.get(key2), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAllAsLocalTransaction() throws Exception {
        Key rootKey = KeyFactory.createKey("Parent", 1);
        Key key = KeyFactory.createKey(rootKey, "Child", 1);
        Key key2 = KeyFactory.createKey(key, "Grandchild", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        DatastoreUtil.put(ds, null, new Entity(key2));
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        gtx.deleteAll(key);
        tx.commit();
        assertThat(tester.count("Child"), is(1));
        assertThat(tester.count("Grandchild"), is(1));
        assertThat(gtx.lockMap.size(), is(0));
        assertThat(gtx.globalJournalMap.size(), is(0));
        assertThat(gtx.localJournalMap.size(), is(2));
        assertThat(gtx.localJournalMap.containsKey(key), is(true));
        assertThat(gtx.localJournalMap.containsKey(key2), is(true));
        assertThat(gtx.localJournalMap.get(key), is(nullValue()));
        assertThat(gtx.localJournalMap.get(key2), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAllAsLocalTransactionForSameRootKey() throws Exception {
        Key rootKey = KeyFactory.createKey("Parent", 1);
        Key key = KeyFactory.createKey(rootKey, "Child", 1);
        Key key2 = KeyFactory.createKey(key, "Grandchild", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        DatastoreUtil.put(ds, null, new Entity(key2));
        gtx.getAsMap(rootKey);
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        gtx.deleteAll(key);
        tx.commit();
        assertThat(tester.count("Child"), is(1));
        assertThat(tester.count("Grandchild"), is(1));
        assertThat(gtx.lockMap.size(), is(0));
        assertThat(gtx.globalJournalMap.size(), is(0));
        assertThat(gtx.localJournalMap.size(), is(2));
        assertThat(gtx.localJournalMap.containsKey(key), is(true));
        assertThat(gtx.localJournalMap.containsKey(key2), is(true));
        assertThat(gtx.localJournalMap.get(key), is(nullValue()));
        assertThat(gtx.localJournalMap.get(key2), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAllAsGlobalTransaction() throws Exception {
        Key rootKey = KeyFactory.createKey("Parent", 1);
        Key key = KeyFactory.createKey(rootKey, "Child", 1);
        Key key2 = KeyFactory.createKey(rootKey, "Child", 2);
        Key key3 = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        DatastoreUtil.put(ds, null, new Entity(key2));
        gtx.getAsMap(key3);
        gtx.deleteAll(rootKey);
        assertThat(gtx.lockMap.size(), is(1));
        Lock lock = gtx.lockMap.get(rootKey);
        assertThat(lock, is(notNullValue()));
        assertThat(lock.globalTransactionKey, is(gtx.globalTransactionKey));
        assertThat(lock.timestamp, is(not(0L)));
        assertThat(gtx.globalJournalMap.size(), is(2));
        assertThat(gtx.globalJournalMap.containsKey(key), is(true));
        assertThat(gtx.globalJournalMap.get(key), is(nullValue()));
        assertThat(gtx.globalJournalMap.containsKey(key2), is(true));
        assertThat(gtx.globalJournalMap.get(key2), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isLocalTransaction() throws Exception {
        assertThat(gtx.isLocalTransaction(), is(true));
        gtx.put(new Entity("Hoge"));
        assertThat(gtx.isLocalTransaction(), is(true));
        gtx.put(new Entity("Hoge"));
        assertThat(gtx.isLocalTransaction(), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void commitLocalTransaction() throws Exception {
        gtx.put(new Entity("Hoge"));
        Journal.apply(ds, gtx.localTransaction, gtx.localJournalMap);
        gtx.commitLocalTransaction();
        assertThat(tester.count("Hoge"), is(1));
        assertThat(tester.count(GlobalTransaction.KIND), is(0));
        assertThat(tester.count(Lock.KIND), is(0));
        assertThat(tester.count(Journal.KIND), is(0));
        assertThat(gtx.isActive(), is(false));
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void commitGlobalTransactionInternally() throws Exception {
        gtx.put(new Entity("Hoge"));
        Journal.apply(ds, gtx.localTransaction, gtx.localJournalMap);
        gtx.commitGlobalTransactionInternally();
        Entity entity = DatastoreUtil.get(ds, null, gtx.globalTransactionKey);
        assertThat(entity, is(notNullValue()));
        assertThat((Boolean) entity
            .getProperty(GlobalTransaction.VALID_PROPERTY), is(true));
        assertThat(gtx.isActive(), is(false));
        assertThat(gtx.localTransaction.isActive(), is(false));
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
        assertThat(tester.count("Hoge"), is(1));
        String encodedKey = KeyFactory.keyToString(gtx.globalTransactionKey);
        assertThat(tester.tasks.size(), is(1));
        TaskQueueAddRequest task = tester.tasks.get(0);
        assertThat(task.getQueueName(), is(GlobalTransaction.QUEUE_NAME));
        assertThat(task.getUrl(), is(GlobalTransactionServlet.SERVLET_PATH));
        assertThat(task.getBody(), is(GlobalTransactionServlet.COMMAND_NAME
            + "="
            + GlobalTransactionServlet.ROLLFORWARD_COMMAND
            + "&"
            + GlobalTransactionServlet.KEY_NAME
            + "="
            + encodedKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toEntity() throws Exception {
        Entity entity = gtx.toEntity();
        assertThat(entity, is(notNullValue()));
        assertThat((Boolean) entity
            .getProperty(GlobalTransaction.VALID_PROPERTY), is(gtx.valid));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setLocalTransactionRootKey() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        gtx.setLocalTransactionRootKey(rootKey);
        assertThat(gtx.localTransactionRootKey, is(rootKey));
        assertThat(gtx.globalTransactionKey, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void commitGlobalTransaction() throws Exception {
        gtx.put(new Entity("Hoge"));
        gtx.put(new Entity("Hoge2"));
        Journal.apply(ds, gtx.localTransaction, gtx.localJournalMap);
        gtx.commitGlobalTransaction();
        assertThat(tester.count("Hoge"), is(1));
        assertThat(tester.count("Hoge2"), is(1));
        assertThat(tester.count(GlobalTransaction.KIND), is(0));
        assertThat(tester.count(Lock.KIND), is(0));
        assertThat(tester.count(Journal.KIND), is(0));
        assertThat(gtx.isActive(), is(false));
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putJournals() throws Exception {
        gtx.put(new Entity("Hoge"));
        gtx.put(new Entity("Hoge2"));
        gtx.putJournals();
        assertThat(tester.count(Journal.KIND), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void commit() throws Exception {
        gtx.put(new Entity("Hoge"));
        gtx.put(new Entity("Hoge2"));
        gtx.commit();
        assertThat(tester.count("Hoge"), is(1));
        assertThat(tester.count("Hoge2"), is(1));
        assertThat(tester.count(GlobalTransaction.KIND), is(0));
        assertThat(tester.count(Lock.KIND), is(0));
        assertThat(tester.count(Journal.KIND), is(0));
        assertThat(gtx.isActive(), is(false));
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollbackLocalTransaction() throws Exception {
        gtx.put(new Entity("Hoge"));
        gtx.rollbackLocalTransaction();
        assertThat(tester.count("Hoge"), is(0));
        assertThat(tester.count(GlobalTransaction.KIND), is(0));
        assertThat(tester.count(Lock.KIND), is(0));
        assertThat(tester.count(Journal.KIND), is(0));
        assertThat(gtx.isActive(), is(false));
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollbackGlobalTransaction() throws Exception {
        gtx.put(new Entity("Hoge"));
        Journal.put(ds, gtx.globalTransactionKey, gtx.globalJournalMap);
        gtx.rollbackGlobalTransaction();
        assertThat(tester.count("Hoge"), is(0));
        assertThat(tester.count(GlobalTransaction.KIND), is(0));
        assertThat(tester.count(Lock.KIND), is(0));
        assertThat(tester.count(Journal.KIND), is(0));
        assertThat(gtx.isActive(), is(false));
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollback() throws Exception {
        gtx.put(new Entity("Hoge"));
        gtx.put(new Entity("Hoge"));
        Journal.put(ds, gtx.globalTransactionKey, gtx.globalJournalMap);
        gtx.rollback();
        assertThat(tester.count("Hoge"), is(0));
        assertThat(tester.count(GlobalTransaction.KIND), is(0));
        assertThat(tester.count(Lock.KIND), is(0));
        assertThat(tester.count(Journal.KIND), is(0));
        assertThat(gtx.isActive(), is(false));
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollbackAsyncGlobalTransaction() throws Exception {
        gtx.setLocalTransactionRootKey(KeyFactory.createKey("Hoge", 1));
        String encodedKey = Datastore.keyToString(gtx.globalTransactionKey);
        gtx.rollbackAsyncGlobalTransaction();
        assertThat(gtx.isActive(), is(false));
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
        assertThat(tester.tasks.size(), is(1));
        TaskQueueAddRequest task = tester.tasks.get(0);
        assertThat(task.getQueueName(), is(GlobalTransaction.QUEUE_NAME));
        assertThat(task.getUrl(), is(GlobalTransactionServlet.SERVLET_PATH));
        assertThat(task.getBody(), is(GlobalTransactionServlet.COMMAND_NAME
            + "="
            + GlobalTransactionServlet.ROLLBACK_COMMAND
            + "&"
            + GlobalTransactionServlet.KEY_NAME
            + "="
            + encodedKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollbackAsync() throws Exception {
        gtx.put(new Entity("Hoge"));
        gtx.put(new Entity("Hoge"));
        String encodedKey = KeyFactory.keyToString(gtx.globalTransactionKey);
        gtx.rollbackAsyncGlobalTransaction();
        assertThat(gtx.isActive(), is(false));
        assertThat(GlobalTransaction.getActiveTransactions().size(), is(0));
        assertThat(tester.tasks.size(), is(1));
        TaskQueueAddRequest task = tester.tasks.get(0);
        assertThat(task.getQueueName(), is(GlobalTransaction.QUEUE_NAME));
        assertThat(task.getUrl(), is(GlobalTransactionServlet.SERVLET_PATH));
        assertThat(task.getBody(), is(GlobalTransactionServlet.COMMAND_NAME
            + "="
            + GlobalTransactionServlet.ROLLBACK_COMMAND
            + "&"
            + GlobalTransactionServlet.KEY_NAME
            + "="
            + encodedKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void submitRollForwardJob() throws Exception {
        gtx.getAsMap(KeyFactory.createKey("Hoge", 1));
        String encodedKey = KeyFactory.keyToString(gtx.globalTransactionKey);
        GlobalTransaction.submitRollForwardJob(
            gtx.localTransaction,
            gtx.globalTransactionKey,
            GlobalTransaction.ROLL_FORWARD_DELAY);
        assertThat(tester.tasks.size(), is(1));
        TaskQueueAddRequest task = tester.tasks.get(0);
        assertThat(task.getQueueName(), is(GlobalTransaction.QUEUE_NAME));
        assertThat(task.getUrl(), is(GlobalTransactionServlet.SERVLET_PATH));
        assertThat(task.getBody(), is(GlobalTransactionServlet.COMMAND_NAME
            + "="
            + GlobalTransactionServlet.ROLLFORWARD_COMMAND
            + "&"
            + GlobalTransactionServlet.KEY_NAME
            + "="
            + encodedKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void submitRollbackJob() throws Exception {
        gtx.getAsMap(KeyFactory.createKey("Hoge", 1));
        String encodedKey = KeyFactory.keyToString(gtx.globalTransactionKey);
        GlobalTransaction.submitRollbackJob(gtx.globalTransactionKey);
        assertThat(tester.tasks.size(), is(1));
        TaskQueueAddRequest task = tester.tasks.get(0);
        assertThat(task.getQueueName(), is(GlobalTransaction.QUEUE_NAME));
        assertThat(task.getUrl(), is(GlobalTransactionServlet.SERVLET_PATH));
        assertThat(task.getBody(), is(GlobalTransactionServlet.COMMAND_NAME
            + "="
            + GlobalTransactionServlet.ROLLBACK_COMMAND
            + "&"
            + GlobalTransactionServlet.KEY_NAME
            + "="
            + encodedKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollForward() throws Exception {
        gtx.getAsMap(KeyFactory.createKey("Hoge2", 1));
        gtx.put(new Entity("Hoge"));
        Journal.put(ds, gtx.globalTransactionKey, gtx.globalJournalMap);
        gtx.commitGlobalTransactionInternally();
        GlobalTransaction.rollForward(ds, gtx.globalTransactionKey);
        assertThat(tester.count("Hoge"), is(1));
        assertThat(tester.count(GlobalTransaction.KIND), is(0));
        assertThat(tester.count(Lock.KIND), is(0));
        assertThat(tester.count(Journal.KIND), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollbackByGlobalTransactionKey() throws Exception {
        gtx.put(new Entity("Hoge"));
        Journal.put(ds, gtx.globalTransactionKey, gtx.globalJournalMap);
        GlobalTransaction.rollback(ds, gtx.globalTransactionKey);
        assertThat(tester.count("Hoge"), is(0));
        assertThat(tester.count(Lock.KIND), is(0));
        assertThat(tester.count(Journal.KIND), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyLockSizeWithin100() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        gtx.getAsMap(key);
        for (int i = 1; i <= 100; i++) {
            gtx.put(new Entity("Hoge" + i));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyLockSizeWithin100ForGetAsMap() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        gtx.getAsMap(key);
        for (int i = 1; i <= 100; i++) {
            gtx.getAsMap(KeyFactory.createKey("Hoge" + i, i));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyJournalSizeWithin500ForPut() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Key parentKey2 = KeyFactory.createKey("Parent", 2);
        gtx.getAsMap(parentKey2);
        for (int i = 1; i <= 500; i++) {
            gtx.put(new Entity(KeyFactory.createKey(parentKey, "Hoge", i)));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyJournalSizeWithin500ForDelete() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Key parentKey2 = KeyFactory.createKey("Parent", 2);
        gtx.getAsMap(parentKey2);
        for (int i = 1; i <= 500; i++) {
            gtx.delete(KeyFactory.createKey(parentKey, "Hoge", i));
        }
    }
}