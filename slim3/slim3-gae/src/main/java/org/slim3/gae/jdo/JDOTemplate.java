/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.slim3.commons.util.BooleanUtil;
import org.slim3.commons.util.ByteUtil;
import org.slim3.commons.util.DateUtil;
import org.slim3.commons.util.DoubleUtil;
import org.slim3.commons.util.FloatUtil;
import org.slim3.commons.util.IntegerUtil;
import org.slim3.commons.util.LongUtil;
import org.slim3.commons.util.RuntimeExceptionUtil;
import org.slim3.commons.util.ShortUtil;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * A template class for JDO.
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
     * The transaction.
     */
    protected Transaction tx;

    /**
     * Executes an action.
     * 
     * @return the executed result
     */
    public final R execute() {
        R returnValue = null;
        pm = PM.getCurrent();
        assertPersistenceManagerIsActive();
        tx = pm.currentTransaction();
        try {
            beforeExecution();
            returnValue = doExecute();
            afterExecution(returnValue);
        } catch (Throwable t) {
            handleThrowable(t);
        }
        return returnValue;
    }

    /**
     * Processes an action before execution.
     */
    protected void beforeExecution() {
        tx.begin();
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
        if (tx.getRollbackOnly()) {
            tx.rollback();
        } else {
            tx.commit();
        }
    }

    /**
     * Handles the exception.
     * 
     * @param t
     *            the exception
     */
    protected void handleThrowable(Throwable t) {
        tx.rollback();
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
     * Looks up the instance of the given type with the given key.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model type
     * @param key
     *            the key
     * @return the model specified by the key
     */
    protected <M> M getObjectByKey(Class<M> modelClass, Key key) {
        assertPersistenceManagerIsActive();
        return pm.getObjectById(modelClass, key);
    }

    /**
     * Looks up the instance of the given type with the given identifier.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model type
     * @param id
     *            the identifier
     * @return the model specified by the identifier
     */
    protected <M> M getObjectById(Class<M> modelClass, long id) {
        assertPersistenceManagerIsActive();
        return pm.getObjectById(modelClass, id);
    }

    /**
     * Looks up the instance of the given type with the given identifier.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model type
     * @param id
     *            the identifier
     * @return the model specified by the identifier
     */
    protected <M> M getObjectByKeyId(Class<M> modelClass, long id) {
        assertPersistenceManagerIsActive();
        return getObjectByKey(modelClass, key(modelClass, id));
    }

    /**
     * Looks up the instance of the given type with the given key.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return the model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    protected <M> M getObjectByKey(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        assertPersistenceManagerIsActive();
        return pm.getObjectById(modelMeta.getModelClass(), key);
    }

    /**
     * Looks up the instance of the given type with the given identifier.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param id
     *            the identifier
     * @return the model specified by the identifier
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
     * Looks up the instance of the given type with the given key identifier.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param id
     *            the identifier
     * @return the model specified by the identifier
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
     * Converts the object to the boolean object.
     * 
     * @param o
     *            the object
     * @return the boolean object
     */
    protected Boolean toBoolean(Object o) {
        return BooleanUtil.toBoolean(o);
    }

    /**
     * Converts the object to the byte object.
     * 
     * @param o
     *            the object
     * @return the byte object
     */
    protected Byte toByte(Object o) {
        return ByteUtil.toByte(o);
    }

    /**
     * Converts the object to the short object.
     * 
     * @param o
     *            the object
     * @return the short object
     */
    protected Short toShort(Object o) {
        return ShortUtil.toShort(o);
    }

    /**
     * Converts the object to the integer object.
     * 
     * @param o
     *            the object
     * @return the integer object
     */
    protected Integer toInteger(Object o) {
        return IntegerUtil.toInteger(o);
    }

    /**
     * Converts the object to the long object.
     * 
     * @param o
     *            the object
     * @return the long object
     */
    protected Long toLong(Object o) {
        return LongUtil.toLong(o);
    }

    /**
     * Converts the object to the float object.
     * 
     * @param o
     *            the object
     * @return the float object
     */
    protected Float toFloat(Object o) {
        return FloatUtil.toFloat(o);
    }

    /**
     * Converts the object to the double object.
     * 
     * @param o
     *            the object
     * @return the double object
     */
    protected Double toDouble(Object o) {
        return DoubleUtil.toDouble(o);
    }

    /**
     * Converts the object to the date object.
     * 
     * @param o
     *            the object
     * @return the date object
     */
    protected Date toDate(Object o) {
        return DateUtil.toDate(o);
    }

    /**
     * Converts the object to the date object.
     * 
     * @param text
     *            the text
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the date object
     */
    protected Date toDate(String text, String pattern) {
        return DateUtil.toDate(text, pattern);
    }

    /**
     * Creates a new key.
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
     * Creates a new key.
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