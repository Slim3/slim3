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

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author higa
 * 
 */
public class ArraySetTest {

    private ArraySet<String> set;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        set = new ArraySet<String>();
        set.add("1");
        set.add("2");
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        set = null;
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        assertThat(set.get(0), is("1"));
        assertThat(set.get(1), is("2"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void iterator() throws Exception {
        Iterator<String> i = set.iterator();
        assertThat(i.next(), is("1"));
        assertThat(i.next(), is("2"));
        assertThat(i.hasNext(), is(false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void size() throws Exception {
        assertThat(set.size(), is(2));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void isEmpty() throws Exception {
        assertThat(set.isEmpty(), is(false));
        set.clear();
        assertThat(set.isEmpty(), is(true));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void contains() throws Exception {
        assertThat(set.contains("1"), is(true));
        assertThat(set.contains("3"), is(false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void add() throws Exception {
        assertThat(set.add("1"), is(false));
        assertThat(set.add("3"), is(true));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
        assertThat(set.remove("1"), is(true));
        assertThat(set.remove("3"), is(false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void clear() throws Exception {
        set.clear();
        assertThat(set.size(), is(0));
    }

    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testClone() throws Exception {
        ArraySet<String> newSet = (ArraySet<String>) set.clone();
        assertThat(newSet, is(set));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void serialize() throws Exception {
        ArraySet<String> newSet = ByteUtil.toObject(ByteUtil.toByteArray(set));
        assertThat(newSet, is(set));
    }
}