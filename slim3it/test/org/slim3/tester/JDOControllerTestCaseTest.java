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
import javax.jdo.Transaction;

import junit.framework.TestCase;

import org.slim3.controller.ControllerConstants;

import slim3.it.model.SampleMeta;

/**
 * @author higa
 * 
 */
public class JDOControllerTestCaseTest extends TestCase {

    private MyTestCase testCase = new MyTestCase();

    @Override
    protected void setUp() throws Exception {
        System.setProperty(
            ControllerConstants.CONTROLLER_PACKAGE_KEY,
            getClass().getPackage().getName() + ".controller");
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        System.clearProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY);
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testSetup() throws Exception {
        testCase.setUp();
        assertNotNull(testCase.pm);
        assertNotNull(testCase.tx);
    }

    /**
     * @throws Exception
     */
    public void testTearDown() throws Exception {
        testCase.setUp();
        testCase.tearDown();
        assertNull(testCase.pm);
        assertNull(testCase.tx);
    }

    /**
     * @throws Exception
     */
    public void testFrom() throws Exception {
        testCase.setUp();
        assertNotNull(testCase.from(new SampleMeta()));
    }

    /**
     * @throws Exception
     */
    public void testRefreshPersistenceManager() throws Exception {
        testCase.setUp();
        PersistenceManager pm = testCase.pm;
        Transaction tx = testCase.tx;
        testCase.refreshPersistenceManager();
        assertNotSame(pm, testCase.pm);
        assertNotSame(tx, testCase.tx);
    }

    private static class MyTestCase extends JDOControllerTestCase {
    }
}