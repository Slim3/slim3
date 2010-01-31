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

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class GlobalTransactionTest extends AppEngineTestCase {

    private GlobalTransaction gtx;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        gtx = new GlobalTransaction();
    }

    /**
     * @throws Exception
     */
    @Test
    public void exists() throws Exception {
        Key key = Datastore.put(new Entity(GlobalTransaction.KIND));
        assertThat(GlobalTransaction.exists(key), is(true));
        assertThat(GlobalTransaction.exists(Datastore
            .allocateId(GlobalTransaction.KIND)), is(false));
        assertThat(DatastoreServiceFactory
            .getDatastoreService()
            .getActiveTransactions()
            .size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getId() throws Exception {
        assertThat(gtx.getId(), is(not(0L)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void lock() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        gtx.lock(key);
        Lock lock = gtx.lockMap.get(key);
        assertThat(lock, is(notNullValue()));
        assertThat(Datastore.get(lock.key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void lockForChildKey() throws Exception {
        Key parentKey = Datastore.createKey("Parent", 1);
        Key childKey = Datastore.createKey(parentKey, "Child", 1);
        gtx.lock(childKey);
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockForSameKey() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        gtx.lock(key);
        gtx.lock(key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void lockWhenConcurrentModificationExceptionOccurred()
            throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        GlobalTransaction otherGtx = new GlobalTransaction();
        otherGtx.lock(key2);
        try {
            gtx.lock(key);
            gtx.lock(key2);
            fail();
        } catch (ConcurrentModificationException e) {
            assertThat(gtx.lockMap.get(key), is(nullValue()));
            assertThat(Datastore.getAsMap(Lock.createKey(key)).size(), is(0));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void unlock() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        gtx.lock(key);
        gtx.unlock();
        assertThat(gtx.isActive(), is(false));
        assertThat(gtx.lockMap.get(key), is(nullValue()));
        assertThat(Datastore.getAsMap(Lock.createKey(key)).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntity() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(key));
        assertThat(gtx.get(key), is(notNullValue()));
        assertThat(gtx.lockMap.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityUsingModelClass() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(key));
        assertThat(gtx.get(Hoge.class, key), is(notNullValue()));
        assertThat(gtx.lockMap.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityUsingModelMeta() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(key));
        assertThat(gtx.get(HogeMeta.get(), key), is(notNullValue()));
        assertThat(gtx.lockMap.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntities() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(key));
        assertThat(gtx.get(Arrays.asList(key)), is(notNullValue()));
        assertThat(gtx.lockMap.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesUsingModelClass() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(key));
        assertThat(gtx.get(Hoge.class, Arrays.asList(key)), is(notNullValue()));
        assertThat(gtx.lockMap.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesUsingModelMeta() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(key));
        assertThat(
            gtx.get(HogeMeta.get(), Arrays.asList(key)),
            is(notNullValue()));
        assertThat(gtx.lockMap.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesVarargs() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(key));
        assertThat(gtx.get(key, key), is(notNullValue()));
        assertThat(gtx.lockMap.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesVarargsUsingModelClass() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(key));
        assertThat(gtx.get(Hoge.class, key, key), is(notNullValue()));
        assertThat(gtx.lockMap.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesVarargsUsingModelMeta() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(key));
        assertThat(gtx.get(HogeMeta.get(), key, key), is(notNullValue()));
        assertThat(gtx.lockMap.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntity() throws Exception {
        Key rootKey = Datastore.createKey("Parent", 1);
        Key key = Datastore.createKey(rootKey, "Child", 1);
        Entity entity = new Entity(key);
        assertThat(gtx.put(entity), is(entity.getKey()));
        assertThat(gtx.lockMap.containsKey(rootKey), is(true));
        assertThat(gtx.rootKeySet.contains(rootKey), is(true));
        Journal journal = gtx.journalMap.get(key);
        assertThat(journal, is(notNullValue()));
        assertThat(journal.key, is(Journal.createKey(key)));
        assertThat(journal.targetEntityProto, is(notNullValue()));
        assertThat(Datastore.getAsMap(key).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModel() throws Exception {
        Hoge hoge = new Hoge();
        assertThat(gtx.put(hoge), is(hoge.getKey()));
        assertThat(gtx.lockMap.containsKey(hoge.getKey()), is(true));
        assertThat(gtx.rootKeySet.contains(hoge.getKey()), is(true));
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
        assertThat(gtx.lockMap.size(), is(2));
        assertThat(gtx.rootKeySet.size(), is(2));
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
        assertThat(gtx.lockMap.size(), is(2));
        assertThat(gtx.rootKeySet.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntity() throws Exception {
        Key rootKey = Datastore.createKey("Parent", 1);
        Key key = Datastore.createKey(rootKey, "Child", 1);
        gtx.delete(key);
        assertThat(gtx.lockMap.containsKey(rootKey), is(true));
        assertThat(gtx.rootKeySet.contains(rootKey), is(true));
        Journal journal = gtx.journalMap.get(key);
        assertThat(journal, is(notNullValue()));
        assertThat(journal.key, is(Journal.createKey(key)));
        assertThat(journal.targetEntityProto, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntities() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        gtx.delete(Arrays.asList(key, key2));
        assertThat(gtx.lockMap.size(), is(2));
        assertThat(gtx.lockMap.containsKey(key), is(true));
        assertThat(gtx.lockMap.containsKey(key2), is(true));
        assertThat(gtx.rootKeySet.size(), is(2));
        assertThat(gtx.rootKeySet.contains(key), is(true));
        assertThat(gtx.rootKeySet.contains(key2), is(true));
        Journal journal = gtx.journalMap.get(key);
        assertThat(journal, is(notNullValue()));
        assertThat(journal.key, is(Journal.createKey(key)));
        assertThat(journal.targetEntityProto, is(nullValue()));
        Journal journal2 = gtx.journalMap.get(key2);
        assertThat(journal2, is(notNullValue()));
        assertThat(journal2.key, is(Journal.createKey(key2)));
        assertThat(journal2.targetEntityProto, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesVarargs() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Key key2 = Datastore.createKey("Hoge", 2);
        gtx.delete(key, key2);
        assertThat(gtx.lockMap.size(), is(2));
        assertThat(gtx.lockMap.containsKey(key), is(true));
        assertThat(gtx.lockMap.containsKey(key2), is(true));
        assertThat(gtx.rootKeySet.size(), is(2));
        assertThat(gtx.rootKeySet.contains(key), is(true));
        assertThat(gtx.rootKeySet.contains(key2), is(true));
        Journal journal = gtx.journalMap.get(key);
        assertThat(journal, is(notNullValue()));
        assertThat(journal.key, is(Journal.createKey(key)));
        assertThat(journal.targetEntityProto, is(nullValue()));
        Journal journal2 = gtx.journalMap.get(key2);
        assertThat(journal2, is(notNullValue()));
        assertThat(journal2.key, is(Journal.createKey(key2)));
        assertThat(journal2.targetEntityProto, is(nullValue()));
    }
}