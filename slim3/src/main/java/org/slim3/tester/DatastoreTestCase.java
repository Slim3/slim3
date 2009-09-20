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
package org.slim3.tester;

import junit.framework.TestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 * A test case for local data store.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class DatastoreTestCase extends TestCase {

    /**
     * The tester for local data store.
     */
    protected DatastoreTester datastoreTester;

    /**
     * The datastore service.
     */
    protected DatastoreService ds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datastoreTester = new DatastoreTester();
        datastoreTester.setUp();
        ds = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    protected void tearDown() throws Exception {
        datastoreTester.tearDown();
        super.tearDown();
    }
}