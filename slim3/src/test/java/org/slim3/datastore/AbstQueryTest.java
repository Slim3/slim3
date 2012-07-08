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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.slim3.repackaged.com.google.gdata.util.common.util.Base64;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.ByteUtil;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class AbstQueryTest extends AppEngineTestCase {

    private AsyncDatastoreService ds = DatastoreServiceFactory
        .getAsyncDatastoreService();

    /**
     * @throws Exception
     */
    @Test
    public void constructor() throws Exception {
        MyQuery q = new MyQuery(ds);
        assertThat(q.query, is(notNullValue()));
        assertThat(q.query.getKind(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingKind() throws Exception {
        MyQuery q = new MyQuery(ds, "Hoge");
        assertThat(q.query.getKind(), is("Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingKindAndAncestorKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        MyQuery q = new MyQuery(ds, "Hoge", key);
        assertThat(q.query.getKind(), is("Hoge"));
        assertThat(q.query.getAncestor(), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingAncestorKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        MyQuery q = new MyQuery(ds, key);
        assertThat(q.query.getAncestor(), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setTx() throws Exception {
        MyQuery q = new MyQuery(ds, "Hoge");
        q.setTx(DatastoreUtil.beginTransaction(ds));
        assertNotNull(q.tx);
        assertTrue(q.txSet);
    }

    /**
     * @throws Exception
     */
    @Test
    public void setTxForNothing() throws Exception {
        MyQuery q = new MyQuery(ds, "Hoge");
        assertThat(q.tx, is(nullValue()));
        assertThat(q.txSet, is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setTxForNull() throws Exception {
        MyQuery q = new MyQuery(ds, "Hoge");
        q.setTx(null);
        assertThat(q.tx, is(nullValue()));
        assertThat(q.txSet, is(true));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void setIllegalTx() throws Exception {
        MyQuery q = new MyQuery(ds, "Hoge");
        Transaction tx = DatastoreUtil.beginTransaction(ds);
        tx.rollback();
        q.setTx(tx);
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyFilterForSingleFilter() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        query.filter("myString", FilterOperator.EQUAL, "aaa");
        query.applyFilter();
        Query.Filter filter = query.getFilter();
        assertThat(filter, is(Query.FilterPredicate.class));
        Query.FilterPredicate fp = (Query.FilterPredicate) filter;
        assertThat(fp.getPropertyName(), is("myString"));
        assertThat(fp.getOperator(), is(FilterOperator.EQUAL));
        assertThat((String) fp.getValue(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyFilterForMultipleFilters() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        query.filter("myString", FilterOperator.EQUAL, "aaa");
        query.filter("myInteger", FilterOperator.EQUAL, 1);
        query.applyFilter();
        Query.Filter filter = query.getFilter();
        assertThat(filter, is(Query.CompositeFilter.class));
        Query.CompositeFilter f = (Query.CompositeFilter) filter;
        assertThat(f.getOperator(), is(Query.CompositeFilterOperator.AND));
        List<Filter> subFilters = f.getSubFilters();
        assertThat(subFilters.size(), is(2));
        Query.Filter sub = subFilters.get(0);
        Query.Filter sub2 = subFilters.get(1);
        assertThat(sub, is(Query.FilterPredicate.class));
        Query.FilterPredicate fp = (Query.FilterPredicate) sub;
        assertThat(fp.getPropertyName(), is("myString"));
        assertThat(fp.getOperator(), is(FilterOperator.EQUAL));
        assertThat((String) fp.getValue(), is("aaa"));
        assertThat(sub2, is(Query.FilterPredicate.class));
        Query.FilterPredicate fp2 = (Query.FilterPredicate) sub2;
        assertThat(fp2.getPropertyName(), is("myInteger"));
        assertThat(fp2.getOperator(), is(FilterOperator.EQUAL));
        assertThat((Integer) fp2.getValue(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void prepareQuery() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        query.filter("myString", FilterOperator.EQUAL, "aaa");
        PreparedQuery pq = query.prepareQuery();
        assertThat(pq, is(notNullValue()));
        Query.Filter filter = query.getFilter();
        assertThat(filter, is(Query.FilterPredicate.class));
        Query.FilterPredicate fp = (Query.FilterPredicate) filter;
        assertThat(fp.getPropertyName(), is("myString"));
        assertThat(fp.getOperator(), is(FilterOperator.EQUAL));
        assertThat((String) fp.getValue(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asEntityList() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        List<Entity> list = query.asEntityList();
        assertThat(list.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asEntityListForKindlessQuery() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds);
        List<Entity> list = query.asEntityList();
        assertThat(list.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asEntityListForKindlessAncestorQuery() throws Exception {
        Key parentKey = DatastoreUtil.put(ds, null, new Entity("Parent"));
        ds.put(new Entity(KeyFactory.createKey(parentKey, "Child", 1)));
        MyQuery query = new MyQuery(ds, parentKey);
        List<Entity> list = query.asEntityList();
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleEntity() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        assertThat(query.asSingleEntity(), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleEntityForKindlessQuery() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds);
        assertThat(query.asSingleEntity(), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyList() throws Exception {
        Key key = DatastoreUtil.put(ds, null, new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        assertThat(query.asKeyList(), is(Arrays.asList(key)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asIterableEntities() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
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
    public void asEntityIterator() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        boolean found = false;
        for (Iterator<Entity> i = query.asEntityIterator(); i.hasNext();) {
            found = true;
            assertThat(i.next(), is(notNullValue()));
        }
        assertThat(found, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultEntityList() throws Exception {
        ds.put(new Entity("Hoge"));
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        QueryResultList<Entity> list = query.limit(1).asQueryResultEntityList();
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), is(1));
        assertThat(list.getCursor(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultEntityIterator() throws Exception {
        ds.put(new Entity("Hoge"));
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        QueryResultIterator<Entity> ite =
            query.limit(1).asQueryResultEntityIterator();
        assertThat(ite, is(notNullValue()));
        assertThat(ite.getCursor(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultEntityIterable() throws Exception {
        ds.put(new Entity("Hoge"));
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        QueryResultIterable<Entity> iterable =
            query.limit(1).asQueryResultEntityIterable();
        assertThat(iterable, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void filter() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        Query.Filter filter =
            new Query.FilterPredicate("myString", FilterOperator.EQUAL, "aaa");
        query.filter(filter);
        assertThat(query.filters.get(0), is(sameInstance(filter)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void encodedFilter() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        Query.Filter filter =
            new Query.FilterPredicate("myString", FilterOperator.EQUAL, "aaa");
        String encodedFilter = Base64.encode(ByteUtil.toByteArray(filter));
        query.encodedFilter(encodedFilter);
        assertThat(query.filters.get(0), is(filter));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sortForSort() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        assertThat(
            query.sort(new Sort("myString", SortDirection.ASCENDING)),
            is(sameInstance(query)));
        assertThat(query.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sortWithPropertyName() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        assertThat(query.sort("myString"), is(sameInstance(query)));
        assertThat(query.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sortForWithPropertyNameAndDirection() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        assertThat(
            query.sort("myString", SortDirection.ASCENDING),
            is(sameInstance(query)));
        assertThat(query.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void encodedSorts() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        query.sort("myString");
        MyQuery query2 = new MyQuery(ds, "Hoge");
        assertThat(
            query2.encodedSorts(query.getEncodedSorts()),
            is(sameInstance(query2)));
        assertThat(query2.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void count() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        assertThat(query.count(), is(1));
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
        MyQuery query = new MyQuery(ds, "Hoge");
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
        MyQuery query = new MyQuery(ds, "Hoge");
        assertThat((Long) query.max("age"), is(20L));
        assertThat(query.max("name"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void offset() throws Exception {
        MyQuery q = new MyQuery(ds, "Hoge");
        assertThat(q.offset(10), is(sameInstance(q)));
        assertThat(q.fetchOptions.getOffset(), is(10));
    }

    /**
     * @throws Exception
     */
    @Test
    public void limit() throws Exception {
        MyQuery q = new MyQuery(ds, "Hoge");
        assertThat(q.limit(100), is(sameInstance(q)));
        assertThat(q.fetchOptions.getLimit(), is(100));
    }

    /**
     * @throws Exception
     */
    @Test
    public void prefetchSize() throws Exception {
        MyQuery q = new MyQuery(ds, "Hoge");
        assertThat(q.prefetchSize(15), is(sameInstance(q)));
        assertThat(q.fetchOptions.getPrefetchSize(), is(15));
    }

    /**
     * @throws Exception
     */
    @Test
    public void chunkSize() throws Exception {
        MyQuery q = new MyQuery(ds, "Hoge");
        assertThat(q.chunkSize(20), is(sameInstance(q)));
        assertThat(q.fetchOptions.getChunkSize(), is(20));
    }

    /**
     * @throws Exception
     */
    @Test
    public void startCursor() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        QueryResultList<Entity> list = query.limit(1).asQueryResultEntityList();
        Cursor cursor = list.getCursor();
        query = new MyQuery(ds, "Hoge");
        assertThat(query.startCursor(cursor), is(sameInstance(query)));
        assertThat(query.fetchOptions.getStartCursor(), is(cursor));
    }

    /**
     * @throws Exception
     */
    @Test
    public void endCursor() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        QueryResultList<Entity> list = query.limit(1).asQueryResultEntityList();
        Cursor cursor = list.getCursor();
        query = new MyQuery(ds, "Hoge");
        assertThat(query.endCursor(cursor), is(sameInstance(query)));
        assertThat(query.fetchOptions.getEndCursor(), is(cursor));
    }

    /**
     * @throws Exception
     */
    @Test
    public void encodedStartCursor() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        QueryResultList<Entity> list = query.limit(1).asQueryResultEntityList();
        Cursor cursor = list.getCursor();
        String encodedCursor = cursor.toWebSafeString();
        query = new MyQuery(ds, "Hoge");
        assertThat(
            query.encodedStartCursor(encodedCursor),
            is(sameInstance(query)));
        assertThat(query.fetchOptions.getStartCursor(), is(cursor));
    }

    /**
     * @throws Exception
     */
    @Test
    public void encodedEndCursor() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery(ds, "Hoge");
        QueryResultList<Entity> list = query.limit(1).asQueryResultEntityList();
        Cursor cursor = list.getCursor();
        String encodedCursor = cursor.toWebSafeString();
        query = new MyQuery(ds, "Hoge");
        assertThat(
            query.encodedEndCursor(encodedCursor),
            is(sameInstance(query)));
        assertThat(query.fetchOptions.getEndCursor(), is(cursor));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEncodedFilter() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        Query.Filter filter =
            new Query.FilterPredicate("myString", FilterOperator.EQUAL, "aaa");
        String encodedFilter = Base64.encode(ByteUtil.toByteArray(filter));
        query.filter(filter);
        query.applyFilter();
        assertThat(query.getEncodedFilter(), is(encodedFilter));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSorts() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        query.query.addSort("aaa", SortDirection.ASCENDING);
        query.query.addSort("bbb", SortDirection.DESCENDING);
        Sort[] sorts = query.getSorts();
        assertThat(sorts.length, is(2));
        assertThat(sorts[0].getPropertyName(), is("aaa"));
        assertThat(sorts[0].getDirection(), is(SortDirection.ASCENDING));
        assertThat(sorts[1].getPropertyName(), is("bbb"));
        assertThat(sorts[1].getDirection(), is(SortDirection.DESCENDING));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEncodedSorts() throws Exception {
        MyQuery query = new MyQuery(ds, "Hoge");
        query.query.addSort("aaa", SortDirection.ASCENDING);
        query.query.addSort("bbb", SortDirection.DESCENDING);
        String encodedSorts = query.getEncodedSorts();
        Sort[] sorts = ByteUtil.toObject(Base64.decode(encodedSorts));
        assertThat(sorts.length, is(2));
        assertThat(sorts[0].getPropertyName(), is("aaa"));
        assertThat(sorts[0].getDirection(), is(SortDirection.ASCENDING));
        assertThat(sorts[1].getPropertyName(), is("bbb"));
        assertThat(sorts[1].getDirection(), is(SortDirection.DESCENDING));
    }

    private static class MyQuery extends AbstractQuery<MyQuery> {

        /**
         * @param ds
         * 
         */
        public MyQuery(AsyncDatastoreService ds) {
            super(ds);
        }

        /**
         * @param ds
         * @param ancestorKey
         * @throws NullPointerException
         */
        public MyQuery(AsyncDatastoreService ds, Key ancestorKey)
                throws NullPointerException {
            super(ds, ancestorKey);
        }

        /**
         * @param ds
         * @param kind
         * @param ancestorKey
         * @throws NullPointerException
         */
        public MyQuery(AsyncDatastoreService ds, String kind, Key ancestorKey)
                throws NullPointerException {
            super(ds, kind, ancestorKey);
        }

        /**
         * @param ds
         * @param kind
         * @throws NullPointerException
         */
        public MyQuery(AsyncDatastoreService ds, String kind)
                throws NullPointerException {
            super(ds, kind);
        }
    }
}