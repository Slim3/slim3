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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * A reference for Model.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 3.0
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
     * Returns the model within the transaction.
     * 
     * @param tx
     *            the transaction
     * @return the model
     */
    public M getModel(Transaction tx) {
        if (model != null) {
            return model;
        }
        return refresh(tx);
    }

    /**
     * Sets the model.
     * 
     * @param model
     *            the model
     * @throws IllegalArgumentException
     *             if the model does not have a primary key or if the kind of
     *             the key is different from the kind of ModelMeta
     */
    public void setModel(M model) throws IllegalArgumentException {
        this.model = model;
        if (model == null) {
            key = null;
        } else {
            Key k = getModelMeta().getKey(model);
            if (k == null) {
                throw new IllegalArgumentException(
                    "The model must have a primary key.");
            }
            validate(k);
            key = k;
        }
    }

    /**
     * Returns the key.
     * 
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key
     *            the key
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of
     *             ModelMeta
     */
    public void setKey(Key key) throws IllegalArgumentException {
        if (key != null) {
            validate(key);
            this.key = key;
        }
        model = null;
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
        model = Datastore.get(getModelClass(), key);
        return model;
    }

    /**
     * Refreshes the model within the transaction.
     * 
     * @param tx
     *            the transaction
     * @return a refreshed model
     */
    public M refresh(Transaction tx) {
        if (key == null) {
            return null;
        }
        model = Datastore.get(tx, getModelClass(), key);
        return model;
    }

    /**
     * Validates the key.
     * 
     * @param key
     *            the key
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of
     *             {@link ModelMeta}
     */
    protected void validate(Key key) throws IllegalArgumentException {
        if (!key.getKind().equals(getModelMeta().getKind())) {
            throw new IllegalArgumentException("The kind("
                + key.getKind()
                + ") of the key("
                + key
                + ") must be "
                + getModelMeta().getKind()
                + ".");
        }
    }
}