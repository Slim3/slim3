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

import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * @author higa
 * 
 */
public class SpikeTest extends DatastoreTestCase {

    /**
     * @throws Exception
     */
    public void testSpike() throws Exception {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key parentKey = ds.put(new Entity("parent"));
        Entity child1 = new Entity("child", parentKey);
        child1.setProperty("name", "child1");
        ds.put(child1);
        Entity child2 = new Entity("child", parentKey);
        child2.setProperty("name", "child2");
        ds.put(child2);
        System.out.println(ds.prepare(
            new Query("child", parentKey).addFilter(
                "name",
                FilterOperator.EQUAL,
                "child1")).asList(FetchOptions.Builder.withOffset(0)));
    }
}
