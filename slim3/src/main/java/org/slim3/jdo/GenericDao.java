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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOOptimisticVerificationException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.slim3.util.LongUtil;

/**
 * A generic dao.
 * 
 * @author higa
 * @param <M>
 *            the model type.
 * @since 3.0
 * 
 */
public class GenericDao<M> {

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
    protected Class<M> modelClass;

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     */
    public GenericDao(Class<M> modelClass) {
        this(modelClass, CurrentPersistenceManager.getAndCheckPresence());
    }

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * @param pm
     *            the persistence manager
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the pm parameter is
     *             null
     */
    public GenericDao(Class<M> modelClass, PersistenceManager pm)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        if (pm == null) {
            throw new NullPointerException("The pm parameter is null.");
        }
        this.modelClass = modelClass;
        this.pm = pm;
        tx = pm.currentTransaction();
    }

    /**
     * Finds a model by key.
     * 
     * @param key
     *            the key
     * @return a model
     */
    public M find(String key) {
        return pm.getObjectById(modelClass, key);
    }

    /**
     * Finds a model by key and checks the version.
     * 
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model
     */
    public M find(String key, long version) {
        M model = pm.getObjectById(modelClass, key);
        if (version != LongUtil.toPrimitiveLong(JDOHelper.getVersion(model))) {
            throw new JDOOptimisticVerificationException(
                "Failed optimistic lock by key("
                    + key
                    + ") and version("
                    + version
                    + ").",
                model);
        }
        return model;
    }

    /**
     * Finds all models.
     * 
     * @return all models
     */
    public List<M> findAll() {
        return from().getResultList();
    }

    /**
     * Makes the model persistent.
     * 
     * @param model
     *            the model
     * @return the persisted model
     */
    public M makePersistent(M model) {
        return pm.makePersistent(model);
    }

    /**
     * Makes the models persistent.
     * 
     * @param models
     *            the models
     * @return the persisted model
     */
    public List<M> makePersistentAll(List<M> models) {
        return (List<M>) pm.makePersistentAll(models);
    }

    /**
     * Makes the model persistent in transaction.
     * 
     * @param model
     *            the model
     * @return the persisted model
     */
    public M makePersistentInTx(M model) {
        begin();
        M t = makePersistent(model);
        commit();
        return t;
    }

    /**
     * Deletes the persistent model from the data store.
     * 
     * @param model
     *            the model
     */
    public void deletePersistent(M model) {
        pm.deletePersistent(model);
    }

    /**
     * Deletes the persistent models from the data store.
     * 
     * @param models
     *            the models
     */
    public void deletePersistentAll(List<M> models) {
        pm.deletePersistentAll(models);
    }

    /**
     * Deletes the persistent model from the data store in transaction.
     * 
     * @param model
     *            the model
     */
    public void deletePersistentInTx(M model) {
        begin();
        deletePersistent(model);
        commit();
    }

    /**
     * Begins transaction.
     */
    public void begin() {
        tx.begin();
    }

    /**
     * Commits transaction.
     */
    public void commit() {
        tx.commit();
    }

    /**
     * Rolls back transaction.
     */
    public void rollback() {
        if (tx.isActive()) {
            tx.rollback();
        }
    }

    /**
     * Creates {@link SelectQuery}.
     * 
     * @return {@link SelectQuery}
     */
    protected SelectQuery<M> from() {
        return new SelectQuery<M>(pm, modelClass);
    }

    /**
     * Sorts the list.
     * 
     * @param list
     *            the list
     * @param criteria
     *            criteria to sort
     * @return the sorted list
     * @throws NullPointerException
     *             if the list parameter is null
     */
    @SuppressWarnings("unchecked")
    protected List<M> sort(List<M> list, OrderCriterion... criteria)
            throws NullPointerException {
        if (list == null) {
            throw new NullPointerException("The list parameter is null.");
        }
        if (criteria.length == 0) {
            return list;
        }
        Collections.sort(list, new AttributeComparator(criteria));
        return list;
    }

    /**
     * Filters the list.
     * 
     * @param list
     *            the list
     * @param criteria
     *            the filter criteria
     * @return the filtered list.
     * @throws NullPointerException
     *             if the list parameter is null or if the model is null
     */
    protected List<M> filter(List<M> list, FilterCriterion... criteria)
            throws NullPointerException {
        if (list == null) {
            throw new NullPointerException("The list parameter is null.");
        }
        if (criteria.length == 0) {
            return list;
        }
        List<M> newList = new ArrayList<M>(list.size());
        outer: for (M model : list) {
            if (model == null) {
                throw new NullPointerException("The model is null.");
            }
            for (FilterCriterion c : criteria) {
                if (c == null) {
                    continue;
                }
                if (!c.accept(model)) {
                    continue outer;
                }
            }
            newList.add(model);
        }
        return newList;
    }
}