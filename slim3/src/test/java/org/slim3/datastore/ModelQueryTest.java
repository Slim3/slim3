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
import java.util.List;

import org.junit.Test;
import org.slim3.datastore.meta.AaaMeta;
import org.slim3.datastore.meta.BbbMeta;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Aaa;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;

/**
 * @author higa
 * 
 */
public class ModelQueryTest extends LocalServiceTestCase {

    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    private HogeMeta meta = new HogeMeta();

    private AaaMeta aaaMeta = new AaaMeta();

    private BbbMeta bbbMeta = new BbbMeta();

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void constructor() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertThat(query.modelMeta, is(sameInstance((ModelMeta) meta)));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void constructorUsingModelMetaAndAncestorKey() throws Exception {
        Key ancestorKey = KeyFactory.createKey("Ancestor", 1);
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta, ancestorKey);
        assertThat(query.modelMeta, is(sameInstance((ModelMeta) meta)));
        assertThat(query.query.getAncestor(), is(ancestorKey));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void constructorUsingTxAndModelMetaAndAncestorKey() throws Exception {
        Key ancestorKey = KeyFactory.createKey("Ancestor", 1);
        ModelQuery<Hoge> query =
            new ModelQuery<Hoge>(ds.beginTransaction(), meta, ancestorKey);
        assertThat(query.modelMeta, is(sameInstance((ModelMeta) meta)));
        assertThat(query.query.getAncestor(), is(ancestorKey));
        assertThat(query.tx, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void filter() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertThat(query, is(sameInstance(query.filter(meta.myString
            .equal("aaa")))));
        assertThat(query.query.getFilterPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sort() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertThat(query.sort(meta.myString.asc), is(sameInstance(query)));
        assertThat(query.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asList() throws Exception {
        Datastore.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        List<Hoge> list = query.asList();
        assertThat(list.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        Datastore.put(hoge);
        Hoge hoge2 = new Hoge();
        hoge2.setMyString("bbb");
        Datastore.put(hoge2);
        List<Hoge> list =
            new ModelQuery<Hoge>(meta).filterInMemory(
                meta.myString.equal("aaa")).asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyString(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndSortInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        Datastore.put(hoge);
        Hoge hoge2 = new Hoge();
        hoge2.setMyString("bbb");
        Datastore.put(hoge2);
        List<Hoge> list =
            new ModelQuery<Hoge>(meta)
                .sortInMemory(meta.myString.desc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyString(), is("bbb"));
        assertThat(list.get(1).getMyString(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListForPolyModel() throws Exception {
        Datastore.put(new Aaa());
        Datastore.put(new Bbb());

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(aaaMeta);
        List<Aaa> list = query.asList();
        assertThat(list.size(), is(2));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(bbbMeta);
        List<Bbb> list2 = query2.asList();
        assertThat(list2.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingle() throws Exception {
        Datastore.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        Hoge hoge = query.asSingle();
        assertThat(hoge, is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test(expected = TooManyResultsException.class)
    public void asSingleWhenTooManyResults() throws Exception {
        Datastore.put(new Entity("Hoge"));
        Datastore.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        query.asSingle();
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleAndFilterInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        Datastore.put(hoge);
        Hoge hoge2 = new Hoge();
        hoge2.setMyString("bbb");
        Datastore.put(hoge2);
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        Hoge ret = query.filterInMemory(meta.myString.equal("aaa")).asSingle();
        assertThat(ret, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleAndSortInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        Datastore.put(hoge);
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        Hoge ret = query.sortInMemory(meta.myString.asc).asSingle();
        assertThat(ret, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleWhenNoEntity() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        Hoge ret = query.filterInMemory(meta.myString.equal("aaa")).asSingle();
        assertThat(ret, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyList() throws Exception {
        Key key = Datastore.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertThat(query.asKeyList(), is(Arrays.asList(key)));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void asKeyListAndFilterInMemory() throws Exception {
        Datastore.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        query.filterInMemory(meta.myString.equal("aaa")).asKeyList();
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyListAndAscSortInMemory() throws Exception {
        Key key = Datastore.createKey(meta, 1);
        Key key2 = Datastore.createKey(meta, 2);
        Datastore.put(new Entity(key2));
        Datastore.put(new Entity(key));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        List<Key> keys = query.sortInMemory(meta.key.asc).asKeyList();
        assertThat(keys.size(), is(2));
        assertThat(keys.get(0), is(key));
        assertThat(keys.get(1), is(key2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyListAndDescSortInMemory() throws Exception {
        Key key = Datastore.createKey(meta, 1);
        Key key2 = Datastore.createKey(meta, 2);
        Datastore.put(new Entity(key));
        Datastore.put(new Entity(key2));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        List<Key> keys = query.sortInMemory(meta.key.desc).asKeyList();
        assertThat(keys.size(), is(2));
        assertThat(keys.get(0), is(key2));
        assertThat(keys.get(1), is(key));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void asKeyListAndSortInMemory() throws Exception {
        Datastore.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        query.sortInMemory(meta.myString.asc).asKeyList();
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyListForPolyModel() throws Exception {
        Datastore.put(new Aaa());
        Datastore.put(new Bbb());

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(aaaMeta);
        List<Key> list = query.asKeyList();
        assertThat(list.size(), is(2));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(bbbMeta);
        List<Key> list2 = query2.asKeyList();
        assertThat(list2.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void ancestor() throws Exception {
        Key key = Datastore.put(new Entity("Parent"));
        Datastore.put(new Entity("Hoge", key));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta, key);
        List<Hoge> list = query.asList();
        assertThat(list.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void min() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        Hoge hoge3 = new Hoge();
        Datastore.put(hoge, hoge2, hoge3);
        assertThat(Datastore.query(meta).min(meta.myInteger), is(1));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void minAndFilterInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        Hoge hoge3 = new Hoge();
        Datastore.put(hoge, hoge2, hoge3);
        Datastore.query(meta).filterInMemory(meta.myInteger.equal(1)).min(
            meta.myInteger);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void minAndSortInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        Hoge hoge3 = new Hoge();
        Datastore.put(hoge, hoge2, hoge3);
        Datastore.query(meta).sortInMemory(meta.myInteger.asc).min(
            meta.myInteger);
    }

    /**
     * @throws Exception
     */
    @Test
    public void minForPolyModel() throws Exception {
        Aaa aaa = new Aaa();
        aaa.setVersion(1L);
        Bbb bbb = new Bbb();
        bbb.setVersion(2L);
        Datastore.put(aaa);
        Datastore.put(bbb);

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(aaaMeta);
        assertThat(query.min(aaaMeta.version), is(2L));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(bbbMeta);
        assertThat(query2.min(bbbMeta.version), is(3L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void max() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        Hoge hoge3 = new Hoge();
        Datastore.put(hoge, hoge2, hoge3);
        assertThat(Datastore.query(meta).max(meta.myInteger), is(2));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void maxAndFilerInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        Hoge hoge3 = new Hoge();
        Datastore.put(hoge, hoge2, hoge3);
        Datastore.query(meta).filterInMemory(meta.myInteger.equal(1)).max(
            meta.myInteger);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void maxAndSortInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        Hoge hoge3 = new Hoge();
        Datastore.put(hoge, hoge2, hoge3);
        Datastore.query(meta).sortInMemory(meta.myInteger.asc).max(
            meta.myInteger);
    }

    /**
     * @throws Exception
     */
    @Test
    public void maxForPolyModel() throws Exception {
        Aaa aaa = new Aaa();
        aaa.setVersion(2L);
        Bbb bbb = new Bbb();
        bbb.setVersion(1L);
        Datastore.put(aaa);
        Datastore.put(bbb);

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(aaaMeta);
        assertThat(query.max(aaaMeta.version), is(3L));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(bbbMeta);
        assertThat(query2.max(bbbMeta.version), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void countAndFilterInMemory() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        query.filterInMemory(meta.myString.equal("aaa")).count();
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void countAndSortInMemory() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        query.sortInMemory(meta.myString.asc).count();
    }

    /**
     * @throws Exception
     */
    @Test
    public void countForPolyModel() throws Exception {
        Datastore.put(new Aaa());
        Datastore.put(new Bbb());

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(aaaMeta);
        assertThat(query.count(), is(2));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(bbbMeta);
        assertThat(query2.count(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void countQuicklyAndFilterInMemory() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        query.filterInMemory(meta.myString.equal("aaa")).countQuickly();
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void countQuicklyAndSortInMemory() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        query.sortInMemory(meta.myString.asc).countQuickly();
    }

    /**
     * @throws Exception
     */
    @Test
    public void countQuicklyForPolyModel() throws Exception {
        Datastore.put(new Aaa());
        Datastore.put(new Bbb());

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(aaaMeta);
        assertThat(query.countQuickly(), is(2));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(bbbMeta);
        assertThat(query2.countQuickly(), is(1));
    }
}