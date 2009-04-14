/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.gae.jdo;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.slim3.gae.unit.LocalDatastoreTestCase;

/**
 * @author higa
 * 
 */
public class JDOTemplateTest extends LocalDatastoreTestCase {

    private PersistenceManager persistenceManager;

    private Transaction transaction;

    private boolean active = false;

    /**
     * @throws Exception
     */
    public void testExecuteForVoid() throws Exception {
        assertNull(new JDOTemplate<Void>() {
            @Override
            public Void doExecute() {
                return null;
            }
        }.execute());
    }

    /**
     * @throws Exception
     */
    public void testExecuteForPersistenceManager() throws Exception {
        new JDOTemplate<Void>() {
            @Override
            public Void doExecute() {
                persistenceManager = pm;
                return null;
            }
        }.execute();
        assertNotNull(persistenceManager);
    }
    
    /**
     * @throws Exception
     */
    public void testExecuteForTransaction() throws Exception {
        new JDOTemplate<Void>() {
            @Override
            public Void doExecute() {
                transaction = tx;
                active = tx.isActive();
                return null;
            }
        }.execute();
        assertNotNull(transaction);
        assertTrue(active);
    }
}