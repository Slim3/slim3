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

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class CurrentPersistenceManagerTest extends TestCase {

    @Override
    protected void tearDown() throws Exception {
        CurrentPersistenceManager.set(null);
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testGetAndSet() throws Exception {
        MockPersistenceManager pm = new MockPersistenceManager();
        CurrentPersistenceManager.set(pm);
        assertSame(pm, CurrentPersistenceManager.getAndCheckPresence());
    }

    /**
     * @throws Exception
     */
    public void testDestroy() throws Exception {
        MockPersistenceManager pm = new MockPersistenceManager();
        CurrentPersistenceManager.set(pm);
        CurrentPersistenceManager.destroy();
        assertNull(CurrentPersistenceManager.get());
        CurrentPersistenceManager.destroy();
        assertNull(CurrentPersistenceManager.get());
    }
}