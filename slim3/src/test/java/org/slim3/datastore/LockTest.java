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

import java.util.ConcurrentModificationException;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

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
    public void get() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Datastore.put(new Lock(globalTransactionKey, rootKey, timestamp)
            .toEntity());
        Lock lock =
            Lock.get(Datastore.beginTransaction(), Lock.createKey(rootKey));
        assertThat(lock.globalTransactionKey, is(globalTransactionKey));
        assertThat(lock.timestamp, is(timestamp));
    }

    /**
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Datastore.put(lock.toEntity());
        Lock.delete(globalTransactionKey);
        assertThat(Datastore.query(Lock.KIND, lock.key).count(), is(0));
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
    public void lockWhenOtherWithin30seconds() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key rootKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Lock other = new Lock(globalTransactionKey, rootKey, timestamp);
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
    public void lockWhenOtherOver30seconds() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, targetKey, timestamp);
        Lock other =
            new Lock(globalTransactionKey, targetKey, timestamp
                - Lock.THIRTY_SECONDS
                - 1);
        other.lock();
        lock.lock();
        assertThat(Datastore.get(lock.getKey()), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isLockByWithin30seconds() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Lock other = new Lock(globalTransactionKey, rootKey, timestamp);
        assertThat(lock.isLockedBy(other), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isLockByOver30seconds() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Lock other =
            new Lock(globalTransactionKey, rootKey, timestamp
                - Lock.THIRTY_SECONDS
                - 1);
        assertThat(lock.isLockedBy(other), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isLockByOver30secondsAndGtxExists() throws Exception {
        Key rootKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, rootKey, timestamp);
        Lock other =
            new Lock(globalTransactionKey, rootKey, timestamp
                - Lock.THIRTY_SECONDS
                - 1);
        Entity entity = new Entity(globalTransactionKey);
        Datastore.put(entity);
        assertThat(lock.isLockedBy(other), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createConcurrentModificationException() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        long timestamp = System.currentTimeMillis();
        Lock lock = new Lock(globalTransactionKey, targetKey, timestamp);
        ConcurrentModificationException e =
            lock.createConcurrentModificationException();
        assertThat(e, is(notNullValue()));
        System.out.println(e);
    }
}