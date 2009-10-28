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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class AbstQueryTest extends LocalServiceTestCase {

    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingKind() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.query.getKind(), is("Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingKindAndAncestorKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        MyQuery q = new MyQuery("Hoge", key);
        assertThat(q.query.getKind(), is("Hoge"));
        assertThat(q.query.getAncestor(), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingAncestorKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        MyQuery q = new MyQuery(key);
        assertThat(q.query.getAncestor(), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void tx() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertEquals(q, q.tx(ds.beginTransaction()));
        assertNotNull(q.tx);
        assertTrue(q.txSet);
    }

    /**
     * @throws Exception
     */
    @Test
    public void noTx() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.tx, is(nullValue()));
        assertThat(q.txSet, is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void nullTx() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        q.tx(null);
        assertThat(q.tx, is(nullValue()));
        assertThat(q.txSet, is(true));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void illegalTx() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        q.tx(tx);
    }

    /**
     * @throws Exception
     */
    @Test
    public void asEntityList() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        List<Entity> list = query.asEntityList();
        assertThat(list.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleEntity() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        assertThat(query.asSingleEntity(), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyList() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        assertThat(query.asKeyList(), is(Arrays.asList(key)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asIterableEntities() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        boolean found = false;
        for (Entity entity : query.asIterableEntities()) {
            found = true;
            assertThat(entity.getKind(), is("Hoge"));
        }
        assertThat(found, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void count() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        assertThat(query.count(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void countQuickly() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        assertThat(query.countQuickly(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void min() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("age", 10);
        ds.put(entity);
        Entity entity2 = new Entity("Hoge");
        entity2.setProperty("age", 20);
        ds.put(entity2);
        Entity entity3 = new Entity("Hoge");
        entity3.setProperty("age", null);
        ds.put(entity3);
        MyQuery query = new MyQuery("Hoge");
        assertThat((Long) query.min("age"), is(10L));
        assertThat(query.max("name"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void max() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("age", 10);
        ds.put(entity);
        Entity entity2 = new Entity("Hoge");
        entity2.setProperty("age", 20);
        ds.put(entity2);
        MyQuery query = new MyQuery("Hoge");
        assertThat((Long) query.max("age"), is(20L));
        assertThat(query.max("name"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void offset() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.offset(10), is(sameInstance(q)));
        Field f = FetchOptions.class.getDeclaredField("offset");
        f.setAccessible(true);
        assertThat((Integer) f.get(q.fetchOptions), is(10));
    }

    /**
     * @throws Exception
     */
    @Test
    public void limit() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.limit(100), is(sameInstance(q)));
        Field f = FetchOptions.class.getDeclaredField("limit");
        f.setAccessible(true);
        assertThat((Integer) f.get(q.fetchOptions), is(100));
    }

    /**
     * @throws Exception
     */
    @Test
    public void prefetchSize() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.prefetchSize(15), is(sameInstance(q)));
        Field f = FetchOptions.class.getDeclaredField("prefetchSize");
        f.setAccessible(true);
        assertThat((Integer) f.get(q.fetchOptions), is(15));
    }

    /**
     * @throws Exception
     */
    @Test
    public void chunkSize() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.chunkSize(20), is(sameInstance(q)));
        Field f = FetchOptions.class.getDeclaredField("chunkSize");
        f.setAccessible(true);
        assertThat((Integer) f.get(q.fetchOptions), is(20));
    }

    private static class MyQuery extends AbstractQuery<MyQuery> {

        /**
         * @param ancestorKey
         * @throws NullPointerException
         */
        public MyQuery(Key ancestorKey) throws NullPointerException {
            super(ancestorKey);
        }

        /**
         * @param kind
         * @param ancestorKey
         * @throws NullPointerException
         */
        public MyQuery(String kind, Key ancestorKey)
                throws NullPointerException {
            super(kind, ancestorKey);
        }

        /**
         * @param kind
         * @throws NullPointerException
         */
        public MyQuery(String kind) throws NullPointerException {
            super(kind);
        }

    }
}