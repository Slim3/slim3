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

import org.slim3.datastore.meta.BarMeta;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class ModelQueryTest extends DatastoreTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    public void testConstructor() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertEquals(meta, query.modelMeta);
    }

    /**
     * @throws Exception
     */
    public void testFilter() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertSame(query, query.filter(meta.myString.equal("aaa")));
        assertEquals(1, query.query.getFilterPredicates().size());
    }

    /**
     * @throws Exception
     */
    public void testFilterForIllegalArgument() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        try {
            query.filter(new BarMeta().key.equal(null));
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testSort() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertSame(query, query.sort(meta.myString.asc));
        assertEquals(1, query.query.getSortPredicates().size());
    }

    /**
     * @throws Exception
     */
    public void testAsList() throws Exception {
        ds.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        List<Hoge> list = query.asList();
        assertEquals(1, list.size());
    }

    /**
     * @throws Exception
     */
    public void testAsSingle() throws Exception {
        ds.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        Hoge hoge = query.asSingle();
        assertNotNull(hoge);
    }

    /**
     * @throws Exception
     */
    public void testAsKeyList() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        List<Key> list = query.asKeyList();
        assertEquals(1, list.size());
        assertEquals(key, list.get(0));
    }

    /**
     * @throws Exception
     */
    public void testAncestor() throws Exception {
        Key key = ds.put(new Entity("Parent"));
        ds.put(new Entity("Hoge", key));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta, key);
        List<Hoge> list = query.asList();
        assertEquals(1, list.size());
    }

    /**
     * @throws Exception
     */
    public void testMin() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        Hoge hoge3 = new Hoge();
        Datastore.put(hoge, hoge2, hoge3);
        assertEquals(Integer.valueOf(1), Datastore.query(meta).min(
            meta.myInteger));
    }

    /**
     * @throws Exception
     */
    public void testMax() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        Hoge hoge3 = new Hoge();
        Datastore.put(hoge, hoge2, hoge3);
        assertEquals(Integer.valueOf(2), Datastore.query(meta).max(
            meta.myInteger));
    }
}