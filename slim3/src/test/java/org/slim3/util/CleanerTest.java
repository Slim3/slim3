/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.slim3.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author higa
 * 
 */
public class CleanerTest {

    private int count;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Cleaner.cleanAll();
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        Cleaner.cleanAll();
    }

    /**
     * @throws Exception
     */
    @Test
    public void forSingle() throws Exception {
        Cleaner.add(new TestCleanable());
        assertThat(Cleaner.cleanables.size(), is(1));
        Cleaner.cleanAll();
        assertThat(count, is(1));
        assertThat(Cleaner.cleanables.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void forMulti() throws Exception {
        Cleaner.add(new TestCleanable());
        Cleaner.add(new TestCleanable());
        assertThat(Cleaner.cleanables.size(), is(2));
        Cleaner.cleanAll();
        assertThat(count, is(2));
        assertThat(Cleaner.cleanables.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void forException() throws Exception {
        Cleaner.add(new TestCleanable());
        Cleaner.add(new TestCleanable2());
        Cleaner.add(new TestCleanable());
        assertThat(Cleaner.cleanables.size(), is(3));
        Cleaner.cleanAll();
        assertThat(count, is(3));
        assertThat(Cleaner.cleanables.size(), is(0));
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