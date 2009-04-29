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
package org.slim3.gae.jdo;

import javax.jdo.PersistenceManager;

import org.slim3.commons.util.RuntimeExceptionUtil;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * A JDO template class.
 * 
 * @author higa
 * @param <R>
 *            the return type
 * @since 3.0
 * 
 */
public abstract class JDOTemplate<R> {

    /**
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * Executes an action.
     * 
     * @return the executed result
     */
    public final R execute() {
        R returnValue = null;
        pm = PM.get();
        try {
            beforeExecution();
            returnValue = doExecute();
            afterExecution(returnValue);
        } catch (Throwable t) {
            handleThrowable(t);
        } finally {
            assertPersistenceManagerIsActive();
            pm.close();
        }
        return returnValue;
    }

    /**
     * Processes an action before execution.
     */
    protected void beforeExecution() {
    }

    /**
     * Executes an action.
     * 
     * @return the executed result
     */
    protected abstract R doExecute();

    /**
     * Processes an action after execution.
     * 
     * @param returnValue
     *            the return value
     */
    protected void afterExecution(R returnValue) {
    }

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
     * Returns the model specified by the key.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return the model
     */
    protected <M> M getObjectByKey(Class<M> modelClass, Key key) {
        assertPersistenceManagerIsActive();
        return pm.getObjectById(modelClass, key);
    }

    /**
     * Returns the model specified by the identifier.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param id
     *            the identifier
     * @return the model
     */
    protected <M> M getObjectById(Class<M> modelClass, long id) {
        assertPersistenceManagerIsActive();
        return pm.getObjectById(modelClass, id);
    }

    /**
     * Returns the model specified by the name.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param name
     *            the name
     * @return the model
     */
    protected <M> M getObjectByName(Class<M> modelClass, String name) {
        assertPersistenceManagerIsActive();
        return pm.getObjectById(modelClass, name);
    }

    /**
     * Returns the model specified by the key identifier.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param id
     *            the identifier
     * @return the model
     */
    protected <M> M getObjectByKeyId(Class<M> modelClass, long id) {
        assertPersistenceManagerIsActive();
        return getObjectByKey(modelClass, key(modelClass, id));
    }

    /**
     * Returns the model specified by the key name.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param name
     *            the name
     * @return the model
     */
    protected <M> M getObjectByKeyName(Class<M> modelClass, String name) {
        assertPersistenceManagerIsActive();
        return getObjectByKey(modelClass, key(modelClass, name));
    }

    /**
     * Returns the model specified by the key.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return the model
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    protected <M> M getObjectByKey(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        assertPersistenceManagerIsActive();
        return getObjectByKey(modelMeta.getModelClass(), key);
    }

    /**
     * Returns the model specified by the identifier.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param id
     *            the identifier
     * @return the model
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    protected <M> M getObjectById(ModelMeta<M> modelMeta, long id)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        assertPersistenceManagerIsActive();
        return getObjectById(modelMeta.getModelClass(), id);
    }

    /**
     * Returns the model specified by the name.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @return the model
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    protected <M> M getObjectByName(ModelMeta<M> modelMeta, String name)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        assertPersistenceManagerIsActive();
        return getObjectByName(modelMeta.getModelClass(), name);
    }

    /**
     * Returns the model specified by the key identifier.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param id
     *            the identifier
     * @return the model
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    protected <M> M getObjectByKeyId(ModelMeta<M> modelMeta, long id)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        assertPersistenceManagerIsActive();
        return getObjectByKeyId(modelMeta.getModelClass(), id);
    }

    /**
     * Returns the model specified by the key name.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @return the model
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    protected <M> M getObjectByKeyName(ModelMeta<M> modelMeta, String name)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        assertPersistenceManagerIsActive();
        return getObjectByKeyName(modelMeta.getModelClass(), name);
    }

    /**
     * Makes the model persistent.
     * 
     * @param <M>
     *            the model type
     * @param model
     *            the model
     * @return the model
     */
    protected <M> M makePersistent(M model) {
        assertPersistenceManagerIsActive();
        return pm.makePersistent(model);
    }

    /**
     * Deletes the model from the data store.
     * 
     * @param model
     *            the model
     */
    protected void deletePersistent(Object model) {
        assertPersistenceManagerIsActive();
        pm.deletePersistent(model);
    }

    /**
     * Creates a new key specified by the identifier.
     * 
     * @param modelClass
     *            the model class
     * @param id
     *            the identifier
     * @return a new key
     */
    protected Key key(Class<?> modelClass, long id) {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return KeyFactory.createKey(modelClass.getSimpleName(), id);
    }

    /**
     * Creates a new key specified by the name.
     * 
     * @param modelClass
     *            the model class
     * @param name
     *            the name
     * @return a new key
     */
    protected Key key(Class<?> modelClass, String name) {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return KeyFactory.createKey(modelClass.getSimpleName(), name);
    }

    /**
     * Creates a new key specified by the identifier.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param id
     *            the identifier
     * @return a new key
     */
    protected Key key(ModelMeta<?> modelMeta, long id) {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return key(modelMeta.getModelClass(), id);
    }

    /**
     * Creates a new key specified by the name.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @return a new key
     */
    protected Key key(ModelMeta<?> modelMeta, String name) {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return key(modelMeta.getModelClass(), name);
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