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

import java.util.Iterator;

import com.google.appengine.api.datastore.Entity;

/**
 * An {@link Iterator} for a model.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 1.0.6
 * 
 */
public class ModelIterator<M> implements Iterator<M> {

    /**
     * The {@link Iterator} for the entity.
     */
    protected Iterator<Entity> entityIterator;

    /**
     * The meta data of the model.
     */
    protected ModelMeta<M> modelMeta;

    /**
     * Constructor.
     * 
     * @param entityIterator
     *            the {@link Iterator} for the entity
     * @param modelMeta
     *            the meta data of the model
     * @throws NullPointerException
     *             if the entityIterator parameter is null or if the modelMeta
     *             parameter is null
     */
    public ModelIterator(Iterator<Entity> entityIterator, ModelMeta<M> modelMeta)
            throws NullPointerException {
        if (entityIterator == null) {
            throw new NullPointerException(
                "The entityIterator parameter must not be null.");
        }
        if (modelMeta == null) {
            throw new NullPointerException(
                "The modelMeta parameter must not be null.");
        }
        this.entityIterator = entityIterator;
        this.modelMeta = modelMeta;
    }

    public boolean hasNext() {
        return entityIterator.hasNext();
    }

    public M next() {
        Entity entity = entityIterator.next();
        ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
        return mm.entityToModel(entity);
    }

    public void remove() {
        entityIterator.remove();
    }
}