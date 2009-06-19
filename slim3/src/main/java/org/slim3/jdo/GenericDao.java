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
package org.slim3.jdo;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

/**
 * A generic dao.
 * 
 * @author higa
 * @param <T>
 *            the model type.
 * @since 3.0
 * 
 */
public class GenericDao<T> {

    /**
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * The transaction.
     */
    protected Transaction tx;

    /**
     * The model class.
     */
    protected Class<T> modelClass;

    /**
     * Constructor.
     * 
     * @param pm
     *            the persistence manager
     * @param modelClass
     *            the model class
     */
    public GenericDao(PersistenceManager pm, Class<T> modelClass) {
        if (pm == null) {
            throw new NullPointerException("The pm parameter is null.");
        }
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        this.pm = pm;
        tx = pm.currentTransaction();
        this.modelClass = modelClass;
    }

    /**
     * Finds a model by key.
     * 
     * @param key
     *            the key
     * @return a model
     */
    public T find(String key) {
        return pm.getObjectById(modelClass, key);
    }

    /**
     * Inserts the model.
     * 
     * @param model
     *            the model
     * @return the persisted model
     */
    public T insert(T model) {
        return pm.makePersistent(model);
    }

    /**
     * Inserts the model in transaction.
     * 
     * @param model
     *            the model
     * @return the persisted model
     */
    public T insertInTx(T model) {
        tx.begin();
        T t = pm.makePersistent(model);
        tx.commit();
        return t;
    }

    /**
     * Updates the model
     * 
     * @param model
     *            the model
     * @return the persisted model
     */
    public T update(T model) {
        return pm.makePersistent(model);
    }

    /**
     * Updates the model in transaction.
     * 
     * @param model
     *            the model
     * @return the persisted model
     */
    public T updateInTx(T model) {
        tx.begin();
        T t = pm.makePersistent(model);
        tx.commit();
        return t;
    }

    /**
     * Deletes the model.
     * 
     * @param model
     *            the model
     */
    public void delete(T model) {
        pm.deletePersistent(model);
    }

    /**
     * Deletes the model in transaction.
     * 
     * @param model
     *            the model
     */
    public void deleteInTx(T model) {
        tx.begin();
        pm.deletePersistent(model);
        tx.commit();
    }

    /**
     * Creates {@link SelectQuery}.
     * 
     * @return {@link SelectQuery}
     */
    protected SelectQuery<T> from() {
        return new SelectQuery<T>(pm, modelClass);
    }
}