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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * An inverse reference for model.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @param <O>
 *            the owner type
 * @since 1.0.0
 * 
 */
public class InverseModelRef<M, O> extends AbstractInverseModelRef<M, O> {

    private static final long serialVersionUID = 1L;

    /**
     * The model.
     */
    protected M model;

    /**
     * Constructor.
     */
    protected InverseModelRef() {
    }

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * @param mappedPropertyName
     *            the mapped property name
     * @param owner
     *            the owner that has this {@link InverseModelRef}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the
     *             mappedPropertyName parameter is null or if the owner
     *             parameter is null
     */
    public InverseModelRef(Class<M> modelClass,
            CharSequence mappedPropertyName, O owner)
            throws NullPointerException {
        super(modelClass, mappedPropertyName, owner);
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
     * Refreshes the model.
     * 
     * @return a refreshed model
     */
    public M refresh() {
        Key key = getOwnerKey();
        if (key == null) {
            return null;
        }
        model =
            Datastore.query(getModelMeta()).filter(
                mappedPropertyName,
                FilterOperator.EQUAL,
                key).asSingle();
        return model;
    }

    /**
     * Clears the state of this {@link InverseModelRef}.
     */
    public void clear() {
        model = null;
    }
}