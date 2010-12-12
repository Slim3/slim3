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
import static org.junit.matchers.JUnitMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.slim3.datastore.meta.AaaMeta;
import org.slim3.datastore.meta.BbbMeta;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Aaa;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class DatastoreDelegateTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    private DatastoreDelegate delegate = new DatastoreDelegate();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CipherFactory.getFactory().setGlobalKey("xxxxxxxxxxxxxxxx");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        CipherFactory.getFactory().clearGlobalKey();
        System.clearProperty(DatastoreDelegate.DEADLINE);
    }

    /**
     * @throws Exception
     */
    @Test
    public void deadline() throws Exception {
        Double deadline = 1.0;
        DatastoreDelegate del = new DatastoreDelegate(deadline);
        assertThat(del.getDeadline(), is(deadline));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deadlineForDefaultConstructor() throws Exception {
        DatastoreDelegate del = new DatastoreDelegate();
        assertThat(del.getDeadline(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deadlineForSystemProperty() throws Exception {
        Double deadline = 1.0;
        System.setProperty(DatastoreDelegate.DEADLINE, deadline.toString());
        DatastoreDelegate del = new DatastoreDelegate();
        assertThat(del.getDeadline(), is(deadline));
    }

    /**
     * @throws Exception
     */
    @Test
    public void datastoreServices() throws Exception {
        Double deadline = 1.0;
        DatastoreDelegate del = new DatastoreDelegate(deadline);
        assertThat(del.getDatastoreService(), is(notNullValue()));
        assertThat(del.getAsyncDatastoreService(), is(notNullValue()));
        assertThat(del.dsConfig, is(notNullValue()));
        assertThat(del.dsConfig.getDeadline(), is(deadline));
    }

    /**
     * @throws Exception
     */
    @Test
    public void datastoreServicesForDefaultConstructor() throws Exception {
        DatastoreDelegate del = new DatastoreDelegate();
        assertThat(del.getDatastoreService(), is(notNullValue()));
        assertThat(del.getAsyncDatastoreService(), is(notNullValue()));
        assertThat(del.dsConfig, is(notNullValue()));
        assertThat(del.dsConfig.getDeadline(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void datastoreServicesForSystemProperty() throws Exception {
        Double deadline = 1.0;
        System.setProperty(DatastoreDelegate.DEADLINE, deadline.toString());
        DatastoreDelegate del = new DatastoreDelegate();
        assertThat(del.getDatastoreService(), is(notNullValue()));
        assertThat(del.getAsyncDatastoreService(), is(notNullValue()));
        assertThat(del.dsConfig, is(notNullValue()));
        assertThat(del.dsConfig.getDeadline(), is(deadline));
    }

    /**
     * @throws Exception
     */
    @Test
    public void beginTransaction() throws Exception {
        assertThat(delegate.beginTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void commit() throws Exception {
        Transaction tx = ds.beginTransaction();
        delegate.commit(tx);
        assertThat(tx.isActive(), is(false));
        assertThat(ds.getCurrentTransaction(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void commitForIllegalTx() throws Exception {
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.commit(tx);
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void rollback() throws Exception {
        Transaction tx = ds.beginTransaction();
        Key key = ds.put(new Entity("Hoge"));
        delegate.rollback(tx);
        assertThat(tx.isActive(), is(false));
        assertThat(ds.getCurrentTransaction(null), is(nullValue()));
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getActiveTransactions() throws Exception {
        assertThat(delegate.getActiveTransactions().size(), is(0));
        ds.beginTransaction();
        assertThat(delegate.getActiveTransactions().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getCurrentTransaction() throws Exception {
        assertThat(delegate.getCurrentTransaction(), is(nullValue()));
        ds.beginTransaction();
        assertThat(delegate.getCurrentTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void beginGlobalTransaction() throws Exception {
        assertThat(delegate.beginGlobalTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateId() throws Exception {
        assertThat(delegate.allocateId("Hoge"), is(not(nullValue())));
        assertThat(delegate.allocateId(Hoge.class), is(not(nullValue())));
        assertThat(delegate.allocateId(meta), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        assertThat(delegate.allocateId(parentKey, "Hoge"), is(not(nullValue())));
        assertThat(
            delegate.allocateId(parentKey, Hoge.class),
            is(not(nullValue())));
        assertThat(delegate.allocateId(parentKey, meta), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdAsync() throws Exception {
        assertThat(delegate.allocateIdAsync("Hoge"), is(not(nullValue())));
        assertThat(delegate.allocateIdAsync(Hoge.class), is(not(nullValue())));
        assertThat(delegate.allocateIdAsync(meta), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdAsyncWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        assertThat(
            delegate.allocateIdAsync(parentKey, "Hoge"),
            is(not(nullValue())));
        assertThat(
            delegate.allocateIdAsync(parentKey, Hoge.class),
            is(not(nullValue())));
        assertThat(
            delegate.allocateIdAsync(parentKey, meta),
            is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIds() throws Exception {
        KeyRange range = delegate.allocateIds("Hoge", 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = delegate.allocateIds(Hoge.class, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = delegate.allocateIds(meta, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsAsync() throws Exception {
        KeyRange range = delegate.allocateIdsAsync("Hoge", 2).get();
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = delegate.allocateIdsAsync(Hoge.class, 2).get();
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = delegate.allocateIdsAsync(meta, 2).get();
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        KeyRange range = delegate.allocateIds(parentKey, "Hoge", 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = delegate.allocateIds(parentKey, Hoge.class, 2);
        assertThat(range, is(notNullValue()));
        assertEquals(2, range.getSize());

        range = delegate.allocateIds(parentKey, meta, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsAsyncWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        KeyRange range = delegate.allocateIdsAsync(parentKey, "Hoge", 2).get();
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = delegate.allocateIdsAsync(parentKey, Hoge.class, 2).get();
        assertThat(range, is(notNullValue()));
        assertEquals(2, range.getSize());

        range = delegate.allocateIdsAsync(parentKey, meta, 2).get();
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createKey() throws Exception {
        assertThat(delegate.createKey("Hoge", 1), is(not(nullValue())));
        assertThat(delegate.createKey(Hoge.class, 1), is(not(nullValue())));
        assertThat(delegate.createKey(meta, 1), is(not(nullValue())));
        assertThat(delegate.createKey("Hoge", "aaa"), is(not(nullValue())));
        assertThat(delegate.createKey(Hoge.class, "aaa"), is(not(nullValue())));
        assertThat(delegate.createKey(meta, "aaa"), is(not(nullValue())));
        Key parentKey = KeyFactory.createKey("Parent", 1);
        assertThat(
            delegate.createKey(parentKey, "Hoge", 1),
            is(not(nullValue())));
        assertThat(
            delegate.createKey(parentKey, Hoge.class, 1),
            is(not(nullValue())));
        assertThat(delegate.createKey(parentKey, meta, 1), is(not(nullValue())));
        assertThat(
            delegate.createKey(parentKey, "Hoge", "aaa"),
            is(not(nullValue())));
        assertThat(
            delegate.createKey(parentKey, Hoge.class, "aaa"),
            is(not(nullValue())));
        assertThat(
            delegate.createKey(parentKey, meta, "aaa"),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void keyToString() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        String encodedKey = KeyFactory.keyToString(key);
        assertThat(delegate.keyToString(key), is(encodedKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void stringToKey() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        String encodedKey = KeyFactory.keyToString(key);
        assertThat(delegate.stringToKey(encodedKey), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putUniqueValue() throws Exception {
        assertThat(delegate.putUniqueValue("screenName", "aaa"), is(true));
        assertThat(ds.getActiveTransactions().size(), is(0));
        assertThat(delegate.putUniqueValue("screenName", "aaa"), is(false));
        assertThat(ds.getActiveTransactions().size(), is(0));
        assertThat(delegate.putUniqueValue("screenName", "bbb"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteUniqueValue() throws Exception {
        assertThat(delegate.putUniqueValue("screenName", "aaa"), is(true));
        delegate.deleteUniqueValue("screenName", "aaa");
        assertThat(delegate.putUniqueValue("screenName", "aaa"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = delegate.get(meta, key);
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = delegate.getAsync(meta, key).get();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullUsingModelMeta() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Hoge model = delegate.getOrNull(meta, key);
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullAsyncUsingModelMeta() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Hoge model = delegate.getOrNullAsync(meta, key).get();
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelWithoutTxUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = delegate.getWithoutTx(meta, key);
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelWithoutTxAsyncUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = delegate.getWithoutTxAsync(meta, key).get();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullWithoutTxUsingModelMeta() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Hoge model = delegate.getOrNullWithoutTx(meta, key);
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullWithoutTxAsyncUsingModelMeta() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Hoge model = delegate.getOrNullWithoutTxAsync(meta, key).get();
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelUsingModelClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = delegate.get(Hoge.class, key);
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncUsingModelClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = delegate.getAsync(Hoge.class, key).get();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullUsingModelClass() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Hoge model = delegate.getOrNull(Hoge.class, key);
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullAsyncUsingModelClass() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Hoge model = delegate.getOrNullAsync(Hoge.class, key).get();
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelWithoutTxUsingModelClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = delegate.getWithoutTx(Hoge.class, key);
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelWithoutTxAsyncUsingModelClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Hoge model = delegate.getWithoutTxAsync(Hoge.class, key).get();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullWithoutTxUsingModelClass() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Hoge model = delegate.getOrNullWithoutTx(Hoge.class, key);
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullWithoutTxAsyncUsingModelClass() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Hoge model = delegate.getOrNullWithoutTxAsync(Hoge.class, key).get();
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Aaa model = delegate.get(Aaa.class, key);
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelAsyncUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Aaa model = delegate.getAsync(Aaa.class, key).get();
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Aaa model = delegate.get(AaaMeta.get(), key);
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelAsyncUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Aaa model = delegate.getAsync(AaaMeta.get(), key).get();
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getModelUsingClassAndValidateKey() throws Exception {
        Key key = ds.put(new Entity("Aaa"));
        delegate.get(Hoge.class, key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncUsingClassAndValidateKey() throws Exception {
        Key key = ds.put(new Entity("Aaa"));
        try {
            delegate.getAsync(Hoge.class, key).get();
            fail();
        } catch (ExecutionException e) {
            assertThat(e.getCause(), is(IllegalArgumentException.class));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelUsingClassAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Hoge model = delegate.get(Hoge.class, key, 1L);
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncUsingClassAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Hoge model = delegate.getAsync(Hoge.class, key, 1L).get();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getModelUsingClassAndCheckVersionWhenError() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Hoge model = delegate.get(Hoge.class, key, 1L);
        assertThat(model, is(notNullValue()));
        delegate.get(Hoge.class, key, 0L);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncUsingClassAndCheckVersionWhenError()
            throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Hoge model = delegate.get(Hoge.class, key, 1L);
        assertThat(model, is(notNullValue()));
        try {
            delegate.getAsync(Hoge.class, key, 0L).get();
            fail();
        } catch (ExecutionException e) {
            assertThat(e.getCause(), is(ConcurrentModificationException.class));
        }
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getModelUsingModelMetaAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Hoge model = delegate.get(meta, key, 1L);
        assertThat(model, is(notNullValue()));
        delegate.get(meta, key, 0L);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncUsingModelMetaAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Hoge model = delegate.get(meta, key, 1L);
        assertThat(model, is(notNullValue()));
        try {
            delegate.getAsync(meta, key, 0L).get();
            fail();
        } catch (ExecutionException e) {
            assertThat(e.getCause(), is(ConcurrentModificationException.class));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelUsingClassAndCheckVersion() throws Exception {
        Key key = delegate.put(new Bbb());
        Aaa model = delegate.get(Aaa.class, key, 1L);
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelAsyncUsingClassAndCheckVersion() throws Exception {
        Key key = delegate.put(new Bbb());
        Aaa model = delegate.getAsync(Aaa.class, key, 1L).get();
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelUsingModelMetaAndCheckVersion() throws Exception {
        Key key = delegate.put(new Bbb());
        Aaa model = delegate.get(AaaMeta.get(), key, 1L);
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelAsyncUsingModelMetaAndCheckVersion()
            throws Exception {
        Key key = delegate.put(new Bbb());
        Aaa model = delegate.getAsync(AaaMeta.get(), key, 1L).get();
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getModelUsingClassAndCheckVersionForValidatingKey()
            throws Exception {
        Key key = delegate.put(new Bbb());
        delegate.get(Hoge.class, key, 1L);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncUsingClassAndCheckVersionForValidatingKey()
            throws Exception {
        Key key = delegate.put(new Bbb());
        try {
            delegate.getAsync(Hoge.class, key, 1L).get();
            fail();
        } catch (ExecutionException e) {
            assertThat(e.getCause(), is(IllegalArgumentException.class));

        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Hoge model = delegate.get(tx, meta, key);
        tx.rollback();
        assertThat(model, is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Hoge model = delegate.getAsync(tx, meta, key).get();
        tx.rollback();
        assertThat(model, is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelInTxUsingModelClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Hoge model = delegate.get(tx, Hoge.class, key);
        tx.rollback();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncInTxUsingModelClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Hoge model = delegate.getAsync(tx, Hoge.class, key).get();
        tx.rollback();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullInTxUsingModelClass() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Transaction tx = ds.beginTransaction();
        Hoge model = delegate.getOrNull(tx, Hoge.class, key);
        tx.rollback();
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullAsyncInTxUsingModelClass() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Transaction tx = ds.beginTransaction();
        Hoge model = delegate.getOrNullAsync(tx, Hoge.class, key).get();
        tx.rollback();
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelInTxUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Hoge model = delegate.get(tx, meta, key);
        tx.rollback();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncInTxUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Hoge model = delegate.getAsync(tx, meta, key).get();
        tx.rollback();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullInTxUsingModelMeta() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Transaction tx = ds.beginTransaction();
        Hoge model = delegate.getOrNull(tx, meta, key);
        tx.rollback();
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelOrNullAsyncInTxUsingModelMeta() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        Transaction tx = ds.beginTransaction();
        Hoge model = delegate.getOrNullAsync(tx, meta, key).get();
        tx.rollback();
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getModelInTxUsingModelClassAndValidateKey() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        delegate.get(null, Aaa.class, key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncInTxUsingModelClassAndValidateKey()
            throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        try {
            delegate.getAsync(null, Aaa.class, key).get();
            fail();
        } catch (ExecutionException e) {
            assertThat(e.getCause(), is(IllegalArgumentException.class));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelInTxUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Transaction tx = ds.beginTransaction();
        Aaa model = delegate.get(tx, Aaa.class, key);
        tx.rollback();
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelAsyncInTxUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Transaction tx = ds.beginTransaction();
        Aaa model = delegate.getAsync(tx, Aaa.class, key).get();
        tx.rollback();
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelInTxUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Transaction tx = ds.beginTransaction();
        Aaa model = delegate.get(tx, AaaMeta.get(), key);
        tx.rollback();
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelAsyncInTxUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Transaction tx = ds.beginTransaction();
        Aaa model = delegate.getAsync(tx, AaaMeta.get(), key).get();
        tx.rollback();
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getModelInTxUsingClassAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Transaction tx = delegate.beginTransaction();
        delegate.get(tx, Hoge.class, key, 0L);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncInTxUsingClassAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Transaction tx = delegate.beginTransaction();
        try {
            delegate.getAsync(tx, Hoge.class, key, 0L).get();
            fail();
        } catch (ExecutionException e) {
            assertThat(e.getCause(), is(ConcurrentModificationException.class));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelInTxUsingClassAndVersion() throws Exception {
        Key key = delegate.put(new Bbb());
        Transaction tx = ds.beginTransaction();
        Aaa model = delegate.get(tx, Aaa.class, key, 1L);
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelAsyncInTxUsingClassAndVersion() throws Exception {
        Key key = delegate.put(new Bbb());
        Transaction tx = ds.beginTransaction();
        Aaa model = delegate.getAsync(tx, Aaa.class, key, 1L).get();
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelInTxUsingModelMetaAndVersion() throws Exception {
        Key key = delegate.put(new Bbb());
        Transaction tx = ds.beginTransaction();
        Aaa model = delegate.get(tx, new AaaMeta(), key, 1L);
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelAsyncInTxUsingModelMetaAndVersion() throws Exception {
        Key key = delegate.put(new Bbb());
        Transaction tx = ds.beginTransaction();
        Aaa model = delegate.getAsync(tx, new AaaMeta(), key, 1L).get();
        assertThat(model, is(notNullValue()));
        assertThat(model.getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getModelInTxUsingClassAndVersionAndValidateKey()
            throws Exception {
        Key key = delegate.put(new Bbb());
        Transaction tx = ds.beginTransaction();
        delegate.get(tx, Hoge.class, key, 1L);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelIAsyncnTxUsingClassAndVersionAndValidateKey()
            throws Exception {
        Key key = delegate.put(new Bbb());
        Transaction tx = ds.beginTransaction();
        try {
            delegate.getAsync(tx, Hoge.class, key, 1L).get();
            fail();
        } catch (ExecutionException e) {
            assertThat(e.getCause(), is(IllegalArgumentException.class));
        }
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getModelInTxUsingModelMetaAndCheckVersion() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Transaction tx = delegate.beginTransaction();
        delegate.get(tx, meta, key, 0L);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelAsyncInTxUsingModelMetaAndCheckVersion()
            throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("version", 1);
        Key key = ds.put(entity);
        Transaction tx = delegate.beginTransaction();
        try {
            delegate.getAsync(tx, meta, key, 0L).get();
            fail();
        } catch (ExecutionException e) {
            assertThat(e.getCause(), is(ConcurrentModificationException.class));
        }
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getModelInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.get(tx, meta, key);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getModelAsyncInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.getAsync(tx, meta, key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = delegate.get(meta, Arrays.asList(key, key2));
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsyncUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models =
            delegate.getAsync(meta, Arrays.asList(key, key2)).get();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsWithoutTxUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models =
            delegate.getWithoutTx(meta, Arrays.asList(key, key2));
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsWithoutTxAsyncUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models =
            delegate.getWithoutTxAsync(meta, Arrays.asList(key, key2)).get();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = delegate.get(Hoge.class, Arrays.asList(key, key2));
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsyncUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models =
            delegate.getAsync(Hoge.class, Arrays.asList(key, key2)).get();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsWithoutTxUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models =
            delegate.getWithoutTx(Hoge.class, Arrays.asList(key, key2));
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsWithoutTxAsyncUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models =
            delegate
                .getWithoutTxAsync(Hoge.class, Arrays.asList(key, key2))
                .get();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        List<Aaa> models = delegate.get(Aaa.class, Arrays.asList(key));
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(1));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsyncUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        List<Aaa> models =
            delegate.getAsync(Aaa.class, Arrays.asList(key)).get();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(1));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        List<Aaa> models = delegate.get(new AaaMeta(), Arrays.asList(key));
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(1));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsyncUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        List<Aaa> models =
            delegate.getAsync(AaaMeta.get(), Arrays.asList(key)).get();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(1));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsUsingModelMetaForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = delegate.get(meta, key, key2);
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsyncUsingModelMetaForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = delegate.getAsync(meta, key, key2).get();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsWithoutTxUsingModelMetaForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = delegate.getWithoutTx(meta, key, key2);
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsWithoutTxAsyncUsingModelMetaForVarargs()
            throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = delegate.getWithoutTxAsync(meta, key, key2).get();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsUsingClassForVarargs() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        List<Aaa> models = delegate.get(Aaa.class, key, key2);
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsUsingAsyncClassForVarargs() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        List<Aaa> models = delegate.getAsync(Aaa.class, key, key2).get();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsUsingModelMetaForVarargs() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        List<Aaa> models = delegate.get(AaaMeta.get(), key, key2);
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsyncUsingModelMetaForVarargs() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        List<Aaa> models = delegate.getAsync(AaaMeta.get(), key, key2).get();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsUsingClassForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = delegate.get(Hoge.class, key, key2);
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsyncUsingClassForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = delegate.getAsync(Hoge.class, key, key2).get();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsWithoutTxUsingClassForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models = delegate.getWithoutTx(Hoge.class, key, key2);
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsWithoutTxAsyncUsingClassForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Hoge> models =
            delegate.getWithoutTxAsync(Hoge.class, key, key2).get();
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
        delegate.get(meta, key, key2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsyncWhenEntityNotFound() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 1);
        try {
            delegate.getAsync(meta, key, key2).get();
            fail();
        } catch (ExecutionException e) {
            assertThat(e.getCause(), is(EntityNotFoundRuntimeException.class));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsInTxUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models = delegate.get(tx, meta, Arrays.asList(key, key2));
        tx.rollback();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsyncInTxUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models =
            delegate.getAsync(tx, meta, Arrays.asList(key, key2)).get();
        tx.rollback();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsInTxUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models =
            delegate.get(tx, Hoge.class, Arrays.asList(key, key2));
        tx.rollback();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsyncInTxUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models =
            delegate.getAsync(tx, Hoge.class, Arrays.asList(key, key2)).get();
        tx.rollback();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsInTxUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        List<Aaa> models =
            delegate.get(tx, Aaa.class, Arrays.asList(key, key2));
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsyncInTxUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        List<Aaa> models =
            delegate.getAsync(tx, Aaa.class, Arrays.asList(key, key2)).get();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsInTxUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        List<Aaa> models =
            delegate.get(tx, AaaMeta.get(), Arrays.asList(key, key2));
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsyncInTxUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        List<Aaa> models =
            delegate
                .getAsync(tx, AaaMeta.get(), Arrays.asList(key, key2))
                .get();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
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
        delegate.get(tx, meta, Arrays.asList(key, key2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getModelsAsyncInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.getAsync(tx, meta, Arrays.asList(key, key2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models = delegate.get(tx, meta, key, key2);
        tx.rollback();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsyncInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models = delegate.getAsync(tx, meta, key, key2).get();
        tx.rollback();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsInTxForVarargsUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models = delegate.get(tx, Hoge.class, key, key2);
        tx.rollback();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsyncInTxForVarargsUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Hoge> models = delegate.getAsync(tx, Hoge.class, key, key2).get();
        tx.rollback();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsInTxForVarargsUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "aaa"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        List<Aaa> models = delegate.get(tx, Aaa.class, key, key2);
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsyncInTxForVarargsUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "aaa"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        List<Aaa> models = delegate.getAsync(tx, Aaa.class, key, key2).get();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsInTxForVarargsUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "aaa"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        List<Aaa> models = delegate.get(tx, AaaMeta.get(), key, key2);
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsyncInTxForVarargsUsingModelMeta()
            throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "aaa"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        List<Aaa> models =
            delegate.getAsync(tx, AaaMeta.get(), key, key2).get();
        assertThat(models, is(notNullValue()));
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(models.get(1).getClass().getName(), is(Bbb.class.getName()));
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
        delegate.get(tx, meta, key, key2);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getModelsAsyncInIllegalTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.getAsync(tx, meta, key, key2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapUsnigModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map = delegate.getAsMap(meta, Arrays.asList(key, key2));
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapAsyncUsnigModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map =
            delegate.getAsMapAsync(meta, Arrays.asList(key, key2)).get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapWithoutTxUsnigModelMeta() throws Exception {
        Key key = delegate.put(new Aaa());
        Key key2 = delegate.put(new Bbb());
        delegate.beginTransaction();
        Map<Key, Aaa> map =
            delegate.getAsMapWithoutTx(AaaMeta.get(), Arrays.asList(key, key2));
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapWithoutTxAsyncUsnigModelMeta() throws Exception {
        Key key = delegate.put(new Aaa());
        Key key2 = delegate.put(new Bbb());
        delegate.beginTransaction();
        Map<Key, Aaa> map =
            delegate.getAsMapWithoutTxAsync(
                AaaMeta.get(),
                Arrays.asList(key, key2)).get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map =
            delegate.getAsMap(Hoge.class, Arrays.asList(key, key2));
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapAsyncUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map =
            delegate.getAsMapAsync(Hoge.class, Arrays.asList(key, key2)).get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        Map<Key, Aaa> map =
            delegate.getAsMap(AaaMeta.get(), Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapAsyncUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        Map<Key, Aaa> map =
            delegate
                .getAsMapAsync(AaaMeta.get(), Arrays.asList(key, key2))
                .get();
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        Map<Key, Aaa> map =
            delegate.getAsMap(Aaa.class, Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapAsyncUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        Map<Key, Aaa> map =
            delegate.getAsMapAsync(Aaa.class, Arrays.asList(key, key2)).get();
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapForVarargsUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map = delegate.getAsMap(meta, key, key2);
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapAsyncForVarargsUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map = delegate.getAsMapAsync(meta, key, key2).get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapWithoutTxForVarargsUsingModelMeta()
            throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map = delegate.getAsMapWithoutTx(meta, key, key2);
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapWithoutTxAsyncForVarargsUsingModelMeta()
            throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map =
            delegate.getAsMapWithoutTxAsync(meta, key, key2).get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapForVarargsUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map = delegate.getAsMap(Hoge.class, key, key2);
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapAsyncForVarargsUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map =
            delegate.getAsMapAsync(Hoge.class, key, key2).get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapWithoutTxForVarargsUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map = delegate.getAsMapWithoutTx(Hoge.class, key, key2);
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapWithoutTxAsyncForVarargsUsingClass()
            throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Hoge> map =
            delegate.getAsMapWithoutTxAsync(Hoge.class, key, key2).get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapForVarargsUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        Map<Key, Aaa> map = delegate.getAsMap(AaaMeta.get(), key, key2);
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapAsyncForVarargsUsingModelMeta()
            throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        Map<Key, Aaa> map =
            delegate.getAsMapAsync(AaaMeta.get(), key, key2).get();
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapForVarargsUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        Map<Key, Aaa> map = delegate.getAsMap(Aaa.class, key, key2);
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapAsyncForVarargsUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Key key2 = delegate.put(new Bbb());
        Map<Key, Aaa> map = delegate.getAsMapAsync(Aaa.class, key, key2).get();
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapInTxUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map =
            delegate.getAsMap(tx, meta, Arrays.asList(key, key2));
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapAsyncInTxUsingModelMeta() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map =
            delegate.getAsMapAsync(tx, meta, Arrays.asList(key, key2)).get();
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapInTxUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map =
            delegate.getAsMap(tx, Hoge.class, Arrays.asList(key, key2));
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapAsyncInTxUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map =
            delegate
                .getAsMapAsync(tx, Hoge.class, Arrays.asList(key, key2))
                .get();
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapInTxUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        Map<Key, Aaa> map =
            delegate.getAsMap(tx, new AaaMeta(), Arrays.asList(key, key2));
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapAsyncInTxUsingModelMeta() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        Map<Key, Aaa> map =
            delegate
                .getAsMapAsync(tx, AaaMeta.get(), Arrays.asList(key, key2))
                .get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapInTxUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        Map<Key, Aaa> map =
            delegate.getAsMap(tx, Aaa.class, Arrays.asList(key, key2));
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapAsyncInTxUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        Map<Key, Aaa> map =
            delegate
                .getAsMapAsync(tx, Aaa.class, Arrays.asList(key, key2))
                .get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map = delegate.getAsMap(tx, meta, key, key2);
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapAsyncInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map = delegate.getAsMapAsync(tx, meta, key, key2).get();
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapInTxForVarargsUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map = delegate.getAsMap(tx, Hoge.class, key, key2);
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelsAsMapAsyncInTxForVarargsUsingClass() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Hoge> map =
            delegate.getAsMapAsync(tx, Hoge.class, key, key2).get();
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapInTxForVarargsUsingModelMeta()
            throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        Map<Key, Aaa> map = delegate.getAsMap(tx, AaaMeta.get(), key, key2);
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapAsyncInTxForVarargsUsingModelMeta()
            throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        Map<Key, Aaa> map =
            delegate.getAsMapAsync(tx, AaaMeta.get(), key, key2).get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapInTxForVarargsUsingClass() throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        Map<Key, Aaa> map = delegate.getAsMap(tx, Aaa.class, key, key2);
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubModelsAsMapAsyncInTxForVarargsUsingClass()
            throws Exception {
        Key key = delegate.put(new Bbb());
        Bbb bbb2 = new Bbb();
        bbb2.setKey(KeyFactory.createKey(key, BbbMeta.get().getKind(), "Hoge"));
        Key key2 = delegate.put(bbb2);
        Transaction tx = ds.beginTransaction();
        Map<Key, Aaa> map =
            delegate.getAsMapAsync(tx, Aaa.class, key, key2).get();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
        assertThat(map.get(key).getClass().getName(), is(Bbb.class.getName()));
        assertThat(map.get(key2).getClass().getName(), is(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntity() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Entity entity = delegate.get(key);
        assertThat(entity, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityAsync() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Entity entity = delegate.getAsync(key).get();
        assertThat(entity, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNull() throws Exception {
        assertThat(
            delegate.getOrNull(KeyFactory.createKey("Hoge", 1)),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullAsync() throws Exception {
        assertThat(delegate
            .getOrNullAsync(KeyFactory.createKey("Hoge", 1))
            .get(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityWithoutTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Entity entity = delegate.getWithoutTx(key);
        assertThat(entity, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityWithoutTxAsync() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Entity entity = delegate.getWithoutTxAsync(key).get();
        assertThat(entity, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullWithoutTx() throws Exception {
        assertThat(
            delegate.getOrNullWithoutTx(KeyFactory.createKey("Hoge", 1)),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullWithoutTxAsync() throws Exception {
        assertThat(delegate.getOrNullWithoutTxAsync(
            KeyFactory.createKey("Hoge", 1)).get(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Entity entity = delegate.get(tx, key);
        tx.rollback();
        assertThat(entity, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityAsyncInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        Entity entity = delegate.getAsync(tx, key).get();
        tx.rollback();
        assertThat(entity, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullInTx() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Transaction tx = ds.beginTransaction();
        Entity entity = delegate.getOrNull(tx, key);
        tx.rollback();
        assertThat(entity, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntityOrNullAsyncInTx() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Transaction tx = ds.beginTransaction();
        Entity entity = delegate.getOrNullAsync(tx, key).get();
        tx.rollback();
        assertThat(entity, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMap() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Entity> map = delegate.getAsMap(Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapAsync() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Entity> map =
            delegate.getAsMapAsync(Arrays.asList(key, key2)).get();
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapWithoutTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge2"));
        ds.beginTransaction();
        Map<Key, Entity> map =
            delegate.getAsMapWithoutTx(Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapWithoutTxAsync() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge2"));
        ds.beginTransaction();
        Map<Key, Entity> map =
            delegate.getAsMapWithoutTxAsync(Arrays.asList(key, key2)).get();
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
        Map<Key, Entity> map = delegate.getAsMap(key, key2);
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapAsyncForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        Map<Key, Entity> map = delegate.getAsMapAsync(key, key2).get();
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapWithoutTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge2"));
        ds.beginTransaction();
        Map<Key, Entity> map = delegate.getAsMapWithoutTx(key, key2);
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapWithoutTxAsyncForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge2"));
        ds.beginTransaction();
        Map<Key, Entity> map = delegate.getAsMapWithoutTxAsync(key, key2).get();
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
        Map<Key, Entity> map = delegate.getAsMap(tx, Arrays.asList(key, key2));
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapAsyncInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Entity> map =
            delegate.getAsMapAsync(tx, Arrays.asList(key, key2)).get();
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
        Map<Key, Entity> map = delegate.getAsMap(tx, key, key2);
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapAsyncInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        Map<Key, Entity> map = delegate.getAsMapAsync(tx, key, key2).get();
        tx.rollback();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapForZeroVarargs() throws Exception {
        Map<Key, Entity> map = delegate.getAsMap();
        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsMapAsyncForZeroVarargs() throws Exception {
        Map<Key, Entity> map = delegate.getAsMapAsync().get();
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
        List<Entity> list = delegate.get(Arrays.asList(key, key2));
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsync() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Entity> list = delegate.getAsync(Arrays.asList(key, key2)).get();
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesWithoutTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge2"));
        ds.beginTransaction();
        List<Entity> list = delegate.getWithoutTx(Arrays.asList(key, key2));
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesWithoutTxAsync() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge2"));
        ds.beginTransaction();
        List<Entity> list =
            delegate.getWithoutTxAsync(Arrays.asList(key, key2)).get();
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
        List<Entity> list = delegate.get(key, key2);
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsyncForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        List<Entity> list = delegate.getAsync(key, key2).get();
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesWithoutTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        ds.beginTransaction();
        List<Entity> list = delegate.getWithoutTx(key, key2);
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesWithoutTxAsyncForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        ds.beginTransaction();
        List<Entity> list = delegate.getWithoutTxAsync(key, key2).get();
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
        delegate.get(key, key2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsyncWhenEntityNotFound() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        Key key2 = KeyFactory.createKey("Hoge", 1);
        try {
            delegate.getAsync(key, key2).get();
            fail();
        } catch (ExecutionException e) {
            assertThat(e.getCause(), is(EntityNotFoundRuntimeException.class));
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Entity> list = delegate.get(tx, Arrays.asList(key, key2));
        tx.rollback();
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsyncInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Entity> list =
            delegate.getAsync(tx, Arrays.asList(key, key2)).get();
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
        delegate.get(tx, Arrays.asList(key, key2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getEntitiesAsyncInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.getAsync(tx, Arrays.asList(key, key2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Entity> list = delegate.get(tx, key, key2);
        tx.rollback();
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEntitiesAsyncInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        List<Entity> list = delegate.getAsync(tx, key, key2).get();
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
        delegate.get(tx, key, key2);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void getEntitiesAsyncInIllegalTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.getAsync(tx, key, key2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModel() throws Exception {
        Hoge hoge = new Hoge();
        assertThat(delegate.put(hoge), is(notNullValue()));
        assertThat(hoge.getKey(), is(notNullValue()));
        assertThat(hoge.getVersion(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModelWithoutTx() throws Exception {
        Hoge hoge = new Hoge();
        assertThat(delegate.putWithoutTx(hoge), is(notNullValue()));
        assertThat(hoge.getKey(), is(notNullValue()));
        assertThat(hoge.getVersion(), is(1L));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void putPolyModel() throws Exception {
        Bbb bbb = new Bbb();
        assertThat(delegate.put(bbb), is(notNullValue()));
        assertThat(bbb.getKey().getKind(), is("Aaa"));
        Entity entity = delegate.get(bbb.getKey());
        assertThat((List<String>) entity.getProperty(meta
            .getClassHierarchyListName()), hasItem(Bbb.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void putModelInTx() throws Exception {
        Hoge hoge = new Hoge();
        Transaction tx = delegate.beginTransaction();
        Key key = delegate.put(tx, hoge);
        tx.rollback();
        assertThat(key, is(not(nullValue())));
        assertThat(hoge.getKey(), is(not(nullValue())));
        assertThat(hoge.getVersion(), is(1L));
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void putModelInIllegalTx() throws Exception {
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.put(tx, new Hoge());
    }

    /**
     * @throws Exception
     */
    @Test
    public void putModels() throws Exception {
        List<Hoge> models = Arrays.asList(new Hoge(), new Hoge());
        List<Key> keys = delegate.put(models);
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
    public void putModelsWithoutTx() throws Exception {
        List<Hoge> models = Arrays.asList(new Hoge(), new Hoge());
        List<Key> keys = delegate.putWithoutTx(models);
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
    public void putModelsForVarargs() throws Exception {
        Hoge hoge = new Hoge();
        Hoge hoge2 = new Hoge();
        List<Key> keys = delegate.put(hoge, hoge2);
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
    public void putModelsWithoutTxForVarargs() throws Exception {
        Hoge hoge = new Hoge();
        Hoge hoge2 = new Hoge();
        List<Key> keys = delegate.putWithoutTx(hoge, hoge2);
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
        List<Key> keys = delegate.put(tx, models);
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
        delegate.put(tx, models);
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
        List<Key> keys = delegate.put(tx, hoge, hoge2);
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
        delegate.put(tx, hoge, hoge2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntity() throws Exception {
        assertThat(delegate.put(new Entity("Hoge")), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntityWithoutTx() throws Exception {
        assertThat(
            delegate.putWithoutTx(new Entity("Hoge")),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void putEntityInTx() throws Exception {
        Transaction tx = ds.beginTransaction();
        Key key = delegate.put(tx, new Entity("Hoge"));
        tx.rollback();
        assertThat(key, is(notNullValue()));
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void putEntityInIllegalTx() throws Exception {
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.put(tx, new Entity("Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntities() throws Exception {
        List<Key> keys =
            delegate.put(Arrays.asList(new Entity("Hoge"), new Entity("Hoge")));
        assertThat(keys, is(notNullValue()));
        assertThat(keys.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntitiesForVarargs() throws Exception {
        List<Key> keys = delegate.put(new Entity("Hoge"), new Entity("Hoge"));
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
        List<Key> keys = delegate.put(tx, Arrays.asList(entity, entity2));
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
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.put(tx, Arrays.asList(entity, entity2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putEntitiesInTxForVarargs() throws Exception {
        Entity entity = new Entity(KeyFactory.createKey("Hoge", 1));
        Entity entity2 =
            new Entity(KeyFactory.createKey(entity.getKey(), "Hoge", 1));
        Transaction tx = ds.beginTransaction();
        List<Key> keys = delegate.put(tx, entity, entity2);
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
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.put(tx, entity, entity2);
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        delegate.delete(key);
        ds.get(key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        delegate.delete(tx, key);
        tx.rollback();
        assertThat(ds.get(key), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void deleteInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.delete(tx, key);
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntities() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        delegate.delete(Arrays.asList(key, key2));
        assertThat(ds.get(Arrays.asList(key, key2)).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesWithoutTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge2"));
        ds.beginTransaction();
        delegate.deleteWithoutTx(Arrays.asList(key, key2));
        assertThat(
            delegate.getAsMapWithoutTx(Arrays.asList(key, key2)).size(),
            is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge"));
        delegate.delete(key, key2);
        assertThat(ds.get(Arrays.asList(key, key2)).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesWithoutTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge2"));
        ds.beginTransaction();
        delegate.deleteWithoutTx(key, key2);
        assertThat(
            delegate.getAsMapWithoutTx(Arrays.asList(key, key2)).size(),
            is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesInTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        delegate.delete(tx, Arrays.asList(key));
        tx.rollback();
        assertThat(ds.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void deleteEntitiesInIllegalTx() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.delete(tx, Arrays.asList(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteEntitiesInTxForVarargs() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        Key key2 = ds.put(new Entity("Hoge", key));
        Transaction tx = ds.beginTransaction();
        delegate.delete(tx, key, key2);
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
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        delegate.delete(tx, key, key2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAll() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Key childKey = KeyFactory.createKey(parentKey, "Child", 1);
        ds.put(new Entity(parentKey));
        ds.put(new Entity(childKey));
        delegate.deleteAll(parentKey);
        assertThat(tester.count("Parent"), is(0));
        assertThat(tester.count("Child"), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAllInTx() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Key childKey = KeyFactory.createKey(parentKey, "Child", 1);
        ds.put(new Entity(parentKey));
        ds.put(new Entity(childKey));
        Transaction tx = ds.beginTransaction();
        delegate.deleteAll(tx, parentKey);
        tx.rollback();
        assertThat(tester.count("Parent"), is(1));
        assertThat(tester.count("Child"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAllWithoutTx() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Key childKey = KeyFactory.createKey(parentKey, "Child", 1);
        ds.put(new Entity(parentKey));
        ds.put(new Entity(childKey));
        Transaction tx = ds.beginTransaction();
        delegate.deleteAllWithoutTx(parentKey);
        tx.rollback();
        assertThat(tester.count("Parent"), is(0));
        assertThat(tester.count("Child"), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelClass() throws Exception {
        assertThat(delegate.query(Hoge.class), is(ModelQuery.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelMeta() throws Exception {
        assertThat(delegate.query(meta), is(ModelQuery.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelClassAndAncestorKey() throws Exception {
        assertThat(
            delegate.query(Hoge.class, KeyFactory.createKey("Parent", 1)),
            is(ModelQuery.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingModelMetaAndAncestorKey() throws Exception {
        assertThat(
            delegate.query(meta, KeyFactory.createKey("Parent", 1)),
            is(ModelQuery.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingTxAndModelClassAndAncestorKey() throws Exception {
        ModelQuery<Hoge> query =
            delegate.query(ds.beginTransaction(), Hoge.class, KeyFactory
                .createKey("Parent", 1));
        assertThat(query, is(ModelQuery.class));
        assertThat(query.tx, is(notNullValue()));
        assertThat(query.txSet, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingTxAndModelMetaAndAncestorKey() throws Exception {
        ModelQuery<Hoge> query =
            delegate.query(ds.beginTransaction(), meta, KeyFactory.createKey(
                "Parent",
                1));
        assertThat(query, is(ModelQuery.class));
        assertThat(query.tx, is(notNullValue()));
        assertThat(query.txSet, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingKind() throws Exception {
        assertThat(delegate.query("Hoge"), is(EntityQuery.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingKindAndAncestorKey() throws Exception {
        assertThat(
            delegate.query("Hoge", KeyFactory.createKey("Parent", 1)),
            is(EntityQuery.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingTxAndKindAndAncestorKey() throws Exception {
        EntityQuery query =
            delegate.query(ds.beginTransaction(), "Hoge", KeyFactory.createKey(
                "Parent",
                1));
        assertThat(query, is(EntityQuery.class));
        assertThat(query.tx, is(notNullValue()));
        assertThat(query.txSet, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void kindlessQuery() throws Exception {
        assertThat(delegate.query(), is(KindlessQuery.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingAncestorKey() throws Exception {
        assertThat(
            delegate.query(KeyFactory.createKey("Parent", 1)),
            is(KindlessQuery.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryUsingTxAndAncestorKey() throws Exception {
        KindlessQuery query =
            delegate.query(ds.beginTransaction(), KeyFactory.createKey(
                "Parent",
                1));
        assertThat(query, is(KindlessQuery.class));
        assertThat(query.tx, is(notNullValue()));
        assertThat(query.txSet, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void filterInMemory() throws Exception {
        List<Hoge> list = new ArrayList<Hoge>();
        Hoge hoge = new Hoge();
        hoge.setMyStringList(Arrays.asList("aaa"));
        list.add(hoge);
        List<Hoge> filtered =
            delegate.filterInMemory(list, HogeMeta.get().myStringList
                .startsWith("aaa"));
        assertThat(filtered.size(), is(1));
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

        List<Hoge> sorted = delegate.sortInMemory(list, meta.myInteger.desc);
        assertThat(sorted.size(), is(3));
        assertThat(sorted.get(0).getMyInteger(), is(3));
        assertThat(sorted.get(1).getMyInteger(), is(2));
        assertThat(sorted.get(2).getMyInteger(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setGlobalCipherKey() throws Exception {
        delegate.setGlobalCipherKey("1234567890ABCDEF");
        Hoge hoge = new Hoge();
        hoge.setMyCipherString("hoge.");
        hoge.setMyCipherLobString("hogehoge.");
        hoge.setMyCipherText(new Text("hogehogehoge."));
        Key key = delegate.put(hoge);
        assertThat(delegate.get(meta, key).getMyCipherString(), is("hoge."));
        assertThat(
            delegate.get(meta, key).getMyCipherLobString(),
            is("hogehoge."));
        assertThat(
            delegate.get(meta, key).getMyCipherText().getValue(),
            is("hogehogehoge."));
        assertThat(
            (String) delegate.get(key).getProperty("myCipherString"),
            not("hoge."));
        assertThat(((Text) delegate.get(key).getProperty("myCipherLobString"))
            .getValue(), not("hogehoge."));
        assertThat(((Text) delegate.get(key).getProperty("myCipherText"))
            .getValue(), not("hogehogehoge."));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setLimitedCipherKey() throws Exception {
        delegate.setLimitedCipherKey("1234567890ABCDEF");
        Hoge hoge = new Hoge();
        hoge.setMyCipherString("hoge.");
        hoge.setMyCipherLobString("hogehoge.");
        hoge.setMyCipherText(new Text("hogehogehoge."));
        Key key = delegate.put(hoge);
        assertThat(delegate.get(meta, key).getMyCipherString(), is("hoge."));
        assertThat(
            delegate.get(meta, key).getMyCipherLobString(),
            is("hogehoge."));
        assertThat(
            delegate.get(meta, key).getMyCipherText().getValue(),
            is("hogehogehoge."));
        assertThat(
            (String) delegate.get(key).getProperty("myCipherString"),
            not("hoge."));
        assertThat(((Text) delegate.get(key).getProperty("myCipherLobString"))
            .getValue(), not("hogehoge."));
        assertThat(((Text) delegate.get(key).getProperty("myCipherText"))
            .getValue(), not("hogehogehoge."));
    }
}