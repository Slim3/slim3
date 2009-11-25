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
