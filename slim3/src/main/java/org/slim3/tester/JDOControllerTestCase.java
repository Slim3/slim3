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

import javax.jdo.PersistenceManager;

import org.slim3.jdo.PMF;

/**
 * A test case for Slim3 Controller and JDO.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class JDOControllerTestCase extends ControllerTestCase {

    /**
     * The tester for local data store.
     */
    protected DatastoreTester datastoreTester = new DatastoreTester();

    /**
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * @throws Exception
     * 
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datastoreTester.setUp();
        pm = PMF.get().getPersistenceManager();
    }

    /**
     * @throws Exception
     * 
     */
    @Override
    protected void tearDown() throws Exception {
        pm.close();
        pm = null;
        datastoreTester.tearDown();
        super.tearDown();
    }
}