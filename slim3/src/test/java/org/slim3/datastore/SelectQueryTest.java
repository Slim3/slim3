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

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class SelectQueryTest extends DatastoreTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    public void testConstructor() throws Exception {
        SelectQuery<Hoge> query = new SelectQuery<Hoge>(meta);
        assertEquals(meta, query.modelMeta);
    }

    /**
     * @throws Exception
     */
    public void testFilter() throws Exception {
        SelectQuery<Hoge> query = new SelectQuery<Hoge>(meta);
        assertSame(query, query.filter(meta.myString.equal("aaa")));
        assertEquals(1, query.filterCriteria.length);
    }

    /**
     * @throws Exception
     */
    public void testAsList() throws Exception {
        ds.put(new Entity("Hoge"));
        SelectQuery<Hoge> query = new SelectQuery<Hoge>(meta);
        List<Hoge> list = query.asList();
        assertEquals(1, list.size());
    }

    /**
     * @throws Exception
     */
    public void testAsSingle() throws Exception {
        ds.put(new Entity("Hoge"));
        SelectQuery<Hoge> query = new SelectQuery<Hoge>(meta);
        Hoge hoge = query.asSingle();
        assertNotNull(hoge);
    }

    /**
     * @throws Exception
     */
    public void testAsKeyList() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        SelectQuery<Hoge> query = new SelectQuery<Hoge>(meta);
        List<Key> list = query.asKeyList();
        assertEquals(1, list.size());
        assertEquals(key, list.get(0));
    }
}