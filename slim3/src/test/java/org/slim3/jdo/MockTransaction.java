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
import javax.transaction.Synchronization;

/**
 * @author higa
 * 
 */
public class MockTransaction implements Transaction {

    /**
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * Whether this transaction is active.
     */
    protected boolean active = false;

    /**
     * The isolation level.
     */
    protected String isolationLevel;

    /**
     * Constructor.
     * 
     * @param pm
     *            the persistence manager
     */
    public MockTransaction(PersistenceManager pm) {
        this.pm = pm;
    }

    public void begin() {
        active = true;
    }

    public void commit() {
        active = false;
    }

    public String getIsolationLevel() {
        return isolationLevel;
    }

    public boolean getNontransactionalRead() {
        return false;
    }

    public boolean getNontransactionalWrite() {
        return false;
    }

    public boolean getOptimistic() {
        return false;
    }

    public PersistenceManager getPersistenceManager() {
        return pm;
    }

    public boolean getRestoreValues() {
        return false;
    }

    public boolean getRetainValues() {
        return false;
    }

    public boolean getRollbackOnly() {
        return false;
    }

    public Boolean getSerializeRead() {
        return null;
    }

    public Synchronization getSynchronization() {
        return null;
    }

    public boolean isActive() {
        return active;
    }

    public void rollback() {
        active = false;
    }

    public void setIsolationLevel(String isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    public void setNontransactionalRead(boolean arg0) {
    }

    public void setNontransactionalWrite(boolean arg0) {
    }

    public void setOptimistic(boolean arg0) {
    }

    public void setRestoreValues(boolean arg0) {
    }

    public void setRetainValues(boolean arg0) {
    }

    public void setRollbackOnly() {
    }

    public void setSerializeRead(Boolean arg0) {
    }

    public void setSynchronization(Synchronization arg0) {
    }
}