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

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * @author higa
 * 
 */
public class CompositeFilterSpikeTest extends AppEngineTestCase {

    private DatastoreService ds =
        DatastoreServiceFactory.getDatastoreService();

    /**
     * @throws Exception
     */
    @Test
    public void and() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("aaa", "111");
        entity.setProperty("bbb", "222");
        ds.put(entity);
        Query query = new Query("Hoge");
        query.setFilter(new CompositeFilter(CompositeFilterOperator.AND,
            Arrays.<com.google.appengine.api.datastore.Query.Filter>asList(
                new Query.FilterPredicate("aaa", FilterOperator.EQUAL, "111"),
                new Query.FilterPredicate("bbb", FilterOperator.EQUAL, "222"))));
        
        assertThat(ds.prepare(query).asList(FetchOptions.Builder.withDefaults()).size(), is(1));
    }
}