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
import javax.jdo.Transaction;

/**
 * A transaction template class.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class TxTemplate {

    /**
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * The transaction.
     */
    protected Transaction tx;

    /**
     * Constructor.
     * 
     * @param pm
     *            the persistence manager
     * @throws NullPointerException
     *             if the pm parameter is null
     */
    public TxTemplate(PersistenceManager pm) throws NullPointerException {
        if (pm == null) {
            throw new NullPointerException("The pm parameter is null.");
        }
        this.pm = pm;
    }

    /**
     * Run this template.
     * 
     */
    public final void run() {
        tx = pm.currentTransaction();
        try {
            tx.begin();
            doRun();
            if (tx.getRollbackOnly()) {
                tx.rollback();
            } else {
                tx.commit();
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * You can implement this method to customize this template.
     * 
     */
    protected abstract void doRun();
}