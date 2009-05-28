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

import org.slim3.controller.ControllerConstants;

import slim3.it.model.Blog;
import slim3.it.model.BlogMeta;

/**
 * @author higa
 * 
 */
public class JDOControllerTestCaseTest extends JDOControllerTestCase {

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
    public void testFrom() throws Exception {
        assertNotNull(from(new BlogMeta()));
        assertNotNull(from(Blog.class));
    }

    /**
     * @throws Exception
     */
    public void testSetUpStart() throws Exception {
        PersistenceManager pm2 = pm;
        Transaction tx2 = tx;
        setUpStart();
        assertNotSame(pm2, pm);
        assertNotSame(tx2, tx);
    }

    /**
     * @throws Exception
     */
    public void testRefreshPersistenceManager() throws Exception {
        PersistenceManager pm2 = pm;
        Transaction tx2 = tx;
        refreshPersistenceManager();
        assertNotSame(pm2, pm);
        assertNotSame(tx2, tx);
    }

    /**
     * @throws Exception
     */
    public void testMakePersistentInTx() throws Exception {
        makePersistentInTx(new Blog());
        assertEquals(1, count(Blog.class));
    }

    /**
     * @throws Exception
     */
    public void testDeletePersistentInTx() throws Exception {
        Blog blog = new Blog();
        makePersistentInTx(blog);
        deletePersistentInTx(blog);
        assertEquals(0, count(Blog.class));
    }
}