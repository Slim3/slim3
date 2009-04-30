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
package org.slim3.gae.unit;

import junit.framework.TestCase;

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
    protected DatastoreTester datastoreTester = new DatastoreTester();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        datastoreTester.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        datastoreTester.tearDown();
        super.tearDown();
    }
}
