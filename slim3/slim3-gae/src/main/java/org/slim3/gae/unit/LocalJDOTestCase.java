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
package org.slim3.gae.unit;

import javax.jdo.PersistenceManager;

import org.slim3.gae.jdo.PM;
import org.slim3.gae.jdo.PMF;

/**
 * A test case for local JDO.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class LocalJDOTestCase extends LocalDatastoreTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        PM.setCurrent(PMF.getPersistenceManagerFactory()
                .getPersistenceManager());
    }

    @Override
    public void tearDown() throws Exception {
        PersistenceManager pm = PM.getCurrent();
        if (pm != null) {
            PM.setCurrent(null);
            try {
                pm.close();
            } finally {
                super.tearDown();
            }
        } else {
            throw new IllegalStateException(
                    "The persistence manager attached to the current thread is not found.");
        }

    }

}