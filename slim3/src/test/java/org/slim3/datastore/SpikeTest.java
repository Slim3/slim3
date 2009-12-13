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

import org.junit.Test;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;

/**
 * @author higa
 * 
 */
public class SpikeTest extends LocalServiceTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void spike() throws Exception {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity("Hoge");
        entity.setProperty("aaa", "222");
        ds.put(entity);
        entity = new Entity("Hoge");
        entity.setProperty("aaa", "111");
        ds.put(entity);
        PreparedQuery pq = ds.prepare(new Query("Hoge").addSort("aaa"));
        QueryResultList<Entity> list =
            pq.asQueryResultList(FetchOptions.Builder.withLimit(1));
        System.out.println(list.size());
        System.out.println(list.get(0));
        Cursor cursor = list.getCursor();
        QueryResultList<Entity> list2 =
            pq.asQueryResultList(FetchOptions.Builder.withCursor(cursor));
        System.out.println(list2.size());
        System.out.println(list2.get(0));

    }
}