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

import javax.jdo.Transaction;

import org.slim3.commons.util.BooleanUtil;
import org.slim3.commons.util.ByteUtil;
import org.slim3.commons.util.DoubleUtil;
import org.slim3.commons.util.FloatUtil;
import org.slim3.commons.util.IntegerUtil;
import org.slim3.commons.util.LongUtil;
import org.slim3.commons.util.RuntimeExceptionUtil;
import org.slim3.commons.util.ShortUtil;

/**
 * A template class for JDO.
 * 
 * @author higa
 * @param <T>
 *            the type
 * @since 3.0
 * 
 */
public abstract class JDOTemplate<T> {

    /**
     * The transaction.
     */
    protected Transaction tx;

    /**
     * Executes an action.
     * 
     * @return the executed result
     */
    public final T execute() {
        T returnValue = null;
        tx = PM.getCurrent().currentTransaction();
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
    protected abstract T doExecute();

    /**
     * Processes an action after execution.
     * 
     * @param returnValue
     *            the return value
     */
    protected void afterExecution(T returnValue) {
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
}