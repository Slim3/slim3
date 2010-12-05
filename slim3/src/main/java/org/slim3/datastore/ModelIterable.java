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

/**
 * @author higa
 * @param <M>
 *            the model type
 * @since 1.0.6
 * 
 */
public class ModelIterable<M> implements Iterable<M> {

    /**
     * The {@link Iterator} for the model.
     */
    protected Iterator<M> modelIterator;

    /**
     * Constructor.
     * 
     * @param modelIterator
     *            the {@link Iterator} for the model
     * @throws NullPointerException
     *             if the modelIterator parameter is null
     */
    public ModelIterable(Iterator<M> modelIterator) throws NullPointerException {
        if (modelIterator == null) {
            throw new NullPointerException(
                "The modelIterator parameter must not be null.");
        }
        this.modelIterator = modelIterator;
    }

    public Iterator<M> iterator() {
        return modelIterator;
    }
}
