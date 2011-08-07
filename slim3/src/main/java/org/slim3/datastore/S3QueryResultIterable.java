/*
 * Copyright 2004-2010 the original author or authors.
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
import com.google.appengine.api.datastore.QueryResultIterable;

/**
 * An {@link QueryResultIterable} for a model.
 * 
 * @author @kissrobber
 * @param <M> the model type
 * @since 1.0.13
 */
public class S3QueryResultIterable<M> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The iterator.
     */
    protected S3QueryResultIterator<M> iterator;

    /**
     * Constructor.
     * 
     * @param iterator
     *            the iterator
     */
    S3QueryResultIterable(S3QueryResultIterator<M> iterator) {
        this.iterator = iterator;
    }

    /**
     * Return the iterator.
     * 
     * @return the iterator
     */
    public S3QueryResultIterator<M> iterator() {
        return iterator;
    }
}