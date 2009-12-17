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

import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * An inverse reference for models.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 3.0
 * 
 */
public class InverseModelListRef<M> extends AbstractModelRef<M> {

    private static final long serialVersionUID = 1L;

    /**
     * The mapped property name.
     */
    protected String mappedPropertyName;

    /**
     * The owner that has this {@link InverseModelListRef}.
     */
    protected Object owner;

    /**
     * The model.
     */
    protected List<M> modelList;

    /**
     * Constructor.
     */
    protected InverseModelListRef() {
    }

    /**
     * Constructor.
     * 
     * @param <O>
     *            the owner type
     * @param mappedAttributeMeta
     *            the mapped {@link AbstractAttributeMeta}
     * @param owner
     *            the owner that has this {@link InverseModelListRef}
     * @throws NullPointerException
     *             if the attributeMeta parameter is null or if the owner
     *             parameter is null
     */
    public <O> InverseModelListRef(
            ModelRefAttributeMeta<M, ModelRef<O>, O> mappedAttributeMeta,
            O owner) throws NullPointerException {
        if (mappedAttributeMeta == null) {
            throw new NullPointerException(
                "The mappedAttributeMeta must not be null.");
        }
        if (owner == null) {
            throw new NullPointerException("The owner must not be null.");
        }
        mappedPropertyName = mappedAttributeMeta.getName();
        setModelMeta(mappedAttributeMeta.modelMeta);
        this.owner = owner;
    }

    /**
     * Returns the models.
     * 
     * @param criteria
     *            the sort criteria
     * 
     * @return the models
     */
    public List<M> getModelList(SortCriterion... criteria) {
        if (modelList != null) {
            return modelList;
        }
        return refresh(criteria);
    }

    /**
     * Refreshes models.
     * 
     * @param criteria
     *            the sort criteria
     * @return refreshed models
     */
    public List<M> refresh(SortCriterion... criteria) {
        ModelMeta<?> ownerModelMeta = Datastore.getModelMeta(owner.getClass());
        Key key = ownerModelMeta.getKey(owner);
        if (key == null) {
            return null;
        }
        modelList =
            Datastore.query(getModelMeta()).filter(
                mappedPropertyName,
                FilterOperator.EQUAL,
                key).sort(criteria).asList();
        return modelList;
    }

    /**
     * Clears the state of this {@link InverseModelListRef}.
     */
    public void clear() {
        modelList = null;
    }
}