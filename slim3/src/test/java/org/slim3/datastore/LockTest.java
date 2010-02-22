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

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class LockTest extends AppEngineTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void createKey() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
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
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key childKey = Datastore.createKey(rootKey, "Child", 1);
        Lock.createKey(childKey);
        fail();
    }

    /**
     * @throws Exception
     */
    @Test
    public void toLock() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock =
            Lock.toLock(new Lock(globalTransactionKey, rootKey, timestamp)
                .toEntity());
        assertThat(lock.key, is(key));
        assertThat(lock.globalTransactionKey, is(globalTransactionKey));
        assertThat(lock.timestamp, is(timestamp));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOrNull() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Datastore.putWithoutTx(new Lock(
            globalTransactionKey,
            rootKey,
            timestamp).toEntity());
        Transaction tx = Datastore.beginTransaction();
        Lock lock = Lock.getOrNull(tx, key);
        assertThat(lock.globalTransactionKey, is(globalTransactionKey));
        assertThat(lock.timestamp, is(timestamp));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOrNullWhenNotFound() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        Transaction tx = Datastore.beginTransaction();
        assertThat(Lock.getOrNull(tx, key), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteInTx() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Datastore.put(lock.toEntity());
        Lock.deleteInTx(globalTransactionKey, lock.key);
        assertThat(Datastore.query(Lock.KIND, lock.key).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteLocksInTx() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Datastore.put(lock.toEntity());
        Lock.deleteInTx(globalTransactionKey, Arrays.asList(lock));
        assertThat(Datastore.query(Lock.KIND, lock.key).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteInTxByGlobalTransactionKey() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Datastore.put(lock.toEntity());
        Lock.deleteInTx(globalTransactionKey);
        assertThat(Datastore.query(Lock.KIND, lock.key).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteWithoutTx() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Datastore.put(lock.toEntity());
        Lock.deleteWithoutTx(globalTransactionKey);
        assertThat(Datastore.query(Lock.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getKeys() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Datastore.put(lock.toEntity());
        List<Key> keys = Lock.getKeys(globalTransactionKey);
        assertThat(keys.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyAndGetAsMap() throws Exception {
        Key key = Datastore.putWithoutTx(new Entity("Hoge"));
        Map<Key, Entity> map =
            Lock.verifyAndGetAsMap(Datastore.beginTransaction(), key, Arrays
                .asList(key));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyAndGetAsMapWhenNoEntityIsFound() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Map<Key, Entity> map =
            Lock.verifyAndGetAsMap(Datastore.beginTransaction(), key, Arrays
                .asList(key));
        assertThat(map.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void verifyAndGetOrNullWhenLockError() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.putWithoutTx(new Entity(Lock.createKey(key)));
        Lock.verifyAndGetAsMap(Datastore.beginTransaction(), key, Arrays
            .asList(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructor() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
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
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key key = Lock.createKey(rootKey);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
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
        Key rootKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        lock.lock();
        assertThat(Datastore.get(lock.getKey()), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockWhenOtherIsNotTimeout() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key globalTransactionKey2 =
            Datastore.allocateId(GlobalTransaction.KIND);
        Key rootKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Lock other = new Lock(globalTransactionKey2, rootKey, timestamp);
        other.lock();
        try {
            lock.lock();
            fail();
        } catch (ConcurrentModificationException e) {
            System.out.println(e.getMessage());
        }
        assertThat(DatastoreServiceFactory
            .getDatastoreService()
            .getActiveTransactions()
            .size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockAndGetAsMap() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(key));
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Lock lock = new Lock(globalTransactionKey, key, timestamp);
        Map<Key, Entity> map = lock.lockAndGetAsMap(Arrays.asList(key));
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
        assertThat(Datastore.get(lock.getKey()), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockAndGetAsMapWhenNoEntityIsFound() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Lock lock = new Lock(globalTransactionKey, key, timestamp);
        assertThat(
            lock.lockAndGetAsMap(Arrays.asList(key)).get(key),
            is(nullValue()));
        assertThat(Datastore.get(lock.getKey()), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void verifyForNoTimeout() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key globalTransactionKey2 =
            Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Lock other = new Lock(globalTransactionKey2, rootKey, timestamp);
        lock.verify(other);
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyForSameGlobalTransactionKey() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Lock other = new Lock(globalTransactionKey, rootKey, timestamp);
        lock.verify(other);
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyForTimeout() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key globalTransactionKey2 =
            Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Lock other =
            new Lock(globalTransactionKey2, rootKey, timestamp
                - Lock.TIMEOUT
                - 1);
        lock.verify(other);
        GlobalTransaction gtx =
            GlobalTransaction.toGlobalTransaction(Datastore
                .getWithoutTx(globalTransactionKey2));
        assertThat(gtx.valid, is(false));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void verifyForTimeoutAndValidGtxExists() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key globalTransactionKey2 =
            Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Lock other =
            new Lock(globalTransactionKey2, rootKey, timestamp
                - Lock.TIMEOUT
                - 1);
        Entity entity = new Entity(globalTransactionKey2);
        entity.setUnindexedProperty(GlobalTransaction.VALID_PROPERTY, true);
        Datastore.put(entity);
        lock.verify(other);
    }

    /**
     * @throws Exception
     */
    @Test
    public void verifyForTimeoutAndInalidGtxExists() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key globalTransactionKey2 =
            Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Lock other =
            new Lock(globalTransactionKey2, rootKey, timestamp
                - Lock.TIMEOUT
                - 1);
        Entity entity = new Entity(globalTransactionKey2);
        entity.setUnindexedProperty(GlobalTransaction.VALID_PROPERTY, false);
        Datastore.put(entity);
        lock.verify(other);
        GlobalTransaction gtx =
            GlobalTransaction.toGlobalTransaction(Datastore
                .getWithoutTx(globalTransactionKey2));
        assertThat(gtx.valid, is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createConcurrentModificationException() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        ConcurrentModificationException e =
            Lock.createConcurrentModificationException(targetKey);
        assertThat(e, is(notNullValue()));
        System.out.println(e);
    }
}