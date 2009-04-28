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

import javax.jdo.Transaction;

/**
 * A JDO template class using transaction.
 * 
 * @author higa
 * @param <R>
 *            the return type
 * @since 3.0
 * 
 */
public abstract class JDOTxTemplate<R> extends JDOTemplate<R> {

    /**
     * The transaction.
     */
    protected Transaction tx;

    @Override
    protected void beforeExecution() {
        tx = pm.currentTransaction();
        tx.begin();
    }

    @Override
    protected void afterExecution(R returnValue) {
        if (tx.getRollbackOnly()) {
            tx.rollback();
        } else {
            tx.commit();
        }
    }

    @Override
    protected void handleThrowable(Throwable t) {
        tx.rollback();
        super.handleThrowable(t);
    }

}