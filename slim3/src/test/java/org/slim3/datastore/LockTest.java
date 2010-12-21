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

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class LockTest extends AppEngineTestCase {

    private AsyncDatastoreService ds =
        DatastoreServiceFactory.getAsyncDatastoreService();

    /**
     * @throws Exception
     */
    @Test
    public void createKey() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        assertThat(key.getParent(), is(rootKey));
        assertThat(key.getKind(), is(Lock.KIND));
        assertThat(key.getId(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void createKeyForNoRoot() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key childKey = KeyFactory.createKey(rootKey, "Child", 1);
        Lock.createKey(childKey);
        fail();
    }

    /**
     * @throws Exception
     */
    @Test
    public void toLock() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock =
            Lock.toLock(ds, new Lock(
                ds,
                globalTransactionKey,
                rootKey,
                timestamp).toEntity());
        assertThat(lock.key, is(key));
        assertThat(lock.globalTransactionKey, is(globalTransactionKey));
        assertThat(lock.timestamp, is(timestamp));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOrNull() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        DatastoreUtil.put(ds, null, new Lock(
            ds,
            globalTransactionKey,
            rootKey,
            timestamp).toEntity());
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        Lock lock = Lock.getOrNull(ds, tx, key);
        assertThat(lock.globalTransactionKey, is(globalTransactionKey));
        assertThat(lock.timestamp, is(timestamp));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOrNullWhenNotFound() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        assertThat(Lock.getOrNull(ds, tx, key), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteInTx() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        DatastoreUtil.put(ds, null, lock.toEntity());
        Lock.deleteInTx(ds, globalTransactionKey, lock.key);
        assertThat(DatastoreUtil.getOrNull(ds, null, lock.key), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteLocksInTx() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        DatastoreUtil.put(ds, null, lock.toEntity());
        Lock.deleteInTx(ds, globalTransactionKey, Arrays.asList(lock));
        assertThat(DatastoreUtil
            .getAsMap(ds, null, Arrays.asList(lock.key))
            .size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteInTxByGlobalTransactionKey() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        DatastoreUtil.put(ds, null, lock.toEntity());
        Lock.deleteInTx(ds, globalTransactionKey);
        assertThat(DatastoreUtil.getOrNull(ds, null, lock.key), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteWithoutTxByGlobalTransactionKey() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        DatastoreUtil.put(ds, null, lock.toEntity());
        Lock.deleteWithoutTx(ds, globalTransactionKey);
        assertThat(DatastoreUtil.getOrNull(ds, null, lock.key), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteWithoutTxByLocks() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        DatastoreUtil.put(ds, null, lock.toEntity());
        Lock.deleteWithoutTx(ds, Arrays.asList(lock));
        assertThat(tester.count(Lock.KIND), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getKeys() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        DatastoreUtil.put(ds, null, lock.toEntity());
        List<Key> keys = Lock.getKeys(ds, globalTransactionKey);
        assertThat(keys.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyAndGetAsMap() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Hoge"));
        Map<Key, Entity> map =
            Lock.verifyAndGetAsMap(
                ds,
                DatastoreUtil.beginTransaction(ds),
                key,
                Arrays.asList(key));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyAndGetAsMapWhenNoEntityIsFound() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Map<Key, Entity> map =
            Lock.verifyAndGetAsMap(
                ds,
                DatastoreUtil.beginTransaction(ds),
                key,
                Arrays.asList(key));
        assertThat(map.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void verifyAndGetOrNullWhenLockError() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(Lock.createKey(key)));
        Lock.verifyAndGetAsMap(
            ds,
            DatastoreUtil.beginTransaction(ds),
            key,
            Arrays.asList(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructor() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        assertThat(lock.ds, is(ds));
        assertThat(lock.key, is(key));
        assertThat(lock.rootKey, is(rootKey));
        assertThat(lock.globalTransactionKey, is(globalTransactionKey));
        assertThat(lock.timestamp, is(timestamp));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toEntity() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        Entity entity = lock.toEntity();
        assertThat(entity.getKey(), is(key));
        assertThat(
            (Key) entity.getProperty(Lock.GLOBAL_TRANSACTION_KEY_PROPERTY),
            is(globalTransactionKey));
        assertThat(
            (Long) entity.getProperty(Lock.TIMESTAMP_PROPERTY),
            is(timestamp));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockWhenOtherIsNotFound() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        lock.lock();
        assertThat(
            DatastoreUtil.getOrNull(ds, null, lock.getKey()),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockWhenOtherIsNotTimeout() throws Exception {
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Key globalTransactionKey2 =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        Lock other = new Lock(ds, globalTransactionKey2, rootKey, timestamp);
        other.lock();
        try {
            lock.lock();
            fail();
        } catch (ConcurrentModificationException e) {
            System.out.println(e.getMessage());
        }
        assertThat(ds.getActiveTransactions().size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockAndGetAsMap() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        DatastoreUtil.put(ds, null, new Entity(key));
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Lock lock = new Lock(ds, globalTransactionKey, key, timestamp);
        Map<Key, Entity> map = lock.lockAndGetAsMap(Arrays.asList(key));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(
            DatastoreUtil.getOrNull(ds, null, lock.getKey()),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockAndGetAsMapWhenNoEntityIsFound() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Lock lock = new Lock(ds, globalTransactionKey, key, timestamp);
        assertThat(
            lock.lockAndGetAsMap(Arrays.asList(key)).get(key),
            is(nullValue()));
        assertThat(
            DatastoreUtil.getOrNull(ds, null, lock.getKey()),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void verifyForNoTimeout() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Key globalTransactionKey2 =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        Lock other = new Lock(ds, globalTransactionKey2, rootKey, timestamp);
        lock.verify(other);
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyForSameGlobalTransactionKey() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        Lock other = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        lock.verify(other);
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyForTimeout() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Key globalTransactionKey2 =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        Lock other =
            new Lock(ds, globalTransactionKey2, rootKey, timestamp
                - Lock.TIMEOUT
                - 1);
        lock.verify(other);
        GlobalTransaction gtx =
            GlobalTransaction.toGlobalTransaction(ds, DatastoreUtil.get(
                ds,
                null,
                globalTransactionKey2));
        assertThat(gtx.valid, is(false));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void verifyForTimeoutAndValidGtxExists() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Key globalTransactionKey2 =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        Lock other =
            new Lock(ds, globalTransactionKey2, rootKey, timestamp
                - Lock.TIMEOUT
                - 1);
        Entity entity = new Entity(globalTransactionKey2);
        entity.setUnindexedProperty(GlobalTransaction.VALID_PROPERTY, true);
        DatastoreUtil.put(ds, null, entity);
        lock.verify(other);
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyForTimeoutAndInalidGtxExists() throws Exception {
        Key rootKey = KeyFactory.createKey("Hoge", 1);
        Key globalTransactionKey =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        Key globalTransactionKey2 =
            DatastoreUtil.allocateId(ds, GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(ds, globalTransactionKey, rootKey, timestamp);
        Lock other =
            new Lock(ds, globalTransactionKey2, rootKey, timestamp
                - Lock.TIMEOUT
                - 1);
        Entity entity = new Entity(globalTransactionKey2);
        entity.setUnindexedProperty(GlobalTransaction.VALID_PROPERTY, false);
        DatastoreUtil.put(ds, null, entity);
        lock.verify(other);
        GlobalTransaction gtx =
            GlobalTransaction.toGlobalTransaction(ds, DatastoreUtil.get(
                ds,
                null,
                globalTransactionKey2));
        assertThat(gtx.valid, is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createConcurrentModificationException() throws Exception {
        Key targetKey = KeyFactory.createKey("Hoge", 1);
        ConcurrentModificationException e =
            Lock.createConcurrentModificationException(targetKey);
        assertThat(e, is(notNullValue()));
        System.out.println(e);
    }
}