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

import java.io.Serializable;

import org.slim3.util.ClassUtil;

import com.google.appengine.api.datastore.Key;

/**
 * A base reference for Model.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 3.0
 * 
 */
public class AbstractModelRef<M> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The model class.
     */
    protected transient Class<M> modelClass;

    /**
     * The model class name.
     */
    protected transient String modelClassName;

    /**
     * The meta data of model.
     */
    protected transient ModelMeta<M> modelMeta;

    /**
     * Constructor.
     */
    protected AbstractModelRef() {
    }

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public AbstractModelRef(Class<M> modelClass) throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        this.modelClass = modelClass;
    }

    /**
     * Returns a model class.
     * 
     * @return a model class
     */
    public Class<M> getModelClass() {
        if (modelClass == null) {
            modelClass = ClassUtil.forName(modelClassName);
        }
        return modelClass;
    }

    /**
     * Returns a meta data of model.
     * 
     * @return a meta data of model
     */
    public ModelMeta<M> getModelMeta() {
        if (modelMeta != null) {
            return modelMeta;
        }
        modelMeta = Datastore.getModelMeta(modelClass);
        return modelMeta;
    }

    /**
     * Validates the key.
     * 
     * @param key
     *            the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of
     *             {@link ModelMeta}
     */
    protected void validate(Key key) throws NullPointerException,
            IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException("The key parameter is null.");
        }
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

    /**
     * Validates the model.
     * 
     * @param model
     *            the model
     * @return a validated key
     * @throws NullPointerException
     *             if the model parameter is null
     * @throws IllegalArgumentException
     *             if the model does not have a primary key or if the kind of
     *             the key is different from the kind of {@link ModelMeta}
     */
    protected Key validate(M model) throws NullPointerException,
            IllegalArgumentException {
        if (model == null) {
            throw new NullPointerException("The model parameter is null.");
        }
        Key k = getModelMeta().getKey(model);
        if (k == null) {
            throw new IllegalArgumentException(
                "The model must have a primary key.");
        }
        validate(k);
        return k;
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.defaultWriteObject();
        s.writeUTF(modelClass.getName());
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        modelClassName = s.readUTF();
    }
}
