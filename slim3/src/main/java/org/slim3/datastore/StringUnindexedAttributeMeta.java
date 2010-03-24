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

/**
 * A meta data of string attribute.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 1.0.1
 * 
 */
public class StringUnindexedAttributeMeta<M> extends
        CoreUnindexedAttributeMeta<M, String> {

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @param fieldName
     *            the field name
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null or if the fieldName parameter is null
     */
    public StringUnindexedAttributeMeta(ModelMeta<M> modelMeta, String name,
            String fieldName) {
        super(modelMeta, name, fieldName, String.class);
    }

    /**
     * Returns the "startsWith" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "startsWith" in-memory filter
     */
    public InMemoryStartsWithCriterion startsWith(String value) {
        return new InMemoryStartsWithCriterion(this, value);
    }

    /**
     * Returns the "endsWith" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "endsWith" in-memory filter
     */
    public InMemoryEndsWithCriterion endsWith(String value) {
        return new InMemoryEndsWithCriterion(this, value);
    }

    /**
     * Returns the "contains" in-memory filter.
     * 
     * @param value
     *            the value
     * @return the "contains" in-memory filter
     */
    public InMemoryContainsCriterion contains(String value) {
        return new InMemoryContainsCriterion(this, value);
    }
}