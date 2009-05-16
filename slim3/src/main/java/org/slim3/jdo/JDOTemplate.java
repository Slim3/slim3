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

import org.slim3.util.RuntimeExceptionUtil;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * A JDO template class.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class JDOTemplate {

    /**
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * Creates a new key.
     * 
     * @param modelClass
     *            the model class
     * @param id
     *            the identity
     * @return a new key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    protected final static Key key(Class<?> modelClass, long id)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return KeyFactory.createKey(modelClass.getSimpleName(), id);
    }

    /**
     * Creates a new key.
     * 
     * @param modelClass
     *            the model class
     * @param name
     *            the name
     * @return a new key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the name parameter
     *             is null
     */
    protected final static Key key(Class<?> modelClass, String name)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        return KeyFactory.createKey(modelClass.getSimpleName(), name);
    }

    /**
     * Runs this template.
     * 
     */
    public final void run() {
        pm = PMF.get().getPersistenceManager();
        try {
            doRun();
        } catch (Throwable t) {
            handleThrowable(t);
        } finally {
            assertPersistenceManagerIsActive();
            pm.close();
        }
    }

    /**
     * You can implement this method to customize this template.
     * 
     */
    protected abstract void doRun();

    /**
     * Handles the exception.
     * 
     * @param t
     *            the exception
     */
    protected void handleThrowable(Throwable t) {
        RuntimeExceptionUtil.wrapAndThrow(t);
    }

    /**
     * Creates a new {@link SelectQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @return a new {@link SelectQuery}
     */
    protected <M> SelectQuery<M> from(ModelMeta<M> modelMeta) {
        assertPersistenceManagerIsActive();
        return new SelectQuery<M>(modelMeta, pm);
    }

    /**
     * Asserts that the current persistence manager is active.
     * 
     * @throws IllegalStateException
     *             if the persistence manager attached to the current thread is
     *             not found or if the persistence manager attached to the
     *             current thread is already closed
     */
    protected void assertPersistenceManagerIsActive()
            throws IllegalStateException {
        if (pm == null) {
            throw new IllegalStateException(
                "The persistence manager attached to the current thread is not found.");
        }
        if (pm.isClosed()) {
            throw new IllegalStateException(
                "The persistence manager attached to the current thread is already closed.");
        }
    }
}