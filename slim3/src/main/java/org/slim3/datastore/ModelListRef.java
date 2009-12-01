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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * A reference for list of models.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 3.0
 * 
 */
public class ModelListRef<M> extends AbstractModelRef<M> {

    private static final long serialVersionUID = 1L;

    /**
     * The list of models.
     */
    protected List<M> modelList = new ModelList();

    /**
     * The list of keys.
     */
    protected List<Key> keyList = new KeyList();

    /**
     * Constructor.
     */
    protected ModelListRef() {
    }

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public ModelListRef(Class<M> modelClass) throws NullPointerException {
        super(modelClass);
    }

    /**
     * Returns the list of models.
     * 
     * @return the list of models
     */
    public List<M> getModelList() {
        if (!modelList.isEmpty()) {
            return modelList;
        }
        return refresh();
    }

    /**
     * Returns the list of models within the transaction.
     * 
     * @param tx
     *            the transaction
     * @return the list of models
     */
    public List<M> getModelList(Transaction tx) {
        if (!modelList.isEmpty()) {
            return modelList;
        }
        return refresh(tx);
    }

    /**
     * Returns the list of keys.
     * 
     * @return the list of keys
     */
    public List<Key> getKeyList() {
        return keyList;
    }

    /**
     * Refreshes the list of models.
     * 
     * @return a refreshed list of models
     */
    public List<M> refresh() {
        modelList.clear();
        if (!keyList.isEmpty()) {
            modelList.addAll(Datastore.get(getModelClass(), keyList));
        }
        return modelList;
    }

    /**
     * Refreshes the list of models within the transaction.
     * 
     * @param tx
     *            the transaction
     * @return a refreshed list of models
     */
    public List<M> refresh(Transaction tx) {
        modelList.clear();
        if (!keyList.isEmpty()) {
            modelList.addAll(Datastore.get(tx, getModelClass(), keyList));
        }
        return modelList;
    }

    private class ModelList extends ArrayList<M> {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean add(M model) {
            if (model == null) {
                throw new NullPointerException("The model parameter is null.");
            }
            Key key = validate(model);
            if (super.add(model)) {
                keyList.add(key);
                return true;
            }
            return false;
        }

        @Override
        public boolean remove(Object model) {
            if (model == null) {
                throw new NullPointerException("The model parameter is null.");
            }
            if (modelClass.isInstance(model)) {
                Key key = validate(modelClass.cast(model));
                if (super.remove(model)) {
                    keyList.remove(key);
                    return true;
                }
                return false;
            }
            throw new IllegalArgumentException("The class("
                + model.getClass()
                + ") of the model must be "
                + getModelClass().getName()
                + ".");
        }

        @Override
        public boolean addAll(Collection<? extends M> models) {
            if (models == null) {
                throw new NullPointerException("The models parameter is null.");
            }
            boolean ret = false;
            for (M model : models) {
                if (add(model)) {
                    ret = true;
                }
            }
            return ret;
        }

        @Override
        public boolean addAll(int index, Collection<? extends M> models) {
            if (models == null) {
                throw new NullPointerException("The models parameter is null.");
            }
            List<Key> kList = new ArrayList<Key>();
            for (M model : models) {
                kList.add(validate(model));
            }
            if (super.addAll(index, models)) {
                keyList.addAll(index, kList);
                return true;
            }
            return false;
        }

        @Override
        public void clear() {
            super.clear();
            keyList.clear();
        }

        @Override
        public M set(int index, M model) {
            if (model == null) {
                throw new NullPointerException("The model parameter is null.");
            }
            Key key = validate(model);
            keyList.set(index, key);
            return super.set(index, model);
        }

        @Override
        public void add(int index, M model) {
            if (model == null) {
                throw new NullPointerException("The model parameter is null.");
            }
            Key key = validate(model);
            super.add(index, model);
            keyList.add(index, key);
        }

        @Override
        public M remove(int index) {
            keyList.remove(index);
            return super.remove(index);
        }
    }

    private class KeyList extends ArrayList<Key> {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean add(Key key) {
            if (key == null) {
                new NullPointerException("The key parameter is null.");
            }
            if (!modelList.isEmpty()) {
                throw new IllegalStateException(
                    "You can use this method only when the list of models is empty. Use modelList.add() instead of this method.");
            }
            return keyList.add(key);
        }

        @Override
        public boolean remove(Object key) {
            if (key == null) {
                throw new NullPointerException("The key parameter is null.");
            }
            if (!modelList.isEmpty()) {
                throw new IllegalStateException(
                    "You can use this method only when the list of models is empty. Use modelList.remove() instead of this method.");
            }
            if (key.getClass() == Key.class) {
                return super.remove(key);
            }
            throw new IllegalArgumentException("The class("
                + key.getClass()
                + ") of the key must be "
                + Key.class.getName()
                + ".");
        }

        @Override
        public boolean addAll(Collection<? extends Key> keys) {
            if (keys == null) {
                throw new NullPointerException("The keys parameter is null.");
            }
            if (!modelList.isEmpty()) {
                throw new IllegalStateException(
                    "You can use this method only when the list of models is empty. Use modelList.addAll() instead of this method.");
            }
            return super.addAll(keys);
        }

        @Override
        public boolean addAll(int index, Collection<? extends Key> keys) {
            if (keys == null) {
                throw new NullPointerException("The keys parameter is null.");
            }
            if (!modelList.isEmpty()) {
                throw new IllegalStateException(
                    "You can use this method only when the list of models is empty. Use modelList.addAll() instead of this method.");
            }
            return super.addAll(index, keys);
        }

        @Override
        public void clear() {
            if (!modelList.isEmpty()) {
                throw new IllegalStateException(
                    "You can use this method only when the list of models is empty. Use modelList.clear() instead of this method.");
            }
            super.clear();
        }

        @Override
        public Key set(int index, Key key) {
            if (key == null) {
                throw new NullPointerException("The key parameter is null.");
            }
            if (!modelList.isEmpty()) {
                throw new IllegalStateException(
                    "You can use this method only when the list of models is empty. Use modelList.set() instead of this method.");
            }
            return super.set(index, key);
        }

        @Override
        public void add(int index, Key key) {
            if (key == null) {
                throw new NullPointerException("The key parameter is null.");
            }
            if (!modelList.isEmpty()) {
                throw new IllegalStateException(
                    "You can use this method only when the list of models is empty. Use modelList.add() instead of this method.");
            }
            super.add(index, key);
        }

        @Override
        public Key remove(int index) {
            if (!modelList.isEmpty()) {
                throw new IllegalStateException(
                    "You can use this method only when the list of models is empty. Use modelList.remove() instead of this method.");
            }
            return super.remove(index);
        }
    }
}