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

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.QueryResultIterator;

/**
 * @author @kissrobber
 * 
 */
public class S3QueryResultIteratorTest extends AppEngineTestCase {

    private AsyncDatastoreService ds =
        DatastoreServiceFactory.getAsyncDatastoreService();

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    @Test
    public void getCursor() throws Exception {
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(ds, meta);
        QueryResultIterator<Entity> iterator =
            query.asQueryResultEntityIterator();
        S3QueryResultIterator<Hoge> s3QueryResultIterator =
            new S3QueryResultIterator<Hoge>(iterator, meta, query
                .getEncodedFilters(), query.getEncodedSorts());
        assertThat(s3QueryResultIterator.getEncodedCursor(), is(notNullValue()));
        assertThat(
            s3QueryResultIterator.getEncodedFilters(),
            is(notNullValue()));
        assertThat(s3QueryResultIterator.getEncodedSorts(), is(notNullValue()));
    }

}
