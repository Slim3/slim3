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

import java.lang.reflect.Field;
import java.util.List;

import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class AbstQueryTest extends DatastoreTestCase {

    /**
     * @throws Exception
     */
    public void testConstructorUsingKind() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertEquals("Hoge", q.query.getKind());
        assertFalse(q.txSet);
    }

    /**
     * @throws Exception
     */
    public void testConstructorUsingKindAndAncestorKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        MyQuery q = new MyQuery("Hoge", key);
        assertEquals("Hoge", q.query.getKind());
        assertEquals(key, q.query.getAncestor());
        assertFalse(q.txSet);
    }

    /**
     * @throws Exception
     */
    public void testConstructorUsingAncestorKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        MyQuery q = new MyQuery(key);
        assertEquals(key, q.query.getAncestor());
        assertFalse(q.txSet);
    }

    /**
     * @throws Exception
     */
    public void testTx() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertEquals(q, q.tx(ds.beginTransaction()));
        assertNotNull(q.tx);
        assertTrue(q.txSet);
    }

    /**
     * @throws Exception
     */
    public void testNoTx() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertNull(q.tx);
        assertFalse(q.txSet);
    }

    /**
     * @throws Exception
     */
    public void testNullTx() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertEquals(q, q.tx(null));
        assertNull(q.tx);
        assertTrue(q.txSet);
    }

    /**
     * @throws Exception
     */
    public void testIllegalTx() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        try {
            Transaction tx = ds.beginTransaction();
            tx.rollback();
            q.tx(tx);
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testAsEntityList() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        List<Entity> list = query.asEntityList();
        assertEquals(1, list.size());
    }

    /**
     * @throws Exception
     */
    public void testAsSingleEntity() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        Entity entity = query.asSingleEntity();
        assertNotNull(entity);
    }

    /**
     * @throws Exception
     */
    public void testAsKeyList() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        List<Key> list = query.asKeyList();
        assertEquals(1, list.size());
        assertEquals(key, list.get(0));
    }

    /**
     * @throws Exception
     */
    public void testAsIterableEntities() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        boolean found = false;
        for (Entity entity : query.asIterableEntities()) {
            found = true;
            assertEquals("Hoge", entity.getKind());
        }
        assertTrue(found);
    }

    /**
     * @throws Exception
     */
    public void testCount() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        assertEquals(1, query.count());
    }

    /**
     * @throws Exception
     */
    public void testCountQuickly() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        assertEquals(1, query.count());
    }

    /**
     * @throws Exception
     */
    public void testOffset() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertSame(q, q.offset(10));
        Field f = FetchOptions.class.getDeclaredField("offset");
        f.setAccessible(true);
        assertEquals(10, f.getInt(q.fetchOptions));
    }

    /**
     * @throws Exception
     */
    public void testLimit() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertSame(q, q.limit(100));
        Field f = FetchOptions.class.getDeclaredField("limit");
        f.setAccessible(true);
        assertEquals(100, f.getInt(q.fetchOptions));
    }

    /**
     * @throws Exception
     */
    public void testPrefetchSize() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertSame(q, q.prefetchSize(15));
        Field f = FetchOptions.class.getDeclaredField("prefetchSize");
        f.setAccessible(true);
        assertEquals(15, f.getInt(q.fetchOptions));
    }

    /**
     * @throws Exception
     */
    public void testChunkSize() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertSame(q, q.chunkSize(20));
        Field f = FetchOptions.class.getDeclaredField("chunkSize");
        f.setAccessible(true);
        assertEquals(20, f.getInt(q.fetchOptions));
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