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
package org.slim3.datastore;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slim3.util.ClassUtil;
import org.slim3.util.Cleanable;
import org.slim3.util.Cleaner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * A class to access datastore.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class Datastore {

    static final int MAX_RETRY = 10;

    private static Logger logger = Logger.getLogger(Datastore.class.getName());

    private static ConcurrentHashMap<String, ModelMeta<?>> modelMetaCache =
        new ConcurrentHashMap<String, ModelMeta<?>>(87);

    private static volatile boolean initialized = false;

    static {
        initialize();
    }

    private static void initialize() {
        Cleaner.add(new Cleanable() {
            public void clean() {
                modelMetaCache.clear();
                initialized = false;
            }
        });
        initialized = true;
    }

    /**
     * Begins the current transaction.
     */
    public static void beginTransaction() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.beginTransaction();
    }

    /**
     * Commits the current transaction.
     */
    public static void commit() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Transaction tx = ds.getCurrentTransaction(null);
        if (tx != null && tx.isActive()) {
            commitInternal(tx);
        }
    }

    private static void commitInternal(Transaction tx) {
        try {
            tx.commit();
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    tx.commit();
                    return;
                } catch (DatastoreTimeoutException e2) {
                    logger.log(Level.WARNING, "Retry("
                        + i
                        + "): "
                        + e2.getMessage(), e2);
                }
            }
            throw e;
        }
    }

    /**
     * Rolls back the current transaction.
     */
    public static void rollback() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Transaction tx = ds.getCurrentTransaction(null);
        if (tx != null && tx.isActive()) {
            rollbackInternal(tx);
        }
    }

    private static void rollbackInternal(Transaction tx) {
        try {
            tx.rollback();
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    tx.rollback();
                    return;
                } catch (DatastoreTimeoutException e2) {
                    logger.log(Level.WARNING, "Retry("
                        + i
                        + "): "
                        + e2.getMessage(), e2);
                }
            }
            throw e;
        }
    }

    /**
     * Returns a {@link SelectQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @return a {@link SelectQuery}
     */
    public static final <M> SelectQuery<M> query(ModelMeta<M> modelMeta) {
        return new SelectQuery<M>(modelMeta);
    }

    /**
     * Returns an entity specified by the key.
     * 
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static Entity get(Key key) throws NullPointerException,
            EntityNotFoundRuntimeException {
        if (key == null) {
            throw new NullPointerException("The key parameter is null.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Transaction tx = ds.getCurrentTransaction(null);
        return getInternal(ds, key, tx);
    }

    private static Entity getInternal(DatastoreService ds, Key key,
            Transaction tx) {
        try {
            try {
                if (tx != null && tx.isActive()) {
                    return ds.get(tx, key);
                }
                return ds.get(key);
            } catch (DatastoreTimeoutException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                for (int i = 0; i < MAX_RETRY; i++) {
                    try {
                        if (tx != null && tx.isActive()) {
                            return ds.get(tx, key);
                        }
                        return ds.get(key);
                    } catch (DatastoreTimeoutException e2) {
                        logger.log(Level.WARNING, "Retry("
                            + i
                            + "): "
                            + e2.getMessage(), e2);
                    }
                }
                throw e;
            }
        } catch (EntityNotFoundException cause) {
            throw new EntityNotFoundRuntimeException(key, cause);
        }
    }

    /**
     * Puts the model to datastore.
     * 
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     */
    public static Key put(Object model) throws NullPointerException {
        if (model == null) {
            throw new NullPointerException("The model parameter is null.");
        }
        ModelMeta<?> modelMeta = getModelMeta(model.getClass());
        Entity entity = modelMeta.modelToEntity(model);
        return put(entity);
    }

    /**
     * Puts the entity to datastore.
     * 
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public static Key put(Entity entity) throws NullPointerException {
        if (entity == null) {
            throw new NullPointerException("The entity parameter is null.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Transaction tx = ds.getCurrentTransaction(null);
        return putInternal(ds, entity, tx);
    }

    private static Key putInternal(DatastoreService ds, Entity entity,
            Transaction tx) {
        try {
            if (tx != null && tx.isActive()) {
                return ds.put(tx, entity);
            }
            return ds.put(entity);
        } catch (DatastoreTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            for (int i = 0; i < MAX_RETRY; i++) {
                try {
                    if (tx != null && tx.isActive()) {
                        return ds.put(tx, entity);
                    }
                    return ds.put(entity);
                } catch (DatastoreTimeoutException e2) {
                    logger.log(Level.WARNING, "Retry("
                        + i
                        + "): "
                        + e2.getMessage(), e2);
                }
            }
            throw e;
        }
    }

    /**
     * Returns a meta data of the model.
     * 
     * @param modelClass
     *            the model class
     * @return a meta data of the model
     */
    protected static ModelMeta<?> getModelMeta(Class<?> modelClass) {
        if (!initialized) {
            initialize();
        }
        ModelMeta<?> modelMeta = modelMetaCache.get(modelClass.getName());
        if (modelMeta != null) {
            return modelMeta;
        }
        modelMeta = createModelMeta(modelClass);
        ModelMeta<?> old =
            modelMetaCache.putIfAbsent(modelClass.getName(), modelMeta);
        return old != null ? old : modelMeta;
    }

    /**
     * Creates a new meta data of the model.
     * 
     * @param modelClass
     *            the model class
     * @return a new meta data of the model
     */
    protected static ModelMeta<?> createModelMeta(Class<?> modelClass) {
        try {
            String metaClassName =
                modelClass.getName().replace(".model.", ".meta.") + "Meta";
            return ClassUtil.newInstance(metaClassName, Thread
                .currentThread()
                .getContextClassLoader());
        } catch (Throwable cause) {
            throw new IllegalArgumentException("The meta data of the model("
                + modelClass.getName()
                + ") is not found.");
        }
    }

    private Datastore() {
    }
}