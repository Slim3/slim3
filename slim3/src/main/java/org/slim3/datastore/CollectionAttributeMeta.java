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
 * A meta data of collection attribute.
 * 
 * @author higa
 * @param <T>
 *            the element type of collection
 * @since 3.0
 * 
 */
public class CollectionAttributeMeta<T> extends AbstractAttributeMeta<T> {

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @param attributeClass
     *            the attribute class
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null of if the attributeClass parameter is null
     */
    public CollectionAttributeMeta(ModelMeta<?> modelMeta, String name,
            Class<?> attributeClass) {
        super(modelMeta, name, attributeClass);
    }
}