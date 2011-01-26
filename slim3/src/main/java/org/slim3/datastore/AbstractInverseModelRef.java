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
public abstract class AbstractInverseModelRef<M, O> extends AbstractModelRef<M> {

    private static final long serialVersionUID = 1L;

    /**
     * The mapped property name.
     */
    protected String mappedPropertyName;

    /**
     * The owner that has this {@link AbstractInverseModelRef}.
     */
    protected O owner;

    /**
     * Constructor.
     */
    protected AbstractInverseModelRef() {
    }

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * @param mappedPropertyName
     *            the mapped property name
     * @param owner
     *            the owner that has this {@link AbstractInverseModelRef}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the
     *             mappedPropertyName parameter is null or if the owner
     *             parameter is null
     */
    public AbstractInverseModelRef(Class<M> modelClass,
            CharSequence mappedPropertyName, O owner)
            throws NullPointerException {
        super(modelClass);
        if (mappedPropertyName == null) {
            throw new NullPointerException(
                "The mappedPropertyName must not be null.");
        }
        if (owner == null) {
            throw new NullPointerException("The owner must not be null.");
        }
        this.mappedPropertyName = mappedPropertyName.toString();
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        Key ownerKey = getOwnerKey();
        if (ownerKey == null) {
            return 0;
        }
        return ownerKey.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        Key ownerKey = getOwnerKey();
        if (ownerKey == null) {
            return false;
        }
        Key otherOwnerKey =
            ((AbstractInverseModelRef<?, ?>) other).getOwnerKey();
        return ownerKey.equals(otherOwnerKey);

    }

    /**
     * Returns the key of owner.
     * 
     * @return a refreshed model
     */
    protected Key getOwnerKey() {
        ModelMeta<?> ownerModelMeta = Datastore.getModelMeta(owner.getClass());
        return ownerModelMeta.getKey(owner);
    }
}