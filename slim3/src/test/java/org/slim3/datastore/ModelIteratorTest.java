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
import org.slim3.datastore.meta.AaaMeta;
import org.slim3.datastore.meta.BbbMeta;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Aaa;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * @author higa
 * 
 */
public class ModelIteratorTest extends AppEngineTestCase {

    private AsyncDatastoreService ds =
        DatastoreServiceFactory.getAsyncDatastoreService();

    private HogeMeta meta = new HogeMeta();

    private AaaMeta aaaMeta = AaaMeta.get();

    private BbbMeta bbbMeta = BbbMeta.get();

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
    @Test
    public void hasNext() throws Exception {
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        EntityQuery query = new EntityQuery(ds, "Hoge");
        Iterator<Entity> iterator = query.asIterator();
        ModelIterator<Hoge> modelIterator =
            new ModelIterator<Hoge>(iterator, meta);
        assertThat(modelIterator.hasNext(), is(true));
        iterator.next();
        assertThat(modelIterator.hasNext(), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void next() throws Exception {
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        EntityQuery query = new EntityQuery(ds, "Hoge");
        Iterator<Entity> iterator = query.asIterator();
        ModelIterator<Hoge> modelIterator =
            new ModelIterator<Hoge>(iterator, meta);
        Hoge hoge = modelIterator.next();
        assertThat(hoge, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void remove() throws Exception {
        DatastoreUtil.put(ds, null, new Entity("Hoge"));
        EntityQuery query = new EntityQuery(ds, "Hoge");
        Iterator<Entity> iterator = query.asIterator();
        ModelIterator<Hoge> modelIterator =
            new ModelIterator<Hoge>(iterator, meta);
        modelIterator.remove();
    }

    /**
     * @throws Exception
     */
    @Test
    public void nextForPolyModel() throws Exception {
        DatastoreUtil.put(ds, null, aaaMeta.modelToEntity(new Aaa()));
        DatastoreUtil.put(ds, null, bbbMeta.modelToEntity(new Bbb()));

        EntityQuery query = new EntityQuery(ds, "Aaa");
        Iterator<Entity> entityIterator = query.asIterator();
        ModelIterator<Aaa> modelIterator =
            new ModelIterator<Aaa>(entityIterator, aaaMeta);
        assertThat(modelIterator.next().getClass().getName(), is(Aaa.class
            .getName()));
        assertThat(modelIterator.next().getClass().getName(), is(Bbb.class
            .getName()));
        assertThat(modelIterator.hasNext(), is(false));
    }
}