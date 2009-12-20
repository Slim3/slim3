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

/**
 * A meta data of string attribute.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 3.0
 * 
 */
public class StringAttributeMeta<M> extends CoreAttributeMeta<M, String> {

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     */
    public StringAttributeMeta(ModelMeta<M> modelMeta, String name) {
        super(modelMeta, name, String.class);
    }

    /**
     * Returns the "startsWith" filter.
     * 
     * @param value
     *            the value
     * @return the "startsWith" filter
     */
    public FilterCriterion startsWith(String value) {
        return new StartsWithCriterion(this, value);
    }

    /**
     * Returns the "endsWith" filter.
     * 
     * @param value
     *            the value
     * @return the "endsWith" filter
     */
    public InMemoryFilterCriterion endsWith(String value) {
        return new EndsWithCriterion(this, value);
    }
}