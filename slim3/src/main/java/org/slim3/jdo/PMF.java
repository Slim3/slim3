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

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * A helper class for PersistenceManagerFactory.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class PMF {

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
        persistenceManagerFactory = JDOHelper.getPersistenceManagerFactory();
    }

    /**
     * Returns the persistence manager factory.
     * 
     * @return the persistence manager factory
     */
    public static PersistenceManagerFactory get() {
        return persistenceManagerFactory;
    }

    /**
     * Constructor.
     */
    private PMF() {
    }
}