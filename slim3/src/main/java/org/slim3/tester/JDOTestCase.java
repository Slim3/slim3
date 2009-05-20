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

import org.slim3.jdo.ModelMeta;
import org.slim3.jdo.PMF;
import org.slim3.jdo.SelectQuery;

/**
 * A test case for JDO.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class JDOTestCase extends DatastoreTestCase {

    /**
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * The transaction.
     */
    protected Transaction tx;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pm = PMF.get().getPersistenceManager();
        tx = pm.currentTransaction();
    }

    @Override
    protected void tearDown() throws Exception {
        if (tx.isActive()) {
            tx.rollback();
        }
        tx = null;
        pm.close();
        pm = null;
        super.tearDown();
    }

    /**
     * Creates a new {@link SelectQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @return a new {@link SelectQuery}
     */
    protected <M> SelectQuery<M> from(ModelMeta<M> modelMeta) {
        return new SelectQuery<M>(modelMeta, pm);
    }

    /**
     * Refreshes the current persistence manager.
     */
    protected void refreshPersistenceManager() {
        pm.close();
        pm = PMF.get().getPersistenceManager();
        tx = pm.currentTransaction();
    }
}
