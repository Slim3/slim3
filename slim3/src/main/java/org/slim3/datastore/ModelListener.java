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
 * A model listener for "Automatic Assigned Property".
 * 
 * @author @kissrobber
 * @param <M> the model type
 * @since 1.0.13
 */
public interface ModelListener<M> {
    /**
     * This method is called before a model is put to datastore.
     * @param model the original model value
     */
    void prePut(M model);

    /**
     * This method is called after a model is get from datastore.
     * 
     * @param model the original model value
     */
    void postGet(M model);
}
