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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.google.appengine.api.datastore.Key;

/**
 * 
 * This is a dao base class.
 * 
 * @author mike
 * @author higa
 * @param <T>
 *            the model type
 * 
 * @since 1.0.10
 * 
 */
public abstract class DaoBase<T> {

    /**
     * The model class.
     */
    protected Class<T> modelClass;

    /**
     * The meta data of model.
     */
    protected ModelMeta<T> m;

    /**
     * Constructor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DaoBase() {
        for (Class<?> c = getClass(); c != Object.class; c = c.getSuperclass()) {
            Type type = c.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass =
                    ((Class) ((ParameterizedType) type)
                        .getActualTypeArguments()[0]);
                break;
            }
        }
        if (modelClass == null) {
            throw new IllegalStateException("No model class is found.");
        }
        m = DatastoreUtil.getModelMeta(modelClass);
    }

    /**
     * Returns the model class.
     * 
     * @return the model class
     */
    public Class<T> getModelClass() {
        return modelClass;
    }

    /**
     * Returns a meta data of model.
     * 
     * @return a meta data of model
     */
    public ModelMeta<T> getModelMeta() {
        return m;
    }

    /**
     * Returns a model.
     * 
     * @param key
     *            the key
     * @return a model
     */
    public T get(Key key) {
        return Datastore.get(modelClass, key);
    }

    /**
     * Returns a model asynchronously.
     * 
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     */
    public Future<T> getAsync(Key key) {
        return Datastore.getAsync(modelClass, key);
    }

    /**
     * Returns a model. Returns null if no entity is found.
     * 
     * @param key
     *            the key
     * @return a model
     */
    public T getOrNull(Key key) {
        return Datastore.getOrNull(modelClass, key);
    }

    /**
     * Returns a model asynchronously.
     * 
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     */
    public Future<T> getOrNullAsync(Key key) {
        return Datastore.getOrNullAsync(modelClass, key);
    }

    /**
     * Determines if a model identified by the key exists
     * 
     * @param key
     *            the key
     * @return whether a model identified by the key exists
     */
    public boolean exists(Key key) {
        return getOrNull(key) != null;
    }

    /**
     * Returns models.
     * 
     * @param keys
     *            the keys
     * @return models
     */
    public List<T> get(List<Key> keys) {
        return Datastore.get(modelClass, keys);
    }

    /**
     * Returns models asynchronously.
     * 
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     */
    public Future<List<T>> getAsync(List<Key> keys) {
        return Datastore.getAsync(modelClass, keys);
    }

    /**
     * Returns models as {@link Map}.
     * 
     * @param keys
     *            the keys
     * @return models
     */
    public Map<Key, T> getAsMap(List<Key> keys) {
        return Datastore.getAsMap(modelClass, keys);
    }

    /**
     * Returns models asynchronously as {@link Map}.
     * 
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     */
    public Future<Map<Key, T>> getAsMapAsync(List<Key> keys) {
        return Datastore.getAsMapAsync(modelClass, keys);
    }

    /**
     * Puts the model.
     * 
     * @param model
     *            the model
     * @return a key
     */
    public Key put(T model) {
        return Datastore.put(model);
    }

    /**
     * Puts the model asynchronously.
     * 
     * @param model
     *            the model
     * @return a key represented as {@link Future}
     */
    public Future<Key> putAsync(T model) {
        return Datastore.putAsync(model);
    }

    /**
     * Puts the models.
     * 
     * @param models
     *            the models
     * @return keys
     */
    public List<Key> put(List<T> models) {
        return Datastore.put(models);
    }

    /**
     * Puts the models asynchronously.
     * 
     * @param models
     *            the models
     * @return keys represented as {@link Future}
     */
    public Future<List<Key>> putAsync(List<T> models) {
        return Datastore.putAsync(models);
    }

    /**
     * Deletes a model specified by the key.
     * 
     * @param key
     *            the key
     */
    public void delete(Key key) {
        Datastore.delete(key);
    }

    /**
     * Deletes a model specified by the key asynchronously.
     * 
     * @param key
     *            the key
     * @return {@link Void} represented as {@link Future}
     */
    public Future<Void> deleteAsync(Key key) {
        return Datastore.deleteAsync(key);
    }

    /**
     * Deletes models specified by the keys.
     * 
     * @param keys
     *            the keys
     */
    public void delete(List<Key> keys) {
        Datastore.delete(keys);
    }

    /**
     * Deletes models specified by the keys asynchronously.
     * 
     * @param keys
     *            the keys
     * @return {@link Void} represented as {@link Future}
     */
    public Future<Void> deleteAsync(List<Key> keys) {
        return Datastore.deleteAsync(keys);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @return a {@link ModelQuery}
     */
    protected ModelQuery<T> query() {
        return Datastore.query(m);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param ancestorKey
     *            the ancestor key
     * 
     * @return a {@link ModelQuery}
     */
    protected ModelQuery<T> query(Key ancestorKey) {
        return Datastore.query(m, ancestorKey);
    }
}