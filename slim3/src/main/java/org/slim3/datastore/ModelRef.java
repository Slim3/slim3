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

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Key;

/**
 * A reference for model like ReferenceProperty of Python.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 1.0.0
 * 
 */
public class ModelRef<M> extends AbstractModelRef<M> {

    private static final long serialVersionUID = 1L;

    /**
     * The model.
     */
    protected M model;

    /**
     * The key.
     */
    protected Key key;

    /**
     * Constructor.
     */
    protected ModelRef() {
    }

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * 
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public ModelRef(Class<M> modelClass) throws NullPointerException {
        super(modelClass);
    }

    /**
     * Returns the model.
     * 
     * @return the model
     */
    public M getModel() {
        if (model != null) {
            return model;
        }
        return refresh();
    }

    /**
     * Sets the model.
     * 
     * @param model
     *            the model
     * @throws IllegalArgumentException
     *             if the model is null or if the model does not have a primary
     *             key or if the kind of the key is different from the kind of
     *             ModelMeta
     */
    public void setModel(M model) throws IllegalArgumentException {
        if (model == null) {
            this.model = null;
            this.key = null;
        } else {
            this.model = model;
        }
    }

    /**
     * Returns the key.
     * 
     * @return the key
     */
    public Key getKey() {
        if (key != null) {
            return key;
        }
        if (model == null) {
            return null;
        }
        key = getModelMeta().getKey(model);
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key
     *            the key
     * @throws IllegalStateException
     *             if the model is set
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of
     *             ModelMeta
     */
    public void setKey(Key key) throws IllegalStateException,
            IllegalArgumentException {
        if (model != null) {
            throw new IllegalStateException(
                "You can not set the key when the model is set.");
        }
        if (key != null) {
            getModelMeta().validateKey(key);
        }
        this.key = key;
    }

    /**
     * Refreshes the model.
     * 
     * @return a refreshed model
     */
    public M refresh() {
        if (key == null) {
            return null;
        }
        model = Datastore.getWithoutTx(getModelMeta(), key);
        return model;
    }

    /**
     * Clears the state of this {@link ModelRef}.
     */
    public void clear() {
        model = null;
        key = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass()) {
            return false;
        }
        Key otherKey = ((ModelRef<?>) obj).getKey();
        if (key == null) {
            return false;
        }
        return key.equals(otherKey);
    }

    /**
     * Assigns a new key to the model if necessary.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @return a key
     * @throws NullPointerException
     *             if the ds parameter is null
     */
    public Key assignKeyIfNecessary(AsyncDatastoreService ds)
            throws NullPointerException {
        if (model == null) {
            return null;
        }
        key = getModelMeta().getKey(model);
        if (key != null) {
            return key;
        }
        key = DatastoreUtil.allocateId(ds, getModelMeta().getKind());
        getModelMeta().setKey(model, key);
        return key;
    }
}