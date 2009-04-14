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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

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
    public final T execute() {
        pm = PM.getPersistenceManager();
        T ret = null;
        try {
            tx = pm.currentTransaction();
            try {
                tx.begin();
                ret = doExecute();
                if (tx.getRollbackOnly()) {
                    tx.rollback();
                } else {
                    tx.commit();
                }
            } catch (Throwable t) {
                tx.rollback();
            }
        } finally {
            pm.close();
        }
        return ret;
    }

    /**
     * Executes an action.
     * 
     * @return the executed result
     */
    protected abstract T doExecute();
}