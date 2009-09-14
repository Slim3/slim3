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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.slim3.util.ThrowableUtil;

/**
 * A class to hold the current persistence manager.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class CurrentPersistenceManager {

    private static final Logger logger =
        Logger.getLogger(CurrentPersistenceManager.class.getName());

    /**
     * {@link ThreadLocal} for persistence manager.
     */
    private static ThreadLocal<PersistenceManager> persistenceManagers =
        new ThreadLocal<PersistenceManager>();

    /**
     * Returns the current persistence manager.
     * 
     * @return the current persistence manager
     */
    public static PersistenceManager get() {
        return persistenceManagers.get();
    }

    /**
     * Returns the current persistence manager and checks the presence.
     * 
     * @return the current persistence manager
     */
    public static PersistenceManager getAndCheckPresence() {
        PersistenceManager pm = get();
        if (pm == null) {
            throw new IllegalStateException(
                "The current persistence manager is not found. You should register JDOFrontController on \"web.xml\".");
        }
        return pm;
    }

    /**
     * Returns the proxy of current persistence manager.
     * 
     * @return the proxy of current persistence manager
     */
    public static PersistenceManager getProxy() {
        return new PersistenceManagerProxy();
    }

    /**
     * Sets the current persistence manager.
     * 
     * @param pm
     *            the current persistence manager
     */
    public static void set(PersistenceManager pm) {
        persistenceManagers.set(pm);
    }

    /**
     * Reopens the current persistence manager.
     */
    public static void reopen() {
        close();
        set(PMF.get().getPersistenceManager());
    }

    /**
     * Closes the current persistence manager.
     */
    public static void close() {
        PersistenceManager pm = get();
        if (pm == null) {
            return;
        }
        Throwable error = null;
        try {
            Transaction tx = pm.currentTransaction();
            if (tx.isActive()) {
                tx.rollback();
            }
        } catch (Throwable e) {
            error = e;
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        try {
            pm.close();
        } catch (Throwable e) {
            if (error == null) {
                error = e;
            }
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        set(null);
        if (error != null) {
            ThrowableUtil.wrapAndThrow(error);
        }
    }

    /**
     * Constructor.
     */
    private CurrentPersistenceManager() {
    }
}