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

public abstract class AbstractInverseModelRef<M, O> extends AbstractModelRef<M> {

    private static final long serialVersionUID = 1L;

    protected String mappedPropertyName;

    protected O owner;

    protected AbstractInverseModelRef() {
    }

    public AbstractInverseModelRef(Class<M> modelClass,
            CharSequence mappedPropertyName, O owner) throws NullPointerException {
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
}