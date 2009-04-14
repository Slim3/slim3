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

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import org.slim3.commons.config.Configuration;

/**
 * A helper class for PersistenceManagerFactory.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class PMF {

    /**
     * The key for persistence manager factory name.
     */
    public static final String PERSISTENCE_MANAGER_FACTORY_NAME_KEY = "slim3.persistenceManagerHelper";

    /**
     * The persistence manager factory.
     */
    private static PersistenceManagerFactory persistenceManagerFactory;

    static {
        initialize();
    }

    /**
     * Initializes this class.
     */
    private static void initialize() {
        String name = Configuration.getInstance().getValue(
                PERSISTENCE_MANAGER_FACTORY_NAME_KEY);
        if (name == null) {
            persistenceManagerFactory = JDOHelper
                    .getPersistenceManagerFactory();
        } else {
            persistenceManagerFactory = JDOHelper
                    .getPersistenceManagerFactory(name);
        }
    }

    /**
     * Returns the persistence manager factory.
     * 
     * @return the persistence manager factory
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory() {
        return persistenceManagerFactory;
    }

    /**
     * Constructor.
     */
    private PMF() {
    }
}