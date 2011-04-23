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
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.slim3.datastore.meta.AaaMeta;
import org.slim3.datastore.meta.BbbMeta;
import org.slim3.datastore.meta.CccMeta;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Aaa;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Ccc;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;

/**
 * @author higa
 * 
 */
public class ModelQueryTest extends AppEngineTestCase {

    private AsyncDatastoreService ds =
        DatastoreServiceFactory.getAsyncDatastoreService();

    private HogeMeta meta = new HogeMeta();

    private AaaMeta aaaMeta = new AaaMeta();

    private BbbMeta bbbMeta = new BbbMeta();

    private CccMeta cccMeta = new CccMeta();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CipherFactory.getFactory().setGlobalKey("xxxxxxxxxxxxxxxx");
    }

    @Override
    public void tearDown() throws Exception {
        CipherFactory.getFactory().clearGlobalKey();
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void constructor() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        assertThat(query.modelMeta, is(sameInstance((ModelMeta) meta)));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void constructorUsingModelMetaAndAncestorKey() throws Exception {
        Key ancestorKey = KeyFactory.createKey("Ancestor", 1);
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta, ancestorKey);
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
            new ModelQuery<Hoge>(
                ds,
                DatastoreUtil.beginTransaction(ds),
                meta,
                ancestorKey);
        assertThat(query.modelMeta, is(sameInstance((ModelMeta) meta)));
        assertThat(query.query.getAncestor(), is(ancestorKey));
        assertThat(query.tx, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void filter() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        assertThat(query, is(sameInstance(query.filter(meta.myString
            .equal("aaa")))));
        assertThat(query.query.getFilterPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sort() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        assertThat(query.sort(meta.myString.asc), is(sameInstance(query)));
        assertThat(query.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asList() throws Exception {
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
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
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        Hoge hoge2 = new Hoge();
        hoge2.setMyString("bbb");
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge2));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
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
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        Hoge hoge2 = new Hoge();
        hoge2.setMyString("bbb");
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge2));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
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
        DatastoreUtil.put(ds, null, AaaMeta.get().modelToEntity(new Aaa()));
        DatastoreUtil.put(ds, null, BbbMeta.get().modelToEntity(new Bbb()));
        DatastoreUtil.put(ds, null, CccMeta.get().modelToEntity(new Ccc()));

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(ds, aaaMeta);
        List<Aaa> list = query.asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getClass().getName(), is(Aaa.class.getName()));
        assertThat(list.get(1).getClass().getName(), is(Bbb.class.getName()));
        assertThat(list.get(2).getClass().getName(), is(Ccc.class.getName()));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(ds, bbbMeta);
        List<Bbb> list2 = query2.asList();
        assertThat(list2.size(), is(2));
        assertThat(list2.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(list2.get(1).getClass().getName(), is(Ccc.class.getName()));

        ModelQuery<Ccc> query3 = new ModelQuery<Ccc>(ds, cccMeta);
        List<Ccc> list3 = query3.asList();
        assertThat(list3.size(), is(1));
        assertThat(list3.get(0).getClass().getName(), is(Ccc.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultList() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        hoge = new Hoge();
        hoge.setMyInteger(2);
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        hoge = new Hoge();
        hoge.setMyInteger(3);
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        S3QueryResultList<Hoge> list =
            query.limit(1).sort(meta.myInteger.asc).asQueryResultList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyInteger(), is(1));
        assertThat(list.hasNext(), is(true));
        assertThat(list.getEncodedCursor(), is(notNullValue()));
        assertThat(list.getEncodedFilters(), is(notNullValue()));
        assertThat(list.getEncodedSorts(), is(notNullValue()));

        ModelQuery<Hoge> query2 = new ModelQuery<Hoge>(ds, meta);
        S3QueryResultList<Hoge> list2 =
            query2
                .limit(1)
                .encodedStartCursor(list.getEncodedCursor())
                .encodedFilters(list.getEncodedFilters())
                .encodedSorts(list.getEncodedSorts())
                .asQueryResultList();
        assertThat(list2.size(), is(1));
        assertThat(list2.get(0).getMyInteger(), is(2));
        assertThat(list2.hasNext(), is(true));
        assertThat(list2.getEncodedCursor(), is(notNullValue()));
        assertThat(list2.getEncodedFilters(), is(notNullValue()));
        assertThat(list2.getEncodedSorts(), is(notNullValue()));

        ModelQuery<Hoge> query3 = new ModelQuery<Hoge>(ds, meta);
        S3QueryResultList<Hoge> list3 =
            query3
                .limit(1)
                .encodedStartCursor(list2.getEncodedCursor())
                .encodedFilters(list2.getEncodedFilters())
                .encodedSorts(list2.getEncodedSorts())
                .asQueryResultList();
        assertThat(list3.size(), is(1));
        assertThat(list3.get(0).getMyInteger(), is(3));
        assertThat(list3.hasNext(), is(false));
        assertThat(list3.getEncodedCursor(), is(notNullValue()));
        assertThat(list3.getEncodedFilters(), is(notNullValue()));
        assertThat(list3.getEncodedSorts(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultListWithNoLimit() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        hoge = new Hoge();
        hoge.setMyInteger(2);
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        hoge = new Hoge();
        hoge.setMyInteger(3);
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        S3QueryResultList<Hoge> list =
            query.sort(meta.myInteger.asc).asQueryResultList();
        assertThat(list.size(), is(3));
        assertThat(list.hasNext(), is(false));
        assertThat(list.getEncodedCursor(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void asQueryResultListWithFilterInMemory() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        query.filterInMemory(meta.myInteger.equal(1)).asQueryResultList();
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void asQueryResultListWithSortInMemory() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        query.sortInMemory(meta.myInteger.asc).asQueryResultList();
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultListForIn() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        assertThat(query
            .filter(meta.myString.in("aaa", "bbb"))
            .asQueryResultList()
            .size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingle() throws Exception {
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        Hoge hoge = query.asSingle();
        assertThat(hoge, is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test(expected = TooManyResultsException.class)
    public void asSingleWhenTooManyResults() throws Exception {
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        query.asSingle();
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleAndFilterInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        Hoge hoge2 = new Hoge();
        hoge2.setMyString("bbb");
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge2));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
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
        DatastoreUtil.put(ds, null, meta.modelToEntity(hoge));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        Hoge ret = query.sortInMemory(meta.myString.asc).asSingle();
        assertThat(ret, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleWhenNoEntity() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        Hoge ret = query.filterInMemory(meta.myString.equal("aaa")).asSingle();
        assertThat(ret, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyList() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        assertThat(query.asKeyList(), is(Arrays.asList(key)));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void asKeyListAndFilterInMemory() throws Exception {
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        query.filterInMemory(meta.myString.equal("aaa")).asKeyList();
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyListAndAscSortInMemory() throws Exception {
        Key key = KeyFactory.createKey(meta.getKind(), 1);
        Key key2 = KeyFactory.createKey(meta.getKind(), 2);
        DatastoreUtil.put(ds, null, new Entity(key2));
        DatastoreUtil.put(ds, null, new Entity(key));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
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
        Key key = KeyFactory.createKey(meta.getKind(), 1);
        Key key2 = KeyFactory.createKey(meta.getKind(), 2);
        DatastoreUtil.put(ds, null, new Entity(key));
        DatastoreUtil.put(ds, null, new Entity(key2));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
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
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        query.sortInMemory(meta.myString.asc).asKeyList();
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyListForPolyModel() throws Exception {
        DatastoreUtil.put(ds, null, AaaMeta.get().modelToEntity(new Aaa()));
        DatastoreUtil.put(ds, null, BbbMeta.get().modelToEntity(new Bbb()));

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(ds, aaaMeta);
        List<Key> list = query.asKeyList();
        assertThat(list.size(), is(2));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(ds, bbbMeta);
        List<Key> list2 = query2.asKeyList();
        assertThat(list2.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asIterator() throws Exception {
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        Iterator<Hoge> iterator = query.asIterator();
        assertThat(iterator.next(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asIteratorForPolyModel() throws Exception {
        DatastoreUtil.put(ds, null, AaaMeta.get().modelToEntity(new Aaa()));
        DatastoreUtil.put(ds, null, BbbMeta.get().modelToEntity(new Bbb()));
        DatastoreUtil.put(ds, null, CccMeta.get().modelToEntity(new Ccc()));

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(ds, aaaMeta);
        Iterator<Aaa> iterator = query.asIterator();
        assertThat(
            iterator.next().getClass().getName(),
            is(Aaa.class.getName()));
        assertThat(
            iterator.next().getClass().getName(),
            is(Bbb.class.getName()));
        assertThat(
            iterator.next().getClass().getName(),
            is(Ccc.class.getName()));
        assertThat(iterator.hasNext(), is(false));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(ds, bbbMeta);
        Iterator<Bbb> iterator2 = query2.asIterator();
        assertThat(iterator2.next().getClass().getName(), is(Bbb.class
            .getName()));
        assertThat(iterator2.next().getClass().getName(), is(Ccc.class
            .getName()));
        assertThat(iterator2.hasNext(), is(false));

        ModelQuery<Ccc> query3 = new ModelQuery<Ccc>(ds, cccMeta);
        Iterator<Ccc> iterator3 = query3.asIterator();
        assertThat(iterator3.next().getClass().getName(), is(Ccc.class
            .getName()));
        assertThat(iterator3.hasNext(), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asIterable() throws Exception {
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        Iterable<Hoge> iterable = query.asIterable();
        assertThat(iterable.iterator(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void ancestor() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Parent"));
        DatastoreUtil.put(ds, null, new Entity("Hoge", key));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta, key);
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
        DatastoreUtil.put(ds, null, Arrays.asList(
            meta.modelToEntity(hoge),
            meta.modelToEntity(hoge2),
            meta.modelToEntity(hoge3)));
        assertThat(new ModelQuery<Hoge>(ds, meta).min(meta.myInteger), is(1));
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
        DatastoreUtil.put(ds, null, Arrays.asList(
            meta.modelToEntity(hoge),
            meta.modelToEntity(hoge2),
            meta.modelToEntity(hoge3)));
        new ModelQuery<Hoge>(ds, meta)
            .filterInMemory(meta.myInteger.equal(1))
            .min(meta.myInteger);
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
        DatastoreUtil.put(ds, null, Arrays.asList(
            meta.modelToEntity(hoge),
            meta.modelToEntity(hoge2),
            meta.modelToEntity(hoge3)));
        new ModelQuery<Hoge>(ds, meta).sortInMemory(meta.myInteger.asc).min(
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
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, aaa));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, bbb));

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(ds, aaaMeta);
        assertThat(query.min(aaaMeta.version), is(2L));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(ds, bbbMeta);
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
        DatastoreUtil.put(ds, null, Arrays.asList(
            meta.modelToEntity(hoge),
            meta.modelToEntity(hoge2),
            meta.modelToEntity(hoge3)));
        assertThat(new ModelQuery<Hoge>(ds, meta).max(meta.myInteger), is(2));
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
        DatastoreUtil.put(ds, null, Arrays.asList(
            meta.modelToEntity(hoge),
            meta.modelToEntity(hoge2),
            meta.modelToEntity(hoge3)));
        new ModelQuery<Hoge>(ds, meta)
            .filterInMemory(meta.myInteger.equal(1))
            .max(meta.myInteger);
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
        DatastoreUtil.put(ds, null, Arrays.asList(
            meta.modelToEntity(hoge),
            meta.modelToEntity(hoge2),
            meta.modelToEntity(hoge3)));
        new ModelQuery<Hoge>(ds, meta).sortInMemory(meta.myInteger.asc).max(
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
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, aaa));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, bbb));

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(ds, aaaMeta);
        assertThat(query.max(aaaMeta.version), is(3L));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(ds, bbbMeta);
        assertThat(query2.max(bbbMeta.version), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void countAndFilterInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        assertThat(
            query.filterInMemory(meta.myString.equal("aaa")).count(),
            is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void countAndSortInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        assertThat(query.sortInMemory(meta.myString.asc).count(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void countForPolyModel() throws Exception {
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, new Aaa()));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, new Bbb()));

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(ds, aaaMeta);
        assertThat(query.count(), is(2));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(ds, bbbMeta);
        assertThat(query2.count(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterByEqualCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).filter(
                new EqualCriterion(meta.myCipherString, "1102")).sortInMemory(
                meta.myCipherString.asc).asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterByInCriterionOfCipherString() throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).filter(
                new InCriterion(meta.myCipherString, Arrays.asList(
                    "1102",
                    "1104"))).sortInMemory(meta.myCipherString.asc).asList();
        assertThat(list.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterByIsNotNullCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).filter(
                new IsNotNullCriterion(meta.myCipherString)).sortInMemory(
                meta.myCipherString.asc).asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
        assertThat(list.get(2).getMyCipherString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterByNotEqualCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filter(new NotEqualCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListOfCipherLobString() throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).sortInMemory(
                meta.myCipherLobString.asc).asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
        assertThat(list.get(2).getMyCipherLobString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListOfCipherText() throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).sortInMemory(meta.key.asc).asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1102"));
        assertThat(list.get(1).getMyCipherText().getValue(), is("1103"));
        assertThat(list.get(2).getMyCipherText().getValue(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByEqualCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new EqualCriterion(meta.myCipherString, "1102")).sortInMemory(
                meta.myCipherString.asc).asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryEqualCriterion(meta.myCipherString, "1102"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByGreaterThanCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new GreaterThanCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryGreaterThanCriterion(
                        meta.myCipherString,
                        "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByGreaterThanOrEqualCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new GreaterThanOrEqualCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1103"));
        assertThat(list.get(1).getMyCipherString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new InMemoryGreaterThanOrEqualCriterion(
                    meta.myCipherString,
                    "1103")).sortInMemory(meta.myCipherString.asc).asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1103"));
        assertThat(list.get(1).getMyCipherString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByInCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new InCriterion(meta.myCipherString, Arrays.asList(
                    "1102",
                    "1104"))).sortInMemory(meta.myCipherString.asc).asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new InMemoryInCriterion(meta.myCipherString, Arrays.asList(
                    "1102",
                    "1104"))).sortInMemory(meta.myCipherString.asc).asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByContainsCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryContainsCriterion(meta.myCipherString, "110"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
        assertThat(list.get(2).getMyCipherString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByEndsWithCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryEndsWithCriterion(meta.myCipherString, "103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1103"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByIsNotNullCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new IsNotNullCriterion(meta.myCipherString)).sortInMemory(
                meta.myCipherString.asc).asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
        assertThat(list.get(2).getMyCipherString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryIsNotNullCriterion(meta.myCipherString))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
        assertThat(list.get(2).getMyCipherString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByLessThanCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new LessThanCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryLessThanCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByLessThanOrEqualCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new LessThanOrEqualCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
        list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new InMemoryLessThanOrEqualCriterion(
                    meta.myCipherString,
                    "1103")).sortInMemory(meta.myCipherString.asc).asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByNotEqualCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new NotEqualCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryNotEqualCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByStartsWithCriterionOfCipherString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new StartsWithCriterion(meta.myCipherString, "110"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
        assertThat(list.get(2).getMyCipherString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryStartsWithCriterion(meta.myCipherString, "110"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
        assertThat(list.get(2).getMyCipherString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByEqualCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new EqualCriterion(meta.myCipherLobString, "1102"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryEqualCriterion(meta.myCipherLobString, "1102"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByGreaterThanCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new GreaterThanCriterion(meta.myCipherLobString, "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherLobString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryGreaterThanCriterion(
                        meta.myCipherLobString,
                        "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherLobString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByGreaterThanOrEqualCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new GreaterThanOrEqualCriterion(
                        meta.myCipherLobString,
                        "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1103"));
        assertThat(list.get(1).getMyCipherLobString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new InMemoryGreaterThanOrEqualCriterion(
                    meta.myCipherLobString,
                    "1103")).sortInMemory(meta.myCipherLobString.asc).asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1103"));
        assertThat(list.get(1).getMyCipherLobString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByInCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new InCriterion(meta.myCipherLobString, Arrays.asList(
                    "1102",
                    "1104"))).sortInMemory(meta.myCipherLobString.asc).asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new InMemoryInCriterion(meta.myCipherLobString, Arrays.asList(
                    "1102",
                    "1104"))).sortInMemory(meta.myCipherLobString.asc).asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByContainsCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryContainsCriterion(meta.myCipherLobString, "110"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
        assertThat(list.get(2).getMyCipherLobString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByEndsWithCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryEndsWithCriterion(meta.myCipherLobString, "103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherLobString(), is("1103"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByIsNotNullCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new IsNotNullCriterion(meta.myCipherLobString)).sortInMemory(
                meta.myCipherLobString.asc).asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
        assertThat(list.get(2).getMyCipherLobString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryIsNotNullCriterion(meta.myCipherLobString))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
        assertThat(list.get(2).getMyCipherLobString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByLessThanCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new LessThanCriterion(meta.myCipherLobString, "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryLessThanCriterion(
                        meta.myCipherLobString,
                        "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByLessThanOrEqualCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new LessThanOrEqualCriterion(meta.myCipherLobString, "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
        list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new InMemoryLessThanOrEqualCriterion(
                    meta.myCipherLobString,
                    "1103")).sortInMemory(meta.myCipherLobString.asc).asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByNotEqualCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new NotEqualCriterion(meta.myCipherLobString, "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryNotEqualCriterion(
                        meta.myCipherLobString,
                        "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByStartsWithCriterionOfCipherLobString()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new StartsWithCriterion(meta.myCipherLobString, "110"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
        assertThat(list.get(2).getMyCipherLobString(), is("1104"));
        list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryStartsWithCriterion(
                        meta.myCipherLobString,
                        "110"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
        assertThat(list.get(2).getMyCipherLobString(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByEqualCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(new EqualCriterion(meta.myCipherText, "1102"))
                .filterInMemory(
                    new EqualCriterion(meta.myCipherText, new Text("1102")))
                .filterInMemory(
                    new InMemoryEqualCriterion(meta.myCipherText, "1102"))
                .filterInMemory(
                    new InMemoryEqualCriterion(meta.myCipherText, new Text(
                        "1102")))
                .sortInMemory(meta.key.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1102"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByGreaterThanCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new GreaterThanCriterion(meta.myCipherText, "1103"))
                .filterInMemory(
                    new GreaterThanCriterion(
                        meta.myCipherText,
                        new Text("1103")))
                .filterInMemory(
                    new InMemoryGreaterThanCriterion(meta.myCipherText, "1103"))
                .filterInMemory(
                    new InMemoryGreaterThanCriterion(
                        meta.myCipherText,
                        new Text("1103")))
                .sortInMemory(meta.key.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByGreaterThanOrEqualCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new GreaterThanOrEqualCriterion(meta.myCipherText, "1103"))
                .filterInMemory(
                    new GreaterThanOrEqualCriterion(
                        meta.myCipherText,
                        new Text("1103")))
                .filterInMemory(
                    new InMemoryGreaterThanOrEqualCriterion(
                        meta.myCipherText,
                        "1103"))
                .filterInMemory(
                    new InMemoryGreaterThanOrEqualCriterion(
                        meta.myCipherText,
                        new Text("1103")))
                .sortInMemory(meta.key.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1103"));
        assertThat(list.get(1).getMyCipherText().getValue(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByInCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta).filterInMemory(
                new InCriterion(meta.myCipherText, Arrays
                    .asList("1102", "1104"))).filterInMemory(
                new InCriterion(meta.myCipherText, Arrays.asList(new Text(
                    "1102"), new Text("1104")))).filterInMemory(
                new InMemoryInCriterion(meta.myCipherText, Arrays.asList(
                    "1102",
                    "1104"))).filterInMemory(
                new InMemoryInCriterion(meta.myCipherText, Arrays.asList(
                    new Text("1102"),
                    new Text("1104")))).sortInMemory(meta.key.asc).asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1102"));
        assertThat(list.get(1).getMyCipherText().getValue(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByContainsCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryContainsCriterion(meta.myCipherText, "110"))
                .sortInMemory(meta.key.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1102"));
        assertThat(list.get(1).getMyCipherText().getValue(), is("1103"));
        assertThat(list.get(2).getMyCipherText().getValue(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByEndsWithCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new InMemoryEndsWithCriterion(meta.myCipherText, "103"))
                .sortInMemory(meta.key.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1103"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByIsNotNullCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(new IsNotNullCriterion(meta.myCipherText))
                .filterInMemory(
                    new InMemoryIsNotNullCriterion(meta.myCipherText))
                .sortInMemory(meta.key.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1102"));
        assertThat(list.get(1).getMyCipherText().getValue(), is("1103"));
        assertThat(list.get(2).getMyCipherText().getValue(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByLessThanCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new LessThanCriterion(meta.myCipherText, "1103"))
                .filterInMemory(
                    new LessThanCriterion(meta.myCipherText, new Text("1103")))
                .filterInMemory(
                    new InMemoryLessThanCriterion(meta.myCipherText, "1103"))
                .filterInMemory(
                    new InMemoryLessThanCriterion(meta.myCipherText, new Text(
                        "1103")))
                .sortInMemory(meta.key.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1102"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByLessThanOrEqualCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new LessThanOrEqualCriterion(meta.myCipherText, "1103"))
                .filterInMemory(
                    new LessThanOrEqualCriterion(meta.myCipherText, new Text(
                        "1103")))
                .filterInMemory(
                    new InMemoryLessThanOrEqualCriterion(
                        meta.myCipherText,
                        "1103"))
                .filterInMemory(
                    new InMemoryLessThanOrEqualCriterion(
                        meta.myCipherText,
                        new Text("1103")))
                .sortInMemory(meta.key.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1102"));
        assertThat(list.get(1).getMyCipherText().getValue(), is("1103"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByNotEqualCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new NotEqualCriterion(meta.myCipherText, "1103"))
                .filterInMemory(
                    new NotEqualCriterion(meta.myCipherText, new Text("1103")))
                .filterInMemory(
                    new InMemoryNotEqualCriterion(meta.myCipherText, "1103"))
                .filterInMemory(
                    new InMemoryNotEqualCriterion(meta.myCipherText, new Text(
                        "1103")))
                .sortInMemory(meta.key.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1102"));
        assertThat(list.get(1).getMyCipherText().getValue(), is("1104"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterInMemoryByStartsWithCriterionOfCipherText()
            throws Exception {
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge1));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge2));
        DatastoreUtil.put(ds, null, DatastoreUtil.modelToEntity(ds, hoge3));
        List<Hoge> list =
            new ModelQuery<Hoge>(ds, meta)
                .filterInMemory(
                    new StartsWithCriterion(meta.myCipherText, "110"))
                .filterInMemory(
                    new InMemoryStartsWithCriterion(meta.myCipherText, "110"))
                .sortInMemory(meta.key.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherText().getValue(), is("1102"));
        assertThat(list.get(1).getMyCipherText().getValue(), is("1103"));
        assertThat(list.get(2).getMyCipherText().getValue(), is("1104"));
    }
}