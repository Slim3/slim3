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

import java.util.Arrays;
import java.util.List;

import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class SpikeTest extends DatastoreTestCase {

    /**
     * @throws Exception
     */
    public void testSpike() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("name", "1");
        Entity entity2 = new Entity("Hoge");
        entity2.setProperty("name", "3");
        Entity entity3 = new Entity("Hoge");
        entity3.setProperty("name", "2");
        ds.put(Arrays.asList(entity, entity2, entity3));
        List<Entity> list =
            ds
                .prepare(
                    new Query("Hoge").addSort("name", SortDirection.DESCENDING))
                .asList(FetchOptions.Builder.withOffset(0));
        System.out.println(list);
    }
}