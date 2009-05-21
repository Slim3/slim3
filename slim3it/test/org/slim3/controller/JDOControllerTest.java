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
package org.slim3.controller;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.slim3.tester.DatastoreTestCase;

import slim3.it.model.Sample;
import slim3.it.model.SampleMeta;

/**
 * @author higa
 * 
 */
public class JDOControllerTest extends DatastoreTestCase {

    private MyController controller = new MyController();

    /**
     * @throws Exception
     */
    public void testSetup() throws Exception {
        controller.setUp();
        assertNotNull(controller.pm);
        assertNotNull(controller.tx);
    }

    /**
     * @throws Exception
     */
    public void testTearDown() throws Exception {
        controller.setUp();
        controller.tearDown();
        assertNull(controller.pm);
        assertNull(controller.tx);
    }

    /**
     * @throws Exception
     */
    public void testFrom() throws Exception {
        controller.setUp();
        assertNotNull(controller.from(new SampleMeta()));
    }

    /**
     * @throws Exception
     */
    public void testRefreshPersistenceManager() throws Exception {
        controller.setUp();
        PersistenceManager pm = controller.pm;
        Transaction tx = controller.tx;
        controller.refreshPersistenceManager();
        assertNotSame(pm, controller.pm);
        assertNotSame(tx, controller.tx);
    }

    /**
     * @throws Exception
     */
    public void testMakePersistentInTx() throws Exception {
        controller.setUp();
        Sample s = new Sample();
        controller.makePersistentInTx(s);
        assertEquals(1, count(Sample.class));
    }

    /**
     * @throws Exception
     */
    public void testDeletePersistentInTx() throws Exception {
        controller.setUp();
        Sample s = new Sample();
        controller.makePersistentInTx(s);
        controller.deletePersistentInTx(s);
        assertEquals(0, count(Sample.class));
    }

    private static class MyController extends JDOController {

        @Override
        protected Navigation run() {
            return null;
        }

    }
}