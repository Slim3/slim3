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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.QueryResultIterator;

/**
 * An {@link QueryResultIterator} for a model.
 * 
 * @author @kissrobber
 * @param <M>
 *            the model type
 * @since 1.0.13
 */
public class S3QueryResultIterator<M> extends ModelIterator<M> implements
        Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The delegate.
     */
    protected QueryResultIterator<Entity> delegate;

    /**
     * The encoded filter.
     */
    protected String encodedFilter;

    /**
     * The array of sorts as encoded string.
     */
    protected String encodedSorts;

    /**
     * Constructor.
     * 
     * @param delegate
     *            the delegate
     * @param modelMeta
     *            the meta data of the model
     * @param encodedFilter
     *            the encoded filter
     * @param encodedSorts
     *            the array of sorts
     */
    public S3QueryResultIterator(QueryResultIterator<Entity> delegate,
            ModelMeta<M> modelMeta, String encodedFilter, String encodedSorts) {
        super(delegate, modelMeta);
        this.delegate = delegate;
        this.encodedFilter = encodedFilter;
        this.encodedSorts = encodedSorts;
    }

    /**
     * Returns the encoded cursor.
     * 
     * @return the encoded cursor
     */
    public String getEncodedCursor() {
        return delegate.getCursor().toWebSafeString();
    }

    /**
     * Returns the encoded filter.
     * 
     * @return the encoded filter
     */
    public String getEncodedFilter() {
        return encodedFilter;
    }

    /**
     * Returns the encoded sorts.
     * 
     * @return the encoded sorts
     */
    public String getEncodedSorts() {
        return encodedSorts;
    }

}