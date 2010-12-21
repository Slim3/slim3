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
import org.slim3.datastore.meta.AaaMeta;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;
import org.slim3.util.FutureUtil;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class AsyncDatastoreDelegateTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    private AsyncDatastoreService ds =
        DatastoreServiceFactory.getAsyncDatastoreService();

    private AsyncDatastoreDelegate delegate = new AsyncDatastoreDelegate();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CipherFactory.getFactory().setGlobalKey("xxxxxxxxxxxxxxxx");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        CipherFactory.getFactory().clearGlobalKey();
        System.clearProperty(AsyncDatastoreDelegate.DEADLINE);
    }

    /**
     * @throws Exception
     */
    @Test
    public void deadline() throws Exception {
        Double deadline = 1.0;
        AsyncDatastoreDelegate del = new AsyncDatastoreDelegate(deadline);
        assertThat(del.getDeadline(), is(deadline));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deadlineForDefaultConstructor() throws Exception {
        AsyncDatastoreDelegate del = new AsyncDatastoreDelegate();
        assertThat(del.getDeadline(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deadlineForSystemProperty() throws Exception {
        Double deadline = 1.0;
        System
            .setProperty(AsyncDatastoreDelegate.DEADLINE, deadline.toString());
        AsyncDatastoreDelegate del = new AsyncDatastoreDelegate();
        assertThat(del.getDeadline(), is(deadline));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSetUp() throws Exception {
        Double deadline = 1.0;
        AsyncDatastoreDelegate del = new AsyncDatastoreDelegate(deadline);
        assertThat(del.getAsyncDatastoreService(), is(notNullValue()));
        assertThat(del.dsConfig, is(notNullValue()));
        assertThat(del.dsConfig.getDeadline(), is(deadline));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSetUpForDefaultConstructor() throws Exception {
        AsyncDatastoreDelegate del = new AsyncDatastoreDelegate();
        assertThat(del.getAsyncDatastoreService(), is(notNullValue()));
        assertThat(del.dsConfig, is(notNullValue()));
        assertThat(del.dsConfig.getDeadline(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSetUpForSystemProperty() throws Exception {
        Double deadline = 1.0;
        System
            .setProperty(AsyncDatastoreDelegate.DEADLINE, deadline.toString());
        AsyncDatastoreDelegate del = new AsyncDatastoreDelegate();
        assertThat(del.getAsyncDatastoreService(), is(notNullValue()));
        assertThat(del.dsConfig, is(notNullValue()));
        assertThat(del.dsConfig.getDeadline(), is(deadline));
    }

    /**
     * @throws Exception
     */
    @Test
    public void beginTransactionAsync() throws Exception {
        assertThat(delegate.beginTransactionAsync(), is(notNullValue()));
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
    public void getAsync() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Hoge"));
        Entity entity = delegate.getAsync((Transaction) null, key).get();
        assertThat(entity, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = EntityNotFoundRuntimeException.class)
    public void getAsyncForNoEntityFound() throws Exception {
        FutureUtil.getQuietly(delegate.getAsync((Transaction) null, KeyFactory
            .createKey("xxx", 1)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsyncUsingModelMeta() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Hoge"));
        Hoge model = delegate.getAsync((Transaction) null, meta, key).get();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getAsyncForIllegalKey() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("xxx"));
        FutureUtil.getQuietly(delegate.getAsync((Transaction) null, meta, key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOrNullAsync() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Hoge"));
        Entity entity = delegate.getOrNullAsync((Transaction) null, key).get();
        assertThat(entity, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOrNullAsyncForNoEntityFound() throws Exception {
        Entity entity =
            delegate.getOrNullAsync(
                (Transaction) null,
                KeyFactory.createKey("xxx", 1)).get();
        assertThat(entity, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOrNullAsyncUsingModelMeta() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Hoge"));
        Hoge model =
            delegate.getOrNullAsync((Transaction) null, meta, key).get();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getOrNullAsyncUsingModelMetaForNoEntityFound() throws Exception {
        Hoge model =
            delegate.getOrNullAsync(
                (Transaction) null,
                meta,
                KeyFactory.createKey("Hoge", "xxx")).get();
        assertThat(model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getOrNullAsyncForIllegalKey() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("xxx"));
        FutureUtil.getQuietly(delegate.getAsync((Transaction) null, meta, key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsyncUsingModelMetaWithVersion() throws Exception {
        Key key =
            DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(
                ds,
                new Hoge()));
        Hoge model = delegate.getAsync((Transaction) null, meta, key, 1L).get();
        assertThat(model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConcurrentModificationException.class)
    public void getAsyncUsingModelMetaWithIllegalVersion() throws Exception {
        Key key =
            DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(
                ds,
                new Hoge()));
        FutureUtil.getQuietly(delegate.getAsync(
            (Transaction) null,
            meta,
            key,
            0L));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getAsyncUsingModelMetaForIllegalKey() throws Exception {
        Key key =
            DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(
                ds,
                new Hoge()));
        FutureUtil.getQuietly(delegate.getAsync((Transaction) null, AaaMeta
            .get(), key, 1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsyncForEntities() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Hoge"));
        List<Entity> entities =
            delegate.getAsync((Transaction) null, Arrays.asList(key)).get();
        assertThat(entities.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsyncUsingModelMetaForModels() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Hoge"));
        List<Hoge> models =
            delegate
                .getAsync((Transaction) null, meta, Arrays.asList(key))
                .get();
        assertThat(models.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsMapAsyncForModels() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Hoge"));
        Map<Key, Hoge> map =
            delegate
                .getAsMapAsync((Transaction) null, meta, Arrays.asList(key))
                .get();
        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAsync() throws Exception {
        Key key =
            delegate.putAsync((Transaction) null, new Entity("Hoge")).get();
        assertThat(key, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAsyncForModel() throws Exception {
        Key key = delegate.putAsync((Transaction) null, new Hoge()).get();
        assertThat(key, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAsyncForModels() throws Exception {
        List<Key> keys =
            delegate.putAsync(
                (Transaction) null,
                Arrays.asList(new Entity("Hoge"), new Hoge())).get();
        assertThat(keys.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAllAsync() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        Key childKey = KeyFactory.createKey(parentKey, "Child", 1);
        DatastoreUtil.put(ds, null, new Entity(parentKey));
        DatastoreUtil.put(ds, null, new Entity(childKey));
        delegate.deleteAllAsync(parentKey).get();
        assertThat(tester.count("Parent"), is(0));
        assertThat(tester.count("Child"), is(0));
    }
}