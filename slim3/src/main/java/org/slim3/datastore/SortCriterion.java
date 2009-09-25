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

import com.google.appengine.api.datastore.Query;

/**
 * A criterion interface for sort.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 3.0
 * 
 */
public interface SortCriterion<M> {

    /**
     * Applies this sort to the query.
     * 
     * @param query
     *            the query
     */
    void apply(Query query);

    /**
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.
     * 
     * @param o1
     *            the first model
     * @param o2
     *            the second model
     * @return the compared result
     * @throws IllegalStateException
     *             if the model is embedded or if the attribute is not
     *             comparable
     */
    int compare(M o1, M o2) throws IllegalStateException;
}