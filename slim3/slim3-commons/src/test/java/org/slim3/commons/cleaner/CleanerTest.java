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
package org.slim3.commons.cleaner;

import junit.framework.TestCase;

/**
 * @author koichik
 * @author higa
 * 
 */
public class CleanerTest extends TestCase {

    private int count;

    @Override
    protected void setUp() throws Exception {
        Cleaner.cleanAll();
    }

    @Override
    protected void tearDown() throws Exception {
        Cleaner.cleanAll();
    }

    /**
     * @throws Exception
     */
    public void testForSingle() throws Exception {
        Cleaner.add(new TestCleanable());
        assertEquals(1, Cleaner.cleanables.size());
        Cleaner.cleanAll();
        assertEquals(1, count);
        assertEquals(0, Cleaner.cleanables.size());
    }

    /**
     * @throws Exception
     */
    public void testForMulti() throws Exception {
        Cleaner.add(new TestCleanable());
        Cleaner.add(new TestCleanable());
        assertEquals(2, Cleaner.cleanables.size());
        Cleaner.cleanAll();
        assertEquals(2, count);
        assertEquals(0, Cleaner.cleanables.size());
    }

    /**
     * @throws Exception
     */
    public void testForException() throws Exception {
        Cleaner.add(new TestCleanable());
        Cleaner.add(new TestCleanable2());
        Cleaner.add(new TestCleanable());
        assertEquals(3, Cleaner.cleanables.size());
        Cleaner.cleanAll();
        assertEquals(3, count);
        assertEquals(0, Cleaner.cleanables.size());
    }

    /**
     * 
     */
    public class TestCleanable implements Cleanable {
        public void clean() {
            ++count;
        }
    }

    /**
     * 
     */
    public class TestCleanable2 implements Cleanable {
        public void clean() {
            ++count;
            throw new RuntimeException();
        }
    }
}