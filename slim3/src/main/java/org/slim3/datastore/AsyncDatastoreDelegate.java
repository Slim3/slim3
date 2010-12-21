/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slim3.util.DoubleUtil;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceConfig;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.utils.FutureWrapper;

/**
 * A delegate to access asynchronous datastore service.
 * 
 * @author higa
 * @since 1.0.6
 * 
 */
public class AsyncDatastoreDelegate {

    /**
     * The key of deadline.
     */
    public static final String DEADLINE = "slim3.datastoreDeadline";

    /**
     * The deadline(seconds).
     */
    protected Double deadline;

    /**
     * The asynchronous datastore service.
     */
    protected AsyncDatastoreService ds;

    /**
     * The datastore service configuration.
     */
    protected DatastoreServiceConfig dsConfig;

    /**
     * Constructor.
     */
    public AsyncDatastoreDelegate() {
        this(null);
    }

    /**
     * Constructor.
     * 
     * @param deadline
     *            the deadline
     */
    public AsyncDatastoreDelegate(Double deadline) {
        if (deadline == null) {
            deadline = DoubleUtil.toDouble(System.getProperty(DEADLINE));
        }
        this.deadline = deadline;
        setUp();
    }

    /**
     * Returns the deadline.
     * 
     * @return the deadline
     */
    protected Double getDeadline() {
        return deadline;
    }

    /**
     * Returns the asynchronous datastore service.
     * 
     * @return the asynchronous datastore service
     */
    public AsyncDatastoreService getAsyncDatastoreService() {
        return ds;
    }

    /**
     * Creates datastore services.
     * 
     */
    protected void setUp() {
        dsConfig = DatastoreServiceConfig.Builder.withDefaults();
        if (deadline != null) {
            dsConfig.deadline(deadline);
        }
        ds = DatastoreServiceFactory.getAsyncDatastoreService(dsConfig);
    }

    /**
     * Begins a transaction asynchronously.
     * 
     * @return a begun transaction represented as {@link Future}
     */
    public Future<Transaction> beginTransactionAsync() {
        return ds.beginTransaction();
    }

    /**
     * Allocates keys within a namespace defined by the kind asynchronously.
     * 
     * @param kind
     *            the kind
     * @param num
     *            the number of allocated keys
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public Future<KeyRange> allocateIdsAsync(String kind, long num)
            throws NullPointerException {
        return DatastoreUtil.allocateIdsAsync(ds, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model
     * asynchronously.
     * 
     * @param modelClass
     *            the model class
     * @param num
     *            the number of allocated keys
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public Future<KeyRange> allocateIdsAsync(Class<?> modelClass, long num)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateIdsAsync(DatastoreUtil.getModelMeta(modelClass), num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model
     * asynchronously.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param num
     *            the number of allocated keys
     * @return keys represented as {@link Future}
     */
    public Future<KeyRange> allocateIdsAsync(ModelMeta<?> modelMeta, long num) {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateIdsAsync(modelMeta.getKind(), num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @param num
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public Future<KeyRange> allocateIdsAsync(Key parentKey, String kind, int num)
            throws NullPointerException {
        return DatastoreUtil.allocateIdsAsync(ds, parentKey, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param num
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public Future<KeyRange> allocateIdsAsync(Key parentKey,
            Class<?> modelClass, int num) throws NullPointerException {
        return allocateIdsAsync(parentKey, DatastoreUtil
            .getModelMeta(modelClass), num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param num
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public Future<KeyRange> allocateIdsAsync(Key parentKey,
            ModelMeta<?> modelMeta, int num) throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateIdsAsync(parentKey, modelMeta.getKind(), num);
    }

    /**
     * Returns an entity specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Future<Entity> getAsync(Key key) throws NullPointerException {
        return getAsync(ds.getCurrentTransaction(null), key);
    }

    /**
     * Returns a model specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getAsync(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getAsync(DatastoreUtil.getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * 
     */
    public <M> Future<M> getAsync(final ModelMeta<M> modelMeta, final Key key)
            throws NullPointerException {
        return getAsync(ds.getCurrentTransaction(null), modelMeta, key);
    }

    /**
     * Returns an entity specified by the key asynchronously. Returns null if no
     * entity is found. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Future<Entity> getOrNullAsync(Key key) throws NullPointerException {
        return getOrNullAsync(ds.getCurrentTransaction(null), key);
    }

    /**
     * Returns a model specified by the key asynchronously. Returns null if no
     * entity is found. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getOrNullAsync(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNullAsync(DatastoreUtil.getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key asynchronously. Returns null if no
     * entity is found. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * 
     */
    public <M> Future<M> getOrNullAsync(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNullAsync(ds.getCurrentTransaction(null), modelMeta, key);
    }

    /**
     * Returns an entity specified by the key without transaction
     * asynchronously.
     * 
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Future<Entity> getWithoutTxAsync(Key key)
            throws NullPointerException {
        return getAsync((Transaction) null, key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getWithoutTxAsync(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getWithoutTxAsync(DatastoreUtil.getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * 
     */
    public <M> Future<M> getWithoutTxAsync(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException {
        return getAsync(null, modelMeta, key);
    }

    /**
     * Returns an entity specified by the key without transaction
     * asynchronously.
     * 
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Future<Entity> getOrNullWithoutTxAsync(Key key)
            throws NullPointerException {
        return getOrNullAsync((Transaction) null, key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getOrNullWithoutTxAsync(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNullWithoutTxAsync(
            DatastoreUtil.getModelMeta(modelClass),
            key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getOrNullWithoutTxAsync(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNullAsync((Transaction) null, modelMeta, key);
    }

    /**
     * Returns a model specified by the key and checks the version
     * asynchronously. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     */
    public <M> Future<M> getAsync(Class<M> modelClass, Key key, Long version)
            throws NullPointerException {
        return getAsync(DatastoreUtil.getModelMeta(modelClass), key, version);
    }

    /**
     * Returns a model specified by the key and checks the version
     * asynchronously. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public <M> Future<M> getAsync(ModelMeta<M> modelMeta, Key key, Long version)
            throws NullPointerException, IllegalArgumentException,
            ConcurrentModificationException {
        return getAsync(ds.getCurrentTransaction(null), modelMeta, key, version);
    }

    /**
     * Returns an entity specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Entity> getAsync(Transaction tx, final Key key)
            throws NullPointerException, IllegalStateException {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        return new FutureWrapper<Map<Key, Entity>, Entity>(getAsMapAsync(
            tx,
            Arrays.asList(key))) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected Entity wrap(Map<Key, Entity> map) throws Exception {
                Entity entity = map.get(key);
                if (entity == null) {
                    throw new EntityNotFoundRuntimeException(key);
                }
                return entity;
            }
        };
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<M> getAsync(Transaction tx, Class<M> modelClass, Key key)
            throws NullPointerException, IllegalStateException {
        return getAsync(tx, DatastoreUtil.getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<M> getAsync(Transaction tx, final ModelMeta<M> modelMeta,
            final Key key) throws NullPointerException, IllegalStateException {
        return new FutureWrapper<Entity, M>(getAsync(tx, key)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected M wrap(Entity entity) throws Exception {
                ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
                mm.validateKey(key);
                return mm.entityToModel(entity);
            }
        };
    }

    /**
     * Returns an entity specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Entity> getOrNullAsync(Transaction tx, final Key key)
            throws NullPointerException, IllegalStateException {
        return new FutureWrapper<Map<Key, Entity>, Entity>(getAsMapAsync(
            tx,
            key)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected Entity wrap(Map<Key, Entity> map) throws Exception {
                return map.get(key);
            }
        };
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<M> getOrNullAsync(Transaction tx, Class<M> modelClass,
            Key key) throws NullPointerException, IllegalStateException {
        return getOrNullAsync(tx, DatastoreUtil.getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<M> getOrNullAsync(Transaction tx,
            final ModelMeta<M> modelMeta, final Key key)
            throws NullPointerException, IllegalStateException {
        return new FutureWrapper<Map<Key, Entity>, M>(getAsMapAsync(tx, key)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected M wrap(Map<Key, Entity> map) throws Exception {
                Entity entity = map.get(key);
                if (entity == null) {
                    return null;
                }
                ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
                mm.validateKey(key);
                return mm.entityToModel(entity);
            }
        };
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<M> getAsync(Transaction tx, Class<M> modelClass, Key key,
            Long version) throws NullPointerException, IllegalStateException {
        return getAsync(
            tx,
            DatastoreUtil.getModelMeta(modelClass),
            key,
            version);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<M> getAsync(Transaction tx, final ModelMeta<M> modelMeta,
            final Key key, final Long version) throws NullPointerException,
            IllegalStateException {
        if (version == null) {
            throw new NullPointerException(
                "The version parameter must not be null.");
        }
        return new FutureWrapper<Entity, M>(getAsync(tx, key)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected M wrap(Entity entity) throws Exception {
                ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
                mm.validateKey(key);
                M model = mm.entityToModel(entity);
                if (version != mm.getVersion(model)) {
                    throw new ConcurrentModificationException(
                        "Failed optimistic lock by key("
                            + key
                            + ") and version("
                            + version
                            + ").");
                }
                return model;
            }
        };
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Future<List<Entity>> getAsync(Iterable<Key> keys)
            throws NullPointerException {
        return getAsync(ds.getCurrentTransaction(null), keys);
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * 
     */
    public Future<List<Entity>> getAsync(Key... keys) {
        return getAsync(Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null of if the keys parameter
     *             is null
     */
    public <M> Future<List<M>> getAsync(Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException {
        return getAsync(DatastoreUtil.getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null of if the keys parameter
     *             is null
     * 
     */
    public <M> Future<List<M>> getAsync(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException {
        return getAsync(ds.getCurrentTransaction(null), modelMeta, keys);

    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> Future<List<M>> getAsync(Class<M> modelClass, Key... keys)
            throws NullPointerException {
        return getAsync(modelClass, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<List<M>> getAsync(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException {
        return getAsync(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Future<List<Entity>> getWithoutTxAsync(Iterable<Key> keys)
            throws NullPointerException {
        return getAsync((Transaction) null, keys);
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * 
     */
    public Future<List<Entity>> getWithoutTxAsync(Key... keys) {
        return getWithoutTxAsync(Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null of if the keys parameter
     *             is null
     */
    public <M> Future<List<M>> getWithoutTxAsync(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException {
        return getWithoutTxAsync(DatastoreUtil.getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null of if the keys parameter
     *             is null
     */
    public <M> Future<List<M>> getWithoutTxAsync(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException {
        return getAsync((Transaction) null, modelMeta, keys);

    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> Future<List<M>> getWithoutTxAsync(Class<M> modelClass,
            Key... keys) throws NullPointerException {
        return getWithoutTxAsync(modelClass, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<List<M>> getWithoutTxAsync(ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException {
        return getWithoutTxAsync(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return future entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     * 
     */
    public Future<List<Entity>> getAsync(Transaction tx,
            final Iterable<Key> keys) throws NullPointerException {
        return new FutureWrapper<Map<Key, Entity>, List<Entity>>(getAsMapAsync(
            tx,
            keys)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected List<Entity> wrap(Map<Key, Entity> map) throws Exception {
                return DatastoreUtil.entityMapToEntityList(keys, map);
            }
        };
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<List<Entity>> getAsync(Transaction tx, Key... keys)
            throws IllegalStateException {
        return getAsync(tx, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<List<M>> getAsync(Transaction tx, Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException {
        return getAsync(tx, DatastoreUtil.getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<List<M>> getAsync(Transaction tx,
            final ModelMeta<M> modelMeta, final Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return new FutureWrapper<Map<Key, Entity>, List<M>>(getAsMapAsync(
            tx,
            keys)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected List<M> wrap(Map<Key, Entity> map) throws Exception {
                return DatastoreUtil.entityMapToModelList(modelMeta, keys, map);
            }
        };
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<List<M>> getAsync(Transaction tx, Class<M> modelClass,
            Key... keys) throws NullPointerException, IllegalStateException {
        return getAsync(tx, DatastoreUtil.getModelMeta(modelClass), Arrays
            .asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<List<M>> getAsync(Transaction tx, ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException, IllegalStateException {
        return getAsync(tx, modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Future<Map<Key, Entity>> getAsMapAsync(Iterable<Key> keys)
            throws NullPointerException {
        return getAsMapAsync(ds.getCurrentTransaction(null), keys);
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     */
    public Future<Map<Key, Entity>> getAsMapAsync(Key... keys) {
        return getAsMapAsync(Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException {
        return getAsMapAsync(DatastoreUtil.getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException {
        return getAsMapAsync(ds.getCurrentTransaction(null), modelMeta, keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Class<M> modelClass,
            Key... keys) throws NullPointerException {
        return getAsMapAsync(DatastoreUtil.getModelMeta(modelClass), Arrays
            .asList(keys));
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException {
        return getAsMapAsync(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Future<Map<Key, Entity>> getAsMapWithoutTxAsync(Iterable<Key> keys)
            throws NullPointerException {
        return getAsMapAsync((Transaction) null, keys);
    }

    /**
     * Returns entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     */
    public Future<Map<Key, Entity>> getAsMapWithoutTxAsync(Key... keys) {
        return getAsMapWithoutTxAsync(Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException {
        return getAsMapWithoutTxAsync(
            DatastoreUtil.getModelMeta(modelClass),
            keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(
            ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException {
        return getAsMapAsync((Transaction) null, modelMeta, keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(Class<M> modelClass,
            Key... keys) throws NullPointerException {
        return getAsMapWithoutTxAsync(
            DatastoreUtil.getModelMeta(modelClass),
            Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(
            ModelMeta<M> modelMeta, Key... keys) throws NullPointerException {
        return getAsMapWithoutTxAsync(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Map<Key, Entity>> getAsMapAsync(Transaction tx,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException {
        return DatastoreUtil.getAsMapAsync(ds, tx, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Map<Key, Entity>> getAsMapAsync(Transaction tx, Key... keys)
            throws IllegalStateException {
        return getAsMapAsync(tx, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return getAsMapAsync(tx, DatastoreUtil.getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            final ModelMeta<M> modelMeta, final Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return new FutureWrapper<Map<Key, Entity>, Map<Key, M>>(getAsMapAsync(
            tx,
            keys)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected Map<Key, M> wrap(Map<Key, Entity> map) throws Exception {
                return DatastoreUtil.entityMapToModelMap(modelMeta, map);
            }
        };
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            Class<M> modelClass, Key... keys) throws NullPointerException,
            IllegalStateException {
        return getAsMapAsync(tx, DatastoreUtil.getModelMeta(modelClass), Arrays
            .asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            ModelMeta<M> modelMeta, Key... keys) throws NullPointerException,
            IllegalStateException {
        return getAsMapAsync(tx, modelMeta, Arrays.asList(keys));
    }

    /**
     * Puts the entity to datastore asynchronously. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param entity
     *            the entity
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public Future<Key> putAsync(Entity entity) throws NullPointerException {
        return putAsync(ds.getCurrentTransaction(null), entity);
    }

    /**
     * Puts the entity to datastore without transaction asynchronously.
     * 
     * @param entity
     *            the entity
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public Future<Key> putWithoutTxAsync(Entity entity)
            throws NullPointerException {
        return putAsync((Transaction) null, entity);
    }

    /**
     * Puts the model to datastore asynchronously. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param model
     *            the model
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the model parameter is null
     */
    public Future<Key> putAsync(Object model) throws NullPointerException {
        return putAsync(ds.getCurrentTransaction(null), model);
    }

    /**
     * Puts the model to datastore without transaction asynchronously.
     * 
     * @param model
     *            the model
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the model parameter is null
     */
    public Future<Key> putWithoutTxAsync(Object model)
            throws NullPointerException {
        return putAsync((Transaction) null, model);
    }

    /**
     * Puts the entity to datastore within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param entity
     *            the entity
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the entity parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Key> putAsync(Transaction tx, Entity entity)
            throws NullPointerException, IllegalStateException {
        if (entity == null) {
            throw new NullPointerException(
                "The entity parameter must not be null.");
        }
        return new FutureWrapper<List<Key>, Key>(DatastoreUtil.putAsync(
            ds,
            tx,
            Arrays.asList(entity))) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected Key wrap(List<Key> list) throws Exception {
                return list.get(0);
            }
        };
    }

    /**
     * Puts the model to datastore within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param model
     *            the model
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the model parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Key> putAsync(Transaction tx, Object model)
            throws NullPointerException, IllegalStateException {
        Entity entity = DatastoreUtil.modelToEntity(ds, model);
        return putAsync(tx, entity);
    }

    /**
     * Puts the models or entities to datastore asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     * @throws NullPointerException
     *             if the models parameter is null
     */
    public Future<List<Key>> putAsync(Iterable<?> models)
            throws NullPointerException {
        return putAsync(ds.getCurrentTransaction(null), models);
    }

    /**
     * Puts the models or entities to datastore without transaction
     * asynchronously.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     * @throws NullPointerException
     *             if the models parameter is null
     */
    public Future<List<Key>> putWithoutTxAsync(Iterable<?> models)
            throws NullPointerException {
        return putAsync((Transaction) null, models);
    }

    /**
     * Puts the models or entities to datastore asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     */
    public Future<List<Key>> putAsync(Object... models) {
        return putAsync(Arrays.asList(models));
    }

    /**
     * Puts the models or entities to datastore without transaction
     * asynchronously.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     */
    public Future<List<Key>> putWithoutTxAsync(Object... models) {
        return putWithoutTxAsync(Arrays.asList(models));
    }

    /**
     * Puts the models or entities to datastore within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     * @throws NullPointerException
     *             if the models parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<List<Key>> putAsync(Transaction tx, Iterable<?> models)
            throws NullPointerException, IllegalStateException {
        List<Entity> entities = DatastoreUtil.modelsToEntities(ds, models);
        return DatastoreUtil.putAsync(ds, tx, entities);
    }

    /**
     * Puts the models or entities to datastore within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<List<Key>> putAsync(Transaction tx, Object... models)
            throws IllegalStateException {
        return putAsync(tx, Arrays.asList(models));
    }

    /**
     * Deletes entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Future<Void> deleteAsync(Iterable<Key> keys)
            throws NullPointerException {
        return deleteAsync(ds.getCurrentTransaction(null), keys);
    }

    /**
     * Deletes entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Future<Void> deleteWithoutTxAsync(Iterable<Key> keys)
            throws NullPointerException {
        return deleteAsync((Transaction) null, keys);
    }

    /**
     * Deletes entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     */
    public Future<Void> deleteAsync(Key... keys) {
        return deleteAsync(Arrays.asList(keys));
    }

    /**
     * Deletes entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     */
    public Future<Void> deleteWithoutTxAsync(Key... keys) {
        return deleteWithoutTxAsync(Arrays.asList(keys));
    }

    /**
     * Deletes entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Void> deleteAsync(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return DatastoreUtil.deleteAsync(ds, tx, keys);
    }

    /**
     * Deletes entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Void> deleteAsync(Transaction tx, Key... keys)
            throws IllegalStateException {
        return deleteAsync(tx, Arrays.asList(keys));
    }

    /**
     * Deletes all descendant entities asynchronously.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public Future<Void> deleteAllAsync(Key ancestorKey)
            throws NullPointerException {
        return deleteAsync(query(ancestorKey).asKeyList());
    }

    /**
     * Deletes all descendant entities within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Void> deleteAllAsync(Transaction tx, Key ancestorKey)
            throws NullPointerException, IllegalStateException {
        return deleteAsync(tx, query(ancestorKey).asKeyList());
    }

    /**
     * Deletes all descendant entities without transaction asynchronously.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public Future<Void> deleteAllWithoutTxAsync(Key ancestorKey)
            throws NullPointerException {
        return deleteWithoutTxAsync(query(ancestorKey).asKeyList());
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> ModelQuery<M> query(Class<M> modelClass)
            throws NullPointerException {
        return new ModelQuery<M>(ds, DatastoreUtil.getModelMeta(modelClass));
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public <M> ModelQuery<M> query(ModelMeta<M> modelMeta)
            throws NullPointerException {
        return new ModelQuery<M>(ds, modelMeta);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the ancestorKey
     *             parameter is null
     */
    public <M> ModelQuery<M> query(Class<M> modelClass, Key ancestorKey)
            throws NullPointerException {
        return new ModelQuery<M>(
            ds,
            DatastoreUtil.getModelMeta(modelClass),
            ancestorKey);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the ancestorKey
     *             parameter is null
     */
    public <M> ModelQuery<M> query(ModelMeta<M> modelMeta, Key ancestorKey)
            throws NullPointerException {
        return new ModelQuery<M>(ds, modelMeta, ancestorKey);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the ancestorKey
     *             parameter is null
     */
    public <M> ModelQuery<M> query(Transaction tx, Class<M> modelClass,
            Key ancestorKey) throws NullPointerException {
        return query(tx, DatastoreUtil.getModelMeta(modelClass), ancestorKey);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the ancestorKey
     *             parameter is null
     */
    public <M> ModelQuery<M> query(Transaction tx, ModelMeta<M> modelMeta,
            Key ancestorKey) throws NullPointerException {
        return new ModelQuery<M>(ds, tx, modelMeta, ancestorKey);
    }

    /**
     * Returns an {@link EntityQuery}.
     * 
     * @param kind
     *            the kind
     * @return an {@link EntityQuery}
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public EntityQuery query(String kind) throws NullPointerException {
        return new EntityQuery(ds, kind);
    }

    /**
     * Returns an {@link EntityQuery}.
     * 
     * @param kind
     *            the kind
     * @param ancestorKey
     *            the ancestor key
     * @return an {@link EntityQuery}
     * @throws NullPointerException
     *             if the kind parameter is null or if the ancestorKey parameter
     *             is null
     */
    public EntityQuery query(String kind, Key ancestorKey)
            throws NullPointerException {
        return new EntityQuery(ds, kind, ancestorKey);
    }

    /**
     * Returns an {@link EntityQuery}.
     * 
     * @param tx
     *            the transaction
     * @param kind
     *            the kind
     * @param ancestorKey
     *            the ancestor key
     * @return an {@link EntityQuery}
     * @throws NullPointerException
     *             if the kind parameter is null or if the ancestorKey parameter
     *             is null
     */
    public EntityQuery query(Transaction tx, String kind, Key ancestorKey)
            throws NullPointerException {
        return new EntityQuery(ds, tx, kind, ancestorKey);
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @return a {@link KindlessQuery}
     */
    public KindlessQuery query() {
        return new KindlessQuery(ds);
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link KindlessQuery}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public KindlessQuery query(Key ancestorKey) throws NullPointerException {
        return new KindlessQuery(ds, ancestorKey);
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @param tx
     *            the transaction
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link KindlessQuery}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public KindlessQuery query(Transaction tx, Key ancestorKey)
            throws NullPointerException {
        return new KindlessQuery(ds, tx, ancestorKey);
    }
}