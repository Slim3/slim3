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

import java.util.List;

import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
    }

    /**
     * @throws Exception
     */
    public void testConstructorUsingKindAndAncestorKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        MyQuery q = new MyQuery("Hoge", key);
        assertEquals("Hoge", q.query.getKind());
        assertEquals(key, q.query.getAncestor());
    }

    /**
     * @throws Exception
     */
    public void testConstructorUsingAncestorKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        MyQuery q = new MyQuery(key);
        assertEquals(key, q.query.getAncestor());
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

    private static class MyQuery extends AbstractQuery {

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