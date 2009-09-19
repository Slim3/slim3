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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

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
        assertNull(ds.getCurrentTransaction(null));
        Transaction tx = ds.beginTransaction();
        assertNotNull(tx);
        assertSame(tx, ds.getCurrentTransaction(null));
        assertTrue(tx.isActive());
        tx.rollback();
        assertFalse(tx.isActive());
        Entity entity = new Entity("Parent");
        Key parentKey = ds.put(entity);
        Key childKey = KeyFactory.createKey(parentKey, "Child", 1);
        ds.put(new Entity(childKey));
        ds.put(new Entity(KeyFactory.createKey(parentKey, "Child", 1)));
    }
}
