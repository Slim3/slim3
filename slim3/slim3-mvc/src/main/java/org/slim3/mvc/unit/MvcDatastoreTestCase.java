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
package org.slim3.mvc.unit;

import junit.framework.TestCase;

import org.slim3.gae.unit.DatastoreTester;

/**
 * A test case for Slim3 MVC and local data store.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class MvcDatastoreTestCase extends TestCase {

    /**
     * The tester for Slim3 MVC.
     */
    protected MvcTester mvcTester = new MvcTester();

    /**
     * The tester for local data store.
     */
    protected DatastoreTester datastoreTester = new DatastoreTester();

    /**
     * @throws Exception
     * 
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mvcTester.setUp();
        datastoreTester.setUp();
    }

    /**
     * @throws Exception
     * 
     */
    @Override
    protected void tearDown() throws Exception {
        datastoreTester.tearDown();
        mvcTester.tearDown();
        super.tearDown();
    }
}