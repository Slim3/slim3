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

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class DatastoreTest extends DatastoreTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    public void testQuery() throws Exception {
        assertNotNull(Datastore.query(meta));
    }

    /**
     * @throws Exception
     */
    public void testCreateModelMeta() throws Exception {
        ModelMeta<?> modelMeta = Datastore.createModelMeta(Hoge.class);
        assertNotNull(modelMeta);
        assertEquals(Hoge.class, modelMeta.getModelClass());
    }

    /**
     * @throws Exception
     */
    public void testCreateModelMetaForNotFound() throws Exception {
        try {
            Datastore.createModelMeta(getClass());
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testGetModelMeta() throws Exception {
        ModelMeta<?> modelMeta = Datastore.getModelMeta(Hoge.class);
        assertNotNull(modelMeta);
        assertSame(modelMeta, Datastore.getModelMeta(Hoge.class));
    }

    /**
     * @throws Exception
     */
    public void testPut() throws Exception {
        assertNotNull(Datastore.put(new Hoge()));
    }

    /**
     * @throws Exception
     */
    public void testPutForEntity() throws Exception {
        assertNotNull(Datastore.put(new Entity("Hoge")));
    }

    /**
     * @throws Exception
     */
    public void testPutInTx() throws Exception {
        Datastore.beginTransaction();
        Key key = Datastore.put(new Entity("Hoge"));
        assertNotNull(key);
        Datastore.rollback();
        try {
            ds.get(key);
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testBeginTransaction() throws Exception {
        Datastore.beginTransaction();
        assertTrue(ds.getCurrentTransaction().isActive());
    }

    /**
     * @throws Exception
     */
    public void testCommit() throws Exception {
        Datastore.beginTransaction();
        Datastore.commit();
        assertNull(ds.getCurrentTransaction(null));
    }

    /**
     * @throws Exception
     */
    public void testRollback() throws Exception {
        Datastore.beginTransaction();
        Key key = Datastore.put(new Hoge());
        assertNotNull(key);
        Datastore.rollback();
        assertNull(ds.getCurrentTransaction(null));
        try {
            ds.get(key);
            fail();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}