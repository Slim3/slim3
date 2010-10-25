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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Text;

/**
 * @author higa
 * 
 */
public class ModelQueryTest extends AppEngineTestCase {

    private static final String KEY128BIT = "1234567890ABCDEF";
    
    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    private HogeMeta meta = new HogeMeta();

    private AaaMeta aaaMeta = new AaaMeta();

    private BbbMeta bbbMeta = new BbbMeta();

    private CccMeta cccMeta = new CccMeta();

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
        Datastore.put(new Ccc());

        ModelQuery<Aaa> query = new ModelQuery<Aaa>(aaaMeta);
        List<Aaa> list = query.asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getClass().getName(), is(Aaa.class.getName()));
        assertThat(list.get(1).getClass().getName(), is(Bbb.class.getName()));
        assertThat(list.get(2).getClass().getName(), is(Ccc.class.getName()));

        ModelQuery<Bbb> query2 = new ModelQuery<Bbb>(bbbMeta);
        List<Bbb> list2 = query2.asList();
        assertThat(list2.size(), is(2));
        assertThat(list2.get(0).getClass().getName(), is(Bbb.class.getName()));
        assertThat(list2.get(1).getClass().getName(), is(Ccc.class.getName()));

        ModelQuery<Ccc> query3 = new ModelQuery<Ccc>(cccMeta);
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
        Datastore.put(hoge);
        hoge = new Hoge();
        hoge.setMyInteger(2);
        Datastore.put(hoge);
        hoge = new Hoge();
        hoge.setMyInteger(3);
        Datastore.put(hoge);
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        S3QueryResultList<Hoge> list =
            query.limit(1).sort(meta.myInteger.asc).asQueryResultList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyInteger(), is(1));
        assertThat(list.hasNext(), is(true));
        assertThat(list.getEncodedCursor(), is(notNullValue()));
        assertThat(list.getEncodedFilters(), is(notNullValue()));
        assertThat(list.getEncodedSorts(), is(notNullValue()));

        ModelQuery<Hoge> query2 = new ModelQuery<Hoge>(meta);
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

        ModelQuery<Hoge> query3 = new ModelQuery<Hoge>(meta);
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
        Datastore.put(hoge);
        hoge = new Hoge();
        hoge.setMyInteger(2);
        Datastore.put(hoge);
        hoge = new Hoge();
        hoge.setMyInteger(3);
        Datastore.put(hoge);
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
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
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        query.filterInMemory(meta.myInteger.equal(1)).asQueryResultList();
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void asQueryResultListWithSortInMemory() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        query.sortInMemory(meta.myInteger.asc).asQueryResultList();
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
    @Test
    public void countAndFilterInMemory() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        Datastore.put(hoge);
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
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
        Datastore.put(hoge);
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertThat(query.sortInMemory(meta.myString.asc).count(), is(1));
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
    @Test
    public void asListAndFilterByEqualCriterionOfCipherString()
            throws Exception {
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filter(new EqualCriterion(meta.myCipherString, "1102"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterByInCriterionOfCipherString() throws Exception {
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filter(
                    new InCriterion(meta.myCipherString, Arrays.asList(
                        "1102",
                        "1104")))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asListAndFilterByIsNotNullCriterionOfCipherString()
            throws Exception {
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filter(new IsNotNullCriterion(meta.myCipherString))
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
    public void asListAndFilterByNotEqualCriterionOfCipherString()
            throws Exception {
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
    public void asListOfCipherText() throws Exception {
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore.query(meta).sortInMemory(meta.key.asc).asList();
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(new EqualCriterion(meta.myCipherString, "1102"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new GreaterThanCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1104"));
        list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new GreaterThanOrEqualCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1103"));
        assertThat(list.get(1).getMyCipherString(), is("1104"));
        list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InMemoryGreaterThanOrEqualCriterion(
                        meta.myCipherString,
                        "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InCriterion(meta.myCipherString, Arrays.asList(
                        "1102",
                        "1104")))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1104"));
        list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InMemoryInCriterion(meta.myCipherString, Arrays.asList(
                        "1102",
                        "1104")))
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
    public void asListAndFilterInMemoryByContainsCriterionOfCipherString()
            throws Exception {
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(new IsNotNullCriterion(meta.myCipherString))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
        assertThat(list.get(2).getMyCipherString(), is("1104"));
        list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new LessThanCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new LessThanOrEqualCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
        list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InMemoryLessThanOrEqualCriterion(
                        meta.myCipherString,
                        "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new NotEqualCriterion(meta.myCipherString, "1103"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1104"));
        list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherString("1102");
        hoge2.setMyCipherString("1103");
        hoge3.setMyCipherString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new StartsWithCriterion(meta.myCipherString, "110"))
                .sortInMemory(meta.myCipherString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherString(), is("1102"));
        assertThat(list.get(1).getMyCipherString(), is("1103"));
        assertThat(list.get(2).getMyCipherString(), is("1104"));
        list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(new EqualCriterion(meta.myCipherLobString, "1102"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new GreaterThanCriterion(meta.myCipherLobString, "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherLobString(), is("1104"));
        list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new GreaterThanOrEqualCriterion(meta.myCipherLobString, "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1103"));
        assertThat(list.get(1).getMyCipherLobString(), is("1104"));
        list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InMemoryGreaterThanOrEqualCriterion(
                        meta.myCipherLobString,
                        "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InCriterion(meta.myCipherLobString, Arrays.asList(
                        "1102",
                        "1104")))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1104"));
        list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InMemoryInCriterion(meta.myCipherLobString, Arrays.asList(
                        "1102",
                        "1104")))
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
    public void asListAndFilterInMemoryByContainsCriterionOfCipherLobString()
            throws Exception {
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(new IsNotNullCriterion(meta.myCipherLobString))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
        assertThat(list.get(2).getMyCipherLobString(), is("1104"));
        list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new LessThanCriterion(meta.myCipherLobString, "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InMemoryLessThanCriterion(meta.myCipherLobString, "1103"))
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new LessThanOrEqualCriterion(meta.myCipherLobString, "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
        list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InMemoryLessThanOrEqualCriterion(
                        meta.myCipherLobString,
                        "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new NotEqualCriterion(meta.myCipherLobString, "1103"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1104"));
        list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InMemoryNotEqualCriterion(meta.myCipherLobString, "1103"))
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherLobString("1102");
        hoge2.setMyCipherLobString("1103");
        hoge3.setMyCipherLobString("1104");
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new StartsWithCriterion(meta.myCipherLobString, "110"))
                .sortInMemory(meta.myCipherLobString.asc)
                .asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getMyCipherLobString(), is("1102"));
        assertThat(list.get(1).getMyCipherLobString(), is("1103"));
        assertThat(list.get(2).getMyCipherLobString(), is("1104"));
        list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InMemoryStartsWithCriterion(meta.myCipherLobString, "110"))
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
                .filterInMemory(
                    new InCriterion(meta.myCipherText, Arrays.asList(
                        "1102",
                        "1104")))
                .filterInMemory(
                    new InCriterion(meta.myCipherText, Arrays.asList(new Text(
                        "1102"), new Text("1104"))))
                .filterInMemory(
                    new InMemoryInCriterion(meta.myCipherText, Arrays.asList(
                        "1102",
                        "1104")))
                .filterInMemory(
                    new InMemoryInCriterion(meta.myCipherText, Arrays.asList(
                        new Text("1102"),
                        new Text("1104"))))
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
    public void asListAndFilterInMemoryByContainsCriterionOfCipherText()
            throws Exception {
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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
        Datastore.setLimitedCipherKey(KEY128BIT);
        Hoge hoge1 = new Hoge();
        Hoge hoge2 = new Hoge();
        Hoge hoge3 = new Hoge();
        hoge1.setMyCipherText(new Text("1102"));
        hoge2.setMyCipherText(new Text("1103"));
        hoge3.setMyCipherText(new Text("1104"));
        Datastore.put(hoge1);
        Datastore.put(hoge2);
        Datastore.put(hoge3);
        List<Hoge> list =
            Datastore
                .query(meta)
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