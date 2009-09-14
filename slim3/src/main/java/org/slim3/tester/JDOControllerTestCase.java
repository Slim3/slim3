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

import org.slim3.jdo.CurrentPersistenceManager;
import org.slim3.jdo.ModelMeta;
import org.slim3.jdo.PMF;
import org.slim3.jdo.SelectQuery;

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
    protected DatastoreTester datastoreTester;

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
        try {
            Class
                .forName("com.google.appengine.tools.development.ApiProxyLocal");
            datastoreTester = new DatastoreTester();
            datastoreTester.setUp();
        } catch (Throwable ignore) {
        }
        CurrentPersistenceManager.set(PMF.get().getPersistenceManager());
        pm = CurrentPersistenceManager.getProxy();
    }

    /**
     * @throws Exception
     * 
     */
    @Override
    protected void tearDown() throws Exception {
        CurrentPersistenceManager.close();
        if (datastoreTester != null) {
            datastoreTester.tearDown();
        }
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
        return new SelectQuery<M>(pm, modelMeta.getModelClass());
    }

    /**
     * Creates a new {@link SelectQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @return a new {@link SelectQuery}
     */
    protected <M> SelectQuery<M> from(Class<M> modelClass) {
        return new SelectQuery<M>(pm, modelClass);
    }

    /**
     * Makes the model persistent in transaction.
     * 
     * @param <T>
     *            the model type
     * @param model
     *            the model
     * @return the persistent model
     */
    protected <T> T makePersistentInTx(T model) {
        begin();
        T t = pm.makePersistent(model);
        commit();
        return t;
    }

    /**
     * Deletes the persistent model from the data store in transaction.
     * 
     * @param model
     *            the model
     */
    protected void deletePersistentInTx(Object model) {
        begin();
        pm.deletePersistent(model);
        commit();
    }

    /**
     * Counts the number of the model.
     * 
     * @param modelClass
     *            the model class
     * @return the number of the model
     */
    protected int count(Class<?> modelClass) {
        return from(modelClass).getResultList().size();
    }

    /**
     * Begins the current transaction.
     */
    protected void begin() {
        pm.currentTransaction().begin();
    }

    /**
     * Rolls back the current transaction.
     */
    protected void commit() {
        pm.currentTransaction().commit();
    }

    /**
     * Rolls back transaction.
     */
    protected void rollback() {
        if (pm.currentTransaction().isActive()) {
            pm.currentTransaction().rollback();
        }
    }

    /**
     * Reopens the current persistence manager.
     */
    protected void reopenPersistenceManager() {
        CurrentPersistenceManager.reopen();
    }
}