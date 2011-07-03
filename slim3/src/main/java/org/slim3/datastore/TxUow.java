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

import com.google.appengine.api.datastore.Transaction;

/**
 * Unit of Work for Transaction.
 * 
 * @author higayasuo
 * @since 1.0.12
 * 
 */
public abstract class TxUow extends AbstractUow {

    /**
     * The transaction.
     */
    protected Transaction tx;

    @Override
    protected final void beginTransaction() {
        tx = Datastore.beginTransaction();
    }

    @Override
    protected final void commit() {
        tx.commit();
    }

    @Override
    protected final void rollback() {
        if (tx.isActive()) {
            tx.rollback();
        }
    }
}
