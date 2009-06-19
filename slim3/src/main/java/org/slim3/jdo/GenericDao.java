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
package org.slim3.jdo;

import javax.jdo.PersistenceManager;

/**
 * A generic dao.
 * 
 * @author higa
 * @param <T>
 *            the model type.
 * @since 3.0
 * 
 */
public class GenericDao<T> {

    /**
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * The model class.
     */
    protected Class<T> modelClass;

    /**
     * Constructor.
     * 
     * @param pm
     *            the persistence manager
     */
    public GenericDao(PersistenceManager pm, Class<T> modelClass) {
        if (pm == null) {
            throw new NullPointerException("The pm parameter is null.");
        }
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        this.pm = pm;
        this.modelClass = modelClass;
    }

    public T find(String key) {
        return pm.getObjectById(modelClass, key);
    }

    public T insert(T model) {
        return pm.makePersistent(model);
    }

    public T update(T model) {
        return pm.makePersistent(model);
    }

    public void delete(T model) {
        pm.deletePersistent(model);
    }
}
