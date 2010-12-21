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

import java.util.Iterator;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class EntityQueryTest extends AppEngineTestCase {

    private AsyncDatastoreService ds =
        DatastoreServiceFactory.getAsyncDatastoreService();

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingTxAndKindAndAncestorKey() throws Exception {
        Key ancestorKey = KeyFactory.createKey("Ancestor", 1);
        EntityQuery query =
            new EntityQuery(
                ds,
                DatastoreUtil.beginTransaction(ds),
                "Hoge",
                ancestorKey);
        assertThat(query.query.getKind(), is("Hoge"));
        assertThat(query.query.getAncestor(), is(ancestorKey));
        assertThat(query.tx, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sort() throws Exception {
        EntityQuery q = new EntityQuery(ds, "Hoge");
        assertThat(q.sort("aaa", SortDirection.DESCENDING), is(sameInstance(q)));
        assertThat(q.query.getSortPredicates().size(), is(1));
        assertThat(
            q.query.getSortPredicates().get(0).getPropertyName(),
            is("aaa"));
        assertThat(
            q.query.getSortPredicates().get(0).getDirection(),
            is(SortDirection.DESCENDING));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asList() throws Exception {
        ds.put(new Entity("Hoge"));
        EntityQuery q = new EntityQuery(ds, "Hoge");
        assertThat(q.asList().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultList() throws Exception {
        ds.put(new Entity("Hoge"));
        EntityQuery q = new EntityQuery(ds, "Hoge");
        assertThat(q.asQueryResultList().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultIterator() throws Exception {
        ds.put(new Entity("Hoge"));
        EntityQuery q = new EntityQuery(ds, "Hoge");
        assertThat(q.asQueryResultIterator(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleEntity() throws Exception {
        ds.put(new Entity("Hoge"));
        EntityQuery q = new EntityQuery(ds, "Hoge");
        Entity entity = q.asSingleEntity();
        assertThat(entity, is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asIterable() throws Exception {
        ds.put(new Entity("Hoge"));
        EntityQuery q = new EntityQuery(ds, "Hoge");
        boolean found = false;
        for (Entity entity : q.asIterable()) {
            found = true;
            assertThat(entity.getKind(), is("Hoge"));
        }
        assertThat(found, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asIterator() throws Exception {
        ds.put(new Entity("Hoge"));
        EntityQuery q = new EntityQuery(ds, "Hoge");
        boolean found = false;
        for (Iterator<Entity> i = q.asIterator(); i.hasNext();) {
            found = true;
            assertThat(i.next().getKind(), is("Hoge"));
        }
        assertThat(found, is(true));
    }
}