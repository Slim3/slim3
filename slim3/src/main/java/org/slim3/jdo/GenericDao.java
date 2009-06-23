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
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOOptimisticVerificationException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.slim3.util.BeanMap;
import org.slim3.util.BeanUtil;
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
     * The class to copy model attributes to map.
     */
    protected CopyModelToMap<M> modelToMap = new CopyModelToMap<M>() {
        @Override
        public void copy(M model, BeanMap map) {
            BeanUtil.copy(model, map);
        }
    };

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
     * @param pm
     *            the persistence manager
     * @param modelClass
     *            the model class
     */
    public GenericDao(PersistenceManager pm, Class<M> modelClass) {
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
     * Finds all models.
     * 
     * @return all models
     */
    public List<BeanMap> findAllAsMapList() {
        return toMapList(from().getResultList());
    }

    /**
     * Inserts the model.
     * 
     * @param model
     *            the model
     * @return the persisted model
     */
    public M insert(M model) {
        return pm.makePersistent(model);
    }

    /**
     * Inserts the model in transaction.
     * 
     * @param model
     *            the model
     * @return the persisted model
     */
    public M insertInTx(M model) {
        tx.begin();
        M t = pm.makePersistent(model);
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
    public M update(M model) {
        return pm.makePersistent(model);
    }

    /**
     * Updates the model in transaction.
     * 
     * @param model
     *            the model
     * @return the persisted model
     */
    public M updateInTx(M model) {
        tx.begin();
        M t = pm.makePersistent(model);
        tx.commit();
        return t;
    }

    /**
     * Deletes the model.
     * 
     * @param model
     *            the model
     */
    public void delete(M model) {
        pm.deletePersistent(model);
    }

    /**
     * Deletes the model in transaction.
     * 
     * @param model
     *            the model
     */
    public void deleteInTx(M model) {
        tx.begin();
        pm.deletePersistent(model);
        tx.commit();
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
     * Converts the model list to the map list.
     * 
     * @param modelList
     *            the model list
     * @return the map list
     */
    protected List<BeanMap> toMapList(List<M> modelList) {
        return toMapList(modelList, null);
    }

    /**
     * Converts the model list to the map list.
     * 
     * @param modelList
     *            the model list
     * @param copy
     *            the class to copy model attributes to map
     * @return the map list
     */
    protected List<BeanMap> toMapList(List<M> modelList, CopyModelToMap<M> copy) {
        if (modelList == null) {
            throw new NullPointerException("The modelList parameter is null.");
        }
        if (copy == null) {
            copy = modelToMap;
        }
        List<BeanMap> mapList = new ArrayList<BeanMap>(modelList.size());
        for (M model : modelList) {
            BeanMap map = new BeanMap();
            copy.copy(model, map);
            mapList.add(map);
        }
        return mapList;
    }
}